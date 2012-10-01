package edu.lmu.cs.wutup.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.CommentDao;
import edu.lmu.cs.wutup.ws.model.Comment;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao CommentDao;

    @Override
    public void createComment(Comment c) {
        CommentDao.createComment(c);
    }

    @Override
    public void updateComment(Comment c) {
        CommentDao.updateComment(c);
    }

    @Override
    public Comment findCommentById(int id) {
        return CommentDao.findCommentById(id);
    }

    /*@Override
    public List<Comment> findCommentsByUserId(int user) {
        return CommentDao.findCommentsByUserId(user);
    }*/

    @Override
    public void deleteComment(Comment c) {
        CommentDao.deleteComment(c);
    }
}
