package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.domain.user.UserRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {

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

    // 회원수정 성공
    @Test
   public void updateUser_success_test() throws Exception {
        // given
        UserRequest.UpdateDTO updateDTO = new UserRequest.UpdateDTO();
        updateDTO.setEmail("updated@metacoding.com");
        updateDTO.setPassword("newpassword123");

        String requestBody = om.writeValueAsString(updateDTO);

        // when
        ResultActions result = mvc.perform(
                put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        System.out.println("updateUser_success_test - Status: " + status);
        System.out.println("updateUser_success_test - Response: " + response);
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 회원수정 실패
    @Test
    public void updateUser_fail_test() throws Exception {
        // given
        UserRequest.UpdateDTO updateDTO = new UserRequest.UpdateDTO();
        updateDTO.setEmail("updated@metacoding.com");
        updateDTO.setPassword("newpassword123");

        String requestBody = om.writeValueAsString(updateDTO);
        String token = "Bearer 131231232131"; // 잘못된 토큰

        // when
        ResultActions result = mvc.perform(
                put("/api/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(401);
        assertThat(response).isNotEmpty();
    }


    // 회원조회 성공
    @Test
    public void getUser_success_test() throws Exception {
        // given
        Integer userId = 1;

        // when
        ResultActions result = mvc.perform(
                get("/api/users/" + userId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
    }

    // 회원조회 실패 - 잘못된 토큰
    @Test
    public void getUser_fail_invalid_token_test() throws Exception {
        // given
        Integer userId = 1;
        String invalidToken = "Bearer invalid_token";

        // when
        ResultActions result = mvc.perform(
                get("/api/users/" + userId)
                        .header("Authorization", invalidToken)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(401);
        assertThat(response).isNotEmpty();
    }

    // 회원조회 실패 - 존재하지 않는 사용자
    @Test
    public void getUser_fail_user_not_found_test() throws Exception {
        // given
        Integer nonExistentUserId = 999;

        // when
        ResultActions result = mvc.perform(
                get("/api/users/" + nonExistentUserId)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        
        assertThat(status).isEqualTo(404);
        assertThat(response).isNotEmpty();
    }
}
