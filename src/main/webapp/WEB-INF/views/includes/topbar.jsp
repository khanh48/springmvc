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

						<i class="fas fa-bell" id="bell-num"> 
						<c:if test="${listNotify.size() > 0}">
								<span
									class="badge rounded-pill position-absolute top-0 start-100 translate-middle bg-danger"
									id="ntf-num"> 
									
									<c:if test="${listNotify.size() < 100}">
									${ listNotify.size() } 
									</c:if>
									<c:if test="${listNotify.size() > 99}">
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
				<!-- Avatar -->
				<div class="dropdown">
					<a class="dropdown" href="#" id="navbarDropdownMenuAvatar"
						role="button" data-bs-toggle="dropdown" aria-expanded="false">
						<img src="${userID.anhdaidien }" class="rounded-circle"
						height="25" alt="avatar">
					</a>
					<ul class="dropdown-menu dropdown-menu-end"
						aria-labelledby="navbarDropdownMenuAvatar">
						<li><a class="dropdown-item"
							href="./profile?user=<?php echo $my_id; ?>">Trang cá nhân</a></li>

						<li><a class="dropdown-item" href="./notification">Thông
								báo</a></li>
						<li class="log"><a class="dropdown-item" href="./search">Tìm
								kiếm</a></li>
						<li><a class="dropdown-item" href="./admin">Quản lý</a></li>
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