package com.metacoding.springv2.web;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2.MyRestDoc;
import com.metacoding.springv2.core.util.JWTUtil;
import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.board.BoardRequest;
import com.metacoding.springv2.domain.user.User;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BoardControllerTest extends MyRestDoc {
    @Autowired
    private ObjectMapper om;

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

    @AfterEach
    void tearDown() {
        // 테스트 후 정리 작업 (필요시)
    }

    // 게시글 목록 조회 성공
    @Test
    public void findAll_success_test() throws Exception {
        //given
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/api/boards")
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("title 1"))
            .andExpect(jsonPath("$[0].content").value("Spring Study 1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].title").value("title 2"))
            .andExpect(jsonPath("$[1].content").value("Spring Study 2"))
            .andDo(MockMvcResultHandlers.print()).andDo(document);
      }

    // 게시글 목록 조회 실패
    @Test
    public void findAll_fail_test() throws Exception {
        //given
        String failToken = "Bearer 123123123123";
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/api/boards")
                        .header("Authorization", failToken)
        );
        // then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").value("로그인 후 이용해주세요"))
            .andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    // 게시글 작성 성공
    @Test
    public void save_success_test() throws Exception {
        // given
        Board board = Board.builder()
                .title("test title")
                .content("test content")
                .user(User.builder().id(1).username("ssar")
                    .password("1234")
                    .email("ssar@metacoding.com")
                    .roles("USER").build())
                .build();
        String requestBody = om.writeValueAsString(board);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/api/boards")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(6))
            .andExpect(jsonPath("$.title").value("test title"))
            .andExpect(jsonPath("$.content").value("test content"))
            .andDo(MockMvcResultHandlers.print()).andDo(document);
      }

    // // 게시글 작성 실패
    @Test
    public void save_fail_test() throws Exception {
        // given
        Board board = Board.builder()
                .title("test title long long long long ======================")
                .content("test content")
                .user(User.builder().id(1).username("ssar")
                    .password("1234")
                    .email("ssar@metacoding.com")
                    .roles("USER").build())
                .build();

        String requestBody = om.writeValueAsString(board);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.post("/api/boards")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isBadRequest()) // 400
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.msg").value("title:제목은 1자 이상 30자 이하로 입력해주세요"))
            .andExpect(jsonPath("$.body").isEmpty())
            .andDo(MockMvcResultHandlers.print()).andDo(document); 
      }

    // // 게시글 상세 조회 성공
    @Test
    public void findById_success_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/api/boards/" + boardId)
                        .header("Authorization", accessToken)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.isBoardOwner").value(true))
            .andExpect(jsonPath("$.boardId").value(boardId))
            .andExpect(jsonPath("$.title").value("title 1"))
            .andExpect(jsonPath("$.content").value("Spring Study 1"))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.username").value("ssar"))
            .andExpect(jsonPath("$.replies", hasSize(0)))
            .andDo(MockMvcResultHandlers.print()).andDo(document);
      }

    // // 게시글 상세 조회 실패
    @Test
    public void findById_fail_test() throws Exception {
        // given
        Integer id = 50;
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.get("/api/boards/" + id)
                        .header("Authorization", accessToken)
        );
        // then
        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.msg").value("게시글을 찾을 수 없습니다"))
            .andExpect(jsonPath("$.body").isEmpty())
            .andDo(MockMvcResultHandlers.print()).andDo(document);
      }

     // 게시글 수정 성공
    @Test
    public void update_success_test() throws Exception {
        // given
        Integer boardId = 1;
        BoardRequest.UpdateDTO updateDTO = new BoardRequest.UpdateDTO();
        updateDTO.setTitle("update title");
        updateDTO.setContent("update content");

        String requestBody = om.writeValueAsString(updateDTO);
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.put("/api/boards/" + boardId)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))              
            .andExpect(jsonPath("$.title").value("update title"))
            .andExpect(jsonPath("$.content").value("update content"))
            .andDo(MockMvcResultHandlers.print()).andDo(document);
      }

    // 게시글 수정 실패
    @Test
    public void update_fail_test() throws Exception {
        // given
        Integer boardId = 1;
        BoardRequest.UpdateDTO updateDTO = new BoardRequest.UpdateDTO();
        updateDTO.setTitle("update title");
        updateDTO.setContent("update content");

        String requestBody = om.writeValueAsString(updateDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.put("/api/boards/" + boardId)
                        .header("Authorization", accessToken1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        // then
        result.andExpect(status().isForbidden()) 
        .andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.msg").value("게시글을 수정할 권한이 없습니다."))
        .andExpect(jsonPath("$.body").isEmpty())
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
                        .header("Authorization", accessToken)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("게시글 삭제 완료"))
            .andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 게시글 삭제 실패
    @Test
    public void deleteById_fail_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.delete("/api/boards/" + boardId)
                        .header("Authorization", accessToken1)
        );
        // then
        result.andExpect(status().isForbidden()) 
            .andExpect(jsonPath("$.status").value(403))
            .andExpect(jsonPath("$.msg").value("게시글을 삭제할 권한이 없습니다."))
            .andExpect(jsonPath("$.body").isEmpty())
            .andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
