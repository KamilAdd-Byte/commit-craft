package pl.commit.craft.pattern;

sealed class BasicModelPattern permits CommitModelPattern {
    private static final String TARGET_LANG = "EN";

    protected BasicModelPattern() {
        throw new IllegalStateException("Utility class");
    }

    public static String getTargetLanguage() {
        return TARGET_LANG;
    }
}
