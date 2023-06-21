package ua.havrysh;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

import static ua.havrysh.ChatMain.URL_CONN;

@WebServlet(name = "ChatServlet", urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {

    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            conn = DriverManager.getConnection(URL_CONN);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        ArrayList<ChatEntry> results = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String sql = "SELECT id, message, author, created_at FROM chat_history";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String message = rs.getString("message");
                String author = rs.getString("author");
                Timestamp createdAt = rs.getTimestamp("created_at");
                results.add(new ChatEntry(id, message, author, createdAt.toInstant()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        results.forEach(result -> out.println(result.toString()));
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String message = request.getParameter("message");
        String author = request.getParameter("author");

        try {
            String sql = "INSERT INTO chat_history VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, message);
            statement.setString(2, author);
            statement.setTimestamp(3, Timestamp.from(Instant.now()));
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
