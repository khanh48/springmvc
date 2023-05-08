<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="left">
	<div class="info">
		<a href="/ho-so">
			<div class="info-top">Hồ sơ cá nhân</div>
		</a>
		<div class="thongbao">
			<a href="/notification">
				<div class="tb">Thông báo</div>
			</a>
			<div class="notify">
				<a href=''>
					<div class='notify-content unread'>noidung</div>
				</a>
			</div>
		</div>
	</div>

	<c:forEach items="${bestgroups }" var="group">
		<div class="group">
			<c:forEach items="${group }" var="i" varStatus="idx">
				<c:if test="${idx.first }">
					<div class="name group-name">
						<a>${i.getNhom()}</a>
					</div>
				</c:if>
				<div class="ps-1">
					<div>
						<a href='/bai-viet/${i.getMabaiviet() }'><div
								class="notify-content"><c:out value="${i.getTieude() }" escapeXml="true" /></div></a>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>