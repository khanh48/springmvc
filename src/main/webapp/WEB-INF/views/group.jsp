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
						<div class="d-flex justify-content-between">
							<a class="fs-4" href="/nhom/${i.getManhom() }">${i.getTennhom()}</a> <i class="fas fa-link-slash"></i>
						</div>
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
						<div class="d-flex justify-content-between">
							<span class="fs-4" href="/nhom/${group.getManhom() }">${group.getTennhom()}</span> <i class="fas fa-link-slash"></i>
						</div>
						<div>
							<span>Mô tả:</span> <span><c:out value="${group.getMota()}"></c:out></span>
						</div>
					</div>
				</div>
				<hr>
				<div class="content">
					<div class="c-body">
						<div class="d-flex justify-content-between">
							<span class="fs-4" href="/nhom/${group.getManhom() }">${group.getTennhom()}</span> <i class="fas fa-link-slash"></i>
						</div>
						<div>
							<span>Mô tả:</span> <span><c:out value="${group.getMota()}"></c:out></span>
						</div>
					</div>
				</div>
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