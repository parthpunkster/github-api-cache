package com.netflix.api.githubapicache.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.api.githubapicache.github.GitHubApi;
import com.netflix.api.githubapicache.github.RepoDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CacheManager {

    private final GitHubApi gitHubApi;
    private final ObjectMapper objectMapper;

    @Autowired
    public CacheManager(GitHubApi gitHubApi, ObjectMapper objectMapper) {
        this.gitHubApi = gitHubApi;
        this.objectMapper = objectMapper;
    }

    public Map<String, Cache<?>> refreshCache() {
        Map<String, Cache<?>> cacheMap = new HashMap<>();
        System.out.println("Refreshing cache");
        try {
            List<JsonNode> repos = gitHubApi.getOrganizationRepos();
            List<RepoDetails> repoDetails = parseJsonNodeToRepoDetails(repos);

            cacheMap.put("organizationRepos", new Cache<>(repos));
            cacheMap.put("githubApiDocs", new Cache<>(gitHubApi.getGitHubApiDocs()));
            cacheMap.put("githubNetflixHomePage", new Cache<>(gitHubApi.getNetflixOrgHome()));
            cacheMap.put("githubNetflixMembers", new Cache<>(gitHubApi.getNetflixOrgMembers()));
            cacheMap.put("forks", generateCacheByNumberOfForks(repoDetails));
            cacheMap.put("openIssues", generateCacheByNumberOfOpenIssues(repoDetails));
            cacheMap.put("stars", generateCacheByNumberOfStars(repoDetails));
            cacheMap.put("lastUpdated", generateCacheByLastUpdatedTime(repoDetails));
        }
        catch (Exception e) {
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }

        System.out.println("Refreshing cache complete");
        return cacheMap;
    }

    private List<RepoDetails> parseJsonNodeToRepoDetails(List<JsonNode> jsonNodes) {
        List<RepoDetails> repoDetailsList = new ArrayList<>();

        for (JsonNode repo : jsonNodes) {
            RepoDetails repoDetails = objectMapper.convertValue(repo, RepoDetails.class);
            repoDetailsList.add(repoDetails);
        }

        return repoDetailsList;
    }

    private Cache<RepoDetails> generateCacheByNumberOfForks(List<RepoDetails> repos) {
        List<RepoDetails> tmp = new ArrayList<>(repos);
        Collections.sort(tmp, Comparator.comparingInt(RepoDetails::getForks).thenComparing(RepoDetails::getRepoName));
        return new Cache<>(tmp);
    }


    private Cache<RepoDetails> generateCacheByNumberOfOpenIssues(List<RepoDetails> repos) {
        List<RepoDetails> tmp = new ArrayList<>(repos);
        Collections.sort(tmp, Comparator.comparingInt(RepoDetails::getOpenIssues).thenComparing(RepoDetails::getRepoName));
        return new Cache<>(tmp);
    }

    private Cache<RepoDetails> generateCacheByNumberOfStars(List<RepoDetails> repos) {
        List<RepoDetails> tmp = new ArrayList<>(repos);
        Collections.sort(tmp, Comparator.comparingInt(RepoDetails::getStars).thenComparing(RepoDetails::getRepoName));
        return new Cache<>(tmp);
    }

    private Cache<RepoDetails> generateCacheByLastUpdatedTime(List<RepoDetails> repos) {
        List<RepoDetails> tmp = new ArrayList<>(repos);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Collections.sort(tmp, Comparator.comparing((RepoDetails repo) -> LocalDateTime.parse(repo.getLastUpdated(), formatter)).thenComparing(RepoDetails::getRepoName));
        return new Cache<>(tmp);
    }

}

