package com.smartContactManager.helper;

import com.smartContactManager.entities.User;

public class ObjectWrapper {
    private User fromView;
    private User toView;
	public User getFromView() {
		return fromView;
	}
	public void setFromView(User fromView) {
		this.fromView = fromView;
	}
	public User getToView() {
		return toView;
	}
	public void setToView(User toView) {
		this.toView = toView;
	}
    
    // Getters and setters for fromView and toView
}
