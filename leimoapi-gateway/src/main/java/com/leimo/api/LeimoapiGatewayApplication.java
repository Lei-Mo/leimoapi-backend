package com.leimo.api;

import com.leimo.api.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDubbo
public class LeimoapiGatewayApplication {

    @DubboReference
    private DemoService demoService;

    // public static void main(String[] args) {
    //     SpringApplication.run(LeimoapiGatewayApplication.class, args);
    // }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(LeimoapiGatewayApplication.class, args);
        LeimoapiGatewayApplication application = context.getBean(LeimoapiGatewayApplication.class);
        String result = application.doSayHello("world");
        String result2 = application.doSayHello2("world");
        System.out.println("result: " + result);
        System.out.println("result: " + result2);
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

    public String doSayHello2(String name) {
        return demoService.sayHello2(name);
    }


    // @Bean
    // public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //     return builder.routes()
    //             .route("tobaidu", r -> r.path("/baidu")
    //                     .uri("http://www.baidu.com"))
    //             .route("toleimo", r -> r.path("/leimo")
    //                     .uri("http://49.235.132.25/"))
    //             .build();
    // }


}



