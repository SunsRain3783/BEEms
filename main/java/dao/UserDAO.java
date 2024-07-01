package dao;

import java.sql.SQLException;
import java.util.List;

import dto.User;
import exception.BeemsSQLException;

public class UserDAO extends DAO {

    public UserDAO() throws BeemsSQLException {
        super();
    }

    //ユーザーの新規登録
    public int createUser(User user) throws BeemsSQLException {
        String sql = "INSERT INTO users (id, username, password, department_id) " +
                     "VALUES (?, ?, ?, ?)";
        try {
            return (int) executeSQL(sql, null, user.getId(), user.getUsername(), user.getPassword(), user.getDepartmentName());
        } catch (SQLException e) {
            throw new BeemsSQLException("社員番号が登録されています");
        }
    }

    //ログイン認証
    public User searchUser(int id) throws BeemsSQLException {
        String sql = "SELECT u.id, u.username, u.password, d.department_name " +
                     "FROM users u JOIN departments d ON u.department_id = d.id " +
                     "WHERE u.id = ?";
        try {
            List<User> users = (List<User>) executeSQL(sql, new User(), id);
            if (!users.isEmpty()) {
                return users.get(0);
            } else {
                throw new BeemsSQLException("ユーザーが見つかりません");
            }
        } catch (SQLException e) {
            throw new BeemsSQLException("ユーザー検索エラー: " + e.getMessage(), e);
        }
    }
    
    //ユーザーのアップデート
    public int updateUser(User user) throws BeemsSQLException {
        String sql = "UPDATE users SET username = ?, password = ?, department_id = (SELECT id FROM departments WHERE department_name = ?) WHERE id = ?";
        try {
            return (int) executeSQL(sql, null, user.getUsername(), user.getPassword(), user.getDepartmentName(), user.getId());
        } catch (SQLException e) {
            throw new BeemsSQLException("ユーザー更新エラー: " + e.getMessage(), e);
        }
    }

    
    public int deleteUser(String username) throws BeemsSQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        try {
            return (int) executeSQL(sql, null, username);
        } catch (SQLException e) {
        	throw new BeemsSQLException("ユーザー削除エラー: " + e.getMessage(), e);
        }
    }
}