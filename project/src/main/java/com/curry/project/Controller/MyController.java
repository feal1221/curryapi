package com.curry.project.Controller;

import com.curry.project.result.ResultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @RequestMapping("/test123")
    public String test() {
        System.out.println("嘿！");
        return "Hello, this is a test response from MyController!";
    }


}
