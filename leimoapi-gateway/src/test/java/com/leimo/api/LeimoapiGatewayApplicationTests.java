package com.leimo.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LeimoapiGatewayApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        long preTime = System.currentTimeMillis();
        System.out.println(preTime);
        Thread.sleep(1000);
        long currTime = System.currentTimeMillis();
        System.out.println(currTime);
        System.out.println(currTime - preTime);
    }


}
