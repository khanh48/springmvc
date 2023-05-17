package me.forum.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Weather {
	// http://api.openweathermap.org/geo/1.0/reverse?lat=16.4637&lon=107.5909&limit=5&appid=
	// data/3.0/onecall?lat=&lon=&exclude=minutely,hourly&units=metric&lang=vi&appid=
	private final static String origin = "http://api.openweathermap.org";
	private final static String apiKey = "2637a837694dd68e21a996ee176b3092";

	public static String GetWeather(float lat, float lon) {
		String path, result = "";
		path = "/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&exclude=minutely,hourly&units=metric&lang=vi&appid=";

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(origin + path + apiKey).build();
		try (Response response = client.newCall(request).execute()) {
			result = response.body().string();
			System.out.println(result);
			response.close();
		} catch (IOException e) {

		}
		return result;
	}
}
