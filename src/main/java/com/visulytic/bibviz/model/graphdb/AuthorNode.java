package com.visulytic.bibviz.model.graphdb;

import java.io.Serializable;

public class AuthorNode implements Node, Serializable {
	private String label;
	private long id;

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
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
