<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="dto.Term"%>
<%@ page import="dto.User"%>
<%
HttpSession httpSession = request.getSession();
User user = (User) httpSession.getAttribute("user");
String successMessage = (String) httpSession.getAttribute("successMessage");
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

			<!--ここから画面左側-->
			<div class="listContainer">
				<!-- 新着用語一覧 -->
				<div id="list-left">
					<a href="result?searchType=keyTerms">
						<p id="pickup-p">注目用語</p>
					</a>
					<ul>
						<%@ page import="java.util.List"%>
						<%@ page import="dto.Term"%>
						<%
						List<Term> keyTerms = (List<Term>) request.getAttribute("keyTerms");
						%>
						<%
						if (keyTerms != null && !keyTerms.isEmpty()) {
						%>
						<%
						for (Term term : keyTerms) {
						%>
						<li><a href="detail?termId=<%=term.getId()%>"><%=term.getTermName()%></a></li>
						<%
						}
						%>
						<%
						} else {
						%>
						<li>注目用語はありません。</li>
						<%
						}
						%>
					</ul>
				</div>

				<!-- アクセスランキング -->
				<div id="list-left">
					<p>アクセスランキング</p>
					<ul>
						<%
						List<Term> accessRankingTerms = (List<Term>) request.getAttribute("accessRankingTerms");
						%>
						<%
						if (accessRankingTerms != null && !accessRankingTerms.isEmpty()) {
							int rank = 1; // 順位を管理するための変数
						%>
						<%
						for (Term term : accessRankingTerms) {
						%>
						<li><a href="detail?termId=<%=term.getId()%>"><%=rank + "位 " + term.getTermName()%></a></li>
						<%
						rank++; // 次の順位に進める
						}
						%>
						<%
						} else {
						%>
						<li>アクセスランキングはありません。</li>
						<%
						}
						%>
					</ul>
				</div>


				<!-- ポイントランキング -->
				<div id="list-left">
					<p>役に立ったランキング</p>
					<ul>
						<%
						List<Term> pointRankingTerms = (List<Term>) request.getAttribute("pointRankingTerms");
						%>
						<%
						if (pointRankingTerms != null && !pointRankingTerms.isEmpty()) {
							int rank = 1; // 順位を管理するための変数
						%>
						<%
						for (Term term : pointRankingTerms) {
						%>
						<li><a href="detail?termId=<%=term.getId()%>"><%=rank + "位 " + term.getTermName()%></a></li>
						<%
						rank++; // 次の順位に進める
						}
						%>
						<%
						} else {
						%>
						<li>役に立ったランキングはありません。</li>
						<%
						}
						%>
					</ul>
				</div>
			</div>

			<!--ここまで画面左側-->

			<!--ここから画面右側-->
			<div class="searchContainer">

				<div class="category">
					<p>カテゴリから探す</p>
					<div class="flex">
						<ul id="flexItem">
							<li><img id="pickupIcon" src="images/pickup.png"></li>
							<li><a href="result?searchType=keyTerms" id="hoge">注目用語</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="newIcon" src="images/new.png"></li>
							<li><a href="result?searchType=newTerms" id="hoge">新着用語</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="requestIcon" src="images/request.png"></li>
							<li><a href="result?searchType=requestTerms" id="hoge">リクエスト用語</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="compIcon" src="images/comp.png"></li>
							<li><a href="result?searchType=compTerms">社内用語</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="dicIcon" src="images/dic.png"></li>
							<li><a href="result?searchType=depTerms&depId=12">一般用語</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=2">経営企画室</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=1">出向部</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=3">総務部</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=5">戦略事業部</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=6">金融第一</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=7">金融第二</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=4">IT経基</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=9">産業第一</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=10">産業第二</a></li>
						</ul>
						<ul id="flexItem">
							<li><img id="departIcon" src="images/depart.png"></li>
							<li><a href="result?searchType=depTerms&depId=8">DXソ</a></li>
						</ul>


					</div>
				</div>

				<div class="order">
					<p>頭文字から探す</p>
					<div class="flex">
						<a id="flexItem" href="result?searchType=iniTerms&initial=あいうえお">あ行</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=かきくけこ">か行</a> <a id="flexItem" href="result?searchType=iniTerms&initial=さしすせそ">さ行</a>
						<a id="flexItem" href="result?searchType=iniTerms&initial=たちつてと">た行</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=なにぬねの">な行</a> <a id="flexItem" href="result?searchType=iniTerms&initial=はひふへほ">は行</a>
						<a id="flexItem" href="result?searchType=iniTerms&initial=まみむめも">ま行</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=やゆよ">や行</a> <a id="flexItem" href="result?searchType=iniTerms&initial=らりるれろわをん">ら/わ行</a>
						<a id="flexItem" href="result?searchType=iniTerms&initial=ABC">A/B/C</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=DEF">D/E/F</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=GHI">G/H/I</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=JKL">J/K/L</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=MNO">M/N/O</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=PQR">P/Q/R</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=STU">S/T/U</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=VWX">V/W/X</a> <a id="flexItem"
							href="result?searchType=iniTerms&initial=YZ">Y/Z</a>
					</div>
				</div>

			</div>

			<!--ここまで画面右側-->
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