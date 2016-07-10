package com.demo.customer.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Customer Service Interface
 * The CustomerServiceImpl implement simple CRUD of a customer
 * Created by Kyle
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ICustomerService{

    @GET
    @Path("/customers/{id}")
    public Response getCustomer(@PathParam("id") Long id);

    @PUT
    @Path("/customers/{id}")
    public Response updateCustomer(@PathParam("id") Long id, String info);

    @POST
    @Path("/customers")
    public Response addCustomer(String info);

    @DELETE
    @Path("/customers/{id}")
    public Response deleteCustomer(@PathParam("id") Long id);

}