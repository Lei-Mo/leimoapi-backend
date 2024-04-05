package com.leimo.leimoapiinterface;

import com.leimo.leimoapiclientsdk.client.LeimoApiClient;
import com.leimo.leimoapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class LeimoapiInterfaceApplicationTests {
    @Resource
    LeimoApiClient leimoApiClient;

    @Test
    void contextLoads() {
        System.out.println(leimoApiClient.getNameByGet("leimo"));
        User user = new User();
        user.setUsername("zhangsan");
        System.out.println(leimoApiClient.getUserNameByPost(user));
    }

}
