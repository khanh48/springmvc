<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="toast-container position-fixed bottom-0 start-0 p-3">
	<div id="liveToast" class="toast" role="alert" aria-live="assertive"
		aria-atomic="true">
		<div class="toast-header">
			<!-- <img src="..." class="rounded me-2" alt="..."> -->
			<strong class="me-auto" id="headerToast"></strong> <small
				id="toastTime"></small>
			<button type="button" class="btn-close" data-bs-dismiss="toast"
				aria-label="Close"></button>
		</div>
		<div class="toast-body" id="toastMessage"></div>
	</div>
</div>

<footer id="ft">
	<div class="top animated"></div>
	<div class="bot">
		<div>Run For Your Life</div>

	</div>
</footer>