package pl.commit.craft.quick;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommitQuickController.class)
class CommitQuickControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommitQuickService commitQuickService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGenerateCommitSuccess() throws Exception {
        String topicScope = "fix";
        boolean isGitCommand = true;

        CommitQuickRequest commitQuickRequest = new CommitQuickRequest(topicScope, isGitCommand);
        String expectedCommitMessage = "git commit -m \"fix: fixed a bug\"";

        when(commitQuickService.generateQuickCommit(topicScope, isGitCommand)).thenReturn(expectedCommitMessage);

        mockMvc.perform(post("/api/v1/commit-quick/craft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commitQuickRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCommitMessage));

        verify(commitQuickService, times(1)).generateQuickCommit(topicScope, isGitCommand);
    }

    @Test
    void testGenerateCommitFailure() throws Exception {
        String topicScope = "invalidTopic";
        boolean isGitCommand = false;

        CommitQuickRequest commitQuickRequest = new CommitQuickRequest(topicScope, isGitCommand);

        when(commitQuickService.generateQuickCommit(topicScope, isGitCommand)).thenReturn("Invalid commit");

        mockMvc.perform(post("/api/v1/commit-quick/craft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commitQuickRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid commit"));

        verify(commitQuickService, times(1)).generateQuickCommit(topicScope, isGitCommand);
    }
}