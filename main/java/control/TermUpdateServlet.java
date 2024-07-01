package control;

import java.io.IOException;

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
import service.HiraganaChecker;

@WebServlet("/termUpdate")
public class TermUpdateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // リクエストパラメータからIDを取得
        int termId = Integer.parseInt(req.getParameter("termId"));
        
        try {
            // TermDAOを使ってIDに基づいて用語を取得
            TermDAO termDao = new TermDAO();
            Term term = termDao.searchTermById(termId);
            
            if (term != null) {
                // 取得した用語をリクエスト属性にセット
                req.setAttribute("term", term);
                req.getRequestDispatcher("term_update.jsp").forward(req, res);
            } else {
                req.setAttribute("errorMessage", "指定されたIDの用語が見つかりませんでした");
                req.getRequestDispatcher("top").forward(req, res);
            }
        } catch (BeemsSQLException e) {
            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            req.getRequestDispatcher("top").forward(req, res);
        }
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // セッションからユーザー情報を取得
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // ユーザー情報からユーザーIDを取得
        int lastEditor = user.getId(); 

        // フォームから送信されたパラメータを取得
        int termId = Integer.parseInt(req.getParameter("termId"));
        String termName = req.getParameter("termName");
        String abbreviation = req.getParameter("abbreviation");
        String reading = req.getParameter("reading");
        String definition = req.getParameter("definition");
        int departmentId = Integer.parseInt(req.getParameter("departmentId"));

        // 既存の用語オブジェクトをデータベースから取得
        TermDAO termDao = null;
        Term term = null;
        try {
        	if (abbreviation.length() > 50) {
                throw new BeemsValidationException("略称は50文字以下で入力してください");
            }
        	if (reading.length() > 50) {
                throw new BeemsValidationException("読み方は50文字以下で入力してください");
            }
        	if (definition.length() > 500) {
                throw new BeemsValidationException("説明は500文字以下で入力してください");
            }
        	if (!HiraganaChecker.isHiragana(
					reading)) {
				throw new BeemsValidationException("読み方はひらがなのみ入力してください");
			}
        	
            termDao = new TermDAO();
            term = termDao.searchTermById(termId);
            
            // 更新する用語オブジェクトを作成
            term.setTermName(termName);
            term.setAbbreviation(abbreviation);
            term.setReading(reading);
            term.setDefinition(definition);
            term.setDepartmentId(departmentId);
            term.setLastEditor(lastEditor); // ユーザーIDを設定
            
            // DAOを使って用語を更新
            termDao.updateTerm(term);
            System.out.println(term.getTermName() + "を編集したよ");
            
			session.setAttribute("successMessage", "用語の編集が完了しました");
            res.sendRedirect("/Beems/detail?termId=" + termId);
            
        } catch (BeemsSQLException e) {
            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            req.getRequestDispatcher("top.jsp").forward(req, res);
            return;
        } catch (BeemsValidationException e) {
			session.setAttribute("errorMessage", e.getMessage());
			res.sendRedirect("/Beems/termUpdate?termId=" + termId);
		}

        
    }



}
