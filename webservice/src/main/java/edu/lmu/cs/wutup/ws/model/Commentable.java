package edu.lmu.cs.wutup.ws.model;

import java.util.ArrayList;

public interface Commentable {

    public void addComment(Comment comment);

    public void removeComment(int commentNumber);

    public void updateComment(int commentNumber, Comment comment);

    public ArrayList<Comment> getComments();

}
