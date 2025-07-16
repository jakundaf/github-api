package com.example.githubapi.controller;

import com.example.githubapi.dto.RepositoryResponse;
import com.example.githubapi.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/{username}/repositories")
    public ResponseEntity<List<RepositoryResponse>> getRepositories(@PathVariable String username) {
        List<RepositoryResponse> repositories = gitHubService.getNonForkRepositories(username);
        return ResponseEntity.ok(repositories);
    }

}
