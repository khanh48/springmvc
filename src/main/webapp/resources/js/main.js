document.addEventListener("DOMContentLoaded", function() {
	// var header = document.querySelector('.header');
	// var l = document.querySelectorAll('.effect');
	// var menu = document.querySelector('.menu-toggle');
	// var main = document.querySelector('.main');
	// window.onscroll = function () {
	//     if (window.pageYOffset > 1) {  
	//         header.classList.add("sticky");
	//         l.forEach(function (li) {
	//             li.classList.remove('ef');
	//         });
	//         document.querySelector('.img').setAttribute('src', './lib/images/logo.png');
	//     }
	//     else {
	//         header.classList.remove("sticky");
	//         l.forEach(function (li) {
	//             li.classList.add('ef');
	//         });
	//         document.querySelector('.img').setAttribute('src', './lib/images/cdlncd.png');
	//     }
	// }
	// if (menu != null)
	//     menu.onclick = function () {
	//         menu.classList.toggle("change");
	//         document.querySelector('#full-menu').classList.toggle("show");
	//     };
	const textarea = document.querySelector("textarea");
	if (textarea != null) {

		textarea.addEventListener("input", autoResize);
		var lineHeight = 24;
	}

	function autoResize() {
		const currentRows = Math.ceil(this.scrollHeight / lineHeight) - 1;
		if (currentRows < 6) {
			this.style.height = 'auto';
			this.style.height = this.scrollHeight + 'px';
		}
	}
	
})

const path = location.pathname.split("/");

function isPC() {
	if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
		return false;
	}
	return true;
}

function removeControl() {
	var inner = $(".carousel-inner");
	if (inner.length > 0) {
		$.each(inner, function(index, item) {
			if (item.querySelectorAll(".carousel-item").length < 2) {
				$(".carousel-control-prev")[index].hidden = true;
				$(".carousel-control-next")[index].hidden = true;
			}
		});
	}
}

function loadReadMore() {
	var ct = document.querySelectorAll('.rm');
	var body = document.querySelectorAll('.c-body');
	var readmore = document.querySelectorAll('.read-more');
	var hide = document.querySelectorAll('.wh');

	for (let i = 0; i < body.length && i < readmore.length && i < ct.length; i++) {
		if (body[i].clientHeight > 250) {
			body[i].setAttribute('style', 'height: 250px;');
			readmore[i].innerHTML = 'Đọc thêm';
			hide[i].classList.remove("hide");
		}
		readmore[i].onclick = function() {
			if (body[i].clientHeight > 250) {
				window.scrollTo(0, ct[i].offsetTop - 70);
				body[i].setAttribute('style', 'height: 250px;');
				readmore[i].innerHTML = 'Đọc thêm';
			}
			else {
				body[i].removeAttribute('style');
				readmore[i].innerHTML = 'Thu gọn';
			}
		}
	}
}

function sendMessage() {
	let chatInput = $(".chat-input");
	if (chatInput.val().length == 0) {
		return;
	}
	let type = (path[1] === "chat" && path[2] === "chatbot") ? "requestChat" : "chat";
	$.ajax({
		type: "POST",
		url: "/chatHandler",
		data: {
			type: type,
			message: chatInput.val(),
			user: path[2]
		},
		success: function(response) {

		}
	});
	const d = new Date();
	$(".list-message").append(addMessage("", chatInput.val(), getTime(), "my-chat"));
	chatInput.val("");
	chatInput.css("height", "26.4px");
}

