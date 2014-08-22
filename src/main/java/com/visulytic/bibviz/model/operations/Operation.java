package com.visulytic.bibviz.model.operations;

import java.util.HashMap;

public class Operation {
	
	private String name;
	private HashMap<String, Object> parameters;
	
	public Operation(){
		parameters = new HashMap<>();
	}
	
	public void setName(String name){
		this.name = name;
	}

}
