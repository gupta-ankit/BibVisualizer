package com.visulytic.bibviz.model.graphdb;

public interface Node {
	public String getLabel();
	public void setLabel(String label);
	public void setId(long id);
	public long getId();
}
