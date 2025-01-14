package pl.commit.craft.quick;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuickCommitServiceTest {
    private final QuickCommitService quickCommitService = new QuickCommitService();


    @Test
    void testGenerateQuickCommit_Audit_NoGitCommand() {
        String result = quickCommitService.generateQuickCommit("audit", false);
        assertEquals("audit: Audit fix", result);
    }

    @Test
    void testGenerateQuickCommit_Fix_NoGitCommand() {
        String result = quickCommitService.generateQuickCommit("fix", false);
        assertEquals("fix: Pull request comments improved", result);
    }

    @Test
    void testGenerateQuickCommit_Test_NoGitCommand() {
        String result = quickCommitService.generateQuickCommit("test", false);
        assertEquals("test: Fixed tests", result);
    }

    @Test
    void testGenerateQuickCommit_UnknownTopic_NoGitCommand() {
        String result = quickCommitService.generateQuickCommit("unknown", false);
        assertEquals("Unknown commit type", result);
    }

    @Test
    void testGenerateQuickCommit_Audit_WithGitCommand() {
        String result = quickCommitService.generateQuickCommit("audit", true);
        assertEquals("git commit --no-verify -m \"audit: Audit fix\"", result);
    }

    @Test
    void testGenerateQuickCommit_Fix_WithGitCommand() {
        String result = quickCommitService.generateQuickCommit("fix", true);
        assertEquals("git commit --no-verify -m \"fix: Pull request comments improved\"", result);
    }

    @Test
    void testGenerateQuickCommit_Test_WithGitCommand() {
        String result = quickCommitService.generateQuickCommit("test", true);
        assertEquals("git commit --no-verify -m \"test: Fixed tests\"", result);
    }

    @Test
    void testGenerateQuickCommit_UnknownTopic_WithGitCommand() {
        String result = quickCommitService.generateQuickCommit("unknown", true);
        assertEquals("git commit --no-verify -m \"Unknown commit type\"", result);
    }
}