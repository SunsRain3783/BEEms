package dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements ResultSetMapper<User> {
    private int id;
    private String username;
    private String password;
    private String departmentName;

    // デフォルトコンストラクタ
    public User() {
    }

    // パラメータ付きコンストラクタ
    public User(int id, String username, String password, String departmentName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.departmentName = departmentName;
    }

    // パラメータ付きコンストラクタ
    public User(String username, String password, String departmentName) {
        this.username = username;
        this.password = password;
        this.departmentName = departmentName;
    }

    // getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public User fromResultSet(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("department_name")
        );
    }
}

