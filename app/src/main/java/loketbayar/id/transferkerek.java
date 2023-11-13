package loketbayar.id;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
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
import loketbayar.id.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class transferkerek extends AppCompatActivity implements PrintingCallback {
    private String username,password;
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private ArrayList<String> noPeserta = new ArrayList<>();
    private ArrayList<String> namaquerydatabpjs = new ArrayList<>();
    private ArrayList<String> premidatabpjs = new ArrayList<>();
    private ArrayList<String> saldodariquerydatabpjs = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    ArrayList<String> listtipe = new ArrayList<>();
    ArrayList<String> jsonnya = new ArrayList<>();
    private float tagihan = 0, admin = 0, total = 0, fee_billers = 0, adminbiller = 0, pengurangan = 0, kembali = 0, totalbayar = 0, harga_loket = 0;
    public static String dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", admin_biller = "", fee_sub_ca = "", fee_sub_ca_1 = "", fee_sub_ca_2 = "", fee_sub_ca_3 = "";
    public static float saldobayar = 0,saldoawal=0;
    private Button btPrint, btPdf;
    private LinearLayout llCetak,llData;
    Printing printing;
    private PdfWriter writer;
    private Button btSelesai;
    private String datauser, tagnya;
    private EditText etidPelanggan;
    private Button btCheck,btBayar,btshare;
    private ImageView ivBack, ivRefresh;
    private EditText etBulan,etHp,etBulan3,etBulan2;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvPelangganJudul,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul, tvJudulNama,tvJuduls;
    private ImageView ivPrint;
    private String idpelanggan="",bulan="",nomerhp="",databpjs="",bulan3="",bulan2="";
    private float admawal = 3000;
    private String errCode, message, subscriberID, nama, tarif, keterangan="", infoText,tipe;
    Spinner spTipe;
    private SharedPreferences boyprefs;
    private String referensi = "", lembarTagihanTotal, productCode, refID, periode = "", registrationNumber = "", transactionName = "";
    private String namaCabang="",noVA="",jumlahPeriode="",jumlahPeserta="",detailPeserta="",customerData="",namaluar="" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_trfkerek);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvJuduls.setText("TRANSFER KE REK");
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        btPrint = findViewById(R.id.btPrint);
        etidPelanggan = findViewById(R.id.etNoVa);
        etBulan = findViewById(R.id.etBulan);
        etBulan2 = findViewById(R.id.etBulan2);
        etBulan3 = findViewById(R.id.etBulan3);
        etHp = findViewById(R.id.etHp);
        tvPelangganJudul = findViewById(R.id.tvPelangganJudul);
        btPdf = findViewById(R.id.btPdf);
        btshare = findViewById(R.id.btshare);
        btSelesai = findViewById(R.id.btSelesai);
        Printooth.INSTANCE.init(this);
        tvTotaljudul = findViewById(R.id.tvTotaljudul);
        tvrefidJudul = findViewById(R.id.tvrefidJudul);
        tvJudulNama = findViewById(R.id.tvJudulNama);
        llData = findViewById(R.id.llData);
        llCetak = findViewById(R.id.llCetak);
        ivPrint = findViewById(R.id.ivPrint);
        llCetak.setVisibility(View.GONE);
   //     btshare.setVisibility(View.GONE);
        spTipe = findViewById(R.id.spTipe);
        listtipe.add("-Silahkan Pilih-");
        listtipe.add("BANK BRI");
        listtipe.add("BANK BNI");
        listtipe.add("BANK BCA");
        listtipe.add("BANK MANDIRI");
        listtipe.add("BANK SYARIAH INODONESIA");
        listtipe.add("BANK BPD JATENG");
        listtipe.add("BANK BPD DIY");
        listtipe.add("BANK CIMB NIAGA");
        listtipe.add("BANK CIMB MEGA");
        listtipe.add("BANK PERMATA");
        listtipe.add("BANK OCBC NISP");
        listtipe.add("BANK BTN");
        listtipe.add("BANK DANAMON");
        listtipe.add("BANK KB BUKOPIN");
        listtipe.add("BANK MUAMALAT");
        listtipe.add("BANK BPD JATENG SYARIAH");
        listtipe.add("BANK BPD DIY SYARIAH");
        listtipe.add("BANK BTN SYARIAH");
        listtipe.add("BANK CIMB NIAGA SYARIAH");
        listtipe.add("BANK PERMATA SYARIAH");
        listtipe.add("BANK DANAMON SYARIAH");
        listtipe.add("BANK KB BUKOPIN SYARIAH");
        listtipe.add("BANK MEGA SYARIAH");
        btBayar = findViewById(R.id.btBayar);
        tvidPelanggan = findViewById(R.id.tvidPelanggan);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
       boyprefs.edit().remove("transferkerek").apply();
        tvNama = findViewById(R.id.tvNama);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);
        tvRef = findViewById(R.id.tvRef);
        tvTagPln = findViewById(R.id.tvTagPln);
        tvNamas = findViewById(R.id.tvNamas);
        ivBack = findViewById(R.id.ivBack);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        tvLembarTagihan = findViewById(R.id.tvLembarTagihan);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivRefresh = findViewById(R.id.ivRefresh);
        JSONObject categoryJSONObj = null;
        btCheck = findViewById(R.id.btCheck);
       //   listtipe.add("BPJS KESEHATAN");
        btBayar.setVisibility(View.VISIBLE);
        // tvAdmin.setText("Rp."+format.format(admins));
        datauser = boyprefs.getString("datauser", "");
        nama_pemilik = PojoMion.AmbilArray(datauser, "nama_pemilik", "dataloket");
        nama_loket = PojoMion.AmbilArray(datauser, "nama_loket", "dataloket");
        kode_loket = PojoMion.AmbilArray(datauser, "kode_loket", "dataloket");
        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        kode_ca = PojoMion.AmbilArray(datauser, "kode_ca", "dataloket");
        kode_sub_ca = PojoMion.AmbilArray(datauser, "kode_sub_ca", "dataloket");
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
        saldobayar= Float.parseFloat(saldo.get(0));
        saldoawal= Float.parseFloat(saldo.get(0));
        Log.e("saldobayar","s:"+saldobayar);
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        String username=boyprefs.getString("username","");
        String password=boyprefs.getString("password","");
        new AmbilProduktrfkerekTask(transferkerek.this).execute("ambil", "TRFKEREK", kode_loket.get(0));
        new AmbilSaldoTask(transferkerek.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(transferkerek.this).execute("login", username, password);
                }
                else{
                    Toast.makeText(transferkerek.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
                    new AmbilSaldoTask(transferkerek.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }
                else{
                    Toast.makeText(transferkerek.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setVisibility(View.VISIBLE);
        llData.setVisibility(View.GONE);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adaInternet()) {
                    noPeserta.clear();
                    namaquerydatabpjs.clear();
                    premidatabpjs.clear();
                    saldodariquerydatabpjs.clear();
                    llCetak.setVisibility(View.GONE);
                    llData.setVisibility(View.GONE);
                    bulan = etBulan.getText().toString();
                    bulan3 = etBulan3.getText().toString();
                    bulan2 = etBulan2.getText().toString();
                    nomerhp = etHp.getText().toString();
                    idpelanggan = etidPelanggan.getText().toString();
                    Log.e("dataproduk", dataproduk);

                    if (idpelanggan.isEmpty()) {
                        Snackbar.make(v, "Masukan Nomor Rek", Snackbar.LENGTH_SHORT).show();
                    }
                     else if (nomerhp.isEmpty()) {
                        Snackbar.make(v, "Masukan Atas Nama", Snackbar.LENGTH_SHORT).show();
                    }
                     else if (bulan.isEmpty()) {
                        Snackbar.make(v, "Masukan Nominal", Snackbar.LENGTH_SHORT).show();
                    }
                     else if (bulan3.isEmpty()) {
                        Snackbar.make(v, "Masukan Keterangan", Snackbar.LENGTH_SHORT).show();
                    }
                     else if (bulan2.isEmpty()) {
                        Snackbar.make(v, "Masukan Admin", Snackbar.LENGTH_SHORT).show();
                    }
                     else {
                        float hg= Float.parseFloat(bulan);
                        if (tipe.equalsIgnoreCase("-Silahkan Pilih-")) {
                            Dialog dialog = new Dialog(transferkerek.this);
                            dialog.setContentView(R.layout.dialogok);
                            dialog.setCancelable(false);
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            TextView tvIf = dialog.findViewById(R.id.tvIf);
                            TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                            Button btTidak = dialog.findViewById(R.id.btTidak);
                            tvIf.setText("Silahkan Pilih Bank");
                            tvTanya.setText("Pesan");
                            btTidak.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                        else if (hg<10000) {
                            Snackbar.make(v, "Masukan Nominal Minimal 10.000", Snackbar.LENGTH_SHORT).show();
                        }
                        else if (hg>2500000) {
                            Snackbar.make(v, "Masukan Nominal Maksimal 2.500.000", Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            int a = (int) (Integer.parseInt(admin_biller));
                            int b = (int) (Integer.parseInt(fee_biller));
                            int d = (int) (Integer.parseInt(fee_app));
                            int e = (int) (Integer.parseInt(fee_ca));
                            int f = (int) (Integer.parseInt(fee_sub_ca));
                            int fsca1 = (int) (Integer.parseInt(fee_sub_ca_1));
                            int fsca2 = (int) (Integer.parseInt(fee_sub_ca_2));
                            int fsca3 = (int) (Integer.parseInt(fee_sub_ca_3));
                            harga_loket = Integer.parseInt(bulan) + (a - b)+d + e + f+fsca1+fsca2+fsca3;
                            new transferkerek.BayarTask(transferkerek.this).execute(getString(R.string.link)+"apis/android/transfer");      }
                    }
                }
                else {
                    Toast.makeText(transferkerek.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
            });
        initview();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item, listtipe);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipe.setAdapter(dataAdapter);
        spTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adaInternet()){
                    tipe = String.valueOf(parent.getItemAtPosition(position));
                    if (tipe.equalsIgnoreCase("BPJS KESEHATAN")) {
                        clearall();
                        tagnya = "BPJSKES";
                        new AmbilProdukBpjsTask(transferkerek.this).execute("ambil", tagnya, kode_loket.get(0));
                    } }
                else{
                    Toast.makeText(transferkerek.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    tipe = String.valueOf(parent.getItemAtPosition(0));
                    tagnya = "-Silahkan Pilih-";
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(transferkerek.this);
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
                            Toast.makeText(transferkerek.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(transferkerek.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    Log.e("admin_biller bayar","adm"+admin_biller);
                    totalbayar= total;
                    int totbay = (int) totalbayar;
                    int a = (int) (Integer.parseInt(admin_biller));
                    int b = (int) (Integer.parseInt(fee_biller));
                    int c = (int) tagihan;
                    int d = (int) (Integer.parseInt(fee_app));
                    int e = (int) (Integer.parseInt(fee_ca));
                    int f = (int) (Integer.parseInt(fee_sub_ca));
                    int fsca1 = (int) (Integer.parseInt(fee_sub_ca_1));
                    int fsca2 = (int) (Integer.parseInt(fee_sub_ca_2));
                    int fsca3 = (int) (Integer.parseInt(fee_sub_ca_3));
                    harga_biller = String.valueOf(c +(a - b));
                    harga_loket = Float.parseFloat(harga_biller) + d + e + f+fsca1+fsca2+fsca3;
                    pengurangan = saldobayar - harga_loket;
                    new transferkerek.BayarTask(transferkerek.this).execute(getString(R.string.link)+"apis/android/ppob/payment");
                }
                else{
                    Toast.makeText(transferkerek.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    public void onBackPressed() {
        if(adaInternet()) {
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(transferkerek.this).execute("login", username, password);
        }  else{
            Toast.makeText(transferkerek.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();
    }

    @Override
    public void disconnected() {

    }

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
            String key2 = "54574da5e2e24a4fafdc24914c13ea61c230de65";
            String secret = "asjkrue*$djasfl134213";
            String secret2 = "KwPARmdhy5thGAfAcNkrzfXNyRaGZw1L";
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
                jsonRequest.put("kode_produk", kode_produk_biller);
                jsonRequest.put("nomor_rekening", idpelanggan);
                jsonRequest.put("bank", tipe);
                jsonRequest.put("nama_penerima", nomerhp);
                jsonRequest.put("nominal", bulan);
                jsonRequest.put("admin", bulan2);
                jsonRequest.put("berita", bulan3);
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
            Log.e("hasilbayar", "s:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("errCode", mainJSONObj.getString("errCode"));
                errCode = mainJSONObj.getString("errCode");
                message = mainJSONObj.getString("msg");
                } catch (JSONException e) {
                e.printStackTrace();
            }
            if (errCode.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(transferkerek.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                llCetak.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.GONE);
                btCheck.setVisibility(View.GONE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("transferkerek", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(transferkerek.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(transferkerek.this, ScanningActivity.class),
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
                                        + "/PDF/" + nomerhp + ".pdf");
                                Document document = new Document(PageSize.A5);
                                Uri path = FileProvider.getUriForFile(
                                        transferkerek.this,
                                        transferkerek.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TRANSFER KE REKENING");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "TRANSFER KE REKENING "+
                                "NO REKENING : " + idpelanggan);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                        btshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                String formattedDate = df.format(c);
                                float niltags = Float.parseFloat(bulan);
                                float nom = Float.parseFloat(String.valueOf(bulan));
                                float adm2= Float.parseFloat(String.valueOf(bulan2));
                                float tot = nom+adm2;
                                float adm = Float.parseFloat(String.valueOf(bulan2));
                                String s = "STRUK BUKTI TRANSFER\nKE REKENING"+"\n\n"+
                                        "TGL TRANSFER  : " + formattedDate + "\n" +
                                        "BANK                 : " + tipe+"\n"+
                                        "NO REKENING  : " + idpelanggan+"\n"+
                                        "NM PENERIMA : " +nomerhp+"\n"+
                                        "KETERANGAN  : "+ bulan3+"\n"+
                                        "NOMINAL          : Rp."+decimalFormat.format(niltags)+"\n\n"+
                                        "ADMIN               : " + "Rp." + decimalFormat.format(adm)+"\n"+
                                        "TOTAL BAYAR   : " + "Rp." + decimalFormat.format(tot)+"\n\n"+
                                        "Terima Kasih"+"\n\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+"\n"+
                                        "PT. SINERGI INTI KUALITAS" + "\n" +
                                        "MudahBayar" ;
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TRANSFER KE REKENING "+
                                        "NO REKENING : " + idpelanggan);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
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
                        boyprefs.edit().putBoolean("transferkerek", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
    }
    public void addMetaData(Document document) {
        document.addTitle("Struk MB");
        document.addSubject("Mudah Bayar");
        document.addKeywords("Mudah Bayar");
    }
    private void printdata() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        float nom = Float.parseFloat(String.valueOf(bulan));
        float adm2= Float.parseFloat(String.valueOf(bulan2));
        float tot = nom+adm2;
        float niltags = Float.parseFloat(bulan);
        float adm = Float.parseFloat(String.valueOf(bulan2));
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK BUKTI TRANSFER\nKE REKENING")
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
                .setText("TGL TRANSFER : " + formattedDate)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA BANK    : " + tipe)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REKENING  : " + idpelanggan )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA PENERIMA: " +nomerhp)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("KETERANGAN   : "+ bulan3)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NOMINAL      : Rp."+decimalFormat.format(niltags) )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN        : " + "Rp." + decimalFormat.format(adm))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR  : " + "Rp." + decimalFormat.format(tot))
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
                .setText(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("PT. SINERGI INTI KUALITAS")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("MudahBayar")
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
        cv.add("STRUK BUKTI TRANSFER\nKE REKENING\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        Date c = Calendar.getInstance().getTime();
        float nom = Float.parseFloat(String.valueOf(bulan));
        float adm2= Float.parseFloat(String.valueOf(bulan2));
        float tot = nom+adm2;
        float niltags = Float.parseFloat(bulan);
        float adm = Float.parseFloat(String.valueOf(bulan2));
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String formattedDate = df.format(c);
        tgl.add("TGL TRANSFER : " + formattedDate + "\n");
        tgl.add("NAMA BANK       : " + tipe + "\n");
        tgl.add("NO REKENING   : " + idpelanggan +"\n");
        tgl.add("NAMA                  : "+nomerhp+"\n");
        tgl.add("KETERANGAN   : "+bulan3+"\n");
        tgl.add("NOMINAL           : RP." +decimalFormat.format(niltags)+ "\n\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        document.add(keterangan);
        Paragraph admins = new Paragraph();
        admins.setAlignment(Element.ALIGN_LEFT);
        admins.setIndentationLeft(60);
        admins.setIndentationRight(60);
        admins.add("ADMIN                : Rp." + decimalFormat.format(adm) + "\n");
        admins.add("TOTAL BAYAR   : Rp." + decimalFormat.format(tot) + "\n");
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+"\n");
        infotext.add("PT. SINERGI INTI KUALITAS" + "\n");
        infotext.add("MudahBayar" + "\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    void clearall(){
        idpelanggan="";
        errCode="";message="";noVA="";nama="";namaCabang="";jumlahPeriode="";jumlahPeserta="";detailPeserta="";customerData="";
        namaquerydatabpjs.clear();
        premidatabpjs.clear();
        saldodariquerydatabpjs.clear();
        noPeserta.clear();
        tagihan = 0;admin = 0;total = 0;fee_billers = 0;adminbiller = 0;saldobayar = 0;pengurangan = 0;
        harga_loket = 0;
        dataproduk="";kode_produk_biller = "";fee_biller = "";fee_app = "";fee_ca = "";
        harga_biller = "";admin_biller = "";fee_sub_ca = ""; fee_sub_ca_1 = ""; fee_sub_ca_2 = ""; fee_sub_ca_3 = "";
        productCode="";
        refID="";
        saldobayar= Float.parseFloat(saldo.get(0));
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
    private String getHash(final String key, final String data) {
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
}