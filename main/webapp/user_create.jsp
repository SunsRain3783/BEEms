<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
HttpSession httpSession = request.getSession();
String errorMessage = (String) httpSession.getAttribute("errorMessage");
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

	<div class="signup">
		<div class="container">
			<div class="signuppanel">
				<img class="logo3" src="images/logo.png">
				<form action="signup" method="post">
					<ul class="flex-list">
						
						<li><input class="textarea" type="number" name="id"
							placeholder="社員番号" required maxlength="7" oninput="preventInvalidInput(this)"></li>
						
						<style>
					        /* 矢印を非表示にするスタイル */
					        input[type=number]::-webkit-outer-spin-button,
					        input[type=number]::-webkit-inner-spin-button {
					            -webkit-appearance: none;
					            margin: 0;
					        }
					    </style>
						
						<li><input class="textarea" type="text" name="username"
							placeholder="氏名" required></li>
						
						<li>
							<div class="select">
								<select name="departmentId">
									<option value="1">出向部</option>
									<option value="2">経営企画室</option>
									<option value="3">総務部</option>
									<option value="4">IT経営基盤統括部</option>
									<option value="5">戦略事業部</option>
									<option value="6">金融第一事業部</option>
									<option value="7">金融第二事業部</option>
									<option value="8">DXソリューション事業部</option>
									<option value="9">産業第一事業部</option>
									<option value="10">産業第二事業部</option>
									<option value="11">BEEms管理部門</option>
								</select>
							</div>
						</li>
						<li><input class="textarea" type="password" name="password"
							id="pa" placeholder="パスワード" required oninput="preventInvalidInput2(this)"></li>
						<li><input class="textarea" type="password" name="password2"
							placeholder="パスワード確認" required oninput="preventInvalidInput2(this)"></li>
						<li><input class="btn2" type="submit" value="新規登録" required></li>
						<li><a href="login">ログインはこちら</a></li>
						
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