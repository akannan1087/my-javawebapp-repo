package com.example.service;

class HelloService {
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }

    public void printLength(String str) {
        System.out.println(str.length()); // SonarQube: Possible null pointer dereference
    }

}
