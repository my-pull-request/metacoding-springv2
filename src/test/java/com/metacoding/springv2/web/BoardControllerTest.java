package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.domain.board.BoardRequest;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.core.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BoardControllerTest {

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

    // 게시글 목록 조회 성공
    @Test
    public void findAll_success_test() throws Exception {
        // when
        ResultActions result = mvc.perform(
                get("/api/boards")
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 게시글 목록 조회 실패 - 인증 필요
    @Test
    public void findAll_fail_unauthorized_test() throws Exception {
        // when
        ResultActions result = mvc.perform(
                get("/api/boards")
                        .header("Authorization", "Bearer invalid_token")
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(401);
        assertThat(response).isNotEmpty();
    }

    // 게시글 작성 성공
    @Test
    @WithMockUser(username = "ssar", roles = "USER")
    public void save_success_test() throws Exception {
        // given
        BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO();
        saveDTO.setTitle("테스트 게시글");
        saveDTO.setContent("테스트 내용입니다.");

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 게시글 작성 실패 - 인증 없음
    @Test
    public void save_fail_unauthorized_test() throws Exception {
        // given
        BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO();
        saveDTO.setTitle("테스트 게시글");
        saveDTO.setContent("테스트 내용입니다.");

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(401);
        assertThat(response).isNotEmpty();
    }

    // 게시글 상세 조회 성공
    @Test
    @WithMockUser(username = "ssar", roles = "USER")
    public void findById_success_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                get("/api/boards/" + boardId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 게시글 상세 조회 실패 - 존재하지 않는 게시글
    @Test
    @WithMockUser(username = "ssar", roles = "USER")
    public void findById_fail_not_found_test() throws Exception {
        // given
        Integer nonExistentBoardId = 999;

        // when
        ResultActions result = mvc.perform(
                get("/api/boards/" + nonExistentBoardId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(404);
        assertThat(response).isNotEmpty();
    }

    // 게시글 수정 성공
    @Test
    @WithMockUser(username = "ssar", roles = "USER")
    public void update_success_test() throws Exception {
        // given
        Integer boardId = 1;
        BoardRequest.UpdateDTO updateDTO = new BoardRequest.UpdateDTO();
        updateDTO.setTitle("수정된 제목");
        updateDTO.setContent("수정된 내용입니다.");

        String requestBody = om.writeValueAsString(updateDTO);

        // when
        ResultActions result = mvc.perform(
                put("/api/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 게시글 수정 실패 - 권한 없음
    @Test
    @WithMockUser(username = "other", roles = "USER")
    public void update_fail_forbidden_test() throws Exception {
        // given
        Integer boardId = 1;
        BoardRequest.UpdateDTO updateDTO = new BoardRequest.UpdateDTO();
        updateDTO.setTitle("수정된 제목");
        updateDTO.setContent("수정된 내용입니다.");

        String requestBody = om.writeValueAsString(updateDTO);

        // when
        ResultActions result = mvc.perform(
                put("/api/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(403);
        assertThat(response).isNotEmpty();
    }

    // 게시글 삭제 성공
    @Test
    @WithMockUser(username = "ssar", roles = "USER")
    public void deleteById_success_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                delete("/api/boards/" + boardId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 게시글 삭제 실패 - 권한 없음
    @Test
    @WithMockUser(username = "other", roles = "USER")
    public void deleteById_fail_forbidden_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                delete("/api/boards/" + boardId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(403);
        assertThat(response).isNotEmpty();
    }
}
