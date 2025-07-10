package com.example.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloServiceTest {

    @Test
    void testSayHello() {
        HelloService service = new HelloService();
        String result = service.sayHello("DevOps");
        assertEquals("Hello, DevOps!", result);
    }
}
