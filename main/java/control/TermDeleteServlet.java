package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.RegisterLogDAO;
import dao.SearchLogDAO;
import dao.TermDAO;
import dto.Term;
import dto.User;
import exception.BeemsSQLException;

@WebServlet("/termDelete")
public class TermDeleteServlet extends HttpServlet {
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int termId = Integer.parseInt(req.getParameter("termId"));

        try {
        	HttpSession session = req.getSession(false); // 既存のセッションを取得（ない場合は新しいセッションは作成しない）
            if (session == null || session.getAttribute("user") == null) {
                res.sendRedirect("login"); // ログインしていない場合はログイン画面にリダイレクト
                return; // リダイレクトしたらそれ以降の処理を行わないため、ここで処理を終了する
            }
        	// セッションからユーザー情報を取得
            User user = (User) session.getAttribute("user");
        	
            TermDAO termdao = new TermDAO();
            Term term = termdao.searchTermById(termId);
            System.out.println(term.getTermName() + "を削除したよ");
            
            RegisterLogDAO registerdao = new RegisterLogDAO();
			SearchLogDAO searchdao = new SearchLogDAO();
		    termdao = new TermDAO();
          //外部キー制約で先にログを削除してからTermを削除する必要がある
			registerdao.deleteRegisterLog(termId);
			searchdao.deleteSearchLog(termId);
			termdao.deleteTerm(termId);

			session.setAttribute("successMessage", "用語の削除が完了しました");
            res.sendRedirect("top"); // トップページにリダイレクトする
        } catch (BeemsSQLException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("top.jsp").forward(req, res);
            return;
        }
    }
}
