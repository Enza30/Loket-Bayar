package loketbayar.id;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import loketbayar.id.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class DatabaseLocal extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mudahbayar.db";
    public static int DATABASE_VERSION;
    private DatabaseLocal databaseLocal;
    public static SQLiteDatabase db;
    private Context ctx;
    private SharedPreferences boyprefs;
    DatabaseLocal(Context context) {

        super(context,  DATABASE_NAME, null,8);
        this.ctx=context;
        boyprefs =context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + "keterangan" + " ( " +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "kodeunik" + " TEXT  ," +
                "kodeloket" + " TEXT  ," +
                "kodeproduk" + " TEXT  ," +
                "idpelanggan" + " TEXT  ," +
                "namagrup" + " TEXT );"

        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + "kolektif" + " ( " +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "kode_biller" + " TEXT  ," +
                "kode_produk_biller" + " TEXT  ," +
                "kode_produk" + " TEXT  ," +
                "nama_produk" + " TEXT  ," +
                "denom" + " TEXT  ," +
                "kategori" + " TEXT  ," +
                "admin_biller" + " TEXT  ," +
                "harga_biller" + " TEXT  ," +
                "tipe_fee" + " TEXT  ," +
                "fee_biller" + " TEXT  ," +
                "fee_app" + " TEXT  ," +
                "status" + " TEXT  ," +
                "created_on" + " TEXT  ," +
                "updated_on" + " TEXT  ," +
                "idmargin" + " TEXT  ," +
                "kode_produk_margin" + " TEXT  ," +
                "kode_ca" + " TEXT  ," +
                "kode_sub_ca" + " TEXT  ," +
                "kode_loket" + " TEXT  ," +
                "fee_ca" + " TEXT  ," +
                "fee_sub_ca" + " TEXT  ," +
                "created_on_margin" + " TEXT  ," +
                "updated_on_margin" + " TEXT  ," +
                "kode_ca_ca" + " TEXT  ," +
                "kode_loket_ca" + " TEXT  ," +
                "id_awal" + " TEXT  ," +
                "kode_loket_awal" + " TEXT  ," +
                "nama_grup" + " TEXT  ," +
                "idpel" + " TEXT  ," +
                "status_awal" + " TEXT  ," +
                "timestamp" + " TEXT ,"+
                "nourut"+"TEXT);"

        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "keterangan" + ";");
        db.execSQL("DROP TABLE IF EXISTS " + "kolektif" + ";");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "keterangan" + ";");
        db.execSQL("DROP TABLE IF EXISTS " + "kolektif" + ";");
        onCreate(db);
    }

    String hilangkan(String table){
        db = this.getWritableDatabase();
        db.delete(table, null, null);

        return "sukses";

    }

    String SaveDataAkhir( String kode_biller,
                                String kode_produk_biller,
                                String kode_produk,
                                String nama_produk,
                                String denom,
                                String kategori,
                                String admin_biller,
                                String harga_biller,
                                String tipe_fee,
                                String fee_biller,
                                String fee_app,
                                String status,
                                String created_on,
                                String updated_on,
                                String idmargin,
                                String kode_produk_margin,
                                String kode_ca,
                                String kode_sub_ca,
                                String kode_loket,
                                String fee_ca,
                                String fee_sub_ca,
                                String created_on_margin,
                                String updated_on_margin,
                                String kode_ca_ca,
                                String kode_loket_ca,
                                String id_awal,
                                String kode_loket_awal,
                                String nama_grup,
                                String idpel,
                                String status_awal,
                                String timestamp,String nourut){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("kode_biller", kode_biller);
        contentValues.put("kode_produk_biller", kode_produk_biller);
        contentValues.put("kode_produk", kode_produk);
        contentValues.put("nama_produk", nama_produk);
        contentValues.put("denom", denom);
        contentValues.put("kategori", kategori);
        contentValues.put("admin_biller", admin_biller);
        contentValues.put("harga_biller", harga_biller);
        contentValues.put("tipe_fee", tipe_fee);
        contentValues.put("fee_biller", fee_biller);
        contentValues.put("fee_app", fee_app);
        contentValues.put("status", status);
        contentValues.put("created_on", created_on);
        contentValues.put("updated_on", updated_on);
        contentValues.put("idmargin", idmargin);
        contentValues.put("kode_produk_margin", kode_produk_margin);
        contentValues.put("kode_ca", kode_ca);
        contentValues.put("kode_sub_ca", kode_sub_ca);
        contentValues.put("kode_loket", kode_loket);
        contentValues.put("fee_ca", fee_ca);
        contentValues.put("fee_sub_ca", fee_sub_ca);
        contentValues.put("created_on_margin", created_on_margin);
        contentValues.put("updated_on_margin", updated_on_margin);
        contentValues.put("kode_ca_ca", kode_ca_ca);
        contentValues.put("kode_loket_ca", kode_loket_ca);
        contentValues.put("id_awal", id_awal);
        contentValues.put("kode_loket_awal", kode_loket_awal);
        contentValues.put("nama_grup", nama_grup);
        contentValues.put("idpel", idpel);
        contentValues.put("status_awal", status_awal);
        contentValues.put("timestamp", timestamp);
        contentValues.put("nourut", nourut);

        long rowInserted = db.insert("kolektif", null, contentValues);
        if(rowInserted!=-1){
            return "sukses";
        }else{
            return "gagal";
        }
    }
    void SaveExcel(String idpelanggan, String namagrup, String kodeunik, String kodeloket){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idpelanggan", idpelanggan);
        contentValues.put("namagrup", namagrup);
        contentValues.put("kodeunik", kodeunik);
        contentValues.put("kodeloket", kodeloket);
        contentValues.put("kodeproduk", "TAGPLN");
        long rowInserted = db.insert("keterangan", null, contentValues);
       
    }

    String Updatenota(String iditem, String qty, String nota){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("qty", qty);
        long rowInserted = db.update("tnota",contentValues,"iditem" + " = ? AND " + "nota" + " = ?",new String[]{iditem, nota});
        if(rowInserted!=-1){
            return "sukses";
        }else{
            return "gagal";
        }
    }
    String hapus(String table, String arg1, String params1){
        db = this.getWritableDatabase();
        if (db.delete(table, arg1 + "=" +"'"+ params1+"'", null)>0){
            return "sukses";
        }else{
            return "gagal";
        }
    }
    String hapus2param(String table, String arg1, String params1,String arg2, String params2){
        db = this.getWritableDatabase();
        if (db.delete(table, arg1 + "=" +"'"+ params1+"'"+" AND "+arg2 + "=" +"'"+ params2+"'", null)>0){
            return "sukses";
        }else{
            return "gagal";
        }
    }
    String hapus3param(String table, String arg1, String params1,String arg2,
                       String params2,String arg3, String params3){
        db = this.getWritableDatabase();
        if (db.delete(table, arg1 + "=" +"'"+ params1+"'"+" AND "+
                arg2 + "=" +"'"+ params2+"'"+" AND "+ arg3 + "=" +"'"+ params3+"'", null)>0){
            return "sukses";
        }else{
            return "gagal";
        }
    }
    public String Ambildatas(String table) {
        db = this.getReadableDatabase();
        String query = ("Select * from " + table);
        Cursor cursor = db.rawQuery(query, null );
        JSONArray resultSet     = new JSONArray();
        JSONArray kl=new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();

        JSONObject studentsObj = new JSONObject();
        try {
            studentsObj.put("data", resultSet);
            kl=studentsObj.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(kl.length() > 0){
            return studentsObj.toString();
        }else{
            return "tidak ada data";
        }

    }

    public String Ambildata(String table, String arg1, String params1) {
        db = this.getReadableDatabase();
        String query = ("Select * from " + table+ " where "+ arg1+ " = " + "'" + params1 + "'");
        Cursor cursor = db.rawQuery(query, null );
        JSONArray resultSet     = new JSONArray();
        JSONArray kl=new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();

        JSONObject studentsObj = new JSONObject();
        try {
            studentsObj.put("data", resultSet);
            kl=studentsObj.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(kl.length() > 0){
            return studentsObj.toString();
        }else{
            return "tidak ada data";
        }

    }


    public String Ambildata2(String table, String arg1, String params1,String arg2, String params2) {
        db = this.getReadableDatabase();
        String query = ("Select * from " + table+ " where "+ arg1+ " = " + "'" + params1 + "'"+ " and "+ arg2+ " = " + "'" + params2 + "'");
        Cursor cursor = db.rawQuery(query, null );
        JSONArray resultSet     = new JSONArray();
        JSONArray kl=new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();

        JSONObject studentsObj = new JSONObject();
        try {
            studentsObj.put("data", resultSet);
            kl=studentsObj.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(kl.length() > 0){
            return studentsObj.toString();
        }else{
            return "tidak ada data";
        }

    }


    public long getTaskCount(String tables) {
        return DatabaseUtils.queryNumEntries(db, tables);
    }
    public int Itung(String TABLE_NAME, String arg1, String isi) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME+ " where "+ arg1+ " = " + "'" + isi + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int count(String TABLE_NAME, String arg1, String isi, String arg2, String isi2) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME+ " where "+ arg1+ " = " + "'" + isi + "'"+ " and "+ arg2+ " = " + "'" + isi2 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public String cariin(String arg1, String params1, String tables, String carian) {
        db = this.getReadableDatabase();
        String query = ("Select " +carian+" from " + tables + " where "+ arg1+ " = " + "'" + params1 + "'");
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public String cariin2param(String arg1, String params1, String arg2, String params2, String tables, String carian) {
        db = this.getReadableDatabase();
        String query = ("Select " +carian+" from " + tables + " where "+ arg1+ " = " + "'" + params1 + "'"+ " and "+ arg2+ " = " + "'" + params2 + "'");
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public Integer GetJumlah(String arg1, String params1, String tables) {
        db = this.getReadableDatabase();
        String query = ("Select * from " + tables + " where "+ arg1+ " = " + "'" + params1 +  "'");
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

}