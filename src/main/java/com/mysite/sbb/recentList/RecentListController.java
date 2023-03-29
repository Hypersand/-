package com.mysite.sbb.recentList;


import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recent_list")
@RequiredArgsConstructor
public class RecentListController {

    private final AnswerService answerService;
    private final CommentService commentService;

    @GetMapping("/answer")
    public String recentAnswerList(Model model) {
        List<Answer> recentAnswers = answerService.getRecentAnswer();
        model.addAttribute("recentAnswers", recentAnswers);

        return "/recentList/recentList_answer";
    }

    @GetMapping("/comment")
    public String recentCommentList(Model model) {
        List<Comment> recentComments = commentService.getRecentComment();

        model.addAttribute("recentComments", recentComments);

        return "/recentList/recentList_comment";
    }

}
