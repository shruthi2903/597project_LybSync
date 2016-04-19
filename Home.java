package com.lyb.lyb_sync;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends ActionBarActivity implements OnClickListener{

	Button btnUser,btnAdmin,btnQuit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		btnUser=(Button)findViewById(R.id.btn_home_user);
		btnUser.setOnClickListener(this);
		
		btnAdmin=(Button)findViewById(R.id.btn_home_admin);
		btnAdmin.setOnClickListener(this);
		
		btnQuit=(Button)findViewById(R.id.btn_home_quit);
		btnQuit.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnUser){
			startActivity(new Intent(Home.this,UserLogin.class));
			finish();
		}
		if(v==btnAdmin){
			startActivity(new Intent(Home.this,AdminLogin.class));
			finish();
		}
		if(v==btnQuit){
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					Home.this);

			builder.setMessage("Are you sure do you want to quit Application?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									finish();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			final AlertDialog alert = builder.create();

			alert.show();
		}
	}
	public boolean onKeyDown(int iKeyCode, KeyEvent event) {

		if (iKeyCode == KeyEvent.KEYCODE_BACK
				|| iKeyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(iKeyCode, event);
	}


}
