package com.metacoding.springv2.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.domain.auth.AuthRequest;


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
            .andExpect(jsonPath("$.roles").value("USER"))
            .andDo(document.document(
                requestFields(
                    fieldWithPath("username").description("사용자명"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("roles").description("역할")
                ),
                responseFields(
                    fieldWithPath("id").description("사용자 ID"),
                    fieldWithPath("username").description("사용자명"),
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("roles").description("역할")
                )
            ));

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
            .andExpect(jsonPath("$.body").isEmpty())
            .andDo(document.document());
  
        
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
            .andExpect(jsonPath("$").value(org.hamcrest.Matchers.startsWith("Bearer ")))
            .andDo(document.document());
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
            .andExpect(jsonPath("$.body").isEmpty()) // body 가 null
            .andDo(document.document());
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
            .andExpect(jsonPath("$.available").value(true))
            .andDo(document.document());
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
                .andExpect(jsonPath("$.available").value(false))
                .andDo(document.document());
    }
}
