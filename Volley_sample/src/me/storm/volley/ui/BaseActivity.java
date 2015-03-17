/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package me.storm.volley.ui;

import me.storm.volley.data.RequestManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyErrorHelper;

public class BaseActivity extends Activity {
	protected Activity activity;
	protected ProgressDialog mProgressDialog ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		mProgressDialog = new ProgressDialog(activity);
	}

	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(this);
	}

	protected void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity, VolleyErrorHelper.getMessage(error, activity), Toast.LENGTH_LONG).show();
				mProgressDialog.cancel();
			}
		};
	}
}
