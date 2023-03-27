<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>
<body>
	<div class="body">
		<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
		<div class="main d-block">
			<div class="content w-100" id="qltv">
				<div>
					<h3 class="ms-2">Quản lý thành viên</h3>

					<form action="/manage-user" method="post">
						<div>

							<span class='my-auto'> <input type="checkbox"
								id='checkBoxAll' class="invisible"> <label
								for="checkBoxAll" class="checkAll">Chọn tất cả</label>
							</span>
							<button type="submit" class="btn-success btn ms-2 btn-sm"
								name="save">Lưu</button>
							<button type="submit" class="btn-danger btn ms-2 btn-sm"
								name="del">Xoá</button>
						</div>
						<div class="table-wrapper">
							<table class="table table-sm table-striped table-hover">
								<thead>
									<tr>
										<th>Chọn</th>
										<th>Tên User</th>
										<th>Họ Tên</th>
										<th>Email</th>
										<th>Số điện thoại</th>
										<th>Chức vụ</th>
									</tr>
								</thead>
								<tbody id="searchRow">
									<tr>
										<td scope="row">Tìm kiếm</td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbID' placeholder="Tài khoản" /></td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbFullName' placeholder="Họ tên" /></td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbEmail' placeholder="Email" /></td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbNumber'
											placeholder="Số điện thoại" /></td>
										<td><select class='form-control f-sm mb-1'
											onchange="findUser()" id='fbRank'>

												<option value=''>Tất cả</option>
												<c:forEach items="${ruleList }" var="r">
													<c:if test="${r.getMachucvu() < userID.getRank() }">
														<option value='${r.getMachucvu() }'>${r.getTenchucvu() }</option>
													</c:if>
												</c:forEach>

										</select></td>
									</tr>

								</tbody>
								<tbody class="table-group-divider" id="tableBody">
									<c:forEach items="${userList }" var="i" varStatus="id">
										<tr>
											<td style="text-align:center; vertical-align:middle;"><input type='checkbox' name='checkbox'
												value='${id.index }' /></td>
											<td><input class='form-control f-sm' type='text'
												name='taikhoan' value='${i.getTaikhoan() }' readonly /></td>
											<td><input class='form-control f-sm' type='text'
												name='hoten' value='${i.getHoten() }' /></td>
											<td><input class='form-control f-sm' type="email"
												name='email' value='${i.getEmail() }' /></td>
											<td><input type='text' class='form-control f-sm mb-1'
												name='sdt' value='${i.getSodienthoai() }' /></td>

											<td><select class='form-control f-sm mb-1' name='chucvu'>
													<c:forEach items="${ruleList }" var="r">
														<c:if test="${r.getMachucvu() < userID.getRank() }">
															<option value='${r.getMachucvu() }'
																<c:if test="${r.getMachucvu() == i.getRank() }">selected</c:if>>${r.getTenchucvu() }</option>
														</c:if>
													</c:forEach>

											</select></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

						</div>

					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>