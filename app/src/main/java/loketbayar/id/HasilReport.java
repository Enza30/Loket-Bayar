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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HasilReport extends AppCompatActivity implements PrintingCallback {
    private SharedPreferences boyprefs;
    private Context ctx;
    private String datareport;
    private PdfWriter writer;
    Printing printing;

    private RecyclerView rvList;
    private int ttlhargalkt = 0,ttlfee=0,totalnya = 0,feettl = 0,feettlbln = 0;
            private int ttltrx = 0;
    private ArrayList<String> kode_produk = new ArrayList<>();
    private ArrayList<String> kategori2 = new ArrayList<>();
    private ArrayList<String> inputs = new ArrayList<>();
    private ArrayList<String> keterangan = new ArrayList<>();
    private ArrayList<String> namalap = new ArrayList<>();
    private ArrayList<String> total = new ArrayList<>();
    private ArrayList<String> tagihan = new ArrayList<>();
    private ArrayList<String> admin = new ArrayList<>();
    private ArrayList<String> denda = new ArrayList<>();
    private ArrayList<String> jumlah_transaksi = new ArrayList<>();
    private ArrayList<String> periode = new ArrayList<>();
    private ArrayList<String> no_reff = new ArrayList<>();
    private ArrayList<String> username = new ArrayList<>();
    private ArrayList<String> hargaloket = new ArrayList<>();
    private ArrayList<String> updated = new ArrayList<>();
    private ArrayList<String> update = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();
    private ArrayList<String> daya = new ArrayList<>();
    private ArrayList<String> fee = new ArrayList<>();
    private ArrayList<String> feebulanan = new ArrayList<>();

    StringBuilder hasiltoken;
    private String bulan, tahun, namalapnya, datanya, apinya,tokennya,tarifnya,dayanya;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo,tvJuduls,tvSiap,tvGagal,tvLunas,tvGagalBayar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_hasil_report);
        rvList = findViewById(R.id.rvList);
        tvSiap = findViewById(R.id.tvSiap);
        tvGagal = findViewById(R.id.tvGagal);
        tvLunas = findViewById(R.id.tvLunas);
        tvGagalBayar = findViewById(R.id.tvGagalBayar);

        datareport = boyprefs.getString("datareport", "");
        bulan = boyprefs.getString("bulan", "");
        tahun = boyprefs.getString("tahun", "");
        kode_produk = PojoMion.AmbilArray(datareport, "nama_produk", "datareport");
        kategori2 = PojoMion.AmbilArray(datareport, "kategori2", "datareport");
        inputs = PojoMion.AmbilArray(datareport, "inputs", "datareport");
        keterangan = PojoMion.AmbilArray(datareport, "keterangan", "datareport");
        total = PojoMion.AmbilArray(datareport, "total", "datareport");
        tagihan = PojoMion.AmbilArray(datareport, "tagihan", "datareport");
        admin = PojoMion.AmbilArray(datareport, "admin", "datareport");
        denda = PojoMion.AmbilArray(datareport, "denda", "datareport");
        jumlah_transaksi = PojoMion.AmbilArray(datareport, "jumlah_transaksi", "datareport");
        periode = PojoMion.AmbilArray(datareport, "periode", "datareport");
        no_reff = PojoMion.AmbilArray(datareport, "no_reff", "datareport");
        username = PojoMion.AmbilArray(datareport, "username", "datareport");
        hargaloket = PojoMion.AmbilArray(datareport, "harga_loket", "datareport");
        update = PojoMion.AmbilArray(datareport, "created_on", "datareport");
        kode_loket = PojoMion.AmbilArray(datareport, "kode_loket", "datareport");
        nama_loket = PojoMion.AmbilArray(datareport, "nama_loket", "kualitas");
        status = PojoMion.AmbilArray(datareport, "status", "datareport");
        fee = PojoMion.AmbilArray(datareport, "fee_loket", "datareport");
        feebulanan = PojoMion.AmbilArray(datareport, "fee_bulanan", "datareport");


        for(int x=0;x<inputs.size();x++) {

            if (status.get(x).equalsIgnoreCase("GAGAL")) {
                namalap.add("-");
            }
            else {
                if (keterangan.get(x).isEmpty()) {
                    namalap.add("-");
                }
                else {
                    namalapnya = "";
                    try {
                        JSONObject mainJSONObj = new JSONObject(keterangan.get(x));
                        apinya = mainJSONObj.getString("api");

                    } catch (JSONException a) {
                        a.printStackTrace();

                    }
                    try {
                        JSONObject mainJSONObj = new JSONObject(apinya);
                        namalapnya = mainJSONObj.getString("nama");
                        namalap.add(namalapnya);
                    } catch (JSONException as) {
                        as.printStackTrace();
                    }


                    if (namalapnya.isEmpty()) {
                        datanya = "";
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(x));
                            apinya = mainJSONObj.getString("api");

                        } catch (JSONException a) {
                            a.printStackTrace();

                        }
                        try {
                            JSONObject mainJSONObj = new JSONObject(apinya);
                            datanya = mainJSONObj.getString("data");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        try {
                            JSONObject mainJSONObj = new JSONObject(datanya);
                            namalapnya = mainJSONObj.getString("nama");
                            namalap.add(namalapnya);
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }


                        if (namalapnya.isEmpty()) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(x));
                                apinya = mainJSONObj.getString("api");

                            } catch (JSONException a) {
                                a.printStackTrace();

                            }
                            try {
                                JSONObject mainJSONObj = new JSONObject(apinya);
                                namalapnya = mainJSONObj.getString("namaPeserta");
                                namalap.add(namalapnya);
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }


                            if (namalapnya.isEmpty()) {
                                try {
                                    JSONObject mainJSONObj = new JSONObject(keterangan.get(x));
                                    apinya = mainJSONObj.getString("api");

                                } catch (JSONException a) {
                                    a.printStackTrace();

                                }
                                try {
                                    JSONObject mainJSONObj = new JSONObject(apinya);
                                    namalapnya = mainJSONObj.getString("customerName");
                                    namalap.add(namalapnya);
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                                if (namalapnya.isEmpty()) {
                                    try {
                                        JSONObject mainJSONObj = new JSONObject(keterangan.get(x));
                                        apinya = mainJSONObj.getString("api");

                                    } catch (JSONException a) {
                                        a.printStackTrace();

                                    }
                                    try {
                                        JSONObject mainJSONObj = new JSONObject(apinya);
                                        namalapnya = mainJSONObj.getString("nama_penerima");
                                        namalap.add(namalapnya);
                                    } catch (JSONException as) {
                                        as.printStackTrace();
                                    }

                                    if (namalapnya.isEmpty()) {
                                        namalap.add("-");
                                    }
                                }
                            }
                        }
                    }
                }



            }

        }



        ReportAdapter boyiadapter2=new ReportAdapter(HasilReport.this,kode_produk,kategori2,inputs,total,tagihan,admin,denda,jumlah_transaksi,
                periode,no_reff,username,hargaloket,update,keterangan,kode_loket,nama_loket,status,namalap,fee,feebulanan);
        boyiadapter2.notifyDataSetChanged();
        boyiadapter2.notifyDataSetChanged();
        rvList.setAdapter(boyiadapter2);
        for(int x=0;x<total.size();x++){
  //          float ttl = Float.parseFloat(total.get(x));
  //          float hrglkt= Float.parseFloat(hargaloket.get(x));
            int ttltrx2= Integer.parseInt(jumlah_transaksi.get(x));
            ttltrx=ttltrx2+ttltrx;
 //           float fee= ttl-hrglkt;

            int d = Integer.parseInt(total.get(x)) ;
            totalnya = totalnya + d;
            int e = Integer.parseInt(hargaloket.get(x)) ;
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
            ttlhargalkt=ttlhargalkt+e;
           // ttltrx++;

            int f = Integer.parseInt(fee.get(x)) ;
            feettl = feettl + f;
            int g = Integer.parseInt(feebulanan.get(x)) ;
            feettlbln=feettlbln+g;
            ttlfee=feettl+feettlbln;


            tvSiap.setText(decimalFormat.format(totalnya));
            tvGagal.setText(decimalFormat.format(ttlhargalkt));
            tvLunas.setText(String.valueOf(ttltrx));
            tvGagalBayar.setText(decimalFormat.format(ttlfee));


        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HasilReport.this, Laporan.class));
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
