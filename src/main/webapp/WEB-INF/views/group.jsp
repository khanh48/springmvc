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
			<c:forEach items="${entities.get('group') }" var="i" varStatus="idx">
				<div class="content">
					<div class="c-body">
						<a class="fs-4" href="/nhom/${i.getManhom() }">${i.getTennhom()}</a>
						<div>
							<span>Mô tả:</span> <span><c:out value="${i.getMota()}"></c:out></span>
						</div>
						<div>
							<span>Bài viết:</span> <span>${entities.get("count")[idx.index]}</span>
						</div>

						<div>
							<span>Bài viết nổi bật:</span> <a class="link-dark" href="/bai-viet/${entities.get('hot')[idx.index].getMabaiviet() }"><i><u><c:out value="${entities.get('hot')[idx.index].getTieude()}" escapeXml="true" /></u></i></a>
						</div>
						<div>
							<span>Bài viết sôi nổi:</span> <a class="link-dark" href="/bai-viet/${entities.get('exalted')[idx.index].getMabaiviet() }"><i><u><c:out value="${entities.get('exalted')[idx.index].getTieude()}" escapeXml="true" /></u></i></a>
						</div>
					</div>
				</div>
				</c:forEach>
				
				<c:if test="${ empty entities}">
				<div class="content">
					<div class="c-body">
						<span class="fs-4" href="/nhom/${group.getManhom() }">${group.getTennhom()}</span>
						<c:if test="${userID.getRank() >= 1 }">
							<button class="btn-edit" value="${group.getManhom() }" onclick="editGroup(this);"></button>
						</c:if>
						<div>
							<span>Mô tả:</span> <span id="group-description"><c:out value="${group.getMota()}"></c:out></span>
							<textarea class="form-control" style="display: none;" id="group-description-edit">aaa</textarea>
						</div>
						<div>
						<span>Sắp xếp:</span>
						<form id="sortting" value="${group.getManhom() }">
  							<input class="form-check-input" type="radio" value="0" name="sortting" id="byDate" checked>
  							<label class="form-check-label" for="byDate">Theo ngày</label>
  							<input class="form-check-input" type="radio" value="1" name="sortting" id="byLike">
  							<label class="form-check-label" for="byLike">Theo lượt thích</label>
  							<input class="form-check-input" type="radio" value = 2 name="sortting" id="byComment">
  							<label class="form-check-label" for="byComment">Theo bình luận</label>
							<input class="form-check-input" type="checkbox" value="" id="ascendingCbx">
	  						<label class="form-check-label" for="ascendingCbx">Tăng dần</label>
  						</form>
						</div>
					</div>
				</div>
				<hr>
				<div id="listPosts"></div>
				<script>
					loadPostWithSort(${group.getManhom()}, true);
					$("#sortting").on("change", function(e) {
						loadPostWithSort(${group.getManhom()}, true);
					});
				</script>
				<div id="loadMore" style="display: none;" onclick="loadPostWithSort(${group.getManhom()}, false);">Tải thêm bài viết</div>
				</c:if>
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
</body>
</html>