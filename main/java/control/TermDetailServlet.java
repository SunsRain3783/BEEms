package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DepartmentDAO;
import dao.SearchLogDAO;
import dao.TermDAO;
import dto.Department;
import dto.SearchLog;
import dto.Term;
import dto.User;
import exception.BeemsSQLException;

@WebServlet("/detail")
public class TermDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // ログインセッションの確認
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login"); // ログインしていない場合はログイン画面にリダイレクト
            return;
        }

        req.setCharacterEncoding("UTF-8");

        try {
            int termId = Integer.parseInt(req.getParameter("termId"));
            TermDAO termDao = new TermDAO();
            DepartmentDAO departmentdao = new DepartmentDAO();
            Term term = termDao.searchTermById(termId);
            User user = (User) session.getAttribute("user");
        	

            if (term != null) {
            	// 閲覧数の更新
            	System.out.println(term.getTermName() + "を" + user.getUsername() + "が閲覧したよ");
                term.setSearchCount(term.getSearchCount() + 1);
                termDao.updateTerm(term); // 更新
                
                //閲覧のログを保存
                SearchLogDAO searchLogDAO = new SearchLogDAO();
    			if (!searchLogDAO.searchLogExists(term.getId(), user.getId())) {//その複合主キーの組み合わせがログに存在しない場合のみログを入れる
    				SearchLog searchlog = new SearchLog(term.getId(), user.getId());
    				searchLogDAO.createSearchLog(searchlog);
    			}
            	
            	Department dp = departmentdao.searchDepartmentById(term.getDepartmentId());
            	String depname = dp.getDepartmentName();
            	
                req.setAttribute("term", term);
                req.setAttribute("depname", depname);
                req.getRequestDispatcher("term_detail.jsp").forward(req, res);
            } else {
                req.setAttribute("errorMessage", "用語が見つかりませんでした");
                req.getRequestDispatcher("result.jsp").forward(req, res);
            }

        } catch (NumberFormatException | BeemsSQLException e) {
            req.setAttribute("errorMessage", "用語の取得に失敗しました");
            req.getRequestDispatcher("result.jsp").forward(req, res);
        }
    }
}
