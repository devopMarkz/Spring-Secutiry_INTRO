package io.github.marcos.libraryapi.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String paginaHome(Authentication authentication){
        return "olá " + authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthCode(@RequestParam("code") String code){
        return "Seu authorization code " + code;
    }
}