package edu.lmu.cs.wutup.ws.model;

import org.joda.time.DateTime;

public class Comment {
    private int id;
    private String body;
    private DateTime timestamp;
    
	public Comment() {
        
    }
    
    public Comment(String body, DateTime timestamp) {
        this.body = body;
        this.timestamp = timestamp;
     }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

}