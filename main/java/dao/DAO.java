
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dto.ResultSetMapper;
import exception.BeemsSQLException;

public abstract class DAO {
    protected String url;
    protected String dbUser;
    protected String dbPass;

    public DAO() throws BeemsSQLException {
        try {
            // JDBCドライバのロード
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // データベース接続情報 localhost:3306/(データベース名)?...に注意
            url = "jdbc:mysql://localhost:3306/beems?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9";
            dbUser = "dbuser";
            dbPass = "pass";
            
        } catch (ClassNotFoundException e) {
            throw new BeemsSQLException("データベース接続エラー: " + e.getMessage(), e);
        }
    }

    protected <T extends ResultSetMapper<T>> List<T> mapResultSetToList(ResultSet rs, T mapper) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            T obj = mapper.fromResultSet(rs);
            list.add(obj);
        }
        return list;
    }

    protected <T extends ResultSetMapper<T>> Object executeSQL(String sql, T mapper, Object... params) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = con.prepareStatement(sql, 
                 sql.trim().toUpperCase().startsWith("INSERT") ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {
            
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param instanceof Integer) {
                    ps.setInt(i + 1, (Integer) param);
                } else if (param instanceof String) {
                    ps.setString(i + 1, (String) param);
                } else if (param instanceof Timestamp) {
                    ps.setTimestamp(i + 1, (Timestamp) param);
                } else if (param instanceof Boolean) {
                    ps.setBoolean(i + 1, (Boolean) param);  // ここを追加
                }
            }

            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                try (ResultSet rs = ps.executeQuery()) {
                    return mapResultSetToList(rs, mapper);
                }
            } else if (sql.trim().toUpperCase().startsWith("INSERT")) {
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("データベース操作が行われませんでした。");
                }
                // 主キーを取得
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // 主キーを返す
                    } else {
                        // 主キーが生成されない場合、入力したIDを返す
                        for (Object param : params) {
                            if (param instanceof Integer) {
                                return param; // 最初のInteger型のパラメータをIDと仮定して返す
                            }
                        }
                        throw new SQLException("主キーの取得に失敗しました。");
                    }
                }
            }else if (sql.trim().toUpperCase().startsWith("DELETE")) {
                int affectedRows = ps.executeUpdate();
                return affectedRows;  // 影響を受けた行数を返す
            } else {
                int affectedRows = ps.executeUpdate();
                return affectedRows;  // 影響を受けた行数を返す
            }
        } catch (SQLException e) {
            throw new SQLException("データベースエラー: " + e.getMessage(), e);
        }
    }
}
