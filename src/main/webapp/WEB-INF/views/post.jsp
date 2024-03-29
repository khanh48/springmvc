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
				<c:if test="${not empty post }">
					<div class="content rm" id="${post.getMabaiviet() }">
						<div class='d-flex justify-content-between'>
							<div class=' c-header'>
								<span> <a class='name'
									href='/ho-so/${post.getUser().getTaikhoan() }'> <img
										class='avt' src="${post.getUser().getAnhdaidien() }"
										alt='avatar'></a></span>
								<div class='c-name'>
									<span><a class='name'
										href='/ho-so/${post.getUser().getTaikhoan() }'>${post.getUser().getHoten() }</a>
										<div class='time'>
											<small class='text-secondary'>${post.getDateFormated() }</small>
										</div> </span>
								</div>
							</div>
							<c:if
								test="${post.getUser().getTaikhoan() eq userID.getTaikhoan() || userID.getRank() >= 2 }">
								<button name='delete-notification' class='btn-close py-1 px-3'
									value='a' data-bs-toggle='modal' data-bs-target='#delete-post'
									onclick="deletePost(${post.getMabaiviet()})"></button>
							</c:if>
						</div>
						<div>
							<div class='title'>
								<a class='name' href="/nhom/${post.getManhom() }">${post.getNhom() }</a>
								<span>></span>
								<div class='name'><c:out value="${post.getTieude() }" escapeXml="true" /></div>
							</div>
						</div>
						<div class='c-body'>${post.getNoidung() }</div>
						<div class='m-0 hide wh' style='text-align: end;'>
							<span class='read-more'></span>
						</div>
						<div id="forpost${post.getMabaiviet() }" class="carousel slide mt-1" data-bs-ride="carousel">
							<div class="carousel-inner">
								<c:forEach items="${post.getImage() }" var="i" varStatus="idx">
							
									<div class="carousel-item <c:if test="${idx.first }">active</c:if>">
										<img src="${i.getUrl() }" class="d-block w-100 postImg" alt="...">
									</div>
								</c:forEach>
							</div>
							<button class="carousel-control-prev" type="button" data-bs-target="#forpost${post.getMabaiviet() }" data-bs-slide="prev">
								<span class="carousel-control-prev-icon" aria-hidden="true"></span>
								<span class="visually-hidden">Previous</span>
							</button>
							<button class="carousel-control-next" type="button" data-bs-target="#forpost${post.getMabaiviet() }" data-bs-slide="next">
								<span class="carousel-control-next-icon" aria-hidden="true"></span>
								<span class="visually-hidden">Next</span>
							</button>
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
										<textarea class='form-control f-sm textarea scroll-bar' id='cmtContent'
											placeholder='Bình luận' name='comment' required></textarea>
									</div>
									<button type='submit' value="${post.getMabaiviet() }"
										name='send' class='btn btn-danger mb-2'>Gửi</button>
								</form>
							</div>
						</div>
					</c:if>
				</c:if>
				<hr>
				<div id="listComments">
					<script>
					loadComment(${post.getMabaiviet()});
					</script>
				</div>
				<div id="loadMoreCmt" style="display: none;"  onclick="loadComment(${post.getMabaiviet()})">Tải
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
						data-bs-dismiss="modal" id="cancel-delete">Hủy</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal modal-alert py-5" tabindex="-1" role="dialog"
		id="delete-cmt">
		<div class="modal-dialog" role="document">
			<div class="modal-content rounded-3 shadow">
				<div class="modal-body p-4 text-center">
					<h5 class="mb-0">Xóa bình luận?</h5>
					<p class="mb-0">Bình luận sẽ bị xóa vĩnh viễn.</p>
				</div>
				<div class="modal-footer flex-nowrap p-0">
					<button name="delete-cmt"
						class="btn btn-lg btn-link text-danger fs-6 text-decoration-none col-6 m-0 rounded-0 border-end"
						id="confirm-yes-1">
						<strong>Xóa</strong>
					</button>
					<button type="button"
						class="btn btn-lg btn-link fs-6 text-decoration-none col-6 m-0 rounded-0"
						data-bs-dismiss="modal" id="cancel-delete-1">Hủy</button>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
	<script src="/resources/js/filecustom.js"></script>
</body>
</html>