package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.*;

public interface CommentDao {

    void createComment(Comment c);

    Comment findCommentById(int id);
    List<Comment> findCommentsByUserId(int user);

    void updateComment(Comment c);

    void deleteComment(Comment c);

    int findNumberOfComments();
}
