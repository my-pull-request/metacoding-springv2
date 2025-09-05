package com.metacoding.springv2.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class AdminControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 정리 작업 (필요시)
    }

    // 관리자 게시글 삭제 성공
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteBoard_success_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                delete("/api/admin/boards/" + boardId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 관리자 게시글 삭제 실패 - 권한 없음 (USER 역할)
    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void deleteBoard_fail_forbidden_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                delete("/api/admin/boards/" + boardId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(403);
        assertThat(response).isNotEmpty();
    }
}

