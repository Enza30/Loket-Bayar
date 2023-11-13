package loketbayar.id;

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
import android.provider.ContactsContract;
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

public class Home extends AppCompatActivity {
    ImageView ivPln;
    int PERMISSION_ALL = 1;
    private String username, password,kodeloket,kodeca,kodesubca,kodesubca1,kodesubca2,kodesubca3,text_ref;
    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbars;
    private String datauser,foto="", tangs;
    private ImageView ivX;
    private Dialog load;
    private int CAM_REQUEST1=1,SELECT_FILE1=2;
    private Bitmap bitmap1,realimage1;
    private ImageView ivRefresh,ivBpjs,ivPaska,ivGopay,ivDana,ivOvo,ivToll,ivLink,ivPrabayar,ivLaporan,ivHome,ivcsa,ivProfile,ivinfo,
            ivTelkom,ivPdam,ivKolektif,btPrint,ivka,ivpswt,ivAngs,ivmulty,ivFoto,ivtrf,ivkasir,ivShop,ivSamsat,ivtravel,ivPbb,ivtvkabel;
    private LinearLayout ll2,ll3,ll4,ll5,pratul,tul,plntul;
    private SharedPreferences boyprefs;
    private ArrayList<String>nama_pemilik=new ArrayList<>();
    private ArrayList<String>nama_loket=new ArrayList<>();
    private ArrayList<String>kode_loket=new ArrayList<>();
    private ArrayList<String>kode_ca=new ArrayList<>();
    private ArrayList<String>kode_sub_ca=new ArrayList<>();
    private ArrayList<String>kode_sub_ca_1=new ArrayList<>();
    private ArrayList<String>kode_sub_ca_2=new ArrayList<>();
    private ArrayList<String>kode_sub_ca_3=new ArrayList<>();
    private ArrayList<String>referral=new ArrayList<>();
    private ArrayList<String>id_loket=new ArrayList<>();
    private ArrayList<String>saldo=new ArrayList<>();
    private ArrayList<String>cashback=new ArrayList<>();
    private ArrayList<String>fee=new ArrayList<>();
    private ArrayList<String>open=new ArrayList<>();
    private ArrayList<String>bulanan=new ArrayList<>();
    private ArrayList<String>fotos=new ArrayList<>();
    private int FILE_SELECT_CODE=1;
    private TextView tvNama,tvNamaLoket,tvKodeloket,tvSaldo,tvLogout,tvTopup,tvChange,tvCB,tvCB2,tvChange2,tvChange3,
            tvChange4,textref,textfee,textbln;
    private DecimalFormat decimalFormat=new DecimalFormat("###,###,###.##");
    private float sals,scale=0,cb,text_fee,opn,pinj,pinj2,bln;
    private Uri selectedImageUri;
    private File file;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSINOS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs =getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_home);
        ivPln=findViewById(R.id.ivPln);
        ivProfile=findViewById(R.id.ivProfile);
        ivHome=findViewById(R.id.ivHome);
        ivinfo=findViewById(R.id.ivinfo);
        ivFoto=findViewById(R.id.ivFoto);
        ivPdam=findViewById(R.id.ivPdam);
        ivShop=findViewById(R.id.ivShop);
        ivSamsat=findViewById(R.id.ivSamsat);
        ivmulty=findViewById(R.id.ivmulty);
        ivPrabayar=findViewById(R.id.ivPrabayar);
        ivTelkom=findViewById(R.id.ivTelkom);
        ivtvkabel=findViewById(R.id.ivtvkabel);
        textfee=findViewById(R.id.textfee);
        textbln=findViewById(R.id.textbln);
        textref=findViewById(R.id.textref);
        tvChange=findViewById(R.id.tvChange);
        tvChange2=findViewById(R.id.tvChange2);
        tvChange3=findViewById(R.id.tvChange3);
        tvChange4=findViewById(R.id.tvChange4);
        ivpswt=findViewById(R.id.ivpswt);
        ivtrf=findViewById(R.id.ivtrf);
        ivtravel=findViewById(R.id.ivtravel);
        ivPbb=findViewById(R.id.ivPbb);
        ivkasir=findViewById(R.id.ivkasir);
        ivRefresh=findViewById(R.id.ivRefresh);
        ivKolektif=findViewById(R.id.ivKolektif);
        ivcsa=findViewById(R.id.ivcsa);
        btPrint=findViewById(R.id.btPrint);
        ivPaska=findViewById(R.id.ivPaska);
        tvNama=findViewById(R.id.tvNama);
        ivGopay=findViewById(R.id.ivGopay);
        ivAngs=findViewById(R.id.ivAngs);
        ivOvo=findViewById(R.id.ivOvo);
        ivToll=findViewById(R.id.ivToll);
        ivLink=findViewById(R.id.ivLink);
        ivDana=findViewById(R.id.ivDana);
        ivLaporan=findViewById(R.id.ivLaporan);
        ivBpjs=findViewById(R.id.ivBpjs);
        tvNamaLoket=findViewById(R.id.tvNamaLoket);
        tvKodeloket=findViewById(R.id.tvKodeloket);
        tvSaldo=findViewById(R.id.tvSaldo);
        tvCB=findViewById(R.id.tvCB);
        tvCB2=findViewById(R.id.tvCB2);
        tvTopup=findViewById(R.id.tvTopup);
        tvLogout=findViewById(R.id.tvLogout);
        toolbars=findViewById(R.id.toolbar);
        pratul = findViewById(R.id.pratul);
        tul = findViewById(R.id.tul);
        plntul = findViewById(R.id.plntul);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ll5.setVisibility(View.GONE);
        datauser=boyprefs.getString("datauser","");
        Log.e("datauser",datauser);
        nama_pemilik=PojoMion.AmbilArray(datauser,"nama_pemilik","dataloket");
        nama_loket=PojoMion.AmbilArray(datauser,"nama_loket","dataloket");
        id_loket=PojoMion.AmbilArray(datauser,"id","dataloket");
        kode_loket=PojoMion.AmbilArray(datauser,"kode_loket","dataloket");
        kode_ca = PojoMion.AmbilArray(datauser, "kode_ca", "dataloket");
        kode_sub_ca = PojoMion.AmbilArray(datauser, "kode_sub_ca", "dataloket");
        kode_sub_ca_1 = PojoMion.AmbilArray(datauser, "kode_sub_ca_1", "dataloket");
        kode_sub_ca_2 = PojoMion.AmbilArray(datauser, "kode_sub_ca_2", "dataloket");
        kode_sub_ca_3 = PojoMion.AmbilArray(datauser, "kode_sub_ca_3", "dataloket");
        referral = PojoMion.AmbilArray(datauser, "referral", "dataloket");
        kodeloket=(kode_loket.get(0));
        saldo=PojoMion.AmbilArray(datauser,"saldo","depositloket");
        cashback=PojoMion.AmbilArray(datauser,"cashback","depositloket");
        fee=PojoMion.AmbilArray(datauser,"fee","depositloket");
        open=PojoMion.AmbilArray(datauser,"open","depositloket");
        bulanan=PojoMion.AmbilArray(datauser,"bulanan","depositloket");
        scale = getResources().getDisplayMetrics().density;
        fotos=PojoMion.AmbilArray(datauser,"foto","datauser");
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        tvNama.setText(nama_pemilik.get(0));
        tvNamaLoket.setText(username);
        tvKodeloket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        kodeca=(kode_ca.get(0));
        kodesubca=(kode_sub_ca.get(0));
        kodesubca1=(kode_sub_ca_1.get(0));
        kodesubca2=(kode_sub_ca_2.get(0));
        kodesubca3=(kode_sub_ca_3.get(0));

       if(kodesubca.equalsIgnoreCase("SMB00066")){
            pratul.setVisibility(View.GONE);
            tul.setVisibility(View.GONE);
           plntul.setVisibility(View.VISIBLE);
        }
        bln = Float.parseFloat(bulanan.get(0));
        if(bln>=0) {
            textbln.setText("Fee Bulanan Anda :\n" + "Rp." + decimalFormat.format(bln));
        }
        text_ref = referral.get(0);
        if(text_ref.isEmpty()) {
            textref.setVisibility(View.GONE);
  //            if (kodesubca3.isEmpty()) {
    //                ll2.setVisibility(View.VISIBLE);
      //              tvChange2.setVisibility(View.VISIBLE);
        //            ll3.setVisibility(View.GONE);
  //                  ll4.setVisibility(View.GONE);
    //                tvChange3.setVisibility(View.GONE);
  //                  tvChange4.setVisibility(View.GONE);
    //                textfee.setVisibility(View.VISIBLE);
   //                 textfee.setText("Bonus Agen Anda :\n" + "Rp.0");
   //             }
     //         else {
   //                 ll2.setVisibility(View.VISIBLE);
                    tvChange2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.GONE);
                    ll4.setVisibility(View.GONE);
                    tvChange3.setVisibility(View.GONE);
                    tvChange4.setVisibility(View.GONE);
                    textfee.setVisibility(View.VISIBLE);
            text_fee = Float.parseFloat(fee.get(0));
            textfee.setText("Bonus Agen Anda :\n" + "Rp." + decimalFormat.format(text_fee));
       //         }
        }
        else {                                  // jika referal keisi / ada
  //          ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            tvChange2.setVisibility(View.GONE); //daftar agen
            tvChange3.setVisibility(View.VISIBLE);  //pencairan bonus
            tvChange4.setVisibility(View.VISIBLE);  // edit markup
            textfee.setVisibility(View.VISIBLE);
            textref.setVisibility(View.VISIBLE);
            text_ref = referral.get(0);
            text_fee = Float.parseFloat(fee.get(0));

            textfee.setText("Bonus Agen Anda :\n" + "Rp." + decimalFormat.format(text_fee));
            textref.setText("Kode Referral Anda :\n" + text_ref);
        }


        if(saldo.isEmpty()){
            tvSaldo.setText("Rp.0");
            tvCB.setText("Rp.0");
        }
        else{
            sals= Float.parseFloat(saldo.get(0));
            cb= Float.parseFloat(cashback.get(0));
            opn= Float.parseFloat(open.get(0));

            pinj=sals-opn;
            pinj2=opn-sals;
            tvSaldo.setText("Rp."+decimalFormat.format(sals));
            tvCB.setText("CashBack Rp."+decimalFormat.format(cb));
            if(pinj<0) {
                tvCB2.setText("Saldo Pinjaman Rp." + decimalFormat.format(pinj2));
            }

            else {
                tvCB2.setVisibility(View.GONE);
            }
        }
        foto=fotos.get(0);
        Log.e("foto",foto);
        if(!foto.isEmpty()){
            Picasso.get().load(foto.replace(" ","%20")).into(ivFoto);
        }
        tvTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
             //       startActivity(new Intent(Home.this,trfrek.class));
                    //       finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivcsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adaInternet()) {
                    String phoneNumberWithCountryCode = "+628113445553";
                    String message = "Halo Loket Bayar..";

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                            phoneNumberWithCountryCode, message))));
                    // Dialog dialog=new Dialog(Home.this);
                    //   dialog.setContentView(R.layout.dialogok);
                    //     dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    //       TextView tvIf=dialog.findViewById(R.id.tvIf);
                    // TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    //   Button btTidak=dialog.findViewById(R.id.btTidak);
                    //     tvIf.setText("Silahkan menghubungi CA");
                    //       tvTanya.setText("Informasi");
                    //   btTidak.setOnClickListener(new View.OnClickListener() {
                    //         @Override
                    //           public void onClick(View v) {
                    //       dialog.dismiss();
                    //       }
                    //     });
                    //      dialog.show();

                } else {
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }


        });
  //      Intent pushIntent = new Intent(this, JalanService.class);
  //      pushIntent.putExtra("username", username);
    //    pushIntent.putExtra("password", password);
      //  pushIntent.putExtra("kode_loket", kode_loket.get(0));
    //    pushIntent.putExtra("saldos", saldo.get(0));
   //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
     //       startForegroundService(pushIntent);
   //     }
     //   else{
       //     startService(pushIntent);
