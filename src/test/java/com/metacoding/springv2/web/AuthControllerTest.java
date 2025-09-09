package com.metacoding.springv2.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.auth.AuthRequest;
import static org.hamcrest.Matchers.startsWith;
import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthControllerTest extends MyRestDoc {
    
        @Autowired
        private ObjectMapper om;

        // 회원가입 성공
        @Test
        public void join_success_test() throws Exception {
        // given
        User user = User.builder()
                .username("testUser")
                .password("1234")
                .email("test@nate.com")
                .roles("USER")
                .build();
    
        String requestBody = om.writeValueAsString(user);
    
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));    
        // then
        result.andExpect(status().isOk()) // HTTP 상태코드 200
              .andExpect(jsonPath("$.msg").value("성공"))
              .andExpect(jsonPath("$.body.id").value(3))
              .andExpect(jsonPath("$.body.username").value("testUser"))
              .andExpect(jsonPath("$.body.email").value("test@nate.com"))
              .andExpect(jsonPath("$.body.roles").value("USER"))
              .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
    

        // 회원가입 실패
        @Test
        public void join_fail_test() throws Exception {
        // given
        User user = User.builder()
            .username("testUser")
            .password("1234")
            .email("test") // 잘못된 이메일
            .roles("USER")
            .build();

        String requestBody = om.writeValueAsString(user);

        // when
        ResultActions result = mvc.perform(
            MockMvcRequestBuilders.post("/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));
        // then
        result.andExpect(status().isBadRequest()) // HTTP 400
          .andExpect(jsonPath("$.msg").value("email:이메일 형식이 올바르지 않습니다"))
          .andExpect(jsonPath("$.body").isEmpty()) 
          .andDo(MockMvcResultHandlers.print()).andDo(document);
        }


        // 로그인 성공
        @Test
        public void login_success_test() throws Exception {
                // given
                AuthRequest.LoginDTO loginDTO = new AuthRequest.LoginDTO("ssar", "1234");
                String requestBody = om.writeValueAsString(loginDTO);
   
                // when
                ResultActions result = mvc.perform(      
                        MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));
                // then
                result.andExpect(status().isOk()) // HTTP 200
                        .andExpect(jsonPath("$.msg").value("성공"))
                        .andExpect(jsonPath("$.body").isNotEmpty())
                        .andExpect(jsonPath("$.body").value(startsWith("Bearer ")))                        
                        .andDo(MockMvcResultHandlers.print()).andDo(document);
        }


        // 로그인 실패
        @Test
        public void login_fail_test() throws Exception {
                // given
                AuthRequest.LoginDTO loginDTO = new AuthRequest.LoginDTO("ssar", "1111");
                String requestBody = om.writeValueAsString(loginDTO);
    
                // when
                ResultActions result = mvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));
    
                // then
                result.andExpect(status().isUnauthorized()) // HTTP 401
                        .andExpect(jsonPath("$.msg").value("유저네임 혹은 비밀번호가 일치하지 않습니다"))
                        .andExpect(jsonPath("$.body").isEmpty()) // body가 null 검증
                        .andDo(MockMvcResultHandlers.print()).andDo(document);
              }
    
              //유저네임 중복체크 성공
              @Test
              public void checkUsername_success_test() throws Exception {
                  // given
                  String username = "testUser";
              
                  // when
                  ResultActions result = mvc.perform(
                          MockMvcRequestBuilders.get("/check-username")
                                  .param("username", username));              
                  // then
                  result.andExpect(status().isOk()) // HTTP 200
                        .andExpect(jsonPath("$.msg").value("성공"))
                        .andExpect(jsonPath("$.body.available").value(true))
                        .andDo(MockMvcResultHandlers.print()).andDo(document);
              }
              
              //유저네임 중복체크 실패
              @Test
              public void checkUsername_fail_test() throws Exception {
                  // given
                  String username = "ssar";
              
                  // when
                  ResultActions result = mvc.perform(
                          MockMvcRequestBuilders.get("/check-username")
                                  .param("username", username));              
                  // then
                  result.andExpect(status().isOk()) // HTTP 200
                        .andExpect(jsonPath("$.msg").value("성공"))
                        .andExpect(jsonPath("$.body.available").value(false))
                        .andDo(MockMvcResultHandlers.print()).andDo(document);
              }
              
}
