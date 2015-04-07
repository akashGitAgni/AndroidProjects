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

public class InstructorMainActivity extends Activity {

	String instId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructor_main);
		instId = getIntent().getExtras().getString("instId");
		// save button
		Button btn1 = (Button) findViewById(R.id.button1);

		// save button click event
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(),
						InstructorRecord.class);
				i.putExtra("instId", instId);
				startActivity(i);
			}
		});

		Button btn2 = (Button) findViewById(R.id.button2);

		// save button click event
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(), InstrructorSchedule.class);
				i.putExtra("instId", instId);
				startActivity(i);
			}
		});

	}
}
