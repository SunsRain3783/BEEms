package dao;

import java.sql.SQLException;
import java.util.List;

import dto.SearchLog;
import dto.Term;
import exception.BeemsSQLException;

public class SearchLogDAO extends DAO {

    public SearchLogDAO() throws BeemsSQLException {
        super();
    }

    // 検索ログを作成し、作成日時を取得するメソッド
    public void createSearchLog(SearchLog log) throws BeemsSQLException {
        String sql = "INSERT INTO search_logs (term_id, user_id) VALUES (?, ?)";
        try {
            int id = (int) executeSQL(sql, null, log.getTermId(), log.getUserId());
        } catch (SQLException e) {
            throw new BeemsSQLException("検索ログ作成エラー: " + e.getMessage(), e);
        }
    }
    
    //検索履歴の削除
    public void deleteSearchLog(int termId) throws BeemsSQLException {
        String sql = "DELETE FROM search_logs WHERE term_id = ?";
        try {
            executeSQL(sql, null, termId);
        } catch (SQLException e) {
            throw new BeemsSQLException("検索ログ削除エラー: " + e.getMessage(), e);
        }
    }

    public List<SearchLog> searchSearchLogsByUserId(int userId) throws BeemsSQLException {
        String sql = "SELECT * FROM search_logs WHERE user_id = ? ORDER BY search_date DESC";
        try {
            List<SearchLog> searchLogs = (List<SearchLog>) executeSQL(sql, new SearchLog(), userId);
            if (searchLogs.isEmpty()) {
                throw new BeemsSQLException("検索ログが見つかりませんでした。");
            }
            return searchLogs;
        } catch (SQLException e) {
            throw new BeemsSQLException("検索ログ検索エラー: " + e.getMessage(), e);
        }
    }

    public List<Term> searchTerms(int userId) throws BeemsSQLException {
        String sql = "SELECT * FROM search_logs WHERE user_id = ? ORDER BY search_date DESC";
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

    // 全ての検索ログを取得するメソッド
    public List<SearchLog> getAllSearchLogs() throws BeemsSQLException {
        String sql = "SELECT * FROM search_logs";
        try {
            return (List<SearchLog>) executeSQL(sql, new SearchLog());
        } catch (SQLException e) {
            throw new BeemsSQLException("全検索ログ取得エラー: " + e.getMessage(), e);
        }
    }
    
    public boolean searchLogExists(int termId, int userId) throws BeemsSQLException {
        String sql = "SELECT * FROM search_logs WHERE term_id = ? AND user_id = ?";
        try {
            List<SearchLog> searchLogs = (List<SearchLog>) executeSQL(sql, new SearchLog(), termId, userId);
            return !searchLogs.isEmpty();
        } catch (SQLException e) {
            throw new BeemsSQLException("検索ログ確認エラー: " + e.getMessage(), e);
        }
    }
}

