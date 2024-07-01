<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="dto.Term"%>
<%@ page import="dto.User"%>
<%@ page import="dto.SearchLog"%>
<%@ page import="dto.RegisterLog"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
HttpSession httpSession = request.getSession();
User user = (User) httpSession.getAttribute("user");
String successMessage = (String) httpSession.getAttribute("successMessage");
String userId = String.valueOf(user.getId());
String searchType = (String) request.getAttribute("searchType"); // 検索タイプを取得
List<SearchLog> searchLogs = (List<SearchLog>) request.getAttribute("searchLogs");
List<RegisterLog> registerLogs = (List<RegisterLog>) request.getAttribute("registerLogs");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
%>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Beems</title>
<link rel="stylesheet" href="StyleSheet.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
	integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<!--	<link rel="stylesheet" href="/css/responsive.css">-->
</head>

<body>

	<!--ここからヘッダー-->
	<header>
		<div class="container">
			<a href="top"><img class="logo" src="images/logo.png"></a>
			<form id="searchForm" action="result" method="get">
				<input type="hidden" name="searchType" value="keyword"> <input
					id="sbox1" name="keyword" type="text" placeholder="キーワードを入力"
					value="<%=request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>"
					required /> <input id="sbtn1" type="submit" value="検索" />
			</form>
			<ul>
				<li>
					<div class="dropdown">
						<input id="tg" class="dropInput" type="checkbox"> <label
							for="tg" class="dropLabel"><img id="userIcon"
							src="images/hamb.png"></label>
						<ul class="menu animation">
							<li><a class="item" href="top">トップページ</a></li>
							<li><a class="item" href="mypage">マイページ</a></li>
							<li><a class="item" href="logout">ログアウト</a></li>
							<li><a class="item" href="termCreate">用語の新規登録</a></li>
							<li><a class="item" href="requestCreate">リクエストの新規登録</a></li>
						</ul>
					</div>
				</li>
				<li><img id="userIcon" src="images/userIcon.png"></li>
				<li><a href="mypage"><%=userId%></a></li>
			</ul>
		</div>
	</header>
	<!--ここまでヘッダー-->


	<div class="main">
		<div class="container">
			<%
			// サーブレットから渡された検索結果を取得
			List<Term> resultTerms = (List<Term>) request.getAttribute("resultTerms");
			%>
			<p id="pleft"><%=request.getAttribute("resultString")%>(<%=resultTerms.size()%>件)
			</p>

			<ul class="newContent">
				<li><a href="termCreate">用語の新規登録</a></li>
				<li><a href="requestCreate">リクエストの新規登録</a></li>
			</ul>
			
			<%
			// 検索結果がある場合のみ表示する
			if (resultTerms != null && !resultTerms.isEmpty()) {
			%>
			
			<div class="panel2">
				<ul>
					<%
					for (int i = 0; i < resultTerms.size(); i++) {
					    Term term = resultTerms.get(i);
					    String searchDate = "";
					    String registerDate = "";
					    if ("logTerms".equals(searchType) && searchLogs != null && i < searchLogs.size()) {
					        searchDate = sdf.format(searchLogs.get(i).getSearchDate());
					    }else if ("registerTerms".equals(searchType) && registerLogs != null && i < registerLogs.size()) {
					        registerDate = sdf.format(registerLogs.get(i).getSearchDate());
					    }
					%>
					<li>
						<div id="resultWord">

							<%
							if ("requestTerms".equals(searchType)) { // リクエスト一覧
							%>
							<i class="fa-solid fa-play" style="color: rgb(180, 200, 200);"></i>
							<span id=reqterm><%=term.getTermName()%></span>
							<form action="answer" method="get" id="btn5form">
								<input type="hidden" name="termId" value="<%=term.getId()%>">
								<input class="btn5" type="submit" value="登録">
							</form>
							<% if (term.getRemarks() != null && !term.getRemarks().isEmpty()) { %>
        						<p><%=term.getRemarks()%></p>
    						<% } else{ %>
    							<br><br>
    						<% }
							
							} else if ("logTerms".equals(searchType)) { // 閲覧履歴
							%>
							<i class="fa-solid fa-play" style="color: rgb(180, 200, 200);"></i>
							<a href="detail?termId=<%=term.getId()%>"><%=term.getTermName()%></a><span id="spanright">閲覧日：<%=searchDate%></span>
							<p><%=term.getDefinition()%></p>
							
							<%
							} else if ("registerTerms".equals(searchType)) { // 過去に新規登録した用語
							%>
							<i class="fa-solid fa-play" style="color: rgb(180, 200, 200);"></i>
							<a href="detail?termId=<%=term.getId()%>"><%=term.getTermName()%></a><span id="spanright">登録日：<%=registerDate%></span>
							<p><%=term.getDefinition()%></p>
							
							<%
							} else { // キーワード検索/新着用語/注目用語 その他
							%>
							<i class="fa-solid fa-play" style="color: rgb(180, 200, 200);"></i>
							<a href="detail?termId=<%=term.getId()%>"><%=term.getTermName()%></a>
							<p><%=term.getDefinition()%></p>
							<%
							}
							%>

						</div>
					</li>
					<%
					}
					}else{
					%>
					<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
					<%
					}
					%>
				</ul>

			</div>
		</div>
	</div>
			

	<!--ここからフッター-->
	<footer>
		<div class="container">
			<p>© 2024 BEEms, All Rights Reserved.</p>
		</div>
	</footer>
	<!--ここまでフッター-->
	
	<!-- メッセージ表示用のスクリプト -->
	<script type="text/javascript">
		window.onload = function() { // HTMLが読み込まれたあとに実行
			setTimeout(function() { // 先にJSが読まれてしまうため、少し遅延させてメッセージを表示
				<%
				if (successMessage != null) {
				%>
					// エラーメッセージをポップアップとして表示
					alert("<%=successMessage%>");
					<% request.getSession().removeAttribute("successMessage"); %>
				<%
				}
				%>
			}, 100); // 0.1秒遅延させてメッセージを表示
		};
	</script>

</body>

</html>