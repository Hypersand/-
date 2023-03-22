package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	@DisplayName("DB에 데이터 저장하기")
	void testJpa1() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해 알고 싶습니다.");
		q1.setCreateData(LocalDateTime.now());
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateData(LocalDateTime.now());
		questionRepository.save(q2);
	}

	@Test
	@DisplayName("데이터 조회하기 - 리스트")
	void testJpa2() {
		List<Question> questionList = questionRepository.findAll();
		assertThat(questionList.size()).isEqualTo(2);

		Question question = questionList.get(0);
		assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
	}

	@Test
	@DisplayName("데이터 조회하기 - id")
	void testJpa3() {
		Optional<Question> id = questionRepository.findById(1L);
		if (id.isPresent()) {
			Question question = id.get();
			assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
		}
	}

	@Test
	@DisplayName("데이터 조회하기 - subject")
	void testJpa4() {
		Question question = questionRepository.findBySubject("sbb가 무엇인가요?");
		assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
	}

	@Test
	@DisplayName("데이터 조회하기 - subject, content")
	void testJpa5() {
		Question question = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해 알고 싶습니다.");
		assertThat(question.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("데이터 조회하기 - subject 내용 포함")
	void testJpa6() {
		List<Question> questionList = questionRepository.findBySubjectLike("sbb%");
		Question question = questionList.get(0);
		assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
	}

	@Test
	@DisplayName("데이터 수정하기")
	void testJpa7() {
		Optional<Question> oq = questionRepository.findById(1L);
		Question question = oq.get();
		question.setSubject("수정된 제목");
		questionRepository.save(question);
		assertThat(question.getSubject()).isEqualTo("수정된 제목");
	}

	@Test
	@DisplayName("데이터 삭제하기")
	void testJpa8() {
		assertThat(questionRepository.count()).isEqualTo(2);
		Optional<Question> oq = questionRepository.findById(1L);
		Question question = oq.get();
		questionRepository.delete(question);
		assertThat(questionRepository.count()).isEqualTo(1);
	}

	@Test
	@DisplayName("답변 데이터 생성 후 저장하기")
	void testJpa9() {
		Optional<Question> oq = this.questionRepository.findById(2L);
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);

	}

	@Test
	@DisplayName("답변 조회하기")
	void testJpa10() {
		Optional<Answer> oa = answerRepository.findById(1L);
		Answer answer = oa.get();
		assertThat(answer.getQuestion().getId()).isEqualTo(2);
	}

	@Test
	@DisplayName("질문에서 답변 리스트 조회하기")
	@Transactional
	void testJpa11() {
		Optional<Question> oq = questionRepository.findById(2L);
		Question question = oq.get();
		List<Answer> answerList = question.getAnswerList();
		assertThat(answerList.size()).isEqualTo(1);
		assertThat(answerList.get(0).getContent()).isEqualTo("네 자동으로 생성됩니다.");
	}

//	@Test
//	@DisplayName("테스트 게시물 300개 생")
//	void testJpa12() {
//		for (int i = 1; i <= 300; i++) {
//			String subject = String.format("테스트 데이터입니다 : " + "[" + i + "]");
//			String content = "내용무";
//			questionService.create(subject, content);
//		}
//	}

}
