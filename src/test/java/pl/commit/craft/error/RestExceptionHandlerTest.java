package pl.commit.craft.error;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.commit.craft.flow.CommitFlowController;
import pl.commit.craft.service.CommitTranslateService;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommitFlowController.class)
@Import(RestExceptionHandler.class)
class RestExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommitTranslateService commitTranslateService;

    @Test
    void shouldHandleHttpMessageNotReadableException() throws Exception {
        // given
        String invalidJson = "{ invalid json }";

        // then
        mockMvc.perform(post("/api/v1/commit-flow/craft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Invalid JSON request"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldHandleGeneralException() throws Exception {
        // given
        String requestBody = "{"
                + "\"major\":\"1\","
                + "\"type\":\"bugfix\","
                + "\"component\":\"componentA\","
                + "\"changeDescription\":\"Fixed issue\","
                + "\"details\":\"Detailed description\","
                + "\"wholeGitCommand\":true"
                + "}";

        // when
        when(commitTranslateService.generateFlowCommit(anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // then
        mockMvc.perform(post("/api/v1/commit-flow/craft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}