<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>
<body>
	<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
	<div class="main">
		<%@ include file="/WEB-INF/views/includes/containerleft.jsp"%>
		<div class="right">
			<div class="content">
				<form method="get">
					<div class="form-group p-1">
						<input type="text" class="form-control f-sm mb-1"
							placeholder="Tìm kiếm" name="value">
					</div>
					<div class="group-file">
						<select class="form-select form-select-sm" name="type">
							<option selected hidden value="0">Tìm kiếm
								theo..</option>
							<option value="0">Người dùng</option>
							<option value="1">Bài viết</option>
						</select>
						<button type="submit" name="search" class="btn btn-danger btn-sm">Tìm</button>
					</div>
				</form>
			</div>
			<c:forEach items="${search_posts }" var="p">
			${p }
			</c:forEach>
			
			<c:forEach items="${search_users }" var="i">
				<div class='content'>
					<div class='pb-2'>
						<a class='c-header' href="/ho-so/${i.getTaikhoan() }">
							<span> <img class='avt' src='${i.getAnhdaidien() }'></span>
							<div class='c-name'>
								<span>
									<div class='name'>${i.getHoten() }</div>
									<div class='time'>
										<i class="fas fa-circle ${i.getStatus() }"></i>
										<small class='text-secondary'>${i.getLastLogin()}</small>
									</div>
								</span>
							</div>
						</a>
					</div>
				</div>
			</c:forEach>
		</div>

	</div>
	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>

</html>