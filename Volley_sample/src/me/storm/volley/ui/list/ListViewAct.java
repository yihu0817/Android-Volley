package me.storm.volley.ui.list;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.storm.volley.R;
import me.storm.volley.util.HttpImageLoader;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ListViewAct extends Activity {
	private ListView mListView;
	private ListBaseAdapter mListBaseAdapter;
	public Map<String, SoftReference<Bitmap>> imageCacheBitmap = new HashMap<String, SoftReference<Bitmap>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_main);

		mListView = (ListView) findViewById(R.id.listView1);

		mListBaseAdapter = new ListBaseAdapter(this);
		mListView.setAdapter(mListBaseAdapter);
	}

	public class ListBaseAdapter extends BaseAdapter {
		
	
		private LayoutInflater mLayoutInflater;

		public ListBaseAdapter(Context context) {
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return UrlBean.urls.length;
		}

		@Override
		public Object getItem(int position) {
			return UrlBean.urls[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) {
					Map map = (HashMap)msg.obj;
					ImageView imageView = (ImageView)map.get("imageView");
					Bitmap bitMap = (Bitmap)map.get("bitMap");
					
					imageView.setImageBitmap(bitMap);
				}
			};
		};
		
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.list_activity_item_view, null);
			}
			final String picUrl = (String) getItem(position);//http://a.hiphotos.baidu.com/p494ed/bc757bd.jpg
			
			final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);

			imageView.setImageResource(R.drawable.ic_launcher);
			
			if (imageCacheBitmap.containsKey(picUrl)) {
				
				SoftReference<Bitmap> softRef = (SoftReference<Bitmap>)imageCacheBitmap.get(picUrl);
				
				if(null != softRef.get()){
					imageView.setImageBitmap(softRef.get());
					Log.v("TAG", "取内存缓存图片111");
				}else{
					onThreadBitmapByHttp(picUrl,imageView);
				}
			}else{
				onThreadBitmapByHttp(picUrl,imageView);
			}
			return convertView;
		}
		
		private void onThreadBitmapByHttp(final String picUrl,final ImageView imageView){
			new Thread() {
				@Override
				public void run() {
					Bitmap bitMap = null;
					try {
						URL url = new URL(picUrl);
						InputStream is = url.openStream();
						bitMap = BitmapFactory.decodeStream(is);
						Log.v("TAG", "取网络图片222");
						imageCacheBitmap.put(picUrl, new SoftReference<Bitmap>(bitMap));
						
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					Message msg = Message.obtain();
					msg.what = 1;
					
					Map map = new HashMap();
					map.put("imageView", imageView);
					map.put("bitMap", bitMap);
					msg.obj = map;
					mHandler.sendMessage(msg);
				}
			}.start();
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		imageCacheBitmap.clear();
	}
}
