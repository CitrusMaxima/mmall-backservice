package com.mmall.controller.provider;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserServiceController {
    @Autowired
    IUserService iUserService;

    @PostMapping("/login/{username}/{password}")
    ServerResponse<User> login(@PathVariable("username") String username, @PathVariable("password") String password) {
        return iUserService.login(username, password);
    }

    @PostMapping("/register")
    ServerResponse<String> register(@RequestBody User user) {
        return iUserService.register(user);
    }

    @PostMapping("/checkVerifyLink/{link}")
    ServerResponse<String> checkVerifyLink(@PathVariable("link") String link) {
        return iUserService.checkVerifyLink(link);
    }

    @PostMapping("/checkValid/{str}/{type}")
    ServerResponse<String> checkValid(@PathVariable("str") String str, @PathVariable("type") String type) {
        return iUserService.checkValid(str, type);
    }

    @PostMapping("/selectQuestion/{username}")
    ServerResponse selectQuestion(@PathVariable("username") String username) {
        return iUserService.selectQuestion(username);
    }

    @PostMapping("/forgetCheckAnswer/{username}/{question}/{answer}")
    ServerResponse<String> forgetCheckAnswer(@PathVariable("username") String username, @PathVariable("question") String question, @PathVariable("answer") String answer) {
        return iUserService.forgetCheckAnswer(username, question, answer);
    }

    @PostMapping("/forgetResetPassword/{username}/{passwordNew}/{forgetToken}")
    ServerResponse<String> forgetResetPassword(@PathVariable("username") String username, @PathVariable("passwordNew") String passwordNew, @PathVariable("forgetToken") String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @PostMapping("/resetPassword/{passwordOld}/{passwordNew}")
    ServerResponse<String> resetPassword(@PathVariable("passwordOld") String passwordOld, @PathVariable("passwordNew") String passwordNew, @RequestBody User user) {
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    @PostMapping("/updateInformation")
    ServerResponse<User> updateInformation(@RequestBody User user) {
        return iUserService.updateInformation(user);
    }

    @PostMapping("/getInformation/{id}")
    ServerResponse<User> getInformation(@PathVariable("id") Integer id) {
        return iUserService.getInformation(id);
    }

    @PostMapping("/checkAdminRole")
    ServerResponse checkAdminRole(@RequestBody User user) {
        return iUserService.checkAdminRole(user);
    }
}
