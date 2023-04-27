<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="/WEB-INF/views/includes/header.jsp"%>
<body>
	<div class="body">
		<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
		<div class="main">
			<%@ include file="/WEB-INF/views/includes/containerleft.jsp"%>
			<div class="right">
				<ul class="list-message">
				</ul>
				<script>
				loadMessage("${chatUser.getTaikhoan()}");
				</script>
				<div class="text-box-chat">
					<button onclick="stopBotSession()">stop</button>
					<div class="chat-text">
						<textarea class="chat-input"></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="toast-container position-fixed bottom-0 start-0 p-3">
		<div id="liveToast" class="toast" role="alert" aria-live="assertive"
			aria-atomic="true">
			<div class="toast-header">
				<!-- <img src="..." class="rounded me-2" alt="..."> -->
				<strong class="me-auto" id="headerToast"></strong> <small
					id="toastTime"></small>
				<button type="button" class="btn-close" data-bs-dismiss="toast"
					aria-label="Close"></button>
			</div>
			<div class="toast-body" id="toastMessage"></div>
		</div>
	</div>

	<footer id="ft">
		<div class="top animated"></div>
	</footer>
	<script src="/resources/js/marked.min.js"></script>
	<script src="/resources/js/filecustom.js"></script>
	<script>
		Prism.highlightAll();
		window.onscroll = function(e){
			if(document.documentElement.scrollTop == 0){
				loadMessage("${chatUser.getTaikhoan()}");
			}
		}
	</script>
</body>
</html>