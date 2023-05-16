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
							placeholder="Tìm kiếm" name="search-content">
					</div>
					<div class="group-file">
						<select class="form-select form-select-sm" name="type">
							<option selected disabled value="" required>Tìm kiếm
								theo..</option>
							<option value="0">Họ tên</option>
							<option value="1">Tiêu đề bài viết</option>
						</select>
						<button type="submit" name="search" class="btn btn-danger btn-sm">Tìm</button>
					</div>
				</form>
			</div>
			<div class='content'>
				<div class='pb-2'>
					<div class=' c-header'>
						<span> <img class='avt' src='#'></span>
						<div class='c-name'>
							<span>
								<div class='name'>Ten</div>
								<div class='time'>
									<small class='text-secondary'>Hoạt động ... phút trước</small>
								</div>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>

</html>