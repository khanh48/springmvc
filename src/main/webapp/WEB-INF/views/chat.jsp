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
				<c:if test="${not empty userID}">
					<div class="text-box-chat pb-3 pt-2">
						<div class="chat-text">
							<c:if test="${chatUser.getTaikhoan() eq 'chatbot' }">
								<button class="btn btn-chat btn-danger btn-sm me-1 ms-0" onclick="stopBotSession()">Dừng</button>
							</c:if>
							<textarea class="chat-input form-control" rows="1"
								placeholder="Tin nhắn"></textarea>
							<button class="btn btn-chat btn-success btn-sm" onclick="sendMessage()">Gửi</button>
						</div>
					</div>
				</c:if>
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
		window.onscroll = function(e) {
			if (document.documentElement.scrollTop == 0) {
				loadMessage("${chatUser.getTaikhoan()}");
			}
		}
	</script>
</body>
</html>