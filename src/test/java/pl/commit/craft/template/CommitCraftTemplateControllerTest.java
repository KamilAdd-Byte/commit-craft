package pl.commit.craft.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommitCraftTemplateController.class)
class CommitCraftTemplateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommitTemplateService commitTemplateService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllTemplates() throws Exception {
        CommitCraftTemplate template1 = new CommitCraftTemplate("template1", "Description 1", "format1", null);
        CommitCraftTemplate template2 = new CommitCraftTemplate("template2", "Description 2", "format2", null);
        List<CommitCraftTemplate> mockTemplates = List.of(template1, template2);

        when(commitTemplateService.getAllTemplates()).thenReturn(mockTemplates);

        mockMvc.perform(get("/api/v1/craft-template/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("template1"))
                .andExpect(jsonPath("$[1].name").value("template2"));

        verify(commitTemplateService, times(1)).getAllTemplates();
    }

    @Test
    void testRemoveTemplate() throws Exception {
        String templateName = "templateToRemove";

        mockMvc.perform(delete("/api/v1/craft-template/removed/{name}", templateName))
                .andExpect(status().isOk())
                .andExpect(content().string("Template removed successfully."));

        verify(commitTemplateService, times(1)).removeDedicatedTemplate(templateName);
    }

    @Test
    void testGenerateJson() throws Exception {
        String templateName = "template1";
        CommitCraftJson mockJson = new CommitCraftJson();
        mockJson.addField("name", templateName);
        mockJson.addField("description", "Template Description");
        when(commitTemplateService.prepareJsonByModel(templateName)).thenReturn(mockJson);

        mockMvc.perform(post("/api/v1/craft-template/generate-json/{name}", templateName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(templateName))
                .andExpect(jsonPath("$.description").value("Template Description"));

        verify(commitTemplateService, times(1)).prepareJsonByModel(templateName);
    }
}