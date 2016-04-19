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
import android.widget.Toast;

public class AdminHome extends ActionBarActivity implements OnClickListener{

	Button btnNewBook,btnRemoveBook,btnUpdateBook,btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_home);
		
		btnNewBook=(Button)findViewById(R.id.btn_adminhome_newbook);
		btnNewBook.setOnClickListener(this);
		
		btnRemoveBook=(Button)findViewById(R.id.btn_adminhome_removebook);
		btnRemoveBook.setOnClickListener(this);
		
		btnUpdateBook=(Button)findViewById(R.id.btn_adminhome_updatebook);
		btnUpdateBook.setOnClickListener(this);
		
		btnBack=(Button)findViewById(R.id.btn_adminhome_back);
		btnBack.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnNewBook){
			startActivity(new Intent(AdminHome.this,NewBook.class));
			finish();
		}
		if(v==btnRemoveBook){
			startActivity(new Intent(AdminHome.this,RemoveBook.class));
			finish();
			//Toast.makeText(AdminHome.this, "Under development", Toast.LENGTH_LONG).show();
		}
		if(v==btnUpdateBook){
			startActivity(new Intent(AdminHome.this,UpdateBook.class));
			finish();
			//Toast.makeText(AdminHome.this, "Under development", Toast.LENGTH_LONG).show();
		}
		if(v==btnBack){
			startActivity(new Intent(AdminHome.this,Home.class));
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
