package org.example.springv3.core.util;

import org.example.springv3.user.User;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {

    @Test
    public void create_test(){
        User user = User.builder().id(1).username("ssar").build();

        String accessToken = JwtUtil.create(user);
        System.out.println(accessToken);
    }

    @Test
    public void verify_test(){
        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLrsJTrs7QiLCJpZCI6MSwiZXhwIjoxNzI2Nzk3NDAxLCJ1c2VybmFtZSI6InNzYXIifQ.EDeHJV6xQULWI-Vd165H0jvUEOpsMpmARATfYx6s_xw1ZDgLL4EGsIvYJyOgawlxVdOuCO2wvxzqDQfcG9EDQg";
        User user = JwtUtil.verify(accessToken);
        System.out.println(user.getId());
        System.out.println(user.getUsername());
    }
}
