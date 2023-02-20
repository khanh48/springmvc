
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

$(document).ready(function() {
	$("#changePassword").click(function(e) {
		$.ajax({
			type: 'POST',
			url: './includes/changePass.php',
			data: {
				user: sessionStorage.getItem("uid"),
				oldPass: $('#oldPass').val(),
				newPass: $('#newPass').val(),
				reNewPass: $('#reNewPass').val()
			},
			success: function(respone) {
				var data = JSON.parse(respone);
				if (data.type === 'failed') {
					$("#failToChangePass").text(data.message);
				} else {
					$("#failToChangePass").text("");

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
				$.each(response, function(index, item) {
					$("#listPosts").append(item)
				})
			}
		})
	})

	$("#confirm-yes").click(function() {
		console.log($(this)[0].value)
		$.ajax({
			type: "POST",
			url: "/delete-post",
			data: { id: $(this)[0].value },
			success: function(response) {
				$.each(response, function(index, item) {
					$("#listPosts").append(item)
				})
			}
		})
	})
})

