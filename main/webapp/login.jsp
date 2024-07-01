<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
HttpSession httpSession = request.getSession();
String errorMessage = (String) httpSession.getAttribute("errorMessage");
String successMessage = (String) httpSession.getAttribute("successMessage");
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

	<form method="post" action="login">
		<div class="login">
			<div class="container">
				<div class="panel">
					<img class="logo2" src="images/logo.png">
					<ul class="flex-list">
						<li><input class="textarea" type="number" name="id"
							placeholder="社員番号" value="<%= request.getAttribute("id") != null ? request.getAttribute("id") : "" %>" required maxlength="7" oninput="preventInvalidInput(this)"></li>
						
						<style>
					        /* 矢印を非表示にするスタイル */
					        input[type=number]::-webkit-outer-spin-button,
					        input[type=number]::-webkit-inner-spin-button {
					            -webkit-appearance: none;
					            margin: 0;
					        }
					    </style>
						
						<li><input class="textarea" type="password" name="password"
							id="pa" placeholder="パスワード" required oninput="preventInvalidInput2(this)"></li>
						<li><input class="btn2" type="submit" value="ログイン"></li>
						<li><a href="signup">新規登録はこちら</a></li>
						
						<script>
						    function preventInvalidInput(input) {
						        // 全角・半角の空白とe, +, -を取り除く
						        input.value = input.value.replace(/[\u3000\s+eE-]/g, '');
						    }
						    function preventInvalidInput2(input) {
						        // 全角・半角の空白を取り除く
						    	input.value = input.value.replace(/[\u3000\s]+/g, '');
						    }
						</script>
   						
					</ul>
				</div>
			</div>
		</div>
	</form>

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
				if (successMessage != null) {
				%>
					// ユーザ新規登録完了の成功メッセージ
					alert("<%=successMessage%>");
					<% request.getSession().removeAttribute("successMessage"); %>
				<%
				}
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
