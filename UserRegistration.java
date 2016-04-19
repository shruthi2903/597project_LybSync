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
import android.widget.EditText;

public class UserRegistration extends ActionBarActivity implements OnClickListener{

	EditText edtUname,edtUid,edtMobile,edtEmail,edtPwd,edtCpwd;
	Button btnNewReg,btnBack;
	String strUname="",strUid="",strMobile="",strEmail="",strPwd="",strCpwd="";
	Database dbcls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_new_reg);
		
		dbcls=new Database(getApplicationContext());
		
		edtUname=(EditText)findViewById(R.id.edt_newuser_name);
		edtUid=(EditText)findViewById(R.id.edt_newuser_id);
		edtMobile=(EditText)findViewById(R.id.edt_newuser_mobile);
		edtEmail=(EditText)findViewById(R.id.edt_newuser_email);
		edtPwd=(EditText)findViewById(R.id.edt_newuser_pwd);
		edtCpwd=(EditText)findViewById(R.id.edt_newuser_cpwd);
		
		btnNewReg=(Button)findViewById(R.id.btn_newuser_registrar);
		btnNewReg.setOnClickListener(this);
		
		btnBack=(Button)findViewById(R.id.btn_newuser_back);
		btnBack.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		strUname=edtUname.getText().toString().trim();
		strUid=edtUid.getText().toString().trim();
		strMobile=edtMobile.getText().toString().trim();
		strEmail=edtEmail.getText().toString().trim();
		strPwd=edtPwd.getText().toString().trim();
		strCpwd=edtCpwd.getText().toString().trim();
		
		if(v==btnBack){
			startActivity(new Intent(UserRegistration.this,UserLogin.class));
			finish();
		}
		if(v==btnNewReg){
			
			if(strUname.length()==0){
				Utils.showAlertDialog(UserRegistration.this, "Message", "Please enter username", false);
			}else if(strUid.length()==0){
				Utils.showAlertDialog(UserRegistration.this, "Message", "Please enter user id", false);
			}else if(strMobile.length()==0){
				Utils.showAlertDialog(UserRegistration.this, "Message", "Please enter mobile number", false);
			}else if(strEmail.length()==0){
				Utils.showAlertDialog(UserRegistration.this, "Message", "Please enter email id", false);
			}else if(!Utils.checkemailformat(UserRegistration.this,strEmail)){
				Utils.showAlertDialog(UserRegistration.this, "Message", "Please enter valid email", false);
			}else if(strPwd.length()==0){
				Utils.showAlertDialog(UserRegistration.this, "Message", "Please enter password", false);
			}else if(strCpwd.length()==0){
				Utils.showAlertDialog(UserRegistration.this, "Message", "Please enter conform password", false);
			}else if(!strPwd.equals(strCpwd)){
				Utils.showAlertDialog(UserRegistration.this, "Message", "password and conform password not match", false);
			}else{
				int userExists=dbcls.checkUserExists(Integer.parseInt(strUid)); 
				if(userExists==0){
					dbcls.InsertUserRegistration(strUname, Integer.parseInt(strUid), strMobile, strEmail, Utils.md5Pwd(getApplicationContext(),strPwd));
					
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							UserRegistration.this);

					builder.setMessage("Successfully registrared..")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {

											startActivity(new Intent(UserRegistration.this,UserLogin.class));
											finish();

										}
									});
					final AlertDialog alert = builder.create();

					alert.show();
				}else{
					Utils.showAlertDialog(UserRegistration.this, "Message", "User already exists", false);
				}
				
			}
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
