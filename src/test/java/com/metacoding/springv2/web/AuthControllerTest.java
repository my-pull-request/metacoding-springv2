package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.domain.auth.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuthControllerTest extends MyRestDoc {
    @Autowired
    private ObjectMapper om;


    // 회원가입 성공
    @Test
    public void join_success_test() throws Exception {
        // given
        AuthRequest.JoinDTO joinDTO = new AuthRequest.JoinDTO();
        joinDTO.setUsername("testUser");
        joinDTO.setPassword("1234");
        joinDTO.setEmail("test@metacoding.com");
        joinDTO.setRoles("USER");

        String requestBody = om.writeValueAsString(joinDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.email").value("test@metacoding.com"))
            .andExpect(jsonPath("$.roles").value("USER"));

    }

    // 회원가입 실패
    @Test
    public void join_fail_test() throws Exception {
        // given
        AuthRequest.JoinDTO joinDTO = new AuthRequest.JoinDTO();
        joinDTO.setUsername("testUser");
        joinDTO.setPassword("1234");
        joinDTO.setEmail("test"); // 이메일 형식 잘못됨
        joinDTO.setRoles("USER");

        //when
        String requestBody = om.writeValueAsString(joinDTO);
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.msg").value("email:이메일 형식이 올바르지 않습니다"))
            .andExpect(jsonPath("$.body").isEmpty());
  
        
    }

    // 로그인 성공
    @Test
    public void login_success_test() throws Exception {
        // given
        AuthRequest.LoginDTO loginDTO = new AuthRequest.LoginDTO();
        loginDTO.setUsername("ssar");
        loginDTO.setPassword("1234");

        String requestBody = om.writeValueAsString(loginDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$").value(org.hamcrest.Matchers.startsWith("Bearer ")));
    }

    // 로그인 실패
    @Test
    public void login_fail_test() throws Exception {
        // given
        AuthRequest.LoginDTO loginDTO = new AuthRequest.LoginDTO();
        loginDTO.setUsername("ssar");
        loginDTO.setPassword("1111");

        String loginRequestBody = om.writeValueAsString(loginDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody)
        );

        // then
        result.andExpect(status().isUnauthorized()) // == 401
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.msg").value("비밀번호가 일치하지 않습니다"))
            .andExpect(jsonPath("$.body").isEmpty()); // body 가 null
    }

    @Test
    public void checkUsername_success_test() throws Exception {
        // given
        String username = "testUser";

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/check-username")
                        .param("username", username)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void checkUsername_fail_test() throws Exception {
        // given
        String username = "ssar";
        
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/check-username")
                        .param("username", username)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }
}
