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

@ExtendWith(MockitoExtension.class)
class CommitTemplateGenerateServiceTest {
    public static final String TEMPLATES_META_SCHEMA_JSON = "src/main/resources/templates/meta-schema.json";

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
        // Mock JSON template structure
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

        // Execute method
        String result = service.generateCommit(TEMPLATE_NAME, VALID_COMMIT_DATA);

        // Verify result
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
        // Mock JSON template structure with required fields
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

        // Execute method and expect exception
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateCommit(TEMPLATE_NAME, incompleteCommitData));

        assertEquals("Missing required fields: [field2]", exception.getMessage());
    }


}