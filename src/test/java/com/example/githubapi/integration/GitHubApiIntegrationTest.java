package com.example.githubapi.integration;

import com.example.githubapi.dto.RepositoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubApiIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @DisplayName("Returns repositories with branches when user exists in GitHub")
    void shouldFetchRepositoriesWithBranches_WhenUserIsValid() {

        // given
        String username = "jakundaf";

        // when
        String url = "http://localhost:" + port + "/api/github/" + username + "/repositories";
        ResponseEntity<RepositoryResponse[]> response = restTemplate.getForEntity(url, RepositoryResponse[].class);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        RepositoryResponse[] repositories = response.getBody();

        assertThat(repositories).isNotNull();
        assertThat(repositories.length).isGreaterThan(0);

        for (RepositoryResponse repo : repositories) {
            assertThat(repo.getRepositoryName()).isNotBlank();
            assertThat(repo.getOwnerLogin()).isEqualTo(username);
            assertThat(repo.getBranches()).isNotEmpty();

            repo.getBranches().forEach(branch -> {
                assertThat(branch.getBranchName()).isNotBlank();
                assertThat(branch.getLastCommitSha()).isNotBlank();
            });
        }

    }


}
