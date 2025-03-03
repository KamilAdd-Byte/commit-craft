package pl.commit.craft.template.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommitTemplateGenerateServiceTest {
    private static final String TEMPLATES_META_SCHEMA_JSON = "src/main/resources/templates/meta-schema.json";

    @InjectMocks
    private CommitTemplateGenerateService service;

    @Mock
    private ObjectMapper objectMapper;

    private static final String TEMPLATE_NAME = "testTemplate";
    private static final String VALID_PATTERN = "Commit: {field1}, {field2}";
    private static final JsonNode VALID_COMMIT_DATA = JsonNodeFactory.instance.objectNode()
            .put("field1", "value1")
            .put("field2", "value2");


    @Test
    void shouldGenerateCommitWhenTemplateAndDataAreValid() throws IOException {
        JsonNode templateNode = JsonNodeFactory.instance.objectNode()
                .put("name", TEMPLATE_NAME)
                .put("pattern", VALID_PATTERN)
                .set("model", JsonNodeFactory.instance.objectNode());

        JsonNode templatesNode = JsonNodeFactory.instance.arrayNode().add(templateNode);

        JsonNode rootNode = JsonNodeFactory.instance.objectNode()
                .set("templates", templatesNode);

        // Mock ObjectMapper behavior
        Mockito.when(objectMapper.readTree(new File(TEMPLATES_META_SCHEMA_JSON)))
                .thenReturn(rootNode);

        String result = service.generateCommit(TEMPLATE_NAME, VALID_COMMIT_DATA);

        assertEquals("Commit: value1, value2", result);
    }

    @Test
    void shouldThrowExceptionWhenTemplateNotFound() throws IOException {
        // Mock empty templates array
        JsonNode rootNode = JsonNodeFactory.instance.objectNode()
                .set("templates", JsonNodeFactory.instance.arrayNode());

        // Mock ObjectMapper behavior
        Mockito.when(objectMapper.readTree(new File(TEMPLATES_META_SCHEMA_JSON)))
                .thenReturn(rootNode);

        // Execute method and expect exception
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateCommit(TEMPLATE_NAME, VALID_COMMIT_DATA));

        assertEquals("Template with name " + TEMPLATE_NAME + " not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldsAreMissing() throws IOException {
        JsonNode modelNode = JsonNodeFactory.instance.objectNode()
                .put("field1", true)
                .put("field2", true);

        JsonNode templateNode = JsonNodeFactory.instance.objectNode()
                .put("name", TEMPLATE_NAME)
                .put("pattern", VALID_PATTERN)
                .set("model", modelNode);

        JsonNode templatesNode = JsonNodeFactory.instance.arrayNode().add(templateNode);

        JsonNode rootNode = JsonNodeFactory.instance.objectNode()
                .set("templates", templatesNode);

        // Mock ObjectMapper behavior
        Mockito.when(objectMapper.readTree(new File(TEMPLATES_META_SCHEMA_JSON)))
                .thenReturn(rootNode);

        JsonNode incompleteCommitData = JsonNodeFactory.instance.objectNode().put("field1", "value1");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateCommit(TEMPLATE_NAME, incompleteCommitData));

        assertEquals("Missing required fields: field2", exception.getMessage());
    }

    @Test
    void shouldGenerateDedicatedCommitWhenTemplateAndDataAreValid() throws IOException {
        // Mock JSON template structure
        JsonNode templateNode = JsonNodeFactory.instance.objectNode()
                .put("name", "test-project")
                .put("description", "Dedykowany dla projektu coś tam")
                .put("pattern", "{ticket_id} {type}({scope}): {message}")
                .set("model", JsonNodeFactory.instance.objectNode()
                        .put("ticket_id", "Numer powiązanego zadania w JIRA")
                        .set("type", JsonNodeFactory.instance.arrayNode()
                                .add("feat")
                                .add("fix")
                                .add("junk")
                                .add("chore")
                                .add("test"))
                );

        JsonNode templatesNode = JsonNodeFactory.instance.objectNode()
                .set("dedicated", JsonNodeFactory.instance.arrayNode().add(templateNode));

        // Mock ObjectMapper behavior using the same path as in production code
        when(objectMapper.readTree(new File("src/main/resources/templates/dedicated-meta-schema.json")))
                .thenReturn(templatesNode);

        JsonNode validCommitData = JsonNodeFactory.instance.objectNode()
                .put("ticket_id", "JIRA-123")
                .put("type", "feat")
                .put("scope", "auth-module")
                .put("message", "Dodano obsługę OAuth2");

        // Execute method
        String result = service.generateDedicatedCommit("test-project", validCommitData);

        // Verify result
        assertEquals("JIRA-123 feat(auth-module): Dodano obsługę OAuth2", result);
    }
}