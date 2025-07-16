package com.example.githubapi.client;

import com.example.githubapi.dto.BranchResponse;
import com.example.githubapi.exception.GitHubUserNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class GitHubClient {


    private final RestClient restClient = RestClient.create("https://api.github.com");

    public List<GitHubRepository> getUserRepositories(String username) {
        log.info("Requesting repositories for user '{}'", username);

        try {
            GitHubRepository[] repos = restClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .body(GitHubRepository[].class);

            if (repos == null) {
                log.warn("Received null when fetching repositories for user '{}'", username);
                return List.of();
            }

            log.info("Fetched {} repositories for user '{}'", repos.length, username);
            return List.of(repos);

        } catch (RestClientResponseException ex) {
            log.error("GitHub API error while fetching repositories for '{}': {} {}", username, ex.getStatusCode(), ex.getMessage());
            if (ex.getStatusCode().value() == 404) {
                throw new GitHubUserNotFoundException(username);
            }
            throw ex;
        }
    }

    public List<BranchResponse> getRepositoryBranches(String username, String repositoryName) {
        log.info("Fetching branches for repo '{}/{}'", username, repositoryName);

        GitHubBranch[] branches = restClient.get()
                .uri("/repos/{username}/{repo}/branches", username, repositoryName)
                .retrieve()
                .body(GitHubBranch[].class);

        if (branches == null) {
            log.warn("Received null when fetching branches for user '{}' in repository '{}'", username, repositoryName);
            return List.of();
        }

        log.info("Fetched {} branches for user '{}' in repository '{}'", branches.length, username, repositoryName);
        return Stream.of(branches)
                .map(branch -> new BranchResponse(branch.getName(), branch.getCommit().getSha()))
                .toList();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GitHubRepository {
        private String name;
        private boolean fork;
        private Owner owner;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Owner {
            private String login;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GitHubBranch {
        private String name;
        private Commit commit;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Commit {
            private String sha;
        }

    }


}
