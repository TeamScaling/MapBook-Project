package com.scaling.libraryservice.caching;

public class Customer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public String hello(){

        return "hello @@@@@@@@@@@@@@@@@@@@@@@@@ "+name;
    }

}
