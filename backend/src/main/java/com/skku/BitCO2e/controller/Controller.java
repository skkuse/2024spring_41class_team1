package com.skku.BitCO2e.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.BitCO2e.model.CodeInput;
import com.skku.BitCO2e.model.User;
import com.skku.BitCO2e.patterns.Pattern1;
import com.skku.BitCO2e.patterns.Pattern2;
import com.skku.BitCO2e.patterns.Pattern3;
import com.skku.BitCO2e.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    private final Pattern1 pattern1;
    private final Pattern2 pattern2;
    private final Pattern3 pattern3;

    @Autowired
    private UserService userService;

    public Controller() {
        this.pattern1 = new Pattern1();
        this.pattern2 = new Pattern2();
        this.pattern3 = new Pattern3();
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        userService.createUser(user);

        return "User created successfully";
    }

    @PostMapping("/refactoring")
    public String refactoring(@RequestBody String codeInput) {
        String refactor1 = pattern1.main(codeInput);
        boolean isPattern1 = refactor1.contains("public class Fixed");

        String refactor2 = pattern2.main(refactor1);
        boolean isPattern2 = !refactor1.equals(refactor2);

        String refactor3 = pattern3.main(refactor2);
        boolean isPattern3 = !refactor2.equals(refactor3);

        Map<String, Object> response = new HashMap<>();
        response.put("isPattern1", isPattern1);
        response.put("isPattern2", isPattern2);
        response.put("isPattern3", isPattern3);
        response.put("code", refactor3);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("Error converting response to JSON", e);
        }
    }
}
