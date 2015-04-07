package com.example.onlinecourseregistration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InstructorRecord extends Activity {

	TextView t;
	// Progress Dialog
	private ProgressDialog pDialog;
	String instId;
	EditText idText;
	EditText mFname, mLname, mDob, mYoe, mGender, mHomePage;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single Courses url
	private static final String url_inst_detials = "http://ix.cs.uoregon.edu/~akasha/dbproj/instructoprofile.php";
	private static final String url_inst_update = "http://ix.cs.uoregon.edu/~akasha/dbproj/instructorwrite.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructors_record);

		// save button
		Button btnSave = (Button) findViewById(R.id.button1);
		idText = (EditText) findViewById(R.id.editText1);

		instId = getIntent().getExtras().getString("instId");
		idText.setText(instId);
		mFname = (EditText) findViewById(R.id.firstName);
		mLname = (EditText) findViewById(R.id.lastName);
		mGender = (EditText) findViewById(R.id.gender);
		mDob = (EditText) findViewById(R.id.dob);
		mYoe = (EditText) findViewById(R.id.yoe);
		mHomePage = (EditText) findViewById(R.id.HomePage);

		new GetStudentRecord().execute();

		// save button click event
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				new UpdateStudentRecord().execute();
			}
		});

	}

	class UpdateStudentRecord extends AsyncTask<String, String, String> {

		int success;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InstructorRecord.this);
			pDialog.setMessage("Updating Instructior details..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating Courses
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("instId", instId));
			params.add(new BasicNameValuePair("firstName", mFname.getText()
					.toString()));
			params.add(new BasicNameValuePair("lastName", mLname.getText()
					.toString()));
			params.add(new BasicNameValuePair("gender", mGender.getText()
					.toString()));
			params.add(new BasicNameValuePair("dob", mDob.getText().toString()));
			params.add(new BasicNameValuePair("yearEnrolled", mYoe.getText()
					.toString()));
			params.add(new BasicNameValuePair("homepage", mHomePage.getText()
					.toString()));

			// getting JSON Object
			// Note that create Courses url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_inst_update,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());
			try {
				success = json.getInt("success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// check for success tag

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
			if (success == 1) {

				Toast t = (Toast.makeText(InstructorRecord.this,
						"StudentRecord are updated", Toast.LENGTH_LONG));
				t.show();

			} else {
				Toast t1 = (Toast.makeText(InstructorRecord.this,
						"Some Error In StudentRecord", Toast.LENGTH_LONG));
				t1.show();
			}
		}

	}

	/**
	 * Background Async Task to Get complete Courses details
	 * */
	class GetStudentRecord extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InstructorRecord.this);
			pDialog.setMessage("Loading Courses details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting Courses details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					int success;
					try {
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("instId", instId));

						JSONObject json = jsonParser.makeHttpRequest(
								url_inst_detials, "POST", params);

						Log.d("Single Courses Details", json.toString());

						success = json.getInt("success");
						if (success == 1) {

							JSONArray CoursesObj = json.getJSONArray("student");

							JSONObject Courses = CoursesObj.getJSONObject(0);

							mFname.setText(Courses.getString("firstName"));

							mLname.setText(Courses.getString("lastName"));

							mGender.setText(Courses.getString("gender"));

							mDob.setText(Courses.getString("dob"));

							mYoe.setText(Courses.getString("yearEnrolled"));

							mHomePage.setText(Courses.getString("homepage"));

						} else {
							// Courses with pid not found
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
		}
	}

}
