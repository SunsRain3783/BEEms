<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" %>
<%@ page import="dto.User"%>
<%@ page import="dto.Term"%>
<%
HttpSession httpSession = request.getSession();
User user = (User) httpSession.getAttribute("user");
String errorMessage = (String) httpSession.getAttribute("errorMessage");
String userId = String.valueOf(user.getId());
Term term = (Term) request.getAttribute("term"); // 用語を取得
%>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Beems</title>
	<link rel="stylesheet" href="StyleSheet.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
		integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
		crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>
	<header>
		<div class="container">
			<a href="top"><img class="logo" src="images/logo.png"></a>
			<form id="searchForm" action="result" method="get">
				<input type="hidden" name="searchType" value="keyword">
				<input id="sbox1" name="keyword" type="text" placeholder="キーワードを入力"
					value="<%=request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>"
					required />
				<input id="sbtn1" type="submit" value="検索" />
			</form>
			<ul>
				<li>
					<div class="dropdown">
						<input id="tg" class="dropInput" type="checkbox">
						<label for="tg" class="dropLabel"><img id="userIcon" src="images/hamb.png"></label>
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

	<div class="main">
		<div class="container">
			<div class="createTermContainer">
				<div class="createContent">
					<p>リクエストされた用語の登録</p>
					
					<form action="answer" method="post">
						<input type="hidden" name="termId" value="<%=term.getId()%>">
						<input class="textarea" type="text" name="termName" placeholder="*名称" value="<%=term.getTermName()%>" id="termname" readonly>
						<input class="textarea" type="text" name="reading" placeholder="*よみかた" id="termname" value="<%= request.getAttribute("reading") != null ? request.getAttribute("reading") : "" %>" required oninput="preventInvalidInput(this)">
						
						<ul class="create-flex-list">
							<li><input class="textarea" type="text" name="abbreviation" placeholder="略称" id="ryaku" value="<%= request.getAttribute("ab") != null ? request.getAttribute("ab") : "" %>" oninput="preventInvalidInput(this)"></li>
							
							<script>
							    function preventInvalidInput(input) {
							        // 空白文字（全角と半角）を取り除く
							        input.value = input.value.replace(/[\u3000\s]+/g, '');
							    }
							</script>
							
							<li>
								<div class="select">
									<select name="departmentId">
										<option value="1" <%=term.getDepartmentId() == 1 ? "selected" : ""%>>出向部</option>
										<option value="2" <%=term.getDepartmentId() == 2 ? "selected" : ""%>>経営企画室</option>
										<option value="3" <%=term.getDepartmentId() == 3 ? "selected" : ""%>>総務部</option>
										<option value="4" <%=term.getDepartmentId() == 4 ? "selected" : ""%>>IT経営基盤統括部</option>
										<option value="5" <%=term.getDepartmentId() == 5 ? "selected" : ""%>>戦略事業部</option>
										<option value="6" <%=term.getDepartmentId() == 6 ? "selected" : ""%>>金融第一事業部</option>
										<option value="7" <%=term.getDepartmentId() == 7 ? "selected" : ""%>>金融第二事業部</option>
										<option value="8" <%=term.getDepartmentId() == 8 ? "selected" : ""%>>DXソリューション事業部</option>
										<option value="9" <%=term.getDepartmentId() == 9 ? "selected" : ""%>>産業第一事業部</option>
										<option value="10" <%=term.getDepartmentId() == 10 ? "selected" : ""%>>産業第二事業部</option>
										<option value="12" <%=term.getDepartmentId() == 11 ? "selected" : ""%>>一般用語</option>
									</select>
								</div>
							</li>
						</ul>
						<textarea class="textarea" type="text" name="definition" placeholder="*説明" id="ex" required><%= request.getAttribute("ex") != null ? request.getAttribute("ex") : "" %></textarea>
						<input class="btn2" type="submit" value="登録">
					</form>
				</div>
			</div>
		</div>
	</div>

	<footer>
		<div class="container">
			<p>© 2024 BEEms, All Rights Reserved.</p>
		</div>
	</footer>

	<!-- メッセージ表示用のスクリプト -->
	<script type="text/javascript">
		window.onload = function() { // HTMLが読み込まれたあとに実行
			setTimeout(function() { // 先にJSが読まれてしまうため、少し遅延させてメッセージを表示
				<%
				if (errorMessage != null) {
				%>
					// エラーメッセージをポップアップとして表示
					alert("<%=errorMessage%>");
					<% request.getSession().removeAttribute("errorMessage"); %>
				<%
				}
				%>
			}, 100); // 0.1秒遅延させてメッセージを表示
		};
	</script>
	
</body>
</html>
