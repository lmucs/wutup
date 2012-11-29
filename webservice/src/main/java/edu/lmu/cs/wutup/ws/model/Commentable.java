package edu.lmu.cs.wutup.ws.model;

//import java.util.ArrayList;
import java.util.List;

public interface Commentable {

    void addComment(Comment comment);

    void removeComment(int commentNumber);

    void updateComment(int commentNumber, Comment comment);

    List<Comment> getComments();
}
