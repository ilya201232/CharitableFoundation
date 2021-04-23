package com.pwmtp.charitable_foundation.controller;

import com.pwmtp.charitable_foundation.domain.User;
import com.pwmtp.charitable_foundation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@RequestMapping("/signup")
public class SignupController {
    private final UserService USER_SERVICE;

    @Autowired
    public SignupController(UserService USER_SERVICE) {
        this.USER_SERVICE = USER_SERVICE;
    }

    @GetMapping
    public Object getPage(User user) {
        return new ModelAndView("signup");
    }

    @PostMapping
    public Object register(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.getFieldErrorCount("password") == 0){
            boolean is_passwords_match;
            boolean is_password_correct;

            //Checking password for rules
            boolean has_upper_char = false; //At least one symbol should be upper case
            boolean has_number = false; //At least one number presents
            for (char ch : user.getPassword().toCharArray()) {
                if (!has_upper_char && Character.isUpperCase(ch)) has_upper_char = true;
                if (!has_number && Character.isDigit(ch)) has_number = true;
            }

            is_password_correct = has_upper_char && has_number;
            if (!is_password_correct) {
                if (!has_upper_char)
                    bindingResult.addError(new FieldError("user", "password", "Password does not have any upper case chars!"));
                if (!has_number)
                    bindingResult.addError(new FieldError("user", "password", "Password does not have any numbers!"));
            }

            //Checking passwords for match
            is_passwords_match = user.getPassword().equals(user.getPasswordConf());
            if (!is_passwords_match){
                bindingResult.addError(new FieldError("user","passwordConf", "Passwords do not match!"));
            }
        }

        if (bindingResult.getFieldErrorCount("number") == 0){
            if (user.getNumber().toCharArray()[0] != '+' && user.getNumber().length()==12){
                bindingResult.addError(new FieldError("user","number", "Phone number should only contain 11 numbers!"));
            }
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView("signup");
        }else{
            USER_SERVICE.register(user);
            return new RedirectView("/activate-account");
        }
    }

    @GetMapping("/activate/{code}")
    public Object activate(@PathVariable String code, Model model) {
        boolean isActivated = USER_SERVICE.activateUser(code);
        if(isActivated) {
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }
        return new RedirectView("/login");
    }

}
