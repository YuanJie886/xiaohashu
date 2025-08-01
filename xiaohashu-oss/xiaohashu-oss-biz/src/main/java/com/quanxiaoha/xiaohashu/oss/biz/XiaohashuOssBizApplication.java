package com.quanxiaoha.xiaohashu.oss.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.quanxiaoha.xiaohashu.user.biz.domain.mapper")
public class XiaohashuOssBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaohashuOssBizApplication.class, args);
    }

}