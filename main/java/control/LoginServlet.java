package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import dto.User;
import exception.BeemsSQLException;
import exception.BeemsValidationException;
import service.HashPassword;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	// ページにアクセスしたとき
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("login.jsp").forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String id = req.getParameter("id");
		String password = req.getParameter("password");

		res.setContentType("text/html;charset=UTF-8");

		UserDAO userdao = null;

		try {
			if (!id.matches("^[0-9]{7}$")) {
				throw new BeemsValidationException("7桁の社員番号を入力してください");
			}
			
			// ユーザーの検証
			userdao = new UserDAO();
			User user = userdao.searchUser(Integer.parseInt(id));
			if (user == null) {
				throw new BeemsValidationException("社員番号が登録されていません");
			}

			if (!HashPassword.hashString(password).equals(user.getPassword())) {
				throw new BeemsValidationException("パスワードが間違っています");
			}

			// セッションにユーザー情報を登録
			HttpSession session = req.getSession(true);
			session.setAttribute("user", user);

			// topサーブレットにリダイレクト
			res.sendRedirect("top");

		} catch (BeemsSQLException |BeemsValidationException e) {
			req.setAttribute("id", id);
			
			HttpSession session = req.getSession(true);
			session.setAttribute("errorMessage", e.getMessage());
			res.sendRedirect("login");
		}
	}

}
