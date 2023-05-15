<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>
<body>
	<div class="body">
		<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
		<div class="main">
			<%@ include file="/WEB-INF/views/includes/containerleft.jsp"%>
			<div class="right">
				<div class="content mb-3">
					<form method="post">
						<ul class="nav justify-content-center">
							<li class="nav-item">
								<button name="makeAsRead" class="nav-link invisible-button">
									Đánh dấu tất cả là đã đọc</button>
							</li>
							<li class="nav-item">
								<button name="deleteNotifications"
									class="nav-link invisible-button">Xóa tất cả thông báo
									đã đọc</button>
							</li>
						</ul>
					</form>
				</div>

				<div class="content">
					<c:forEach items="${listNotify }" var="i" varStatus="idx">
						<div class="alert alert-dismissible py-0 my-0 py-1" id="ntf-${i.getMathongbao()}">
							<button onclick="deleteNotification(${i.getMathongbao() })" class="btn-close py-1"></button>
							<a href="${i.getUrl() }">
								<div class="notification d-flex justify-content-between">
									<span class='<c:if test="${!i.isTrangthai() }">unread</c:if>'>${i.getNoidung() }</span>
									<small class="text-secondary">${i.getDateFormated()}</small>
								</div>
							</a>
						</div>
						<c:if test="${!idx.last }">
							<hr class="mx-2 my-0">
						</c:if>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>
</html>