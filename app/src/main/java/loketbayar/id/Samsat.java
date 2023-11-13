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
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Samsat extends AppCompatActivity implements PrintingCallback {
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
    ArrayList<String> nilaiTagihan_array = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    ArrayList<String> listtipe = new ArrayList<>();
    ArrayList<String> jsonnya = new ArrayList<>();
    private float tagihan = 0, admin = 0, total = 0, fee_billers = 0, adminbiller = 0, pengurangan = 0, kembali = 0, totalbayar = 0, harga_loket = 0, total2 = 0;
    public static String dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", admin_biller = "", fee_sub_ca = "";
    public static float saldobayar = 0,saldoawal=0;
    private Button btPrint, btPdf;
    private LinearLayout llCetak,llData;
    Printing printing;
    private PdfWriter writer;
    private int nilaiTagihan=0,nilaitag = 0 ;
    private Button btSelesai,btshare;
    private String datauser, tagnya,tagihan2;
    private EditText etidPelanggan;
    private Button btCheck,btBayar;
    private ImageView ivBack, ivRefresh;
    private EditText etBulan,etHp;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvPelangganJudul,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul, tvJudulNama,tvJuduls;
    private ImageView ivPrint;
    private String idpelanggan="",bulan="",nomerhp="",databpjs="";
    private float admawal = 3000;
    private String responseCode, message, subscriberID, nama, tarif, keterangan="", infoText,tipe;
    Spinner spTipe;
    private SharedPreferences boyprefs;
    private String referensi = "", lembarTagihanTotal, productCode, refID="", periode = "", registrationNumber = "", transactionName = "";
    private String namaCabang="",noVA="",jumlahPeriode="",jumlahPeserta="",detailPeserta="",customerData="",namaluar="" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_samsat);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvJuduls.setText("SAMOLNAS");
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        btPrint = findViewById(R.id.btPrint);
        etidPelanggan = findViewById(R.id.etNoVa);
        etBulan = findViewById(R.id.etBulan);
        etHp = findViewById(R.id.etHp);
        tvPelangganJudul = findViewById(R.id.tvPelangganJudul);
        btshare= findViewById(R.id.btshare);
        btshare.setVisibility(View.GONE);
        btPdf = findViewById(R.id.btPdf);
        btSelesai = findViewById(R.id.btSelesai);
        Printooth.INSTANCE.init(this);
        tvTotaljudul = findViewById(R.id.tvTotaljudul);
        tvrefidJudul = findViewById(R.id.tvrefidJudul);
        tvJudulNama = findViewById(R.id.tvJudulNama);
        llData = findViewById(R.id.llData);
        llCetak = findViewById(R.id.llCetak);
        ivPrint = findViewById(R.id.ivPrint);
        llCetak.setVisibility(View.GONE);
        spTipe = findViewById(R.id.spTipe);
        listtipe.add("-Silahkan Pilih-");
        listtipe.add("BPJS KESEHATAN I");
        listtipe.add("BPJS KESEHATAN II");

        btBayar = findViewById(R.id.btBayar);
        tvidPelanggan = findViewById(R.id.tvidPelanggan);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        boyprefs.edit().remove("bpjs").apply();

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
   //     listtipe.add("BPJS KESEHATAN");


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
        tvNamaLoket.setText(nama_loket.get(0));
              tvKodeloket.setText(kode_loket.get(0));
        saldobayar= Float.parseFloat(saldo.get(0));
        saldoawal= Float.parseFloat(saldo.get(0));
        Log.e("saldobayar","s:"+saldobayar);
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        String username=boyprefs.getString("username","");
        String password=boyprefs.getString("password","");
        new AmbilSaldoTask(Samsat.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                new LoginTask(Samsat.this).execute("login", username, password);
                }
                else{
                    Toast.makeText(Samsat.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
                }
        });
        boyprefs.edit().remove("Samsat").apply();
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                String username=boyprefs.getString("username","");
                String password=boyprefs.getString("password","");
                new AmbilSaldoTask(Samsat.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }
                else{
                    Toast.makeText(Samsat.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
                }
        });
        btBayar.setVisibility(View.VISIBLE);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    btshare.setVisibility(View.GONE);
                noPeserta.clear();
                namaquerydatabpjs.clear();
                premidatabpjs.clear();
                saldodariquerydatabpjs.clear();
                llCetak.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
                bulan=etBulan.getText().toString();
                nomerhp=etHp.getText().toString();
                idpelanggan = etidPelanggan.getText().toString();
                Log.e("dataproduk",dataproduk);

                if (idpelanggan.isEmpty()) {
                    Snackbar.make(v, "Masukan Id Pelanggan", Snackbar.LENGTH_SHORT).show();
                }
                else  if (bulan.isEmpty()) {
                    Snackbar.make(v, "Masukan Bulan", Snackbar.LENGTH_SHORT).show();
                }
                else  if (nomerhp.isEmpty()) {
                    Snackbar.make(v, "Masukan Nomer Hp", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if (tipe.equalsIgnoreCase("BPJS KESEHATAN I"))  {
                    new AmbilBpjsTask(Samsat.this).execute("https://mudahbayar.com/devel/inquiry.php?produk=SAMOLNAS&idpel="+idpelanggan+
                            "&bln="+bulan);
                }
                    else if (tipe.equalsIgnoreCase("BPJS KESEHATAN II"))  {
                        new AmbilBpjsTask(Samsat.this).execute("https://mudahbayar.com/loket/inquiry.php?produk=BPJSKES&idpel="+idpelanggan+
                                "&bln="+bulan+"&nohp="+nomerhp);
                    }

                    else{
                        Dialog dialog=new Dialog(Samsat.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                        TextView tvIf=dialog.findViewById(R.id.tvIf);
                        TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                        Button btTidak=dialog.findViewById(R.id.btTidak);
                        tvIf.setText("Silahkan Pilih Produk");
                        tvTanya.setText("Pesan");
                        btTidak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();


                            }
                        });
                        dialog.show();
                    }

                }
                }
                else{
                    Toast.makeText(Samsat.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initview();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listtipe);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipe.setAdapter(dataAdapter);
        spTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adaInternet()){
                    btshare.setVisibility(View.GONE);
                    noPeserta.clear();
                    namaquerydatabpjs.clear();
                    premidatabpjs.clear();
                    saldodariquerydatabpjs.clear();
                    llCetak.setVisibility(View.GONE);
                    llData.setVisibility(View.GONE);
                    tipe = String.valueOf(parent.getItemAtPosition(position));
                    if (tipe.equalsIgnoreCase("BPJS KESEHATAN I")) {
                        clearall();
                        tagnya = "BPJSKSX";
                        new AmbilProdukBpjsTask(Samsat.this).execute("ambil", tagnya, kode_loket.get(0));
                    }
                    else if (tipe.equalsIgnoreCase("BPJS KESEHATAN II")) {
                        clearall();
                        tagnya = "BPJSKES";
                        new AmbilProdukBpjsTask(Samsat.this).execute("ambil", tagnya, kode_loket.get(0));
                    }
                }
                else{
                    Toast.makeText(Samsat.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    spTipe.setSelection(0);
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
                    Dialog dialog = new Dialog(Samsat.this);
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
                            Toast.makeText(Samsat.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(Samsat.this, "No Printer connected", Toast.LENGTH_SHORT).show();
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
                harga_biller = String.valueOf(c +(a - b));
                harga_loket = Float.parseFloat(harga_biller) + d + e + f;
                pengurangan = saldobayar - harga_loket;
                new AmbilSaldoTasks(Samsat.this).execute("ambil",username,password,
                        kode_loket.get(0), String.valueOf(saldobayar),
                        String.valueOf(harga_loket),String.valueOf(saldoawal));
                }
                else{
                    Toast.makeText(Samsat.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
            new LoginTask(Samsat.this).execute("login", username, password);
        }  else{
            Toast.makeText(Samsat.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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

    public class AmbilBpjsTask extends AsyncTask<String, String, String> {

        private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password, token;
        private Dialog load, dialogload;

        AmbilBpjsTask(Context context) {
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
            String link = params[0];

            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.connect();

                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("username", username);
                jsonRequest.put("password", password);

                // Write Request to output stream to server.
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonRequest.toString());
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
                } else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }
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
            Log.e("databpjs",s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                databpjs=s;
                llData.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.VISIBLE);
                if (tipe.equalsIgnoreCase("BPJS KESEHATAN I")) {
                mulai2();}
                else if (tipe.equalsIgnoreCase("BPJS KESEHATAN II")) {
                    mulai();}
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
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        llData.setVisibility(View.GONE);
                        boyprefs.edit().putBoolean("Samsat", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);

                    }
                });
                dialog.show();
            }
        }

        void mulai() {
            jsonnya.clear();
            Log.e("databpjs", databpjs);
            Log.e("dataproduk", dataproduk);
            try {
                JSONObject mainJSONObj = new JSONObject(databpjs);
                noVA = mainJSONObj.getString("noVA");
                namaluar = mainJSONObj.getString("nama");
                namaCabang = mainJSONObj.getString("namaCabang");
                jumlahPeriode = mainJSONObj.getString("jumlahPeriode");
                jumlahPeserta = mainJSONObj.getString("jumlahPeserta");
                detailPeserta = mainJSONObj.getString("detailPeserta");
                customerData = mainJSONObj.getString("customerData");
                productCode = mainJSONObj.getString("productCode");
                tagihan = Float.parseFloat(mainJSONObj.getString("tagihan"));
                admin = Float.parseFloat(mainJSONObj.getString("admin"));
                total = Float.parseFloat(mainJSONObj.getString("total"));
                refID = mainJSONObj.getString("refID");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                // jsonString is a string variable that holds the JSON
                JSONArray itemArray = new JSONArray(detailPeserta);
                for (int i = 0; i < itemArray.length(); i++) {
                    String value = itemArray.getString(i);
                    //   Log.e("json", i+"="+value);
                    noPeserta.add(PojoMion.AmbilArray2(value, "noPeserta"));
                    namaquerydatabpjs.add(PojoMion.AmbilArray2(value, "nama"));
                    premidatabpjs.add(PojoMion.AmbilArray2(value, "premi"));
                    saldodariquerydatabpjs.add(PojoMion.AmbilArray2(value, "saldo"));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (int x = 0; x < jsonnya.size(); x++) {
                try {
                    JSONObject mainJSONObj = new JSONObject(jsonnya.get(x));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            tvNamas.setText(namaluar);
            tvJudullembar.setText("JUMLAH PERIODE");
            tvLembarTagihan.setText(jumlahPeriode);
            tvrefidJudul.setText("JUMLAH PESERTA");
            tvRef.setText(jumlahPeserta);
            tvJudulTag.setText("JUMLAH TAGIHAN");
            float tagihannns=tagihan;
            tvTagPln.setText("Rp."+decimalFormat.format(tagihannns));
            tvPelangganJudul.setText("ADMIN");
            tvidPelanggan.setText("Rp."+decimalFormat.format(admin));
            tvTotaljudul.setText("TOTAL BAYAR");
            tvTotalBayar.setText("Rp."+decimalFormat.format(total));
            tvRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialogpeserta=new Dialog(context);
                    dialogpeserta.setContentView(R.layout.dialogpeserta);
                    Window window = dialogpeserta.getWindow();
                    assert window != null;
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.TOP;
                    wlp.verticalMargin= (float) 0.0769;
                    wlp.flags=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(wlp);
                    dialogpeserta.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    RecyclerView ivPeserta=dialogpeserta.findViewById(R.id.ivPeserta);
                    PesertaAdapter boyiadapter2=new PesertaAdapter(Samsat.this,noPeserta,namaquerydatabpjs,premidatabpjs,saldodariquerydatabpjs);
                    boyiadapter2.notifyDataSetChanged();
                    ivPeserta.setAdapter(boyiadapter2);
                    dialogpeserta.show();
                }
            });
            tvrefidJudul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialogpeserta=new Dialog(context);
                    dialogpeserta.setContentView(R.layout.dialogpeserta);
                    Window window = dialogpeserta.getWindow();
                    assert window != null;
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.TOP;
                    wlp.verticalMargin= (float) 0.0769;
                    wlp.flags=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(wlp);
                    dialogpeserta.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    RecyclerView ivPeserta=dialogpeserta.findViewById(R.id.ivPeserta);
                    PesertaAdapter boyiadapter2=new PesertaAdapter(Samsat.this,noPeserta,namaquerydatabpjs,premidatabpjs,saldodariquerydatabpjs);
                    boyiadapter2.notifyDataSetChanged();
                    ivPeserta.setAdapter(boyiadapter2);
                    dialogpeserta.show();
                }
            });

        }

        void mulai2() {
            jsonnya.clear();
            tagihan2="";
            Log.e("databpjs", databpjs);
            Log.e("dataproduk", dataproduk);
            try {
                JSONObject mainJSONObj = new JSONObject(databpjs);
                noVA = mainJSONObj.getString("noVA");
                namaluar = mainJSONObj.getString("namaPeserta");
               namaCabang = mainJSONObj.getString("namaCabang");
                jumlahPeriode = mainJSONObj.getString("jumlahBulan");
                jumlahPeserta = mainJSONObj.getString("jumlahPeserta");
            //    detailPeserta = mainJSONObj.getString("detailPeserta");
            //    customerData = mainJSONObj.getString("customerData");
                productCode = mainJSONObj.getString("productCode");

                tagihan2 = mainJSONObj.getString("tagihan");
         //       admin = Float.parseFloat(mainJSONObj.getString("admin"));
                total = Float.parseFloat(mainJSONObj.getString("totalTagihan"));
                refID = mainJSONObj.getString("refID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonnya.clear();
            try {
                // jsonString is a string variable that holds the JSON
                JSONArray itemArray = new JSONArray(tagihan2);
                for (int i = 0; i < itemArray.length(); i++) {
                    String value = itemArray.getString(i);
                    Log.e("json", i + "=" + value);
                    jsonnya.add(value);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tagihan = 0;
            admin=0;
            for (int x = 0; x < jsonnya.size(); x++) {
            try {
                JSONObject mainJSONObj = new JSONObject(jsonnya.get(x));

                           //   Log.e("json", i+"="+value);
                    int adm = Integer.parseInt(mainJSONObj.getString("admin"));
                    admin = admin + adm;
             //       nilaiTagihan = Integer.parseInt(PojoMion.AmbilArray2(value, "nilaiTagihan"));
               //     nilaiTagihan_array.add(nilaiTagihan);
                    int nilaitagi = Integer.parseInt(mainJSONObj.getString("nilaiTagihan"));
                    tagihan = tagihan + nilaitagi;
                }

            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }}


            tvNamas.setText(namaluar);
            tvJudullembar.setText("JUMLAH PERIODE");
            tvLembarTagihan.setText(jumlahPeriode);
            tvrefidJudul.setText("JUMLAH PESERTA");
            tvRef.setText(jumlahPeserta);
            tvJudulTag.setText("JUMLAH TAGIHAN");
            float tagihannns=tagihan;
            tvTagPln.setText("Rp."+decimalFormat.format(tagihannns));
            tvPelangganJudul.setText("ADMIN");
            tvidPelanggan.setText("Rp."+decimalFormat.format(admin));
            tvTotaljudul.setText("TOTAL BAYAR");
            tvTotalBayar.setText("Rp."+decimalFormat.format(total));


        }

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

            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(linkbayar);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.connect();

                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("username", "s");
                jsonRequest.put("password", "s");

                // Write Request to output stream to server.
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonRequest.toString());
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
                } else {
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_SHORT).show();
                }
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
                Log.e("responseCode", mainJSONObj.getString("responseCode"));
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
                if (tipe.equalsIgnoreCase("BPJS KESEHATAN I")) {
                    referensi = mainJSONObj.getString("reff");}
                else if (tipe.equalsIgnoreCase("BPJS KESEHATAN II")) {
                    referensi = mainJSONObj.getString("noReferensi");
                }
                infoText = mainJSONObj.getString("info");
                customerData = mainJSONObj.getString("customerData");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (responseCode.equalsIgnoreCase("00")) {
                keterangan=s;

                int a = (int) (Integer.parseInt(admin_biller));
                int b = (int) (Integer.parseInt(fee_biller));
                int c = (int) tagihan;
                int d = (int) (Integer.parseInt(fee_app));
                int e = (int) (Integer.parseInt(fee_ca));
                int f = (int) (Integer.parseInt(fee_sub_ca));
                harga_biller = String.valueOf(c +(a - b));
                harga_loket = Float.parseFloat(harga_biller) + d + e + f;

                String fee_loket = String.valueOf(total - harga_loket);
                String username = boyprefs.getString("username", "");
                keterangan = s;
                Log.e("message", message);
                int saldoss= (int) pengurangan;
                int feeapp= (int) (Integer.parseInt(fee_app));
                int feeca= (int) (Integer.parseInt(fee_ca));
                int feesubca= (int) (Integer.parseInt(fee_sub_ca));
                Log.e("message", message);
                int g = (int)saldobayar;
                int h = (int)harga_loket;
                int i = g + h;
                int j =  g;
                new SaveTask(context, llCetak,tvSaldo).execute("save", kode_ca.get(0), kode_sub_ca.get(0), kode_loket.get(0), tagnya, "Andro", username,
                        idpelanggan, refID, harga_biller, String.valueOf(harga_loket), String.valueOf(tagihan), String.valueOf(admin), "0",
                        "1",jumlahPeriode, fee_app, fee_ca, fee_sub_ca,fee_loket, String.valueOf(totalbayar), referensi, "sukses", keterangan, String.valueOf(pengurangan),String.valueOf(i), String.valueOf(j));
                llCetak.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.GONE);
                btshare.setVisibility(View.VISIBLE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);

                tvIf.setText("Transaksi Berhasil");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("bpjs", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Samsat.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Samsat.this, ScanningActivity.class),
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
                                String root = Environment.getExternalStorageDirectory().toString();
                                File myDir = new File(root + "/PDF");
                                myDir.mkdirs();
                                File h = new File(Environment.getExternalStorageDirectory().toString()
                                        + "/PDF/" + namaluar + ".pdf");
                                Document document = new Document(PageSize.A4);
                                Uri path = FileProvider.getUriForFile(
                                        Samsat.this,
                                        Samsat.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Tagihan BPJS");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Pembayaran Tagihan BPJS KESEHATAN "+
                                        "a.n "+nama);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                            }
                        });
                        btshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);
                                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                                String formattedDate = df.format(c);
                                String s = "STRUK PEMBAYARAN IURAN\nBPJS KESEHATAN"+"\n"+
                                        "NO REFERENSI   : " + referensi+"\n"+
                                        "TANGGAL : " + formattedDate+"\n"+
                                        "NO VA   : " + noVA+"\n"+
                                        "NAMA    : " +namaluar+"\n"+
                                        "JUMLAH PESERTA : "+jumlahPeserta+" ORANG"+"\n"+
                                        "PERIODE        : "+jumlahPeriode+" BULAN"+"\n"+
                                        "JUMLAH TAGIHAN : Rp."+decimalFormat.format(tagihan)+"\n"+
                                        "BPJS Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah"+"\n"+
                                        "ADMIN BANK     : " + "Rp." + decimalFormat.format(admin)+"\n"+
                                        "TOTAL BAYAR    : " + "Rp." + decimalFormat.format(total)+"\n"+
                                        "Terima Kasih"+"\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Tagihan BPJS KESEHATAN "+
                                        "a.n "+nama);
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
                                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                            }
                        });
                    }
                });
                dialog.show();

            } else {


                kembali = saldobayar + harga_loket;
                new BalikinSaldoTask(Samsat.this).execute("ambil",username,
                        password,kode_loket.get(0),String.valueOf(kembali));
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
                        boyprefs.edit().putBoolean("bpjs", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);

                    }
                });
                dialog.show();
            }

        }

    }

    class AmbilSaldoTasks extends AsyncTask<String, Void, String> {
        private Context ctx;
        private String results;
        ArrayList<String> saldonya = new ArrayList<>();

        AmbilSaldoTasks(Context ctx) {
            this.ctx = ctx;
        }

        private Dialog dialog;
        private Dialog load;
        private String base, login_url;

        ArrayList<String> saldo = new ArrayList<>();
        private SharedPreferences boyprefs;

        @Override
        protected void onPreExecute() {
            boyprefs = ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
            dialog = new Dialog(ctx);
            dialog.setContentView(R.layout.dialogload2);
            dialog.setCancelable(false);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialog.findViewById(R.id.ivX);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url = ctx.getResources().getString(R.string.xyz) + "ambilsaldo2/";

            String method = params[0];
            if (method.equalsIgnoreCase("ambil")) {
                username = params[1];
                password = params[2];
                String kode_loket = params[3];
                String saldobayar = params[4];
                String totalbayars = params[5];
                String saldoawals = params[6];

                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                            URLEncoder.encode("kode_loket", "UTF-8") + "=" + URLEncoder.encode(kode_loket, "UTF-8") + "&" +
                            URLEncoder.encode("saldobayar", "UTF-8") + "=" + URLEncoder.encode(saldobayar, "UTF-8") + "&" +
                            URLEncoder.encode("totalbayar", "UTF-8") + "=" + URLEncoder.encode(totalbayars, "UTF-8") + "&" +
                            URLEncoder.encode("saldoawal", "UTF-8") + "=" + URLEncoder.encode(saldoawals, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String response = "";
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            try {
                if (result.contains("datasaldo")) {
                    results = result;
                    Log.e("datasaldo", result);
                    saldonya = PojoMion.AmbilArray(result, "saldo", "datasaldo");
                    float s = Float.parseFloat(saldonya.get(0));
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
                    tvSaldo.setText("Rp." + decimalFormat.format(s));
                    saldobayar = s;
                    new BayarTask(Samsat.this).execute("https://mudahbayar.com/loket/bayar.php?produk="+tagnya+"&refid="+refID+"&nominal="+total );


                }
                else if(result.contains("uasoq")) {
                    Log.e("uasoq", result);
                    saldonya = PojoMion.AmbilArray(result, "saldo", "uasoq");
                    float s = Float.parseFloat(saldonya.get(0));
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
                    tvSaldo.setText("Rp." + decimalFormat.format(s));
                    saldobayar = s;
                    int a = (int) (Integer.parseInt(admin_biller));
                    int b = (int) (Integer.parseInt(fee_biller));
                    int c = (int) tagihan;
                    int d = (int) (Integer.parseInt(fee_app));
                    int e = (int) (Integer.parseInt(fee_ca));
                    int f = (int) (Integer.parseInt(fee_sub_ca));
                    harga_biller = String.valueOf(c +(a - b));
                    harga_loket = Float.parseFloat(harga_biller) + d + e + f;

                    pengurangan = saldobayar - harga_loket;
                    if (pengurangan < 0) {
                        Dialog dialog = new Dialog(Samsat.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf = dialog.findViewById(R.id.tvIf);
                        TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                        Button btTidak = dialog.findViewById(R.id.btTidak);
                        tvIf.setText("Saldo Tidak Mencukupi");
                        tvTanya.setText("Pesan");
                        btTidak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });
                        dialog.show();
                    } else {
                        new BayarTask(Samsat.this).execute("https://mudahbayar.com/loket/bayar.php?produk="+tagnya+"&refid="+refID+"&nominal="+total );

                    }
                }
                else{
                    Toast.makeText(ctx,"User / Password Salah", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(ctx, Loginpage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    ctx.startActivity(intent);
                    ((Activity)ctx).finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
        //        Toast.makeText(ctx, "eror:" + result, Toast.LENGTH_LONG).show();
                Toast.makeText(ctx,"Gagal Login / Tidak Ada Sinyal", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ctx, Loginpage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ctx.startActivity(intent);
                ((Activity)ctx).finish();
            }
        }
    }

    class BalikinSaldoTask extends AsyncTask<String, Void, String> {
        private Context ctx;
        private String results;
        ArrayList<String> saldonya = new ArrayList<>();

        BalikinSaldoTask(Context ctx) {
            this.ctx = ctx;
        }

        private Dialog dialog;
        private Dialog load;
        private String base, login_url;

        ArrayList<String> saldo = new ArrayList<>();
        private SharedPreferences boyprefs;

        @Override
        protected void onPreExecute() {
            boyprefs = ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
            dialog = new Dialog(ctx);
            dialog.setContentView(R.layout.dialogload2);
            dialog.setCancelable(false);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ivX = dialog.findViewById(R.id.ivX);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url = ctx.getResources().getString(R.string.xyz) + "balikinsaldo/";

            String method = params[0];
            if (method.equalsIgnoreCase("ambil")) {
                String username = params[1];
                String password = params[2];
                String kode_loket = params[3];
                String pengembalian = params[4];


                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                            URLEncoder.encode("kode_loket", "UTF-8") + "=" + URLEncoder.encode(kode_loket, "UTF-8") + "&" +
                            URLEncoder.encode("pengembalian", "UTF-8") + "=" + URLEncoder.encode(pengembalian, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String response = "";
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            try {
                if (result.contains("datasaldo")) {

                    Log.e("datasaldo", result);
                    saldonya = PojoMion.AmbilArray(result, "saldo", "datasaldo");
                    float s = Float.parseFloat(saldonya.get(0));
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
                    tvSaldo.setText("Rp." + decimalFormat.format(s));
                    saldobayar = s;

                } else {
                    Dialog dialog=new Dialog(Samsat.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Server Error");
                    tvTanya.setText("Error!");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            kembali = saldobayar + harga_loket;
                            new BalikinSaldoTask(Samsat.this).execute("ambil",username,
                                    password,kode_loket.get(0),String.valueOf(kembali));
                        }
                    });
                    dialog.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Dialog dialog=new Dialog(Samsat.this);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText("server Error");
                tvTanya.setText(result);
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        kembali = saldobayar + harga_loket;
                        new BalikinSaldoTask(Samsat.this).execute("ambil",username,
                                password,kode_loket.get(0),String.valueOf(kembali));
                    }
                });
                dialog.show();

            }
        }
    }


    public void addMetaData(Document document) {
        document.addTitle("Faktur");
        document.addSubject("Faktur");
        document.addKeywords("Company");
    }


    private void printdata() {

        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN IURAN\nBPJS KESEHATAN")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REFERENSI   : " + referensi)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c);
        printables.add(new TextPrintable.Builder()
                .setText("TANGGAL : " + formattedDate)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO VA   : " + noVA )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA    : " +namaluar)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("JUMLAH PESERTA : "+jumlahPeserta+" ORANG" )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PERIODE        : "+jumlahPeriode+" BULAN" )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("JUMLAH TAGIHAN : Rp."+decimalFormat.format(tagihan) )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("BPJS Menyatakan Struk Ini\nSebagai Bukti Pembayaran\nYang Sah")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK     : " + "Rp." + decimalFormat.format(admin))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR    : " + "Rp." + decimalFormat.format(total))
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC437())
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
        cv.add("STRUK PEMBAYARAN IURAN\nBPJS KESEHATAN\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(140);
        tgl.setIndentationRight(140);
        tgl.add("NO REFERENSI : " + referensi + "\n");
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c);
        tgl.add("TANGGAL : " + formattedDate + "\n");
        tgl.add("NO VA       : " + noVA + "\n");
        tgl.add("NAMA        : "+namaluar+"\n");
        tgl.add("JUMLAH PESERTA : "+jumlahPeserta+" ORANG\n");
        tgl.add("PERIODE                 : " +jumlahPeriode+" BULAN\n");
        tgl.add("JUMLAH TAGIHAN  : RP." +decimalFormat.format(tagihan)+ "\n");
        tgl.add("\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("BPJS Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph admins = new Paragraph();
        admins.setAlignment(Element.ALIGN_LEFT);
        admins.setIndentationLeft(140);
        admins.setIndentationRight(140);
        admins.add("ADMIN BANK         : Rp." + decimalFormat.format(admin) + "\n");
        admins.add("TOTAL BAYAR       : Rp." + decimalFormat.format(total) + "\n");
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");

        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+"\n");
        document.add(infotext);
        document.left(90);
        document.right(90);

    }

    void clearall(){

        idpelanggan="";
        responseCode="";message="";noVA="";nama="";namaCabang="";jumlahPeriode="";jumlahPeserta="";detailPeserta="";customerData="";
        namaquerydatabpjs.clear();
        premidatabpjs.clear();
        saldodariquerydatabpjs.clear();
        noPeserta.clear();
        tagihan = 0;admin = 0;total = 0;fee_billers = 0;adminbiller = 0;saldobayar = 0;pengurangan = 0;
        harga_loket = 0;
        dataproduk="";kode_produk_biller = "";fee_biller = "";fee_app = "";fee_ca = "";
        harga_biller = "";admin_biller = "";fee_sub_ca = "";
        productCode="";
        refID="";

        saldobayar= Float.parseFloat(saldo.get(0));
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
}
