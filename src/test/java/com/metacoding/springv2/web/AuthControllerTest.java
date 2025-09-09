package com.metacoding.springv2.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.auth.AuthRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuthControllerTest extends MyRestDoc {
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
                        .content(requestBody)
        ).andDo(MockMvcResultHandlers.print()).andDo(document);

        // then
        String responseBody = result.andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode json = om.readTree(responseBody);

        assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(200);
        assertThat(json.get("msg").asText()).isEqualTo("성공");
        assertThat(json.get("body").get("id").asInt()).isEqualTo(3);
        assertThat(json.get("body").get("username").asText()).isEqualTo("testUser");
        assertThat(json.get("body").get("email").asText()).isEqualTo("test@nate.com");
        assertThat(json.get("body").get("roles").asText()).isEqualTo("USER");
  }

    // 회원가입 실패
    @Test
    public void join_fail_test() throws Exception {
        // given
        User user = User.builder()
                        .username("testUser")
                        .password("1234")
                        .email("test")
                        .roles("USER")
                        .build();

        String requestBody = om.writeValueAsString(user);


        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                ).andDo(MockMvcResultHandlers.print()).andDo(document);

        // then
        String responseBody = result.andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode json = om.readTree(responseBody);

        // then
        assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(400);
        assertThat(json.get("msg").asText()).isEqualTo("email:이메일 형식이 올바르지 않습니다");
        assertThat(json.get("body").isEmpty()).isTrue();
   
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
                        .content(requestBody)
                        ).andDo(MockMvcResultHandlers.print()).andDo(document);
    
        // then
        String responseBody = result.andReturn()
                                    .getResponse()
                                    .getContentAsString();
    
        JsonNode json = om.readTree(responseBody);
    
        // HTTP status code
        assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(200);
        assertThat(json.get("msg").asText()).isEqualTo("성공");
        assertThat(json.get("body").asText()).isNotEmpty();
        assertThat(json.get("body").asText()).startsWith("Bearer ");
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
                        .content(requestBody)
                        ).andDo(MockMvcResultHandlers.print()).andDo(document);
    
        // then
        String responseBody = result.andReturn()
                                    .getResponse()
                                    .getContentAsString();
    
        JsonNode json = om.readTree(responseBody);
    
        // HTTP 상태코드 검증
        assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(401);
        assertThat(json.get("msg").asText()).isEqualTo("유저네임 혹은 비밀번호가 일치하지 않습니다");
        assertThat(json.get("body").isNull()).isTrue();
    }
    

    @Test
    public void checkUsername_success_test() throws Exception {
        // given
        String username = "testUser";
    
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/check-username")
                        .param("username", username)
                        ).andDo(MockMvcResultHandlers.print()).andDo(document);
    
        // then
        String responseBody = result.andReturn()
                                    .getResponse()
                                    .getContentAsString();
    
        JsonNode json = om.readTree(responseBody);
    
        assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(200);
        assertThat(json.get("msg").asText()).isEqualTo("성공");
        assertThat(json.get("body").get("available").asBoolean()).isTrue();
    }
    
    @Test
    public void checkUsername_fail_test() throws Exception {
          // given
          String username = "ssar";
    
          // when
          ResultActions result = mvc.perform(
                  MockMvcRequestBuilders.get("/check-username")
                          .param("username", username)
                          ).andDo(MockMvcResultHandlers.print()).andDo(document);
      
          // then
          String responseBody = result.andReturn()
                                      .getResponse()
                                      .getContentAsString();
      
          JsonNode json = om.readTree(responseBody);
      
          assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(200);
          assertThat(json.get("msg").asText()).isEqualTo("성공");
          assertThat(json.get("body").get("available").asBoolean()).isFalse();
      }
}
