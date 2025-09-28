package com.knowledge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.knowledge.mapper")
public class KnowledgeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeManagementApplication.class, args);
    }
}