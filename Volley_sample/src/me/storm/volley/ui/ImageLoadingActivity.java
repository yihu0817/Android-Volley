/*
 * Created by Storm Zhang, Feb 13, 2014.
 */

package me.storm.volley.ui;

import java.io.File;

import me.storm.volley.R;
import me.storm.volley.data.RequestManager;
import me.storm.volley.ui.list.VolleyListViewAct;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;

public class ImageLoadingActivity extends BaseActivity {
	private NetworkImageView mImageView;
	private ImageView mImageView1;
	private String picUrl = "http://c.hiphotos.baidu.com/image/w%3D1280%3Bcrop%3D0%2C0%2C1280%2C800/sign=2abcf809eb24b899de3c7d3a563626f6/43a7d933c895d143afcf362a71f082025aaf0779.jpg";
	private String picUrl1 = "http://a.hiphotos.baidu.com/pic/w%3D230/sign=f61a1f6efcfaaf5184e386bcbc5494ed/94cad1c8a786c917473fe571c83d70cf3bc757bd.jpg";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_loading);

		mImageView = (NetworkImageView) findViewById(R.id.iv_image);
		mImageView1 = (ImageView) findViewById(R.id.iv_image1);
		
		Button btnImageLoadingRequest = (Button) findViewById(R.id.btn_image_loading);
		btnImageLoadingRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageLoader imageLoader = RequestManager.getImageLoader();
				mImageView.setImageUrl(picUrl, imageLoader);
			}
		});
		
        //=======================================================
		ImageRequest imageRequest = new ImageRequest(picUrl1,new Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						Log.i("llb", "bitmap height" + response.getHeight()+ "&width=" + response.getWidth());
						mImageView1.setImageBitmap(response);// 显示图片
					}
				}, 200, 200, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(ImageLoadingActivity.this, "请求图片失败了", 0).show();
					}
				});

		//第一种调用方法
//		RequestQueue requestQueue = RequestManager.getRequestQueue();
//		requestQueue.add(imageRequest);
//		requestQueue.start();
		
		
		//第二种指定存储位置方法
		File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		File file = new File(sdDir, "xinhua");
		DiskBasedCache cache = new DiskBasedCache(file, 20 * 1024 * 1024);
		
		Network network = new BasicNetwork(new HurlStack());
		RequestQueue queue = new RequestQueue(cache, network);
		queue.add(imageRequest);
		queue.start();

	}
}
