# GitHub API 

Project exposes a REST API that integrates with the public [GitHub REST API v3](https://docs.github.com/en/rest) and returns a filtered list of repositories with branches for a given user.

---

## ✅ Requirements

- Java 21
- Maven
- Spring Boot 3.5
- No pagination
- No WebFlux
- No DDD/Hexagonal architecture
- One integration test (happy path)

---

## 🚀 How to run

Clone the repository and run:

```
./mvnw spring-boot:run
```

The application will start at:
```
http://localhost:8080
```
---

## 🔗 Endpoint
```
GET /api/github/{username}/repositories
```
Returns all non-fork public repositories for a given GitHub user, including their branches and the latest commit SHA for each branch.

✅ Example: Success response (200 OK)

```
[
  {
    "repositoryName": "my-repo",
    "ownerLogin": "jakundaf",
    "branches": [
      {
        "branchName": "main",
        "lastCommitSha": "a94d8d2e8b8e0c7f89c3ed61c3c3b1f7056e4c49"
      },
      {
        "branchName": "dev",
        "lastCommitSha": "c1eabf28bc4b478c9343fcd8791f23fd2e1e5e9c"
      }
    ]
  }
]
```

❌ Example: Error response (404 Not Found)
If the GitHub user does not exist:

```
{
  "status": 404,
  "message": "GitHub user 'someuser' does not exist."
}
```
---
## 🧪 Integration Test

Test class: GitHubApiIntegrationTest

This test runs against the real GitHub API and verifies:

- the API works correctly for a valid GitHub user

- non-fork repositories are returned

- branches and commit SHAs are included

- the structure complies with the task requirements

Run the test with:
```
./mvnw test
```

You can edit the tested user inside the test class (e.g. jakundaf or your own GitHub username with public repositories).

---
##  🛡️ Technologies
- Java 21
- Spring Boot 3.5
- Spring Web (with RestClient)
- SLF4J (via Lombok's @Slf4j)
- JUnit 5 + Spring Boot Test
- AssertJ for fluent assertions
---

## ℹ️ Notes
- GitHub API integration is handled using RestClient from Spring 6 (recommended over RestTemplate)
- No extra business logic or validation is added
- Error handling is implemented for non-existing users
- Application logs all key steps using SLF4J
- Code is clean, concise and aligned with production standards










































