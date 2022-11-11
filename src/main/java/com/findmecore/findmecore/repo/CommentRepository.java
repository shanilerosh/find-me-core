package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Comment;
import com.findmecore.findmecore.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ShanilErosh
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
