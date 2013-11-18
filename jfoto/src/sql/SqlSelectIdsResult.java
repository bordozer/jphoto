package sql;

import java.util.List;

public class SqlSelectIdsResult {

	private List<Integer> ids;
	private int recordQty;

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds( final List<Integer> ids ) {
		this.ids = ids;
	}

	public int getRecordQty() {
		return recordQty;
	}

	public void setRecordQty( final int recordQty ) {
		this.recordQty = recordQty;
	}
}
