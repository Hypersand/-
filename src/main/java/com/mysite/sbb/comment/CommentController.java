package com.mysite.sbb.comment;

import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final QuestionService questionService;

    private final CommentService commentService;
    private final UserService userService;


    @PostMapping("/create/{id}")
    public String create(@PathVariable Long id, @ModelAttribute @Validated CommentForm commentForm, BindingResult bindingResult, Model model, Principal principal) {
        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question/question_detail";
        }

        commentService.create(question, commentForm.getContent(), user);

        return "redirect:/question/detail/" + question.getId();
    }

}
