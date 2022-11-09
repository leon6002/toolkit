package com.leo.toolkit;

import com.leo.toolkit.models.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class NullTest {
    @Test
    public void test1(){
        User user = null;
        user = Optional.ofNullable(user).orElse(createUser());
        System.out.println(user.toString());
    }

    @Test
    public void test2(){
        User user = null;
        user = Optional.ofNullable(user).orElseGet(()->createUser());
        System.out.println(user.toString());
    }

    private User createUser(){
        User user = new User();
        user.setName("leo");
        return user;
    }
}
