package dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Term implements ResultSetMapper<Term> {
    private int id;
    private String termName;
    private String abbreviation; // 略語
    private String reading; // 読み方
    private String definition; // 用語の説明
    private Timestamp createdDate; // 作成日
    private int searchCount; // 検索回数
    private Timestamp requestedDate;
    private int helpedCount; // 役に立ったポイント
    private int lastEditor; // 最終編集者
    private boolean requestDeletion; // 削除依頼
    private boolean keyTerm; // 注目用語
    private String remarks; // 備考
    private int departmentId; //部門ID

    // デフォルトコンストラクタ
    public Term() {
    }

    // パラメータ付きコンストラクタ
    public Term(int id, String termName, String abbreviation, String reading, String definition,
                Timestamp createdDate, int searchCount, Timestamp requestedDate, int helpedCount, int lastEditor,
                boolean requestDeletion, boolean keyTerm, String remarks, int departmentId) {
        this.id = id;
        this.termName = termName;
        this.abbreviation = abbreviation;
        this.reading = reading;
        this.definition = definition;
        this.createdDate = createdDate;
        this.searchCount = searchCount;
        this.requestedDate = requestedDate;
        this.helpedCount = helpedCount;
        this.lastEditor = lastEditor;
        this.requestDeletion = requestDeletion;
        this.keyTerm = keyTerm;
        this.remarks = remarks;
        this.departmentId = departmentId;
    }

    // TermCreateServletで呼び出す、idがまだ割り振られていないから
    public Term(String termName, String abbreviation, String reading, String definition, int lastEditor) {
        this.termName = termName;
        this.abbreviation = abbreviation;
        this.reading = reading;
        this.definition = definition;
        this.lastEditor = lastEditor;
    }

    // TermUpdateServletで呼び出す
    public Term(int id, String termName, String abbreviation, String reading, String definition, Timestamp createdDate,
                int lastEditor, boolean requestDeletion, boolean keyTerm) {
        this.id = id;
        this.termName = termName;
        this.abbreviation = abbreviation;
        this.reading = reading;
        this.definition = definition;
        this.createdDate = createdDate;
        this.lastEditor = lastEditor;
        this.requestDeletion = requestDeletion;
        this.keyTerm = keyTerm;
        
    }
    
    //削除依頼の切り替え
    public void toggleRequestDeletion() {
        this.requestDeletion = !this.requestDeletion;
    }
    
    //注目用語の切り替え
    public void toggleKeyTerm() {
        this.keyTerm = !this.keyTerm;
    }


    // getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public Timestamp getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Timestamp requestedDate) {
        this.requestedDate = requestedDate;
    }

    public int getHelpedCount() {
        return helpedCount;
    }

    public void setHelpedCount(int helpedCount) {
        this.helpedCount = helpedCount;
    }

    public int getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(int lastEditor) {
        this.lastEditor = lastEditor;
    }

    public boolean isRequestDeletion() {
        return requestDeletion;
    }

    public void setRequestDeletion(boolean requestDeletion) {
        this.requestDeletion = requestDeletion;
    }

    public boolean isKeyTerm() {
        return keyTerm;
    }

    public void setKeyTerm(boolean keyTerm) {
        this.keyTerm = keyTerm;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public Term fromResultSet(ResultSet rs) throws SQLException {
        return new Term(
            rs.getInt("id"),
            rs.getString("term_name"),
            rs.getString("abbreviation"),
            rs.getString("reading"),
            rs.getString("definition"),
            rs.getTimestamp("created_date"),
            rs.getInt("search_count"),
            rs.getTimestamp("request_date"),
            rs.getInt("helped_count"),
            rs.getInt("last_editor"),
            rs.getBoolean("request_deletion"),
            rs.getBoolean("key_term"),
            rs.getString("remarks"),
            rs.getInt("department_id")
        );
    }
    
    //デバッグ用
    @Override
    public String toString() {
        return "Term [id=" + id + ", termName=" + termName + ", abbreviation=" + abbreviation + 
               ", reading=" + reading + ", definition=" + definition + ", createdDate=" + createdDate + 
               ", searchCount=" + searchCount + ", requestedDate=" + requestedDate + ", helpedCount=" + 
               helpedCount + ", lastEditor=" + lastEditor + ", departmentId=" + departmentId + "]";
    }
    
}
