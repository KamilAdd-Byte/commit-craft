package pl.commit.craft.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.ConstraintViolation;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitTemplateServiceTest {
    private CommitTemplateService commitTemplateService;
    private Validator validator;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        commitTemplateService = new CommitTemplateService(objectMapper);
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator( new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void commitCraftTemplate_ValidationShouldPass() {
        Map<String, Object> model = new HashMap<>();
        model.put("type", "feat");
        model.put("scope", Arrays.asList("auth", "api"));

        CommitCraftTemplate template = new CommitCraftTemplate(
                "conventional",
                "Conventional commit template",
                "type(scope): description",
                model
        );

        Set<ConstraintViolation<CommitCraftTemplate>> violations = validator.validate(template);

        assertTrue(violations.isEmpty());
    }

    @Test
    void getAllTemplates_ShouldCombineTemplatesFromBothFiles() throws IOException {
        // given
        ObjectNode rootNode = mock(ObjectNode.class);
        ObjectNode rootDedicatedNode = mock(ObjectNode.class);
        JsonNode templatesNode = mock(JsonNode.class);
        JsonNode dedicatedNode = mock(JsonNode.class);

        CommitCraftTemplate template1 = new CommitCraftTemplate(
                "template1",
                "Regular template",
                "feat: description",
                Map.of("type", "feat")
        );

        CommitCraftTemplate template2 = new CommitCraftTemplate(
                "template2",
                "Dedicated template",
                "fix: description",
                Map.of("type", "fix")
        );

        // when
        when(objectMapper.readTree(any(File.class)))
                .thenReturn(rootNode)
                .thenReturn(rootDedicatedNode);

        when(rootNode.path("templates")).thenReturn(templatesNode);
        when(rootDedicatedNode.path("dedicated")).thenReturn(dedicatedNode);

        when(templatesNode.toString()).thenReturn("[{\"regular\":\"template\"}]");
        when(dedicatedNode.toString()).thenReturn("[{\"dedicated\":\"template\"}]");

        when(objectMapper.readValue(eq(templatesNode.toString()), any(TypeReference.class)))
                .thenReturn(List.of(template1));
        when(objectMapper.readValue(eq(dedicatedNode.toString()), any(TypeReference.class)))
                .thenReturn(List.of(template2));

        List<CommitCraftTemplate> result = commitTemplateService.getAllTemplates();

        assertEquals(2, result.size());
        assertEquals("template1", result.get(0).getName());
        assertEquals("template2", result.get(1).getName());

        // Verify file reads happened exactly once for each file
        verify(objectMapper, times(2)).readTree(any(File.class));
        verify(objectMapper, times(2)).readValue(anyString(), any(TypeReference.class));

        // Verify correct paths were accessed
        verify(rootNode).path("templates");
        verify(rootDedicatedNode).path("dedicated");
    }

    @Test
    void getAllTemplates_ShouldHandleEmptyFiles() throws IOException {
        // given
        ObjectNode rootNode = mock(ObjectNode.class);
        ObjectNode rootDedicatedNode = mock(ObjectNode.class);
        JsonNode templatesNode = mock(JsonNode.class);
        JsonNode dedicatedNode = mock(JsonNode.class);

        // Configure mock behavior
        when(objectMapper.readTree(any(File.class)))
                .thenReturn(rootNode)
                .thenReturn(rootDedicatedNode);

        when(rootNode.path("templates")).thenReturn(templatesNode);
        when(rootDedicatedNode.path("dedicated")).thenReturn(dedicatedNode);

        when(templatesNode.toString()).thenReturn("[]");
        when(dedicatedNode.toString()).thenReturn("[]");

        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        // when
        List<CommitCraftTemplate> result = commitTemplateService.getAllTemplates();

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllTemplates_ShouldHandleIOException() throws IOException {
        // given
        when(objectMapper.readTree(any(File.class)))
                .thenThrow(new IOException("File not found"));

        // when/then
        assertThrows(IOException.class, () -> commitTemplateService.getAllTemplates());
    }

    @Test
    void getCommitCraftJson_ShouldCreateValidCommitCraftJson() throws IOException {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("type", "feat");
        model.put("scope", Arrays.asList("auth", "api"));

        CommitCraftTemplate selectedTemplate = new CommitCraftTemplate(
                "conventional",
                "Conventional commit template",
                "type(scope): description",
                model
        );
        String selectedTemplateName = "conventional";

        // when
        CommitCraftJson result = commitTemplateService.getCommitCraftJson(selectedTemplate, selectedTemplateName);

        // then
        assertNotNull(result);
        assertEquals(selectedTemplateName, result.getFields().get("name"));
    }

    @Test
    void prepareJsonByModel_ShouldReturnValidCommitCraftJson() throws IOException {
        // given
        ObjectNode rootNode = mock(ObjectNode.class);
        ObjectNode rootDedicatedNode = mock(ObjectNode.class);
        JsonNode templatesNode = mock(JsonNode.class);
        JsonNode dedicatedNode = mock(JsonNode.class);

        CommitCraftTemplate template1 = new CommitCraftTemplate(
                "template1",
                "Regular template",
                "feat: description",
                Map.of("type", "feat")
        );

        CommitCraftTemplate template2 = new CommitCraftTemplate(
                "template2",
                "Dedicated template",
                "fix: description",
                Map.of("type", "fix", "scope", Arrays.asList("auth", "api"))
        );

        // Configure mock behavior
        when(objectMapper.readTree(any(File.class)))
                .thenReturn(rootNode)
                .thenReturn(rootDedicatedNode);

        when(rootNode.path("templates")).thenReturn(templatesNode);
        when(rootDedicatedNode.path("dedicated")).thenReturn(dedicatedNode);

        when(templatesNode.toString()).thenReturn("[{\"name\":\"template1\",\"description\":\"Regular template\",\"commitMessage\":\"feat: description\",\"model\":{\"type\":\"feat\"}}]");
        when(dedicatedNode.toString()).thenReturn("[{\"name\":\"template2\",\"description\":\"Dedicated template\",\"commitMessage\":\"fix: description\",\"model\":{\"type\":\"fix\",\"scope\":[\"auth\",\"api\"]}}]");

        when(objectMapper.readValue(eq(templatesNode.toString()), any(TypeReference.class)))
                .thenReturn(List.of(template1));
        when(objectMapper.readValue(eq(dedicatedNode.toString()), any(TypeReference.class)))
                .thenReturn(List.of(template2));

        // when
        CommitCraftJson result = commitTemplateService.prepareJsonByModel("template2");

        // then
        assertNotNull(result, "The result should not be null");

        // Verify interactions
        verify(objectMapper, times(2)).readTree(any(File.class));
        verify(objectMapper, times(2)).readValue(anyString(), any(TypeReference.class));
        verify(rootNode).path("templates");
        verify(rootDedicatedNode).path("dedicated");
    }
}