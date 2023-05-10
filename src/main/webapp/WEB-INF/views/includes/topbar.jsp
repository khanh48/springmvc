<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Navbar -->
<nav class="navbar sticky-top navbar-expand-lg">
	<!-- Container wrapper -->
	<div class="container-fluid">
		<!-- Toggle button -->

		<div class="logo">
			<a class="navbar-brand mt-2 mt-lg-0" href="/"> <img
				src="/resources/images/logo.png" height="50" alt="logo">
			</a><i class="bi bi-bag-fill"></i>
			<form method="get">
				<div class="ms-2 search-group">
					<input class="search" type="text" name="find"
						placeholder="Tìm kiếm" />
					<button type="submit" name="go" class="search-btn">
						<img src="/resources/images/search_icon.png">
					</button>
				</div>
			</form>
		</div>
		<!-- Collapsible wrapper -->

		<!-- Right elements -->
		<div class="d-flex align-items-center nav-right">

			<!-- Icon -->
			<!-- <a class="text-reset me-3" href="#">
                <i class="fas fa-shopping-cart"></i>
            </a> -->
			<c:if test="${empty userID }">
				<a class="nav-link log me-2" href="./search">Tìm kiếm</a>
				<a class="nav-link me-2" href="#" data-bs-toggle='modal'
					data-bs-target='#modal-login'>Đăng nhập</a>
			</c:if>
			<c:if test="${not empty userID }">
				<!-- Notifications -->
				<div class="dropdown">
					<a class="me-3 dropdown" href="#" id="navbarDropdownMenuLink"
						role="button" data-bs-toggle="dropdown" aria-expanded="true">

						<i class="fas fa-bell" id="bell-num"> <c:if
								test="${unread > 0}">
								<span
									class="badge rounded-pill position-absolute top-0 start-100 translate-middle bg-danger"
									id="ntf-num"> <c:if test="${unread < 100}">
									${ unread } 
									</c:if> <c:if test="${unread > 99}">
									99+ 
									</c:if>
								</span>
							</c:if>

					</i>

					</a>
					<ul class="dropdown-menu dropdown-menu-end dropdown-notify"
						id="bell-ntf">
						<c:forEach var="i" items="${listNotify}">
							<li><a class="dropdown-item text-wrap" href="${i.url}">
									<p class="small mb-0">${i.getDateFormated()}</p>
									<p class="mb-0 <c:if test="${not i.trangthai }">unread</c:if>">${i.noidung}</p>
							</a></li>
						</c:forEach>
					</ul>
				</div>


				<div class="dropdown">
					<a class="me-3 dropdown" href="#" id="navbarDropdownMenuChat"
						role="button" data-bs-toggle="dropdown" data-bs-auto-close="outside" aria-expanded="true">

						<i class="fas fa-comments" id="bell-msg"> <c:if
								test="${unreadMessage > 0}">
								<span
									class="badge rounded-pill position-absolute top-0 start-100 translate-middle bg-danger"
									id="msg-num"> <c:if test="${unreadMessage < 100}">
									${ unreadMessage } 
									</c:if> <c:if test="${unreadMessage > 99}">
									99+ 
									</c:if>
								</span>
							</c:if>

					</i>

					</a>
					<div class="dropdown-menu dropdown-menu-end dropdown-notify"
						id="chat-users" >
						<div class="chat-menu">
							<div class="input-group">
								<input class="form-control" type="text" placeholder="ID hoặc tên" aria-describedby="button-addon1" autocomplete="off" id="searchToChat" />
								<a class="btn btn-outline-secondary" href="/chat/chatbot" type="button" id="button-addon1">
						<i class="fas fa-robot"></i></a>
							</div>
							<div id="schat-result">
							<!-- 
								<a class='dropdown-item d-flex' href='/chat/admin'>
									<img class='rounded-circle' height="40" width="40" src='/resources/images/default_avatar.png'>
									<span class="ms-1">
										<span class="fw-bold" style="font-size: 0.9em">ADMIN</span>
										<small class="d-block">Đang hoạt động</small>
									</span>
								</a>
								 -->
							</div>
						</div>
						<hr class="dropdown-divider">
						<div class="chat-container">
							<c:forEach items="${listMessage }" var="i">
								<a id="chat-${i.getNguoigui().getTaikhoan() }"
									class="dropdown-item text-wrap" style="display: flex;"
									href="/chat/${i.getNguoigui().getTaikhoan() }"> <span><img
										alt="" class="avt" src="${i.getNguoigui().getAnhdaidien() }">
								</span> <span class="overflow-hidden w-100 ms-1"> <span
										class="d-flex justify-content-between"> <span
											class="fw-bold">${i.getNguoigui().getHoten() }</span> <small
											class="small">${i.getFomattedDate() }</small>
									</span>
										<p
											class="mb-0 preview-message <c:if test="${not i.isTrangthai() }">unread</c:if>">
											<c:out value="${i.getNoidung()}" escapeXml="true" />
										</p>
								</span>
								</a>
							</c:forEach>

						</div>
					</div>
				</div>
				<!-- Avatar -->
				<div class="dropdown">
					<a class="dropdown" href="#" id="navbarDropdownMenuAvatar"
						role="button" data-bs-toggle="dropdown"
						data-bs-auto-close="outside" aria-expanded="false"> <img
						src="${userID.anhdaidien }" class="rounded-circle" height="30"
						width="30" alt="avatar">
					</a>
					<ul class="dropdown-menu dropdown-menu-end"
						aria-labelledby="navbarDropdownMenuAvatar">
						<li><a class="dropdown-item" href="/ho-so">Trang cá nhân</a></li>

						<li><a class="dropdown-item" href="./notification">Thông
								báo</a></li>
						<li class="log"><a class="dropdown-item" href="./search">Tìm
								kiếm</a></li>

						<c:if test="${userID.getRank() >= 2 }">
							<li><a class="dropdown-item" data-bs-toggle="collapse"
								data-bs-target="#menu_item1" href="">Quản lý&ensp;<i
									class="fa-solid fa-caret-down"></i>
							</a>
								<ul id="menu_item1" class="submenu collapse">
									<li><a class="dropdown-item" href="/quan-ly">Thành
											viên</a></li>
									<li><a class="dropdown-item" href="/quan-ly-bai-viet">Bài
											viết</a></li>
									<li><a class="dropdown-item" href="#">Phê duyệt</a></li>
								</ul></li>
						</c:if>

						<li>
							<hr class="dropdown-divider">
						</li>
						<li><a class="dropdown-item" href="/logout">Đăng xuất</a></li>
					</ul>
				</div>
			</c:if>
		</div>
		<!-- Right elements -->
	</div>
	<!-- Container wrapper -->
</nav>
<!-- Navbar -->
<%@ include file="/WEB-INF/views/includes/loginform.jsp"%>