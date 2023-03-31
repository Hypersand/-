package com.mysite.sbb.user;


import com.mysite.sbb.question.Question;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(@ModelAttribute UserCreateForm userCreateForm) {
        return "/user/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/user/signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "/user/signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getPassword1(),userCreateForm.getEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "/user/login_form";
    }

    @GetMapping("/question")
    public String userQuestionList(Principal principal, Model model) {
        SiteUser user = userService.getUser(principal.getName());
        List<Question> questions = user.getQuestion();
        model.addAttribute("questions", questions);
        model.addAttribute("user", user);

        return "/user/mypage_question";
    }

    @GetMapping("/answer")
    public String userAnswerList() {
        return "/user/mypage_answer";
    }

    @GetMapping("/comment")
    public String userCommentList() {
        return "/user/mypage_comment";
    }


    @PreAuthorize("isAnonymous()")
    @GetMapping("/forgotPassword")
    public String findPassword() {

        return "/user/forgot_password";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String email) {
        System.out.println("email  : " + email);
        MailForm mailForm = userService.createMailForm(email);
        userService.sendEmail(mailForm);

        return "redirect:/user/login";
    }

    @GetMapping("/updatePassword")
    public String updatePassword(PasswordUpdateForm passwordUpdateForm) {
        return "/user/update_password";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Validated PasswordUpdateForm passwordUpdateForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "/user/update_password";
        }

        if (!passwordUpdateForm.newPassword1.equals(passwordUpdateForm.newPassword2)) {
            return "/user/update_password";
        }

        SiteUser user = userService.getUser(principal.getName());
        userService.updatePassword(passwordUpdateForm.newPassword1, user);

        return "redirect:/user/login";
    }

}
