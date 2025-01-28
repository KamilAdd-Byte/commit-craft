package pl.commit.craft.pattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommitModelPatternTest {
    @Test
    void testGetPatternWithGitCommandWithoutComponentAndDetails() {
        String result = CommitModelPattern.getPattern(true, "", "");
        String expected = "git commit -m \"%s %s: %s\"";
        assertEquals(expected, result);
    }

    @Test
    void testGetPatternWithGitCommandWithoutComponent() {
        String result = CommitModelPattern.getPattern(true, "", "Some details");
        String expected = "git commit -m \"%s %s: %s\n\n%s\"";
        assertEquals(expected, result);
    }

    @Test
    void testGetPatternWithGitCommandWithoutDetails() {

        String result = CommitModelPattern.getPattern(true, "Component", "");
        String expected = "git commit -m \"%s %s(%s): %s\"";
        assertEquals(expected, result);
    }

    @Test
    void testGetPatternWithGitCommand() {
        String result = CommitModelPattern.getPattern(true, "Component", "Some details");
        String expected = "git commit -m \"%s %s(%s): %s\n\n%s\"";
        assertEquals(expected, result);
    }

    @Test
    void testGetPatternWithoutGitCommandWithoutComponentAndDetails() {
        String result = CommitModelPattern.getPattern(false, "", "");
        String expected = "%s %s: %s";
        assertEquals(expected, result);
    }

    @Test
    void testGetPatternWithoutGitCommandWithoutComponent() {
        String result = CommitModelPattern.getPattern(false, "", "Some details");
        String expected = "%s %s: %s\n\n%s";
        assertEquals(expected, result);
    }

    @Test
    void testGetPatternWithoutGitCommandWithoutDetails() {
        String result = CommitModelPattern.getPattern(false, "Component", "");
        String expected = "%s %s(%s): %s";
        assertEquals(expected, result);
    }

    @Test
    void testGetPatternWithoutGitCommand() {
        String result = CommitModelPattern.getPattern(false, "Component", "Some details");
        String expected = "%s %s(%s): %s\n\n%s";
        assertEquals(expected, result);
    }
}
