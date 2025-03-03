package pl.commit.craft.quick;

import org.springframework.stereotype.Service;

@Service
class CommitQuickService {
    private static final String AUDIT_COMMIT = "audit: Audit fix";
    private static final String PR_FIX_COMMIT = "fix: Pull request comments improved";
    private static final String TEST_FIX_COMMIT = "test: Fixed tests";
    private static final String WIP_COMMIT = "wip: Work in progress";

    public String generateQuickCommit(String topicScope, String message, boolean isGitCommand) {
        String commitMessage;

        if (message == null || message.isEmpty()) {
            commitMessage = switch (topicScope) {
                case "audit" -> AUDIT_COMMIT;
                case "fix" -> PR_FIX_COMMIT;
                case "test" -> TEST_FIX_COMMIT;
                case "wip" -> WIP_COMMIT;
                default -> "unknown: Unknown commit type";
            };
        } else {
            commitMessage = topicScope + ": " + message;
        }

        return isGitCommand
                ? String.format("git commit --no-verify -m \"%s\"", commitMessage).trim().toLowerCase()
                : commitMessage.toLowerCase();
    }
}