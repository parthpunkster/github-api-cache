package com.netflix.api.githubapicache.controller;

import com.netflix.api.githubapicache.github.RepoDetails;
import com.netflix.api.githubapicache.service.CustomViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/view")
public class CustomViewController {
    private final CustomViewService customViewService;

    @Autowired
    public CustomViewController(CustomViewService customViewService) {
        this.customViewService = customViewService;
    }

    @GetMapping("/bottom/{n}/forks")
    public List<List<String>> getBottomNReposByForks(@PathVariable int n) {
        System.out.println("Get Bottom "+ n + " Repos By Forks");
        return customViewService.getBottomNReposByForks(n);
    }

    @GetMapping("/bottom/{n}/last_updated")
    public List<List<String>> getBottomNReposByLastUpdated(@PathVariable int n) {
        System.out.println("Get Bottom "+ n + " Repos By Last Updated");
        return customViewService.getBottomNReposByLastUpdated(n);
    }

    @GetMapping("/bottom/{n}/open_issues")
    public List<List<String>> getBottomNReposByOpenIssues(@PathVariable int n) {
        System.out.println("Get Bottom "+ n + " Repos By Open Issues");
        return customViewService.getBottomNReposByOpenIssues(n);
    }

    @GetMapping("/bottom/{n}/stars")
    public List<List<String>> getBottomNReposByStars(@PathVariable int n) {
        System.out.println("Get Bottom "+ n + " Repos By Stars");
        return customViewService.getBottomNReposByStars(n);
    }
}

