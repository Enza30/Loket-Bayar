package loketbayar.id;

import android.Manifest;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.content.CursorLoader;
import android.net.ConnectivityManager;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import loketbayar.id.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static loketbayar.id.FilePickUtils.getDataColumn;
import static loketbayar.id.FilePickUtils.isDownloadsDocument;
import static loketbayar.id.FilePickUtils.isExternalStorageDocument;
import static loketbayar.id.FilePickUtils.isMediaDocument;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.PairedPrinter;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Pln2 extends AppCompatActivity implements PrintingCallback {
    private String datapln = "", keterangan = "", registrationDate = "",username,password,msn = "", powerpurchasede = "",
            jsontask3 = "", no_reff = "",responseCode, message, subscriberID, nama, tarif, daya, lembarTagihanSisa,
            lembarTagihan,totalTagihan, refnumber, infoText, detilTagihan,tokenNumber, biayaMeterai, ppn, ppj, angsuran,
            rpToken, kwh,arraynya = "", lembarTagihanTotal,productCode, refID, periode = "", registrationNumber = "",
            transactionName = "",tipe, idpelanggan,dari,token="",datauser, tagnya="",FeeSchema,FeeBulanan,hargalkt,tagpln,
            datareport,gpslat,gpslong,foto="", tangs,ket,koordinat;
    private Button btPrint, btPdf,btCheck,btSelesai,btupdatetul;
    private ImageView ivX,ivFototul;
    private LinearLayout llCetak, llToken, llPilih,llData,idpasca,adminlb,llketerangan;
    StringBuilder hasiltoken;
    Printing printing;
    private ImageView ivPrint;
    private Bitmap bitmap1,realimage1;
    private static final int PERMISSIONS_REQUEST_READ_CAMERA = 2;
    private Dialog load;
    private float admawal = 3000,tkn=0,admins = 0,adminss = 0, totalbayar = 0, tagplns = 0, fee_billers = 0,
            adminbiller = 0, pengurangan = 0,kembali = 0, lemtag = 0, harga_loket = 0,as = 20000,sals, taggs = 0,tokenpln2;
    private Button btBayar,btshare;
    private CheckBox ck3000, ck3500, ck5000,ck3250,ck4000,ck4500;
    private ImageView ivBack, ivRefresh;
    private float scale=0;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,
            tvTagPln,tvhrgloket, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul, tvJudulNama,tvgps,tvplg;
    private SharedPreferences boyprefs;
    private int nilaiTagihan = 0, denda = 0, admin = 0, total = 0,totalbayarr = 0,nominaltokenn=0;
    private EditText etidPelanggan,etketerangan;
    private int CAM_REQUEST1=1,SELECT_FILE1=2;
    public static String dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", admin_biller = "", fee_sub_ca = "", fee_sub_ca_1 = "", fee_sub_ca_2 = "", fee_sub_ca_3 = "";
    public static boolean udahceksaldo=false;
    public static float saldobayar = 0,saldoawal=0;
    private ArrayList<String> powerPurchaseDenom = new ArrayList<>();
    private ArrayList<String> namaproduklist = new ArrayList<>();
    ArrayList<String> meterAwal = new ArrayList<>();
    ArrayList<String> meterAkhir = new ArrayList<>();
    ArrayList<String> periodes = new ArrayList<>();
    ArrayList<String> nilaiTagihans = new ArrayList<>();
    ArrayList<String> dendas = new ArrayList<>();
    ArrayList<String> adminarray = new ArrayList<>();
    ArrayList<String> totals = new ArrayList<>();
    ArrayList<String> fee = new ArrayList<>();
    PdfWriter writer;
    Spinner spTipe, spNominal;
    ArrayList<String> listtipe = new ArrayList<>();
    ArrayList<String> listtipe2 = new ArrayList<>();
    ArrayList<String> jsonnya = new ArrayList<>();
    private DecimalFormat format = new DecimalFormat("###,###,###.##");
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    StringBuilder lembar_tagihan_untuk_data;
    StringBuilder mtrawal_tagihan_untuk_data;
    StringBuilder mtrakhir_tagihan_untuk_data;
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> idpellist = new ArrayList<>();
    private ArrayList<String> namalist = new ArrayList<>();
    private ArrayList<String> alamatlist = new ArrayList<>();
    private ArrayList<String> tariflist = new ArrayList<>();
    private ArrayList<String> dayalist = new ArrayList<>();
    private ArrayList<String> rbmlist = new ArrayList<>();
    private ArrayList<String> gollist = new ArrayList<>();
    private ArrayList<String> totaltaglist = new ArrayList<>();
    private ArrayList<String>fotolist=new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_pln2);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        btPrint = findViewById(R.id.btPrint);
        btupdatetul = findViewById(R.id.btupdatetul);
        btPdf = findViewById(R.id.btPdf);
        ivFototul=findViewById(R.id.ivFototul);
        btSelesai = findViewById(R.id.btSelesai);
        Printooth.INSTANCE.init(this);
        tvTotaljudul = findViewById(R.id.tvTotaljudul);
        tvrefidJudul = findViewById(R.id.tvrefidJudul);
        tvJudulNama = findViewById(R.id.tvJudulNama);
        tvgps = findViewById(R.id.tvgps);
        tvplg = findViewById(R.id.tvplg);
        llPilih = findViewById(R.id.llPilih);
        llketerangan = findViewById(R.id.llketerangan);
        idpasca = findViewById(R.id.idpasca);
        llData = findViewById(R.id.llData);
        llCetak = findViewById(R.id.llCetak);
        llToken = findViewById(R.id.llToken);
        ivPrint = findViewById(R.id.ivPrint);
        adminlb = findViewById(R.id.adminlb);
        llCetak.setVisibility(View.GONE);
        llToken.setVisibility(View.GONE);
        adminlb.setVisibility(View.GONE);
        llketerangan.setVisibility(View.GONE);
        llPilih.setEnabled(true);
        spTipe = findViewById(R.id.spTipe);
        spNominal = findViewById(R.id.spNominal);
        listtipe2.add("-Silahkan Pilih-");
        listtipe2.add("20.000");
        listtipe2.add("50.000");
        listtipe2.add("100.000");
        listtipe2.add("200.000");
        listtipe2.add("500.000");
        listtipe2.add("1.000.000");
        btBayar = findViewById(R.id.btBayar);
        btshare = findViewById(R.id.btshare);
        tvidPelanggan = findViewById(R.id.tvidPelanggan);
        ck3000 = findViewById(R.id.ck3000);
        boyprefs.edit().remove("mDeviceAddress").apply();
        ck3500 = findViewById(R.id.ck3500);
        ck3250 = findViewById(R.id.ck3250);
        ck4000 = findViewById(R.id.ck4000);
        ck4500 = findViewById(R.id.ck4500);
        ck5000 = findViewById(R.id.ck5000);
        tvNama = findViewById(R.id.tvNama);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);
        tvRef = findViewById(R.id.tvRef);
        tvTagPln = findViewById(R.id.tvTagPln);
        tvhrgloket= findViewById(R.id.tvhrgloket);
        tvNamas = findViewById(R.id.tvNamas);
        ivBack = findViewById(R.id.ivBack);
        etidPelanggan = findViewById(R.id.etidPelanggan);
        etketerangan = findViewById(R.id.etketerangan);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        tvLembarTagihan = findViewById(R.id.tvLembarTagihan);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivRefresh = findViewById(R.id.ivRefresh);
        JSONObject categoryJSONObj = null;
        btCheck = findViewById(R.id.btCheck);
        scale = getResources().getDisplayMetrics().density;
        datareport=boyprefs.getString("datareport","");
        Log.e("datareport",datareport);
        listtipe= PojoMion.AmbilArray(datareport,"demo","datareport");
        idpellist= PojoMion.AmbilArray(datareport,"inputs","datareport");
        namalist= PojoMion.AmbilArray(datareport,"nama","datareport");
        alamatlist= PojoMion.AmbilArray(datareport,"alamat","datareport");
        tariflist= PojoMion.AmbilArray(datareport,"tarif","datareport");
        dayalist= PojoMion.AmbilArray(datareport,"daya","datareport");
        totaltaglist= PojoMion.AmbilArray(datareport,"rptag","datareport");
        rbmlist= PojoMion.AmbilArray(datareport,"no_rbm","datareport");
        gollist= PojoMion.AmbilArray(datareport,"gol","datareport");
        fotolist=PojoMion.AmbilArray(datareport,"foto","datareport");

        idpellist.add(0,"-1");
        namalist.add(0,"-1");
        alamatlist.add(0,"-1");
        tariflist.add(0,"-1");
        dayalist.add(0,"-1");
        totaltaglist.add(0,"-1");
        rbmlist.add(0,"-1");
        gollist.add(0,"-1");
        fotolist.add(0,"-1");

        listtipe.add(0,"-Silahkan Pilih-");




        btBayar.setVisibility(View.VISIBLE);
        // tvAdmin.setText("Rp."+format.format(admins));
        datauser = boyprefs.getString("datauser", "");
        nama_pemilik = PojoMion.AmbilArray(datauser, "nama_pemilik", "dataloket");
        nama_loket = PojoMion.AmbilArray(datauser, "nama_loket", "dataloket");
        kode_loket = PojoMion.AmbilArray(datauser, "kode_loket", "dataloket");
        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        kode_ca = PojoMion.AmbilArray(datauser, "kode_ca", "dataloket");
        kode_sub_ca = PojoMion.AmbilArray(datauser, "kode_sub_ca", "dataloket");
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);
        sals = Float.parseFloat(saldo.get(0));
        saldobayar= Float.parseFloat(saldo.get(0));
        saldoawal= Float.parseFloat(saldo.get(0));
        Log.e("saldobayar","s:"+saldobayar);
        tvSaldo.setText("Rp." + decimalFormat.format(sals));
        tvNama.setText(nama_pemilik.get(0));
        String username=boyprefs.getString("username","");
        String password=boyprefs.getString("password","");
        new AmbilSaldoTask(Pln2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));

        if(!foto.isEmpty()){
            Picasso.get().load(foto.replace(" ","%20")).into(ivFototul);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(Pln2.this).execute("login", username, password);
                }else{
                    Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        boyprefs.edit().remove("pln").apply();
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    tvSaldo.setText("");
                    String username=boyprefs.getString("username","");
                    String password=boyprefs.getString("password","");
                    new AmbilSaldoTask(Pln2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setVisibility(View.VISIBLE);
        btBayar.setVisibility(View.VISIBLE);
        btshare.setVisibility(View.GONE);
        ck3000.setChecked(true);
        ck3000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck3500.setChecked(false);
                ck5000.setChecked(false);
                ck3250.setChecked(false);
                ck4000.setChecked(false);
                ck4500.setChecked(false);
                adminss = 3000;

            }
        });
        ck3500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck3000.setChecked(false);
                ck3250.setChecked(false);
                ck5000.setChecked(false);
                ck4000.setChecked(false);
                ck4500.setChecked(false);
                adminss = 3500;
            }
        });
        ck5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck3000.setChecked(false);
                ck3250.setChecked(false);
                ck3500.setChecked(false);
                ck4000.setChecked(false);
                ck4500.setChecked(false);
                adminss = 5000;

            }
        });
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    idpelanggan = etidPelanggan.getText().toString();
                    Log.e("dataproduk",dataproduk);
                    if (idpelanggan.isEmpty()) {
                        Dialog dialog=new Dialog(Pln2.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf=dialog.findViewById(R.id.tvIf);
                        TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                        Button btTidak=dialog.findViewById(R.id.btTidak);
                        tvIf.setText("Masukkan Id Pelanggan");
                        tvTanya.setText("Pesan");
                        btTidak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else {

                        clearall2();

                            if (!ck3000.isChecked() && !ck3500.isChecked() && !ck5000.isChecked()) {
                                Dialog dialog = new Dialog(Pln2.this);
                                dialog.setContentView(R.layout.dialogok);
                                dialog.setCancelable(false);
                                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                TextView tvIf = dialog.findViewById(R.id.tvIf);
                                TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                                Button btTidak = dialog.findViewById(R.id.btTidak);
                                tvIf.setText("Silahkan Pilih Admin");
                                tvTanya.setText("Pesan");
                                btTidak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                            else {
                                btBayar.setEnabled(true);
                                llCetak.setVisibility(View.GONE);
                                llData.setVisibility(View.GONE);
                                llPilih.setVisibility(View.GONE);
                                btshare.setVisibility(View.GONE);
                                adminlb.setEnabled(false);
                                ck3000.setEnabled(false);
                                ck3500.setEnabled(false);
                                ck5000.setEnabled(false);
                                idpasca.setVisibility(View.VISIBLE);
                                new AmbilPlnTasks(Pln2.this).execute(getString(R.string.link) + "apis/android/taglis/inquiry");
                            }


                       }
                }else{
                    Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivFototul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    ket = etketerangan.getText().toString();
                    koordinat = tvgps.getText().toString();
                    tangs=GetDate.getDateTime();
                    final CharSequence[] items = {"Ambil dari Camera","Pilih dari Gallery"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(Pln2.this);
                    builder.setTitle("Ingin Ganti Foto Profile?");
                    builder.setCancelable(true);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @SuppressLint("IntentReset")
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items[item].equals("Ambil dari Camera")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAM_REQUEST1);

                                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                                } else {
                                    // Android version is lesser than 6.0 or the permission is already granted.
                                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                    File fileb = new File(Environment.getExternalStorageDirectory()+ File.separator + tangs+".jpg");
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Pln2.this,
                                            Pln2.this.getApplicationContext().getPackageName() + ".loketbayar.fileprovider", fileb));
                                    intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivityForResult(intent, CAM_REQUEST1);
                                }
                            }
                            else if(items[item].equals("Pilih dari Gallery")){
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(photoPickerIntent, SELECT_FILE1);
                            }

                            else if (items[item].equals("Cancel")) {
                                ivFototul.setAlpha((float) 0.5);
                                dialog.dismiss();
                                ivFototul.setAlpha((float) 1.0);

                            } else {
                                ivFototul.setAlpha((float) 1.0);

                            }
                        }
                    });
                    builder.show();
                    }
                else{
                    Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    // Display in Toast
                    Toast.makeText(Pln2.this,
                            "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
                            Toast.LENGTH_LONG).show();
                    gpslat = String.valueOf(location.getLatitude());
                    gpslong = String.valueOf(location.getLongitude());
                    tvgps.setText("Titik Koordinat : "+ gpslat+", "+gpslong);
                }
            }
        });

        initview();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listtipe);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipe.setAdapter(dataAdapter);
        spTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
                public void onItemSelected (AdapterView  < ? > parent, View view,int position,
                long id){
                    if(adaInternet())   {
                tipe = String.valueOf(parent.getItemAtPosition(position));
                int pos=listtipe.indexOf(tipe);
                llToken.setVisibility(View.GONE);
                llCetak.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
                llPilih.setVisibility(View.GONE);
                btshare.setVisibility(View.GONE);
                btBayar.setVisibility(View.VISIBLE);
                        if (tipe.equalsIgnoreCase("-Silahkan Pilih-"))
                        {
                            llketerangan.setVisibility(View.GONE);
                            etidPelanggan.setText("");
                        }
                         else {
                            foto=fotolist.get(pos);
                            Log.e("foto",foto);
                            if(!foto.isEmpty()){
                                Picasso.get().load(foto.replace(" ","%20")).into(ivFototul);
                            }

                            tvplg.setText(namalist.get(pos)+"\n"+alamatlist.get(pos)+"\n"+tariflist.get(pos)+" / "+dayalist.get(pos)+
                                    " Gol : "+gollist.get(pos)+"\n"+rbmlist.get(pos)+"\n"+"Rp. "+totaltaglist.get(pos));
                            tvgps.setText("Titik Koordinat : "+ gpslat+", "+gpslong);
                            llketerangan.setVisibility(View.VISIBLE);
                            etidPelanggan.setText(idpellist.get(pos));
                            idpelanggan= etidPelanggan.getText().toString();
                            clearall();
                            llToken.setVisibility(View.GONE);
                            adminlb.setVisibility(View.VISIBLE);
                            adminlb.setEnabled(true);
                            ck3000.setEnabled(true);
                            ck3500.setEnabled(true);
                            ck5000.setEnabled(true);
                            tagnya = "TAGPLN";
                            adminlb.setVisibility(View.VISIBLE);
                            ck3000.setChecked(true);
                            token = "";
                            adminss = 3000;
                        }
                tvNamas.setText("");
                tvidPelanggan.setText("");
                tvRef.setText("");
                tvLembarTagihan.setText("");
                tvTagPln.setText("");
                tvhrgloket.setText("");
                tvTotalBayar.setText("");

                    }
                    else
                    {
                        Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                        spTipe.setSelection(0);
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listtipe2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNominal.setAdapter(dataAdapter2);
        spNominal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                token = String.valueOf(parent.getItemAtPosition(position));
                if (token.equalsIgnoreCase("20.000")) {
                    tokenpln2 = 20000;
                           }
                else if (token.equalsIgnoreCase("50.000")) {
                    tokenpln2 = 50000;
                                 }
                else if (token.equalsIgnoreCase("100.000")) {
                    tokenpln2 = 100000;

                }
                else if (token.equalsIgnoreCase("200.000")) {
                    tokenpln2 = 200000;
                                     }
                else if (token.equalsIgnoreCase("500.000")) {
                    tokenpln2 = 500000;
                }
                else if (token.equalsIgnoreCase("1.000.000")) {
                    tokenpln2 = 1000000;
                }
                else
                {
                    spNominal.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adaInternet()) {
     //               Log.e("admin_biller bayar", "adm" + admin_biller);
                    btBayar.setEnabled(false);
                    if (tipe.equalsIgnoreCase("PLN PASCABAYAR")) {
                        new BayarTask(Pln2.this).execute(getString(R.string.link)+"apis/android/taglis/payment");
                    }
                    else if (tipe.equalsIgnoreCase("PLN PRABAYAR/TOKEN")) {
                        new BayarTask2(Pln2.this).execute(getString(R.string.link)+"apis/android/ppob/payment");
                    }
                    else if (tipe.equalsIgnoreCase("PLN NONTAGLIS")) {
                        new BayarTask3(Pln2.this).execute(getString(R.string.link)+"apis/android/ppob/payment");
                    }
                } else {
                    Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(Pln2.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                    TextView tvIf = dialog.findViewById(R.id.tvIf);
                    Button btTidak = dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Printer " + Printooth.INSTANCE.getPairedPrinter().getName() + " Has Connected");
                    tvTanya.setText("Ganti Printer?");
                    btTidak.setText("OK");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Printooth.INSTANCE.removeCurrentPrinter();
                            PairedPrinter.Companion.removePairedPrinter();
                            printing = null;
                            Toast.makeText(Pln2.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(Pln2.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(adaInternet()){
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(Pln2.this).execute("login", username, password);
        }else{
            Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void connectingWithPrinter() {
        Log.e("printer", "connected");
    }
    @Override
    public void connectionFailed(String s) {
        Log.e("printer", s);
    }
    @Override
    public void onError(String s) {
        Log.e("printer", s);
    }
    @Override
    public void onMessage(String s) {
        Log.e("printer", s);
    }
    @Override
    public void printingOrderSentSuccessfully() {
        Log.e("printer", "sukses");
    }
    @Override
    public void disconnected() {

    }

    // **********************************************************************************************************************************
// **********************************************************************************************************************************
    public class AmbilPlnTasks extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password, token;
        private Dialog load, dialogload;
        AmbilPlnTasks(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogload = new Dialog(context);
            dialogload.setContentView(R.layout.dialogload2);
            dialogload.setCancelable(false);
            Objects.requireNonNull(dialogload.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialogload.findViewById(R.id.ivX);
            dialogload.show();
            boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        }
        @Override
        protected String doInBackground(String... params) {
            String linkbayar = params[0];
            String key = "Zb2M#E0T4W$*";
            String key2 = "235fcc7eba35c1a1055832df20654702c14bbb4d774140b805a8554cd32adb04";
            String secret = "asjkrue*$djasfl134213";
            String secret2 = "hvk2yTNzW0uu5K49r4WrwwQ7AuUEgVIz";
            int timestamp = (int) (new Date().getTime()/1000);
            String authToken = key2+ String.valueOf(timestamp)+ secret2;
            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(linkbayar);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("apikey", key2);
                urlConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                urlConnection.setRequestProperty("Authentication", getHash(secret2, authToken));
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("operator", username);
                jsonRequest.put("kode_produk", "TAGPLN");
                jsonRequest.put("idpel", idpelanggan);
                jsonRequest.put("admin", adminss);
                // Write Request to output stream to server.
                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(jsonRequest.toString());
                out.flush();
                out.close();
                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();
                String statusMsg = urlConnection.getResponseMessage();
               // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String returndata = dta.toString();
                    return returndata;
                }
                else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }

                urlConnection.disconnect();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogload.dismiss();
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                llData.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.VISIBLE);
                datapln = s;
                mulai();
            }
            else {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        llPilih.setVisibility(View.GONE);
                        llData.setVisibility(View.GONE);
                        btBayar.setVisibility(View.GONE);
                        datapln = "";
                        boyprefs.edit().putBoolean("Pln2", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }

        }
        void mulai() {
            jsonnya.clear();
            Log.e("datapln", datapln);
            try {
                JSONObject mainJSONObj = new JSONObject(datapln);
                subscriberID = mainJSONObj.getString("subscriberID");
                nama = mainJSONObj.getString("nama");
                tarif = mainJSONObj.getString("tarif");
                daya = mainJSONObj.getString("daya");
                lembarTagihanTotal = mainJSONObj.getString("lembarTagihanTotal");
                lembarTagihan = mainJSONObj.getString("lembarTagihan");
                totalTagihan = mainJSONObj.getString("totalTagihan");
                productCode = mainJSONObj.getString("productCode");
                refID = mainJSONObj.getString("refID");
                hargalkt = mainJSONObj.getString("hargaLoket");
                tagpln =mainJSONObj.getString("totalTagihanPLN");
                arraynya = mainJSONObj.getString("detilTagihan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lemtag = Float.parseFloat(lembarTagihan);
            admins = adminss * lemtag;
            tvidPelanggan.setText(subscriberID);
            tvJudulNama.setText("NAMA");
            tvNamas.setText(nama);
            tvJudullembar.setText("LEMBAR TAGIHAN");
            tvLembarTagihan.setText(lembarTagihanTotal);
            tvrefidJudul.setText("TARIF/DAYA");
            tvRef.setText(tarif + "/" + daya);
            tvJudulTag.setText("RP TAG PLN");
            tvTagPln.setText("Rp." + decimalFormat.format(Float.parseFloat(tagpln)));
            tvTotaljudul.setText("TOTAL BAYAR");
            tvTotalBayar.setText("Rp." + decimalFormat.format(Float.parseFloat(totalTagihan)));
            tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(Float.parseFloat(hargalkt)));
        }
    }
    public class AmbilPlnTasks2 extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password;
        private Dialog load, dialogload;
        AmbilPlnTasks2(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogload = new Dialog(context);
            dialogload.setContentView(R.layout.dialogload2);
            dialogload.setCancelable(false);
            Objects.requireNonNull(dialogload.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialogload.findViewById(R.id.ivX);
            dialogload.show();
            boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        }
        @Override
        protected String doInBackground(String... params) {
            String linkbayar = params[0];
            String key = "Zb2M#E0T4W$*";
            String key2 = "235fcc7eba35c1a1055832df20654702c14bbb4d774140b805a8554cd32adb04";
            String secret = "asjkrue*$djasfl134213";
            String secret2 = "hvk2yTNzW0uu5K49r4WrwwQ7AuUEgVIz";
            int timestamp = (int) (new Date().getTime()/1000);
            String authToken = key2+ String.valueOf(timestamp)+ secret2;
            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(linkbayar);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("apikey", key2);
                urlConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                urlConnection.setRequestProperty("Authentication", getHash(secret2, authToken));
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("operator", username);
                jsonRequest.put("kode_produk", "TOKENPLN");
                jsonRequest.put("idpel", idpelanggan);
                jsonRequest.put("nominal", tokenpln2);
                jsonRequest.put("admin", adminss);
                // Write Request to output stream to server.
                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(jsonRequest.toString());
                out.flush();
                out.close();
                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();
                String statusMsg = urlConnection.getResponseMessage();
                // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String returndata = dta.toString();
                    return returndata;
                }
                else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }
                urlConnection.disconnect();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogload.dismiss();
            Log.e("spln2","pln2:"+s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(responseCode.equalsIgnoreCase("00")) {
                jsonnya.clear();
                taggs=0;
                powerPurchaseDenom.clear();
                llPilih.setVisibility(View.VISIBLE);
                llData.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.VISIBLE);
                datapln = s;
                try {
                    JSONObject mainJSONObj = new JSONObject(datapln);
                    jsonnya.add(mainJSONObj.getString("data"));
                    refID = mainJSONObj.getString("refID");
                    productCode = mainJSONObj.getString("productCode");
                    powerpurchasede = mainJSONObj.getString("powerPurchaseDenom");
                    hargalkt = mainJSONObj.getString("hargaLoket");
                    totalTagihan = mainJSONObj.getString("total");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                admin=0;
                for (int x = 0; x < jsonnya.size(); x++) {
                    try {
                        JSONObject mainJSONObj = new JSONObject(jsonnya.get(x));
                        msn = mainJSONObj.getString("msn");
                        nama = mainJSONObj.getString("nama");
                        subscriberID = mainJSONObj.getString("subscriberID");
                        tarif = mainJSONObj.getString("tarif");
                        daya = mainJSONObj.getString("daya");

                        int adm = Integer.parseInt(mainJSONObj.getString("admin"));
                        admin = admin + adm;
                        Log.e("admindaricjeck","adm:"+admin);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tvidPelanggan.setText(msn);
                tvJudulNama.setText("NO METER");
                tvNamas.setText(msn);
                tvrefidJudul.setText("TARIF/DAYA");
                tvRef.setText(tarif + "/" + daya);
                tvJudullembar.setText("NAMA");
                tvLembarTagihan.setText(nama);
                tvJudulTag.setText("TOTAL BAYAR");
                tvTotaljudul.setText("");
                tvTotalBayar.setText("");
                tvTagPln.setText("Rp." + decimalFormat.format(Float.parseFloat(totalTagihan)));
                tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(Float.parseFloat(hargalkt)));
            }
          else

            {
                datapln = "";
                btBayar.setVisibility(View.GONE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        llPilih.setVisibility(View.GONE);
                        llData.setVisibility(View.GONE);
                        datapln = "";
                        boyprefs.edit().putBoolean("pln", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }




        }
    }
    public class AmbilPlnTasks3 extends AsyncTask<String, String, String> {
       private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password, token;
        private Dialog load, dialogload;
        AmbilPlnTasks3(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogload = new Dialog(context);
            dialogload.setContentView(R.layout.dialogload2);
            dialogload.setCancelable(false);
            Objects.requireNonNull(dialogload.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialogload.findViewById(R.id.ivX);
            dialogload.show();
            boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        }
        @Override
        protected String doInBackground(String... params) {
            String linkbayar = params[0];
            String key = "Zb2M#E0T4W$*";
            String key2 = "235fcc7eba35c1a1055832df20654702c14bbb4d774140b805a8554cd32adb04";
            String secret = "asjkrue*$djasfl134213";
            String secret2 = "hvk2yTNzW0uu5K49r4WrwwQ7AuUEgVIz";

            int timestamp = (int) (new Date().getTime()/1000);
            String authToken = key2+ String.valueOf(timestamp)+ secret2;
            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(linkbayar);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("apikey", key2);
                urlConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                urlConnection.setRequestProperty("Authentication", getHash(secret2, authToken));
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("operator", username);
                jsonRequest.put("kode_produk", "NONTAGPLN");
                jsonRequest.put("idpel", idpelanggan);
                // Write Request to output stream to server.
                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(jsonRequest.toString());
                out.flush();
                out.close();
                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();
                String statusMsg = urlConnection.getResponseMessage();
                // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String returndata = dta.toString();
                    return returndata;
                }
                else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }
                urlConnection.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //jsonnya.clear();
            Log.e("s", "s:" + s);
            dialogload.dismiss();
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                btBayar.setVisibility(View.VISIBLE);
                datapln = s;
                llToken.setVisibility(View.GONE);
                llData.setVisibility(View.VISIBLE);
                llPilih.setVisibility(View.VISIBLE);
                try {
                    JSONObject mainJSONObj = new JSONObject(datapln);
                    jsontask3 = mainJSONObj.getString("data");
                    refID = mainJSONObj.getString("refID");
                    productCode = mainJSONObj.getString("productCode");
                    totalTagihan = mainJSONObj.getString("totalTagihan");
                    hargalkt = mainJSONObj.getString("hargaLoket");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                admin=0;
                nilaiTagihan=0;
                try {
                    JSONObject mainJSONObj = new JSONObject(jsontask3);
                    nama = mainJSONObj.getString("nama");
                    admin = Integer.parseInt(mainJSONObj.getString("admin"));
                    registrationNumber = mainJSONObj.getString("registrationNumber");
                    registrationDate = mainJSONObj.getString("registrationDate");
                    transactionName = mainJSONObj.getString("transactionName");
                    subscriberID = mainJSONObj.getString("registrationNumber");
                    nilaiTagihan = Integer.parseInt(mainJSONObj.getString("nilaiTagihan"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                llPilih.setVisibility(View.GONE);
                llToken.setVisibility(View.GONE);
                tvidPelanggan.setText(subscriberID);
                tvNamas.setText(nama);
                tvJudulNama.setText("NAMA");
                tvrefidJudul.setText("RP TAG PLN");
                tvRef.setText("Rp." + decimalFormat.format(nilaiTagihan));
                tvJudullembar.setText("NAMA TRANSAKSI");
                tvLembarTagihan.setText(transactionName);
                tvTagPln.setText("Rp." + decimalFormat.format(admin));
                tvJudulTag.setText("ADMIN");
                tvTotaljudul.setText("TOTAL BAYAR");
                tvTotalBayar.setText("Rp." + decimalFormat.format(Float.parseFloat(totalTagihan)));
                tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(Float.parseFloat(hargalkt)));
                     }
            else {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        llPilih.setVisibility(View.GONE);
                        llData.setVisibility(View.GONE);
                        llToken.setVisibility(View.GONE);
                        llCetak.setVisibility(View.GONE);
                        boyprefs.edit().putBoolean("pln", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
    }
// **********************************************************************************************************************************
// **********************************************************************************************************************************
    public class BayarTask extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private Dialog load, dialogload;
        BayarTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogload = new Dialog(context);
            dialogload.setContentView(R.layout.dialogload2);
            dialogload.setCancelable(false);
            Objects.requireNonNull(dialogload.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialogload.findViewById(R.id.ivX);
            dialogload.show();
            boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        }
        @Override
        protected String doInBackground(String... params) {
            String linkbayar = params[0];
            String key = "Zb2M#E0T4W$*";
            String key2 = "235fcc7eba35c1a1055832df20654702c14bbb4d774140b805a8554cd32adb04";
            String secret = "asjkrue*$djasfl134213";
            String secret2 = "hvk2yTNzW0uu5K49r4WrwwQ7AuUEgVIz";
            int timestamp = (int) (new Date().getTime()/1000);
            String authToken = key2+ String.valueOf(timestamp)+ secret2;
            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(linkbayar);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("apikey", key2);
                urlConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                urlConnection.setRequestProperty("Authentication", getHash(secret2, authToken));
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("operator", username);
                jsonRequest.put("kode_produk", "TAGPLN");
                jsonRequest.put("refid", refID);
                // Write Request to output stream to server.
                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(jsonRequest.toString());
                out.flush();
                out.close();
                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();
                String statusMsg = urlConnection.getResponseMessage();
                // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String returndata = dta.toString();
                    return returndata;
                }
                else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }
                urlConnection.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogload.dismiss();
            udahceksaldo=false;
            Log.e("hasilbayar", "s:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("responseCode", mainJSONObj.getString("responseCode"));
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(Pln2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                mtrawal_tagihan_untuk_data = new StringBuilder();
                mtrakhir_tagihan_untuk_data = new StringBuilder();
                lembar_tagihan_untuk_data = new StringBuilder();
                try {
                    JSONObject mainJSONObj = new JSONObject(s);
                    Log.e("responseCode", mainJSONObj.getString("responseCode"));
                    responseCode = mainJSONObj.getString("responseCode");
                    message = mainJSONObj.getString("message");
                    subscriberID = mainJSONObj.getString("subscriberID");
                    nama = mainJSONObj.getString("nama");
                    tarif = mainJSONObj.getString("tarif");
                    daya = mainJSONObj.getString("daya");
                    lembarTagihanSisa = mainJSONObj.getString("lembarTagihanSisa");
                    lembarTagihan = mainJSONObj.getString("lembarTagihan");
                    detilTagihan = mainJSONObj.getString("detilTagihan");
                    totalTagihan = mainJSONObj.getString("totalTagihan");
                    refnumber = mainJSONObj.getString("refnumber");
                    infoText = mainJSONObj.getString("infoText");
                    no_reff = mainJSONObj.getString("refnumber");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonnya.clear();
                try {
                    // jsonString is a string variable that holds the JSON
                    JSONArray itemArray = new JSONArray(detilTagihan);
                    for (int i = 0; i < itemArray.length(); i++) {
                        String value = itemArray.getString(i);
                        Log.e("json", i + "=" + value);
                        jsonnya.add(value);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                for (int x = 0; x < jsonnya.size(); x++) {
                    try {
                        JSONObject mainJSONObj = new JSONObject(jsonnya.get(x));
                        Log.e("isinya", mainJSONObj.getString("periode"));
                        mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                        mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                        lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "periode"));
                    }
                    catch(JSONException as){
                        as.printStackTrace();
                    }
                }

                new AmbilSaldoTask(Pln2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                llCetak.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.GONE);
                btshare.setVisibility(View.VISIBLE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText("Transaksi Berhasil");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("pln", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Pln2.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Pln2.this, ScanningActivity.class),
                                            ScanningActivity.SCANNING_FOR_PRINTER);
                                    initview();
                                } else {
                                    initview();
                                    printdata();
                                }
                            }
                        });
                        btPdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // String root = Environment.getExternalStorageDirectory().toString();
                                String dest = context.getExternalFilesDir(null) + "/RTS";
                                File myDir = new File(dest + "/PDF");
                                if (!myDir.exists())
                                    myDir.mkdirs();
                                File h = new File(dest
                                        + "/PDF/" + subscriberID + ".pdf");
                                Document document = new Document(PageSize.A4);
                                Uri path = FileProvider.getUriForFile(
                                        Pln2.this,
                                        Pln2.this.getApplicationContext().getPackageName() + ".loketbayar.fileprovider", //(use your app signature + ".provider" )
                                        h);
// Create Pdf Writer for Writting into New Created Document
                                try {
                                    writer = PdfWriter.getInstance(document, new FileOutputStream(h));

                                } catch (DocumentException | FileNotFoundException e) {
                                    e.printStackTrace();
                                }
// Open Document for Writting into document
                                document.open();
// User Define Method
                                addMetaData(document);
                               try {
                                    addTitlePage(document);
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }
// Close Document after writting all content
                                document.close();
                                // send email
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("plain/text");
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Listrik");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Pembayaran Tagihan Listrik "+
                                        "a.n "+nama);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                        btshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                mTextView.getText().toString();
                                String s = "STRUK PEMBAYARAN TAGIHAN LISTRIK\n\n"+
                                        "ID PELANGGAN  : " + subscriberID+"\n"+
                                        "NAMA   : " + nama+"\n"+
                                        "TARIF/DAYA     : " + tarif + "/" + daya+"\n"+
                                        "BL/TH                : " + lembar_tagihan_untuk_data +"\n"+
                                        "STAND METER : " + mtrawal_tagihan_untuk_data + " -" + mtrakhir_tagihan_untuk_data+"\n\n"+
                                        "RP TAG PLN     : " + "Rp." +decimalFormat.format(Float.parseFloat(tagpln))+"\n"+
                                        "NO REF             : " + no_reff+"\n\n"+
                                        "PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah"+"\n\n"+
                                        "ADMIN BANK    : " + "Rp." + decimalFormat.format(admins)+"\n"+
                                        "TOTAL BAYAR   : " + "Rp." + decimalFormat.format(Float.parseFloat(totalTagihan))+"\n\n"+
                                        infoText+"\n"+"Terima Kasih"+"\n\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Tagihan Listrik "+
                                        "a.n "+nama);
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
                                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                            }
                        });
                    }
                });
                dialog.show();
            }
            else {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        boyprefs.edit().putBoolean("pln", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
    }
    public class BayarTask2 extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private Dialog load, dialogload;
        BayarTask2(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogload = new Dialog(context);
            dialogload.setContentView(R.layout.dialogload2);
            dialogload.setCancelable(false);
            Objects.requireNonNull(dialogload.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialogload.findViewById(R.id.ivX);
            dialogload.show();
            boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        }
        @Override
        protected String doInBackground(String... params) {
            String linkbayar = params[0];
            String key = "Zb2M#E0T4W$*";
            String key2 = "235fcc7eba35c1a1055832df20654702c14bbb4d774140b805a8554cd32adb04";
            String secret = "asjkrue*$djasfl134213";
            String secret2 = "hvk2yTNzW0uu5K49r4WrwwQ7AuUEgVIz";
            int timestamp = (int) (new Date().getTime()/1000);
            String authToken = key2+ String.valueOf(timestamp)+ secret2;
            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(linkbayar);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("apikey", key2);
                urlConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                urlConnection.setRequestProperty("Authentication", getHash(secret2, authToken));
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("operator", username);
                jsonRequest.put("kode_produk", "TOKENPLN");
                jsonRequest.put("refid", refID);
                // Write Request to output stream to server.
                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(jsonRequest.toString());
                out.flush();
                out.close();
                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();
                String statusMsg = urlConnection.getResponseMessage();
                // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String returndata = dta.toString();
                    return returndata;
                }
                else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }
                urlConnection.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogload.dismiss();
            udahceksaldo=false;
            Log.e("hasilbayar", "s:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("responseCode", mainJSONObj.getString("responseCode"));
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(Pln2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                try {
                    JSONObject mainJSONObj = new JSONObject(s);
                    Log.e("responseCode", mainJSONObj.getString("responseCode"));
                    responseCode = mainJSONObj.getString("responseCode");
                    message = mainJSONObj.getString("message");
                    detilTagihan = mainJSONObj.getString("data");
                    totalTagihan = mainJSONObj.getString("totalTagihan");
                    infoText = mainJSONObj.getString("infotext");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject mainJSONObj = new JSONObject(detilTagihan);
                    msn = mainJSONObj.getString("msn");
                    nama = mainJSONObj.getString("nama");
                    tarif = mainJSONObj.getString("tarif");
                    daya = mainJSONObj.getString("daya");
                    subscriberID = mainJSONObj.getString("subscriberID");
                    refnumber = mainJSONObj.getString("ref");
                    tokenNumber = mainJSONObj.getString("tokenNumber");
                    hasiltoken=new StringBuilder();
                    for(int y=0;y<tokenNumber.length();y++){
                        if(y>=4){
                            if(y%4 ==0){
                                hasiltoken.append(" ").append(tokenNumber.charAt(y));
                            }else{
                                hasiltoken.append(tokenNumber.charAt(y));
                            }
                        }else{
                            hasiltoken.append(tokenNumber.charAt(y));
                        }
                    }
                    tokenNumber=hasiltoken.toString();
                    Log.e("hasiltoken",hasiltoken.toString());
                    biayaMeterai = mainJSONObj.getString("biayaMeterai");
                    ppn = mainJSONObj.getString("ppn");
                    ppj = mainJSONObj.getString("ppj");
                    no_reff = mainJSONObj.getString("ref");
                    angsuran = mainJSONObj.getString("angsuran");
                    rpToken = mainJSONObj.getString("rpToken");
                    kwh = mainJSONObj.getString("kwh");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                idpasca.setVisibility(View.VISIBLE);
                tvTotalBayar.setVisibility(View.VISIBLE);
                tvTotaljudul.setVisibility(View.VISIBLE);
                tvTotaljudul.setText("TOKEN");
                tvTotalBayar.setText(tokenNumber);
                llCetak.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.GONE);
                btshare.setVisibility(View.VISIBLE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText("Transaksi Berhasil");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("pln", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Pln2.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Pln2.this, ScanningActivity.class),
                                            ScanningActivity.SCANNING_FOR_PRINTER);
                                    initview();
                                } else {
                                    initview();
                                    printdata2();
                                }
                            }
                        });
                        btPdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // String root = Environment.getExternalStorageDirectory().toString();
                                String dest = context.getExternalFilesDir(null) + "/RTS";
                                File myDir = new File(dest + "/PDF");
                                if (!myDir.exists())
                                    myDir.mkdirs();
                                File h = new File(dest
                                        + "/PDF/" + subscriberID + ".pdf");
                                Document document = new Document(PageSize.A5);
                                Uri path = FileProvider.getUriForFile(
                                        Pln2.this,
                                        Pln2.this.getApplicationContext().getPackageName() + ".loketbayar.fileprovider", //(use your app signature + ".provider" )
                                        h);
