package loketbayar.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;




import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.PairedPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class PrintSetters extends AppCompatActivity implements PrintingCallback {
    Printing printing;
    private SharedPreferences boyprefs;
    TextView tvPrinter;
    private String username,password,datauser;
    private ImageView ivBack,ivRefresh;
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvPelangganJudul,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul,
            tvrefidJudul, tvJudulNama,tvJuduls,tvJumlahTagihan,tvTagihan,tvAdmin;
    private ImageView ivPrint;
    private DecimalFormat decimalFormat;
    private float saldobayar=0;
    private Button btPrint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_print_setters);
        Printooth.INSTANCE.init(this);
        initview();
        decimalFormat=new DecimalFormat("###,###,###.##");
        tvNama = findViewById(R.id.tvNama);
        tvPrinter = findViewById(R.id.tvPrinter);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);
        tvRef = findViewById(R.id.tvRef);
        tvTagPln = findViewById(R.id.tvTagPln);
        btPrint = findViewById(R.id.btPrint);
        tvNamas = findViewById(R.id.tvNamas);
        ivBack = findViewById(R.id.ivBack);
        ivPrint = findViewById(R.id.ivPrint);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        tvLembarTagihan = findViewById(R.id.tvLembarTagihan);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivRefresh = findViewById(R.id.ivRefresh);
        datauser = boyprefs.getString("datauser", "");
        nama_pemilik = PojoMion.AmbilArray(datauser, "nama_pemilik", "dataloket");
        nama_loket = PojoMion.AmbilArray(datauser, "nama_loket", "dataloket");
        kode_loket = PojoMion.AmbilArray(datauser, "kode_loket", "dataloket");

        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        new AmbilSaldoTask(PrintSetters.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        saldobayar= Float.parseFloat(saldo.get(0));
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        tvJuduls.setText("PRINTER SETTING");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                new LoginTask(PrintSetters.this).execute("login", username, password);
                }else{
                    Toast.makeText(PrintSetters.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }  }
        });
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    tvSaldo.setText("");
                username=boyprefs.getString("username","");
                password=boyprefs.getString("password","");
                new AmbilSaldoTask(PrintSetters.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
            }else{
                Toast.makeText(PrintSetters.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }
            }
        });
        if (Printooth.INSTANCE.hasPairedPrinter()) {

            tvPrinter.setText("Printer " + Printooth.INSTANCE.getPairedPrinter().getName() + " Has Connected");


        } else {
            tvPrinter.setText("No Printer Has Connected");

        }
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(PrintSetters.this);
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
                            tvPrinter.setText("");
                            startActivityForResult(new Intent(PrintSetters.this, ScanningActivity.class),
                                    ScanningActivity.SCANNING_FOR_PRINTER);

                            initview();
                        }
                    });
                    dialog.show();
                }else{
                    startActivityForResult(new Intent(PrintSetters.this, ScanningActivity.class),
                            ScanningActivity.SCANNING_FOR_PRINTER);
                    initview();
                }

            }
        });
    }
    void initview() {
        btPrint=findViewById(R.id.btPrint);
        ivPrint=findViewById(R.id.ivPrint);
        tvPrinter=findViewById(R.id.tvPrinter);
        ivPrint.setVisibility(View.GONE);
        if (printing != null) {
            printing.setPrintingCallback(this);
        }
        try{
            tvPrinter.setText(String.format("Printer %s Has Connected", Objects.requireNonNull(Printooth.INSTANCE.getPairedPrinter()).getName()));

        }catch (Exception e){
            e.printStackTrace();
            tvPrinter.setText("No Printer Has Connected");
        }




    }

    @Override
    public void connectingWithPrinter() {
        tvPrinter.setText(String.format("Printer %s Has Connected", Printooth.INSTANCE.getPairedPrinter().getName()));

    }

    @Override
    public void connectionFailed(String s) {
        tvPrinter.setText(String.format("Printer %s Failed to Connect", Printooth.INSTANCE.getPairedPrinter().getName()));

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void printingOrderSentSuccessfully() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();

    }

    @Override
    public void onBackPressed() {
        if(adaInternet()) {
        String username = boyprefs.getString("username", "");
        String password = boyprefs.getString("password", "");
        new LoginTask(PrintSetters.this).execute("login", username, password);
        }else{
            Toast.makeText(PrintSetters.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }

    @Override
    public void disconnected() {

    }
}
