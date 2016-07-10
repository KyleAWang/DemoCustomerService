package com.demo.customer.vo;

import java.io.Serializable;

/**
 * Created by Kyle
 */
public class Customer implements Serializable{

    private static final long serialVersionUID = 6089959089717740380L;
    private Long id;
    private String name;
    private String address;
    private String tel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
