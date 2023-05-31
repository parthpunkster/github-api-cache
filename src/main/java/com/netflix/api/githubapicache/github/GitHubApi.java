package com.netflix.api.githubapicache.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

public interface GitHubApi {
    List<JsonNode> getGitHubApiDocs() throws JsonProcessingException;
    List<JsonNode> getNetflixOrgHome() throws JsonProcessingException;

    List<JsonNode> getNetflixOrgMembers() throws JsonProcessingException;


    List<JsonNode> getOrganizationRepos() throws JsonProcessingException;

    ResponseEntity<?> proxyToGitHub(HttpServletRequest request, String requestBody);

}

