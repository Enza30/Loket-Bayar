package loketbayar.id;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class Rubpas extends AppCompatActivity {
    private EditText etPassword,etPassbaru,etKonfirm,etPassword2;
    private Button btSubmit;
    private String password,passwordlama,passbaru,konfirmpas;
    private SharedPreferences boyprefs;
    private ImageView ivBack,ivPrint,ivRefresh;
    private Dialog load;
    private ImageView ivX;
    private TextView tvJuduls;
    private String datauser,dataprofile;
    private DecimalFormat format = new DecimalFormat("###,###,###.##");
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvJudulIdPel,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul,
            tvJudulNama,tvTotalTagihanjudul,tvTotalTagihan,tvNilaiTagihanjudul,tvNilaiTagihan,
            tvAdmJudul,tvAdmin;
    private float sals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boyprefs =getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_rubpas);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvNamaLoket= findViewById(R.id.tvNamaLoket);
        tvKodeloket= findViewById(R.id.tvKodeloket);
        tvSaldo= findViewById(R.id.tvSaldo);
                tvNama= findViewById(R.id.tvNama);
        tvKodeloket= findViewById(R.id.tvKodeloket);
        tvJuduls.setText("UBAH PASSWORD");
        super.onCreate(savedInstanceState);
        datauser = boyprefs.getString("datauser", "");
        dataprofile=boyprefs.getString("dataprofile","");
//        datauser=boyprefs.getString("datauser","");
        nama_pemilik = PojoMion.AmbilArray(dataprofile, "nama_pemilik", "dataprofile");
        nama_loket = PojoMion.AmbilArray(dataprofile, "nama_loket", "dataprofile");
        kode_loket = PojoMion.AmbilArray(dataprofile, "kode_loket", "dataprofile");
        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        email = PojoMion.AmbilArray(dataprofile, "email", "datauser");

        sals = Float.parseFloat(saldo.get(0));
        tvSaldo.setText("Rp." + decimalFormat.format(sals));
        tvNama.setText(nama_pemilik.get(0));
        String username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);
        new AmbilSaldoTask(Rubpas.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        etPassword=findViewById(R.id.etPassword);
        etPassword2=findViewById(R.id.etPassword2);
        etPassword2.setText(email.get(0));
        etPassword2.setEnabled(false);
        etPassbaru=findViewById(R.id.etPassbaru);
        etKonfirm=findViewById(R.id.etKonfirm);
        ivBack=findViewById(R.id.ivBack);
        ivPrint = findViewById(R.id.ivPrint);
        ivPrint.setVisibility(View.GONE);
        ivRefresh = findViewById(R.id.ivRefresh);
        btSubmit=findViewById(R.id.btSubmit);
        password=boyprefs.getString("password","");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(Rubpas.this).execute("login", username, password);
                }else{
                    Toast.makeText(Rubpas.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    String username=boyprefs.getString("username","");
                    String password=boyprefs.getString("password","");
                    new AmbilSaldoTask(Rubpas.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Rubpas.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordlama=etPassword.getText().toString();
                passbaru=etPassbaru.getText().toString();
                konfirmpas=etKonfirm.getText().toString();
                if(!passwordlama.equals(password)){
                    Toast.makeText(Rubpas.this,"Password Lama Anda Salah", Toast.LENGTH_SHORT).show();

                }else if(passwordlama.isEmpty()){
                    Toast.makeText(Rubpas.this,"Masukan Password Lama", Toast.LENGTH_SHORT).show();

                }else if(konfirmpas.isEmpty()){
                    Toast.makeText(Rubpas.this,"Masukan Konfirmasi Password", Toast.LENGTH_SHORT).show();

                }else if(passbaru.isEmpty()){
                    Toast.makeText(Rubpas.this,"Masukan Password Baru", Toast.LENGTH_SHORT).show();

                }else if(!konfirmpas.equals(passbaru)){
                    Toast.makeText(Rubpas.this,"Konfirmasi Password Tidak Sama Dengan Password Baru", Toast.LENGTH_SHORT).show();
                }else{
                    if(adaInternet()) {
                    load=new Dialog(Rubpas.this);
                    load.setContentView(R.layout.dialogload2);
                    load.setCancelable(false);
                    ivX=load.findViewById(R.id.ivX);
                    Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                    load.show();
                    String username=boyprefs.getString("username","");
                    new GantiPasswordBundle(Rubpas.this,load).requestAction(username,password,
                            passbaru,konfirmpas,
                            getResources().getString(R.string.xyz)+getString(R.string.ubahpas));
                    } else{
                        Toast.makeText(Rubpas.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(adaInternet()) {
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(Rubpas.this).execute("login", username, password);
        } else{
            Toast.makeText(Rubpas.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
}