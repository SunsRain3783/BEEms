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

@WebServlet("/signup")
public class UserCreateServlet extends HttpServlet {

	// ページにアクセスしたとき
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("user_create.jsp").forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String password2 = req.getParameter("password2");
		String departmentId = req.getParameter("departmentId");
		String[] ids = req.getParameterValues("id");//getParameterValueだと例えば0が7桁の時,0は1桁で処理されてしまう
		//String id= req.getParameter("id");
		String id = "";

		try {
			for (String idValue : ids) {
				id += idValue;
			}
			// ユーザー名の長さの確認
            if (username.length() > 30) {
                throw new BeemsValidationException("ユーザー名は30文字以下で入力してください");
            }
			if (!id.matches("^[0-9]{7}$")) {
				throw new BeemsValidationException("7桁の社員番号を入力してください");
			}
			if (!password.equals(password2)) {
				throw new BeemsValidationException("パスワードが一致しません");
			}
			if (!password.matches("^[a-zA-Z0-9]{4,12}$")) {
				throw new BeemsValidationException("4桁以上12桁以下でパスワードを入力してください");
			}

			// ユーザーオブジェクトの作成
			User user = new User(Integer.parseInt(id), username, HashPassword.hashString(password), departmentId);

			// ユーザー登録  
			UserDAO userdao = new UserDAO();
			userdao.createUser(user);

			HttpSession session = req.getSession(true);
			session.setAttribute("successMessage", "新規登録が完了しました");
			res.sendRedirect("login");

		} catch (BeemsSQLException e) {
			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
			req.getRequestDispatcher("user_create.jsp").forward(req, res);
		} catch (BeemsValidationException e) {
			req.setAttribute("id", id);
			req.setAttribute("username", username);
			HttpSession session = req.getSession(true);
			session.setAttribute("errorMessage", e.getMessage());
			res.sendRedirect("signup");
		}
	}

}
