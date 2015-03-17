package me.storm.volley.ui.list;

import java.io.File;

import me.storm.volley.R;
import me.storm.volley.data.RequestManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

public class VolleyListViewAct extends Activity {
	private ListView mListView;
	private ListBaseAdapter mListBaseAdapter;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_main);

		mListView = (ListView) findViewById(R.id.listView1);

		imageLoader = RequestManager.getImageLoader();

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

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			String picUrl = (String) getItem(position);// http://a.hiphotos.baidu.com/p494ed/bc757bd.jpg

			if (convertView == null) {
				convertView = mLayoutInflater.inflate(
						R.layout.list_activity_item_view, null);
			}

			NetworkImageView imageView = (NetworkImageView) convertView
					.findViewById(R.id.imageView1);

			imageView.setImageResource(R.drawable.ic_launcher);

			imageView.setImageUrl(picUrl, imageLoader);

			return convertView;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
