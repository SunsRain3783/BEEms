<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" %>
<%@ page import="dto.User"%>
<%@ page import="dto.Term"%>
<%
HttpSession httpSession = request.getSession();
User user = (User) httpSession.getAttribute("user");
String errorMessage = (String) httpSession.getAttribute("errorMessage");
String userId = String.valueOf(user.getId());
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
					<form action="requestCreate" method="post">
						<p>リクエストの新規登録</p>
						<ul class="create-flex-list">
							<li>
								<input class="textarea" type="text" name="termName" placeholder="*リクエストしたい用語" id="requestname" value="<%= request.getAttribute("termName") != null ? request.getAttribute("termName") : "" %>" required oninput="preventInvalidInput(this)">
							</li>
						</ul>
						<textarea class="textarea" id="ex" type="text" name="remarks" placeholder="備考(リクエストしたい部門等)"><%= request.getAttribute("remarks") != null ? request.getAttribute("remarks") : "" %></textarea>
						<input class="btn2" type="submit" value="登録">
						
						<script>
						    function preventInvalidInput(input) {
						        // 空白文字（全角と半角）を取り除く
						        input.value = input.value.replace(/[\u3000\s]+/g, '');
						    }
						</script>
						
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
