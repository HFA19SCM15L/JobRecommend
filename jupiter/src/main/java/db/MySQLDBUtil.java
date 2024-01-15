package db;

public class MySQLDBUtil {
	private static final String INSTANCE = "xxxx";
	private static final String PORT_NUM = "3306";
	public static final String DB_NAME = "xxxx";
	private static final String USERNAME = "xxxx";
	private static final String PASSWORD = "xxxx";
	public static final String URL = "jdbc:mysql://"
	        + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
	        + "?user=" + USERNAME
			+ "&password=" + PASSWORD + "&autoReconnect=true&serverTimezone=UTC";
}