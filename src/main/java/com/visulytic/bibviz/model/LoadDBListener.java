package com.visulytic.bibviz.model;

import com.visulytic.bibviz.view.LoadDBEvent;

public interface LoadDBListener {

	public void dbLoaded(LoadDBEvent event);
}
