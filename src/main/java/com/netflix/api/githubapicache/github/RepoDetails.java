package com.netflix.api.githubapicache.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RepoDetails {

    private String repoName;
    private int forks;
    private int openIssues;
    private int stars;
    private String lastUpdated;

    @JsonProperty("forks_count")
    public int getForks() {
        return forks;
    }

    @JsonProperty("open_issues_count")
    public int getOpenIssues() {
        return openIssues;
    }

    @JsonProperty("stargazers_count")
    public int getStars() {
        return stars;
    }

    @JsonProperty("updated_at")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("full_name")
    public String getRepoName() {
        return repoName;
    }

    public String toString() {
        return "RepoDetails [repoName=" + repoName + ", forks=" + forks + ", openIssues=" + openIssues + ", stars=" + stars
                + ", lastUpdated=" + lastUpdated + "]";
    }
}

