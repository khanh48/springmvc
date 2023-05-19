package me.forum.Service;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Weather {
	private final static String origin = "http://api.openweathermap.org";
	private final static String apiKey = "2637a837694dd68e21a996ee176b3092";

	public static String GetWeather(float lat, float lon) {
		String path;
		path = "/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&exclude=minutely,hourly&units=metric&lang=vi&appid=";

		return GetApi(origin + path + apiKey);
	}

	public static String GetGeo(float lat, float lon) {
		String path = "/geo/1.0/reverse?lat=" + lat + "&lon=" + lon + "&limit=5&appid=";
		return GetApi(origin + path + apiKey);
	}

	public static String GetGeo(String city) {
		String path = "/geo/1.0/direct?q=" + city + "&limit=5&appid=";
		return GetApi(origin + path + apiKey);
	}

	public static String GetApi(String path) {
		String result = null;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(path).build();
		try (Response response = client.newCall(request).execute()) {
			result = response.body().string();
			response.close();
		} catch (IOException e) {

		}
		return result;
	}
}
