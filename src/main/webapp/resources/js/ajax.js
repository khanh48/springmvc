
function like(id, isPost, toUser) {
	var token = sessionStorage.getItem("token");
	var tagID = isPost ? "p" + id : "c" + id;
	var tagLike = isPost ? "pl" + id : "cl" + id;
	$.ajax({
		type: 'POST',
		url: '/sendLike',
		data: {
			"id": id,
			"isPost": isPost,
			"token": token,
			"to": toUser
		},
		success: function(respone) {
			var data = respone
			$('#' + tagID).text(data.count > 0 ? data.count : "");
			$classLiked = "fas-liked";
			$tagLikeSelected = $('#' + tagLike);
			if (data.status) {
				if (!$tagLikeSelected.hasClass($classLiked))
					$tagLikeSelected.addClass($classLiked);
			} else {
				$('#' + tagLike).removeClass($classLiked);
			}
		}
	})

}

function loadMore(id) {

	var start = $("#listComments > div").length;
	console.log("click")
	$.ajax({
		type: "GET",
		url: "/loadComment",
		data: {
			id: id,
			start: start,
			limit: 10
		},
		success: function(response) {
			if (response.length < 10) {
				$("#loadMoreCmt").hide();
			}
			$.each(response, function(index, item) {
				$("#listComments").append(item)
			})
		}
	})
}

function delPost(id) {
	$.ajax({
		type: "POST",
		url: "/deletePost",
		data: { pid: id },
		success: function(response) {
			if (response.type === "success") {
				$("#" + response.message).html("<div class='c-body'>Bài viết đã bị xóa.</div>");
				$("#cancel-delete").click();
			}
		}
	})
}
function delComment(id) {
	$.ajax({
		type: "POST",
		url: "/deleteCmt",
		data: { cid: id },
		success: function(response) {
			if (response.type === "success") {
				$("#" + response.message).html("<div class='c-body'>Bình luận đã bị xóa.</div>");
				$("#cancel-delete-1").click();
			}
		}
	})
}

$(document).ready(function() {
	$("#changePassword").click(function(e) {
		$.ajax({
			type: 'POST',
			url: '/changePass',
			data: {
				oldPass: $('#oldPass').val(),
				newPass: $('#newPass').val(),
				confirmPass: $('#confirmPass').val()
			},
			success: function(respone) {
				var data = respone;
				if (data.type === 'failed') {
					$("#failToChangePass").text(data.message);
				} else {
					$("#failToChangePass").text("");
					$('#oldPass').val("");
					$('#newPass').val("");
					$('#confirmPass').val("");
					$('#cancelChange').click();


					$('#headerToast').text("Thông báo");
					$('#toastMessage').text(data.message);
					const toast = new bootstrap.Toast($('#liveToast'))
					toast.show()
				}
			}
		})
	})

	$("#loadMore").click(function() {
		var start = $("#listPosts > div").length;
		console.log("click")
		$.ajax({
			type: "GET",
			url: "/loadPost",
			data: {
				start: start,
				limit: 10
			},
			success: function(response) {
				if (response.length < 10) {
					$("#loadMore").hide();
				}
				$.each(response, function(index, item) {
					$("#listPosts").append(item)
				})
				removeControl();
			}
		})
	})

	$("#confirm-yes").click(function() {
		delPost($(this)[0].value)
	})

	$("#confirm-yes-1").click(function() {
		delComment($(this)[0].value)
	})
})

