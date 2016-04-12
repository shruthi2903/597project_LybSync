package com.lyb.lyb_sync;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewBook extends ActionBarActivity implements OnClickListener {

	Button btnSubmit,btnBack;
	EditText edtBid,edtBname,edtBauthor,edtBrack;
	Spinner spinBfloor;
	String strId="",strName="",strAuthor="",strRack="",strFloor="";
	int floorId=0;
	Database dbcls;
	String[] floorIds=new String[]{"Select floor","1st floor","2nd floor","3nd floor","4th floor"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newbook_reg);

		dbcls=new Database(NewBook.this);
		
		edtBid=(EditText)findViewById(R.id.edt_newbook_id);
		edtBname=(EditText)findViewById(R.id.edt_newbook_name);
		edtBauthor=(EditText)findViewById(R.id.edt_newbook_author);
		edtBrack=(EditText)findViewById(R.id.edt_newbook_rackno);
		
		spinBfloor=(Spinner)findViewById(R.id.spin_newbook_floorno);
		ArrayAdapter<String> fAdp=new ArrayAdapter<String>(NewBook.this,android.R.layout.simple_spinner_item, floorIds);
		spinBfloor.setAdapter(fAdp);
		
		btnBack = (Button) findViewById(R.id.btn_newbook_back);
		btnBack.setOnClickListener(this);
		
		btnSubmit = (Button) findViewById(R.id.btn_newbook_submit);
		btnSubmit.setOnClickListener(this);
		
		spinBfloor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				floorId=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		strId=edtBid.getText().toString().trim();
		strName=edtBname.getText().toString().trim();
		strAuthor=edtBauthor.getText().toString().trim();
		strRack=edtBrack.getText().toString().trim();
		strFloor=spinBfloor.getSelectedItem().toString().trim();
		
		if (v == btnBack) {
			startActivity(new Intent(NewBook.this, AdminHome.class));
			finish();
		}if (v == btnSubmit) {
			if(strId.length()==0){
				Utils.showAlertDialog(NewBook.this, "Message", "Please enter book id", false);
			}else if(strName.length()==0){
				Utils.showAlertDialog(NewBook.this, "Message", "Please enter book name", false);
			}else if(strAuthor.length()==0){
				Utils.showAlertDialog(NewBook.this, "Message", "Please enter author name", false);
			}else if(floorId<=0){
				Utils.showAlertDialog(NewBook.this, "Message", "Please select floor", false);
			}else if(strRack.length()==0){
				Utils.showAlertDialog(NewBook.this, "Message", "Please enter rack number", false);
			}else{
				int checkBookExists=dbcls.checkBookExists(Integer.parseInt(strId));
				if(checkBookExists==0){  
					dbcls.InsertBookRegistration(Integer.parseInt(strId), strName, strAuthor, String.valueOf(floorId), strRack, 0);
					
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							NewBook.this);

					builder.setMessage("Successfully created..")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {

											startActivity(new Intent(NewBook.this,AdminHome.class));
											finish();

										}
									});
					final AlertDialog alert = builder.create();

					alert.show();
				}else{
					Utils.showAlertDialog(NewBook.this, "Message", "Book already exists", false);
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
