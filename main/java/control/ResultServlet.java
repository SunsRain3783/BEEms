package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DepartmentDAO;
import dao.RegisterLogDAO;
import dao.SearchLogDAO;
import dao.TermDAO;
import dto.Department;
import dto.RegisterLog;
import dto.SearchLog;
import dto.Term;
import dto.User;
import exception.BeemsSQLException;
import exception.BeemsValidationException;

@WebServlet("/result")
public class ResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// ログインセッションの確認
		HttpSession session = req.getSession(false); // 既存のセッションを取得（ない場合は新しいセッションは作成しない）
		if (session == null || session.getAttribute("user") == null) {
			res.sendRedirect("login"); // ログインしていない場合はログイン画面にリダイレクト
			return; // リダイレクトしたらそれ以降の処理を行わないため、ここで処理を終了する
		}
		
		String searchType = req.getParameter("searchType");

		req.setCharacterEncoding("UTF-8");
		try {
			// セッションからユーザー情報を取得
	        User user = (User) session.getAttribute("user");
			
			List<Term> resultTerms = new ArrayList<>();
			String resultString = "";
			TermDAO termdao = new TermDAO();

            // 検索方法に応じて処理を分岐
			
			//キーワード検索
            if ("keyword".equals(searchType)) {
                String keyword = req.getParameter("keyword");
                // 検索文字列の長さチェック
                if (keyword.length() > 50) {
                    throw new BeemsValidationException("50文字以下で入力してください");
                }
                resultString = "「" + keyword + "」の検索結果";
                resultTerms = termdao.searchForKeyword(keyword, true);
                req.setAttribute("keyword", keyword);
            }
            
            //注目用語一覧
            else if ("keyTerms".equals(searchType)) {
            	resultString = "注目用語";
                resultTerms = termdao.searchKeyTermsAll();
            }
            
            //新着用語一覧
            else if ("newTerms".equals(searchType)) {
            	resultString = "新着用語＊1か月以内";
                resultTerms = termdao.searchNewTermsAll();
            }
            
            //リクエスト用語一覧
            else if ("requestTerms".equals(searchType)) {
            	resultString = "リクエスト用語";
                resultTerms = termdao.searchRequestTerms();
            }
            
            //社内用語一覧
            else if ("compTerms".equals(searchType)) {
            	resultString = "社内用語";
                resultTerms = termdao.searchCompTerms();
            }
            
            //一般,各部門用語一覧
            else if ("depTerms".equals(searchType)) {
            	int depId = Integer.parseInt(req.getParameter("depId"));
            	DepartmentDAO departmentdao = new DepartmentDAO();
                resultTerms = termdao.searchDepTerms(depId);
                Department dp = departmentdao.searchDepartmentById(depId);
                resultString = dp.getDepartmentName();
            }
            
            //頭文字一覧
            else if ("iniTerms".equals(searchType)) {
                String initial = req.getParameter("initial");

                // initialの各文字で検索し、結果を結合する
                for (char ch : initial.toCharArray()) {
                    String initialStr = String.valueOf(ch);
                    List<Term> terms = termdao.searchTermsByInitial(initialStr);
                    resultTerms.addAll(terms);
                }

                // resultString を設定
                StringBuilder sb = new StringBuilder("「");
                for (char ch : initial.toCharArray()) {
                    sb.append(ch).append("/");
                }
                sb.deleteCharAt(sb.length() - 1); // 最後の '/' を削除
                sb.append("」から始まる用語");
                resultString = sb.toString();
            }
            
            //検索履歴一覧
            else if ("logTerms".equals(searchType)) {
            	SearchLogDAO searchLogDAO = new SearchLogDAO();
                List<SearchLog> searchLogs = searchLogDAO.searchSearchLogsByUserId(user.getId());
                
                for (SearchLog searchlog : searchLogs) {
                    resultTerms.add(termdao.searchTermById(searchlog.getTermId()));
                }
                
            	resultString = "検索履歴";
                req.setAttribute("searchLogs", searchLogs);
            }
            
            //過去に登録した用語一覧
            else if ("registerTerms".equals(searchType)) {
            	resultString = "あなたが登録した用語";
            	RegisterLogDAO registerLogDAO = new RegisterLogDAO();
                List<RegisterLog> registerLogs = registerLogDAO.searchRegisterLogsByUserId(user.getId());
                
                for (RegisterLog registerlog : registerLogs) {
                    resultTerms.add(termdao.searchTermById(registerlog.getTermId()));
                }
                req.setAttribute("registerLogs", registerLogs);
            }
            
          //削除依頼一覧
			else if("deleteTerms".equals(searchType)) {
				resultString = "削除依頼されている用語";
				resultTerms = termdao.searchTermsForDeletion();
			}
            
            req.setAttribute("resultString", resultString);
			req.setAttribute("resultTerms", resultTerms);
			req.setAttribute("searchType", searchType);
			req.getRequestDispatcher("result.jsp").forward(req, res);
			
		} catch (BeemsSQLException e) {
			req.setAttribute("resultString", e.getMessage());
			req.setAttribute("resultTerms", new ArrayList<>());
			req.getRequestDispatcher("result.jsp").forward(req, res);
		} catch (BeemsValidationException e) {
			session.setAttribute("errorMessage", e.getMessage());
			res.sendRedirect("top");
		}
	}

}
