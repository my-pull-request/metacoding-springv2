package com.metacoding.springv2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.core.util.JWTUtil;
import com.metacoding.springv2.domain.reply.ReplyRequest;
import com.metacoding.springv2.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        accessToken = JWTUtil.create(user);    
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 정리 작업 (필요시)
    }

    // 댓글 작성 성공
    @Test
    public void save_success_test() throws Exception {
        // given
        ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO();
        saveDTO.setComment("test comment");
        saveDTO.setBoardId(1);

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/api/replies")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())                        
            .andExpect(jsonPath("$.comment").value(saveDTO.getComment()))  
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.username").value("ssar"))
            .andExpect(jsonPath("$.boardId").value(1))
            .andDo(MockMvcResultHandlers.print()).andDo(document);
      }

    // 댓글 작성 실패
    @Test
    public void save_fail_test() throws Exception {
        // given
        ReplyRequest.SaveDTO saveDTO = new ReplyRequest.SaveDTO();
        saveDTO.setComment(""); // 댓글 없음
        saveDTO.setBoardId(1);

        String requestBody = om.writeValueAsString(saveDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/api/replies")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        // then
        result.andExpect(status().isBadRequest()) 
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.msg", startsWith("comment:")))
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
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(status().isOk())
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
            .andExpect(jsonPath("$.body").isEmpty())
            .andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}

