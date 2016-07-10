package com.demo.customer.webservice.bdd.steps;

import com.demo.customer.webservice.ICustomerService;
import com.demo.customer.webservice.impl.CustomerServiceImpl;
import org.apache.cxf.common.i18n.Exception;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.local.LocalConduit;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeStories;
import org.omg.CORBA.Object;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle
 */
public class CustomerLifeSteps {


    private final static String ENDPOINT_ADDRESS = "http://localhost:8080/api/service1";
    private final static String WADL_ADDRESS = ENDPOINT_ADDRESS + "?_wadl";
    private static Server server;

    protected static String getENDPOINT_ADDRESS(){
        return ENDPOINT_ADDRESS;
    }

    protected static WebClient getWebClient(){
        WebClient client = WebClient.create(getENDPOINT_ADDRESS());
        WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);
        client.type(MediaType.APPLICATION_JSON_TYPE);
        client.accept(MediaType.APPLICATION_JSON_TYPE);
        return client;

    }

    @BeforeStories
    public static void initialize() throws Exception, InterruptedException {
        startServer();
        waitForWADL();
    }

    private static void startServer() throws Exception{
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(ICustomerService.class);

        List<Object> providers = new ArrayList<Object>();
        sf.setProvider(providers);
        sf.setResourceProvider(ICustomerService.class, new SingletonResourceProvider(new CustomerServiceImpl(), true));
        sf.setAddress(ENDPOINT_ADDRESS);


        server = sf.create();
    }

    private static void waitForWADL() throws Exception, InterruptedException {
        WebClient client = WebClient.create(WADL_ADDRESS);
        // wait for 20 secs or so
        for (int i = 0; i < 20; i++) {
            Thread.currentThread().sleep(1000);
            Response response = client.get();
            if (response.getStatus() == 200) {
                break;
            }
        }
        // no WADL is available yet - throw an exception or give tests a chance to run anyway
    }


    @AfterStories
    public static void destroy() throws Exception{
        server.stop();
        server.destroy();
    }

}
