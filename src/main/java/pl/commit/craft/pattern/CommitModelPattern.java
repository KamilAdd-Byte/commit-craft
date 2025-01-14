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
     * Główna metoda wybierająca odpowiedni wzorzec na podstawie flagi `wholeGitCommand`, `component` i `details`.
     * @param wholeGitCommand - jeśli true, zwraca wzorzec z pełnym git commit.
     * @param component - nazwa komponentu, może być pusta.
     * @param details - szczegóły, mogą być puste.
     * @return odpowiedni wzorzec.
     */
    public static String getPattern(boolean wholeGitCommand, String component, String details) {
        if (wholeGitCommand) {
            return getPatternWithGitCommand(component, details);
        } else {
            return getPatternWithoutGitCommand(component, details);
        }
    }

    /**
     * Zwraca odpowiedni wzorzec z pełnym poleceniem Git, w zależności od tego, czy `component` i `details` są puste.
     * @param component - nazwa komponentu.
     * @param details - szczegóły.
     * @return wzorzec z pełnym poleceniem Git.
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
     * Zwraca odpowiedni wzorzec bez pełnego polecenia Git, w zależności od tego, czy `component` i `details` są puste.
     * @param component - nazwa komponentu.
     * @param details - szczegóły.
     * @return wzorzec bez pełnego polecenia Git.
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
