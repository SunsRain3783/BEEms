package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Department;
import dto.Term;
import exception.BeemsSQLException;

public class TermDAO extends DAO {

	public TermDAO() throws BeemsSQLException {
		super();
	}

	//キーワード検索
	public List<Term> searchForKeyword(String searchText, boolean requestDateNull) throws BeemsSQLException {
	    // ローカル変数としての条件フラグの定義 ビットマスク(ビットフラグ)
	    final int EXACT_MATCH = 0b0001; // 完全一致
	    final int PHRASE_MATCH = 0b0010; // フレーズ一致
	    final int FILTERED_MATCH = 0b0100; // 絞り込み一致
	    final int PARTIAL_MATCH = 0b1000; // 部分一致
	 
	    int conditions = EXACT_MATCH | PARTIAL_MATCH;
	 
	    List<Integer> termIds = new ArrayList<>();
	    List<String> queries = new ArrayList<>();
	    List<String> parameters = new ArrayList<>();
	 
	    String requestDateCondition = requestDateNull ? "IS NULL" : "IS NOT NULL";
	 
	    // 条件に基づいてクエリを動的に追加
	    if ((conditions & EXACT_MATCH) != 0) {
	        queries.add("SELECT DISTINCT id FROM terms WHERE (term_name = ? OR abbreviation = ? OR reading = ?) AND request_date " + requestDateCondition);
	        parameters.add(searchText);
	        parameters.add(searchText);
	        parameters.add(searchText);
	    }
	    if ((conditions & PHRASE_MATCH) != 0) {
	        queries.add("SELECT DISTINCT id FROM terms WHERE (term_name LIKE ? OR abbreviation LIKE ? OR reading LIKE ?) AND request_date " + requestDateCondition);
	        parameters.add("%" + searchText + "%");
	        parameters.add("%" + searchText + "%");
	        parameters.add("%" + searchText + "%");
	    }
	    if ((conditions & FILTERED_MATCH) != 0) {
	        String filteredText = "%" + searchText.replace(" ", "%") + "%";
	        queries.add("SELECT DISTINCT id FROM terms WHERE (term_name LIKE ? OR abbreviation LIKE ? OR reading LIKE ?) AND request_date " + requestDateCondition);
	        parameters.add(filteredText);
	        parameters.add(filteredText);
	        parameters.add(filteredText);
	    }
	    if ((conditions & PARTIAL_MATCH) != 0) {
	        String[] words = searchText.split(" ");
	        for (String word : words) {
	            queries.add("SELECT DISTINCT id FROM terms WHERE (term_name LIKE ? OR abbreviation LIKE ? OR reading LIKE ?) AND request_date " + requestDateCondition);
	            parameters.add("%" + word + "%");
	            parameters.add("%" + word + "%");
	            parameters.add("%" + word + "%");
	        }
	    }
	 
	    // 用語の説明で一致
	    if ((conditions & PARTIAL_MATCH) != 0) {
	        String[] words = searchText.split(" ");
	        for (String word : words) {
	            queries.add("SELECT DISTINCT id FROM terms WHERE definition LIKE ? AND request_date " + requestDateCondition);
	            parameters.add("%" + word + "%");
	        }
	    }
	 
	    // 動的に生成されたクエリを結合
	    String query = String.join(" UNION ", queries);
	 
	    // クエリの内容をデバッグ出力
	    System.out.println("Generated SQL Query: " + query);
	 
	    try (Connection con = DriverManager.getConnection(url, dbUser, dbPass);
	         PreparedStatement preparedStatement = con.prepareStatement(query)) {
	 
	        // パラメータを設定
	        int index = 1;
	        for (String param : parameters) {
	            preparedStatement.setString(index++, param);
	        }
	 
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                termIds.add(resultSet.getInt("id"));
	            }
	        }
	    } catch (SQLException e) {
	        throw new BeemsSQLException("用語ID検索エラー: " + e.getMessage(), e);
	    }
	 
	    // 用語IDに基づいて用語オブジェクトのリストを取得
	    TermDAO termDAO = new TermDAO();
	    List<Term> terms = new ArrayList<>();
	    for (int id : termIds) {
	        terms.add(termDAO.searchTermById(id));
	    }
	 
	    return terms;
	}

	//注目用語検索
	public List<Term> searchKeyTerms() throws BeemsSQLException {
        String countSql = "SELECT count FROM display_counts WHERE id = 1";
        String sql = "SELECT * FROM terms WHERE key_term = 1 LIMIT ?";
        int displayCount = 0;
 
        try (Connection con = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement countStmt = con.prepareStatement(countSql);
             ResultSet rs = countStmt.executeQuery()) {
            
            // displayCountの値を取得
            if (rs.next()) {
                displayCount = rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new BeemsSQLException("display_countテーブルからのcount値の取得エラー: " + e.getMessage(), e);
        }
 
        // メインクエリの実行
        try {
            return (List<Term>) executeSQL(sql, new Term(), displayCount);
        } catch (SQLException e) {
            throw new BeemsSQLException("注目用語検索エラー: " + e.getMessage(), e);
        }
    }
	
	//注目用語の検索(全件)
	public List<Term> searchKeyTermsAll() throws BeemsSQLException {
        String sql = "SELECT * FROM terms WHERE key_term = 1";
        try {
            return (List<Term>) executeSQL(sql, new Term());
        } catch (SQLException e) {
            throw new BeemsSQLException("注目用語検索エラー: " + e.getMessage(), e);
        }
    }

	//新着用語検索(一か月以内)
	public List<Term> searchNewTerms() throws BeemsSQLException {
	    String countSql = "SELECT count FROM display_counts WHERE id = 1";
	    String sql = "SELECT * FROM terms WHERE created_date >= NOW() - INTERVAL 1 MONTH AND request_date IS NULL ORDER BY created_date DESC LIMIT ?";
	    int displayCount = 0;
	    // display_countsから表示数(count)を取得
	    try (Connection con = DriverManager.getConnection(url, dbUser, dbPass);
	         PreparedStatement countStmt = con.prepareStatement(countSql);
	         ResultSet rs = countStmt.executeQuery()) {
	 
	        // displayCountの値を取得
	        if (rs.next()) {
	            displayCount = rs.getInt("count");
	        }
	    } catch (SQLException e) {
	        throw new BeemsSQLException("display_countテーブルからのcount値の取得エラー: " + e.getMessage(), e);
	    }
	 
	    // メインクエリの実行
	    try {
	        return (List<Term>) executeSQL(sql, new Term(), displayCount);
	    } catch (SQLException e) {
	        throw new BeemsSQLException("新着用語検索エラー: " + e.getMessage(), e);
	    }
	}
	
	//新着用語の検索(全件)
	public List<Term> searchNewTermsAll() throws BeemsSQLException {
	    String sql = "SELECT * FROM terms WHERE created_date >= NOW() - INTERVAL 1 MONTH AND request_date IS NULL ORDER BY created_date DESC";
	    try {
	        return (List<Term>) executeSQL(sql, new Term());
	    } catch (SQLException e) {
	        throw new BeemsSQLException("新着用語検索エラー: " + e.getMessage(), e);
	    }
	}
	
	//リクエスト用語検索
	public List<Term> searchRequestTerms() throws BeemsSQLException {
	    String sql = "SELECT * FROM terms WHERE request_date IS NOT NULL ORDER BY request_date DESC";
	    try {
	        return (List<Term>) executeSQL(sql, new Term());
	    } catch (SQLException e) {
	        throw new BeemsSQLException("リクエスト用語検索エラー: " + e.getMessage(), e);
	    }
	}

	//削除依頼検索
	public List<Term> searchDeletionTerms() throws BeemsSQLException {
		String sql = "SELECT * FROM terms WHERE request_deletion = TRUE";
		try {
			return (List<Term>) executeSQL(sql, new Term());
		} catch (SQLException e) {
			throw new BeemsSQLException("リクエスト検索エラー: " + e.getMessage(), e);
		}
	}
	
	//社内用語の検索
	public List<Term> searchCompTerms() throws BeemsSQLException {
	    int startId = 1;
	    int endId = 10;
	 
	    String sql = "SELECT * FROM terms WHERE department_id BETWEEN ? AND ? AND request_date IS NULL";
	 
	    try {
	        return (List<Term>) executeSQL(sql, new Term(), startId, endId);
	    } catch (SQLException e) {
	        throw new BeemsSQLException("用語検索エラー: " + e.getMessage(), e);
	    }
	}
	
	//一般用語、各部門用語の検索
	public List<Term> searchDepTerms(int departmentId) throws BeemsSQLException {
	    String sql = "SELECT * FROM terms WHERE department_id = ? AND request_date IS NULL";

	    try {
	        return (List<Term>) executeSQL(sql, new Term(), departmentId);
	    } catch (SQLException e) {
	        throw new BeemsSQLException("用語検索エラー: " + e.getMessage(), e);
	    }
	}
	
	//頭文字の検索
	public List<Term> searchTermsByInitial(String initial) throws BeemsSQLException {
	    String sql = "SELECT * FROM terms WHERE (term_name LIKE ? OR abbreviation LIKE ? OR reading LIKE ?) AND request_date IS NULL";
	    try {
	        return (List<Term>) executeSQL(sql, new Term(), initial + "%", initial + "%", initial + "%");
	    } catch (SQLException e) {
	        throw new BeemsSQLException("用語検索エラー: " + e.getMessage(), e);
	    }
	}
    
    //アクセスランキングの取得
    public List<Term> searchByAccessRanking() throws BeemsSQLException {
		String countSql = "SELECT count FROM display_counts WHERE id = 1";
		String sql = "SELECT * FROM terms WHERE request_date IS NULL ORDER BY search_count DESC LIMIT ?";
		int displayCount = 0;

		try (Connection con = DriverManager.getConnection(url, dbUser, dbPass);
				PreparedStatement countStmt = con.prepareStatement(countSql);
				ResultSet rs = countStmt.executeQuery()) {

			// displayCountの値を取得
			if (rs.next()) {
				displayCount = rs.getInt("count");
			}
		} catch (SQLException e) {
			throw new BeemsSQLException("display_countテーブルからのcount値の取得エラー: " + e.getMessage(), e);
		}

		// メインクエリの実行
		try {
			return (List<Term>) executeSQL(sql, new Term(), displayCount);
		} catch (SQLException e) {
			throw new BeemsSQLException("アクセスランキング検索エラー: " + e.getMessage(), e);
		}
	}

    //役に立ったランキングの取得
	public List<Term> searchByPointRanking() throws BeemsSQLException {
		String countSql = "SELECT count FROM display_counts WHERE id = 1";
		String sql = "SELECT * FROM terms WHERE request_date IS NULL ORDER BY helped_count DESC LIMIT ?";
		int displayCount = 0;

		try (Connection con = DriverManager.getConnection(url, dbUser, dbPass);
				PreparedStatement countStmt = con.prepareStatement(countSql);
				ResultSet rs = countStmt.executeQuery()) {

			// displayCountの値を取得
			if (rs.next()) {
				displayCount = rs.getInt("count");
			}
		} catch (SQLException e) {
			throw new BeemsSQLException("display_countテーブルからのcount値の取得エラー: " + e.getMessage(), e);
		}

		// メインクエリの実行
		try {
			return (List<Term>) executeSQL(sql, new Term(), displayCount);
		} catch (SQLException e) {
			throw new BeemsSQLException("ポイントランキング検索エラー: " + e.getMessage(), e);
		}
	}
	
	//用語の抽出
	public Term searchTermById(int id) throws BeemsSQLException {
		String sql = "SELECT * FROM terms WHERE id = ?";
		try {
			List<Term> terms = (List<Term>) executeSQL(sql, new Term(), id);
			if (terms.isEmpty()) {
				throw new BeemsSQLException("用語が見つかりません。");
			}
			return terms.get(0);
		} catch (SQLException e) {
			throw new BeemsSQLException("用語検索エラー: " + e.getMessage(), e);
		}
	}
	
	//用語のアップデート
	public void updateTerm(Term term) throws BeemsSQLException {
	    String sql = "UPDATE terms SET term_name = ?, abbreviation = ?, reading = ?, definition = ?, search_count = ?, request_deletion = ?, helped_count = ?, key_term = ?, request_date = ?, last_editor = ?, department_id = ? WHERE id = ?";
	    String sqlNullRequestDate = "UPDATE terms SET term_name = ?, abbreviation = ?, reading = ?, definition = ?, search_count = ?, request_deletion = ?, helped_count = ?, key_term = ?, request_date = NULL, last_editor = ?, department_id = ? WHERE id = ?";
	    
	    try {
	        if (term.getRequestedDate() != null) {
	            executeSQL(sql, null, term.getTermName(), term.getAbbreviation(),
	                       term.getReading(), term.getDefinition(), term.getSearchCount(),
	                       term.isRequestDeletion(), term.getHelpedCount(), term.isKeyTerm(), 
	                       term.getRequestedDate(), term.getLastEditor(), term.getDepartmentId(), term.getId());
	        } else {
	            executeSQL(sqlNullRequestDate, null, term.getTermName(), term.getAbbreviation(),
	                       term.getReading(), term.getDefinition(), term.getSearchCount(),
	                       term.isRequestDeletion(), term.getHelpedCount(), term.isKeyTerm(), term.getLastEditor(),term.getDepartmentId(),
	                       term.getId());
	        }
	    } catch (SQLException e) {
	        throw new BeemsSQLException("用語更新エラー: " + e.getMessage(), e);
	    }
	}

	//用語の新規登録
	public Term createTerm(Term term) throws BeemsSQLException {
        String sql = "INSERT INTO terms (term_name, abbreviation, reading, definition, last_editor, department_id) VALUES (?, ?, ?, ?, ?,?)";
        try {
            int id = (int) executeSQL(sql, null, term.getTermName(), term.getAbbreviation(), term.getReading(), term.getDefinition(), term.getLastEditor(),term.getDepartmentId());
            term.setId(id);
            return term;
        } catch (SQLException e) {
            throw new BeemsSQLException("用語作成エラー: " + e.getMessage(), e);
        }
    }
	
	//用語の削除
	public void deleteTerm(int termId) throws BeemsSQLException {
		String sql = "DELETE FROM terms WHERE id = ?";
		try {
			executeSQL(sql, null, termId);
		} catch (SQLException e) {
			throw new BeemsSQLException("用語削除エラー: " + e.getMessage(), e);
		}
	}
	
	//削除依頼された用語を検索
    public List<Term> searchTermsForDeletion() throws BeemsSQLException {
        String sql = "SELECT * FROM terms WHERE request_deletion = ?";
        try {
            return (List<Term>) executeSQL(sql, new Term(), true);
        } catch (SQLException e) {
            throw new BeemsSQLException("用語検索エラー: " + e.getMessage(), e);
        }
    }
    
    //リクエストの新規登録
    public Term createRequest(Term term) throws BeemsSQLException {
        String sql = "INSERT INTO terms (term_name, remarks, request_date, department_id) VALUES (?, ?, ?, ?)";
        try {
             int id = (int) executeSQL(sql, null, term.getTermName(),term.getRemarks(),term.getRequestedDate(), 1);
            term.setId(id);  // Termオブジェクトに主キーを設定
            return term;  // 作成された用語オブジェクトを返す
            
        } catch (Exception e) {
            throw new BeemsSQLException("リクエスト作成エラー: " + e.getMessage(), e);
        }
    }
	
    //全用語検索
	public List<Term> searchAllTerms() throws BeemsSQLException {
		String sql = "SELECT * FROM terms";
		try {
			return (List<Term>) executeSQL(sql, new Term());
		} catch (SQLException e) {
			throw new BeemsSQLException("全用語検索エラー: " + e.getMessage(), e);
		}
	}

	

	

	public List<Term> searchTermsByUserId(int userId) throws BeemsSQLException {
		String sql = "SELECT t.* FROM terms t INNER JOIN search_logs s ON t.id = s.term_id WHERE s.user_id = ?";
		try {
			return (List<Term>) executeSQL(sql, new Term(), userId);
		} catch (SQLException e) {
			throw new BeemsSQLException("ユーザーIDによる用語検索エラー: " + e.getMessage(), e);
		}
	}

	public List<Department> searchRelatedDepartments(int termId) throws BeemsSQLException {
		String sql = "SELECT d.* FROM departments d INNER JOIN term_departments td ON d.id = td.department_id WHERE td.term_id = ?";
		try {
			return (List<Department>) executeSQL(sql, new Department(), termId);
		} catch (SQLException e) {
			throw new BeemsSQLException("関連部署検索エラー: " + e.getMessage(), e);
		}
	}
}
