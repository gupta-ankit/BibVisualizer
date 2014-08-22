package com.visulytic.bibviz.model.graphdb;

import java.io.Serializable;

public class DefaultEdge implements Edge, Serializable{

	private String type;

	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void setType(String type){
		this.type = type;
	}

}
