package pl.commit.craft.quick;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommitQuickServiceTest {
    private final CommitQuickService commitQuickService = new CommitQuickService();


    @Test
    void testGenerateQuickCommit_Audit_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("audit", false);
        assertEquals("audit: Audit fix", result);
    }

    @Test
    void testGenerateQuickCommit_Fix_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("fix", false);
        assertEquals("fix: Pull request comments improved", result);
    }

    @Test
    void testGenerateQuickCommit_Test_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("test", false);
        assertEquals("test: Fixed tests", result);
    }

    @Test
    void testGenerateQuickCommit_UnknownTopic_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("unknown", false);
        assertEquals("Unknown commit type", result);
    }

    @Test
    void testGenerateQuickCommit_Audit_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("audit", true);
        assertEquals("git commit --no-verify -m \"audit: Audit fix\"", result);
    }

    @Test
    void testGenerateQuickCommit_Fix_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("fix", true);
        assertEquals("git commit --no-verify -m \"fix: Pull request comments improved\"", result);
    }

    @Test
    void testGenerateQuickCommit_Test_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("test", true);
        assertEquals("git commit --no-verify -m \"test: Fixed tests\"", result);
    }

    @Test
    void testGenerateQuickCommit_UnknownTopic_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("unknown", true);
        assertEquals("git commit --no-verify -m \"Unknown commit type\"", result);
    }
}