package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TermDAO;
import dto.Term;
import exception.BeemsSQLException;

@WebServlet("/deleteRequest")
public class DeleteRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int termId = Integer.parseInt(req.getParameter("termId"));

        try {
            TermDAO termDao = new TermDAO();
            Term term = termDao.searchTermById(termId);

            if (term != null) {
            	System.out.println("削除依頼ボタンが押されたよ");
            	System.out.println(term.isRequestDeletion());
            	// requestDeletion フィールドを切り替える
            	term.toggleRequestDeletion();
            	System.out.println(term.isRequestDeletion());
                termDao.updateTerm(term); // 更新

                // 詳細ページにリダイレクト
                res.sendRedirect("/Beems/detail?termId=" + termId);
                return;
            } else {
                req.setAttribute("errorMessage", "用語が見つかりませんでした");
                req.getRequestDispatcher("top.jsp").forward(req, res);
                return;
            }
        } catch (BeemsSQLException e) {
            req.setAttribute("errorMessage", "用語の取得に失敗しました");
            req.getRequestDispatcher("top.jsp").forward(req, res);
            return;
        }
    }
}

