<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>
<body>
	<div class="body">
		<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
		<div class="main d-block">
			<div class="content w-100 pb-3">
				<div>
					<h3 class="ms-2">Quản lý bài viết</h3>

					<form action="/manage-user" method="post">
						<div class="mb-2">

							<span class='my-auto'> <input type="checkbox"
								id='checkBoxAll' class="invisible"> <label
								for="checkBoxAll" class="checkAll">Chọn tất cả</label>
							</span>
							<button type="submit" class="btn-success btn ms-2 btn-sm"
								name="save">Lưu</button>
							<button type="submit" class="btn-danger btn ms-2 btn-sm"
								name="del">Xoá</button>
						</div>
						<div class="table-wrapper mx-2 border-secondary">
							<table
								class="mb-0 table table-success table-sm table-striped table-hover table-bordered ">
								<thead>
									<tr>
										<th>Chọn</th>
										<th>Người đăng</th>
										<th>Mã bài viết</th>
										<th>Tiêu đề</th>
										<th>Nội dung</th>
										<th>Nhóm</th>
									</tr>
								</thead>
								<tbody id="searchRow">
									<tr>
										<td scope="row">Tìm kiếm</td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbAuthor' placeholder="Người đăng" /></td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbID'
											placeholder="Mã bài viết" /></td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbTitle' placeholder="Tiêu đề" /></td>
										<td><input class='form-control f-sm' type='text'
											onkeyup="findUser()" id='fbContent' placeholder="Nội dung" /></td>
										<td><select class='form-control f-sm mb-1'
											onchange="findUser()" id='fbGroup'>

												<option value=''>Tất cả</option>
												<c:forEach items="${groupList }" var="g">
													<option value='${g.getManhom() }'>${g.getTennhom() }</option>

												</c:forEach>

										</select></td>
									</tr>

								</tbody>
								<tbody id="tableBody">
									<c:forEach items="${postList }" var="i" varStatus="id">
										<tr>
											<td style="text-align: center; vertical-align: middle;"><input
												type='checkbox' name='checkbox' value='${id.index }' /></td>
											<td><input class='form-control f-sm' type='text'
												name='taikhoan' value='${i.getUser().getTaikhoan() }' readonly /></td>
											<td><input class='form-control f-sm' type='text'
												name='mabaiviet' value='${i.getMabaiviet() }' /></td>
											<td><input class='form-control f-sm' type="email"
												name='tieude' value='${i.getTieude() }' /></td>
											<td><input type='text' class='form-control f-sm mb-1'
												name='noidung' value='${i.getNoidung() }' /></td>
											<td><select class='form-control f-sm mb-1' name='chucvu'>
													<c:forEach items="${groupList }" var="g">
															<option value='${g.getManhom() }'
																<c:if test="${g.getManhom() == i.getManhom() }">selected</c:if>>${g.getTennhom() }</option>
														
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

	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>
</html>