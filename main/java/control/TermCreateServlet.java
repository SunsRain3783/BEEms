package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.RegisterLogDAO;
import dao.TermDAO;
import dto.RegisterLog;
import dto.Term;
import dto.User;
import exception.BeemsSQLException;
import exception.BeemsValidationException;
import service.HiraganaChecker;

@WebServlet("/termCreate")
public class TermCreateServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("term_create.jsp").forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		// セッションからユーザー情報を取得
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");

		// ユーザー情報からユーザーIDを取得
		int lastEditor = user.getId();

		// フォームから送信されたパラメータを取得
		String termName = req.getParameter("termName");
		String abbreviation = req.getParameter("abbreviation");
		String reading = req.getParameter("reading");
		String definition = req.getParameter("definition");
		int departmentId = Integer.parseInt(req.getParameter("departmentId"));

		try {
			TermDAO termDao = new TermDAO();
			//既に登録/リクエストされている用語はできないようにする
			List<Term> terms = termDao.searchAllTerms();
			for (Term term : terms) {
				if (termName.equals(term.getTermName())) {
					throw new BeemsValidationException("その用語は既に登録またはリクエストされています");
				}
			}
			if (termName.length() > 50) {
				throw new BeemsValidationException("名称は50文字以下で入力してください");
			}
			if (abbreviation.length() > 50) {
				throw new BeemsValidationException("略称は50文字以下で入力してください");
			}
			if (reading.length() > 50) {
				throw new BeemsValidationException("読み方は50文字以下で入力してください");
			}
			if (definition.length() > 500) {
				throw new BeemsValidationException("説明は500文字以下で入力してください");
			}
			if (!HiraganaChecker.isHiragana(
					reading)) {
				throw new BeemsValidationException("読み方はひらがなのみ入力してください");
			}

			// 新しい用語オブジェクトを作成
			Term newTerm = new Term();
			newTerm.setTermName(termName);
			newTerm.setAbbreviation(abbreviation);
			newTerm.setReading(reading);
			newTerm.setDefinition(definition);
			newTerm.setDepartmentId(departmentId);
			newTerm.setLastEditor(lastEditor);

			termDao = new TermDAO();
			termDao.createTerm(newTerm);

			//登録用語のログを保存
			RegisterLogDAO registerLogDAO = new RegisterLogDAO();
			RegisterLog registerlog = new RegisterLog(newTerm.getId(), user.getId());
			registerLogDAO.createRegisterLog(registerlog);

			System.out.println(user.getUsername() + "が" + newTerm.getTermName() + "を追加したよ");

			session.setAttribute("successMessage", "用語の新規登録が完了しました");
			res.sendRedirect("/Beems/result?searchType=registerTerms");

		} catch (BeemsSQLException e) {
			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
			req.getRequestDispatcher("top.jsp").forward(req, res);
		} catch (BeemsValidationException e) {
			req.setAttribute("termName", termName);
			req.setAttribute("reading", reading);
			req.setAttribute("ab", abbreviation);
			req.setAttribute("ex", definition);

			session.setAttribute("errorMessage", e.getMessage());
			req.getRequestDispatcher("term_create.jsp").forward(req, res);
		}
	}
}
