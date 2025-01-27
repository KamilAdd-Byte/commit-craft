package pl.commit.craft.pattern;

sealed class BasicModelPattern permits CommitModelPattern {
    private static final String DEFAULT_TARGET_LANG = "EN";

    protected BasicModelPattern() {
        throw new IllegalStateException("Utility class");
    }

    public static String getTargetLanguage(String language) {
        return language == null ? DEFAULT_TARGET_LANG : language;
    }
}
