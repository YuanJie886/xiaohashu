package com.quanxiaoha.xiaohashu.auth;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NacosTests {

    @Setter
    @NacosValue(value = "${alarm.type}" , autoRefreshed = true)
    private String alarmType;

    @Value("${rate-limit.api.limit}")
    private Integer limit;


    @Test
    void send() {
        System.out.println(alarmType);
        System.out.println(limit);
    }

}
