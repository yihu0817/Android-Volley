package me.storm.volley.ui.myvolley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import me.storm.volley.R;
import me.storm.volley.data.RequestManager;
import me.storm.volley.ui.list.UrlBean;
import me.storm.volley.ui.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyErrorHelper;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.MyRequest;
import com.android.volley.toolbox.NetworkImageView;

public class VolleyDemo1 extends Activity {
	private final static String IMAGE_URL = "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1209/07/c1/13698570_1347000164468_320x480.png";
	private final static String PIC_URL = "http://a.hiphotos.baidu.com/pic/w%3D230/sign=f61a1f6efcfaaf5184e386bcbc5494ed/94cad1c8a786c917473fe571c83d70cf3bc757bd.jpg";

	private final static String SERVER_URL = "http://192.168.133.1";

	private Button mOkBtn, mOKOneBtn, mOkTwoBtn, mOkThreeBtn, mOKFourBtn,
			mMyBtn;
	private ImageView mShowImg, mShow2Img;
	private NetworkImageView mNetWorkImg;
	private TextView mJson1Txt, mMessageTxt, mMyTxt;
	private WebView mWebView;

	private RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.volley_demo1);

		mRequestQueue = RequestManager.getRequestQueue();// Volley.newRequestQueue(this);

		mOkBtn = (Button) findViewById(R.id.btn_image_loading);
		mOKOneBtn = (Button) findViewById(R.id.btn_image1);
		mOkTwoBtn = (Button) findViewById(R.id.btn_image2);
		mOkThreeBtn = (Button) findViewById(R.id.btn_json1);
		mOKFourBtn = (Button) findViewById(R.id.btn_txt);
		mMyBtn = (Button) findViewById(R.id.btn_my);

		mNetWorkImg = (NetworkImageView) findViewById(R.id.iv_image);
		mShowImg = (ImageView) findViewById(R.id.iv_image1);
		mShow2Img = (ImageView) findViewById(R.id.iv_image2);
		mJson1Txt = (TextView) findViewById(R.id.iv_text1);
		mMessageTxt = (TextView) findViewById(R.id.message_tv);
		mMyTxt = (TextView) findViewById(R.id.tx_my);

		mWebView = (WebView) findViewById(R.id.webView1);

		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		mWebView.setWebViewClient(new WebViewClient() {
			// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		mOkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onImageRequest();
			}
		});

		mOKOneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onNetWorkImg();
			}
		});

		mOkTwoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onRequestByImageLoader();

			}
		});

		mOkThreeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onPostJosnRequest();

			}
		});

		mOKFourBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 onStringRequest();
			}
		});

		mMyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onMyJsonRequest();
			}
		});
	}

	private void onNetWorkImg() {
		ImageLoader imageLoader = RequestManager.getImageLoader();

		mNetWorkImg.setDefaultImageResId(R.drawable.ic_launcher);// 默认图片
		mNetWorkImg.setErrorImageResId(android.R.drawable.ic_input_delete);// 出错时的图片
		mNetWorkImg.setImageUrl(IMAGE_URL, imageLoader);// 请求图片
	}

	private void onRequestByImageLoader() {
		ImageLoader loader = RequestManager.getImageLoader();

		ImageListener listener = ImageLoader.getImageListener(mShow2Img,
				R.drawable.ic_launcher, android.R.drawable.ic_input_delete);

		loader.get(UrlBean.urls[7], listener, 0, 0);// 获取图片
	}

	// =============================
	private void onImageRequest() {

		ImageRequest imageRequest = new ImageRequest(PIC_URL,
				new Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						mShowImg.setImageBitmap(response);
					}
				}, 0, 0, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(VolleyDemo1.this, "请求图片失败了", 0).show();
					}
				});

		mRequestQueue.add(imageRequest);
	}

	private void onGetJosnRequest() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", "xinhua");
			jsonObject.put("password", "123456");

			Log.v("TAG", "jsonRequest.toString() :" + jsonObject.toString()); // {"name":"xinhua","password":"123456"}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = SERVER_URL + "/xinhua/JsonDemoAction?json="
				+ jsonObject.toString();

		JsonObjectRequest jsonRequest = new JsonObjectRequest(Method.GET, url,
				jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							mJson1Txt.setText("name :"
									+ response.getString("name")
									+ " password :"
									+ response.getString("password"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mJson1Txt.setText("请求出错 :" + error.getMessage());
					}

				});

		mRequestQueue.add(jsonRequest);
	}

	private void onPostJosnRequest() {

		// JSONObject jsonObject = new JSONObject();
		// try {
		// jsonObject.put("name", "xinhua");
		// jsonObject.put("password", "123456");
		//
		// Log.v("TAG", "jsonRequest.toString() :" + jsonObject.toString()); //
		// {"name":"xinhua","password":"123456"}
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

		// POST 参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("name", "xiaoming");
		maps.put("password", "123");

		String url = SERVER_URL + "/xinhua/JsonDemoAction";

		MyJsonPostRequest jsonRequest = new MyJsonPostRequest(Method.POST, url,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							mJson1Txt.setText("name :"
									+ response.getString("name")
									+ " password :"
									+ response.getString("password"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mJson1Txt.setText("请求出错 :" + error.getMessage());
					}

				}, maps);

		mRequestQueue.add(jsonRequest);
	}

	private void onStringRequest() {

		// String url = "http://www.baidu.com";
		String url = SERVER_URL + "/code.txt";
		MyStringRequest stringRequest = new MyStringRequest(Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mMessageTxt.setText("你好 ：" + response);
						// mWebView.loadData(response,
						// "text/html; charset=UTF-8", "utf-8");
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mMessageTxt.setText(error.getMessage());
					}

				}) {
			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				String str = null;
				try {
					str = new String(response.data, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return Response.success(str,
						HttpHeaderParser.parseCacheHeaders(response));
			}
		};

		mRequestQueue.add(stringRequest);

	}


	private void onMyJsonRequest() {
		String url = SERVER_URL + "/xinhua/MyJsonAction";

		// POST 参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("name", "李四");
		maps.put("password", "xinhua");
	
		
		MyRequest<User> myRequest = new MyRequest<User>(Method.POST, url,
				new Listener<User>() {

					@Override
					public void onResponse(User response) {
						mMyTxt.setText("name :"+response.getUserName()+ " password :"+response.getPassWord() + " id :"+ response.getId());
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mMyTxt.setText("错误信息 :"+VolleyErrorHelper.getMessage(error, VolleyDemo1.this));
					}
				}, (Class<User>) User.class,maps);
		
		mRequestQueue.add(myRequest);
	}

}
