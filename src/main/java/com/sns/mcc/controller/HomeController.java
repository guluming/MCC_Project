package com.sns.mcc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    @GetMapping("/chat")
    public String chatGet() {

        log.info("@ChatController, chat GET()");

        return "chat";
    }
}
