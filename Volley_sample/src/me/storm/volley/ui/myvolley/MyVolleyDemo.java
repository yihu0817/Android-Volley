package me.storm.volley.ui.myvolley;

import java.util.HashMap;
import java.util.Map;

import me.storm.volley.R;
import me.storm.volley.data.RequestManager;
import me.storm.volley.ui.BaseActivity;
import me.storm.volley.ui.list.UrlBean;
import me.storm.volley.ui.model.User;
import me.storm.volley.vendor.VolleyApi;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
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

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.MyRequest;
import com.android.volley.toolbox.NetworkImageView;

public class MyVolleyDemo extends BaseActivity {

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
		mNetWorkImg.setImageUrl(VolleyApi.IMAGE_URL, imageLoader);// 请求图片
	}

	private void onRequestByImageLoader() {
		ImageLoader loader = RequestManager.getImageLoader();

		ImageListener listener = ImageLoader.getImageListener(mShow2Img,
				R.drawable.ic_launcher, android.R.drawable.ic_input_delete);

		loader.get(UrlBean.urls[7], listener, 0, 0);// 获取图片
	}

	// =============================
	private void onImageRequest() {
		executeRequest(new ImageRequest(VolleyApi.PIC_URL,
				onImageBitmapListener(), 0, 0, Config.ARGB_8888,
				errorListener()));
	}

	private Response.Listener onImageBitmapListener() {
		return new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				mShowImg.setImageBitmap(response);
			}
		};
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

		String url = VolleyApi.MY_JSONDEMO_ACTION + "?json="
				+ jsonObject.toString();

		executeRequest(new JsonObjectRequest(Method.GET, url, null,
				onJSONObjectGetListener(), errorListener()));

	}

	private Response.Listener onJSONObjectGetListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					mJson1Txt.setText("name :" + response.getString("name")
							+ " password :" + response.getString("password"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}

	private void onPostJosnRequest() {
		Map<String, String> parames = new HashMap<String, String>();
		parames.put("name", "xiaoming");
		parames.put("password", "123");

		executeRequest(new MyJsonPostRequest(Method.POST,
				VolleyApi.MY_JSONDEMO_ACTION, onJSONObjectPostListener(),
				errorListener(), parames));
	}

	private Response.Listener onJSONObjectPostListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					mJson1Txt.setText("name :" + response.getString("name")
							+ " password :" + response.getString("password"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}

	private void onStringRequest() {

		executeRequest(new MyStringRequest(Method.GET,
				VolleyApi.MY_STRING_ACTION, onStringListener(), errorListener()));
	}

	private Response.Listener onStringListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				mMessageTxt.setText("你好 ：" + response);
			}
		};
	}

	private void onMyJsonRequest() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("name", "李四1");
		maps.put("password", "xinhua");

		mProgressDialog.show();
		MyRequest request = new MyRequest<User>(Method.POST,
				VolleyApi.MY_JSON_ACTION, onUserListener(), errorListener(),
				User.class, maps);
		request.setShouldCache(true);
		executeRequest(request);
	}

	private Response.Listener onUserListener() {
		return new Response.Listener<User>() {
			@Override
			public void onResponse(User response) {
				mMyTxt.setText("name :" + response.getUserName()
						+ " password :" + response.getPassWord() + " id :"
						+ response.getId());
				mProgressDialog.cancel();
			}
		};
	}
}
