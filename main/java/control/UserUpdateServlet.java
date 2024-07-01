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

@WebServlet("/mypageCustom")
public class UserUpdateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false); // 既存のセッションを取得（ない場合は新しいセッションは作成しない）
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login"); // ログインしていない場合はログイン画面にリダイレクト
            return; // リダイレクトしたらそれ以降の処理を行わないため、ここで処理を終了する
        }

        // セッションからユーザー情報を取得
        User user = (User) session.getAttribute("user");

        // 必要な情報をリクエストにセット
        req.setAttribute("number", user.getId());
        req.setAttribute("name", user.getUsername());
        req.setAttribute("departmentName", user.getDepartmentName());

        // マイページのJSPにフォワード
        req.getRequestDispatcher("mypage_custom.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // パラメータを取得
        int id = ((User) req.getSession().getAttribute("user")).getId();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String password2 = req.getParameter("confirmPassword");
        String departmentName = req.getParameter("departmentName");

        try {
        	if (username.length() > 30) {
                throw new BeemsValidationException("ユーザー名は30文字以下で入力してください");
            }
    		if (!password.equals(password2)) {
    			throw new BeemsValidationException("パスワードが一致しません");
    		}
    		if (!password.matches("^[a-zA-Z0-9]{4,12}$")) {
    			throw new BeemsValidationException("4桁以上12桁以下でパスワードを入力してください");
    		}

            // ユーザーオブジェクトを作成
            User user = new User(id, username, HashPassword.hashString(password), departmentName);
        	
            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(user);

            // セッションのユーザー情報を更新
            req.getSession().setAttribute("user", user);

            // 更新が成功したらマイページにリダイレクト
            HttpSession session = req.getSession(false);
			session.setAttribute("successMessage", "ユーザーの編集が完了しました");
			res.sendRedirect("mypage");
			
			
        } catch (BeemsSQLException e) {
            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            req.getRequestDispatcher("mypage_custom.jsp").forward(req, res);
        } catch (BeemsValidationException e) {
			HttpSession session = req.getSession(false);
			session.setAttribute("errorMessage", e.getMessage());
			res.sendRedirect("mypageCustom");
		}
    }
}
