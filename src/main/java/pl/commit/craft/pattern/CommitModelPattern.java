package pl.commit.craft.pattern;

public final class CommitModelPattern extends BasicModelPattern {
    private static final String GIT_COMMAND = "git commit -m";

    private static final String GIT_COMMAND_COMMITING_WORK_PATTERN = GIT_COMMAND + " \"%s %s(%s): %s\n\n%s\"";
    private static final String GIT_COMMAND_COMMITING_WORK_PATTERN_WITHOUT_COMPONENT = GIT_COMMAND + " \"%s %s: %s\n\n%s\"";
    private static final String GIT_COMMAND_COMMITING_WORK_PATTERN_WITHOUT_DETAILS = GIT_COMMAND + " \"%s %s(%s): %s\"";
    private static final String GIT_COMMAND_COMMITING_WORK_PATTERN_WITHOUT_COMPONENT_AND_DETAILS = GIT_COMMAND + " \"%s %s: %s\"";

    private static final String COMMITING_WORK_PATTERN = "%s %s(%s): %s\n\n%s";
    private static final String COMMITING_WORK_PATTERN_WITHOUT_COMPONENT = "%s %s: %s\n\n%s";
    private static final String COMMITING_WORK_PATTERN_WITHOUT_DETAILS = "%s %s(%s): %s";
    private static final String COMMITING_WORK_PATTERN_WITHOUT_COMPONENT_AND_DETAILS = "%s %s: %s";

    private static String getGitCommandCommittingWorkPattern() {
        return GIT_COMMAND_COMMITING_WORK_PATTERN;
    }

    private static String getGitCommandCommittingWorkPatternWithoutComponent() {
        return GIT_COMMAND_COMMITING_WORK_PATTERN_WITHOUT_COMPONENT;
    }

    private static String getGitCommandCommittingWorkPatternWithoutDetails() {
        return GIT_COMMAND_COMMITING_WORK_PATTERN_WITHOUT_DETAILS;
    }

    private static String getGitCommandCommittingWorkPatternWithoutComponentAndDetails() {
        return GIT_COMMAND_COMMITING_WORK_PATTERN_WITHOUT_COMPONENT_AND_DETAILS;
    }

    private static String getCommittingWorkPattern() {
        return COMMITING_WORK_PATTERN;
    }

    private static String getCommittingWorkPatternWithoutComponent() {
        return COMMITING_WORK_PATTERN_WITHOUT_COMPONENT;
    }

    private static String getCommittingWorkPatternWithoutDetails() {
        return COMMITING_WORK_PATTERN_WITHOUT_DETAILS;
    }

    private static String getCommittingWorkPatternWithoutComponentAndDetails() {
        return COMMITING_WORK_PATTERN_WITHOUT_COMPONENT_AND_DETAILS;
    }

    /**
     * Main method for selecting the appropriate pattern based on the `wholeGitCommand`, `component`, and `details` flags.
     *
     * @param wholeGitCommand - if true, returns a pattern with the full Git commit command.
     * @param component - the name of the component, may be empty.
     * @param details - additional details, may be empty.
     * @return the appropriate pattern.
     */
    public static String getPattern(boolean wholeGitCommand, String component, String details) {
        if (wholeGitCommand) {
            return getPatternWithGitCommand(component, details);
        } else {
            return getPatternWithoutGitCommand(component, details);
        }
    }

    /**
     * Returns the appropriate pattern with the full Git command,
     * depending on whether `component` and `details` are empty or not.
     *
     * @param component - the name of the component.
     * @param details - additional details.
     * @return a pattern with the full Git commit command.
     */
    private static String getPatternWithGitCommand(String component, String details) {
        if (component.isEmpty() && details.isEmpty()) {
            return getGitCommandCommittingWorkPatternWithoutComponentAndDetails();
        } else if (component.isEmpty()) {
            return getGitCommandCommittingWorkPatternWithoutComponent();
        } else if (details.isEmpty()) {
            return getGitCommandCommittingWorkPatternWithoutDetails();
        } else {
            return getGitCommandCommittingWorkPattern();
        }
    }

    /**
     * Returns the appropriate pattern without the full Git command,
     * depending on whether `component` and `details` are empty or not.
     *
     * @param component - the name of the component.
     * @param details - additional details.
     * @return a pattern without the full Git commit command.
     */
    private static String getPatternWithoutGitCommand(String component, String details) {
        if (component.isEmpty() && details.isEmpty()) {
            return getCommittingWorkPatternWithoutComponentAndDetails();
        } else if (component.isEmpty()) {
            return getCommittingWorkPatternWithoutComponent();
        } else if (details.isEmpty()) {
            return getCommittingWorkPatternWithoutDetails();
        } else {
            return getCommittingWorkPattern();
        }
    }
}
