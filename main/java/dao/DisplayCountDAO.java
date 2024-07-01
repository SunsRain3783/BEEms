package dao;

import java.sql.SQLException;
import java.util.List;

import dto.DisplayCount;
import exception.BeemsSQLException;

public class DisplayCountDAO extends DAO {

    public DisplayCountDAO() throws BeemsSQLException {
        super();
    }

    public DisplayCount searchDisplayCount(int id) throws BeemsSQLException {
        String sql = "SELECT * FROM display_counts WHERE id = ?";
        try {
            return ((List<DisplayCount>)executeSQL(sql, new DisplayCount(), id)).get(0);
        } catch (SQLException e) {
            throw new BeemsSQLException("DisplayCount検索エラー: " + e.getMessage(), e);
        }
    }

    public int updateDisplayCount(int id, int count) throws BeemsSQLException {
        String sql = "UPDATE display_counts SET count = ? WHERE id = ?";
        try {
            return (int) executeSQL(sql, new DisplayCount(), count, id);
        } catch (SQLException e) {
            throw new BeemsSQLException("DisplayCount更新エラー: " + e.getMessage(), e);
        }
    }
}


/*package edu.beems.dao;

import java.sql.SQLException;
import java.util.List;

import edu.beems.dto.DisplayCount;
import edu.beems.exception.beemsSQLException;

public class DisplayCountDAO extends DAO {

    public DisplayCountDAO() throws beemsSQLException {
        super();
    }

    public DisplayCount searchDisplayCount(int id) throws beemsSQLException {
        String sql = "SELECT * FROM display_counts WHERE id = ?";
        try {
            List<DisplayCount> displayCounts = (List<DisplayCount>) executeSQL(sql, new DisplayCount(), id);
            if (displayCounts.isEmpty()) {
                throw new beemsSQLException("DisplayCountが見つかりません。");
            }
            return displayCounts.get(0);
        } catch (SQLException e) {
            throw new beemsSQLException("DisplayCount検索エラー: " + e.getMessage(), e);
        }
    }

    public int updateDisplayCount(int id, int count) throws beemsSQLException {
        String sql = "UPDATE display_counts SET count = ? WHERE id = ?";
        try {
            return (int) executeSQL(sql, null, count, id);
        } catch (SQLException e) {
            throw new beemsSQLException("DisplayCount更新エラー: " + e.getMessage(), e);
        }
    }
}
*/