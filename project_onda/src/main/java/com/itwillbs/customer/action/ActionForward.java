package com.itwillbs.customer.action;

public class ActionForward {

	// 이동(경로정보, 이동방식 담아서 오면 이동)
	private String path; // 이동경로
	private boolean isRedirect; // 이동방식
	// true -> sendRedirect() 방식
	// false -> foward() 방식
	
	//alt shift s -> r
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
		

	

}
