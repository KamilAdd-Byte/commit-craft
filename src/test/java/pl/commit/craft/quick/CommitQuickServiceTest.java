package pl.commit.craft.quick;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommitQuickServiceTest {
    private final CommitQuickService commitQuickService = new CommitQuickService();


    @Test
    void testGenerateQuickCommit_Audit_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("audit", null, false);
        assertEquals("audit: audit fix", result);
    }

    @Test
    void testGenerateQuickCommit_Fix_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("fix", null,false);
        assertEquals("fix: pull request comments improved", result);
    }

    @Test
    void testGenerateQuickCommit_Test_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("test", null,false);
        assertEquals("test: fixed tests", result);
    }

    @Test
    void testGenerateQuickCommit_UnknownTopic_NoGitCommand() {
        String result = commitQuickService.generateQuickCommit("unknown", "", false);
        assertEquals("unknown: unknown commit type", result);
    }

    @Test
    void testGenerateQuickCommit_Audit_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("audit", "", true);
        assertEquals("git commit --no-verify -m \"audit: audit fix\"", result);
    }

    @Test
    void testGenerateQuickCommit_Fix_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("fix", "", true);
        assertEquals("git commit --no-verify -m \"fix: pull request comments improved\"", result);
    }

    @Test
    void testGenerateQuickCommit_Test_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("test", "", true);
        assertEquals("git commit --no-verify -m \"test: fixed tests\"", result);
    }

    @Test
    void testGenerateQuickCommit_UnknownTopic_WithGitCommand() {
        String result = commitQuickService.generateQuickCommit("unknown", null, true);
        assertEquals("git commit --no-verify -m \"unknown: unknown commit type\"", result);
    }

    @Test
    void testGenerateQuickCommit_WIP_WithGitCommandAndMessage() {
        String result = commitQuickService.generateQuickCommit("audit", "check approach", true);
        assertEquals("git commit --no-verify -m \"audit: check approach\"", result);
    }
}