// Create Pdf Writer for Writting into New Created Document
                                try {
                                    writer = PdfWriter.getInstance(document, new FileOutputStream(h));
                                } catch (DocumentException | FileNotFoundException e) {
                                    e.printStackTrace();
                                }
// Open Document for Writting into document
                                document.open();
// User Define Method
                                addMetaData(document);
                                try {
                                    addTitlePage2(document);
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }
// Close Document after writting all content
                                document.close();
                                // send email
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembelian TOKEN Listrik");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Pembelian TOKEN Listrik "+
                                        "a.n "+nama);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                        btshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                mTextView.getText().toString();
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                String formattedDate = df.format(c);
                                String s = "STRUK PEMBELIAN TOKEN LISTRIK PRABAYAR\n\n"+
                                        "TGL BAYAR          : " + formattedDate + "\n" +
                                        "NOMOR METER  : " + msn+"\n"+
                                        "ID PELANGGAN  : " + subscriberID+"\n"+
                                        "NAMA            : " + nama+"\n"+
                                        "TARIF/DAYA : " + tarif + "/" + daya+"\n\n"+
                                        "RP TAG PLN : " + "Rp." + decimalFormat.format((tokenpln2))+"\n"+
                                        "JUMLAH KWH : " + kwh+"\n"+
                                        "NO REF         : " + no_reff+"\n\n"+
                               //         "MATERAI          : " + "Rp." + biayaMeterai+"\n"+
                               //         "PPN                   : Rp." + ppn+"\n"+
                               //         "PPJ                    : Rp." + ppj+"\n"+
                               //         "ANGSURAN      : Rp." + angsuran+"\n"+
                               //         "RP STROOM     : Rp." + rpToken+"\n"+
                                        "TOKEN               :        "+"\n"  + tokenNumber+"\n\n"+
                                        "PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah"+"\n\n"+
                                        "ADMIN BANK    : " + "Rp." + decimalFormat.format(adminss)+"\n"+
                                        "TOTAL BAYAR   : " + "Rp." + decimalFormat.format(Float.parseFloat(totalTagihan))+"\n\n"+

                                        infoText+"\n"+"Terima Kasih"+"\n\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembelian TOKEN Listrik "+
                                        "a.n "+nama);
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
                                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                            }
                        });
                    }
                });
                dialog.show();
            } else {
                Log.e("productCode",productCode);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        boyprefs.edit().putBoolean("pln", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
    }
    public class BayarTask3 extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private Dialog load, dialogload;
        BayarTask3(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogload = new Dialog(context);
            dialogload.setContentView(R.layout.dialogload2);
            dialogload.setCancelable(false);
            Objects.requireNonNull(dialogload.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialogload.findViewById(R.id.ivX);
            dialogload.show();
            boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        }
        @Override
        protected String doInBackground(String... params) {
            String linkbayar = params[0];
            String key = "Zb2M#E0T4W$*";
            String key2 = "235fcc7eba35c1a1055832df20654702c14bbb4d774140b805a8554cd32adb04";
            String secret = "asjkrue*$djasfl134213";
            String secret2 = "hvk2yTNzW0uu5K49r4WrwwQ7AuUEgVIz";
            int timestamp = (int) (new Date().getTime()/1000);
            String authToken = key2+ String.valueOf(timestamp)+ secret2;
            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(linkbayar);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("apikey", key2);
                urlConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                urlConnection.setRequestProperty("Authentication", getHash(secret2, authToken));
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("operator", username);
                jsonRequest.put("kode_produk", "NONTAGPLN");
                jsonRequest.put("refid", refID);
                // Write Request to output stream to server.
                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(jsonRequest.toString());
                out.flush();
                out.close();
                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();
                String statusMsg = urlConnection.getResponseMessage();
                // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String returndata = dta.toString();
                    return returndata;
                }
                else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }
                urlConnection.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogload.dismiss();
            udahceksaldo=false;
            Log.e("hasilbayar", "s:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("responseCode", mainJSONObj.getString("responseCode"));
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(Pln2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                try {
                    JSONObject mainJSONObj = new JSONObject(s);
                    Log.e("responseCode", mainJSONObj.getString("responseCode"));
                    responseCode = mainJSONObj.getString("responseCode");
                    message = mainJSONObj.getString("message");
                    infoText = mainJSONObj.getString("infotext");
                    detilTagihan = mainJSONObj.getString("data");
                    totalTagihan = mainJSONObj.getString("totalTagihan");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                admin=0;
                nilaiTagihan=0;
                total=0;
                try {
                    JSONObject mainJSONObj = new JSONObject(detilTagihan);
                    registrationNumber = mainJSONObj.getString("registrationNumber");
                    transactionName = mainJSONObj.getString("transactionName");
                    registrationDate = mainJSONObj.getString("registrationDate");
                    nama = mainJSONObj.getString("nama");
                    subscriberID = mainJSONObj.getString("registrationNumber");
                    nilaiTagihan = Integer.parseInt(mainJSONObj.getString("nilaiTagihan"));
                    no_reff = mainJSONObj.getString("ref");
                    admin = Integer.parseInt(mainJSONObj.getString("admin"));
                    total = mainJSONObj.getInt("total");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                llCetak.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.GONE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText("Transaksi Berhasil");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("pln", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Pln2.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Pln2.this, ScanningActivity.class),
                                            ScanningActivity.SCANNING_FOR_PRINTER);
                                    initview();
                                } else {
                                    initview();
                                    printdata3();
                                }
                            }
                        });
                        btPdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // String root = Environment.getExternalStorageDirectory().toString();
                                String dest = context.getExternalFilesDir(null) + "/RTS";
                                File myDir = new File(dest + "/PDF");
                                if (!myDir.exists())
                                    myDir.mkdirs();
                                File h = new File(dest
                                        + "/PDF/" + subscriberID + ".pdf");
                                Document document = new Document(PageSize.A4);
                                Uri path = FileProvider.getUriForFile(
                                        Pln2.this,
                                        Pln2.this.getApplicationContext().getPackageName() + ".loketbayar.fileprovider", //(use your app signature + ".provider" )
                                        h);
