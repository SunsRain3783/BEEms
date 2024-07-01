<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="dto.User"%>
<%@ page import="dto.Term"%>
<%
HttpSession httpSession = request.getSession();
User user = (User) httpSession.getAttribute("user");
String successMessage = (String) httpSession.getAttribute("successMessage");
String userId = String.valueOf(user.getId());
Term term = (Term) request.getAttribute("term"); // 用語を取得
String depname = (String) request.getAttribute("depname"); // 部門名を取得
boolean isAdminDepartment = user.getDepartmentName().equals("BEEms管理部門"); // 管理部門かどうか判定
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

			<div class="termContainer">

				<ul class="edit">
				
					<li id="padright">
					
					<form id="ButtonForm" action="<%= isAdminDepartment ? "keyTermRequest" : "deleteRequest" %>" method="post">
			            <input type="hidden" name="termId" value="<%=term.getId()%>">
			            <% if (isAdminDepartment) { %>
				            <button type="submit" class="btn7" id="Button" <% if (term.isKeyTerm()) {%> style="display:none;" <% } %>>
				                注目用語に設定する
				            </button>
				            <button type="submit" class="btn7" id="Button" <% if (!term.isKeyTerm()) {%> style="display:none;" <% } %>>
				                注目用語に設定済み
				            </button>
				            <a class="btn6" href="termDelete?termId=<%=term.getId()%>" onclick="return confirm('本当に削除しますか?');">削除</a>
			            <% } else { %>
			            	<button type="submit" class="btn3" id="Button" <% if (term.isRequestDeletion()) {%> style="display:none;" <% } %>>
				                削除依頼
				            </button>
				            <button type="submit" class="btn3" id="Button" <% if (!term.isRequestDeletion()) {%> style="display:none;" <% } %>>
				                削除依頼済み
				            </button>
			            <% } %>
			        </form>
			        
			        <script type="text/javascript">
					    document.addEventListener('DOMContentLoaded', function() {
					            document.getElementById('Button').addEventListener('click', function(event) {
					                event.preventDefault(); // フォームのデフォルトの送信を防ぐ
					                document.getElementById('ButtonForm').submit(); // フォームをサブミット
					            });
					    });
					</script>
			        
					<li><a class="btn2" href="termUpdate?termId=<%=term.getId()%>" id="edbtn">編集</a></li>

				</ul>

				<div class="content">
					<p><%=term.getTermName()%></p>

					<ul class="termul">
						<li>略称：<%=term.getAbbreviation().isEmpty() ? "登録されていません" : term.getAbbreviation()%></li>
						<li>関連部門：<%=depname%></li>
					</ul>

					<span><%=term.getDefinition()%></span> <span id="lastp">最終編集者：<%=term.getLastEditor()%></span>

					<!-- フィードバック用ボタン -->
					<form id="feedbackForm" action="helpful" method="post">
						<input type="hidden" name="termId" value="<%=term.getId()%>">
						<input type="hidden" id="feedback" name="feedback" value="">
						<button type="submit" class="feedback-button" data-value="1">
							<i class="fa-regular fa-face-smile-beam" id="point">役に立った</i>
						</button>
						<button type="submit" class="feedback-button" data-value="0">
							<i class="fa-regular fa-face-meh" id="point">まあまあ</i>
						</button>
						<button type="submit" class="feedback-button" data-value="-1">
							<i class="fa-regular fa-face-tired" id="point">役に立たなかった</i>
						</button>
					</form>

					<script type="text/javascript">
					    document.querySelectorAll('.feedback-button').forEach(button => {
					        button.addEventListener('click', function () {
					            // フィードバックの値をセットしてフォームを送信
					            document.getElementById('feedback').value = this.getAttribute('data-value');
					            document.getElementById('feedbackForm').submit();
					        });
					    });
					</script>
					<br>

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
				if (successMessage != null) {
				%>
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