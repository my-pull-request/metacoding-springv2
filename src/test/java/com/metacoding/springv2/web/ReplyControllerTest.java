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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        // 테스트용 사용자 생성 및 JWT 토큰 생성
        User user = User.builder()
                .id(1)
                .username("ssar")
                .password("1234")
                .email("ssar@nate.com")
                .roles("USER")
                .build();
        accessToken = JWTUtil.create(user);
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 정리 작업 (필요시)
    }

    // 댓글 작성 성공
    @Test
    @WithMockUser(username = "ssar", roles = "USER")
    public void save_success_test() throws Exception {
        // given
        ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO();
        saveDTO.setComment("테스트 댓글입니다.");
        saveDTO.setBoardId(1);

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                post("/api/replies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 댓글 작성 실패 - 인증 없음
    @Test
    public void save_fail_unauthorized_test() throws Exception {
        // given
        ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO();
        saveDTO.setComment("테스트 댓글입니다.");
        saveDTO.setBoardId(1);

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                post("/api/replies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(401);
        assertThat(response).isNotEmpty();
    }

    // 댓글 삭제 성공
    @Test
    @WithMockUser(username = "ssar", roles = "USER")
    public void deleteById_success_test() throws Exception {
        // given
        Integer replyId = 1;

        // when
        ResultActions result = mvc.perform(
                delete("/api/replies/" + replyId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 댓글 삭제 실패 - 권한 없음
    @Test
    @WithMockUser(username = "other", roles = "USER")
    public void deleteById_fail_forbidden_test() throws Exception {
        // given
        Integer replyId = 1;

        // when
        ResultActions result = mvc.perform(
                delete("/api/replies/" + replyId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(403);
        assertThat(response).isNotEmpty();
    }
}

