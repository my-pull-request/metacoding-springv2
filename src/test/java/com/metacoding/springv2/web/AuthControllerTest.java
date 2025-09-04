package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.domain.auth.AuthRequest;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.user.UserRepository;
import com.metacoding.springv2.core.util.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuthControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;
    
    private String accessToken;

    @BeforeEach
    public void setUp() {
        
        // 테스트 시작 전에 실행할 코드
        System.out.println("setUp");
        User ssar = User.builder()
                .id(1)
                .username("ssar")
                .roles("USER")
                .build();
        accessToken = JWTUtil.create(ssar);
    }

    @AfterEach
    public void tearDown() { 
        System.out.println("tearDown");
    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    void join_success_test() throws Exception {
        // given
        AuthRequest.JoinDTO joinDTO = new AuthRequest.JoinDTO();
        joinDTO.setUsername("testuser");
        joinDTO.setPassword("1234");
        joinDTO.setEmail("test@example.com");
        joinDTO.setRoles("USER");

        String requestBody = om.writeValueAsString(joinDTO);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        String response = result.andReturn().getResponse().getContentAsString();
        int status = result.andReturn().getResponse().getStatus();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).contains("testuser");
        assertThat(response).contains("test@example.com");
        assertThat(response).contains("USER");
    }


    @DisplayName("로그인 성공 테스트")
    @Test
    void login_success_test() throws Exception {
        // given - 회원가입 먼저 수행
        AuthRequest.JoinDTO joinDTO = new AuthRequest.JoinDTO();
        joinDTO.setUsername("testuser");
        joinDTO.setPassword("1234");
        joinDTO.setEmail("test@example.com");
        joinDTO.setRoles("USER");

        String joinRequestBody = om.writeValueAsString(joinDTO);
        mvc.perform(
                post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinRequestBody)
        );

        // 로그인 요청
        AuthRequest.LoginDTO loginDTO = new AuthRequest.LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("1234");

        String loginRequestBody = om.writeValueAsString(loginDTO);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody)
        );

        // then
        String response = result.andReturn().getResponse().getContentAsString();
        int status = result.andReturn().getResponse().getStatus();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).startsWith("Bearer ");
    }

    @DisplayName("회원가입 실패 테스트 - 중복된 유저네임")
    @Test
    void join_fail_duplicate_username_test() throws Exception {
        // given - 첫 번째 회원가입
        AuthRequest.JoinDTO firstJoinDTO = new AuthRequest.JoinDTO();
        firstJoinDTO.setUsername("testuser");
        firstJoinDTO.setPassword("1234");
        firstJoinDTO.setEmail("test1@example.com");
        firstJoinDTO.setRoles("USER");

        String firstRequestBody = om.writeValueAsString(firstJoinDTO);
        mvc.perform(
                post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstRequestBody)
        );

        // 두 번째 회원가입 (같은 유저네임)
        AuthRequest.JoinDTO secondJoinDTO = new AuthRequest.JoinDTO();
        secondJoinDTO.setUsername("testuser");
        secondJoinDTO.setPassword("5678");
        secondJoinDTO.setEmail("test2@example.com");
        secondJoinDTO.setRoles("USER");

        String secondRequestBody = om.writeValueAsString(secondJoinDTO);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondRequestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("회원가입 실패 테스트 - 상태: " + status + ", 응답: " + response);
        assertThat(status).isEqualTo(400);
    }

    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    @Test
    void login_fail_wrong_password_test() throws Exception {
        // given - 회원가입 먼저 수행
        AuthRequest.JoinDTO joinDTO = new AuthRequest.JoinDTO();
        joinDTO.setUsername("testuser");
        joinDTO.setPassword("1234");
        joinDTO.setEmail("test@example.com");
        joinDTO.setRoles("USER");

        String joinRequestBody = om.writeValueAsString(joinDTO);
        mvc.perform(
                post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinRequestBody)
        );

        // 잘못된 비밀번호로 로그인 시도
        AuthRequest.LoginDTO loginDTO = new AuthRequest.LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("wrongpassword");

        String loginRequestBody = om.writeValueAsString(loginDTO);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("로그인 실패 테스트 - 상태: " + status + ", 응답: " + response);
        assertThat(status).isEqualTo(400);
    }

    @DisplayName("로그인 실패 테스트 - 존재하지 않는 유저")
    @Test
    void login_fail_user_not_found_test() throws Exception {
        // given
        AuthRequest.LoginDTO loginDTO = new AuthRequest.LoginDTO();
        loginDTO.setUsername("nonexistentuser");
        loginDTO.setPassword("1234");

        String loginRequestBody = om.writeValueAsString(loginDTO);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody)
        );

        // then
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("존재하지 않는 유저 테스트 - 상태: " + status + ", 응답: " + response);
        assertThat(status).isEqualTo(400);
    }


}
