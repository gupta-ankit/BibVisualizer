package com.visulytic.bibviz.model.graphdb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PublicationNode implements Node, Serializable{

	private String label;
	private String key;
	private long id;
	private Map<String, String> properties = new HashMap<>();
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setProperty(String property, String value){
		this.properties.put(property, value);
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}
}
