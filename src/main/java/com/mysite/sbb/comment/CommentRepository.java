package com.mysite.sbb.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT co FROM Comment co ORDER BY co.createDate DESC LIMIT 10")
    List<Comment> findRecentTenComment();
}
