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


public class MainActivity extends Activity {
	EditText stdId,instID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		if (Build.VERSION.SDK_INT >= 9) {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();

			StrictMode.setThreadPolicy(policy);
		}
		// save button
		Button btn1 = (Button) findViewById(R.id.studentButton);
		stdId = (EditText)findViewById(R.id.studentID);
		instID = (EditText)findViewById(R.id.instructorId);
		

		// save button click event
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(),
                        StudentMainActivity.class);
				i.putExtra("studentId",stdId.getText().toString());
				startActivity(i);
			}
		});
		
		Button btn2 = (Button) findViewById(R.id.instButton);
		

		// save button click event
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update Courses
				Intent i = new Intent(getApplicationContext(),
                        InstructorMainActivity.class);
				i.putExtra("instId",instID.getText().toString());
				startActivity(i);
			}
		});

	}
}

