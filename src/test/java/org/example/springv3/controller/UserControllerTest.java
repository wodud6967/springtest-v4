package org.example.springv3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springv3.user.UserRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@DataJpaTest 이거는 레파지토리만 뜨운다
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //랜덤으로 포트를 하나 만든다.
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //가짜로 8080을띄운다
public class UserControllerTest {


    @Autowired
    private MockMvc mvc; // 톰켓입구부터 들어가게 도와준다. 포스트맨 같은거 이걸쓰기위해서는 어노테이션 @AutoConfigureMockMvc필요

    private ObjectMapper om = new ObjectMapper(); // 메번띄우기 귀찮으니까 설정

    @Test
    public void join_text() throws Exception {
        //given
        UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
        joinDTO.setUsername("haha");
        joinDTO.setPassword("1234");
        joinDTO.setEmail("haha@nate.com");

        String requestBody = om.writeValueAsString(joinDTO); //json으로 만들어보기
        // System.out.println(requestBody);
        //when

        //이게 실제 테스트코드
        ResultActions actions = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //eye
        String reponseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(reponseBody);

        //then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body.id").value(4));
        actions.andExpect(jsonPath("$.body.username").value("haha"));
        actions.andExpect(jsonPath("$.body.email").value("haha@nate.com"));
        actions.andExpect(jsonPath("$.body.profile").isEmpty()); // null검은


    }

    @Test
    public void login_text() throws Exception {
        //given
        UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
        loginDTO.setUsername("ssar");
        loginDTO.setPassword("1234");


        String requestBody = om.writeValueAsString(loginDTO); //json으로 만들어보기
        //System.out.println(requestBody);
        //when
        //이게 실제 테스트코드
        ResultActions actions = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //eye
        String reponseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(reponseBody);

        String responseJwt = actions.andReturn().getResponse().getHeader("Authorization");
        System.out.println(responseJwt);

        //MockMvcResultMatchers.jsonPath 이거를 static으로 import함
        //then
        actions.andExpect(header().string("Authorization", Matchers.notNullValue()));
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body").isEmpty());

    }


    //테스트는 독립적이어야한다
   /* @Order(1) 이런게 잇는데 쓰지말자
    @Test
    public void join_text(){

    }

    @Order(2)
    @Test
    public void join_text(){

    }*/
    //테스트는 격리 되어야한다.
}
