package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.User; // 必要に応じて適切なパッケージに修正

@WebServlet("/mypage")
public class MyPageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession(false); // 既存のセッションを取得（ない場合は新しいセッションは作成しない）
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login"); // ログインしていない場合はログイン画面にリダイレクト
            return; // リダイレクトしたらそれ以降の処理を行わないため、ここで処理を終了する
        }

        // セッションからユーザー情報を取得
        User user = (User) session.getAttribute("user");

        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getUsername());
        session.setAttribute("departmentName", user.getDepartmentName());
        
        // マイページのJSPにフォワード
        req.getRequestDispatcher("mypage.jsp").forward(req, res);
    }
}