function login() {
	this.event.preventDefault();
	let spinner = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>Loggin...`;
	$("#btn-login").prop("disabled", true);
	$("#btn-login").html(spinner);
	$.ajax({
		type: 'POST',
		url: '/login',
		data: {
			"userName": $("#user-name-log").val(),
			"password": $("#pwd-log").val()
		},
		success: function(respone) {
			var result = respone;
			$("#btn-login").prop("disabled", false);
			$("#btn-login").html("Đăng nhập");
			if (result.type === "failed") {
				$('#err1-log').text(result.message);
			}
			else if (result.type === "success") {
				$('#err1-log').text("");
				setCookie("phuot.token", result.token);
				location.reload();
			}
		}
	});
	return false;
}

function getWeather(force) {
	let val = JSON.parse(localStorage.getItem("phuot.city"));
	if(sessionStorage.getItem("phuot.weather") == null || force){
		$.ajax({
			type: "GET",
			url: "/getWeather",
			data: {
				lat: val.lat,
				lon: val.lon
			},
			success: function(data) {
				let d = new Date();
				sessionStorage.setItem("phuot.weather", JSON.stringify(data));	
				sessionStorage.setItem("phuot.weather.time", getTime());	
				setWeatherDetail(val.name, data);
			}
		});
	}else{
		let data = JSON.parse(sessionStorage.getItem("phuot.weather"));
		setWeatherDetail(val.name, data);
	}
}

function setWeatherDetail(name, data){
	$("#nameCity").text(name);
	$("#weatherImg").css("background-image",`url('http://openweathermap.org/img/wn/${data.current.weather[0]["icon"]}@2x.png')`);
	$("#weatherTemp").text(data.current.temp.toFixed() + "°C");
	$("#weatherRange").text(data.daily[0]["temp"]["min"].toFixed() + "° - "+data.daily[0]["temp"]["max"].toFixed()+"°");
	$("#lastUpdate").text(sessionStorage.getItem("phuot.weather.time"));
}

function getCities(){
	$.getJSON("/resources/json/province.json",function (data) {
		let cbxCity, selected, defaultCity;
		cbxCity = "";
		defaultCity = JSON.parse(localStorage.getItem("phuot.city"));
   		$.each(data, function (index, item) {
			selected = defaultCity.name == item.name?"selected":"";
        	cbxCity += `<option value='{"name":"${item.name}","lat":${item.lat},"lon":${item.lon}}'${selected}>${item.name}</option>`;
    	});
    	$("#cbxCity").html(cbxCity);
    	$("#cbxCity").select2({
			width: "100%"
		});
		$("#cbxCity").on("change", function(e){
			localStorage.setItem("phuot.city", $(this).val());
			getWeather(true);
		});
	});
}

$(document).ready(function() {
	removeControl();
	
	if (isPC()) {
		$(".chat-input").on("keydown", function(e) {

			if (e.which == 13 && !e.shiftKey) {
				e.preventDefault();
				sendMessage();
			}
		});
	}

	if ($("#listComments > div").length < 10) {
		$("#loadMoreCmt").hide();
	}
	if ($("#listPosts > div").length < 10) {
		$("#loadMore").hide();
	}
	$("#checkBoxAll").click(function() {
		$('input[name="checkbox"]').prop('checked', $(this).is(":checked"));
	});

	$("#checkAll").click(function() {
		$('input[name="check[]"]').prop('checked', $(this).is(":checked"));
	});

	$('.notify').on("mouseenter", function() {
		$(this).removeClass('newNotify');
	});
	$('#register').submit(function(e) {
		e.preventDefault();

		$.ajax({
			type: "POST",
			url: "/register",
			data: $(this).serialize(),
			success: function(respone) {
				data = respone;
				if (data.type === "name_size") {
					$("#err1,#err2,#err3").text("");
					$("#err").text(data.message);
				} else if (data.type === "user_format") {
					$("#err,#err2,#err3").text("");
					$("#err1").text(data.message);
				} else if (data.type === "pass_format") {
					$("#err,#err1,#err2").text("");
					$("#err3").text(data.message);
				} else if (data.type === "repass_not_same") {
					$("#err,#err1,#err3").text("");
					$("#err2").text(data.message);
				} else {
					$("#err,#err1,#err2,#err3").text("");

					$('#headerToast').text("Thông báo");
					$('#toastMessage').text(data.message);
					const toast = new bootstrap.Toast($('#liveToast'));
					toast.show();
				}
			}
		})
	});


})
function deletePost(id) {
	$("#confirm-yes").val(id);
}
function deleteCmt(id) {
	$("#confirm-yes-1").val(id);
}