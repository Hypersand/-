package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Page<Answer> findAllByQuestion(Question question, Pageable pageable);

    @Query(value = "SELECT an FROM Answer an ORDER BY an.createDate DESC LIMIT 10")
    List<Answer> findRecentTenAnswer();

}
