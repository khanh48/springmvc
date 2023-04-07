var soc = new WebSocket("ws://localhost:8088/websocket");
soc.onopen = function() {
	console.log("connected");
	auth(sessionStorage.getItem('token'));
}
var result = "";
soc.onmessage = function(response) {
	var data = JSON.parse(response.data);
	switch (data.type) {
		case "newNotification":
			var ntfNum = $("#ntf-num");
			var a = parseInt(ntfNum.text());
			var s = "<li>";
			s += "<a class='dropdown-item text-wrap' href='" + data.url + "'>";
			s += "<p class='small mb-0'>" + data.date + "</p>";
			s += "<p class='mb-0 unread'>" + data.message + "</p>";
			s += "</a></li>";
			if (isNaN(a)) {

				$("#bell-num").html("<span class='badge rounded-pill position-absolute top-0 start-100 translate-middle bg-danger' id='ntf-num'>1</span>");
			} else if (a < 99) {

				ntfNum.text(a + 1);
			} else {
				ntfNum.text("99+");
			}
			$("#bell-ntf").prepend(s);
		case "newResult":
			if (data.value == null) {
				result = "";
				
				break;
			} else {
				result += data.value;
			}
			console.log(response.data);
			document.querySelector("#show-code").innerHTML = marked.parse(result);
				Prism.highlightAll();


	}
}
soc.onerror = function() {
	console.log("failed");
}

soc.onclose = function() {
	console.log("closed");
}

function auth(token) {
	soc.send(JSON.stringify({ "type": "auth", "token": token }))
}
/*

websocket.onopen = function () {
	console.log("connected");
}
websocket.onmessage = function (event) {
	console.log(event);
	var Data = JSON.parse(event.data);
	if (Data.type === "request") {
		var accepted = {
			type: "accepted",
			user: sessionStorage.getItem("uid")
		}
		websocket.send(JSON.stringify(accepted));
	}
	else {
		$(".notify").prepend("<a href='" + Data.notify.url + "'><div class='notify-content unread'>" + Data.notify.msg + "</div></a>");
		$(".notify").addClass("newNotify")
		$('#headerToast').text("Thông báo");
		$('#toastMessage').html("<a href='" + Data.notify.url + "'><div class='notify-link'>" + Data.notify.msg + "</div></a>");
		const toast = new bootstrap.Toast($('#liveToast'))
		toast.show()
	}

};
websocket.onerror = function (event) {
	console.log("Problem due to some Error");
};

websocket.onclose = function (event) {
	console.log("Connection Closed");
};
function stop() {
	this.event.preventDefault();
}
function sendLike(from, to, notify) {
	var messageJSON = {
		from: from,
		to: to,
		notify: notify
	};

	websocket.send(JSON.stringify(messageJSON));
};

function sendcm(from, to, milliseconds, msg, id) {
	var messageJSON = {
		from: from,
		to: to,
		notify:
		{
			msg: msg,
			id: id,
			url: "./post.php?id=" + id + "&r=" + milliseconds
		}
	};

	websocket.send(JSON.stringify(messageJSON));
};
*/
