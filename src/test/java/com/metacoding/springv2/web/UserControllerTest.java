package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.core.util.JWTUtil;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.user.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class UserControllerTest extends MyRestDoc {
    @Autowired
    private ObjectMapper om;


    private String accessToken;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성 및 JWT 토큰 생성
        User user = User.builder()
                .id(1)
                .username("ssar")
                .password("1234")
                .email("ssar@metacoding.com")
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
        updateDTO.setEmail("update@metacoding.com");
        updateDTO.setPassword("12345");

        String requestBody = om.writeValueAsString(updateDTO);

        // when
        ResultActions result = mvc.perform(
            MockMvcRequestBuilders.put("/api/users")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value("ssar"))
        .andExpect(jsonPath("$.email").value("update@metacoding.com"))
        .andExpect(jsonPath("$.roles").value("USER"));
      }

    // 회원수정 실패
    @Test
    public void updateUser_fail_test() throws Exception {
        // given
        UserRequest.UpdateDTO updateDTO = new UserRequest.UpdateDTO();
        updateDTO.setEmail("update@metacoding.com");
        updateDTO.setPassword("12345");

        String requestBody = om.writeValueAsString(updateDTO);
        String failToken = "Bearer 131231232131"; // 잘못된 토큰

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.put("/api/users")
                        .header("Authorization", failToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").value("로그인 후 이용해주세요"));
      }


    // 회원조회 성공
    @Test
    public void getUser_success_test() throws Exception {
        // given
        Integer userId = 1;
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/api/users/" + userId)
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value("ssar"))
        .andExpect(jsonPath("$.email").value("ssar@metacoding.com"))
        .andExpect(jsonPath("$.roles").value("USER"));   
     }


    // 회원조회 실패
    @Test
    public void getUser_fail_test() throws Exception {
        // given
        Integer id = 20;
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/api/users/" + id)
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.msg").value("접근할 수 없는 유저입니다"));
      }
}
