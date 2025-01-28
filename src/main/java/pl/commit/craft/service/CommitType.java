package pl.commit.craft.service;

import lombok.Getter;

@Getter
enum CommitType {
    FEAT("feat"),
    FIX("fix"),
    DOCS("docs"),
    STYLE("style"),
    REFACTOR("refactor"),
    TEST("test"),
    CHORE("chore"),
    AUDIT("audit");

    private final String type;

    CommitType(String type) {
        this.type = type;
    }

    public static boolean isValidType(String type) {
        for (CommitType commitType : CommitType.values()) {
            if (commitType.getType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
