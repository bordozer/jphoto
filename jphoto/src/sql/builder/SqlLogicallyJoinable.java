package sql.builder;

public interface SqlLogicallyJoinable {

	String join();

	String getJoinOperator();
}
