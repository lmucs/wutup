package edu.lmu.cs.wutup.ws.service;

import edu.lmu.cs.wutup.ws.model.Comment;

public interface CommentService {

    void createComment(Comment c);
    void updateComment(Comment c);
    Comment findCommentById(int id);
    void deleteComment(Comment c);
    //List<Comment> findCommentsByUserId(int user);
}
