
function like(id, isPost, toUser) {
	var tagID = isPost ? "p" + id : "c" + id;
	var tagLike = isPost ? "pl" + id : "cl" + id;
	$.ajax({
		type: 'POST',
		url: '/sendLike',
		data: {
			"id": id,
			"isPost": isPost,
			"to": toUser
		},
		success: function(respone) {
			var data = respone;
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
	});

}
function loadMessage(uid) {

	var start = $(".list-message > li").length;
	$.ajax({
		type: "POST",
		url: "/loadMessage",
		data: {
			uid: uid,
			start: start,
			limit: 10
		},
		success: function(response) {
			$.each(response.payLoad, function(index, item) {
				let msg = addMessage(item.avatar, item.message, item.time, item.sender);
				if (uid == "chatbot" && item.sender == "other-chat") {
					msg = addBotMessage(item.avatar, item.message, item.time, item.sender)
				}
				$(".list-message").prepend(msg);
			})
			let len = $(".chat").length;
			let top;
			Prism.highlightAll();
			if (len > 0) {
				if (start == 0) {
					top = document.documentElement.scrollHeight;
				} else {
					top = $(".chat")[len - start].offsetTop - 78;
				}
				window.scrollTo({ top: top, behavior: 'smooth' });
			}

		}
	});

}

function loadComment(id) {

	var start = $("#listComments > div").length;
	$.ajax({
		type: "GET",
		url: "/loadComment",
		data: {
			id: id,
			start: start,
			limit: 10
		},
		success: function(response) {
			if (response.length >= 10) {
				$("#loadMoreCmt").show();
			}else{
				$("#loadMoreCmt").hide();
			}
			$.each(response, function(index, item) {
				$("#listComments").append(item);
			})
		}
	});
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
	});
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
	});
}
function deleteNotification(id) {
	$.ajax({
		type: "POST",
		url: "/deleteNofity",
		data: { id: id },
		success: function(response) {
			if (response == "success") {
				$("#ntf-"+id).text("Đã xóa thông báo.");
			}
		}
	});
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
			$("#tableBody").html(response.result);
		}
	});
}

function stopBotSession() {
	$.ajax({
		type: "POST",
		url: "/stopBotSession",
		data: {
			stopBot: "true"
		},
		success: function(response) {
		}
	});
}
function loadPost(uid) {
	var start = $("#listPosts > div").length;
	$.ajax({
		type: "GET",
		url: "/loadPost",
		data: {
			start: start,
			limit: 10,
			uid: uid
		},
		success: function(response) {
			if (response.length >= 10) {
				$("#loadMore").show();
			}else{
				$("#loadMore").hide();
			}
			$.each(response, function(index, item) {
				$("#listPosts").append(item);
			})
			removeControl();
		}
	});
}


function loadPostWithSort(gid, reset) {
	let sortOption = $('input[name=sortting]:checked', '#sortting').val();
	let isAscending = $("#ascendingCbx").is(":checked");
	
	if(reset == true){
		$("#listPosts").html("");
	}
	var start = $("#listPosts > div").length;
	$.ajax({
		type: "GET",
		url: "/loadPostSortting",
		data: {
			gid: gid,
			sortOption: sortOption,
			isAscending: isAscending,
			start: start,
			limit: 10,
			reset: reset
		},
		success: function(response) {
			$.each(response, function(index, item) {
				$("#listPosts").append(item);
			})
			removeControl();
			if (response.length >= 10) {
				$("#loadMore").show();
			}else{
				$("#loadMore").hide();
			}
		}
	});
}

function editGroup(e){
	e.classList.replace("btn-edit","btn-save");
	e.setAttribute("onclick","saveGroup(this);");
	$("#group-description-edit").val($("#group-description").text());
	$("#group-description").hide();
	$("#group-description-edit").show();
	
}

function saveGroup(e){
	e.classList.replace("btn-save","btn-edit");
	e.setAttribute("onclick","editGroup(this);");
	$("#group-description").show();
	$("#group-description-edit").hide();
	$.ajax({
		type: "GET",
		url: "/editGroup",
		data: {
			id: e.value,
			mota: $("#group-description-edit").val()
		},
		success: function(response) {
			$("#group-description").text(response.result);
		}
	});
}

function pinPost(e, id) {
	$.ajax({
		type: "GET",
		url: "/pinPost",
		data: {
			id: id
		},
		success: function(response) {
			console.log(response);
			if(response == "pinned"){
				e.classList.replace("btn-pin","btn-unpin");
			}
			else if(response == "unpinned"){
				e.classList.replace("btn-unpin","btn-pin");
			}
		}
	});
}

function searchToChat(input) {
	if(input == "") {
		$("#schat-result").html("");
		return;
	}
	$.ajax({
		type: "GET",
		url: "/searchToChat",
		data: {
			input: input
		},
		success: function(response) {
			$("#schat-result").html("");
			let result = "";
			$.each(response.result, function(index, item) {
				result += "<a class='dropdown-item d-flex' href='/chat/" + item.id + "'>";
				result += "<img class='rounded-circle' height='40' width='40' src='" + item.avatar + "'>";
				result += "<span class='ms-1'>";
				result += "<span class='fw-bold' style='font-size: 0.9em;'>" + item.hoten + "</span>";
				result += "<small class='d-block'>" + item.lastlogin + "</small></span></a>";
				
			});
			$("#schat-result").html(result);
		}
	});
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
			$("#tableBody").html(response.result);
		}
	});
}


function getWeather() {
	let val = JSON.parse($("#cbxCity").val());
	console.log(val);
	$.ajax({
		type: "GET",
		url: "/getWeather",
		data: {
			lat: 21.333,
			lon: 106.333
		},
		success: function(data) {
			$("#nameCity").text(val.name);
			$("#weatherImg").css("background-image",`url('http://openweathermap.org/img/wn/${data.current.weather[0]["icon"]}@2x.png')`);
			$("#weatherTemp").text(data.current.temp.toFixed() + "°C");
			$("#weatherRange").text(data.daily[0]["temp"]["min"].toFixed() + "° - "+data.daily[0]["temp"]["max"].toFixed()+"°");
			console.log(response);
		}
	});
}

$(document).ready(function() {
	getWeather();
	
	$("#searchToChat").on("input keyup", function(e) {
		searchToChat($(this).val());
	});

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
					const toast = new bootstrap.Toast($('#liveToast'));
					toast.show();
				}
			}
		});
	});



	$("#confirm-yes").click(function() {
		delPost($(this)[0].value);
	});

	$("#confirm-yes-1").click(function() {
		delComment($(this)[0].value);
	});
});

