package dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Department implements ResultSetMapper<Department> {
    private int id;
    private String departmentName; // 部門名, ユーザが用語登録時に指定するからidあるが一意になる

    // デフォルトコンストラクタ
    public Department() {
    }

    // パラメータ付きコンストラクタ
    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public Department(int id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    // getter&setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public Department fromResultSet(ResultSet rs) throws SQLException {
        return new Department(
            rs.getInt("id"),
            rs.getString("department_name")
        );
    }
}

