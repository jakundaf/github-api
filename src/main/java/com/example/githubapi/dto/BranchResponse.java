package com.example.githubapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchResponse {

    private String branchName;
    private String lastCommitSha;

}
