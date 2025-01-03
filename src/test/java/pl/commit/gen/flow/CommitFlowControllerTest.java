package pl.commit.gen.flow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.commit.gen.service.CommitService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class CommitFlowControllerTest {

    @Mock
    private CommitService commitService;

    @InjectMocks
    private CommitFlowController commitFlowController;

    private MockMvc mockMvc;

    @Test
    void testGenerateCommitSuccess() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(commitFlowController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

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
        when(commitService.generateFlowCommit("1", "bugfix", "componentA", "Fixed issue", "Detailed description", true))
                .thenReturn("git commit -m \"1 bugfix(componentA): Fixed issue\n\nDetailed description\"");

       //then
        mockMvc.perform(post("/api/commit-flow/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())  // Oczekiwany status HTTP 200
                .andExpect(content().string("git commit -m \"1 bugfix(componentA): Fixed issue\n\nDetailed description\""));  // Oczekiwana odpowiedź
    }
}
