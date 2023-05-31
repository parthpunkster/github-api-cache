package com.netflix.api.githubapicache;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,DataSourceAutoConfiguration.class }, scanBasePackages = "com.netflix.api.githubapicache")
@EnableScheduling
public class GithubApiCacheApplication {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide the port number as a command line argument.");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);

        SpringApplication app = new SpringApplication(GithubApiCacheApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", Integer.toString(port)));
        app.run(args);
    }
}
