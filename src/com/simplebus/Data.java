package com.simplebus;

public class Data {
	public String title;
	public String content;
	public int status;
	public Data(String title,String content){
		this.title = title;
		this.content = content;
		this.status = 0;
	}
	public Data(String title,String content, int status){
		this.title = title;
		this.content = content;
		this.status = 1;
	}
}
