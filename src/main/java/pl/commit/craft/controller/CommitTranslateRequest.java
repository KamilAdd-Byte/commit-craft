package pl.commit.craft.controller;

public record CommitTranslateRequest(
        String major,
        String type,
        String component,
        String changeDescription,
        String details,
        boolean wholeGitCommand
) {}
