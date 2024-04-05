package com.leimo.leimoapiinterface;

import com.leimo.leimoapiclientsdk.client.LeimoApiClient;
import com.leimo.leimoapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestClient {

    @Test
    public void testClient() {
        LeimoApiClient client = new LeimoApiClient("leimo", "abcdefgh");
        User user = new User("leimo");
        client.getUserNameByPost(user);

        // client.getNameByGet("haha");
        // client.getNameByPost("heihei");
    }

    @Test
    public void demo() {
        System.out.println(System.currentTimeMillis());
    }
}
