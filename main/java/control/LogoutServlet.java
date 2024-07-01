package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
    	HttpSession session = req.getSession(false); // 既存のセッションを取得する
        if (session != null) {
            session.invalidate(); // セッションを無効化する
        }
        res.sendRedirect("login"); // ログインページにリダイレクトする
    }
}

