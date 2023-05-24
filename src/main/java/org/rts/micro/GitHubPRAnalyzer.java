package org.rts.micro;

import org.kohsuke.github.*;

import java.io.IOException;
import java.util.*;

public class GitHubPRAnalyzer {

    private static final String OAUTH_TOKEN = "your-github-oauth-token";
    //private static final String REPO_NAME = "owner/repo"; // change to your repo name
    //private static final int PR_NUMBER = 1; // change to your PR number

    public static Set<String> affectedServices(String repoName, int prNumber) throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(OAUTH_TOKEN).build();
        GHRepository repo = github.getRepository(repoName);
        GHPullRequest pr = repo.getPullRequest(prNumber);

        Map<String, String> svcPathMappings = getServicePathMappings(repo);

        Set<String> affectedServices = new HashSet<>();
        for (GHPullRequestFileDetail fileDetail : pr.listFiles()) {
            String fileName = fileDetail.getFilename();
            for (Map.Entry<String, String> entry : svcPathMappings.entrySet()) {
                if (fileName.startsWith(entry.getValue())) {
                    affectedServices.add(entry.getKey());
                    break;
                }
            }
        }
        System.out.println("Affected services: " + affectedServices);
        return affectedServices;
    }

    private static Map<String, String> getServicePathMappings(GHRepository repo) throws IOException {
        // assuming svc_path_mappings.json is in the root of the repo
        GHContent content = repo.getFileContent("svc_path_mappings.json");
        String json = String.valueOf(content.read().readAllBytes());

        // Use a JSON library like Jackson or Gson to parse the JSON string into a Map
        // This is a placeholder and won't compile
        Map<String, String> svcPathMappings = parseJson(json);

        return svcPathMappings;
    }

    private static Map<String, String> parseJson(String json) {
        // Use a JSON library like Jackson or Gson to parse the JSON string into a Map
        // This is a placeholder and won't compile
        return new HashMap<>();
    }
}
