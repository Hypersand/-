package com.mysite.sbb.comment;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Question question, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setQuestion(question);
        comment.setCreateDate(LocalDateTime.now());
        comment.setAuthor(author);
        commentRepository.save(comment);

        return comment;
    }

    public List<Comment> getRecentComment() {
        return commentRepository.findRecentTenComment();
    }

}
