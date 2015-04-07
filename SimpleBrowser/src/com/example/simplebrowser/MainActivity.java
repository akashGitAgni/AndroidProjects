package com.example.simplebrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity implements View.OnClickListener {

	private Button btnGo;
	EditText mUrl;
	private WebView webView;

	private Button btnBack;
	private Button btnForward;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	String bookmarks[];
	private Button btnClr;
	private Button btnZin;
	private Button btnZout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		sharedPreferences = getSharedPreferences("bookMarks", MODE_PRIVATE);
		editor = sharedPreferences.edit();

		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setDisplayZoomControls(true);

		btnGo = (Button) findViewById(R.id.buttonGo);
		mUrl = (EditText) findViewById(R.id.url_address);
		btnGo.setOnClickListener(this);
		mUrl.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.buttonBack);
		btnBack.setOnClickListener(this);

		btnForward = (Button) findViewById(R.id.buttonForward);
		btnForward.setOnClickListener(this);

		btnZin = (Button) findViewById(R.id.buttonZoomin);
		btnZout = (Button) findViewById(R.id.buttonZoomout);
		btnZout.setOnClickListener(this);
		btnZin.setOnClickListener(this);

		btnClr = (Button) findViewById(R.id.buttonClear);
		btnClr.setOnClickListener(this);

		mUrl.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						String url = "";
						if (mUrl.getText().toString().startsWith("http://")) {
							url = mUrl.getText().toString();
						} else {
							url = "http://" + mUrl.getText().toString();
						}
						webView.loadUrl(url);

						return true;
					default:
						break;
					}
				}
				return false;

			}
		});

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.url_address:
			break;
		case R.id.buttonGo:
			String url = "";
			if (mUrl.getText().toString().startsWith("http://")) {
				url = mUrl.getText().toString();
			} else {
				url = "http://" + mUrl.getText().toString();
			}
			webView.loadUrl(url);

			break;
		case R.id.buttonBack:
			webView.goBack();
			break;

		case R.id.buttonClear:
			mUrl.setText("");
			break;
		case R.id.buttonForward:
			webView.goForward();
			break;
		case R.id.buttonZoomin:
			webView.zoomIn();
			break;
		case R.id.buttonZoomout:
			webView.zoomOut();
			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}

	String[] getBookMarks() {
		int count = sharedPreferences.getInt("count", 0);
//		Log.d("xxx", "Count" + count);
		String bookmarks[] = new String[count];
		for (int i = 0; i < count; i++) {
			String url = sharedPreferences.getString(Integer.toString(i + 1),
					"");
//			Log.d("xxx", "Count" + count + "Url-" + url);
			bookmarks[i] = url;
		}
		return bookmarks;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_bookmark:
			bookmarks = getBookMarks();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("BookMarks").setItems(bookmarks,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String url = bookmarks[which];
							webView.loadUrl(url);
							dialog.cancel();
						}
					});
			builder.create();
			builder.show();
			return true;

		case R.id.menu_save:
			int count = sharedPreferences.getInt("count", 0);
//			Log.d("xxx", "Count" + count);
			editor.putString(Integer.toString(count + 1), webView.getUrl()
					.toString());
			editor.putInt("count", (count + 1));
			editor.commit();
			return true;
		case R.id.menu_exit:
			finish();
			return true;

		case R.id.menu_home:
			String url = "http://www.google.com";
			webView.loadUrl(url);
			return true;

		default:
			return true;
		}
	}

	private class MyWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			Log.d("xxx", "URL" + url);
			mUrl.setText(url);
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView webview, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(webview, errorCode, description, failingUrl);

			AlertDialog.Builder abox = new AlertDialog.Builder(
					MainActivity.this);
			abox.setMessage(description);
			abox.create();

			CharSequence okBtn = "OK";
			abox.setPositiveButton(okBtn,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							if (webView.canGoBack()) {
								webView.goBack();
							}
							arg0.dismiss();
						}
					});
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mUrl.setText(url);
		}
	}

}
