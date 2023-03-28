package com.mysite.sbb.user;


import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final QuestionService questionService;

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
}