// Create Pdf Writer for Writting into New Created Document
                                try {
                                    writer = PdfWriter.getInstance(document, new FileOutputStream(h));
                                } catch (DocumentException | FileNotFoundException e) {
                                    e.printStackTrace();
                                }
// Open Document for Writting into document
                                document.open();
// User Define Method
                                addMetaData(document);
                                try {
                                    addTitlePage3(document);
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }
// Close Document after writting all content
                                document.close();
                                // send email
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Non Tagihan Listrik");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Pembayaran Non Tagihan Listrik "+
                                        "a.n "+nama);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                        btshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String s = "STRUK PEMBAYARAN\nNON TAGIHAN LISTRIK\n\n"+
                                        "ID PELANGGAN  : " + subscriberID+"\n"+
                                        "NAMA : " + nama+"\n"+
                                        "TRANSAKSI : " + transactionName+"\n\n"+
                        //                "NO REGISTRASI   : " + registrationNumber +"\n\n"+
                        //                "TGL REGISTRASI : " + registrationDate+"\n\n"+
                                        "RP TAG PLN         : " + "Rp." + decimalFormat.format(nilaiTagihan)+"\n"+
                                        "NO REF                  : " + no_reff+"\n\n"+
                                        "PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah"+"\n\n"+
                                        "ADMIN BANK      : " + "Rp." + decimalFormat.format(admin)+"\n"+
                                        "TOTAL BAYAR     : " + "Rp." + decimalFormat.format(Float.parseFloat(totalTagihan))+"\n\n"+
                                        infoText+"\n"+"Terima Kasih"+"\n\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Non Tagihan Listrik "+
                                        "a.n "+nama);
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
                                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                            }
                        });
                    }
                });
                dialog.show();
            } else {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        boyprefs.edit().putBoolean("pln", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
    }
// **********************************************************************************************************************************
// **********************************************************************************************************************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();

         if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_OK) {
                Log.e("masuk", "ambilfoto");
                File fileb = new File(Environment.getExternalStorageDirectory() + File.separator + tangs + ".jpg");
                ExifInterface exif = null;
                realimage1 = null;
             Toast.makeText(Pln2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                ByteArrayOutputStream byteArrayOutputStream;
                byte[] byteArray;
                bitmap1 = BitmapFactory.decodeFile(fileb.getAbsolutePath());
                int w = bitmap1.getWidth() / 4;
                int h = bitmap1.getHeight() / 4;
                bitmap1 = decodeSampledBitmapFromFile(fileb.getAbsolutePath(), w, h);
                try {
                    exif = new ExifInterface(fileb.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert exif != null;
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                // Toast.makeText(Register.this,String.valueOf(orientation),Toast.LENGTH_LONG).show();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_UNDEFINED:
                        //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);

                        realimage1 = rotate(bitmap1, 0);
                        ivFototul.setImageBitmap(realimage1);
                        ivFototul.setAlpha((float) 1.0);
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        ViewGroup.LayoutParams layoutParams = ivFototul.getLayoutParams();
                        layoutParams.width = (int) (70 * scale);
                        layoutParams.height = (int) (70 * scale);
                        ivFototul.setLayoutParams(layoutParams);
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                        //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                        realimage1 = rotate(bitmap1, 0);
                        ivFototul.setImageBitmap(realimage1);
                        ivFototul.setAlpha((float) 1.0);
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        layoutParams = ivFototul.getLayoutParams();
                        layoutParams.width = (int) (70 * scale);
                        layoutParams.height = (int) (70 * scale);
                        ivFototul.setLayoutParams(layoutParams);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                        realimage1 = rotate(bitmap1, 180);
                        ivFototul.setImageBitmap(realimage1);
                        ivFototul.setAlpha((float) 1.0);
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        layoutParams = ivFototul.getLayoutParams();
                        layoutParams.width = (int) (70 * scale);
                        layoutParams.height = (int) (70 * scale);
                        ivFototul.setLayoutParams(layoutParams);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                        realimage1 = rotate(bitmap1, 90);
                        ivFototul.setImageBitmap(realimage1);
                        ivFototul.setAlpha((float) 1.0);
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        layoutParams = ivFototul.getLayoutParams();
                        layoutParams.width = (int) (70 * scale);
                        layoutParams.height = (int) (70 * scale);
                        ivFototul.setLayoutParams(layoutParams);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                        realimage1 = rotate(bitmap1, 270);
                        ivFototul.setImageBitmap(realimage1);
                        ivFototul.setAlpha((float) 1.0);
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        layoutParams = ivFototul.getLayoutParams();
                        layoutParams.width = (int) (70 * scale);
                        layoutParams.height = (int) (70 * scale);
                        ivFototul.setLayoutParams(layoutParams);
                        break;
                }
                load = new Dialog(Pln2.this);
                load.setContentView(R.layout.dialogload2);
                load.setCancelable(false);
                ivX = load.findViewById(R.id.ivX);
                Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                load.show();

                SaveFoto(getResources().getString(R.string.xyz) + "simpanfototul", foto, username, password,idpelanggan,ket,koordinat);
            }
         else if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_CANCELED) {
                ivFototul.setAlpha((float) 1.0);
                foto = "";
                //    ivFoto.setImageResource(R.drawable.tblfoto);
                //       ViewGroup.LayoutParams layoutParams = ivFoto.getLayoutParams();
                //        layoutParams = ivFoto.getLayoutParams();
                //        layoutParams.width = (int) (80 * scale);
                //        layoutParams.height = (int) (80 * scale);
                //         ivFoto.setLayoutParams(layoutParams);
            }
         else if (requestCode == SELECT_FILE1 && resultCode == Activity.RESULT_OK) {
                Log.e("masuk", "galery");
                bitmap1 = null;
                if (data != null) {
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                assert bitmap1 != null;
                int w = bitmap1.getWidth() / 4;
                int h = bitmap1.getHeight() / 4;
                Matrix m = new Matrix();
                m.setRectToRect(new RectF(0, 0, bitmap1.getWidth(), bitmap1.getHeight()), new RectF(0, 0, w, h), Matrix.ScaleToFit.CENTER);
                realimage1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), m, true);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                ivFototul.setImageBitmap(realimage1);
                ViewGroup.LayoutParams layoutParams = ivFototul.getLayoutParams();
                layoutParams = ivFototul.getLayoutParams();
                layoutParams.width = (int) (70 * scale);
                layoutParams.height = (int) (70 * scale);
                ivFototul.setLayoutParams(layoutParams);
                ivFototul.setAlpha((float) 1.0);
                load = new Dialog(Pln2.this);
                load.setContentView(R.layout.dialogload2);
                load.setCancelable(false);
                ivX = load.findViewById(R.id.ivX);
                Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                load.show();
                SaveFoto(getResources().getString(R.string.xyz) + "simpanfototul", foto, username, password,idpelanggan,ket,koordinat);
            }
         else if (requestCode == SELECT_FILE1 && resultCode == Activity.RESULT_CANCELED) {
                ivFototul.setAlpha((float) 1.0);
                //          ViewGroup.LayoutParams layoutParams = ivFoto.getLayoutParams();
                //          layoutParams = ivFoto.getLayoutParams();
                //          layoutParams.width = (int) (80 * scale);
                //          layoutParams.height = (int) (80 * scale);
                //          ivFoto.setLayoutParams(layoutParams);
                //      ivFoto.setImageResource(R.drawable.tblfoto);
                foto = "";
            }



    }
    public void addMetaData(Document document) {
        document.addTitle("Struk MB");
        document.addSubject("Mudah Bayar");
        document.addKeywords("Mudah Bayar");
    }
    void initview() {
        if (printing != null) {
            printing.setPrintingCallback(this);
        }
    }
