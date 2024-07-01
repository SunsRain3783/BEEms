package dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RegisterLog extends SearchLog {

    public RegisterLog() {}

    public RegisterLog(int termId, int userId) {
        super(termId, userId);
    }

    public RegisterLog(int termId, int userId, Timestamp searchDate) {
        super(termId, userId, searchDate);
    }

    @Override
    public RegisterLog fromResultSet(ResultSet rs) throws SQLException {
        return new RegisterLog(
            rs.getInt("term_id"),
            rs.getInt("user_id"),
            rs.getTimestamp("search_date")
        );
    }
}

