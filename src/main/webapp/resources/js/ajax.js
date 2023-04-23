
function like(id, isPost, toUser) {
	var token = getCookie("phuot.token");
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

function findUser() {
	var taikhoan = $("#fbID").val();
	var hoten = $("#fbFullName").val();
	var email = $("#fbEmail").val();
	var sdt = $("#fbNumber").val();
	var chucvu = $("#fbRank").val();
	
	$.ajax({
		type: "POST",
		url: "/findUser",
		data: {
			taikhoan: taikhoan,
			hoten: hoten,
			email: email,
			sdt: sdt,
			chucvu: chucvu
		},
		success: function(response) {
			$("#tableBody").html(response.result)
		}
	})
}

function stopBotSession() {
	$.ajax({
		type: "POST",
		url: "/stopBotSession",
		data: {
			stopBot: "true"
		},
		success: function(response) {
			isStart = true;
		}
	})
}

function findPost() {
	var taikhoan = $("#fbAuthor").val();
	var id = $("#fbID").val();
	var tieude = $("#fbTitle").val();
	var noidung = $("#fbContent").val();
	var nhom = $("#fbGroup").val();
	
	$.ajax({
		type: "POST",
		url: "/findPost",
		data: {
			taikhoan: taikhoan,
			mabaiviet: id,
			tieude: tieude,
			noidung: noidung,
			nhom: nhom
		},
		success: function(response) {
			$("#tableBody").html(response.result)
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

