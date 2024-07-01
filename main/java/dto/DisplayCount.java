package dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayCount implements ResultSetMapper<DisplayCount> {
    private int id;
    private int count;

    // デフォルトコンストラクタ
    public DisplayCount() {
    }

    // パラメータ付きコンストラクタ
    public DisplayCount(int id, int count) {
        this.id = id;
        this.count = count;
    }

    // getter&setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public DisplayCount fromResultSet(ResultSet rs) throws SQLException {
        return new DisplayCount(
            rs.getInt("id"),
            rs.getInt("count")
        );
    }
}

