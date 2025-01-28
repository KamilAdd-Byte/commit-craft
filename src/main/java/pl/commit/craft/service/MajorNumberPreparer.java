package pl.commit.craft.service;

import lombok.Getter;

/**
 * A utility class that prepares a {@link MajorNumber} by extracting it from a provided issue link.
 * The issue link must contain a valid format where the issue identifier is located after the last '/' character.
 */
@Getter
class MajorNumberPreparer {
    private static final String PATTERN_ISSUE_CHAR = "/";
    private final MajorNumber majorNumber;

    private MajorNumberPreparer(String linkIssue) {
        this.majorNumber = new MajorNumber(extractMajorNumber(linkIssue));
    }

    private String extractMajorNumber(String linkIssue) {
        if (linkIssue == null || !linkIssue.contains(PATTERN_ISSUE_CHAR)) {
            throw new IllegalArgumentException("Invalid issueNumber format");
        }
        return linkIssue.substring(linkIssue.lastIndexOf(PATTERN_ISSUE_CHAR) + 1);
    }

    static MajorNumberPreparer of(String linkIssue) {
        return new MajorNumberPreparer(linkIssue);
    }
}
