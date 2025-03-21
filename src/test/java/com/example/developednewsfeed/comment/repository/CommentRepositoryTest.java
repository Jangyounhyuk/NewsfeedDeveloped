//package com.example.developednewsfeed.comment.repository;
//
//import com.example.developednewsfeed.comment.entity.Comment;
//import com.example.developednewsfeed.post.entity.Post;
//import com.example.developednewsfeed.user.entity.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class CommentRepositoryTest {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Test
//    void postId로_댓글리스트_조회_테스트() {
//        // given
//        Long postId = 1L;
//
//        User user = User.builder().email("test@test.com").build();
//
//        Post post = new Post();
//
//        Comment comment1 = Comment.builder().post(post).build();
//        Comment comment2 = Comment.builder().post(post).build();
//        commentRepository.save(comment1);
//        commentRepository.save(comment2);
//
//        // when
//        List<Comment> result = commentRepository.findAllByPostId(postId);
//
//        // then
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals(comment1, result.get(0));
//        assertEquals(comment2, result.get(1));
//    }
//}