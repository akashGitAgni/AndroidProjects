package com.example.onlinecourseregistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchCourses extends Activity {

	List<DepartmentModel> deptData;
	List<String> list = new ArrayList<String>();
	private ProgressDialog pDialog;
	String studentId;
	EditText idText;
	String deptIDSearch = null;
	JSONParser jsonParser = new JSONParser();
	private Spinner deptSpinner;
	String cIdSearch = null;
	private static final String url_dept_list = "http://ix.cs.uoregon.edu/~akasha/dbproj/deptlist.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.course_search);
		studentId = getIntent().getExtras().getString("studentID");
		deptData = new ArrayList<DepartmentModel>();
		list.add("No Selection");
		deptSpinner = (Spinner) findViewById(R.id.deptspinner);
		deptSpinner.getSelectedItemPosition();
		new GetDepartmentList().execute();
		idText = (EditText) findViewById(R.id.courseid);
		Button regButton = (Button) findViewById(R.id.button1);
		regButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = deptSpinner.getSelectedItemPosition();
				DepartmentModel dp;
				if (position > 0) {
					dp = deptData.get(position - 1);
					deptIDSearch = dp.deptID;
				}

				if (idText.getText().toString().length() > 0)
					cIdSearch = idText.getText().toString();
				else
					cIdSearch = "";
				Intent i = new Intent(getApplicationContext(), CourseList.class);
				i.putExtra("studentID", studentId);
				i.putExtra("deptID", deptIDSearch);
				Log.d("SearchCourses", "deptIDSearch----" + deptIDSearch);
				i.putExtra("cIdSearch", cIdSearch);
				startActivity(i);

			}
		});
		Button newSearchButton = (Button) findViewById(R.id.button2);

	}

	/**
	 * Background Async Task to Get complete Courses details
	 * */
	class GetDepartmentList extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchCourses.this);
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

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("studentId",
								studentId));

						JSONObject json = jsonParser.makeHttpRequest(
								url_dept_list, "POST", params);

						Log.d("Single Courses Details", json.toString());

						success = json.getInt("success");
						if (success == 1) {
							// successfully received Courses details
							JSONArray Coursess = json
									.getJSONArray("department");
							// looping through All Coursess
							for (int i = 0; i < Coursess.length(); i++) {
								JSONObject c = Coursess.getJSONObject(i);

								DepartmentModel cm = new DepartmentModel();
								cm.deptID = c.getString("deptId");
								cm.deptName = c.getString("deptName");
								list.add(cm.deptName);
								deptData.add(cm);

							}

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
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					SearchCourses.this, android.R.layout.simple_spinner_item,
					list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			deptSpinner.setAdapter(dataAdapter);

		}
	}

}
