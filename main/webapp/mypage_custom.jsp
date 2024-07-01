<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.User"%>
<%
HttpSession httpSession = request.getSession();
User user = (User) httpSession.getAttribute("user");
String errorMessage = (String) httpSession.getAttribute("errorMessage");
String userId = String.valueOf(user.getId());
String userName = user.getUsername();
String departmentName = String.valueOf(user.getDepartmentName()); // 部門IDを取得
%>
<!DOCTYPE html>
<html lang="ja">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Beems</title>
	<link rel="stylesheet" href="StyleSheet.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
		integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
		crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>

<body>

	<!--ここからヘッダー-->
	<header>
		<div class="container">
			<a href="top"><img class="logo" src="images/logo.png"></a>
			<form id="searchForm" action="result" method="get">
				<input type="hidden" name="searchType" value="keyword"> 
				<input id="sbox1" name="keyword" type="text" placeholder="キーワードを入力" required /> 
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
	<!--ここまでヘッダー-->

	<div class="main">
		<div class="container">

			<div class="mypage-info">

				<img id="mypage-userIcon" src="images/userIcon.png">

				<form action="mypageCustom" method="post">
					<ul>
						<li><span>社員番号　　</span><span><%= userId %></span></li>
						<li><span>氏名　　　　</span><input class="textarea" type="text" name="username" value="<%= userName %>" required onkeydown="preventInvalidInput(event)"></li>
						<li><span>部門名　　　</span>
							<div class="select2">
								<select name="departmentName">
									<option value="出向部" <%= "出向部".equals(departmentName) ? "selected" : "" %>>出向部</option>
									<option value="経営企画室" <%= "経営企画室".equals(departmentName) ? "selected" : "" %>>経営企画室</option>
									<option value="総務部" <%= "総務部".equals(departmentName) ? "selected" : "" %>>総務部</option>
									<option value="IT経営基盤統括部" <%= "IT経営基盤統括部".equals(departmentName) ? "selected" : "" %>>IT経営基盤統括部</option>
									<option value="戦略事業部" <%= "戦略事業部".equals(departmentName) ? "selected" : "" %>>戦略事業部</option>
									<option value="金融第一事業部" <%= "金融第一事業部".equals(departmentName) ? "selected" : "" %>>金融第一事業部</option>
									<option value="金融第二事業部" <%= "金融第二事業部".equals(departmentName) ? "selected" : "" %>>金融第二事業部</option>
									<option value="DXソリューション事業部" <%= "DXソリューション事業部".equals(departmentName) ? "selected" : "" %>>DXソリューション事業部</option>
									<option value="産業第一事業部" <%= "産業第一事業部".equals(departmentName) ? "selected" : "" %>>産業第一事業部</option>
									<option value="産業第二事業部" <%= "産業第二事業部".equals(departmentName) ? "selected" : "" %>>産業第二事業部</option>
									<option value="BEEms管理部門" <%= "BEEms管理部門".equals(departmentName) ? "selected" : "" %>>BEEms管理部門</option>
								</select>
							</div>
						</li>
						<li><span>パスワード　</span><input class="textarea" type="password" name="password" required oninput="preventInvalidInput(this)"></li>
						<li><span>確認　　　　</span><input class="textarea" type="password" name="confirmPassword" required oninput="preventInvalidInput(this)"></li>
					</ul>
					<div class="mypage-flex">
						<ul>
							<li><input class="btn" type="button" value="戻る" onclick="history.back();"></li>
							<li><input class="btn" type="submit" value="更新"></li>
						</ul>
						
						<script>
						    function preventInvalidInput(input) {
						        // 空白文字（全角と半角）を取り除く
						        input.value = input.value.replace(/[\u3000\s]+/g, '');
						    }
						</script>
						
					</div>
				</form>

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
