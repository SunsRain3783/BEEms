<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.User"%>
<%
HttpSession httpSession = request.getSession();
User user = (User) httpSession.getAttribute("user");
String successMessage = (String) httpSession.getAttribute("successMessage");
String userId = String.valueOf(user.getId());
String userName = user.getUsername();
String departmentName = user.getDepartmentName();
boolean isAdminDepartment = user.getDepartmentName().equals("BEEms管理部門");
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

    <!--ここからヘッダー-->
	<header>
		<div class="container">
			<a href="top"><img class="logo" src="images/logo.png"></a>
			<form id="searchForm" action="result" method="get">
				<input type="hidden" name="searchType" value="keyword"> <input
					id="sbox1" name="keyword" type="text" placeholder="キーワードを入力"
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
				<li><a href="mypage"><%= userId %></a></li>
			</ul>
		</div>
	</header>
	<!--ここまでヘッダー-->


    <div class="main">
        <div class="container">

            <div class="mypage-info">

                <img id="mypage-userIcon" src="images/userIcon.png">

                <ul>
                    <li><span>社員番号　　<%= userId %></span></li>
                    <li><span>氏名　　　　<%= userName %></span></li>
                    <li><span>部門名　　　<%= departmentName %></span></li>
                </ul>

                <a href="mypageCustom">編集</a><br>
                <a href="logout">ログアウト</a>

            </div>

            <div class="mypage-flex">

                <ul>
                    <li><button class="btn" onclick="location.href='result?searchType=logTerms'">閲覧履歴</button></li>
                    <li><button class="btn" onclick="location.href='result?searchType=registerTerms'">登録用語</button></li>
                </ul>
                <ul>
                    <% if (isAdminDepartment) { %>
                    <li><button class="btn10" id="delreq" onclick="location.href='result?searchType=deleteTerms'">削除依頼一覧</button></li>
                    <%} %>
                </ul>

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
