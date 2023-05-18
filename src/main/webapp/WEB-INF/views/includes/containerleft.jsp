<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="left">
	<div class="info"">
		<div class="d-flex" style="height: 50px;">
		<c:if test="${not empty userID }">
			<a href="/ho-so">
				<img class="avt" alt="" src="${userID.getAnhdaidien() }">
			</a>
			<span class="ms-2">
				<a class="text-black" href="/ho-so"><span class="d-block">${userID.getHoten() }</span></a>
				<small>ID: ${userID.getTaikhoan() }</small>
			</span>
		</c:if>
		</div>
		<div class="weatherWidget" style="position: relative;">
			<div class="d-flex justify-content-center mt-2">
				<span id="nameCity"></span>
				<div id="citySelect" style="display: none;width: 60%;">
				<select id='cbxCity'>
				</select>
				</div>
			</div>
			
			<div class="d-flex justify-content-center"><span style="height: 70px;width: 70px;" id="weatherImg"></span></div>
			<div class="text-center fs-2" id="weatherTemp"></div>
			<div class="text-center" id="weatherRange"></div>
			<div class="d-flex justify-content-end" ></div>
			<span style="position:absolute;right:7px;top:2px;" >
				<button class="btn-setting btn-hover" onclick="changeCity(this, true)"></button>
			</span>
			<span style="position:absolute;right:7px;top:147px;" >
				<small id="lastUpdate" style="vertical-align: top;"></small>
				<button class="btn-reload btn-hover p-0"></button>
			</span>
		</div>
	</div>

	<c:forEach items="${bestgroups }" var="group">
		<div class="group">
			<c:forEach items="${group }" var="i" varStatus="idx">
				<c:if test="${idx.first }">
					<a href="/nhom/${i.getManhom() }">
					<div class="name group-name">${i.getNhom()}</div>
					</a>
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