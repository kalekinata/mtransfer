package com.bank.mtransfer.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/")
    public String main(Model model, HttpServletResponse response){
        System.out.println(response.getHeaderNames());
        return "home";
    }
}
