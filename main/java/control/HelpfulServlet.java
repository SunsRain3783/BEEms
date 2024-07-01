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

@WebServlet("/helpful")
public class HelpfulServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int termId = Integer.parseInt(req.getParameter("termId"));
        int feedback = Integer.parseInt(req.getParameter("feedback"));

        try {
            TermDAO termdao = new TermDAO();
            Term term = termdao.searchTermById(termId);
            String msg = "";

            if (feedback == 1) {
            	msg = "役に立ったボタンが押されたよ";
                System.out.println(msg);
                term.setHelpedCount(term.getHelpedCount() + 1);
            } else if (feedback == -1) {
            	msg = "役に立たなかったボタンが押されたよ";
                System.out.println(msg);
                term.setHelpedCount(term.getHelpedCount() - 1);
            } else if (feedback == 0) {
            	msg = "まあまあボタンが押されたよ";
                System.out.println(msg);
            }

            termdao.updateTerm(term);
            
        } catch (BeemsSQLException e) {
            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
        }

        res.sendRedirect("/Beems/detail?termId=" + termId);
    }
}
