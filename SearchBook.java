package com.lyb.lyb_sync;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchBook extends ActionBarActivity implements OnClickListener,OnItemSelectedListener{

	AutoCompleteTextView autoEdtBooks;
	Button btnSubmit,btnBack,btnContinue;
	Spinner spinLoan,spinShare,spinWait;
	ImageButton btnSearch;
	Database dbcls;
	private ProgressDialog progressDialog;
	ArrayList<String> bookNames = null;       
	ArrayList<String> bookCodes;
	Cursor cur;
	int bookPosition=0;   
	String bookName="",author="",floor="",rack="",bookdate="";
	int bookId=0,status=0,loanPosition=0,sharePosition=0,waitPosition=0,waitCount=0;
	String[] loanList=new String[]{"Select","1 hr","2 hrs","3 hrs","4 hrs","5 hrs"};
	String[] shareList=new String[]{"Select","Yes","No"};
	String[] waitList=new String[]{"Select","Yes","No"};
	TextView txtBookName,txtBookAuthor,txtBookLocation,txtBookStatus,txtLoan,txtShare,txtWait;
	SharedPreferences pref;
	int userID=0;
	LinearLayout hideBookstatus,hideBookTrans;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_book);
		
		dbcls=new Database(SearchBook.this);
		
		pref = getApplicationContext().getSharedPreferences("lybsync", 0);
		
		txtBookStatus=(TextView)findViewById(R.id.txt_searchbook_status);
		txtBookName=(TextView)findViewById(R.id.txt_searchbook_name);
		txtBookAuthor=(TextView)findViewById(R.id.txt_searchbook_author);
		txtBookLocation=(TextView)findViewById(R.id.txt_searchbook_florrack);
		
		txtLoan=(TextView)findViewById(R.id.txt_searchbook_loan);
		txtShare=(TextView)findViewById(R.id.txt_searchbook_share);
		txtWait=(TextView)findViewById(R.id.txt_searchbook_wait);
		
		autoEdtBooks=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		
		spinLoan=(Spinner)findViewById(R.id.spin_searchbook_loanhrs);
		spinLoan.setOnItemSelectedListener(this);
		ArrayAdapter<String> loanAdp=new ArrayAdapter<String>(SearchBook.this,android.R.layout.simple_spinner_item, loanList);
		spinLoan.setAdapter(loanAdp);
		
		spinShare=(Spinner)findViewById(R.id.spin_searchbook_share);
		spinShare.setOnItemSelectedListener(this);
		ArrayAdapter<String> shareAdp=new ArrayAdapter<String>(SearchBook.this,android.R.layout.simple_spinner_item, shareList);
		spinShare.setAdapter(shareAdp);
		
		spinWait=(Spinner)findViewById(R.id.spin_searchbook_waitlist);
		spinWait.setOnItemSelectedListener(this);
		ArrayAdapter<String> fAdp=new ArrayAdapter<String>(SearchBook.this,android.R.layout.simple_spinner_item, waitList);
		spinWait.setAdapter(fAdp);
		
		btnSubmit=(Button)findViewById(R.id.btn_searchbook_submit);
		btnSubmit.setOnClickListener(this);
		btnSubmit.setVisibility(View.GONE);
		
		btnBack=(Button)findViewById(R.id.btn_searchbook_back);
		btnBack.setOnClickListener(this);
		
		btnSearch=(ImageButton)findViewById(R.id.btn_searchbook_search);
		btnSearch.setOnClickListener(this);
		
		btnContinue=(Button)findViewById(R.id.btn_searchbook_continue);
		btnContinue.setOnClickListener(this);
		
		autoEdtBooks.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override 
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				hideBookstatus.setVisibility(View.GONE);
				hideBookTrans.setVisibility(View.GONE);
				bookPosition=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});  
		cur=dbcls.getAllBooks();
		if(cur.getCount()>0){
			GetBooksData _getBooks=new GetBooksData();
			_getBooks.execute();
		}else{
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					SearchBook.this);

			builder.setMessage("No books are available..")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

				 					startActivity(new Intent(SearchBook.this,UserHome.class));
									finish();

								}
							});
			final AlertDialog alert = builder.create();

			alert.show();   
		}
		
		
		userID=Integer.parseInt(pref.getString("userId","0"));
		
		
		hideBookstatus=(LinearLayout)findViewById(R.id.layout_searchbook_bookstatus);
		hideBookstatus.setVisibility(View.GONE);
		
		hideBookTrans=(LinearLayout)findViewById(R.id.layout_searchbook_booktrans);
		hideBookTrans.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		btnSubmit.setVisibility(View.GONE);
		if(btnBack==v){
			startActivity(new Intent(SearchBook.this,UserHome.class));
			finish();
		}
		if(btnSearch==v){
			
			cur=dbcls.getSelectBook(Integer.parseInt(bookCodes.get(bookPosition).toString().trim()));
			Log.i("select book id","book id:"+bookId);
			if(cur.getCount()>0)
	        {
				
				if(cur.moveToFirst())
	        	{
	        		
					bookId=Integer.parseInt(cur.getString(cur.getColumnIndex(cur.getColumnName(0))));
	        		 bookName=cur.getString(cur.getColumnIndex(cur.getColumnName(1)));
	        		 author=cur.getString(cur.getColumnIndex(cur.getColumnName(2)));
	        		 floor=cur.getString(cur.getColumnIndex(cur.getColumnName(3)));	
	        		 rack=cur.getString(cur.getColumnIndex(cur.getColumnName(4)));
	        		 status=Integer.parseInt(cur.getString(cur.getColumnIndex(cur.getColumnName(5))));
	        		 bookdate=cur.getString(cur.getColumnIndex(cur.getColumnName(6)));
	        	}
				txtBookName.setText("Book Name:"+bookName);
				txtBookAuthor.setText("Author:"+author);
				txtBookLocation.setText(floor+" st/nd floor, rack number: "+rack);
				if(status==0){
					txtBookStatus.setText("Book Status: Available");
				}else{
					txtBookStatus.setText("Book Status: Not available");
				}
				hideBookstatus.setVisibility(View.VISIBLE);
				
	        }else{
	        	Utils.showAlertDialog(SearchBook.this, "Message", "no records found...", false);
	        }
		}
		if(btnContinue==v){
			btnSubmit.setVisibility(View.VISIBLE);
			hideBookTrans.setVisibility(View.VISIBLE);
			if(status==0){
				spinWait.setVisibility(View.GONE);
				spinLoan.setVisibility(View.VISIBLE);
				spinShare.setVisibility(View.VISIBLE);
				
				txtLoan.setVisibility(View.VISIBLE);
				txtShare.setVisibility(View.VISIBLE);
				txtWait.setVisibility(View.GONE);
				
			}else{
				spinWait.setVisibility(View.VISIBLE);
				spinLoan.setVisibility(View.GONE);
				spinShare.setVisibility(View.GONE);
				
				txtLoan.setVisibility(View.GONE);
				txtShare.setVisibility(View.GONE);
				txtWait.setVisibility(View.VISIBLE);
			}
		}
		if(btnSubmit==v){
			
			if(autoEdtBooks.getText().toString().trim().length()==0){
				Utils.showAlertDialog(SearchBook.this, "Message", "search book", false);
			}else if(bookId==0){
				Utils.showAlertDialog(SearchBook.this, "Message", "Please get the book details", false);
			}else if(status==0 && loanPosition<=0){
				Utils.showAlertDialog(SearchBook.this, "Message", "Please select loan hours", false);
			}else if(status==0 && sharePosition<=0){
				Utils.showAlertDialog(SearchBook.this, "Message", "Do you want to share the book?", false);
			}else if(status==1 && waitPosition<=0){
				Utils.showAlertDialog(SearchBook.this, "Message", "Do you want to be wait list?", false);
			}else if(status==0){
				dbcls.InsertBookTransaction(userID, bookId, loanPosition, sharePosition, 0);
				boolean statusFlag=dbcls.updateBookAvailable(bookId, 1);
				if(statusFlag){
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchBook.this);

					builder.setMessage("Successfully book selected..")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {

						 					startActivity(new Intent(SearchBook.this,UserHome.class));
											finish();

										}
									});
					final AlertDialog alert = builder.create();

					alert.show();
				}
			}else if(waitPosition==1){
				cur=dbcls.getWaitList(bookId);
				if(cur.getCount()>0){
					if(cur.moveToFirst())
		        	{
						waitCount=Integer.parseInt(cur.getString(cur.getColumnIndex(cur.getColumnName(0))));
		        	}
				}
				boolean waitFlag=dbcls.updateWaitList(bookId, waitCount+1);
				if(waitFlag){
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchBook.this);

					builder.setMessage("Your waiting list is:"+(waitCount+1))
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {

											startActivity(new Intent(SearchBook.this,UserHome.class));
											finish();

										}
									});
					final AlertDialog alert = builder.create();

					alert.show();
				}
			}else{
				startActivity(new Intent(SearchBook.this,UserHome.class));
				finish();
			}
		}
	}
	class GetBooksData extends AsyncTask<String, String, String> {
		public void onPreExecute() {
			progressDialog = new ProgressDialog(SearchBook.this);
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
					//bookCodes.add("0");
					//bookNames.add("Which tile are you looking for?");
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
				Log.i("get all books","doInback exeption:"+exception.toString());
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
							SearchBook.this,
							android.R.layout.simple_spinner_item, bookNames);
					autoEdtBooks.setAdapter(adp);
					autoEdtBooks.setThreshold(1);
		        }else{
		        	Utils.showAlertDialog(SearchBook.this, "Message", "no records found...", false);
		        }
						
			} catch (NullPointerException e) {
				Toast.makeText(SearchBook.this, "no record found...",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbcls.closeDB();
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(spinLoan==parent){
			loanPosition=position;
		}
		if(spinShare==parent){
			sharePosition=position;
		}
		if(spinWait==parent){
			waitPosition=position;
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onKeyDown(int iKeyCode, KeyEvent event) {

		if (iKeyCode == KeyEvent.KEYCODE_BACK
				|| iKeyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(iKeyCode, event);
	}

}
