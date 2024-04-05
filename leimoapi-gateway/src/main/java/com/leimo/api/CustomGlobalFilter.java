package com.leimo.api;

import com.leimo.leimoapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
        // 业务逻辑：
        // 1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getPath());
        log.info("请求方法：" + request.getMethod());
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getRemoteAddress().getHostString();
        log.info("请求来源地址：" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();

        // 2.（黑白名单）
        // if (!IP_WHITE_LIST.contains(sourceAddress)) {
        //     log.info("拦截请求地址: " + sourceAddress);
        //     response.setStatusCode(HttpStatus.FORBIDDEN);
        //     return response.setComplete();
        // }

        // 3.用户鉴权（判断 ak、sk 是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        // todo 1).校验权限,这里模拟一下,实际应该查询数据库验证权限。以及校验secretKey
        if (accessKey != null && !accessKey.equals("leimo")) {
            return handleNoAuto(response);
        }
        String serverSign = SignUtils.getSign(accessKey, "abcdefgh");
        if (!sign.equals(serverSign)) {
            return handleNoAuto(response);
        }

        // todo 3).校验一下随机数,实际到后端去存储了,后端存储用hashmap或redis都可以（每一次请求前端发一个随机数，一次请求对应一个随机数，随机数可以根据timestamp 5分钟一清）
        // 校验随机数,模拟一下,直接判断nonce是否大于10000
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuto(response);
        }

        // 4).校验时间戳与当前时间的差距（例如不能超过5分钟，跟随机数进行配合）
        // 毫秒除以1000获得当前的秒
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuto(response);
        }

        // 4.请求的模拟接口是否存在？
        // todo 从数据库中查询接口是否存在（使用远程调用backend项目来查询，避免了再次写server层等）

        // 5.请求转发，调用模拟接口
        Mono<Void> filter = chain.filter(exchange);
        log.info("响应：" + response.getStatusCode());


        // 6.响应日志
        return handleResponse(exchange, chain, 1l, 1l);

        // // 7.调用成功，接口调用次数 + 1（移到handleResponse中）
        // if (response.getStatusCode() == HttpStatus.OK) {
        //
        // } else {
        //     // 9.调用失败，返回一个规范的错误码
        //     return handleInvokeError(response);
        // }


    }


    /**
     * 处理响应
     * <p>
     * 预期是等模拟接口调用完成，才记录响应日志、统计调用次数。
     * 但现实是 chain.filter 方法立刻返回了，直到 filter 过滤器 return 后才调用了模拟接口。
     * 原因是：chain.filter 是个异步操作，理解为前端的 promise
     * 解决方案：利用 response 装饰者，增强原有 response 的处理能力
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {

                                        try {
                                            // todo 7. 调用成功，接口调用次数 + 1 invokeCount
                                            // innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);// 释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); // data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 无权限直接拦截
     */
    public Mono<Void> handleNoAuto(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}