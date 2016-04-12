package com.lyb.lyb_sync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserHome extends ActionBarActivity implements OnClickListener{

	Button btnSearchBook,btnReturnBook,btnBack;
	Database dbcls;
	SharedPreferences pref;
	int userID=0;
	Cursor cur;
	String strUname="User";
	TextView txtUserName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_home);
		
		pref = getApplicationContext().getSharedPreferences("lybsync", 0);
		
		dbcls= new Database(UserHome.this);
		 
		txtUserName=(TextView)findViewById(R.id.txt_userhome_title);
		
		btnSearchBook=(Button)findViewById(R.id.btn_userhome_searchbook);
		btnSearchBook.setOnClickListener(this);
		
		btnReturnBook=(Button)findViewById(R.id.btn_userhome_returnbook);
		btnReturnBook.setOnClickListener(this);
		
		btnBack=(Button)findViewById(R.id.btn_userhome_back);
		btnBack.setOnClickListener(this);
		
		userID=Integer.parseInt(pref.getString("userId","0"));
		
		cur=dbcls.getUserName(userID);
		if(cur.getCount()>0){
			if(cur.moveToFirst()){
				strUname=cur.getString(cur.getColumnIndex(cur.getColumnName(0)));
			}
			txtUserName.setText("Welcom to "+strUname);
		}        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnSearchBook){
			startActivity(new Intent(UserHome.this,SearchBook.class));
			finish();
		}
		if(v==btnReturnBook){
			startActivity(new Intent(UserHome.this,ReturnBook.class));
			finish();
			//Toast.makeText(UserHome.this, "Under development", Toast.LENGTH_LONG).show();
		}
		if(v==btnBack){
			startActivity(new Intent(UserHome.this,UserLogin.class));
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
