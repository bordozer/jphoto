package sql.builder;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class SqlConditionList implements SqlLogicallyJoinable {

	private List<SqlLogicallyJoinable> logicallyItems = newArrayList();

	protected SqlConditionList( final SqlLogicallyJoinable... logicallyItems ) {
//		this.logicallyItems.add( condition );

		if ( logicallyItems != null ) {
			Collections.addAll( this.logicallyItems, logicallyItems );
		}
	}

	@Override
	public String join() {
		return SqlSelectUtils.joinWithBrackets( logicallyItems, getJoinOperator() );
	}

	public void setLogicallyItems( final List<SqlLogicallyJoinable> logicallyItems ) {
		this.logicallyItems = logicallyItems;
	}

	public List<SqlLogicallyJoinable> getLogicallyItems() {
		return logicallyItems;
	}
}
