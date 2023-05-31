package com.netflix.api.githubapicache.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Component
public class GitHubApiImpl implements GitHubApi {
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String organization;

    private final String githubApiToken;

    @Autowired
    public GitHubApiImpl(RestTemplate restTemplate, @Value("${GITHUB_API_TOKEN:}") String githubApiToken, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.baseUrl = "https://api.github.com";
        this.organization = "Netflix";
        this.githubApiToken = githubApiToken;
    }

    @Override
    public List<JsonNode> getOrganizationRepos() throws JsonProcessingException {
        List<JsonNode> repos = new ArrayList<>();

        // Set the pagination parameters
        int page = 1;
        int perPage = 100;

        while (true) {
            // Build the URL with pagination parameters
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .pathSegment("orgs", organization, "repos")
                    .queryParam("page", page)
                    .queryParam("per_page", perPage)
                    .toUriString();

            if (!githubApiToken.isEmpty()) {
                restTemplate.getInterceptors().add((request, body, execution) -> {
                    request.getHeaders().set("Authorization", "Bearer " + githubApiToken);
                    return execution.execute(request, body);
                });
            }

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            // Process the response
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                System.out.println("Response Type: " + response.getBody().getClass().getTypeName());
                List<JsonNode> repoPage = parseJsonArray(responseBody);
                repos.addAll(repoPage);

                // Check if there are more pages
                String linkHeader = response.getHeaders().getFirst("Link");
                if (linkHeader == null || !linkHeader.contains("rel=\"next\"")) {
                    break;
                }
            } else {
                System.out.println("Error: " + response.getStatusCode());
                break;
            }

            // Increment the page number for the next request
            page++;
        }
        return repos;
    }

    @Override
    public List<JsonNode> getGitHubApiDocs() throws JsonProcessingException {

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl).toUriString();

        if (!githubApiToken.isEmpty()) {
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().set("Authorization", "Bearer " + githubApiToken);
                return execution.execute(request, body);
            });
        }

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Error: " + response.getStatusCode());
            return null;
        }

        String responseBody = response.getBody();
        System.out.println("Response Type: " + response.getBody().getClass().getTypeName());
        List<JsonNode> apis = Arrays.asList(objectMapper.readValue(responseBody, new TypeReference<JsonNode>() {}));
        return apis;
    }

    @Override
    public List<JsonNode> getNetflixOrgHome() throws JsonProcessingException {

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment("orgs", organization)
                .toUriString();

        if (!githubApiToken.isEmpty()) {
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().set("Authorization", "Bearer " + githubApiToken);
                return execution.execute(request, body);
            });
        }

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Error: " + response.getStatusCode());
            return null;
        }

        String responseBody = response.getBody();
        System.out.println("Response Type: " + response.getBody().getClass().getTypeName());
        List<JsonNode> home = Arrays.asList(objectMapper.readValue(responseBody, new TypeReference<JsonNode>() {}));
        return home;
    }

    @Override
    public List<JsonNode> getNetflixOrgMembers() throws JsonProcessingException {
        List<JsonNode> members = new ArrayList<>();

        // Set the pagination parameters
        int page = 1;
        int perPage = 100;

        while (true) {
            // Build the URL with pagination parameters
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .pathSegment("orgs", organization, "members")
                    .queryParam("page", page)
                    .queryParam("per_page", perPage)
                    .toUriString();

            if (!githubApiToken.isEmpty()) {
                restTemplate.getInterceptors().add((request, body, execution) -> {
                    request.getHeaders().set("Authorization", "Bearer " + githubApiToken);
                    return execution.execute(request, body);
                });
            }

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            // Process the response
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                System.out.println("Response Type: " + response.getBody().getClass().getTypeName());
                List<JsonNode> membersPage = parseJsonArray(responseBody);
                members.addAll(membersPage);

                // Check if there are more pages
                String linkHeader = response.getHeaders().getFirst("Link");
                if (linkHeader == null || !linkHeader.contains("rel=\"next\"")) {
                    break;
                }
            } else {
                System.out.println("Error: " + response.getStatusCode());
                break;
            }

            // Increment the page number for the next request
            page++;
        }
        return members;
    }

    @Override
    public ResponseEntity<?> proxyToGitHub(HttpServletRequest request, String requestBody) {
        String newPath = request.getRequestURI().substring(request.getContextPath().length());
        String githubUrl = baseUrl + newPath;

        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName, request.getHeader(headerName));
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(githubUrl, httpMethod, httpEntity, String.class);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.putAll(response.getHeaders());

        return new ResponseEntity<>(response.getBody(), responseHeaders, response.getStatusCode());
    }


    private List<JsonNode> parseJsonArray(String responseData) throws JsonProcessingException {
        return objectMapper.readValue(responseData, new TypeReference<List<JsonNode>>() {});
    }
}

