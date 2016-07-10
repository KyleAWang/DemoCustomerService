package com.demo.customer.webservice.tdd;

import com.demo.customer.utils.GeneralTools;
import com.demo.customer.vo.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class CustomerServiceTest extends AbstractServiceTest {

    @Test
    public void whenCustomerIdEquals111ThenGetNullCustomer(){
        WebClient client = getWebClient();


        client.path("/customers/111");
        Response response = client.get();
        assertEquals(response.getStatus(), GeneralTools.code_notfound);
        assertEquals(response.readEntity(String.class), GeneralTools.info_no_customer);

    }

    @Test
    public void whenNewACustomerThenShouldBeSuccessful(){
        WebClient client = getWebClient();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");
        String info = GeneralTools.javaToJson(customer, Customer.class);

        client.path("/customers");
        Response response = client.post(info);
        String str  = response.readEntity(String.class);
        Customer customer1 = GeneralTools.jsonObjToJava(str, Customer.class);
        assertEquals(response.getStatus(), GeneralTools.code_success);
        assertNotNull(customer1);
        assertEquals(customer1.getId(), customer.getId());
        assertEquals(customer1.getAddress(), customer.getAddress());
        assertEquals(customer1.getName(), customer.getName());
        assertEquals(customer1.getTel(), customer.getTel());
    }

    @Test
    public void whenNewACustomerWithoutIdThenShouldBeFail(){
        WebClient client = getWebClient();

        Customer customer = new Customer();
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");
        String info = GeneralTools.javaToJson(customer, Customer.class);

        client.path("/customers");
        Response response = client.post(info);
        String str = response.readEntity(String.class);

        assertEquals(response.getStatus(), GeneralTools.code_error);
        assertEquals(response.readEntity(String.class), GeneralTools.info_customer_error);

    }

    @Test
    public void whenNewTwoCustomerWithDuplicateThenShouldBeFail(){
        WebClient client = getWebClient();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");

        String info = GeneralTools.javaToJson(customer, Customer.class);
        client.path("/customers");
        Response response = client.post(info);
        String str = response.readEntity(String.class);
        assertEquals(response.getStatus(), GeneralTools.code_success);


        WebClient client1 = getWebClient();

        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Kyle1");
        customer1.setAddress("Three Kings");
        customer1.setTel("02111112222");

        String info1 = GeneralTools.javaToJson(customer1, Customer.class);
        client1.path("/customers");
        Response response1 = client1.post(info1);
        String str1 = response1.readEntity(String.class);

        Customer customer2 = GeneralTools.jsonObjToJava(str1, Customer.class);

        assertEquals(response1.getStatus(), GeneralTools.code_error);
        assertEquals(response1.readEntity(String.class), GeneralTools.info_duplicate_id);
        assertNull(customer2);

    }


    @Test
    public void whenUpdateCustomerWithAvailableIdThenShouldBeSuccessful(){
        WebClient client = getWebClient();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");
        String info = GeneralTools.javaToJson(customer, Customer.class);

        client.path("/customers");
        Response response = client.post(info);
        assertEquals(response.getStatus(), GeneralTools.code_success);


        WebClient client1 = getWebClient();


        Customer _customer2 = new Customer();
        _customer2.setId(1L);
        _customer2.setName("Kyle2");
        _customer2.setAddress("Three Kings2");
        _customer2.setTel("02111112222");

        String info2 = GeneralTools.javaToJson(_customer2, Customer.class);
        client1.path("/customers/"+_customer2.getId());
        Response response2= client1.put(info2);
        String str = response2.readEntity(String.class);

        Customer customer1 = GeneralTools.jsonObjToJava(str, Customer.class);
        assertNotNull(customer1);
        assertEquals(response.getStatus(), GeneralTools.code_success);
        assertEquals(customer1.getId(), _customer2.getId());
        assertEquals(customer1.getAddress(), _customer2.getAddress());
        assertEquals(customer1.getName(), _customer2.getName());
        assertEquals(customer1.getTel(), _customer2.getTel());
    }

    @Test
    public void whenGetCustomerIdEquals1ThenShouldBeSuccessful(){
        WebClient client = getWebClient();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");
        String info = GeneralTools.javaToJson(customer, Customer.class);

        client.path("/customers");
        Response response = client.post(info);
        assertEquals(response.getStatus(), GeneralTools.code_success);

        client.reset();

        client.path("/customers/1");
        Response response1 = client.get();
        String str = response1.readEntity(String.class);
        Customer customer1 = GeneralTools.jsonObjToJava(str, Customer.class);
        assertEquals(response1.getStatus(), GeneralTools.code_success);
        assertEquals(customer1.getId(), new Long(1));

    }

    @Test
    public void whenRemoveCustomerWithLegalIdThenShouldBeSuccessful(){
        WebClient client = getWebClient();


        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");
        String info = GeneralTools.javaToJson(customer, Customer.class);

        client.path("/customers");
        Response response = client.post(info);
        assertEquals(response.getStatus(), GeneralTools.code_success);

        client.reset();

        client.path("/customers/1");
        Response response1 = client.delete();

        assertEquals(response1.getStatus(), GeneralTools.code_success);

    }

    @Test
    public void whenRemoveCustomerWithIllegalIdThenShouldBeSuccessful(){
        WebClient client = getWebClient();

        client.path("/customers/23");
        Response response = client.delete();
        assertEquals(response.getStatus(), GeneralTools.code_notfound);
    }

    @Test
    public void givenCustomerWasRemovedWhenGetItAgainThenShouldBeFailed(){
        WebClient client = getWebClient();


        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Kyle");
        customer.setAddress("Three Kings");
        customer.setTel("02111112222");
        String info = GeneralTools.javaToJson(customer, Customer.class);

        client.path("/customers");
        Response response = client.post(info);
        assertEquals(response.getStatus(), GeneralTools.code_success);

        client.reset();

        client.path("/customers/1");
        Response response1 = client.delete();
        assertEquals(response1.getStatus(), GeneralTools.code_success);

        client.reset();

        client.path("/customers/1");
        Response response2 = client.get();
        assertEquals(response2.getStatus(), GeneralTools.code_notfound);


    }

    @After
    public void removeCustomer(){

        WebClient client = getWebClient();
        client.reset();
        client.path("/customers/1");
        Response response11 = client.delete();

    }




}