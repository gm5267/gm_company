package com.gm.ace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 应用启动类
 *
 * @author guoym
 */
@SpringBootApplication
// Mapper 扫描限定在仓储层：各 Repository 已用 mybatis 的 @Mapper 标注，此处仅扩大扫描范围。
// 必须排除 convert 包——其中的 MapStruct @Mapper(componentModel="spring") 接口会被
// @MapperScan 误判为 MyBatis Mapper 而生成第二个同名 bean，与 MapStruct 生成的 @Component 实现冲突。
@MapperScan(basePackages = "com.gm.ace.module",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = "com\\.gm\\.ace\\.module\\.[^.]+\\.convert\\..*"))
public class AceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AceBackendApplication.class, args);
    }
}
