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
					<li class="chat">
						<div>
							<span><img class='avatar-chat'
								src="${userID.getAnhdaidien() }" alt='avatar'></span>
							<div class="chat-content other-chat">hihi</div>
						</div>
					</li>
					<li class="chat">
						<div>
							<div class="chat-content my-chat">hihi1hiahisdhaisdhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
						</div>
					</li>
					<li class="chat">
						<div>
							<div class="chat-content my-chat">hihi1hiahisdhaisdhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
						</div>
					</li>
					<li class="chat">
						<div>
							<span><img class='avatar-chat'
								src="${userID.getAnhdaidien() }" alt='avatar'></span>
							<div class="chat-content other-chat">hihi1hiahisdhaisdhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
						</div>
					</li>
					
					<li class="chat">
						<div>
							<span><img class='avatar-chat'
								src="${userID.getAnhdaidien() }" alt='avatar'></span>
							<div class="chat-content other-chat">hihi1hiahisdhaisdhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
						</div>
					</li>
					<li class="chat">
						<div>
							<span><img class='avatar-chat'
								src="${userID.getAnhdaidien() }" alt='avatar'></span>
							<div class="chat-content other-chat">hihi1hiahisdhaisdhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
						</div>
					</li>
					<li class="chat">
						<div>
							<span><img class='avatar-chat'
								src="${userID.getAnhdaidien() }" alt='avatar'></span>
							<div class="chat-content other-chat">hihi1hiahisdhaisdhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
						</div>
					</li>
					<li class="chat">
						<div>
							<span><img class='avatar-chat'
								src="${userID.getAnhdaidien() }" alt='avatar'></span>
							<div class="chat-content other-chat">hihi1hiahisdhaisdhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</div>
						</div>
					</li>
				</ul>
				<div class="text-box-chat">a</div>
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
	<script src="/resources/js/filecustom.js"></script>
</body>
</html>