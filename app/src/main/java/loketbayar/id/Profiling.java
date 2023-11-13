package loketbayar.id;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profiling extends AppCompatActivity {
    private SharedPreferences boyprefs;
    private String dataprofile,datauser;
    private EditText etKodeca,etkodeloket,etNamaLoket,etNamaPemilik,etTipeIdentitas,etNomerIdentitas,
            etAlamat,etKelurahan,etKecamatan,etKota,etProvinsi,etKodepos,etMail,etHp,etHp2,etMail2;
    private Button btDaftar, btUpdate,btDaftar2, btUpdate2;
    private Dialog load;
    private ArrayList<String> idloket=new ArrayList<>();
    private ArrayList<String> kode_ca=new ArrayList<>();
    private ArrayList<String> kode_loket=new ArrayList<>();
    private ArrayList<String> nama_loket=new ArrayList<>();
    private ArrayList<String> nama_pemilik=new ArrayList<>();
    private ArrayList<String> tipe_identitas=new ArrayList<>();
    private ArrayList<String> nomor_identitas=new ArrayList<>();
    private ArrayList<String> alamat=new ArrayList<>();
    private ArrayList<String> kelurahan=new ArrayList<>();
    private ArrayList<String> kecamatan=new ArrayList<>();
    private ArrayList<String> kota=new ArrayList<>();
    private ArrayList<String> provinsi=new ArrayList<>();
    private ArrayList<String> kode_pos=new ArrayList<>();
    private ArrayList<String> email=new ArrayList<>();
    private ArrayList<String> hp=new ArrayList<>();
    private ArrayList<String> emailuser=new ArrayList<>();
    private ArrayList<String> iduser=new ArrayList<>();

    String idlokets;
    String kode_cas;
    String kode_lokets;
    String nama_lokets;
    String nama_pemiliks;
    String tipe_identitass;
    String nomor_identitass;
    String alamats;
    String kelurahans;
    String kecamatans;
    String kotas;
    String provinsis;
    String kode_poss;
    String emails;
    String hps;
    String emailusr;
    String idusr;
    private ImageView ivX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs =getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_profiling);
        btDaftar=findViewById(R.id.btDaftar);
        btUpdate=findViewById(R.id.btUpdate);
        btDaftar2=findViewById(R.id.btDaftar2);
        btUpdate2=findViewById(R.id.btUpdate2);
        etkodeloket=findViewById(R.id.etkodeloket);
        etNamaLoket=findViewById(R.id.etNamaLoket);
        etNamaPemilik=findViewById(R.id.etNamaPemilik);
        etTipeIdentitas=findViewById(R.id.etTipeIdentitas);
        etNomerIdentitas=findViewById(R.id.etNomerIdentitas);
        etAlamat=findViewById(R.id.etAlamat);
        etKelurahan=findViewById(R.id.etKelurahan);
        etKecamatan=findViewById(R.id.etKecamatan);
        etKota=findViewById(R.id.etKota);
        etProvinsi=findViewById(R.id.etProvinsi);
        etKodepos=findViewById(R.id.etKodepos);
        etMail=findViewById(R.id.etMail);
        etMail2=findViewById(R.id.etMail2);
        etHp=findViewById(R.id.etHp);
        etHp2=findViewById(R.id.etHp2);
        etKodeca=findViewById(R.id.etKodeca);
        dataprofile=boyprefs.getString("dataprofile","");
        datauser=boyprefs.getString("datauser","");
        Log.e("dataprofile",dataprofile);
        idloket= PojoMion.AmbilArray(dataprofile,"id","dataprofile");
        kode_ca= PojoMion.AmbilArray(dataprofile,"kode_ca","dataprofile");
        kode_loket= PojoMion.AmbilArray(dataprofile,"kode_loket","dataprofile");
        nama_loket= PojoMion.AmbilArray(dataprofile,"nama_loket","dataprofile");
        nama_pemilik= PojoMion.AmbilArray(dataprofile,"nama_pemilik","dataprofile");
        tipe_identitas= PojoMion.AmbilArray(dataprofile,"tipe_identitas","dataprofile");
        nomor_identitas= PojoMion.AmbilArray(dataprofile,"nomor_identitas","dataprofile");
        alamat= PojoMion.AmbilArray(dataprofile,"alamat","dataprofile");
        kelurahan= PojoMion.AmbilArray(dataprofile,"kelurahan","dataprofile");
        kecamatan= PojoMion.AmbilArray(dataprofile,"kecamatan","dataprofile");
        kota= PojoMion.AmbilArray(dataprofile,"kota","dataprofile");
        provinsi= PojoMion.AmbilArray(dataprofile,"provinsi","dataprofile");
        kode_pos= PojoMion.AmbilArray(dataprofile,"kode_pos","dataprofile");
        // kode_pos= PojoMion.AmbilArray(dataprofile,"email","datauser");
        email= PojoMion.AmbilArray(dataprofile,"email","dataprofile");
        hp= PojoMion.AmbilArray(dataprofile,"hp","dataprofile");
        emailuser= PojoMion.AmbilArray(dataprofile,"email","datauser");
        iduser= PojoMion.AmbilArray(dataprofile,"id","datauser");
        etKodeca.setText(kode_ca.get(0));
        etkodeloket.setText(kode_loket.get(0));
        etKodeca.setEnabled(false);
        etHp2.setEnabled(false);
        etKodeca.setVisibility(View.GONE);
        etkodeloket.setEnabled(false);
        etNamaLoket.setText(nama_loket.get(0));
        etNamaLoket.setEnabled(false);
        etNamaPemilik.setText(nama_pemilik.get(0));
        etTipeIdentitas.setText(tipe_identitas.get(0));
        etNomerIdentitas.setText(nomor_identitas.get(0));
        etAlamat.setText(alamat.get(0));
        etKelurahan.setText(kelurahan.get(0));
        etKecamatan.setText(kecamatan.get(0));
        etKota.setText(kota.get(0));
        etProvinsi.setText(provinsi.get(0));
        etKodepos.setText(kode_pos.get(0));
        etHp.setText(hp.get(0));
        String username = boyprefs.getString("username", "");
        etHp2.setText(username);
        etMail.setText(email.get(0));
        etMail2.setText(emailuser.get(0));

        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(Profiling.this).execute("login", username, password);
                }else{
                    Toast.makeText(Profiling.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                } }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    kode_cas=etKodeca.getText().toString();
                    kode_lokets= etkodeloket.getText().toString();
                    nama_lokets= etNamaLoket.getText().toString();
                    nama_pemiliks= etNamaPemilik.getText().toString();
                    tipe_identitass= etTipeIdentitas.getText().toString();
                    nomor_identitass= etNomerIdentitas.getText().toString();
                    alamats= etAlamat.getText().toString();
                    kelurahans=etKelurahan.getText().toString();
                    kecamatans= etKecamatan.getText().toString();
                    kotas=etKota.getText().toString();
                    provinsis= etProvinsi.getText().toString();
                    kode_poss=etKodepos.getText().toString();
                    emails=etMail.getText().toString();
                    hps=etHp.getText().toString();
                    //          emailusr=etMail2.getText().toString();
                    //          idusr=iduser.get(0);
                    load=new Dialog(Profiling.this);
                    load.setContentView(R.layout.dialogload2);
                    load.setCancelable(false);
                    ivX=load.findViewById(R.id.ivX);
                    Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                    load.show();
                    SimpanProfil(getResources().getString(R.string.xyz)+"simpanprofil",
                            idloket.get(0),
                            kode_cas,
                            kode_lokets,
                            nama_lokets,
                            nama_pemiliks,
                            tipe_identitass,
                            nomor_identitass,
                            alamats,
                            kelurahans,
                            kecamatans,
                            kotas,
                            provinsis,
                            kode_poss,
                            emails,
                            hps);
                }else{
                    Toast.makeText(Profiling.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }}
        });

        btDaftar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(Profiling.this).execute("login", username, password);

                }else{
                    Toast.makeText(Profiling.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                } }
        });
        btUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    kode_cas=etKodeca.getText().toString();
                    kode_lokets= etkodeloket.getText().toString();
                    nama_lokets= etNamaLoket.getText().toString();
                    nama_pemiliks= etNamaPemilik.getText().toString();
                    tipe_identitass= etTipeIdentitas.getText().toString();
                    nomor_identitass= etNomerIdentitas.getText().toString();
                    alamats= etAlamat.getText().toString();
                    kelurahans=etKelurahan.getText().toString();
                    kecamatans= etKecamatan.getText().toString();
                    kotas=etKota.getText().toString();
                    provinsis= etProvinsi.getText().toString();
                    kode_poss=etKodepos.getText().toString();
                    emails=etMail.getText().toString();
                    hps=etHp.getText().toString();
                    emailusr=etMail2.getText().toString();
                    idusr=iduser.get(0);
                    load=new Dialog(Profiling.this);
                    load.setContentView(R.layout.dialogload2);
                    load.setCancelable(false);
                    ivX=load.findViewById(R.id.ivX);
                    Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                    load.show();
                    SimpanProfil2(getResources().getString(R.string.xyz)+"simpanprofil2",
                            idloket.get(0),
                            emailusr,idusr);
                }else{
                    Toast.makeText(Profiling.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }}
        });





    }
    @Override
    public void onBackPressed() {
        if(adaInternet()) {
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(Profiling.this).execute("login", username, password);
        }else{
            Toast.makeText(Profiling.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }

    void SimpanProfil(String POST_ORDER,String idlokets,String kode_cas,String kode_lokets,String nama_lokets,String nama_pemiliks,String tipe_identitass,
                      String nomor_identitass,String alamats,String kelurahans,String kecamatans,String kotas,String provinsis,String kode_poss,
                      String emails,String hps) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if(ServerResponse.trim().equalsIgnoreCase("sukses")) {
                        load.dismiss();
                        Toast.makeText(Profiling.this, "Berhasil Update", Toast.LENGTH_SHORT).show();
                    }
                    else if(ServerResponse.trim().equalsIgnoreCase("gagal")) {
                        load.dismiss();
                        Toast.makeText(Profiling.this, "Gagal Update (Email sudah ada / Data sama)", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 200);

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        load.dismiss();
                        Log.e("errorambilmulti",volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idloket", idlokets);
                params.put("kode_ca", kode_cas);
                params.put("kode_loket", kode_lokets);
                params.put("nama_loket", nama_lokets);
                params.put("nama_pemilik", nama_pemiliks);
                params.put("tipe_identitas", tipe_identitass);
                params.put("nomor_identitas", nomor_identitass);
                params.put("alamat", alamats);
                params.put("kelurahan", kelurahans);
                params.put("kecamatan", kecamatans);
                params.put("kota", kotas);
                params.put("provinsi", provinsis);
                params.put("kode_pos", kode_poss);
                params.put("email", emails);
                params.put("hp", hps);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Profiling.this);
        requestQueue.add(stringRequest);
    }

    void SimpanProfil2(String POST_ORDER,String idlokets,String emailusr,String idusr) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if(ServerResponse.trim().equalsIgnoreCase("sukses")) {
                        load.dismiss();
                        Toast.makeText(Profiling.this, "Berhasil Update", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        load.dismiss();
                        Toast.makeText(Profiling.this, "Gagal Update (Email sudah ada / Data sama)", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 200);

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        load.dismiss();
                        Log.e("errorambilmulti",volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idloket", idlokets);
                params.put("emailusr", emailusr);
                params.put("idusr", idusr);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Profiling.this);
        requestQueue.add(stringRequest);
    }


    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
}
