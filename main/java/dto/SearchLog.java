package dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SearchLog implements ResultSetMapper<SearchLog> {
    private int termId;
    private int userId;
    private Timestamp searchDate;
    
    public SearchLog() {}
    
    public SearchLog(int termId,int userId) {
    	this.termId = termId;
    	this.userId = userId;
    }
    
    public SearchLog(int termId,int userId,Timestamp searchDate) {
    	this.termId = termId;
    	this.userId = userId;
    	this.searchDate = searchDate;
    }

    public int getTermId() {
        return termId;
    }
    

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Timestamp searchDate) {
        this.searchDate = searchDate;
    }

    @Override
    public SearchLog fromResultSet(ResultSet rs) throws SQLException {
        return new SearchLog(
        		rs.getInt("term_id"),
        		rs.getInt("user_id"),
        		rs.getTimestamp("search_date")
        		);
    }
}
