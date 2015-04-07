package com.example.onlinecourseregistration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StudentMainActivity extends Activity {

	String mStudentID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_main);
		mStudentID = getIntent().getExtras().getString("studentId");
		// save button
		Button btn1 = (Button) findViewById(R.id.button1);

		// save button click event
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(),
						StudentRecord.class);
				i.putExtra("studentID", mStudentID);
				startActivity(i);
			}
		});

		Button btn2 = (Button) findViewById(R.id.button2);

		// save button click event
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(), CourseList.class);
				i.putExtra("studentID", mStudentID);
				startActivity(i);
			}
		});

		Button btn3 = (Button) findViewById(R.id.button3);

		// save button click event
		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(),
						SearchCourses.class);
				i.putExtra("studentID", mStudentID);
				startActivity(i);
			}
		});
		Button btn4 = (Button) findViewById(R.id.button4);

		// save button click event
		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(), MyCourses.class);
				i.putExtra("studentID", mStudentID);
				startActivity(i);
			}
		});

		Button btn5 = (Button) findViewById(R.id.button5);

		// save button click event
		btn5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(),
						StudentSchedule.class);
				i.putExtra("studentID", mStudentID);
				startActivity(i);
			}
		});

	}
}
