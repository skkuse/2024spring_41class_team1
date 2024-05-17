package com.skku.BitCO2e.controller;

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
        String refactor2 = pattern2.main(refactor1);
        String refactor3 = pattern3.main(refactor2);
        return refactor3;
    }

}
