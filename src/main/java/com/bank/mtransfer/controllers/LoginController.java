package com.bank.mtransfer.controllers;

import com.bank.mtransfer.models.db.User;
import com.bank.mtransfer.models.payload.request.*;
import com.bank.mtransfer.models.payload.response.*;
import com.bank.mtransfer.repository.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
public class LoginController {

    private final ClientRepository clientRepository;
    private final ClientParamRepository clientParamRepository;
    private final AccRepository accRepository;
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginController(ClientRepository clientRepository, ClientParamRepository clientParamRepository, AccRepository accRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.clientParamRepository = clientParamRepository;
        this.accRepository = accRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(){
        System.out.println("Hello login");
        return "login";
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    @PostMapping("/login")
    public String loginUser(@Valid @RequestParam(value = "username") String username,
                            @Valid @RequestParam(value = "password") String password,
                            HttpServletResponse response,
                            ModelMap model) {

        User userdt = userRepository.loginUser(username);

        if (userdt == null){
            model.addAttribute("username", username);
            model.addAttribute("errorUsername", "Логин отсутствует в системе");
            model.addAttribute("styleUsername", "color: red;");
            return "login";
        }

        if (!passwordEncoder.matches(password, userdt.getPassword())){
            model.addAttribute("username", username);
            model.addAttribute("errorPassword", "Неверный пароль");
            model.addAttribute("stylePassword", "color: red;");
            return "login";
        }


        LoginRequest loginRequest = new LoginRequest(username, password);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> userdata = restTemplate.postForEntity("http://localhost:8084/api/auth/signin", loginRequest, String.class);
        String user = userdata.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        System.out.println(user);

        if (user == null || user.equals("")) {
            log.warn("Была выполнена неудачная попытка входа");
            model.addAttribute("username", loginRequest.getUsername());
            model.addAttribute("error", "Не удалось войти. Неверное имя пользователя или пароль.");
            return "login";
        } else {
            String[] cookies = user.split(";");

            Cookie cookie = new Cookie(cookies[0].split("=")[0], cookies[0].split("=")[1]);
            cookie.setPath(cookies[1].split("=")[1]);
            cookie.setMaxAge(Integer.parseInt(cookies[2].split("=")[1]));
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);

            return "redirect:/home";
        }
    }

    @PostMapping("/signout")
    public String logout(HttpServletResponse response){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> logout = restTemplate.postForEntity("http://localhost:8084/api/auth/signout", null, String.class);
        String logdata = logout.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        System.out.println(logdata.toString());

        String[] cookies = logdata.split(";");

        Cookie cookie = new Cookie(cookies[0].split("=")[0], "");
        cookie.setPath(cookies[1].split("=")[1]);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Gson gson = new Gson();
        MessageResponse status = gson.fromJson(logout.getBody(), MessageResponse.class);
        log.warn("code: "+status.getCode()+", message: "+status.getMessage());
        return "redirect:/login";
    }
}