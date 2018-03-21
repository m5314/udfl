package com.moj.udfl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * @author m5314
 *         Created by m5314 on 2017/6/02.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

	Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mButton = (Button) findViewById(R.id.btn);
		mButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn:
				startActivity(new Intent(this,SecondActivity.class));
				break;
			default:
				break;
		}
	}
}
