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
				<c:if test="${not empty profile }">
					<div class="content">
						<div class='mt-0 ms-2'>
							<div class=' c-header'>
								<img class='avt-profile rounded-circle'
									src='${profile.getAnhdaidien() }'>
								<div>
									<div class='name'>${profile.getHoten() }</div>
									<p class='ps-2 mb-0'>Bài viết: ${postOfUser.size() }</p>
									<p class='ps-2 mb-0'>Giới tính: ${profile.getGioitinh() }</p>
									<p class='ps-2 mb-0'>Ngày sinh: ${profile.getNgaysinh() }</p>
									<p class='ps-2 mb-0'>Số điện thoại:
										${profile.getSodienthoai() }</p>
									<p class='ps-2 mb-0'>Sở thích: ${profile.getSothich() }</p>
								</div>
							</div>
						</div>
					</div>
					<c:if test="${profile.getTaikhoan() == userID.getTaikhoan()}">
						<div class='content'>
							<div>
								<form action="/updateProfile" method='post' enctype='multipart/form-data'>
									<div class='form-group p-1'>
										<table>
											<tr>
												<td><label for='avt'>Ảnh đại diện:</label></td>
												<td><input type='file' class='form-control f-sm mb-1'
													name='avt' id='avt' accept='image/jpeg,image/jpg,image/png' /></td>
											</tr>
											<tr>
												<td><label for='hoten'>Họ tên:</label></td>
												<td><input type='text' class='form-control f-sm mb-1'
													name='hoten' id='hoten' value='${profile.getHoten()}' /></td>
											</tr>
											<tr>
												<td><label for='gioitinh'>Giới tính:</label></td>
												<td><select id='gioitinh'
													class='form-control f-sm mb-1' name='gioitinh'>
														<option value=''>Khác</option>
														<option value='Nam'
															<c:if test="${profile.getGioitinh() == 'Nam' }">selected</c:if>>Nam</option>
														<option value='Nữ'
															<c:if test="${profile.getGioitinh() == 'Nữ' }">selected</c:if>>Nữ</option>
												</select></td>
											</tr>
											<tr>
												<td><label for='ngaysinh'>Ngày sinh:</label></td>
												<td><input type='date' class='form-control f-sm mb-1'
													name='ngaysinh' id='ngaysinh'
													value="${profile.getNgaysinh()}" /></td>
											</tr>
											<tr>
												<td><label for='sdt'>Số điện thoại:</label></td>
												<td><input type='text' class='form-control f-sm mb-1'
													name='sdt' id='sdt' value='${profile.getSodienthoai()}' /></td>
											</tr>
											<tr>
												<td><label for='sothich'>Sở thích:</label></td>
												<td><textarea class='form-control f-sm' name='sothich' id='sothich'>${profile.getSothich()}</textarea></td>
											</tr>
										</table>
										<div>
											<button class='btn btn-success my-2 f-sm' name='save'>Lưu</button>
											<span class='my-auto' data-bs-toggle='modal'
												data-bs-target='#changePass' id='changePassBtn'>Đổi
												mật khẩu</span>
										</div>
									</div>
								</form>
							</div>
						</div>
					</c:if>
				</c:if>
				<div id="listPosts">
					<c:forEach items="${postOfUser}" var="i">

						<div class='content'>
							<div class='d-flex justify-content-between'>
								<div class=' c-header'>
									<span> <a class='name' href='/ho-so/${i.getUser().getTaikhoan() }'> <img class='avt'
											src="${i.getUser().getAnhdaidien() }" alt='avatar'></a></span>
									<div class='c-name'>
										<span><a class='name' href='/ho-so/${i.getUser().getTaikhoan() }'>${i.getUser().getHoten() }</a>
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
							<c:if test="${not empty i.getImage() }">
								<div id='forpost${i.getMabaiviet() }'
									class='carousel slide mt-1' data-bs-ride='carousel'>
									<div class='carousel-inner '>
										<c:forEach items="${i.getImage()}" var="ima" varStatus="idx">
											<div
												class='carousel-item <c:if test="${idx.first }">active</c:if>'>
												<img src='${ima.getUrl() }' class='d-block w-100 postImg'
													alt='...'>
											</div>

										</c:forEach>

									</div>
									<button class='carousel-control-prev' type='button'
										data-bs-target='#forpost${i.getMabaiviet() }'
										data-bs-slide='prev'>
										<span class='carousel-control-prev-icon' aria-hidden='true'></span>
										<span class='visually-hidden'>Previous</span>
									</button>
									<button class='carousel-control-next' type='button'
										data-bs-target='#forpost${i.getMabaiviet() }'
										data-bs-slide='next'>
										<span class='carousel-control-next-icon' aria-hidden='true'></span>
										<span class='visually-hidden'>Next</span>
									</button>
								</div>
							</c:if>
							<hr class='m-0'>
							<div class='interactive p-1 m-0'>
								<button type='button' class='like p-1'
									onclick="like(${i.getMabaiviet() },true, '${i.getUser().getTaikhoan()}')">
									<i
										class='fas fa-heart action <c:if test="${ i.IsLiked(userID.getTaikhoan()) }">fas-liked</c:if>'
										id='pl${i.getMabaiviet() }'></i> <span class='count-like'
										id='p${i.getMabaiviet() }'> <c:if
											test="${i.getCountLike() > 0}">${i.getCountLike() }</c:if>
									</span>
								</button>
								<button type='button' class='comment p-1'
									onclick="window.location.href='/bai-viet/${i.getMabaiviet()}'">
									<i class='fas fa-comment action'></i> <span
										class='count-comment'> <c:if
											test="${i.getCountComment() > 0}">${i.getCountComment() }</c:if>


									</span>
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
	
	
    <div class="modal modal-alert py-5" tabindex="-1" role="dialog" id="changePass">
        <div class="modal-dialog modal-dialog-centered modal-sm" role="document">
            <div class="modal-content rounded-3 shadow">
                <div class="modal-body p-4">
                    <small class="text-danger" id="failToChangePass"></small>
                    <table>
                        <tr>
                            <td><label class="form-label" for="oldPass">Mật khẩu cũ:</label></td>
                            <td><input class="form-control" type="password" name="oldPass" id="oldPass"></td>
                        </tr>
                        <tr>
                            <td><label class="form-label" for="newPass">Mật khẩu mới:</label></td>
                            <td><input class="form-control" type="password" name="newPass" id="newPass"></td>
                        </tr>
                        <tr>
                            <td><label class="form-label" for="confirmPass">Nhập lại mật khẩu:</label></td>
                            <td><input class="form-control" type="password" name="confirmPass" id="confirmPass"></td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer flex-nowrap p-0">
                    <button
                        class="btn btn-lg btn-link text-success fs-6 text-decoration-none col-6 m-0 rounded-0 border-end"
                        id="changePassword"><strong>Xác nhận</strong></button>
                    <button class="btn btn-lg btn-link fs-6 text-decoration-none col-6 m-0 rounded-0"
                        data-bs-dismiss="modal" id="cancelChange">Hủy</button>
                </div>
            </div>
        </div>
    </div>

	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>
</html>