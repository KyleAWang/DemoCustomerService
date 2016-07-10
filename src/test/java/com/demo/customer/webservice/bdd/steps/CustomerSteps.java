package com.demo.customer.webservice.bdd.steps;

import com.demo.customer.utils.GeneralTools;
import com.demo.customer.vo.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.local.LocalConduit;
import org.jbehave.core.annotations.*;
import org.junit.Assert;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Kyle
 */
public class CustomerSteps extends Assert {
    private Customer _new = null;
    private Customer _customer2 = null;
    private Response response;

    protected static WebClient getWebClient(){
        WebClient client = WebClient.create(CustomerLifeSteps.getENDPOINT_ADDRESS());
        WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);
        client.type(MediaType.APPLICATION_JSON_TYPE);
        client.accept(MediaType.APPLICATION_JSON_TYPE);
        return client;

    }

    /**
     *customer_new.story
     */
    @Given("A new customer")
    public void createANewCustomer(){
        _new = new Customer();
        _new.setId(1L);
        _new.setName("Kyle");
        _new.setAddress("Three Kings");
        _new.setTel("11112222");
    }

    @When("I want to new a customer")
    public void callCreateApi(){
        WebClient client = getWebClient();
        String info = GeneralTools.javaToJson(_new, Customer.class);

        client.path("/customers");
        response = client.post(info);
    }

    @Then("I get a new customer")
    public void getCorrectCustomer(){
        String str  = response.readEntity(String.class);
        Customer customer1 = GeneralTools.jsonObjToJava(str, Customer.class);
        assertEquals(response.getStatus(), GeneralTools.code_success);
        assertNotNull(customer1);
        assertEquals(customer1.getId(), _new.getId());
        assertEquals(customer1.getAddress(), _new.getAddress());
        assertEquals(customer1.getName(), _new.getName());
        assertEquals(customer1.getTel(), _new.getTel());
    }

    /**
     * customer_get.story
     */
    @Given("there is a customer in system")
    public void aCustomerExists(){
        WebClient client = getWebClient();

        _new = new Customer();
        _new.setId(1L);
        _new.setName("Kyle");
        _new.setAddress("Three Kings");
        _new.setTel("11112222");

        String info = GeneralTools.javaToJson(_new, Customer.class);

        client.path("/customers");
        Response _response = client.post(info);
        assertEquals(_response.getStatus(), GeneralTools.code_success);
    }

    @When("I want to get it by id")
    public void toGetTheCustomer(){
//        client.reset();
        WebClient client = getWebClient();
        client.path("/customers/1");
        response = client.get();
    }

    @Then("I get it")
    public void getTheCustomer(){
        String str = response.readEntity(String.class);
        Customer customer1 = GeneralTools.jsonObjToJava(str, Customer.class);
        assertEquals(response.getStatus(), GeneralTools.code_success);
        assertEquals(customer1.getId(), new Long(1));
    }

    /**
     * customer_update.story
     */
    @Given("a customer in system")
    public void thereIsACustomer(){
        WebClient client = getWebClient();

        _new = new Customer();
        _new.setId(1L);
        _new.setName("Kyle");
        _new.setAddress("Three Kings");
        _new.setTel("11112222");

        String info = GeneralTools.javaToJson(_new, Customer.class);

        client.path("/customers");
        Response _response = client.post(info);
        assertEquals(_response.getStatus(), GeneralTools.code_success);
    }

    @When("I want to change some information")
    public void toChangeInfo(){
//        client.reset();
        WebClient client = getWebClient();

        _customer2 = null;
        _customer2 = new Customer();
        _customer2.setId(1L);
        _customer2.setName("Kyle2");
        _customer2.setAddress("Three Kings2");
        _customer2.setTel("02111112222");

        String info2 = GeneralTools.javaToJson(_customer2, Customer.class);
        client.path("/customers/"+_customer2.getId());
        response= client.put(info2);
    }

    @Then("I will get updated customer")
    public void getChangedCustomer(){
        String str = response.readEntity(String.class);

        Customer customer1 = GeneralTools.jsonObjToJava(str, Customer.class);
        assertNotNull(customer1);
        assertEquals(response.getStatus(), GeneralTools.code_success);
        assertEquals(customer1.getId(), _customer2.getId());
        assertEquals(customer1.getAddress(), _customer2.getAddress());
        assertEquals(customer1.getName(), _customer2.getName());
        assertEquals(customer1.getTel(), _customer2.getTel());
    }

    /**
     * customer_delete.story
     */
    @Given("a customer in system for delete")
    public void aCustomerExists4Delete(){

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");
        String info = GeneralTools.javaToJson(customer, Customer.class);

        WebClient client = getWebClient();
        client.path("/customers");
        Response response = client.post(info);
        assertEquals(response.getStatus(), GeneralTools.code_success);
    }

    @When("I want to delete it")
    public void toDeleteId(){
//        client.reset();
        WebClient client1 = getWebClient();

        client1.path("/customers/1");
        response = client1.delete();
    }

    @Then("I will delete it")
    public void deleteTheCustomer(){
        assertEquals(response.getStatus(), GeneralTools.code_success);
    }


//    @BeforeStories
//    public void initStories(){
//        client = getWebClient();
//    }

    @BeforeStory
    public void initStory(){
        response = null;
        _new = null;
//        client.reset();
    }

    @AfterStory
    public void clearCustomer(){
        WebClient client = getWebClient();
        client.path("/customers/1");
        Response response11 = client.delete();
    }
}
