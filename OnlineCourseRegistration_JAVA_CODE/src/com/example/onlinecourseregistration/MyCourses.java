package com.example.onlinecourseregistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.onlinecourseregistration.CourseList.RegisterForCourses;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyCourses extends ListActivity {

	ArrayList<CourseModel> coursedata;
	private ProgressDialog pDialog;
	String studentId;
	EditText idText;
	Spinner termSelect;
	String selectedTerm;
	JSONParser jsonParser = new JSONParser();

	private static final String url_course_search = "http://ix.cs.uoregon.edu/~akasha/dbproj/mycourses.php";
	private static final String url_deregister_course = "http://ix.cs.uoregon.edu/~akasha/dbproj/coursederegister.php";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mycourses);
		coursedata = new ArrayList<CourseModel>();
		studentId = getIntent().getExtras().getString("studentID");
		// new GetMyCourses().execute();

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.term, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		termSelect = (Spinner) findViewById(R.id.termspin);
		termSelect.setAdapter(adapter);
		termSelect.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				// your code here
				coursedata.clear();
				CustomAdapter adapter = new CustomAdapter(
						getApplicationContext(), coursedata);
				setListAdapter(adapter);
				selectedTerm = (String) termSelect.getSelectedItem();
				new GetMyCourses().execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
		Button regButton = (Button) findViewById(R.id.button1);
		regButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new RegisterForCourses().execute();
			}
		});	

	}
	
	class RegisterForCourses extends AsyncTask<String, String, String> {

		int success;
		

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MyCourses.this);
			pDialog.setMessage("Registering for Courses..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating Courses
		 * */
		protected String doInBackground(String... args) {

			for (CourseModel cm : coursedata) {
				if (cm.selected) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("courseId", cm.courseId));
					params.add(new BasicNameValuePair("term", cm.term));
					params.add(new BasicNameValuePair("studentId", studentId));

					JSONObject json = jsonParser.makeHttpRequest(
							url_deregister_course, "POST", params);

					// check log cat fro response
					Log.d("Create Response", json.toString());
					try {
						success = json.getInt("success");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// check for success tag
				}
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done

			pDialog.dismiss();

			if (success == 1) {

				Toast t = (Toast.makeText(MyCourses.this,
						"Courses are Unregistered", Toast.LENGTH_LONG));
				t.show();
				coursedata.clear();
				new GetMyCourses().execute();

			} else {
				Toast t1 = (Toast.makeText(MyCourses.this,
						"Error In course Registration", Toast.LENGTH_LONG));
				t1.show();
			}

		}

	}

	class GetMyCourses extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MyCourses.this);
			pDialog.setMessage("Loading Courses details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					int success;
					try {
						List<NameValuePair> params;
						JSONObject json;

						params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("studentId",
								studentId));
						json = jsonParser.makeHttpRequest(url_course_search,
								"POST", params);

						Log.d("Single Courses Details", json.toString());

						success = json.getInt("success");
						if (success == 1) {
							JSONArray Coursess = json.getJSONArray("courses");
							// looping through All Coursess
							for (int i = 0; i < Coursess.length(); i++) {
								JSONObject c = Coursess.getJSONObject(i);

								CourseModel cm = new CourseModel();
								cm.courseId = c.getString("crnno");
								cm.term = c.getString("term");
								cm.details = c.getString("courseName")
										+ " term:" + c.getString("term")
										+ " deptId:" + c.getString("deptId")
										+ " credits:" + c.getString("credits")
										+ " textbook:"
										+ c.getString("textbook") + " grade:"
										+ c.getString("grade");

								Log.d("Mycourses", "selectedTerm-----------"
										+ selectedTerm + "term" + cm.term);
								if (selectedTerm == null)
									coursedata.add(cm);
								else if (cm.term.equals(selectedTerm)) {
									coursedata.add(cm);
								}

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
			CustomAdapter adapter = new CustomAdapter(getApplicationContext(),
					coursedata);
			setListAdapter(adapter);
		}
	}

	// define your custom adapter
	private class CustomAdapter extends BaseAdapter {
		// boolean array for storing
		// the state of each CheckBox
		boolean[] checkBoxState;
		ViewHolder viewHolder;
		Context context;
		private LayoutInflater inflater;

		public CustomAdapter(Context context, ArrayList<CourseModel> CourseList) {

			this.context = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// class for caching the views in a row
		private class ViewHolder {
			TextView courseId;
			TextView details;
			CheckBox checkBox;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.course_list_view, null);
				viewHolder = new ViewHolder();
				// cache the views
				viewHolder.courseId = (TextView) convertView
						.findViewById(R.id.courseid);
				viewHolder.details = (TextView) convertView
						.findViewById(R.id.details);
				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);

				// link the cached views to the convertview
				convertView.setTag(viewHolder);

			} else
				viewHolder = (ViewHolder) convertView.getTag();

			viewHolder.courseId.setText(coursedata.get(position).courseId);
			viewHolder.details.setText(coursedata.get(position).details);
			viewHolder.checkBox.setChecked(coursedata.get(position).selected);

			viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					if (((CheckBox) v).isChecked())
						coursedata.get(position).selected = true;
					else
						coursedata.get(position).selected = false;

				}
			});

			// return the view to be displayed
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return coursedata.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
