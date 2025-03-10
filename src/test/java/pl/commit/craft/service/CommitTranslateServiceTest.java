package pl.commit.craft.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.commit.craft.translate.TranslateCommitCraft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommitTranslateServiceTest {

    @Mock
    private TranslateCommitCraft translateCommitCraft;

    @InjectMocks
    private CommitTranslateService commitTranslateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateTranslateCommitValidType() {
        // given
        String major = "link/1.0.0";
        String type = "feat";
        String component = "UI";
        String changeDescription = "Add new button";
        String details = "Added a new button to the main page.";
        boolean wholeGitCommand = true;

        // when
        when(translateCommitCraft.translate(changeDescription, "EN")).thenReturn("Add new button");
        when(translateCommitCraft.translate(details, "EN")).thenReturn("Added a new button to the main page.");

        String commitMessage = commitTranslateService.generateTranslateCommit(major, type, component, changeDescription, details, wholeGitCommand, "EN");

        // then
        assertNotNull(commitMessage);
        assertTrue(commitMessage.contains("feat"));
        assertTrue(commitMessage.contains("UI"));
        assertTrue(commitMessage.contains("add new button"));
        assertTrue(commitMessage.contains("added a new button to the main page."));
    }

    @Test
    void testGenerateTranslateCommitInvalidType() {
        // given
        String type = "invalidType";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commitTranslateService.generateTranslateCommit(null, type, "UI", "Description", "Details", false, "EN");
        });

        // then
        assertEquals("Invalid commit type: invalidType", exception.getMessage());
    }

    @Test
    void testGenerateTranslateCommitEmptyDetails() {
        // given
        String major = "link/1.0.0";
        String type = "fix";
        String component = "Backend";
        String changeDescription = "Fix bug in payment module";
        String details = "";
        boolean wholeGitCommand = true;

        // when
        when(translateCommitCraft.translate(changeDescription, "EN")).thenReturn("Fix bug in payment module");

        String commitMessage = commitTranslateService.generateTranslateCommit(major, type, component, changeDescription, details, wholeGitCommand, "EN");

        // then
        assertNotNull(commitMessage);
        assertFalse(commitMessage.contains("details"));
    }

    @Test
    void testGenerateTranslateCommitWithTaskNumberAndWholeGitCommandIsFalse() {
        // given
        String major = "link/TEET-1234";
        String type = "feat";
        String component = "UI";
        String changeDescription = "Add new feature";
        boolean wholeGitCommand = false;

        // when
        when(translateCommitCraft.translate(changeDescription, "EN")).thenReturn("Add new feature");

        String commitMessage = commitTranslateService.generateTranslateCommit(major, type, component, changeDescription, "", wholeGitCommand, "EN");

        // then
        assertNotNull(commitMessage);
        assertTrue(commitMessage.contains("TEET-1234"));
        assertThat(commitMessage).isEqualTo("TEET-1234 feat(UI): add new feature");
    }

    @Test
    void testGenerateFlowCommitWithTaskNumber() {
        // given
        String major = "link/TEET-1234";
        String type = "fix";
        String component = "Report";
        String changeDescription = "Add new feature";
        String details = "";
        boolean wholeGitCommand = true;

        String commitMessage = commitTranslateService.generateFlowCommit(major, type, component, changeDescription, details, wholeGitCommand);

        // then
        assertNotNull(commitMessage);
        assertTrue(commitMessage.contains("TEET-1234"));
        assertThat(commitMessage).isEqualTo("git commit -m \"TEET-1234 fix(Report): add new feature\"");
    }
}