// **********************************************************************************************************************************
// **********************************************************************************************************************************
    private void printdata() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("STRUK PEMBAYARAN TAGIHAN LISTRIK")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + subscriberID)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA         : " + nama)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TARIF/DAYA   : " + tarif + "/" + daya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("BL/TH        :" + lembar_tagihan_untuk_data)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("STAND METER  :" + mtrawal_tagihan_untuk_data + " -" + mtrakhir_tagihan_untuk_data +"\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("RP TAG PLN   : " + "Rp." + decimalFormat.format(Float.parseFloat(tagpln)))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REF       : " + no_reff)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK   : " + "Rp." + decimalFormat.format(admins))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR  : " + "Rp." + decimalFormat.format(Float.parseFloat(totalTagihan)))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        int lemsisa= Integer.parseInt(lembarTagihanSisa);
                if (lemsisa>1) {
                    printables.add(new TextPrintable.Builder()
                            .setText("Lembar Tagihan Sisa : " +  lembarTagihanSisa)
                            .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                }
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Terima Kasih")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Informasi Hubungi Call Center\n123 Atau Hub PLN Terdekat")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        Printooth.INSTANCE.printer(new PairedPrinter(Printooth.INSTANCE.getPairedPrinter().getName(),
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);
    }
    public void addTitlePage(Document document) throws DocumentException {
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN TAGIHAN LISTRIK\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(140);
        tgl.setIndentationRight(140);
        tgl.add("ID PELANGGAN");
        tgl.add("  : " + subscriberID + "\n");
        tgl.add("NAMA");
        tgl.add("                   : " + nama + "\n");
        tgl.add("TARIF/DAYA");
        tgl.add("        : " + tarif + "/" + daya + "\n");
        document.add(tgl);
        Paragraph tgll = new Paragraph();
        tgll.setAlignment(Element.ALIGN_LEFT);
        tgll.setIndentationLeft(140);
        tgll.setIndentationRight(140);
        tgll.add("BL/TH");
        tgll.add("                   : " + lembar_tagihan_untuk_data + "\n");
        tgll.add("STAND METER");
        tgll.add("   : " + mtrawal_tagihan_untuk_data + " -" + mtrakhir_tagihan_untuk_data + "\n\n");
        tgll.add("RP TAG PLN");
        tgll.add("        : Rp." + decimalFormat.format(Float.parseFloat(tagpln)) + "\n");
        tgll.add("NO REFF             :");
        tgll.add(no_reff + "\n");
        tgll.add("\n");
        document.add(tgll);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(140);
        keterangan.setIndentationRight(140);
        keterangan.add("PLN Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph admin = new Paragraph();
        admin.setAlignment(Element.ALIGN_LEFT);
        admin.setIndentationLeft(140);
        admin.setIndentationRight(140);
        admin.add("ADMIN BANK  : Rp." + decimalFormat.format(admins) + "\n");
        admin.add("TOTAL BAYAR   : Rp." + decimalFormat.format(Float.parseFloat(totalTagihan)) + "\n");
        admin.add("\n");
        document.add(admin);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(140);
        infotext.setIndentationRight(140);
        int lemsisa= Integer.parseInt(lembarTagihanSisa);
        if (lemsisa>1) {
            infotext.add("Lembar Tagihan Sisa : " +  lembarTagihanSisa+ "\n\n");
        }
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add("Informasi Hubungi Call Center\n123 Atau Hub PLN Terdekat" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0) + " / "+ username+ "\n");
        document.add(infotext);
        document.left(90);
        document.right(90);
    }
