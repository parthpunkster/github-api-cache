package com.netflix.api.githubapicache.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.api.githubapicache.github.GitHubApi;
import com.netflix.api.githubapicache.service.CustomViewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Enumeration;
import java.util.List;

@RestController
public class GitHubApiController {

    private final CustomViewService customViewService;
    private final GitHubApi gitHubApi;

    @Autowired
    public GitHubApiController(CustomViewService customViewService, GitHubApi gitHubApi) {
        this.customViewService = customViewService;
        this.gitHubApi = gitHubApi;
    }

    @GetMapping("/")
    public JsonNode getGithubApiDocs() {
        return customViewService.getGithubApiDocs();
    }

    @GetMapping("/orgs/Netflix")
    public JsonNode getNetflixOrgHome() {
        return customViewService.getNetflixOrgHome();
    }

    @GetMapping("/orgs/Netflix/members")
    public List<JsonNode> getNetflixOrgMembers() {
        return customViewService.getNetflixOrgMembers();
    }

    @GetMapping("/orgs/Netflix/repos")
    public List<JsonNode> getNetflixRepos() {
        return customViewService.getOrganizationRepos();
    }

    @RequestMapping("/**")
    public ResponseEntity<?> proxyToGitHub(HttpServletRequest request, @RequestBody(required = false) String requestBody) {
        return gitHubApi.proxyToGitHub(request, requestBody);
    }
}
