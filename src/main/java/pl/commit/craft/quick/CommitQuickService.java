package pl.commit.craft.quick;

import org.springframework.stereotype.Service;

@Service
class CommitQuickService {
    private static final String AUDIT_COMMIT = "audit: Audit fix";
    private static final String PR_FIX_COMMIT = "fix: Pull request comments improved";
    private static final String TEST_FIX_COMMIT = "test: Fixed tests";

    public String generateQuickCommit(String topicScope, boolean isGitCommand) {
        String commitMessage = switch (topicScope) {
            case "audit" -> AUDIT_COMMIT;
            case "fix" -> PR_FIX_COMMIT;
            case "test" -> TEST_FIX_COMMIT;
            default -> "Unknown commit type";
        };

        return isGitCommand
                ? String.format("git commit --no-verify -m \"%s\"", commitMessage).trim()
                : commitMessage;
    }
}