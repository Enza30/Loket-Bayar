package loketbayar.id;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.pdf.PdfWriter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HasilMutasi extends AppCompatActivity implements PrintingCallback {
    private SharedPreferences boyprefs;
    private Context ctx;
    private String datareport,kualitas;
    private PdfWriter writer;
    Printing printing;

    private RecyclerView rvList;
    private int ttlhargalkt = 0,ttlfee=0,totalnya = 0,saldoakhr = 0,saldoakhrr = 0;
    private int ttltrx = 0;
    private ArrayList<String> tipe = new ArrayList<>();
    private ArrayList<String> kredit = new ArrayList<>();
    private ArrayList<String> keterangan = new ArrayList<>();
    private ArrayList<String> debet = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> update = new ArrayList<>();


    StringBuilder hasiltoken;
    private String bulan, tahun, namalapnya, datanya, apinya,tokennya,tarifnya,dayanya;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo,tvJuduls,tvSiap,tvGagal,tvLunas,tvGagalBayar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_hasil_mutasi);
        rvList = findViewById(R.id.rvList);
        tvSiap = findViewById(R.id.tvSiap);
        tvGagal = findViewById(R.id.tvGagal);
        tvLunas = findViewById(R.id.tvLunas);
        tvGagalBayar = findViewById(R.id.tvGagalBayar);
      //  kualitas = boyprefs.getString("kualitas", "");
        datareport = boyprefs.getString("datareport", "");
        bulan = boyprefs.getString("bulan", "");
        tahun = boyprefs.getString("tahun", "");
        keterangan = PojoMion.AmbilArray(datareport, "keterangan", "datareport");
        debet = PojoMion.AmbilArray(datareport, "debet", "datareport");
        kredit = PojoMion.AmbilArray(datareport, "kredit", "datareport");
        tipe = PojoMion.AmbilArray(datareport, "debet", "datareport");
        saldo = PojoMion.AmbilArray(datareport, "saldo", "datareport");
        nama_loket = PojoMion.AmbilArray(datareport, "kode_loket", "datareport");
        kode_loket = PojoMion.AmbilArray(datareport, "kode_loket", "datareport");
        update = PojoMion.AmbilArray(datareport, "created_on", "datareport");




        MutasiAdapter boyiadapter2=new MutasiAdapter(HasilMutasi.this,tipe,debet,keterangan,kredit,saldo,nama_loket,kode_loket,update);
        boyiadapter2.notifyDataSetChanged();
        boyiadapter2.notifyDataSetChanged();
        rvList.setAdapter(boyiadapter2);

        for(int x=0;x<kredit.size();x++) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");

           int ttl = Integer.parseInt(debet.get(x));
           int hrglkt = Integer.parseInt(kredit.get(x));
            int saldoawl = Integer.parseInt(saldo.get(x));
            saldoakhr=saldoawl+ttl-hrglkt;

            int e = Integer.parseInt(kredit.get(x));
            ttlhargalkt = ttlhargalkt + e;

            int d = Integer.parseInt(debet.get(x));
            totalnya = totalnya + d;


            tvSiap.setText(decimalFormat.format(totalnya));
            tvGagal.setText(decimalFormat.format(ttlhargalkt));
     //       tvLunas.setText(String.valueOf(x));
       //     tvGagalBayar.setText(decimalFormat.format(saldoakhr));

        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HasilMutasi.this, Laporan.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();
    }
    void initview() {
        if (printing != null) {
            printing.setPrintingCallback(this);
        }


    }

    @Override
    public void connectingWithPrinter() {

    }

    @Override
    public void connectionFailed(String s) {

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
    public void disconnected() {

    }
}
