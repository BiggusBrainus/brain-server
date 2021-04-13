package at.htlkaindorf.bigbrain.server.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author m4ttm00ny
 */

public class DB_Cached_Connection {
    private Queue<Statement> statementQueue = new LinkedList<>();
    private Connection conn;

    public DB_Cached_Connection(Connection conn) {
        this.conn = conn;
    }

    public Statement getStatement() throws SQLException {
        if (conn == null) {
            throw new RuntimeException("Not connected to DB!");
        }
        if (!statementQueue.isEmpty()) {
            return statementQueue.poll();
        }
        return conn.createStatement();
    }

    public void releaseStatement(Statement statement) {
        if (conn == null) {
            throw new RuntimeException("Not connected to DB!");
        }
        statementQueue.offer(statement);
    }

    public void close() throws SQLException {
        while (!statementQueue.isEmpty()) {
            Statement st = statementQueue.poll();
            st.close();
        }
        conn.close();
    }
}
