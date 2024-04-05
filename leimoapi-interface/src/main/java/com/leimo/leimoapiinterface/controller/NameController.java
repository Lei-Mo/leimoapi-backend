package com.leimo.leimoapiinterface.controller;

import com.leimo.leimoapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称 API
 */
@CrossOrigin
@RestController
@RequestMapping("/name")
public class NameController {

    // /name/
    @GetMapping("")
    public String getNameByGet(String name, HttpServletRequest request) {

        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
//
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");

        // todo 1.校验权限,这里模拟一下,实际应该查询数据库验证权限。以及校验secretKey
        if (!accessKey.equals("leimo")) {
            throw new RuntimeException("无权限");
        }

        // todo 3.校验一下随机数,实际到后端去存储了,后端存储用hashmap或redis都可以（每一次请求前端发一个随机数，一次请求对应一个随机数，随机数可以根据timestamp 5分钟一清）
        // 校验随机数,模拟一下,直接判断nonce是否大于10000
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }

        // todo 4.校验时间戳与当前时间的差距（例如不能超过5分钟，跟随机数进行配合）
//        if (timestamp) {}
        String result = "POST 用户名字是" + user.getUsername();
        return result;
    }
}
