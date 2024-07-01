package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TermDAO;
import dto.Term;
import exception.BeemsSQLException;

@WebServlet("/top")
public class TopServlet extends HttpServlet {
	Boolean isDoGet = false;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession(false); // 既存のセッションを取得（ない場合は新しいセッションは作成しない）
		if (session == null || session.getAttribute("user") == null) {
			res.sendRedirect("login"); // ログインしていない場合はログイン画面にリダイレクト
			return; // リダイレクトしたらそれ以降の処理を行わないため、ここで処理を終了する
		}

		req.setCharacterEncoding("UTF-8");
		try {
			TermDAO termdao = new TermDAO();

			// 注目用語の取得
			List<Term> keyTerms = termdao.searchKeyTerms();
//			// 新着用語のデバッグ情報を出力
//			System.out.println("注目用語:");
//			for (Term term : keyTerms) {
//				System.out.println(term);
//			}
			req.setAttribute("keyTerms", keyTerms);

			// 検索ランキングの取得
			List<Term> accessRankingTerms = termdao.searchByAccessRanking();
//			// 検索ランキングのデバッグ情報を出力
//			System.out.println("アクセスランキング:");
//			for (Term term : accessRankingTerms) {
//				System.out.println(term);
//			}
			req.setAttribute("accessRankingTerms", accessRankingTerms);

			// 役に立ったランキングの取得
			List<Term> pointRankingTerms = termdao.searchByPointRanking();
//			// 役に立ったランキングのデバッグ情報を出力
//			System.out.println("ポイントランキング:");
//			for (Term term : pointRankingTerms) {
//				System.out.println(term);
//			}
			req.setAttribute("pointRankingTerms", pointRankingTerms);

			req.getRequestDispatcher("top.jsp").forward(req, res);

		} catch (BeemsSQLException e) {
			req.setAttribute("errorMessage", e.getMessage());
			req.getRequestDispatcher("top.jsp").forward(req, res);
		}
	}

}
