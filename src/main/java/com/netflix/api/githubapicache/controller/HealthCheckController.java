package com.netflix.api.githubapicache.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheckController {
    @GetMapping
    public HttpStatus healthCheck() {
        System.out.println("Health check");
        return HttpStatus.OK;
    }
}
