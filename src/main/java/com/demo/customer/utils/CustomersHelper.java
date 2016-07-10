package com.demo.customer.utils;

import com.demo.customer.vo.Customer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kyle
 */
public class CustomersHelper {
    private static ConcurrentHashMap<Long, Customer> customers;
    private CustomersHelper(){
    }

    public static ConcurrentHashMap<Long, Customer> getCustomers(){
        if (customers == null){
            synchronized (CustomersHelper.class){
                if (customers == null){
                    customers = new ConcurrentHashMap<Long, Customer>();
                }
            }
        }
        return customers;
    }
}
