package com.netflix.api.githubapicache.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.api.githubapicache.cache.Cache;
import com.netflix.api.githubapicache.cache.CacheManager;
import com.netflix.api.githubapicache.cache.CacheService;
import com.netflix.api.githubapicache.github.GitHubApi;
import com.netflix.api.githubapicache.github.RepoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class CustomViewService extends CacheService {

    @Autowired
    public CustomViewService(CacheManager cacheManager) {
        super(cacheManager);
    }

    public JsonNode getGithubApiDocs() {
        List<JsonNode> repos = (List<JsonNode>) getCachedData("githubApiDocs");
        return repos.get(0);
    }

    public JsonNode getNetflixOrgHome() {
        List<JsonNode> repos = (List<JsonNode>) getCachedData("githubNetflixHomePage");
        return repos.get(0);
    }

    public List<JsonNode> getNetflixOrgMembers() {
        List<JsonNode> repos = (List<JsonNode>) getCachedData("githubNetflixMembers");
        return repos;
    }
    public List<JsonNode> getOrganizationRepos() {
        List<JsonNode> repos = (List<JsonNode>) getCachedData("organizationRepos");
        return repos.stream().toList();
    }

    public List<List<String>> getBottomNReposByForks(int n) {
        List<RepoDetails> repos = (List<RepoDetails>) getCachedData("forks");
        return repos.subList(0, Math.min(repos.size(), n))
                .stream()
                .sorted(Comparator.comparing(RepoDetails::getForks).reversed().thenComparing(RepoDetails::getRepoName))
                .map(repo -> List.of(repo.getRepoName(), Integer.toString(repo.getForks())))
                .collect(Collectors.toList());
    }

    public List<List<String>> getBottomNReposByLastUpdated(int n) {
        List<RepoDetails> repos = (List<RepoDetails>) getCachedData("lastUpdated");
        return repos.subList(0, Math.min(repos.size(), n))
                .stream()
                .sorted(Comparator.comparing(RepoDetails::getLastUpdated).reversed().thenComparing(RepoDetails::getRepoName))
                .map(repo -> List.of(repo.getRepoName(), repo.getLastUpdated()))
                .collect(Collectors.toList());
    }

    public List<List<String>> getBottomNReposByOpenIssues(int n) {
        List<RepoDetails> repos = (List<RepoDetails>) getCachedData("openIssues");
        return repos.subList(0, Math.min(repos.size(), n))
                .stream()
                .sorted(Comparator.comparing(RepoDetails::getOpenIssues).reversed().thenComparing(RepoDetails::getRepoName))
                .map(repo -> List.of(repo.getRepoName(), Integer.toString(repo.getOpenIssues())))
                .collect(Collectors.toList());
    }

    public List<List<String>> getBottomNReposByStars(int n) {
        List<RepoDetails> repos = (List<RepoDetails>) getCachedData("stars");
        return repos.subList(0, Math.min(repos.size(), n))
                .stream()
                .sorted(Comparator.comparing(RepoDetails::getStars).reversed().thenComparing(RepoDetails::getRepoName))
                .map(repo -> List.of(repo.getRepoName(), Integer.toString(repo.getStars())))
                .collect(Collectors.toList());
    }

}

