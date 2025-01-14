package pl.commit.craft.quick;

record QuickCommitRequest(
        String topicScope,
        boolean isGitCommand
) {
}
