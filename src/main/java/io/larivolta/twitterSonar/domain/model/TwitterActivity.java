package io.larivolta.twitterSonar.domain.model;

import java.util.Date;

public class TwitterActivity {

	private String screen_name;
	private Date created_at;
	private String text;
//TODO	private String activityType; //TR, twitt, reply, like
	
	public TwitterActivity(String screen_name, Date created_at, String text) {
		super();
		this.screen_name = screen_name;
		this.created_at = created_at;
		this.text = text;
	}	
	
	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
