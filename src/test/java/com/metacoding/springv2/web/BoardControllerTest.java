package com.metacoding.springv2.web;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.board.BoardRequest;
import com.metacoding.springv2.core.util.JwtUtil;
import com.metacoding.springv2.user.User;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest extends MyRestDoc {

        @Autowired
        private ObjectMapper om;

        User testUser;
        private String accessToken; // 성공 테스트용 토큰

        @BeforeEach
        void setUp() {
                // 테스트용 사용자 생성 및 JWT 토큰 생성
                testUser = User.builder()
                                .id(1)
                                .username("ssar")
                                .password("1234")
                                .email("ssar@metacoding.com")
                                .roles("USER")
                                .build();
                accessToken = JwtUtil.create(testUser);
        }

        // 게시글 목록 조회 성공
        @Test
        public void findAll_success_test() throws Exception {
                // given
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.get("/api/boards")
                                                .header("Authorization", accessToken));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body", hasSize(5)))
                                .andExpect(jsonPath("$.body[0].id").value(1))
                                .andExpect(jsonPath("$.body[0].title").value("title 1"))
                                .andExpect(jsonPath("$.body[0].content").value("Spring Study 1"))
                                .andExpect(jsonPath("$.body[1].id").value(2))
                                .andExpect(jsonPath("$.body[1].title").value("title 2"))
                                .andExpect(jsonPath("$.body[1].content").value("Spring Study 2"))
                                .andExpect(jsonPath("$.body[2].id").value(3))
                                .andExpect(jsonPath("$.body[2].title").value("title 3"))
                                .andExpect(jsonPath("$.body[2].content").value("Spring Study 3"))
                                .andExpect(jsonPath("$.body[3].id").value(4))
                                .andExpect(jsonPath("$.body[3].title").value("title 4"))
                                .andExpect(jsonPath("$.body[3].content").value("Spring Study 4"))
                                .andExpect(jsonPath("$.body[4].id").value(5))
                                .andExpect(jsonPath("$.body[4].title").value("title 5"))
                                .andExpect(jsonPath("$.body[4].content").value("Spring Study 5"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글 작성 성공
        @Test
        public void save_success_test() throws Exception {
                // given
                Board board = Board.builder()
                                .title("test title")
                                .content("test content")
                                .user(testUser)
                                .build();
                String requestBody = om.writeValueAsString(board);

                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.post("/api/boards")
                                                .header("Authorization", accessToken)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(6))
                                .andExpect(jsonPath("$.body.title").value("test title"))
                                .andExpect(jsonPath("$.body.content").value("test content"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글 상세 조회 성공
        @Test
        public void findById_success_test() throws Exception {
                // given
                Integer boardId = 5;
                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.get("/api/boards/" + boardId)
                                                .header("Authorization", accessToken));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.boardId").value(boardId))
                                .andExpect(jsonPath("$.body.title").value("title 5"))
                                .andExpect(jsonPath("$.body.content").value("Spring Study 5"))
                                .andExpect(jsonPath("$.body.userId").value(2))
                                .andExpect(jsonPath("$.body.username").value("cos"))
                                .andExpect(jsonPath("$.body.isBoardOwner").value(false))
                                .andExpect(jsonPath("$.body.replies", hasSize(2)))
                                .andExpect(jsonPath("$.body.replies[0].id").value(4))
                                .andExpect(jsonPath("$.body.replies[0].username").value("ssar"))
                                .andExpect(jsonPath("$.body.replies[0].comment").value("reply 4"))
                                .andExpect(jsonPath("$.body.replies[0].isOwner").value(true))
                                .andExpect(jsonPath("$.body.replies[1].id").value(5))
                                .andExpect(jsonPath("$.body.replies[1].username").value("ssar"))
                                .andExpect(jsonPath("$.body.replies[1].comment").value("reply 5"))
                                .andExpect(jsonPath("$.body.replies[1].isOwner").value(true))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글 수정 성공
        @Test
        public void update_success_test() throws Exception {
                // given
                Integer boardId = 1;
                BoardRequest.UpdateDTO updateDTO = new BoardRequest.UpdateDTO("update test", "update content");

                String requestBody = om.writeValueAsString(updateDTO);

                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.put("/api/boards/" + boardId)
                                                .header("Authorization", accessToken)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body.id").value(1))
                                .andExpect(jsonPath("$.body.title").value("update test"))
                                .andExpect(jsonPath("$.body.content").value("update content"))
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }

        // 게시글 삭제 성공
        @Test
        public void deleteById_success_test() throws Exception {
                // given
                Integer boardId = 1;

                // when
                ResultActions result = mvc.perform(
                                MockMvcRequestBuilders.delete("/api/boards/" + boardId)
                                                .header("Authorization", accessToken));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.msg").value("성공"))
                                .andExpect(jsonPath("$.body").isEmpty())
                                .andDo(MockMvcResultHandlers.print()).andDo(document);
        }
}
