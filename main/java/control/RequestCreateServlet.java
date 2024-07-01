package control;
 
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TermDAO;
import dto.Term;
import dto.User;
import exception.BeemsSQLException;
import exception.BeemsValidationException;
 
@WebServlet("/requestCreate")
public class RequestCreateServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("request_create.jsp").forward(req, res);
    }
	
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // セッションからユーザー情報を取得
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        String termNameRequest = (String)req.getParameter("termName");
        String remarks = req.getParameter("remarks");
        try {
        	Term newTerm = new Term();
            TermDAO termdao = new TermDAO();
            //既に登録/リクエストされている用語はできないようにする
            List<Term> terms = termdao.searchAllTerms();
            for(Term term:terms) {
            	if(termNameRequest.equals(term.getTermName())) {
            		throw new BeemsValidationException("その用語は既に登録またはリクエストされています");
            	}
            }
            if (termNameRequest.length() > 50) {
                throw new BeemsValidationException("名称は50文字以下で入力してください");
            }
            if (remarks.length() > 100) {
                throw new BeemsValidationException("備考は100文字以下で入力してください");
            }

            newTerm.setTermName(termNameRequest);
            newTerm.setRemarks(remarks);
            newTerm.setRequestedDate(Timestamp.valueOf(LocalDateTime.now()));
            
            termdao.createRequest(newTerm);

            session.setAttribute("successMessage", "リクエストの新規登録が完了しました");
            res.sendRedirect("/Beems/result?searchType=requestTerms");
            
        } catch (BeemsSQLException e) {
            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            req.getRequestDispatcher("request_create.jsp").forward(req, res);
        } catch(BeemsValidationException e) {
        	
        	req.setAttribute("termName", termNameRequest);
        	req.setAttribute("remarks", remarks);
        	
        	session.setAttribute("errorMessage", e.getMessage());
        	req.getRequestDispatcher("request_create.jsp").forward(req, res);
        }
    }
}