package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.core.util.JwtUtil;
import com.metacoding.springv2.domain.reply.ReplyRequest;
import com.metacoding.springv2.domain.user.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ReplyControllerTest extends MyRestDoc {
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
            accessToken = JwtUtil.create(user);    
        }

        @AfterEach
        void tearDown() {
            // 테스트 후 정리 작업 (필요시)
        }

        // 댓글 작성 성공
        @Test
        public void save_success_test() throws Exception {
            // given
            ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO("test comment",1);

            String requestBody = om.writeValueAsString(saveDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/api/replies")
                            .header("Authorization", accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody));
            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").isNumber())                        
                .andExpect(jsonPath("$.body.comment").value("test comment"))  
                .andExpect(jsonPath("$.body.userId").value(1))
                .andExpect(jsonPath("$.body.username").value("ssar"))
                .andExpect(jsonPath("$.body.boardId").value(1))
                .andDo(MockMvcResultHandlers.print()).andDo(document);
            }

        // 댓글 작성 실패
        @Test
        public void save_fail_test() throws Exception {
            // given
            ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO("",1);
            String requestBody = om.writeValueAsString(saveDTO);

            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.post("/api/replies")
                            .header("Authorization", accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody));
            // then
            result.andExpect(status().isBadRequest()) 
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.msg").value("comment:댓글은 1자 이상 100자 이하로 입력해주세요"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 댓글 삭제 성공
        @Test
        public void deleteById_success_test() throws Exception {
            // given
            Integer replyId = 4;
            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.delete("/api/replies/" + replyId)
                            .header("Authorization", accessToken));
            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 댓글 삭제 실패
        @Test
        public void deleteById_fail_test() throws Exception {
            // given
            Integer replyId = 1;
            // when
            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders.delete("/api/replies/" + replyId)
                            .header("Authorization", accessToken)
            );
            // then
            result.andExpect(status().isForbidden()) 
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.msg").value("댓글을 삭제할 권한이 없습니다"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andDo(MockMvcResultHandlers.print()).andDo(document);  
        }
}