// **********************************************************************************************************************************
// **********************************************************************************************************************************
    private void printdata2() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBELIAN TOKEN\nLISTRIK PRABAYAR")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String formattedDate = df.format(c);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + formattedDate)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NOMOR METER  : " + msn)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + subscriberID)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA         : " + nama)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TARIF/DAYA   : " + tarif + "/" + daya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("RP TAG PLN   : Rp." + decimalFormat.format(tokenpln2))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("JUMLAH KWH   : " + kwh)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REFF      : " + no_reff)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("MATERAI      : " + "Rp." + biayaMeterai)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("PPN          : Rp." + ppn)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("PPJ          : Rp." + ppj)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("ANGSURAN     : Rp." + angsuran)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("RP STROOM    : Rp." + rpToken)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOKEN        : "+"\n"  + tokenNumber)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK   : Rp." + decimalFormat.format(adminss))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR  : Rp." + decimalFormat.format(Float.parseFloat(totalTagihan)))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Terima Kasih")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(infoText)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        Printooth.INSTANCE.printer(new PairedPrinter(Printooth.INSTANCE.getPairedPrinter().getName(),
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);
    }
    public void addTitlePage2(Document document) throws DocumentException {
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBELIAN TOKEN LISTRIK\nPRABAYAR\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(57);
        tgl.setIndentationRight(57);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String formattedDate = df.format(c);
        tgl.add("TGL BAYAR         : " + formattedDate + "\n");
        tgl.add("NO METER");
        tgl.add("          : " + msn + "\n");
        tgl.add("ID PELANGGAN");
        tgl.add("  : " + subscriberID + "\n");
        tgl.add("NAMA");
        tgl.add("                   : " + nama + "\n");
        tgl.add("TARIF/DAYA");
        tgl.add("        : " + tarif + "/" + daya + "\n");
        tgl.add("RP TAG PLN");
        tgl.add("        : Rp." + decimalFormat.format(tkn) + "\n");
        tgl.add("No REFF              :");
        tgl.add("" + no_reff + "\n");
        tgl.add("MATERAI");
        tgl.add("             : Rp." + biayaMeterai + "\n");
        tgl.add("PPN");
        tgl.add("                      : Rp." + ppn + "\n");
        tgl.add("PPJ");
        tgl.add("                       : Rp." + ppj + "\n");
        tgl.add("ANGSURAN");
        tgl.add("         : Rp." + angsuran + "\n");
        tgl.add("RP STROOM");
        tgl.add("        : Rp." + rpToken + "\n");
        tgl.add("JUMLAH KWH");
        tgl.add("      : " + kwh + "\n");
        tgl.add("TOKEN                 : "+ "\n");
        tgl.add("" + tokenNumber + "\n\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("PLN Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph tgll = new Paragraph();
        tgll.setAlignment(Element.ALIGN_LEFT);
        tgll.setIndentationLeft(57);
        tgll.setIndentationRight(57);
        float ads = admin;
        tgll.add("ADMIN BANK");
        tgll.add("       : Rp." + decimalFormat.format(admawal) + "\n");
        tgll.add("TOTAL BAYAR");
        tgll.add("     : Rp." + decimalFormat.format(totalbayarr) + "\n");
        tgll.add("\n");
        document.add(tgll);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(infoText + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username);
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
// *************************************************************************************************************************
// *************************************************************************************************************************
    private void printdata3() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN\nNON TAGIHAN LISTRIK")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + subscriberID)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA      : " + nama)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TRANSAKSI : " + transactionName)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
                printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("NO REGISTRASI : " + registrationNumber)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("TGL REGISTRASI: " + registrationDate)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
        printables.add(new TextPrintable.Builder()
                .setText("RP TAG PLN    : " + "Rp." + decimalFormat.format(nilaiTagihan))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REF        : " + no_reff)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK    : " + "Rp." + decimalFormat.format(admin))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR   : " + "Rp." + decimalFormat.format(Float.parseFloat(totalTagihan)))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Terima Kasih")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Informasi Hubungi Call Center\n123 Atau Hub PLN Terdekat")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        Printooth.INSTANCE.printer(new PairedPrinter(Printooth.INSTANCE.getPairedPrinter().getName(),
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);    }
    public void addTitlePage3(Document document) throws DocumentException {
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN\nNON TAGIHAN LISTRIK\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(140);
        tgl.setIndentationRight(140);
        tgl.add("ID PELANGGAN");
        tgl.add("   : " + subscriberID + "\n");
        tgl.add("NAMA");
        tgl.add("                    : " + nama + "\n");
        tgl.add("TRANSAKSI");
        tgl.add("          : " + transactionName + "\n");
        tgl.add("NO REGISTRASI");
        tgl.add("   : " + registrationNumber + "\n");
        tgl.add("TGL REGISTRASI");
        tgl.add(" : " + registrationDate + "\n");
        float nt = nilaiTagihan;
        tgl.add("RP TAG PLN");
        tgl.add("         : Rp." + decimalFormat.format(nilaiTagihan) + "\n");
        tgl.add("NO REFF               :");
        tgl.add("" + no_reff + "\n");
        tgl.add("\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(140);
        keterangan.setIndentationRight(140);
        keterangan.add("PLN Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph admins = new Paragraph();
        admins.setAlignment(Element.ALIGN_LEFT);
        admins.setIndentationLeft(140);
        admins.setIndentationRight(140);
        admins.add("ADMIN BANK        : Rp." + decimalFormat.format(admin) + "\n");
        float totas = Float.parseFloat(totalTagihan);
        admins.add("TOTAL BAYAR      : Rp." + decimalFormat.format(totas) + "\n");
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(140);
        infotext.setIndentationRight(140);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add("Informasi Hubungi Call Center\n123 Atau Hub PLN Terdekat" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0) + " / "+ username+ "\n");
        document.add(infotext);
        document.left(90);
        document.right(90);
    }
//*********************************************************************************************************************
//*********************************************************************************************************************
    void adminoff(){
        ck3500.setClickable(false);
        ck3000.setClickable(false);
        ck5000.setClickable(false);
        ck4500.setClickable(false);
        ck4000.setClickable(false);
        ck3250.setClickable(false);
        llToken.setClickable(false);
    }
    void clearall(){
        sals=0; taggs = 0;
       token = "";
        responseCode="";message="";subscriberID="";nama="";tarif="";daya="";lembarTagihanSisa="";lembarTagihan="";
        totalTagihan="";refnumber="";infoText="";detilTagihan="";
        totalbayar = 0;tagplns = 0;fee_billers = 0;adminbiller = 0;saldobayar = 0;pengurangan = 0;lemtag = 0;
        harga_loket = 0;
        msn = "";powerpurchasede = "";jsontask3 = "";no_reff = "";
        tagplns=0;
        meterAwal .clear();
        meterAkhir .clear();
        periodes .clear();
        nilaiTagihans .clear();
        dendas .clear();
        adminarray .clear();
        totals .clear();
        fee .clear();
        arraynya = "";
        lembarTagihanTotal="";
        periode="";
        registrationNumber ="";
        transactionName="";
        nilaiTagihan = 0;denda = 0;admin = 0;total = 0;
        ck3500.setChecked(false);
        ck3000.setChecked(false);
        ck5000.setChecked(false);
        ck3250.setChecked(false);
        ck4000.setChecked(false);
        ck4500.setChecked(false);
        saldobayar= Float.parseFloat(saldo.get(0));
    }
    void clearall2(){
        sals=0;
        admawal=3000;
 //       adminss= 3000;
        subscriberID="";nama="";tarif="";daya="";lembarTagihanSisa="";lembarTagihan="";
        totalTagihan="";refnumber="";infoText="";detilTagihan="";msn = "";powerpurchasede = "";tkn=0;
        totalbayar = 0;tagplns = 0;fee_billers = 0;adminbiller = 0;saldobayar = 0;pengurangan = 0;lemtag = 0;
        harga_loket = 0;
        msn = "";powerpurchasede = "";jsontask3 = "";no_reff = "";denda = 0;admin = 0;total = 0;
        tagplns=0;
        meterAwal .clear();
        meterAkhir .clear();
        periodes .clear();
        nilaiTagihans .clear();
        dendas .clear();
        adminarray .clear();
        totals .clear();
        fee .clear();
        arraynya = "";
        lembarTagihanTotal="";
        periode="";
        registrationNumber ="";
        transactionName="";
        nilaiTagihan = 0;denda = 0;total = 0;
   //     ck3500.setChecked(false);
   //     ck3000.setChecked(false);
   //     ck5000.setChecked(false);
   //     ck3250.setChecked(false);
   //     ck4000.setChecked(false);
   //     ck4500.setChecked(false);
        saldobayar= Float.parseFloat(saldo.get(0));
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
    private static String getHash(final String key, final String data) {
        String result = "";
        try {
            SecretKeySpec hashHmac = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(hashHmac);
            result = Base64.encodeToString(mac.doFinal(data.getBytes("UTF-8")), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
    void SaveFoto(String POST_ORDER,String fotonya,String usernames,String passwords,String idpelanggan,String ket,String koordinat) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if(ServerResponse.trim().equalsIgnoreCase("sukses")){
                        load.dismiss();
                        Toast.makeText(Pln2.this, "Berhasil Ganti Foto", Toast.LENGTH_SHORT).show();
                    }else{
                        load.dismiss();
                        Log.e("errorsavefoto",ServerResponse);
                        Toast.makeText(Pln2.this, "Gagal Silahkan Diulang", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 50);

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        load.dismiss();
                        Log.e("vooley save foto",volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("foto", fotonya);
                params.put("username", usernames);
                params.put("password", passwords);
                params.put("idpelanggan", idpelanggan);
                params.put("ket", ket);
                params.put("koordinat", koordinat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Pln2.this);
        requestQueue.add(stringRequest);
    }

}