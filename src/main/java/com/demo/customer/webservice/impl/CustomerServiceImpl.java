package com.demo.customer.webservice.impl;

import com.demo.customer.utils.CustomersHelper;
import com.demo.customer.utils.GeneralTools;
import com.demo.customer.vo.Customer;
import com.demo.customer.webservice.ICustomerService;
import com.sun.istack.internal.logging.Logger;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;

/**
 * Customer Service Implementation
 * The CustomerServiceImpl implement
 * Created by Kyle
 */
public class CustomerServiceImpl implements ICustomerService {
    private static Logger logger = Logger.getLogger(CustomerServiceImpl.class);


    /**
     * Get a customer by ID.
     * @param id A customer ID.
     * @return This returns a customer detail and response info.
     */
    public Response getCustomer(Long id) {
        Customer customer = CustomersHelper.getCustomers().get(id);
        if (customer != null){
            String str = GeneralTools.javaToJson(customer, Customer.class);
            return  Response.ok(str).build();
        }else {
            return Response.status(GeneralTools.code_notfound).entity(GeneralTools.info_no_customer).build();
        }
    }

    /**
     * Update a customer by ID and new detail.
     * @param id A customer ID.
     * @param info New customer's detail.
     * @return This returns new customer with the same id and response info.
     */
    public Response updateCustomer(Long id, String info) {
        Customer customer = CustomersHelper.getCustomers().get(id);
        if (customer != null && StringUtils.isNotEmpty(info)){
            Customer _newOne = (Customer)GeneralTools.jsonObjToJava(info, Customer.class);
            if (_newOne != null){
                customer.setAddress(_newOne.getAddress());
                customer.setName(_newOne.getName());
                customer.setTel(_newOne.getTel());
                String str = GeneralTools.javaToJson(_newOne, Customer.class);
                return Response.ok(str).build();
            }else {
                return Response.status(GeneralTools.code_error).entity(GeneralTools.info_customer_error).build();
            }
        }else {
            return Response.status(GeneralTools.code_notfound).entity(GeneralTools.info_no_customer).build();
        }
    }

    /**
     * Add a new customer.
     * @param info A new customer info.
     * @return This returns the saved customer and response info.
     */
    public Response addCustomer(String info) {
        if (StringUtils.isNotEmpty(info)){
            Customer _newOne = (Customer)GeneralTools.jsonObjToJava(info, Customer.class);
            if (_newOne != null && _newOne.getId() != null){
                Customer _oldOne = CustomersHelper.getCustomers().get(_newOne.getId());
                if (_oldOne == null){
                    CustomersHelper.getCustomers().put(_newOne.getId(), _newOne);
                    String str = GeneralTools.javaToJson(_newOne, Customer.class);
                    return Response.ok().entity(str).build();
                }else {
                    return Response.status(GeneralTools.code_error).entity(GeneralTools.info_duplicate_id).build();
                }
            }else {
                return Response.status(GeneralTools.code_error).entity(GeneralTools.info_customer_error).build();
            }
        }else {
            return Response.status(GeneralTools.code_error).entity(GeneralTools.info_customer_error).build();
        }
    }

    /**
     * Delete a customer by ID
     * @param id A customer ID.
     * @return This returns response info with true of false result.
     */
    public Response deleteCustomer(Long id) {
        Customer customer = CustomersHelper.getCustomers().get(id);
        if (customer != null){
            CustomersHelper.getCustomers().remove(id);
            return Response.ok().build();
        }else {
            return Response.status(GeneralTools.code_notfound).entity(GeneralTools.info_no_customer).build();
        }
    }
}
