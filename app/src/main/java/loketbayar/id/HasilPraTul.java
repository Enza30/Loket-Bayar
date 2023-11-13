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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HasilPraTul extends AppCompatActivity implements PrintingCallback {
    private SharedPreferences boyprefs;
    private Context ctx;
    private String datareport;
    private PdfWriter writer;
    Printing printing;

    private RecyclerView rvList;
    private int ttlhargalkt = 0,ttlfee=0,totalnya = 0,feettl = 0,feettlbln = 0;
            private int ttltrx = 0;
    private ArrayList<String> kode_produk = new ArrayList<>();
    private ArrayList<String> alamat = new ArrayList<>();
    private ArrayList<String> inputs = new ArrayList<>();
    private ArrayList<String> tarif = new ArrayList<>();
    private ArrayList<String> namalap = new ArrayList<>();
    private ArrayList<String> daya = new ArrayList<>();
    private ArrayList<String> tagihan = new ArrayList<>();
    private ArrayList<String> gol = new ArrayList<>();
    private ArrayList<String> denda = new ArrayList<>();
    private ArrayList<String> jumlah_transaksi = new ArrayList<>();
    private ArrayList<String> periode = new ArrayList<>();
    private ArrayList<String> no_rbm = new ArrayList<>();
    private ArrayList<String> username = new ArrayList<>();
    private ArrayList<String> hargaloket = new ArrayList<>();
    private ArrayList<String> updated = new ArrayList<>();
    private ArrayList<String> update = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();
    private ArrayList<String> fee = new ArrayList<>();
    private ArrayList<String> feebulanan = new ArrayList<>();

    StringBuilder hasiltoken;
    private String bulan, tahun, namalapnya, datanya, apinya,tokennya,tarifnya,dayanya;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo,tvJuduls,tvSiap,tvGagal,tvLunas,tvGagalBayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_hasil_pratul);
        rvList = findViewById(R.id.rvList);
        tvSiap = findViewById(R.id.tvSiap);
        tvGagal = findViewById(R.id.tvGagal);
        tvLunas = findViewById(R.id.tvLunas);
        tvGagalBayar = findViewById(R.id.tvGagalBayar);

        datareport = boyprefs.getString("datareport", "");
        bulan = boyprefs.getString("bulan", "");
        tahun = boyprefs.getString("tahun", "");
        kode_produk = PojoMion.AmbilArray(datareport, "daya", "datareport");
        hargaloket = PojoMion.AmbilArray(datareport, "daya", "datareport");
        nama_loket = PojoMion.AmbilArray(datareport, "daya", "kualitas");
        status = PojoMion.AmbilArray(datareport, "daya", "datareport");
        fee = PojoMion.AmbilArray(datareport, "daya", "datareport");
        feebulanan = PojoMion.AmbilArray(datareport, "daya", "datareport");

        alamat = PojoMion.AmbilArray(datareport, "alamat", "datareport");
        tarif = PojoMion.AmbilArray(datareport, "tarif", "datareport");
        daya = PojoMion.AmbilArray(datareport, "daya", "datareport");
        gol = PojoMion.AmbilArray(datareport, "gol", "datareport");
        namalap = PojoMion.AmbilArray(datareport, "nama", "datareport");
        no_rbm = PojoMion.AmbilArray(datareport, "no_rbm", "datareport");
        username = PojoMion.AmbilArray(datareport, "username", "datareport");
        kode_loket = PojoMion.AmbilArray(datareport, "kode_loket", "datareport");
        periode = PojoMion.AmbilArray(datareport, "bl_awal_bl_akhir", "datareport");
        jumlah_transaksi = PojoMion.AmbilArray(datareport, "lembar", "datareport");
        denda = PojoMion.AmbilArray(datareport, "rpbk", "datareport");
        tagihan = PojoMion.AmbilArray(datareport, "rptag", "datareport");
        inputs = PojoMion.AmbilArray(datareport, "inputs", "datareport");
        update = PojoMion.AmbilArray(datareport, "created_on", "datareport");

        PraTulAdapter boyiadapter2=new PraTulAdapter(HasilPraTul.this,kode_produk,alamat,inputs,daya,tagihan,gol,denda,jumlah_transaksi,
                periode,no_rbm,username,hargaloket,update,tarif,kode_loket,nama_loket,status,namalap,fee,feebulanan);
        boyiadapter2.notifyDataSetChanged();
        boyiadapter2.notifyDataSetChanged();
        rvList.setAdapter(boyiadapter2);
        for(int x=0;x<daya.size();x++){
            int ttltrx2= Integer.parseInt(tagihan.get(x));
            ttltrx=ttltrx2+ttltrx;

            int d = Integer.parseInt(denda.get(x)) ;
            totalnya = totalnya + d;

            int e = Integer.parseInt(jumlah_transaksi.get(x)) ;
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
            ttlhargalkt=ttlhargalkt+e;

            feettl++;

            tvSiap.setText(decimalFormat.format(ttltrx));
            tvGagal.setText(decimalFormat.format(totalnya));
            tvLunas.setText(String.valueOf(ttlhargalkt));
            tvGagalBayar.setText(decimalFormat.format(feettl));
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(HasilPraTul.this, Home.class));
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