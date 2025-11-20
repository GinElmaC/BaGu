package com.GinElmaC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args){
        List<User> users = new ArrayList<>();
        for(int i = 0;i<20;i++){
            users.add(new User(i, UUID.randomUUID().toString()));
        }
        users.sort(Comparator.comparing(User::getAge).thenComparing(User::getName));
    }

    static class User{
        int age;
        String name;

        public int getAge() {
            return age;
        }

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
