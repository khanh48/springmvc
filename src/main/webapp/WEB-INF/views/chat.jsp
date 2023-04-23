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
					<c:forEach items="${messageList }" var="msg">

						<li class="chat">
							<div>
								<c:if
									test="${msg.getNguoigui().getTaikhoan() != userID.getTaikhoan() }">
									<span> <img class='avatar-chat'
										src="${msg.getNguoigui().getAnhdaidien() }" alt='avatar'></span>
								</c:if>
								<div
									class="chat-content <c:if test="${userID.getTaikhoan() == msg.getNguoigui().getTaikhoan() }">my-chat</c:if>
									<c:if test="${userID.getTaikhoan() != msg.getNguoigui().getTaikhoan() }">other-chat</c:if>">${msg.getNoidung() }</div>
							</div>
						</li>
					</c:forEach>

				</ul>
				<div class="text-box-chat">
					<button onclick="stopBotSession()">stop</button>
					<div class="chat-text">
						<div class="chat-input" contenteditable="true"></div>
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
	</script>
</body>
</html>