package loketbayar.id;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Profiling2 extends AppCompatActivity {
    private SharedPreferences boyprefs;
    private String dataprofile,datauser,foto="";
    private TextView idnama,etkodeloket,etNamaLoket,etMail,etHp,etTipeIdentitas,etNomerIdentitas,etAlamat,etKodepos,etHp2,etMail2
            ,tvSaldo,tvNama,tvNamaLoket,tvJuduls,tvKodeloket;
    private Dialog load;
    private ImageView ivFoto,ivBack;
    private Button btCheck2;
    public static float saldobayar = 0;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> idloket=new ArrayList<>();
    private ArrayList<String> kode_ca=new ArrayList<>();
    private ArrayList<String> kode_loket=new ArrayList<>();
    private ArrayList<String> id_loket=new ArrayList<>();
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
    private ArrayList<String>fotos=new ArrayList<>();

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
        setContentView(R.layout.activity_profiling2);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvJuduls.setText("PROFIL");
        ivFoto=findViewById(R.id.ivFoto);
        ivBack = findViewById(R.id.ivBack);
        etkodeloket=findViewById(R.id.etkodeloket);
        etNamaLoket=findViewById(R.id.etNamaLoket);
        etTipeIdentitas=findViewById(R.id.etTipeIdentitas);
        etNomerIdentitas=findViewById(R.id.etNomerIdentitas);
        etAlamat=findViewById(R.id.etAlamat);
        etKodepos=findViewById(R.id.etKodepos);
        etMail=findViewById(R.id.etMail);
        etMail2=findViewById(R.id.etMail2);
        etHp=findViewById(R.id.etHp);
        etHp2=findViewById(R.id.etHp2);
        idnama=findViewById(R.id.idnama);
        btCheck2 = findViewById(R.id.btCheck2);
        tvSaldo = findViewById(R.id.tvSaldo);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvNama = findViewById(R.id.tvNama);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        dataprofile=boyprefs.getString("dataprofile","");
        datauser=boyprefs.getString("datauser","");
        Log.e("dataprofile",dataprofile);
        idloket= PojoMion.AmbilArray(dataprofile,"id","dataprofile");
        kode_ca= PojoMion.AmbilArray(dataprofile,"kode_ca","dataprofile");
        kode_loket= PojoMion.AmbilArray(dataprofile,"kode_loket","dataprofile");
        id_loket=PojoMion.AmbilArray(dataprofile,"id","dataprofile");
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
        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        String username = boyprefs.getString("username", "");
        String password=boyprefs.getString("password","");
        new AmbilSaldoTask(Profiling2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));

        fotos=PojoMion.AmbilArray(datauser,"foto","datauser");
        saldobayar= Float.parseFloat(saldo.get(0));
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);
        idnama.setText(nama_pemilik.get(0));
        etkodeloket.setText(kode_loket.get(0));
        etNamaLoket.setText(nama_loket.get(0));
        etTipeIdentitas.setText(tipe_identitas.get(0));
        etNomerIdentitas.setText(nomor_identitas.get(0));
        etAlamat.setText(alamat.get(0)+" "+kelurahan.get(0)+" "+kecamatan.get(0)+" "+kota.get(0)+" "+provinsi.get(0));
        etKodepos.setText(kode_pos.get(0));
        etHp.setText(hp.get(0));
        etHp2.setText(username);
        etMail.setText(email.get(0));
        etMail2.setText(emailuser.get(0));

        foto=fotos.get(0);
        Log.e("foto",foto);
        if(!foto.isEmpty()){
            Picasso.get().load(foto.replace(" ","%20")).into(ivFoto);
        }
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    tvSaldo.setText("");
                    String username=boyprefs.getString("username","");
                    String password=boyprefs.getString("password","");
                    new AmbilSaldoTask(Profiling2.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }
                else{
                    Toast.makeText(Profiling2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(Profiling2.this).execute("login", username, password);
                }
                else{
                    Toast.makeText(Profiling2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    new AmbilProfilRequest3(Profiling2.this).requestAction(username,password,id_loket.get(0),nama_loket.get(0),
                            getResources().getString(R.string.xyz)+"ambilprofiling");
                }else{
                    Toast.makeText(Profiling2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    @Override
    public void onBackPressed() {
        if(adaInternet()) {
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(Profiling2.this).execute("login", username, password);
        }else{
            Toast.makeText(Profiling2.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
}
