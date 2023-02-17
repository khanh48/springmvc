<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>

<body>
	<div class="body">
		<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
		<div class="main">
			<div class="right">
				<div class="content">
					<form method="post" enctype="multipart/form-data">
						<div class="form-group p-1">
							<input type="text" class="form-control f-sm mb-1"
								placeholder="Tiêu đề" name="tieude" required>
							<textarea class="form-control f-sm" placeholder="Nội dung"
								name="noidung" required></textarea>
						</div>
						<div class="group-file">
							<select class="form-select form-select-sm" name="nhom">
								<option selected disabled value="">Nhóm</option>
								<option value="Bắc">Bắc</option>
								<option value="Trung">Trung</option>
								<option value="Nam">Nam</option>
							</select> <input type="file" id="file-1" class="inputfile inputfile-1"
								name="uploadImg[]"
								data-multiple-caption="{count} files selected" accept="image/*"
								multiple /> <label for="file-1"> <i
								class="fas fa-images"></i> <span>Choose images&hellip;</span></label>
							<button type="submit" name="post" class="btn btn-danger btn-sm">Đăng</button>
						</div>
					</form>
				</div>
				<div id="listPosts">
					<c:forEach items="${posts}" var="i">

						<div class='content rm'>
							<div class='d-flex justify-content-between $loggedin'>
								<div class=' c-header'>
									<span> <a class='name' href='#'> <img class='avt'
											src="${i.getUser().getAnhdaidien() }" alt='avatar'></a></span>
									<div class='c-name'>
										<span><a class='name' href='#'>${i.getUser().getHoten() }</a>
											<div class='time'>
												<small class='text-secondary'>${i.getDateFormated() }</small>
											</div> </span>
									</div>
								</div>
								<button name='delete-notification' class='btn-close py-1 px-3'
									value='a' data-bs-toggle='modal' data-bs-target='#delete-post'
									onclick="deletePost(${i.getMabaiviet()})"></button>
							</div>
							<div>
								<div class='title'>
									<div class='name'>${i.getNhom() }</div>
									<span>></span>
									<div class='name'>${i.getTieude() }</div>
								</div>
							</div>
							<div class='c-body'>${i.getNoidung() }</div>
							<div class='m-0 hide wh' style='text-align: end;'>
								<span class='read-more'></span>
							</div>
							<hr class='m-0'>
							<div class='interactive p-1 m-0'>
								<button type='button' class='like p-1'
									onclick="like(${i.getMabaiviet() },true, '${i.getUser().getTaikhoan()}')">
									<i
										class='fas fa-heart action <c:if test="${i.IsLiked(userID.getTaikhoan(), true) }">fas-liked</c:if>'
										id='pl${i.getMabaiviet() }'></i> <span class='count-like'
										id='p${i.getMabaiviet() }'>${i.getCountLike() }</span>
								</button>
								<button type='button' class='comment p-1'
									onclick="window.location.href='./post.php?id='">
									<i class='fas fa-comment action'></i> <span
										class='count-comment'>${i.getCountComment() }</span>
								</button>
								<button type='button' class='share p-1'>
									<i class='fas fa-share action'></i><span class='count-share'></span>
								</button>
							</div>
						</div>

					</c:forEach>
				</div>
				<div id="loadMore">Tải thêm bài viết</div>
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
							data-bs-dismiss="modal">Hủy</button>
					</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
	<script src="/resources/js/filecustom.js"></script>
</body>
</html>