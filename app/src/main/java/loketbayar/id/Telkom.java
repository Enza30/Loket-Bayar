package loketbayar.id;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Telkom extends AppCompatActivity implements PrintingCallback {
    private String dataTelkom = "", keterangan = "", registrationDate = "",username,password,msn = "", powerpurchasede = "",
            jsontask3 = "", no_reff = "",responseCode, message, nama,totalTagihan,
            tokenNumber, biayaMeterai, ppn, ppj, angsuran, rpToken, kwh,
            arraynya = "",kode_produk_database="", lembarTagihanTotal,kode_data_produk_utk_api="", productCode,
            refID, periode = "", registrationNumber = "", transactionName = "",datauser, tagnya="",id,email,namecontact,
            phoneNo,kodeArea,divre,datel,jumlahTagihan,TAG = "Contacts",tipe, idpelanggan, token = "",FeeSchema,FeeBulanan;
    private Button btPrint, btPdf,btCheck,btSelesai,btshare,btBayar;
    private LinearLayout llCetak,llData;
    Printing printing;
    private ImageView ivPrint,ivBack, ivRefresh,ivIcon7;
    private float admins = 0, totalbayar = 0, tagTelkoms = 0,adminbiller = 0, kembali = 0,pengurangan = 0, lemtag = 0,
            harga_loket = 0,admawal = 3000,sals, taggs = 0,as = 20000;
    public static String dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", admin_biller = "", fee_sub_ca = "", fee_sub_ca_1 = "", fee_sub_ca_2 = "", fee_sub_ca_3 = "";
    public static boolean udahceksaldo=false;
    public static float saldobayar = 0,saldoawal=0,total_tagihan_api=0;
    private ArrayList<String> powerPurchaseDenom = new ArrayList<>();
    ArrayList<String> meterAwal = new ArrayList<>();
    ArrayList<String> meterAkhir = new ArrayList<>();
    ArrayList<String> periodes = new ArrayList<>();
    ArrayList<String> nilaiTagihans = new ArrayList<>();
    ArrayList<String> dendas = new ArrayList<>();
    ArrayList<String> adminarray = new ArrayList<>();
    ArrayList<String> totals = new ArrayList<>();
    ArrayList<Integer> fee = new ArrayList<>();
    ArrayList<String> periode_array = new ArrayList<>();
    ArrayList<Integer> nilaiTagihan_array = new ArrayList<>();
    ArrayList<Integer> admin_array = new ArrayList<>();
    ArrayList<Integer> total_array = new ArrayList<>();
    ArrayList<Integer> fee_array = new ArrayList<>();
    PdfWriter writer;
    Spinner spTipe, spNominal;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvJudulIdPel,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul,
            tvJudulNama,tvJuduls,tvTotalTagihanjudul,tvTotalTagihan,tvNilaiTagihanjudul,tvNilaiTagihan,
            tvAdmJudul,tvAdmin,tvhrgloket;
    private SharedPreferences boyprefs;
    private int nilaiTagihan = 0, denda = 0, admin = 0, total = 0, nilaitag = 0 ;
    ArrayList<String> listtipe = new ArrayList<>();
    ArrayList<String> jsonnya = new ArrayList<>();
    private DecimalFormat format = new DecimalFormat("###,###,###.##");
    private EditText etidPelanggan;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int RESULT_PICK_CONTACT = 3;
    private StringBuffer periode_dari_database,nilai_tagihan_dari_database;
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_telkom);
        tvAdmin = findViewById(R.id.tvAdmin);
        tvhrgloket = findViewById(R.id.tvhrgloket);
        tvTotalTagihanjudul = findViewById(R.id.tvTotalTagihanjudul);
        tvTotalTagihan = findViewById(R.id.tvTotalTagihan);
        tvNilaiTagihanjudul = findViewById(R.id.tvNilaiTagihanjudul);
        tvNilaiTagihan = findViewById(R.id.tvNilaiTagihan);
        tvAdmJudul = findViewById(R.id.tvAdmJudul);
        ivIcon7 = findViewById(R.id.ivIcon7);
        tvJudullembar = findViewById(R.id.tvJudullembar);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        tvJudulIdPel = findViewById(R.id.tvJudulIdPel);
        btPrint = findViewById(R.id.btPrint);
        tvJuduls = findViewById(R.id.tvJuduls);
        btPdf = findViewById(R.id.btPdf);
        btSelesai = findViewById(R.id.btSelesai);
        Printooth.INSTANCE.init(this);
        tvTotaljudul = findViewById(R.id.tvTotaljudul);
        tvrefidJudul = findViewById(R.id.tvrefidJudul);
        tvJudulNama = findViewById(R.id.tvJudulNama);
        llData = findViewById(R.id.llData);
        llCetak = findViewById(R.id.llCetak);
        ivPrint = findViewById(R.id.ivPrint);
        btshare = findViewById(R.id.btshare);
        btshare.setVisibility(View.GONE);
        tvJuduls.setText("TELKOM");
        llCetak.setVisibility(View.GONE);
        spTipe = findViewById(R.id.spTipe);
        spNominal = findViewById(R.id.spNominal);
        btBayar = findViewById(R.id.btBayar);
        tvidPelanggan = findViewById(R.id.tvidPelanggan);
        boyprefs.edit().remove("mDeviceAddress").apply();
        tvNama = findViewById(R.id.tvNama);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);
        tvRef = findViewById(R.id.tvRef);
        tvTagPln = findViewById(R.id.tvTagPln);
        tvNamas = findViewById(R.id.tvNamas);
        ivBack = findViewById(R.id.ivBack);
        etidPelanggan = findViewById(R.id.etidPelanggan);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        tvLembarTagihan = findViewById(R.id.tvLembarTagihan);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivRefresh = findViewById(R.id.ivRefresh);
        JSONObject categoryJSONObj = null;
        btCheck = findViewById(R.id.btCheck);
        listtipe.add("-Silahkan Pilih -");
        listtipe.add("TELKOM PSTN");
        listtipe.add("TELKOM SPEEDY / INDIHOME");
        listtipe.add("TELKOM FLEXI");
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
        sals = Float.parseFloat(saldo.get(0));
        saldobayar= Float.parseFloat(saldo.get(0));
        saldoawal= Float.parseFloat(saldo.get(0));
        Log.e("saldobayar","s:"+saldobayar);
        tvSaldo.setText("Rp." + decimalFormat.format(sals));
        tvNama.setText(nama_pemilik.get(0));
        String username=boyprefs.getString("username","");
        String password=boyprefs.getString("password","");
        new AmbilSaldoTask(Telkom.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                new LoginTask(Telkom.this).execute("login", username, password);
            }else{
                Toast.makeText(Telkom.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }
            }
        });
        boyprefs.edit().remove("Telkom").apply();
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    tvSaldo.setText("");
                String username=boyprefs.getString("username","");
                String password=boyprefs.getString("password","");
                new AmbilSaldoTask(Telkom.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Telkom.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setVisibility(View.VISIBLE);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    btshare.setVisibility(View.GONE);
               clearall();
                periode_dari_database=new StringBuffer();
                llCetak.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
                idpelanggan = etidPelanggan.getText().toString();
                Log.e("dataproduk",dataproduk);

                if (idpelanggan.isEmpty()) {
                    Dialog dialog=new Dialog(Telkom.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Masukkan Id Pelanggan");
                    tvTanya.setText("Pesan");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
                    if (tipe.equalsIgnoreCase("TELKOM PSTN")) {
                        new AmbilTelkomTasks2(Telkom.this)
                                .execute(getString(R.string.link)+"apis/android/ppob/inquiry");
                    } else if (tipe.equalsIgnoreCase("TELKOM SPEEDY / INDIHOME")) {
                        new AmbilTelkomTasks2(Telkom.this)
                                .execute(getString(R.string.link)+"apis/android/ppob/inquiry");
                    }
                    else  if (tipe.equalsIgnoreCase("TELKOM FLEXI"))  {
                        new AmbilTelkomTasks2(Telkom.this)
                                .execute(getString(R.string.link)+"apis/android/ppob/inquiry");
                    }else{
                        Dialog dialog=new Dialog(Telkom.this);
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
                    Toast.makeText(Telkom.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
                tipe = String.valueOf(parent.getItemAtPosition(position));
                llCetak.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
                btBayar.setVisibility(View.VISIBLE);
//                etidPelanggan.setText("");
                tvNamas.setText("");
                tvidPelanggan.setText("");
                tvRef.setText("");
                tvLembarTagihan.setText("");
                tvTagPln.setText("");
                tvTotalBayar.setText("");
                if (tipe.equalsIgnoreCase("TELKOM PSTN")) {
                    kode_data_produk_utk_api = "TELKOMPSTN";
                    kode_produk_database="PSTN";
                    new AmbilProdukTaskTelkom(Telkom.this).execute("ambil", kode_produk_database,kode_loket.get(0));
                    token = "";
                } if (tipe.equalsIgnoreCase("TELKOM SPEEDY / INDIHOME")) {
                    kode_data_produk_utk_api = "TELKOMSPEEDY";
                    kode_produk_database="INDIHOME";
                    new AmbilProdukTaskTelkom(Telkom.this).execute("ambil", kode_produk_database, kode_loket.get(0));                } else if (tipe.equalsIgnoreCase("Telkom Flexy")) {
                } if (tipe.equalsIgnoreCase("TELKOM FLEXI")) {
                    kode_data_produk_utk_api = "TELKOMFLEXI";
                    kode_produk_database="FLEXI";
                    new AmbilProdukTaskTelkom(Telkom.this).execute("ambil", kode_produk_database, kode_loket.get(0));
                }
                }
                else
                {
                    Toast.makeText(Telkom.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    spTipe.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    Log.e("admin_biller bayar", "adm" + admin_biller);
                    float jumstag = Float.parseFloat(jumlahTagihan);
                    float admin_billers = Float.parseFloat(admin_biller);
                    float fee_billers = Float.parseFloat(fee_biller);
                    float fee_apps = Float.parseFloat(fee_app);
                    float fee_cas = Float.parseFloat(fee_ca);
                    float fee_sub_cas = Float.parseFloat(fee_sub_ca);
                    float adms = admin_billers * jumstag;
                    float feebils = Float.parseFloat(fee_biller) * jumstag;
                    float fap = Float.parseFloat(fee_app) * jumstag;
                    float fca = Float.parseFloat(fee_ca) * jumstag;
                    float fsca = Float.parseFloat(fee_sub_ca) * jumstag;
                    float fsca1 = Float.parseFloat(fee_sub_ca_1) * jumstag;
                    float fsca2 = Float.parseFloat(fee_sub_ca_2) * jumstag;
                    float fsca3 = Float.parseFloat(fee_sub_ca_3) * jumstag;
                    float toks = nilaiTagihan;
                    totalbayar = toks + (adms - feebils) + fap + fca + fsca+ fsca1+ fsca2+ fsca3;
                    pengurangan = saldobayar - totalbayar;
                    int totbay = (int) totalbayar;
                    new Telkom.BayarTask(Telkom.this).execute(getString(R.string.link)+"apis/android/ppob/payment");
                }else{
                        Toast.makeText(Telkom.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(Telkom.this);
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
                            Toast.makeText(Telkom.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(Telkom.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivIcon7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    // Android version is lesser than 6.0 or the permission is already granted.
                    selectSingleContact();
                }

            }
        });
    }
    private void selectSingleContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }
    @Override
    public void onBackPressed() {
        if(adaInternet()) {
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(Telkom.this).execute("login", username, password);
        } else{
            Toast.makeText(Telkom.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void connectingWithPrinter() {
        Log.e("printer", "connected");
    }
    @Override
    public void connectionFailed(String s) {
        Log.e("printer", s);
    }
    @Override
    public void onError(String s) {
        Log.e("printer", s);
    }
    @Override
    public void onMessage(String s) {
        Log.e("printer", s);
    }
    @Override
    public void printingOrderSentSuccessfully() {
        Log.e("printer", "sukses");
    }

    @Override
    public void disconnected() {

    }

    public class AmbilTelkomTasks2 extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password;
        private Dialog load, dialogload;
        AmbilTelkomTasks2(Context context) {
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
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("operator", username);
                jsonRequest.put("kode_produk", kode_produk_biller);
                jsonRequest.put("idpel", idpelanggan);
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
            }
            catch (ProtocolException e) {
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
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogload.dismiss();
           Log.e("sTelkom2", "Telkom2:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
                FeeSchema = mainJSONObj.getString("FeeSchema");
                FeeBulanan = mainJSONObj.getString("FeeBulanan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                jsonnya.clear();
                taggs=0;
                powerPurchaseDenom.clear();
                llData.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.VISIBLE);
                dataTelkom = s;
                try {
                    JSONObject mainJSONObj = new JSONObject(dataTelkom);
                    jsonnya.add(mainJSONObj.getString("tagihan"));
                    kodeArea = mainJSONObj.getString("kodeArea");
                    divre = mainJSONObj.getString("divre");
                    datel = mainJSONObj.getString("datel");
                    nama = mainJSONObj.getString("nama");
                    jumlahTagihan = mainJSONObj.getString("jumlahTagihan");
                    totalTagihan = mainJSONObj.getString("totalTagihan");
                    productCode = mainJSONObj.getString("productCode");
                    refID = mainJSONObj.getString("refID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                admin = 0;
                nilaitag = 0;
                for (int x = 0; x < jsonnya.size(); x++) {
                    try {
                        // jsonString is a string variable that holds the JSON
                        JSONArray itemArray = new JSONArray(jsonnya.get(x));
                        for (int i = 0; i < itemArray.length(); i++) {
                            String value = itemArray.getString(i);
                            //   Log.e("json", i+"="+value);
                            periode = PojoMion.AmbilArray2(value, "periode");
                            periode_array.add(periode);
                            nilaiTagihan = Integer.parseInt(PojoMion.AmbilArray2(value, "nilaiTagihan"));
                            nilaiTagihan_array.add(nilaiTagihan);
                            int nilaitagi = Integer.parseInt(PojoMion.AmbilArray2(value, "nilaiTagihan"));
                            nilaitag = nilaitag + nilaitagi;
                            int adm = Integer.parseInt(PojoMion.AmbilArray2(value, "admin"));
                            admin = admin + adm;
                            int totals = Integer.parseInt(PojoMion.AmbilArray2(value, "total"));
                            total_tagihan_api = total_tagihan_api + totals;
                            totalbayar = totalbayar + totals;
                            int fees = Integer.parseInt(PojoMion.AmbilArray2(value, "fee"));
                            fee_array.add(fees);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                tvJudulIdPel.setText("NO PELANGGAN");
                tvidPelanggan.setText(idpelanggan);
                tvJudulNama.setText("KODE AREA");
                tvNamas.setText(kodeArea);
                tvrefidJudul.setText("DIVRE/DATEL");
                tvRef.setText(divre + "/" + datel);
                tvJudullembar.setText("NAMA");
                tvLembarTagihan.setText(nama);
                admins = admin;
                nilaiTagihan = nilaitag;
                tvJudulTag.setText("PERIODE");
                for (int x = 0; x < periode_array.size(); x++) {
                    periode_dari_database.append("").append(periode_array.get(x)).append(" ");
                }
                nilai_tagihan_dari_database = new StringBuffer();
                for (int x = 0; x < periode_array.size(); x++) {
                    float z = nilaiTagihan_array.get(x);
                   nilai_tagihan_dari_database.append("").append("Rp.").append(decimalFormat.format(z)).append(" ");
                }
                tvTagPln.setText(periode_dari_database.toString());
                tvTotaljudul.setText("JUMLAH TAGIHAN");
                float jumtag = Float.parseFloat(jumlahTagihan);
                tvTotalBayar.setText(jumlahTagihan);
                float acccc = nilaitag;
                tvNilaiTagihan.setText("Rp." + decimalFormat.format(acccc));
                float adddd = admin;
                tvAdmin.setText("Rp." + decimalFormat.format(adddd));
                float tottag = Float.parseFloat(totalTagihan);
                tvTotalTagihan.setText("Rp." + decimalFormat.format(tottag));
                float jumstag = Float.parseFloat(jumlahTagihan);
                float admin_billers = Float.parseFloat(admin_biller);
                float adms = admin_billers * jumstag;
                float feebils = Float.parseFloat(fee_biller) * jumstag;
                float fap = Float.parseFloat(fee_app) * jumstag;
                float fca = Float.parseFloat(fee_ca) * jumstag;
                float fsca = Float.parseFloat(fee_sub_ca) * jumstag;
                float fsca1 = Float.parseFloat(fee_sub_ca_1) * jumstag;
                float fsca2 = Float.parseFloat(fee_sub_ca_2) * jumstag;
                float fsca3 = Float.parseFloat(fee_sub_ca_3) * jumstag;
                float toks = nilaiTagihan;
                totalbayar = toks + (adms - feebils) + fap + fca + fsca+ fsca1+ fsca2+ fsca3;

                if (FeeSchema.equalsIgnoreCase("DEPOSIT")) {
                    if (FeeBulanan.equalsIgnoreCase("OFF")) {
                        tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(totalbayar));
                    }
                    else {
                        tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(tottag));
                    }
                }
                else {
                    tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(tottag));

                }


            }else {
//                btBayar.setVisibility(View.GONE);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf = dialog.findViewById(R.id.tvIf);
                TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                Button btTidak = dialog.findViewById(R.id.btTidak);
                tvIf.setText(message);
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
  //                      llData.setVisibility(View.GONE);
                      //  dataTelkom = "";
                        boyprefs.edit().putBoolean("Telkom", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
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
                jsonRequest.put("refid", refID);
                jsonRequest.put("nominal", harga_loket);
                jsonRequest.put("admin", admin);
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
            udahceksaldo=false;
            Log.e("hasilbayar", "s:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("responseCode", mainJSONObj.getString("responseCode"));
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(Telkom.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
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
                tvIf.setText("Transaksi berhasil");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("Telkom", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Telkom.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Telkom.this, ScanningActivity.class),
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
                                        + "/PDF/" + nama + ".pdf");
                                Document document = new Document(PageSize.A4);
                                Uri path = FileProvider.getUriForFile(
                                        Telkom.this,
                                        Telkom.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Tagihan TELKOM");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Pembayaran Tagihan "+tipe+
                                        "a.n "+nama);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                        btshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                float jumtags= Float.parseFloat(jumlahTagihan);
                                float accccc=nilaiTagihan;
                                float addddd=admins;
                                float tottag= Float.parseFloat(totalTagihan);
                                String s = "STRUK PEMBAYARAN TAGIHAN\n"+tipe+"\n\n"+
                                        "NO PELANGGAN : " + idpelanggan+"\n"+
                                        "Nama          : " + nama+"\n\n"+
                                        "DIVRE/DATEL   : " + divre + "/" + datel+"\n"+
                                        "JUMLAH TAGIHAN  : " +decimalFormat.format(jumtags)+"\n"+
                                        "PERIODE            : " + periode_dari_database.toString()+"\n"+
                                        "RP TAGIHAN     : Rp." + decimalFormat.format(accccc)+"\n\n"+
                                        "Telkom Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah"+"\n\n"+
                                        "ADMIN BANK   : Rp." + decimalFormat.format(addddd)+"\n"+
                                        "TOTAL BAYAR  : Rp." + decimalFormat.format(tottag)+"\n"+
                                        "Terima Kasih"+"\n\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan "+tipe+
                                        "a.n "+nama);
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
                        boyprefs.edit().putBoolean("Telkom", true).apply();
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
    void initview() {
        if (printing != null) {
            printing.setPrintingCallback(this);
        }
    }
    private void printdata() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN TAGIHAN\n"+tipe)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO PELANGGAN : " + idpelanggan)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("Nama         : " + nama)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("DIVRE/DATEL  : " + divre + "/" + datel)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float jumtags= Float.parseFloat(jumlahTagihan);
        printables.add(new TextPrintable.Builder()
                .setText("JML TAGIHAN  : " +decimalFormat.format(jumtags) )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PERIODE      : " + periode_dari_database.toString())
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
      float accccc=nilaiTagihan;
        printables.add(new TextPrintable.Builder()
                .setText("RP TAGIHAN   : Rp." + decimalFormat.format(accccc))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Telkom Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float addddd=admins;
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK   : Rp." + decimalFormat.format(addddd))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float tottag= Float.parseFloat(totalTagihan);
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR  : Rp." + decimalFormat.format(tottag))
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
        cv.add("STRUK PEMBAYARAN TAGIHAN\n "+tipe+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(140);
        tgl.setIndentationRight(140);
        tgl.add("NO PELANGGAN    : " + idpelanggan + "\n");
        tgl.add("Nama                       : " + nama + "\n");
        tgl.add("DIVRE/DATEL         : " + divre + "/" + datel + "\n");
        float jumtags= Float.parseFloat(jumlahTagihan);
        tgl.add("JUMLAH TAGIHAN : " + decimalFormat.format(jumtags)+ "\n");
        tgl.add("PERIODE                : " + periode_dari_database.toString() + "\n");
        tgl.add("NILAI TAGIHAN      : Rp." + decimalFormat.format(nilaiTagihan) + "\n");
        tgl.add("\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("Telkom Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph admin = new Paragraph();
        admin.setAlignment(Element.ALIGN_LEFT);
        admin.setIndentationLeft(140);
        admin.setIndentationRight(140);
        admin.add("ADMIN BANK     : Rp." + decimalFormat.format(admins) + "\n");
        admin.add("TOTAL BAYAR   : Rp." + decimalFormat.format(total_tagihan_api) + "\n");
        admin.add("\n");
        document.add(admin);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForResult
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("ContactFragment", "Failed to pick contact");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                selectSingleContact();
            } else {
                Toast.makeText(this, "Harap terima Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void contactPicked(Intent data) {
        Uri uri = data.getData();
        Log.i(TAG, "contactPicked() uri " + uri.toString());
        Cursor cursor;
        ContentResolver cr = getContentResolver();
        try {
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (null != cur && cur.getCount() > 0) {
                cur.moveToFirst();
                for (String column : cur.getColumnNames()) {
                    Log.i(TAG, "contactPicked() Contacts column " + column + " : " + cur.getString(cur.getColumnIndex(column)));
                }
            }
            if (cur.getCount() > 0) {
                //Query the content uri
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (null != cursor && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (String column : cursor.getColumnNames()) {
                        Log.i(TAG, "contactPicked() uri column " + column + " : " + cursor.getString(cursor.getColumnIndex(column)));
                    }
                }
                cursor.moveToFirst();
                id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Log.i(TAG, "contactPicked() uri id " + id);
                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                Log.i(TAG, "contactPicked() uri contact id " + contact_id);
                // column index of the contact name
                namecontact = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // column index of the phone number
                phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //get Email id of selected contact....
                Log.e("ContactsFragment", "::>> " + id + namecontact + phoneNo);
                @SuppressLint("Recycle") Cursor cur1 = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contact_id}, null);
                if (null != cur1 && cur1.getCount() > 0) {
                    cur1.moveToFirst();
                    for (String column : cur1.getColumnNames()) {
                        Log.i(TAG, "contactPicked() Email column " + column + " : " + cur1.getString(cur1.getColumnIndex(column)));
                        email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    //HERE YOU GET name, phoneno & email of selected contact from contactlist.....
                    phoneNo=phoneNo.replace("-","").replace("+62","0")
                            .replace(" ","");
                    phoneNo=phoneNo.trim();
                    etidPelanggan.setText(phoneNo);
                } else {
                    phoneNo=phoneNo.replace("-","").replace("+62","0")
                            .replace(" ","");;
                    phoneNo=phoneNo.trim();
                    etidPelanggan.setText(phoneNo);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    void clearall(){
        llData.setVisibility(View.GONE);
   //     periode="";nilaiTagihan="";periode_array="";nilaiTagihan_array="";
//tvTagPln="";tvNilaiTagihan="";tvTotalTagihan="";
        keterangan="";totalTagihan="";
        nilaiTagihan_array .clear();
        periode_array .clear();
        total_tagihan_api=0;
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