package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;

public interface CommentDao {

    void addComment(Integer eventId, Comment comment);

    void updateComment(Integer commentId, Comment comment);

    List<Comment> findComments(int commentableId, PaginationData pagination);

    void deleteComment(int commentableId, int commentId);
}
