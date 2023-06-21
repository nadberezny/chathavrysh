package ua.havrysh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ChatMain {

    public static final String URL_CONN = "jdbc:derby:havrysh;create=true";

    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection(URL_CONN);
        Statement statement = conn.createStatement();
        String sql = "CREATE TABLE chat_history (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1), message VARCHAR(255), author VARCHAR(255), created_at TIMESTAMP)";
        statement.execute(sql);
    }
}
