package com.lyb.lyb_sync;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UserLogin extends ActionBarActivity implements OnClickListener{

	EditText edtUserId,edtPwd;
	Button btnLogin,btnNewReg,btnBack;
	String strUid="",strPwd="";
	Database dbcls;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);
		
		dbcls=new Database(UserLogin.this);
		
		pref = UserLogin.this.getSharedPreferences("lybsync", 0);
		
		edtUserId=(EditText)findViewById(R.id.edt_userlogin_userid);
		//edtUserId.setText("12");
		edtPwd=(EditText)findViewById(R.id.edt_userlogin_pwd);
		//edtPwd.setText("vijay");  
		
		btnLogin=(Button)findViewById(R.id.btn_userlogin_login);
		btnLogin.setOnClickListener(this);
		
		btnNewReg=(Button)findViewById(R.id.btn_userlogin_newreg);
		btnNewReg.setOnClickListener(this);
		
		btnBack=(Button)findViewById(R.id.btn_userlogin_back);
		btnBack.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		strUid=edtUserId.getText().toString().trim();
		strPwd=edtPwd.getText().toString().trim();
		//Utils.md5Pwd(UserLogin.this,strPwd)
		if(v==btnLogin){
			if(strUid.length()==0){
				Utils.showAlertDialog(UserLogin.this, "Message", "Please enter userid", false);
			}else if(strPwd.length()==0){
				Utils.showAlertDialog(UserLogin.this, "Message", "Please enter password", false);
			}else{
				int logincheck=dbcls.checkLogin(strUid, Utils.md5Pwd(UserLogin.this,strPwd));
				if(logincheck==1){
					
					Editor editor = pref.edit();
					editor.putString("userId", edtUserId.getText().toString().trim());
					editor.commit();
					
					startActivity(new Intent(UserLogin.this,UserHome.class));
					finish();
				}else{
					Utils.showAlertDialog(UserLogin.this, "Message", "Login failed..", false);
				}
			
			}
			
		}
		if(v==btnNewReg){
			startActivity(new Intent(UserLogin.this,UserRegistration.class));
			finish();
		}
		if(v==btnBack){
			startActivity(new Intent(UserLogin.this,Home.class));
			finish();
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbcls.closeDB();
	}
	
	public boolean onKeyDown(int iKeyCode, KeyEvent event) {

		if (iKeyCode == KeyEvent.KEYCODE_BACK
				|| iKeyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(iKeyCode, event);
	}
}
