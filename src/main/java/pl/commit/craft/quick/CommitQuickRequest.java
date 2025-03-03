package pl.commit.craft.quick;

record CommitQuickRequest(
        String topicScope,
        String message,
        boolean isGitCommand
) {
}
