package ru.exmp.movielist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//test controller
@Controller
public class HomeController {

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "its working";
    }
}