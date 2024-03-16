package com.umami.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    public static void main(String[] args) {
        try {

            String Token = System.getenv("TOKEN");

            JDA bot = JDABuilder.createDefault(Token).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
