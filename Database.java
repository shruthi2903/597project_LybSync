package com.lyb.lyb_sync;


 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class Database extends SQLiteOpenHelper {
 
    // Logcat tag
    private static final String LOG = Database.class.getName();
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "LYBSYNC";
 
    // Table Names
    private static final String TABLE_USER_REGISTRATION = "USER_REGISTRATION";
    private static final String TABLE_NEW_BOOK_REGISTRATION = "BOOK_REGISTRATION";
    private static final String TABLE_LYBSYNC_TRANSACTION = "LYBSYNC_TRANSACTION";
 
    // user registration column names
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_MOBILE = "user_mobile";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PASSWORD = "user_password";
   
    // book registration column names
    private static final String BOOK_ID = "book_id";
    private static final String BOOK_NAME = "book_name";
    private static final String BOOK_AUTHOR = "book_author";
    private static final String BOOK_FLOOR = "book_floor";
    private static final String BOOK_RACK = "book_rack";
    private static final String BOOK_STATUS = "book_status";
    
    // book transaction column names
    private static final String TRAN_USER_ID = "tran_user_id";
    private static final String TRAN_BOOK_ID = "tran_book_id";
    private static final String TRAN_LOAN_HRS = "tran_loan_hrs";
    private static final String TRAN_SHARE_ID = "tran_share_id";
    private static final String TRAN_WAIT_LIST = "tran_wait_list";

   
    // Common column names
    private static final String KEY_CREATED_AT = "created_at";
 
 
    // Table Create Statements
    // user table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER_REGISTRATION + "(" + USER_ID + " INTEGER PRIMARY KEY," + USER_NAME
            + " TEXT," + USER_MOBILE + " TEXT," + USER_EMAIL + " TEXT," + USER_PASSWORD + " TEXT," + KEY_CREATED_AT
            + " DATETIME" + ")";
 
    // book table create statement
    private static final String CREATE_TABLE_BOOK = "CREATE TABLE " + TABLE_NEW_BOOK_REGISTRATION
            + "(" + BOOK_ID + " INTEGER PRIMARY KEY," + BOOK_NAME + " TEXT," + BOOK_AUTHOR + " TEXT," 
            + BOOK_FLOOR + " TEXT," + BOOK_RACK + " TEXT," + BOOK_STATUS + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    
    // transaction table create statement
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_LYBSYNC_TRANSACTION
            + "(" + TRAN_USER_ID + " INTEGER," + TRAN_BOOK_ID + " INTEGER," + TRAN_LOAN_HRS + " INTEGER," 
            + TRAN_SHARE_ID + " INTEGER," + TRAN_WAIT_LIST + " INTEGER," + KEY_CREATED_AT + " DATETIME" + ")";
 
 
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_REGISTRATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEW_BOOK_REGISTRATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LYBSYNC_TRANSACTION);
 
        // create new tables
        onCreate(db);
    }
 
    // ------------------------ "USER" table methods ----------------//
 
    /**
     * Creating USER
     */
    public long InsertUserRegistration(String uname,int uid,String umobile,String uemail,String upassword) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(USER_NAME, uname);
        values.put(USER_ID, uid);
        values.put(USER_MOBILE, umobile);
        values.put(USER_EMAIL, uemail);
        values.put(USER_PASSWORD, upassword);
        values.put(KEY_CREATED_AT, getDateTime());
 
        Log.i("user registration","inserted user details");
        // insert row
        long user_tb = db.insert(TABLE_USER_REGISTRATION, null, values);
        Log.i("user registration result","inserted user details values"+user_tb);
        return user_tb;
    }
    
    public int checkUserExists(int uid){
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select count (*) FROM USER_REGISTRATION where user_id = "+uid+"";
		Cursor cur = db.rawQuery(qry, null);
		Log.i("Check User Exists ", "qry:" + qry);
		cur.moveToFirst();
	    int count= cur.getInt(0);
	    Log.i("Check User Exists ", "result:" + count);
	    cur.close();
	    return count;
    }
    
    public int checkLogin(String uname,String upwd){
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select count (*) FROM USER_REGISTRATION where user_id = '"+uname+"' and user_password = '"+upwd+"'";
		Cursor cur = db.rawQuery(qry, null);
		Log.i("Check Login ", "qry:" + qry);
		cur.moveToFirst();
	    int count= cur.getInt(0);
	    Log.i("Check Login result ", "result:" + count);
	    cur.close();
	    return count;
    }
   
    public Cursor getUserName(int uid){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select user_name FROM USER_REGISTRATION where user_id="+uid;
		Cursor cur = db.rawQuery(qry, null);
		 Log.i("get user name ", "executed");
		return cur;
    	
    } 
    /**
     * Creating book
     */
    
    public long InsertBookRegistration(int bid,String bname,String bauthor,String bfloor,String brack,int bstatus) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(BOOK_ID, bid);
        values.put(BOOK_NAME, bname);
        values.put(BOOK_AUTHOR, bauthor);
        values.put(BOOK_FLOOR, bfloor);
        values.put(BOOK_RACK, brack);
        values.put(BOOK_STATUS, bstatus);
        values.put(KEY_CREATED_AT, getDateTime());
 
        Log.i("new book registration","inserted book details");
        // insert row
        long book_tb = db.insert(TABLE_NEW_BOOK_REGISTRATION, null, values);
 
        return book_tb;
    }
    public int checkBookExists(int bid){
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select count (*) FROM BOOK_REGISTRATION where book_id = "+bid+"";
		Cursor cur = db.rawQuery(qry, null);
		Log.i("Check book Exists ", "qry:" + qry);
		cur.moveToFirst();
	    int count= cur.getInt(0);
	    Log.i("Check book Exists ", "result:" + count);
	    cur.close();
	    return count;
    }
    
    public Cursor getAllBooks(){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select book_id, book_name FROM BOOK_REGISTRATION ".trim();
		Cursor cur = db.rawQuery(qry, null);
		 Log.i("get all book result ", "executed "+cur);
		return cur;
    	
    }  
    //select * from books b, trans t where b.bid=t.bid and uid=1
    public Cursor getReturnBooks(int userId){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select book_id, book_name,book_author FROM BOOK_REGISTRATION b, LYBSYNC_TRANSACTION t where b.book_id=t.tran_book_id and tran_user_id="+userId;
		Cursor cur = db.rawQuery(qry, null);
		 Log.i("get return book result ", "executed "+cur);
		return cur;
    	
    }
    
    public boolean updateReturnBook(int book_id, int flag_value) {
    	SQLiteDatabase db = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(BOOK_STATUS, flag_value);
		Log.i("updated return book: ", "book id:"+book_id);
		return db.update(TABLE_NEW_BOOK_REGISTRATION, v, BOOK_ID
				+ "=" + book_id + "", null) > 0;
	}
    
    public Cursor getWaitList(int tranBookId){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select tran_wait_list FROM LYBSYNC_TRANSACTION where tran_book_id="+tranBookId;
		Cursor cur = db.rawQuery(qry, null);
		 Log.i("get wait list record ", "executed");
		return cur;
    	
    } 
    
    public Cursor getSelectBook(int bid){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select * FROM BOOK_REGISTRATION where book_id = "+bid+"".trim();
		Cursor cur = db.rawQuery(qry, null);
		 Log.i("select book result ", "executed");
		return cur;
    	
    }
    
    /**
     * Updating a book
     */
    
    public boolean updateBookAvailable(int book_id, int flag_value) {
    	SQLiteDatabase db = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(BOOK_STATUS, flag_value);
		Log.i("updated book available: ", "book id:"+book_id);
		return db.update(TABLE_NEW_BOOK_REGISTRATION, v, BOOK_ID
				+ "=" + book_id + "", null) > 0;
	}
    
    public boolean updateWaitList(int book_id, int flag_value) {
    	SQLiteDatabase db = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(TRAN_WAIT_LIST, flag_value);
		Log.i("updated waitlist: ", "book id:"+book_id);
		return db.update(TABLE_LYBSYNC_TRANSACTION, v, TRAN_BOOK_ID
				+ "=" + book_id + "", null) > 0;
	}

    public int updateBook(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();  
        values.put(BOOK_STATUS, tag_id);
 
        // updating row
        return db.update(TABLE_USER_REGISTRATION, values, BOOK_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    
    /**
     * Deleting a todo tag
     */
    public boolean deleteReturnBook(int id) { 
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_LYBSYNC_TRANSACTION, TRAN_BOOK_ID + " = ?",
                new String[] { String.valueOf(id) })>0;
    }
    
    /**
     * Creating transaction
     */
    public long InsertBookTransaction(int tran_uid,int tran_bid,int tran_loanhrs,int tran_share,int tran_wait) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TRAN_USER_ID, tran_uid);
        values.put(TRAN_BOOK_ID, tran_bid);
        values.put(TRAN_LOAN_HRS, tran_loanhrs);
        values.put(TRAN_SHARE_ID, tran_share);
        values.put(TRAN_WAIT_LIST, tran_wait);
        values.put(KEY_CREATED_AT, getDateTime());
 
        Log.i("transaction","inserted transaction details");
        // insert row
        long tran_tb = db.insert(TABLE_LYBSYNC_TRANSACTION, null, values);
 
        return tran_tb;
    }
  
    public Cursor getAllAdminBooks(){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	String qry = "select * FROM BOOK_REGISTRATION ".trim();
		Cursor cur = db.rawQuery(qry, null);
		 Log.i("get all admin book result ", "executed "+cur);
		return cur;
    	
    }   
    
    public boolean deleteAdminBook(int id) { 
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NEW_BOOK_REGISTRATION, BOOK_ID + " = ?",
                new String[] { String.valueOf(id) })>0;
    }
 
    public boolean updateAdminBook(int book_id, String bname,String bauthor,String bfloor,String brack) {
    	SQLiteDatabase db = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(BOOK_NAME, bname); 
		v.put(BOOK_AUTHOR, bauthor);
		v.put(BOOK_FLOOR, bfloor);
		v.put(BOOK_RACK, brack);
		Log.i("updated booklist admin: ", "book id:"+book_id);
		return db.update(TABLE_NEW_BOOK_REGISTRATION, v, BOOK_ID
				+ "=" + book_id + "", null) > 0;
	}
    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
 
    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
