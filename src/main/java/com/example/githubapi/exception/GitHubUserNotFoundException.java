package com.example.githubapi.exception;

public class GitHubUserNotFoundException extends RuntimeException {
    public GitHubUserNotFoundException(String username) {
        super("Github user '" + username + "' does not exist.");
    }
}
