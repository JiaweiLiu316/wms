package com.swiftsprinttech.wms01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiawe
 */
@SpringBootApplication // 扫描 Mapper 接口所在包
@MapperScan("com.swiftsprinttech.wms01.mappers")
public class Wms01Application {

    public static void main(String[] args) {
        SpringApplication.run(Wms01Application.class, args);
    }

}
