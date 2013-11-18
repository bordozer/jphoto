package sql;

import core.interfaces.BaseEntity;

import java.util.List;

public class SqlSelectResult<T extends BaseEntity> {

	private List<T> items;
	private int recordQty;

	public List<T> getItems() {
		return items;
	}

	public void setItems( List<T> items ) {
		this.items = items;
	}

	public int getRecordQty() {
		return recordQty;
	}

	public void setRecordQty( int recordQty ) {
		this.recordQty = recordQty;
	}
}
