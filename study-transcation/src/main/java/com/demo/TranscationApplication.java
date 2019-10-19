package com.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * User: lanxinghua
 * Date: 2019/10/19 13:52
 * Desc:
 */
@SpringBootApplication(scanBasePackages = {"com.demo"})
@MapperScan("com.demo.mapper")
public class TranscationApplication {
    public static void main(String[] args) {
        SpringApplication.run(TranscationApplication.class);
    }
}
