package org.example.springv3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springv3.board.BoardRepository;
import org.example.springv3.board.BoardRequest;
import org.example.springv3.core.util.JwtUtil;
import org.example.springv3.user.User;
import org.example.springv3.user.UserRequest;
import org.hamcrest.Matchers;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;
    private ObjectMapper om = new ObjectMapper();
    private String accessToken;



    @BeforeEach
    public void setUp(){
        System.out.println("나 실행돼? setUp");
        User sessionUser = User.builder().id(1).username("ssar").build();
        accessToken = JwtUtil.create(sessionUser);
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 2;

        // when
        ResultActions actions = mvc.perform(
                delete("/api/board/"+id)
                        .header("Authorization", "Bearer "+accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body").isEmpty());

    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO();
        saveDTO.setTitle("title 11");
        saveDTO.setContent("content 11");

        String requestBody = om.writeValueAsString(saveDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                post("/api/board")
                        .header("Authorization", "Bearer "+accessToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );



        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body.title").value("title 11"));
        actions.andExpect(jsonPath("$.body.content").value("content 11"));
        actions.andExpect(jsonPath("$.body.id").isNumber());

    }
    @Test
    public void save_fail_test_1() throws Exception {
        // given
        BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO();
        saveDTO.setTitle("");
        saveDTO.setContent("content 11");

        String requestBody = om.writeValueAsString(saveDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                post("/api/board")
                        .header("Authorization", "Bearer "+accessToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );



        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(jsonPath("$.status").value(400));
        //밑에거랑 body 검증 필요 이렇게 test를 몇개 해줘야한다 프론트가 애매한것들 몇개 추려서
      //  actions.andExpect(jsonPath("$.msg").value("false"));


    }


}