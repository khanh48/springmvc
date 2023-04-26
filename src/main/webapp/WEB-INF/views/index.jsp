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
				<c:if test="${not empty userID }">
					<div class="content">
						<form action="/addPost" method="post"
							enctype="multipart/form-data">
							<div class="form-group p-1">
								<input type="text" class="form-control f-sm mb-1"
									placeholder="Tiêu đề" name="tieude" required>
								<textarea class="form-control f-sm textarea" placeholder="Nội dung"
									name="noidung" required></textarea>
							</div>
							<div class="group-file">
								<select class="form-select form-select-sm" name="nhom">
									<option value="1" selected hidden>Nhóm</option>
									<c:forEach items="${groups }" var="gr">
										<option value="${gr.getManhom() }">${gr.getTennhom() }</option>
									</c:forEach>
								</select> <input type="file" id="file-1" class="inputfile inputfile-1"
									name="uploadImg" data-multiple-caption="{count} files selected"
									accept="image/*" multiple /> <label for="file-1"> <i
									class="fas fa-images"></i> <span>Choose images&hellip;</span></label>
								<button type="submit" name="post" class="btn btn-danger btn-sm">Đăng</button>
							</div>
						</form>
					</div>
				</c:if>
				<div id="listPosts"></div>
				<script>
					loadPost(null);
				</script>
				<div id="loadMore" onclick="loadPost(null);">Tải thêm bài viết</div>
			</div>
		</div>
	</div>


	<div class="modal modal-alert py-5" tabindex="-1" role="dialog"
		id="delete-post">
		<div class="modal-dialog" role="document">
			<div class="modal-content rounded-3 shadow">
				<div class="modal-body p-4 text-center">
					<h5 class="mb-0">Xóa bài viết?</h5>
					<p class="mb-0">Bài viết sẽ bị xóa vĩnh viễn.</p>
				</div>
				<div class="modal-footer flex-nowrap p-0">
					<button type="button"
						class="btn btn-lg btn-link text-danger fs-6 text-decoration-none col-6 m-0 rounded-0 border-end"
						id="confirm-yes">
						<strong>Xóa</strong>
					</button>
					<button type="button"
						class="btn btn-lg btn-link fs-6 text-decoration-none col-6 m-0 rounded-0"
						data-bs-dismiss="modal" id="cancel-delete">Hủy</button>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
	<script src="/resources/js/filecustom.js"></script>
</body>
</html>