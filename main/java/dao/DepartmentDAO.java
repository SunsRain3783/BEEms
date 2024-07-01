package dao;

import java.sql.SQLException;
import java.util.List;

import dto.Department;
import exception.BeemsSQLException;

public class DepartmentDAO extends DAO{
    private String url;
    private String dbUser;
    private String dbPass;

    public DepartmentDAO() throws BeemsSQLException {
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Database connection details
            url = "jdbc:mysql://localhost:3306/beems?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9";
            dbUser = "dbuser";
            dbPass = "pass";
        } catch (ClassNotFoundException e) {
            throw new BeemsSQLException("データベース接続エラー: " + e.getMessage(), e);
        }
    }

    // CRUD
    public Department createDepartment(Department department) throws BeemsSQLException {
        String sql = "INSERT INTO departments (department_name) VALUES (?)";
        try {
            int id = (int) executeSQL(sql, null, department.getDepartmentName());
            department.setId(id);
            return department;
        } catch (SQLException e) {
            throw new BeemsSQLException("部門追加エラー: " + e.getMessage(), e);
        }
    }

    public Department searchDepartmentByName(String departmentName) throws BeemsSQLException {
        String sql = "SELECT * FROM departments WHERE department_name = ?";
        try {
            List<Department> departments = (List<Department>) executeSQL(sql, new Department(), departmentName);
            if (departments.isEmpty()) {
                throw new BeemsSQLException("指定された部門が見つかりません。");
            }
            return departments.get(0);
        } catch (SQLException e) {
            throw new BeemsSQLException("部門検索エラー: " + e.getMessage(), e);
        }
    }

    public Department searchDepartmentById(int id) throws BeemsSQLException {
        String sql = "SELECT * FROM departments WHERE id = ?";
        try {
            List<Department> departments = (List<Department>) executeSQL(sql, new Department(), id);
            if (departments.isEmpty()) {
                throw new BeemsSQLException("指定された部門が見つかりません。");
            }
            return departments.get(0);
        } catch (SQLException e) {
            throw new BeemsSQLException("部門検索エラー: " + e.getMessage(), e);
        }
    }

    public List<Department> searchAllDepartments() throws BeemsSQLException {
        String sql = "SELECT * FROM departments";
        try {
            return (List<Department>) executeSQL(sql, new Department());
        } catch (SQLException e) {
            throw new BeemsSQLException("全ての部門検索エラー: " + e.getMessage(), e);
        }
    }

    public int updateDepartment(Department department) throws BeemsSQLException {
        String sql = "UPDATE departments SET department_name = ? WHERE id = ?";
        try {
            return (int) executeSQL(sql, null, department.getDepartmentName(), department.getId());
        } catch (SQLException e) {
            throw new BeemsSQLException("部門更新エラー: " + e.getMessage(), e);
        }
    }

    public int deleteDepartment(int id) throws BeemsSQLException {
        String sql = "DELETE FROM departments WHERE id = ?";
        try {
            return (int) executeSQL(sql, null, id);
        } catch (SQLException e) {
            throw new BeemsSQLException("部門削除エラー: " + e.getMessage(), e);
        }
    }
}

