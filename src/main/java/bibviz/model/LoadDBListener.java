package bibviz.model;

import bibviz.visualization.LoadDBEvent;

public interface LoadDBListener {

	public void dbLoaded(LoadDBEvent event);
}
