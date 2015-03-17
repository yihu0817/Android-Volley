package me.storm.volley.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpImageLoader {
	private static HttpImageLoader HTTPIMAGELoader = null;
	
	public static HttpImageLoader getInstance(){
		if(HTTPIMAGELoader == null){
			HTTPIMAGELoader = new HttpImageLoader();
		}
		return HTTPIMAGELoader;
	}
	
	public Bitmap getBitMapByHttp(String imageUrl) {
		URL url;
		try {
			url = new URL(imageUrl);
			InputStream is;
			is = url.openStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
