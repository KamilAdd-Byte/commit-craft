package pl.commit.craft.template;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommitDedicatedTemplateValidatorTest {
    private static final Logger log = LoggerFactory.getLogger(CommitDedicatedTemplateValidator.class);

    @InjectMocks
    private CommitDedicatedTemplateValidator validator;

    private ListAppender<ILoggingEvent> listAppender;


    @BeforeEach
    void setUp() {
        listAppender = new ListAppender<>();
        listAppender.start();

        ((ch.qos.logback.classic.Logger) log).addAppender(listAppender);
    }

    @Test
    void testValidatePatternAndModelScope_success() {
        Map<String, Object> model = Map.of(
                "type", "feat",
                "scope", "core",
                "message", "message"
        );

        CommitCraftTemplate validTemplate = new CommitCraftTemplate("feat-{scope}", "Standard commit", "feat-{scope}", model);

        boolean result = CommitDedicatedTemplateValidator.validatePatternAndModelScope(validTemplate);

        assertFalse(result);
        assertFalse(listAppender.list.stream().anyMatch(event -> event.getMessage().contains("Pattern matches the model keys.")));
    }

    @Test
    void testValidatePatternAndModelScope_missingModelKey() {
        Map<String, Object> model = Map.of(
                "type", "feat",
                "scope", "core"
        );

        CommitCraftTemplate invalidTemplate = new CommitCraftTemplate("feat-{scope}-{extraKey}", "Invalid commit", "feat-{scope}-{extraKey}", model);

        boolean result = CommitDedicatedTemplateValidator.validatePatternAndModelScope(invalidTemplate);

        assertFalse(result);
        assertFalse(listAppender.list.stream().anyMatch(event -> event.getMessage().contains("Pattern contains an extra key not in the model: extraKey")));
    }

    @Test
    void testValidatePatternAndModelScope_extraModelKey() {
        Map<String, Object> model = Map.of(
                "type", "feat",
                "scope", "core",
                "message", "extra"
        );

        CommitCraftTemplate invalidTemplate = new CommitCraftTemplate("feat-{scope}", "Invalid commit", "feat-{scope}", model);

        boolean result = CommitDedicatedTemplateValidator.validatePatternAndModelScope(invalidTemplate);

        assertFalse(result);
        assertFalse(listAppender.list.stream().anyMatch(event -> event.getMessage().contains("Pattern is missing key: message")));
    }
}