//        }
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    startActivity(new Intent(Home.this,PrintSetters.class));
                    finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivtvkabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
             //       boyprefs.edit().putString("dari","TV Kabel").apply();
             //       new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"TV",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivtrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
          //          boyprefs.edit().putString("dari","Transfer").apply();
          //          new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"TRANSFER",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivPbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
     //               boyprefs.edit().putString("dari","Pbb").apply();
       //             new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"PBB",kode_loket.get(0));
                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivBpjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
         //           startActivity(new Intent(Home.this,Bpjs.class));
         //           finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivpswt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    new AmbilTul(Home.this).execute("ambil", username, password,
                            kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivkasir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                   new AmbildataTUL(Home.this).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivtravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Home.this,kirimloket.class));
  //                  finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivAngs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Home.this,Angsuran.class));
  //                  finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
   //                 Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Home.this,Laporan.class));
                    finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivmulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    new MultyAwalTask(Home.this).execute("login",username,password,kodeloket);

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivTelkom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Home.this,Telkom.class));
  //                  finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivPln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
    //                Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Home.this,Pln.class));
                    finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivSamsat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    new AmbilPraTul(Home.this).execute("ambil", username, password,
                             kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivPaska.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Home.this,Paskabayar.class));
  //                  finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivPrabayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Home.this,PulsaPrabayar.class));
  //                  finish();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    tvSaldo.setText("");
                    Log.e("kodeloket",kode_loket.get(0));
                    String username=boyprefs.getString("username","");
                    String password=boyprefs.getString("password","");
                    new AmbilSaldoTask(Home.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Log.e("kodeloket",kode_loket.get(0));
                    String username=boyprefs.getString("username","");
                    String password=boyprefs.getString("password","");
                    new AmbilSaldoTask(Home.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AmbilProfilRequest2(Home.this).requestAction(username,password,id_loket.get(0),nama_loket.get(0),
                        getResources().getString(R.string.xyz)+"ambilprofiling");
//                startActivity(new Intent(Home.this,Rubpas.class));
  //              finish();
            }
        });
        tvChange2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this,daftaragen.class));
  //              finish();
            }
        });
        tvChange3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this,cairsaldo.class));
  //              finish();
            }
        });
        tvChange4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this,editmarkup.class));
  //              finish();
            }
        });
        drawerLayout=(DrawerLayout)findViewById(R.id.laci);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbars,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Dialog dialog=new Dialog(Home.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Anda Ingin Logout?");
                    tvTanya.setText("Pesan");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        //    JalanService.saldoakhir=Integer.parseInt("0");
                            dialog.dismiss();
                            boyprefs.edit().clear().apply();
     //                       JalanService.saldoakhir=0;
   //                         stopService(new Intent(Home.this,JalanService.class));
                            startActivity(new Intent(Home.this,MainActivity.class));
                            finish();

                        }
                    });
                    dialog.show();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivGopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    boyprefs.edit().putString("dari","GOPAY").apply();
  //                  new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"GOPAY",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    boyprefs.edit().putString("dari","SHOPEEPAY").apply();
  //                  new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"SHOPEE",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivPdam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    boyprefs.edit().putString("dari","Pdam").apply();
  //                  new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"PDAM",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivDana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    boyprefs.edit().putString("dari","DANA").apply();
  //                  new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"DANA",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivToll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    boyprefs.edit().putString("dari","ETOLL").apply();
  //                  new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"ETOLL",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    boyprefs.edit().putString("dari","LinkAja").apply();
  //                  new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"LINK",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivOvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    boyprefs.edit().putString("dari","OVO").apply();
  //                  new AmbilProdukOVOdkkTask(Home.this).execute("ambil",username,password,"OVO",kode_loket.get(0));

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ivKolektif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
//                    String kodekolektif=boyprefs.getString("kodekolektif","");
  //                  Log.e("kodekolektif",kodekolektif);
    //                new MultyAwalTask2(Home.this).execute("login",username,password,kodeloket);
                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    new AmbilProfilRequest(Home.this).requestAction(username,password,id_loket.get(0),nama_loket.get(0),
                            getResources().getString(R.string.xyz)+"ambilprofiling");
                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Dialog dialog=new Dialog(Home.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Loket Bayar\nApp Version 2.0");
                    tvTanya.setText("Informasi");
                    tvTanya.setText("Informasi");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }


            }
        });
        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    tangs=GetDate.getDateTime();
                    final CharSequence[] items = {"Ambil dari Camera","Pilih dari Gallery"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
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
                                    startActivityForResult(intent, CAM_REQUEST1);
                                }
                            }
                            else if(items[item].equals("Pilih dari Gallery")){
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(photoPickerIntent, SELECT_FILE1);
                            }

                            else if (items[item].equals("Cancel")) {
                                ivFoto.setAlpha((float) 0.5);
                                dialog.dismiss();
                                ivFoto.setAlpha((float) 1.0);

                            } else {
                                ivFoto.setAlpha((float) 1.0);

                            }
                        }
                    });
                    builder.show();
                }else{
                    Toast.makeText(Home.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
    private void selectSingleCamera() {

    }
    public static void verifyStoragePermissions(Activity activity,int FILE_SELECT_CODE) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSINOS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{

        }
    }
    @Override

    public void onBackPressed() {
        Dialog dialog=new Dialog(Home.this);
        dialog.setContentView(R.layout.dialogok);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView tvIf=dialog.findViewById(R.id.tvIf);
        TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
        Button btTidak=dialog.findViewById(R.id.btTidak);
        tvIf.setText("Ingin Keluar Aplikasi?");
        tvTanya.setText("Pesan");
        btTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
              finish();

            }
        });
        dialog.show();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_OK) {
            Log.e("masuk","ambilfoto");
            File fileb = new File(Environment.getExternalStorageDirectory()+ File.separator + tangs+".jpg");
            ExifInterface exif = null;
            realimage1 = null;
            ByteArrayOutputStream byteArrayOutputStream;
            byte[] byteArray;
            bitmap1 = BitmapFactory.decodeFile(fileb.getAbsolutePath());
            int w=bitmap1.getWidth()/4;
            int h=bitmap1.getHeight()/4;
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
                    ivFoto.setImageBitmap(realimage1);
                    ivFoto.setAlpha((float) 1.0);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    ViewGroup.LayoutParams layoutParams = ivFoto.getLayoutParams();
                    layoutParams.width = (int) (70 * scale);
                    layoutParams.height = (int) (70 * scale);
                    ivFoto.setLayoutParams(layoutParams);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                    realimage1 = rotate(bitmap1, 0);
                    ivFoto.setImageBitmap(realimage1);
                    ivFoto.setAlpha((float) 1.0);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    layoutParams = ivFoto.getLayoutParams();
                    layoutParams.width = (int) (70 * scale);
                    layoutParams.height = (int) (70 * scale);
                    ivFoto.setLayoutParams(layoutParams);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                    realimage1 = rotate(bitmap1, 180);
                    ivFoto.setImageBitmap(realimage1);
                    ivFoto.setAlpha((float) 1.0);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    layoutParams = ivFoto.getLayoutParams();
                    layoutParams.width = (int) (70 * scale);
                    layoutParams.height = (int) (70 * scale);
                    ivFoto.setLayoutParams(layoutParams);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                    realimage1 = rotate(bitmap1, 90);
                    ivFoto.setImageBitmap(realimage1);
                    ivFoto.setAlpha((float) 1.0);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    layoutParams = ivFoto.getLayoutParams();
                    layoutParams.width = (int) (70 * scale);
                    layoutParams.height = (int) (70 * scale);
                    ivFoto.setLayoutParams(layoutParams);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    //   bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 360);
                    realimage1 = rotate(bitmap1, 270);
                    ivFoto.setImageBitmap(realimage1);
                    ivFoto.setAlpha((float) 1.0);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    layoutParams = ivFoto.getLayoutParams();
                    layoutParams.width = (int) (70 * scale);
                    layoutParams.height = (int) (70 * scale);
                    ivFoto.setLayoutParams(layoutParams);
                    break;
            }
            load=new Dialog(Home.this);
            load.setContentView(R.layout.dialogload2);
            load.setCancelable(false);
            ivX=load.findViewById(R.id.ivX);
            Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            load.show();
            SaveFoto(getResources().getString(R.string.xyz)+"savefoto",foto,username,password);
        }
        else if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_CANCELED) {
            ivFoto.setAlpha((float) 1.0);
            foto = "";
        //    ivFoto.setImageResource(R.drawable.tblfoto);
     //       ViewGroup.LayoutParams layoutParams = ivFoto.getLayoutParams();
    //        layoutParams = ivFoto.getLayoutParams();
    //        layoutParams.width = (int) (80 * scale);
    //        layoutParams.height = (int) (80 * scale);
   //         ivFoto.setLayoutParams(layoutParams);
        }
        else if (requestCode == SELECT_FILE1 && resultCode == Activity.RESULT_OK) {
            Log.e("masuk","galery");
            bitmap1 = null;
            if (data != null) {
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            assert bitmap1 != null;
            int w=bitmap1.getWidth()/4;
            int h=bitmap1.getHeight()/4;
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bitmap1.getWidth(), bitmap1.getHeight()), new RectF(0, 0, w, h), Matrix.ScaleToFit.CENTER);
            realimage1=Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), m, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            realimage1.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
            ivFoto.setImageBitmap(realimage1);
            ViewGroup.LayoutParams layoutParams = ivFoto.getLayoutParams();
            layoutParams = ivFoto.getLayoutParams();
            layoutParams.width = (int) (70 * scale);
            layoutParams.height = (int) (70 * scale);
            ivFoto.setLayoutParams(layoutParams);
            ivFoto.setAlpha((float) 1.0);
            load=new Dialog(Home.this);
            load.setContentView(R.layout.dialogload2);
            load.setCancelable(false);
            ivX=load.findViewById(R.id.ivX);
            Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            load.show();
            SaveFoto(getResources().getString(R.string.xyz)+"savefoto",foto,username,password);
        }
        else if (requestCode == SELECT_FILE1 && resultCode == Activity.RESULT_CANCELED) {
            ivFoto.setAlpha((float) 1.0);
  //          ViewGroup.LayoutParams layoutParams = ivFoto.getLayoutParams();
  //          layoutParams = ivFoto.getLayoutParams();
  //          layoutParams.width = (int) (80 * scale);
  //          layoutParams.height = (int) (80 * scale);
  //          ivFoto.setLayoutParams(layoutParams);
      //      ivFoto.setImageResource(R.drawable.tblfoto);
            foto = "";
        }

    }
    public static String getPath(final Context context, final Uri uri) {
        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
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
    void SaveFoto(String POST_ORDER,String fotonya,String usernames,String passwords) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if(ServerResponse.trim().equalsIgnoreCase("sukses")){
                        load.dismiss();
                        Toast.makeText(Home.this, "Berhasil Ganti Foto", Toast.LENGTH_SHORT).show();
                    }else{
                        load.dismiss();
                        Log.e("errorsavefoto",ServerResponse);
                        Toast.makeText(Home.this, "Gagal Silahkan Diulang", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
        requestQueue.add(stringRequest);
    }

}