package com.lyb.lyb_sync;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReturnBook extends ActionBarActivity implements OnItemClickListener{

	Button btnBack;
	ListView listView;
	Database dbcls;
	Cursor cur;
	private ProgressDialog progressDialog;
	ArrayList<String> listbNames = null;       
	ArrayList<String> listbCodes=null;
	ArrayList<String> listAuthor=null;
	Myadpter myAdapter;
	int listPosition=0;
	SharedPreferences pref;
	int userID=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.return_book);
		
		pref = getApplicationContext().getSharedPreferences("lybsync", 0);
		dbcls=new Database(ReturnBook.this);
		myAdapter = new Myadpter(ReturnBook.this);
		
		btnBack=(Button)findViewById(R.id.btn_returnbook_back);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ReturnBook.this,UserHome.class));
				finish();
			}
		});
		listView=(ListView)findViewById(R.id.listView_returnbook);
		listView.setOnItemClickListener(this);
		
		GetReturnBooksData _getRecords=new GetReturnBooksData();
		_getRecords.execute();
		
		userID=Integer.parseInt(pref.getString("userId","0"));
		
	}
	
	class GetReturnBooksData extends AsyncTask<String, String, String> {
		public void onPreExecute() {
			progressDialog = new ProgressDialog(ReturnBook.this);
			progressDialog.setMessage("Please wait...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try { 
				cur=dbcls.getReturnBooks(userID);
						
				if(cur.getCount()>0)
		        {
					listbNames = new ArrayList<String>();
					listbCodes = new ArrayList<String>();
					listAuthor = new ArrayList<String>();
					
					if(cur.moveToFirst())
		        	{
		        		do{
		        			String bookCode=cur.getString(cur.getColumnIndex(cur.getColumnName(0)));
		        			String bookName=cur.getString(cur.getColumnIndex(cur.getColumnName(1)));
		        			String bauthor=cur.getString(cur.getColumnIndex(cur.getColumnName(2)));
		        			listbNames.add(bookName);
		        			listbCodes.add(bookCode);
		        			listAuthor.add(bauthor);
		        		}while(cur.moveToNext());   
		        	}
		        }


			} catch (Exception exception) {
				exception.toString();
				Log.i("get return books","doInback exeption:"+exception.toString());
			}    
			progressDialog.dismiss();       
			return null;
		}   

		public void onPostExecute(String params) {
			progressDialog.dismiss();
			try {
				if(cur.getCount()>0)
		        {
					listView.setAdapter(myAdapter);
					myAdapter.notifyDataSetChanged();
					
		        }else{
		        	final AlertDialog.Builder builder = new AlertDialog.Builder(
							ReturnBook.this);

					builder.setMessage("No records found...")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {

											startActivity(new Intent(ReturnBook.this,UserHome.class));
											finish();

										}
									});
					final AlertDialog alert = builder.create();

					alert.show();
		        }
						
			} catch (NullPointerException e) {
				Toast.makeText(ReturnBook.this, "no record found...",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	public class Myadpter extends BaseAdapter {
		@SuppressWarnings("unused")
		private Context context;

		public Myadpter(Context c) {
			context = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listbNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub

			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = getLayoutInflater();
			View myview = inflater.inflate(R.layout.returnbook_listrow, null);

			TextView bname = (TextView) myview.findViewById(R.id.txt_returnbook_bname);
			TextView author = (TextView) myview
					.findViewById(R.id.txt_returnbook_author);

			bname.setText(listbNames.get(position));
			author.setText(listAuthor.get(position).toString());
		
			return myview;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		listPosition= Integer.parseInt(listbCodes.get(position).toString().trim());
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				ReturnBook.this);

		builder.setMessage("Are you sure do you want to return book?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {

								boolean updateflag=dbcls.updateReturnBook(listPosition, 0);
								if(updateflag){
									boolean delflag=dbcls.deleteReturnBook(listPosition);
									
									if(delflag){
										startActivity(new Intent(ReturnBook.this,ReturnBook.class));
										finish();
									}else{
										Utils.showAlertDialog(ReturnBook.this, "Message", "Sorry, Try again(del trans)", false);
									}
								}else{
									Utils.showAlertDialog(ReturnBook.this, "Message", "Sorry, Try again(update book)", false);
								}
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
