package loketbayar.id;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.PairedPrinter;
import com.mazenrashed.printooth.utilities.Printing;

import androidx.appcompat.app.AppCompatActivity;

import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Laporan extends AppCompatActivity {
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    public static ArrayList<String> tahunlist = new ArrayList<>();
    private ArrayList<String> bulanlist = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> PilihanList = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private ArrayList<String> referral = new ArrayList<>();
    private String username,password, tahun,bulan,terpilih, daritanggal,sampaitanggal,sampai;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvPelangganJudul,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul, tvJudulNama,tvJuduls;
    private ImageView ivPrint;
    private SharedPreferences boyprefs;
    private Spinner spBulan,spTahun;
    private Spinner spPilihan;
    private ImageView ivBack,ivRefresh;
    private String datauser,text_ref;
    private float saldobayar=0;
    private Button btCheck;
    Printing printing;
    private EditText etDari,etSampai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_laporan);
        etDari=findViewById(R.id.etDari);
        etSampai=findViewById(R.id.etSampai);
        terpilih="";
        spPilihan=findViewById(R.id.spPilihan);

        tvJuduls = findViewById(R.id.tvJuduls);
        tvJuduls.setText("LAPORAN");
        tvNama = findViewById(R.id.tvNama);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);
        tvRef = findViewById(R.id.tvRef);
        tvTagPln = findViewById(R.id.tvTagPln);
        tvNamas = findViewById(R.id.tvNamas);
        btCheck = findViewById(R.id.btCheck);
        ivBack = findViewById(R.id.ivBack);
        ivPrint = findViewById(R.id.ivPrint);
        Printooth.INSTANCE.init(this);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        tvLembarTagihan = findViewById(R.id.tvLembarTagihan);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivRefresh = findViewById(R.id.ivRefresh);
        datauser = boyprefs.getString("datauser", "");
        Log.e("datauser",datauser);
        nama_pemilik = PojoMion.AmbilArray(datauser, "nama_pemilik", "dataloket");
        nama_loket = PojoMion.AmbilArray(datauser, "nama_loket", "dataloket");
        kode_loket = PojoMion.AmbilArray(datauser, "kode_loket", "dataloket");
        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        kode_ca = PojoMion.AmbilArray(datauser, "kode_ca", "dataloket");
        kode_sub_ca = PojoMion.AmbilArray(datauser, "kode_sub_ca", "dataloket");
        referral = PojoMion.AmbilArray(datauser, "referral", "dataloket");
        new AmbilSaldoTask(Laporan.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);
        saldobayar= Float.parseFloat(saldo.get(0));
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
//        ivPrint.setVisibility(View.GONE);
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(Laporan.this);
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
                            Toast.makeText(Laporan.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(Laporan.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    new LoginTask(Laporan.this).execute("login", username, password);
                }else{
                    Toast.makeText(Laporan.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    tvSaldo.setText("");
                    new AmbilSaldoTask(Laporan.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Laporan.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                } }
        });
        PilihanList.add("Laporan");
        PilihanList.add("Rekap");
        PilihanList.add("Mutasi Saldo");
        text_ref = referral.get(0);
        if(!text_ref.isEmpty()) {
//            PilihanList.add("Mutasi Downline");
        }
        ArrayAdapter<String> pilihanadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, PilihanList);
        pilihanadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilihanadapter.notifyDataSetChanged();
        spPilihan.setAdapter(pilihanadapter);
        String tanggals=GetDate.ambiltanggal();
        spPilihan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                terpilih=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etDari.setText(tanggals);
        etSampai.setText(tanggals);
        etDari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Laporan.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String days= String.valueOf(dayOfMonth);
                                Log.e(days,String.valueOf(days.length()));
                                if(days.length()==1){
                                    days="0"+days;
                                    dayOfMonth= Integer.parseInt(days);
                                }

                                etDari.setText(days + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        etSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Laporan.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String days= String.valueOf(dayOfMonth);
                                Log.e(days,String.valueOf(days.length()));
                                if(days.length()==1){
                                    days="0"+days;
                                    dayOfMonth= Integer.parseInt(days);
                                }

                                etSampai.setText(days + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    daritanggal = "";
                    sampaitanggal = "";

                    daritanggal = etDari.getText().toString();
                    sampaitanggal = etSampai.getText().toString();
                    if (terpilih.equalsIgnoreCase("Laporan")) {
                        new AmbilReportTasl(Laporan.this).execute("ambil", username, password,
                                daritanggal, sampaitanggal, kode_loket.get(0), terpilih);
                    } else if (terpilih.equalsIgnoreCase("Rekap")) {
                        new AmbilReportTasl2(Laporan.this).execute("ambil", username, password,
                                daritanggal, sampaitanggal, kode_loket.get(0), terpilih);
                    } else if (terpilih.equalsIgnoreCase("Mutasi Saldo")) {
                        new AmbilReportTasl3(Laporan.this).execute("ambil", username, password,
                                daritanggal, sampaitanggal, kode_loket.get(0), terpilih);
                    }
                    else if (terpilih.equalsIgnoreCase("Mutasi Downline")) {
                        new AmbilReportTasl4(Laporan.this).execute("ambil", username, password,
                                daritanggal, sampaitanggal, kode_loket.get(0), terpilih);
                    }
                }

            else{
                    Toast.makeText(Laporan.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                } }
        });

    }



    @Override
    public void onBackPressed() {
        if(adaInternet()) {
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(Laporan.this).execute("login", username, password);
        }else{
            Toast.makeText(Laporan.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }

}
