package com.lyb.lyb_sync;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateBook extends ActionBarActivity implements OnClickListener,OnItemSelectedListener{

	EditText edtBid,edtBname,edtBauthor,edtBrack;
	Button btnSubmit,btnBack;
	Spinner spinBook,spinBfloor;
	String[] floorIds=new String[]{"Select floor","1st floor","2nd floor","3nd floor","4th floor"};
	int floorId=0,bookPosition=0;
	Database dbcls;
	private ProgressDialog progressDialog;
	ArrayList<String> bookNames = null;       
	ArrayList<String> bookCodes;
	Cursor cur;  
	String bid="",bookName="",author="",floor="",rack="";
	String strId="",strName="",strAuthor="",strRack="",strFloor="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_book);
		
		dbcls=new Database(UpdateBook.this);

		edtBid=(EditText)findViewById(R.id.edt_updatebook_id);
		edtBid.setEnabled(false);
		
		edtBname=(EditText)findViewById(R.id.edt_updatebook_name);
		edtBauthor=(EditText)findViewById(R.id.edt_updatebook_author);
		edtBrack=(EditText)findViewById(R.id.edt_updatebook_rackno);
		
		spinBfloor=(Spinner)findViewById(R.id.spin_updatebook_floorno);
		spinBfloor.setOnItemSelectedListener(this);
		ArrayAdapter<String> fAdp=new ArrayAdapter<String>(UpdateBook.this,android.R.layout.simple_spinner_item, floorIds);
		spinBfloor.setAdapter(fAdp);
		
		spinBook=(Spinner)findViewById(R.id.spin_updatebook_selectbook);
		spinBook.setOnItemSelectedListener(this);
		
		btnSubmit=(Button)findViewById(R.id.btn_updatebook_submit);
		btnSubmit.setOnClickListener(this);
		
		btnBack=(Button)findViewById(R.id.btn_updatebook_back);
		btnBack.setOnClickListener(this);
		
		GetUpdateBooksData _update=new GetUpdateBooksData();
		_update.execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnSubmit){
			strId=edtBid.getText().toString().trim();
			strName=edtBname.getText().toString().trim();
			strAuthor=edtBauthor.getText().toString().trim();
			strRack=edtBrack.getText().toString().trim();
			strFloor=spinBfloor.getSelectedItem().toString().trim();
			if(strId.length()==0){
				Utils.showAlertDialog(UpdateBook.this, "Message", "Please enter book id", false);
			}else if(strName.length()==0){
				Utils.showAlertDialog(UpdateBook.this, "Message", "Please enter book name", false);
			}else if(strAuthor.length()==0){
				Utils.showAlertDialog(UpdateBook.this, "Message", "Please enter author name", false);
			}else if(floorId<=0){
				Utils.showAlertDialog(UpdateBook.this, "Message", "Please select floor", false);
			}else if(strRack.length()==0){
				Utils.showAlertDialog(UpdateBook.this, "Message", "Please enter rack number", false);
			}else{
				boolean updateFlag=dbcls.updateAdminBook(Integer.parseInt(strId), strName, strAuthor, String.valueOf(floorId), strRack);
				if(updateFlag){
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							UpdateBook.this);

					builder.setMessage("Successfully updated..")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {

											startActivity(new Intent(UpdateBook.this,UpdateBook.class));
											finish();

										}
									});
					final AlertDialog alert = builder.create();

					alert.show();
				}else{
					Utils.showAlertDialog(UpdateBook.this, "Message", "Please try again..", false);
				}
			}
		}
		if(v==btnBack){
			startActivity(new Intent(UpdateBook.this,AdminHome.class));
			finish();
		}
		
	}
	
	class GetUpdateBooksData extends AsyncTask<String, String, String> {
		public void onPreExecute() {
			progressDialog = new ProgressDialog(UpdateBook.this);
			progressDialog.setMessage("Loading books..");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try { 
				cur=dbcls.getAllBooks();
				
				if(cur.getCount()>0)
		        {
					bookNames = new ArrayList<String>();
					bookCodes = new ArrayList<String>();
					bookCodes.add("0");
					bookNames.add("Select book");
					if(cur.moveToFirst())
		        	{
		        		do{
		        			String bookCode=cur.getString(cur.getColumnIndex(cur.getColumnName(0)));
		        			String bookName=cur.getString(cur.getColumnIndex(cur.getColumnName(1)));
		        			bookNames.add(bookName);
		        			bookCodes.add(bookCode);  
		        		}while(cur.moveToNext());   
		        	}
		        }


			} catch (Exception exception) {
				exception.toString();
				Log.i("get all books updte","doInback exeption:"+exception.toString());
			}    
			progressDialog.dismiss();       
			return null;
		}   

		public void onPostExecute(String params) {
			progressDialog.dismiss();
			try {
				if(cur.getCount()>0)
		        {
			
					ArrayAdapter<String> adp = new ArrayAdapter<String>(
							UpdateBook.this,
							android.R.layout.simple_spinner_item, bookNames);
					spinBook.setAdapter(adp);
					
		        }else{
		        	final AlertDialog.Builder builder = new AlertDialog.Builder(
							UpdateBook.this);

					builder.setMessage("No records found...")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {

											startActivity(new Intent(UpdateBook.this,AdminHome.class));
											finish();

										}
									});
					final AlertDialog alert = builder.create();

					alert.show();
		        }
						
			} catch (NullPointerException e) {
				Toast.makeText(UpdateBook.this, "no record found...",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	
	public boolean onKeyDown(int iKeyCode, KeyEvent event) {

		if (iKeyCode == KeyEvent.KEYCODE_BACK
				|| iKeyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(iKeyCode, event);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent==spinBook){
			bookPosition=position;
			if(bookPosition>0){
				selectedBookDetails();
			}
		}
		if(parent==spinBfloor){
			floorId=position;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public void selectedBookDetails(){
		cur=dbcls.getSelectBook(Integer.parseInt(bookCodes.get(bookPosition).toString().trim()));
		Log.i("select book id","book id:"+bookPosition);
		if(cur.getCount()>0)
        {
			
			if(cur.moveToFirst())
        	{
        		
				bid=cur.getString(cur.getColumnIndex(cur.getColumnName(0)));
        		bookName=cur.getString(cur.getColumnIndex(cur.getColumnName(1)));
        		author=cur.getString(cur.getColumnIndex(cur.getColumnName(2)));
        		floor=cur.getString(cur.getColumnIndex(cur.getColumnName(3)));	
        		rack=cur.getString(cur.getColumnIndex(cur.getColumnName(4)));
        	}
			edtBid.setText(bid);
			edtBname.setText(bookName);
			edtBauthor.setText(author);
			edtBrack.setText(rack);
			spinBfloor.setSelection(Integer.parseInt(floor));
			
			
        }else{
        	Utils.showAlertDialog(UpdateBook.this, "Message", "no records found...", false);
        }
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbcls.closeDB();
	}
}
