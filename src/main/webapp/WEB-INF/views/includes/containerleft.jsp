<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="left">
	<div class="info">
		<div class="d-flex">
			<a href="/ho-so">
				<img class="avt" alt="" src="${userID.getAnhdaidien() }">
			</a>
			<span class="ms-2">
				<a class="text-black" href="/ho-so"><span class="d-block">${userID.getHoten() }</span></a>
				<small>ID: ${userID.getTaikhoan() }</small>
			</span>
		</div>
		<div id="citySelect">
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
						<a href='/bai-viet/${i.getMabaiviet() }'>
						<div class="notify-content"><c:out value="${i.getTieude() }" escapeXml="true" /></div>
						</a>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>