package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.domain.reply.ReplyRequest;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.core.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ReplyControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext context;

    private String accessToken;
    private String accessToken1;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성 및 JWT 토큰 생성
        User user = User.builder()
                .id(1)
                .username("ssar")
                .password("1234")
                .email("ssar@metacoding.com")
                .roles("USER")
                .build();
        accessToken = JWTUtil.create(user);    

        User user2 = User.builder()
                .id(2)
                .username("cos")
                .password("1234")
                .email("cos@metacoding.com")
                .roles("ADMIN")
                .build();
        accessToken1 = JWTUtil.create(user2);
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 정리 작업 (필요시)
    }

    // 댓글 작성 성공
    @Test
    public void save_success_test() throws Exception {
        // given
        ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO();
        saveDTO.setComment("test comment");
        saveDTO.setBoardId(1);

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/api/replies")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())                        
            .andExpect(jsonPath("$.comment").value(saveDTO.getComment()))  
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.username").value("ssar"))
            .andExpect(jsonPath("$.boardId").value(1));
      }

    // 댓글 작성 실패
    @Test
    public void save_fail_test() throws Exception {
        // given
        ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO();
        saveDTO.setComment(""); // 댓글 없음
        saveDTO.setBoardId(1);

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/api/replies")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        // then
        result.andExpect(status().isBadRequest()) 
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.msg", startsWith("comment:")))
            .andExpect(jsonPath("$.body").isEmpty());
      }

    // 댓글 삭제 성공
    @Test
    public void deleteById_success_test() throws Exception {
        // given
        Integer replyId = 4;
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.delete("/api/replies/" + replyId)
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(status().isOk());
    }

    // 댓글 삭제 실패
    @Test
    public void deleteById_fail_test() throws Exception {
        // given
        Integer replyId = 1;
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.delete("/api/replies/" + replyId)
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(status().isForbidden()) 
            .andExpect(jsonPath("$.status").value(403))
            .andExpect(jsonPath("$.body").isEmpty());
    }
}

