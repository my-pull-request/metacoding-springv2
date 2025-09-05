package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.domain.auth.AuthRequest;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.core.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

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
        int status = result.andReturn().getResponse().getStatus();
        String response = result.andReturn().getResponse().getContentAsString();

        User userResponse = om.readValue(response, User.class);

        assertThat(status).isEqualTo(200);
        assertThat(userResponse.getId()).isEqualTo(3);
        assertThat(userResponse.getUsername()).isEqualTo("testUser");
        assertThat(userResponse.getEmail()).isEqualTo("test@metacoding.com");
        assertThat(userResponse.getRoles()).isEqualTo("USER");

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
        String response = result.andReturn().getResponse().getContentAsString();
        int status = result.andReturn().getResponse().getStatus();
        
        assertThat(status).isEqualTo(200);
        assertThat(response).isNotEmpty();
        assertThat(response).startsWith("Bearer "); // 토큰이 Bearer 로 시작하는지 검증
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

}
