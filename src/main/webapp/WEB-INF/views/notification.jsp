<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>
<body class="line-numbers">
	<div class="body">
		<%@ include file="/WEB-INF/views/includes/topbar.jsp"%>
		<div id="show-code"></div>
	</div>
	<textarea class="fixed" rows=1 cols=20>initial content</textarea>



	<script>
		const textarea = document.querySelector("textarea");
		textarea.addEventListener("input", autoResize);
		const lineHeight = parseInt(window.getComputedStyle(textarea)
				.getPropertyValue('line-height'));
		const padding = parseInt(window.getComputedStyle(textarea)
				.getPropertyValue('padding-top'));

		function autoResize() {
			const currentRows = Math.ceil(this.scrollHeight / lineHeight);
			console.log(currentRows);
			if (currentRows >= 7) {
				this.style.overflowY = 'scroll';
				this.style.resize = 'none';
			} else {
				this.style.overflowY = 'auto';
				this.style.resize = 'both';
				this.style.height = 'auto';
			this.style.height = this.scrollHeight + 'px';
			}
		} 
	</script>
</body>
</html>