package com.example.githubapi.service;

import com.example.githubapi.client.GitHubClient;
import com.example.githubapi.dto.BranchResponse;
import com.example.githubapi.dto.RepositoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubClient gitHubClient;

    public List<RepositoryResponse> getNonForkRepositories(String username) {
        log.info("Fetching non-fork repositories for user '{}'", username);

        List<GitHubClient.GitHubRepository> repositories = gitHubClient.getUserRepositories(username);
        long forkCount = repositories.stream().filter(GitHubClient.GitHubRepository::isFork).count();

        log.info("Found {} repositories ({} forks) for '{}'", repositories.size(), forkCount, username);


        List<RepositoryResponse> response = repositories.stream()
                .filter(repo -> !repo.isFork())
                .map(repo -> {
                    List<BranchResponse> branches = gitHubClient.getRepositoryBranches(username, repo.getName());
                    return new RepositoryResponse(repo.getName(), repo.getOwner().getLogin(), branches);
                })
                .toList();

        log.info("Returning {} non-fork repositories for '{}'", response.size(), username);
        return response;
    }
}
