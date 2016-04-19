package com.lyb.lyb_sync;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AdminLogin extends ActionBarActivity implements OnClickListener{

	EditText edtUname,edtPwd;
	Button btnLogin,btnBack;
	String strUserId="",strPwd="";
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_login);
		
		edtUname=(EditText)findViewById(R.id.edt_adminlogin_userid);
		edtUname.setText("admin");
		
		edtPwd=(EditText)findViewById(R.id.edt_adminlogin_pwd);
		edtPwd.setText("admin@123");
		
		btnLogin=(Button)findViewById(R.id.btn_adminlogin_login);
		btnLogin.setOnClickListener(this);
		
		btnBack=(Button)findViewById(R.id.btn_adminlogin_back);
		btnBack.setOnClickListener(this);
		  
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		strUserId=edtUname.getText().toString().trim();
		strPwd=edtPwd.getText().toString().trim();
		
		if(v==btnLogin){
			if(strUserId.length()==0){
				Utils.showAlertDialog(AdminLogin.this, "Message", "Please enter username", false);
			}else if(!strUserId.equalsIgnoreCase("admin")){
				Utils.showAlertDialog(AdminLogin.this, "Message", "Please enter valid username", false);
			}else if(strPwd.length()==0){
				Utils.showAlertDialog(AdminLogin.this, "Message", "Please enter password", false);
			}else if(!strPwd.equalsIgnoreCase("admin@123")){
				Utils.showAlertDialog(AdminLogin.this, "Message", "Please enter valid password", false);
			} else {
				startActivity(new Intent(AdminLogin.this, AdminHome.class));
				finish();
			}
		}
		if(v==btnBack){

			startActivity(new Intent(AdminLogin.this, Home.class));
			finish();
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
