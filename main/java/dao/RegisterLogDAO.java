package dao;

import java.sql.SQLException;
import java.util.List;

import dto.RegisterLog;
import dto.Term;
import exception.BeemsSQLException;

public class RegisterLogDAO extends DAO {

    public RegisterLogDAO() throws BeemsSQLException {
        super();
    }

    // 検索ログを作成し、作成日時を取得するメソッド
    public void createRegisterLog(RegisterLog log) throws BeemsSQLException {
        String sql = "INSERT INTO register_logs (term_id, user_id) VALUES (?, ?)";
        try {
            int id = (int) executeSQL(sql, null, log.getTermId(), log.getUserId());
        } catch (SQLException e) {
            throw new BeemsSQLException("検索ログ作成エラー: " + e.getMessage(), e);
        }
    }

    public List<RegisterLog> searchRegisterLogsByUserId(int userId) throws BeemsSQLException {
        String sql = "SELECT * FROM register_logs WHERE user_id = ? ORDER BY search_date DESC";
        try {
            List<RegisterLog> searchlogs = (List<RegisterLog>) executeSQL(sql, new RegisterLog(), userId);
            if (searchlogs.isEmpty()) {
                throw new BeemsSQLException("あなたが登録した用語");
            }
            return searchlogs;
        } catch (SQLException e) {
            throw new BeemsSQLException("検索ログ検索エラー: " + e.getMessage(), e);
        }
    }


    public List<Term> searchTerms(int userId) throws BeemsSQLException {
        String sql = "SELECT * FROM register_logs WHERE user_id = ? AND request_date IS NOT NULL";
        try {
            List<Term> terms = (List<Term>) executeSQL(sql, new Term(), userId);
            if (terms.isEmpty()) {
                throw new BeemsSQLException("検索ログが見つかりませんでした。");
            }
            return terms;
        } catch (SQLException e) {
            throw new BeemsSQLException("検索用語検索エラー: " + e.getMessage(), e);
        }
    }
    
    //登録用語の削除
    public void deleteRegisterLog(int termId) throws BeemsSQLException {
        String sql = "DELETE FROM register_logs WHERE term_id = ?";
        try {
            executeSQL(sql, null, termId);
        } catch (SQLException e) {
            throw new BeemsSQLException("登録ログ削除エラー: " + e.getMessage(), e);
        }
    }


    // 全ての検索ログを取得するメソッド
    public List<RegisterLog> getAllSearchLogs() throws BeemsSQLException {
        String sql = "SELECT * FROM register_logs";
        try {
            return (List<RegisterLog>) executeSQL(sql, new RegisterLog());
        } catch (SQLException e) {
            throw new BeemsSQLException("全検索ログ取得エラー: " + e.getMessage(), e);
        }
    }
}

