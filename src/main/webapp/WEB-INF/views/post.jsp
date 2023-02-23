<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>

<body>
	<div class="body">
		<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
		<div class="main">
			<div class="right">
				<c:if test="${not empty post }">
					<div class="content">
						<div class='d-flex justify-content-between'>
							<div class=' c-header'>
								<span> <a class='name' href='#'> <img class='avt'
										src="${post.getUser().getAnhdaidien() }" alt='avatar'></a></span>
								<div class='c-name'>
									<span><a class='name' href='#'>${post.getUser().getHoten() }</a>
										<div class='time'>
											<small class='text-secondary'>${post.getDateFormated() }</small>
										</div> </span>
								</div>
							</div>
							<button name='delete-notification' class='btn-close py-1 px-3'
								value='a' data-bs-toggle='modal' data-bs-target='#delete-post'
								onclick="deletePost(${post.getMabaiviet()})"></button>
						</div>
						<div>
							<div class='title'>
								<div class='name'>${post.getNhom() }</div>
								<span>></span>
								<div class='name'>${post.getTieude() }</div>
							</div>
						</div>
						<div class='c-body'>${post.getNoidung() }</div>
						<div class='m-0 hide wh' style='text-align: end;'>
							<span class='read-more'></span>
						</div>
						<hr class='m-0'>
						<div class='interactive p-1 m-0'>
							<button type='button' class='like p-1'
								onclick="like( ${post.getMabaiviet() },true, '${post.getUser().getTaikhoan()}')">
								<i
									class='fas fa-heart action <c:if test="${ post.IsLiked(userID.getTaikhoan()) }">fas-liked</c:if>'
									id='pl${post.getMabaiviet() }'></i> <span class='count-like'
									id='p${post.getMabaiviet() }'> <c:if
										test="${post.getCountLike() > 0}">${post.getCountLike() }</c:if>
								</span>
							</button>
							<button type='button' class='share p-1'>
								<i class='fas fa-share action'></i><span class='count-share'></span>
							</button>
						</div>

					</div>
					<c:if test="${not empty userID }">
						<div class='content'>
							<div>
								<form action="/addComment" method='post' id='sendCmt'>
									<div class='form-group p-1'>
										<textarea class='form-control f-sm' id='cmtContent'
											placeholder='Bình luận' name='comment' required></textarea>
									</div>
									<button type='submit' value="${post.getMabaiviet() }"
										name='send' class='btn btn-danger mb-2'>Gửi</button>
								</form>
							</div>
						</div>
					</c:if>
				</c:if>
					<div id="listComments">
						<c:forEach items="${comments}" var="i">

							<div class='content rm'>
								<div class='d-flex justify-content-between'>
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
										onclick="deletePost(${i.getMabinhluan()})"></button>
								</div>
								<div class='c-body'>${i.getNoidung() }</div>
								<div class='m-0 hide wh' style='text-align: end;'>
									<span class='read-more'></span>
								</div>
								<hr class='m-0'>
								<div class='interactive p-1 m-0'>
									<button type='button' class='like p-1'
										onclick="like(${i.getMabinhluan() }, false, '${i.getUser().getTaikhoan()}')">
										<i
											class='fas fa-heart action <c:if test="${ i.IsLiked(userID.getTaikhoan()) }">fas-liked</c:if>'
											id='cl${i.getMabinhluan() }'></i> <span class='count-like'
											id='c${i.getMabinhluan() }'> <c:if
												test="${i.getCountLike() > 0}">${i.getCountLike() }</c:if>
										</span>
									</button>
								</div>
							</div>

						</c:forEach>
					</div>
					<div id="loadMoreCmt" onclick="loadMore(${post.getMabaiviet()})">Tải
						thêm bình luận</div>

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