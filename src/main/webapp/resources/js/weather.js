document.addEventListener('DOMContentLoaded', function () {
    //Key và base của api lấy địa chỉ
    const api = {
        key: "2637a837694dd68e21a996ee176b3092",
        base: "http://api.openweathermap.org"
    }
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(acceptLocation, deniedLocation);
    } else {
        console.log("failed to get current position");
    }

    //http://api.openweathermap.org/geo/1.0/
    function acceptLocation(position) {
        fetch(`${api.base}/geo/1.0/reverse?lat=${position.coords.latitude}&lon=${position.coords.longitude}&limit=5&appid=${api.key}`)
            .then(location => {
                return location.json();
            }).then(getDefaultLocation)
    }
    function deniedLocation() {
        getResults("Hà Nội")
    }

    function getDefaultLocation(loc) {
        getData(loc)
    }
    //sự kiện ô nhập địa chỉ
    const searchbox = document.querySelector('.search-box');
    searchbox.addEventListener('keypress', setQuery);
    document.querySelector('.but').onmousedown = function () {
        getResults(searchbox.value);
    }
    function setQuery(evt) {
        if (evt.keyCode == 13) {
            getResults(searchbox.value);
        }
    }


    //lấy thông tin địa chỉ từ api thành json
    function getResults(query) {
        fetch(`${api.base}/geo/1.0/direct?q=${query}&limit=5&appid=${api.key}`)
            .then(location => {
                return location.json();
            }).then(getData);
    }

    function titleCase(str) {
        var splitStr = str.toLowerCase().split(' ');
        for (var i = 0; i < splitStr.length; i++) {
            splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
        }
        return splitStr.join(' ');
    }

    function getData(location) {
        //lấy thông tin thời tiết về json
        fetch(`${api.base}/data/3.0/onecall?lat=${location[0]["lat"]}&lon=${location[0]["lon"]}&exclude=minutely,hourly&units=metric&lang=vi&appid=${api.key}`)
            .then(weather => {
                return weather.json();
            }).then(displayResults);

        let city = document.querySelector('.location .city');
        city.innerText = `${location[0]["local_names"]["vi"]}`;

        //hiển thị thông tin nhiệt độ, ngày tháng...
        function displayResults(weather) {
            var dayOfWeek = ["Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy", "Chủ Nhật"]
            var curDate = new Date(weather.current.dt * 1000)

            let temp = document.querySelector('.current .temp');
            temp.innerHTML = `${weather.current.temp.toFixed()}<span>°C</span>`;

            let date = document.querySelector('.location .date');
            date.innerHTML = `${dayOfWeek[curDate.getDay() - 1]}, Ngày ${curDate.getDate()} Tháng 
            ${curDate.getMonth()} Năm ${curDate.getFullYear()}`;

            let weather_el = document.querySelector('.img');
            weather_el.style.backgroundImage = `url('http://openweathermap.org/img/wn/${weather.current.weather[0]["icon"]}@2x.png')`

            let hilow = document.querySelector('.hi-low');
            hilow.innerText = `${weather.daily[0]["temp"]["min"].toFixed()}°C - ${weather.daily[0]["temp"]["max"].toFixed()}°C`;
        }

    }
});
