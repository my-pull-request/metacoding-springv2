package com.metacoding.springv2.web;

import com.metacoding.springv2.core.util.JWTUtil;
import com.metacoding.springv2.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AdminControllerTest {
    @Autowired
    private MockMvc mvc;


    private String accessToken;
    private String accessToken1;


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

        User user2 = User.builder()
                .id(2)
                .username("cos")
                .password("1234")
                .email("cos@metacoding.com")
                .roles("ADMIN")
                .build();
        accessToken1 = JWTUtil.create(user2);
    }

    
    // 관리자 게시글 삭제 실패
    @Test
    public void deleteById_fail_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.delete("/api/admin/boards/" + boardId)
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(status().isForbidden()) 
            .andExpect(jsonPath("$.status").value(403));
    }

    // 관리자 게시글 삭제 성공
    @Test
    public void deleteById_success_test() throws Exception {
        // given
        Integer boardId = 1;
        
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.delete("/api/admin/boards/" + boardId)
                        .header("Authorization", accessToken1)
        );
        // then
        result.andExpect(status().isOk());
    }

}

