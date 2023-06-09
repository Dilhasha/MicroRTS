package org.rts.micro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.*;

public class GitHubRepoAnalyzer {

    private static final String OAUTH_TOKEN = "your-github-oauth-token";
//    private static final String REPO_NAME = "owner/repo"; // change to your repo name

    public static Map<String, Set<String>> getTestToServicesMap(String repoName) throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(OAUTH_TOKEN).build();
        GHRepository repo = github.getRepository(repoName);
        // assuming test_svc_mappings.json is in the root of the repo
        GHContent content = repo.getFileContent("test_svc_mappings.json");
        String json = String.valueOf(content.read().readAllBytes());
        // Use Jackson library to parse the JSON string into a Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> map = mapper.readValue(json, Map.class);

        // Convert List to Set for each test
        Map<String, Set<String>> testToServicesMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            testToServicesMap.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        return testToServicesMap;
    }
}
