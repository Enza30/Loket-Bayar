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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Pdam extends AppCompatActivity implements PrintingCallback {
    StringBuilder mtrawal_tagihan_untuk_data;
    StringBuilder mtrakhir_tagihan_untuk_data;
    StringBuilder pemakaianawal;
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private ArrayList<String> namaproduklist = new ArrayList<>();
    private ArrayList<String> kodeproduklist = new ArrayList<>();
    private ArrayList<String> kode_produk_biller_list = new ArrayList<>();
    private ArrayList<String> admin_biller_list = new ArrayList<>();
    private ArrayList<String> harga_biller_list = new ArrayList<>();
    private ArrayList<String> fee_biller_list = new ArrayList<>();
    private ArrayList<String> fee_app_list = new ArrayList<>();
    private ArrayList<String> denomlist = new ArrayList<>();
    ArrayList<String> periode=new ArrayList<>();
    ArrayList<String> pemakaian=new ArrayList<>();
    ArrayList<String> meterAwal=new ArrayList<>();
    ArrayList<String> meterAkhir=new ArrayList<>();
    ArrayList<String> nilaiTagihan=new ArrayList<>();
    ArrayList<String> penalty=new ArrayList<>();
    ArrayList<String> admin=new ArrayList<>();
    ArrayList<String> total=new ArrayList<>();
    ArrayList<String> tarif=new ArrayList<>();
    ArrayList<String> alamat=new ArrayList<>();
    ArrayList<String> fee=new ArrayList<>();
    ArrayList<String> tagihanlain=new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private ImageView ivIcon;
    public static String datapdam="", dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", admin_biller = "", fee_sub_ca = "", fee_sub_ca_1 = "", fee_sub_ca_2 = "", fee_sub_ca_3 = "";
    public static float saldobayar = 0,saldoawal=0;
    private Button btPrint, btPdf;
    private LinearLayout llData,llCetak;
    Printing printing;
    private String idpel,pilihantipeterpilih="",kodeprodukterpilih="",datalist,denomterpilih,apinya = "",detilTagihan="",FeeSchema,FeeBulanan;
    private PdfWriter writer;
    private Button btSelesai;
    private String datauser, kodeproduk,dari,username,password;
    private EditText etHape;
    ArrayList<String> listtipe = new ArrayList<>();
    private Button btCheck,btBayar,btshare;
    private ImageView ivBack, ivRefresh;
    private Dialog load;
    private float harga_loket=0,kembali = 0;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvPelangganJudul,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul,
            tvrefidJudul, tvJudulNama,tvJuduls,tvJumlahTagihan,tvTagihan,tvAdmin,tvhrgloket;
    private ImageView ivPrint;
    private String datapaska="",namaproduk="";
    private ArrayList<String> jsonnya=new ArrayList<>();
    private String responseCodeQuery1, messagequery1, kodeAreaquery1, namaquery1, divrequery1,
            keterangan="", datelquery1,jmltagihanquery1,totalTagihanquery1,productCodequery1,refIDquery1,tagihanquey1untukarray,idPelquery1;
    private float tagihanLainquery1=0,nilaiTagihanquery1=0,adminquery1=0,pemakaianquery1=0,penaltyquery1=0,
            totalquery1=0,feequeryquery1=0,tarifquery1=0;
    private String responseCodeQuerybayar, messagequeryQuerybayar,idpelQuerybayar,namaquerybayar, periodequerybayar="",
            jmltagihanquerybayar,tagihanquerybayar,totalTagihan,refquerbayar="";
    private float pengurangan=0,totalbayar=0;
    private SharedPreferences boyprefs;
    private Spinner spTipe;
    private StringBuffer penalty_data,meterdata,tagihan_lain_data,periode_data,tagihan_data,admin_data,pemakaian_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_pdam);
        tvJumlahTagihan = findViewById(R.id.tvJumlahTagihan);
        tvTagihan = findViewById(R.id.tvTagihan);
        tvAdmin = findViewById(R.id.tvAdmin);
        tvhrgloket = findViewById(R.id.tvhrgloket);
        tvJuduls = findViewById(R.id.tvJuduls);
        llCetak = findViewById(R.id.llCetak);
        ivIcon = findViewById(R.id.ivIcon);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvJuduls.setText("PDAM");
        dari=boyprefs.getString("dari","");
   //     tvJuduls.setText(dari);
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        btPrint = findViewById(R.id.btPrint);
        etHape = findViewById(R.id.etHp);
        tvPelangganJudul = findViewById(R.id.tvPelangganJudul);
        btshare = findViewById(R.id.btshare);
        btPdf = findViewById(R.id.btPdf);
        btSelesai = findViewById(R.id.btSelesai);
        Printooth.INSTANCE.init(this);
        tvTotaljudul = findViewById(R.id.tvTotaljudul);
        tvrefidJudul = findViewById(R.id.tvrefidJudul);
        tvJudulNama = findViewById(R.id.tvJudulNama);
        llData = findViewById(R.id.llData);
        ivPrint = findViewById(R.id.ivPrint);
        spTipe = findViewById(R.id.spTipe);
        btBayar = findViewById(R.id.btBayar);
        tvidPelanggan = findViewById(R.id.tvidPelanggan);
        boyprefs.edit().remove("Pdam").apply();
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
        btCheck = findViewById(R.id.btCheck);
        btshare.setVisibility(View.GONE);
        datalist=boyprefs.getString("datalist","");
        Log.e("datalist",datalist);
        namaproduklist= PojoMion.AmbilArray(datalist,"nama_produk","datalist");
        kodeproduklist= PojoMion.AmbilArray(datalist,"kode_produk","datalist");
        denomlist= PojoMion.AmbilArray(datalist,"denom","datalist");
        kode_produk_biller_list= PojoMion.AmbilArray(datalist,"kode_produk_biller","datalist");
        admin_biller_list= PojoMion.AmbilArray(datalist,"admin_biller","datalist");
        harga_biller_list= PojoMion.AmbilArray(datalist,"harga_biller","datalist");
        fee_biller_list= PojoMion.AmbilArray(datalist,"fee_biller","datalist");
        fee_app_list= PojoMion.AmbilArray(datalist,"fee_app","datalist");
        admin_biller_list.add(0,"-1");
        harga_biller_list.add(0,"-1");
        fee_biller_list.add(0,"-1");
        fee_app_list.add(0,"-1");
        kode_produk_biller_list.add(0,"-1");
        kodeproduklist.add(0,"-1");
        denomlist.add(0,"-1");
        namaproduklist.add(0,"-Silahkan Pilih-");
        btBayar.setVisibility(View.VISIBLE);
        // tvAdmin.setText("Rp."+format.format(admins));
        datauser = boyprefs.getString("datauser", "");
        nama_pemilik = PojoMion.AmbilArray(datauser, "nama_pemilik", "dataloket");
        nama_loket = PojoMion.AmbilArray(datauser, "nama_loket", "dataloket");
        kode_loket = PojoMion.AmbilArray(datauser, "kode_loket", "dataloket");
        saldo = PojoMion.AmbilArray(datalist, "saldo", "datasaldo");
        kode_ca = PojoMion.AmbilArray(datauser, "kode_ca", "dataloket");
        kode_sub_ca = PojoMion.AmbilArray(datauser, "kode_sub_ca", "dataloket");
        btBayar.setVisibility(View.VISIBLE);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);

        llData.setVisibility(View.GONE);
        saldobayar= Float.parseFloat(saldo.get(0));
        saldoawal= Float.parseFloat(saldo.get(0));
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        new AmbilSaldoTask(Pdam.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(Pdam.this).execute("login", username, password);
                }else{
                    Toast.makeText(Pdam.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    tvSaldo.setText("");
                    username=boyprefs.getString("username","");
                    password=boyprefs.getString("password","");
                    new AmbilSaldoTask(Pdam.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(Pdam.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initview();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, namaproduklist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipe.setAdapter(dataAdapter);
        spTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adaInternet())   {
                clearall3();
                clearall2();
                llCetak.setVisibility(View.GONE);
                btshare.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
                pilihantipeterpilih= String.valueOf(parent.getItemAtPosition(position));
                int pos=namaproduklist.indexOf(pilihantipeterpilih);
                kodeproduk=kodeproduklist.get(pos);
                kodeprodukterpilih=kodeproduklist.get(pos);
                denomterpilih=denomlist.get(pos);
                kode_produk_biller=kode_produk_biller_list.get(pos);
                admin_biller=admin_biller_list.get(pos);
                harga_biller=harga_biller_list.get(pos);
                fee_biller=fee_biller_list.get(pos);
                fee_app=fee_app_list.get(pos);
                if(!pilihantipeterpilih.equalsIgnoreCase("-Silahkan Pilih-")){
                    new AmbilProdukTaskPDAM(Pdam.this).execute("ambil", kodeproduk, kode_loket.get(0));
                }
                }
                else
                {
                    Toast.makeText(Pdam.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    spTipe.setSelection(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btCheck.setText("PROSES");
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    btshare.setVisibility(View.GONE);
                    clearall2();
                    idpel=etHape.getText().toString();
                    llCetak.setVisibility(View.GONE);
                    if(idpel.isEmpty()){
                        llData.setVisibility(View.GONE);
                        btBayar.setVisibility(View.GONE);
                        Dialog dialog=new Dialog(Pdam.this);
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
                    }else{
                        if(!pilihantipeterpilih.equalsIgnoreCase("-Silahkan Pilih-")){
                            Log.e("pilihanterpilih","pt: "+pilihantipeterpilih);
                            new AProdatmarOvodkkTasks(Pdam.this)
                                    .execute(getString(R.string.link)+"apis/android/ppob/inquiry");
                        }else {
                            llData.setVisibility(View.GONE);
                            btBayar.setVisibility(View.GONE);
                            Dialog dialog=new Dialog(Pdam.this);
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
                }else{
                    Toast.makeText(Pdam.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(Pdam.this);
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
                            Toast.makeText(Pdam.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(Pdam.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    int jumtag= Integer.parseInt(jmltagihanquery1);
                    float adms= Float.parseFloat(admin_biller);
                    float feebils= Float.parseFloat(fee_biller);
                    float fap= Float.parseFloat(fee_app);
                    float fca= Float.parseFloat(fee_ca);
                    float fsca= Float.parseFloat(fee_sub_ca);
                    float fsca1= Float.parseFloat(fee_sub_ca_1);
                    float fsca2= Float.parseFloat(fee_sub_ca_2);
                    float fsca3= Float.parseFloat(fee_sub_ca_3);
                    harga_loket = nilaiTagihanquery1 +penaltyquery1+((adms*jumtag) - (feebils*jumtag)) + (fap*jumtag) +
                            (fca*jumtag)+(fsca*jumtag)+(fsca1*jumtag)+(fsca2*jumtag)+(fsca3*jumtag);
                    username=boyprefs.getString("username","");
                    password=boyprefs.getString("password","");
                    pengurangan = saldobayar - harga_loket;
                    new Pdam.BayarTask(Pdam.this).execute(getString(R.string.link)+"apis/android/ppob/payment");
                }else{
                    Toast.makeText(Pdam.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        if(adaInternet()){
        String username = boyprefs.getString("username", "");
        String password = boyprefs.getString("password", "");
        new LoginTask(Pdam.this).execute("login", username, password);
        }else{
            Toast.makeText(Pdam.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();
    }
    void clearall(){
        etHape.setText("");
        llData.setVisibility(View.GONE);
        saldobayar= Float.parseFloat(saldo.get(0));
    }
    void clearall3(){
     //   etHape.setText("");
        llData.setVisibility(View.GONE);
        saldobayar= Float.parseFloat(saldo.get(0));
    }
    void clearall2(){
        //   etHape.setText("");
        llData.setVisibility(View.GONE);
        saldobayar= Float.parseFloat(saldo.get(0));
        nilaiTagihanquery1=0;
        penaltyquery1=0;
        jmltagihanquery1="0";
        adminquery1=0;
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
                jsonRequest.put("refid", refIDquery1);
                jsonRequest.put("nominal", harga_loket);
                jsonRequest.put("admin", adminquery1);
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
            String ref="";
            Log.e("hasilbayar", "s:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("responseCode", mainJSONObj.getString("responseCode"));
                messagequeryQuerybayar=mainJSONObj.getString("message");
                responseCodeQuerybayar=mainJSONObj.getString("responseCode");
                ref=mainJSONObj.getString("reff");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCodeQuerybayar.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(Pdam.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                apinya="";
                detilTagihan="";
                mtrawal_tagihan_untuk_data = new StringBuilder();
                mtrakhir_tagihan_untuk_data = new StringBuilder();
                pemakaianawal = new StringBuilder();
                try {
                    JSONObject mainJSONObj = new JSONObject(s);
                    detilTagihan = mainJSONObj.getString("tagihan");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
                jsonnya.clear();
                try {
                    // jsonString is a string variable that holds the JSON
                    JSONArray itemArray = new JSONArray(detilTagihan);
                    for (int i = 0; i < itemArray.length(); i++) {
                        String value = itemArray.getString(i);
                        Log.e("json", i + "=" + value);
                        jsonnya.add(value);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                for (int x = 0; x < jsonnya.size(); x++) {
                    try {
                        JSONObject mainJSONObj = new JSONObject(jsonnya.get(x));
                        Log.e("isinya", mainJSONObj.getString("periode"));
                        mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                        mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                        pemakaianawal.append(" ").append(mainJSONObj.getString("pemakaian"));

                    }
                    catch(JSONException as){
                        as.printStackTrace();
                    }
                }
                btBayar.setVisibility(View.GONE);
                btshare.setVisibility(View.VISIBLE);
                llData.setVisibility(View.VISIBLE);
                btPrint.setVisibility(View.VISIBLE);
                btPdf.setVisibility(View.VISIBLE);
                llCetak.setVisibility(View.VISIBLE);
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
                                boyprefs.edit().putBoolean("Pdam", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Pdam.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Pdam.this, ScanningActivity.class),
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
                                        + "/PDF/" + namaquery1 + ".pdf");
                                Document document = new Document(PageSize.A5);
                                Uri path = FileProvider.getUriForFile(
                                        Pdam.this,
                                        Pdam.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran PDAM");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Pembayaran Tagihan "+pilihantipeterpilih+
                                        "a.n "+namaquery1);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                        btshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);
                                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
                                String formattedDate = df.format(c);
                                String s = "STRUK PEMBAYARAN TAGIHAN\n"+pilihantipeterpilih+"\n\n"+
                                        "TGL BAYAR : " + formattedDate+"\n"+
                                        "ID PELANGGAN : " + idpel+"\n"+
                                        "NAMA          : " + namaquery1+"\n\n"+
                                        "JML TAGIHAN  : " + jmltagihanquery1+"\n"+
                                        "PERIODE        :" + periode_data+"\n\n"+
                                        "METER           :" + mtrawal_tagihan_untuk_data+" -"+mtrakhir_tagihan_untuk_data+"\n"+
                                        "PEMAKAIAN :" + pemakaianawal+"\n\n"+
                                        "TAGIHAN      : " + tagihan_data+"\n"+
                                        "DENDA          : " + penalty_data+"\n\n"+
                                        "Struk Ini Merupakan Bukti Pembayaran Yang Sah"+"\n\n"+
                                        "ADMIN BANK    : " + admin_data+"\n"+
                                        "TOTAL BAYAR   : Rp."+decimalFormat.format(totalbayar)+"\n\n"+
                                        "Terima Kasih"+"\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan "+pilihantipeterpilih+
                                "a.n "+namaquery1);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                            }
                        });
                    }
                });
                dialog.show();
            }
            else if(responseCodeQuerybayar.equalsIgnoreCase("68")){
                new AmbilSaldoTask(Pdam.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                btBayar.setVisibility(View.GONE);
                llData.setVisibility(View.VISIBLE);
                btshare.setVisibility(View.GONE);
                btPrint.setVisibility(View.GONE);
                btPdf.setVisibility(View.GONE);
                llCetak.setVisibility(View.VISIBLE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText("Transaksi Pending dan Hubungi Admin");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("Pdam", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Pdam.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Pdam.this, ScanningActivity.class),
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
                                        + "/PDF/" + refIDquery1 + ".pdf");
                                Document document = new Document(PageSize.A5);
                                Uri path = FileProvider.getUriForFile(
                                        Pdam.this,
                                        Pdam.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello...");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "refid " + refIDquery1);
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                    }
                });
                dialog.show();
           }
            else {
                llCetak.setVisibility(View.GONE);
                btBayar.setVisibility(View.GONE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(messagequeryQuerybayar);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        boyprefs.edit().putBoolean("Pdam", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
    }
    public class AProdatmarOvodkkTasks extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password, token;
        private Dialog load, dialogload;
        AProdatmarOvodkkTasks(Context context) {
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
                jsonRequest.put("idpel", idpel);
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
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogload.dismiss();
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                responseCodeQuery1 = mainJSONObj.getString("responseCode");
                messagequery1 = mainJSONObj.getString("message");
                FeeSchema = mainJSONObj.getString("FeeSchema");
                FeeBulanan = mainJSONObj.getString("FeeBulanan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCodeQuery1.equalsIgnoreCase("00")) {
                try {
                    JSONObject mainJSONObj = new JSONObject(s);
                    responseCodeQuery1 = mainJSONObj.getString("responseCode");
                    messagequery1 = mainJSONObj.getString("message");
                    namaquery1 = mainJSONObj.getString("nama");
                    jmltagihanquery1 = mainJSONObj.getString("jumlahTagihan");
                    totalTagihanquery1 = mainJSONObj.getString("totalTagihan");
                    productCodequery1 = mainJSONObj.getString("productCode");
                    refIDquery1 = mainJSONObj.getString("refID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                llData.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.VISIBLE);
                datapdam = s;
                Log.e("datapdam",datapdam);
                Log.e("refIDquery1",refIDquery1);
                mulai();
                keterangan = s;
            }
            else {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText(messagequery1);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        llData.setVisibility(View.GONE);
                        datapdam = "";
                        boyprefs.edit().putBoolean("Pdam", true).apply();
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
            //  Log.e("datapdam", datapdam);
            String arraynya="";
            try {
                JSONObject mainJSONObj = new JSONObject(datapdam);
                totalTagihan = mainJSONObj.getString("totalTagihan");
                arraynya = mainJSONObj.getString("tagihan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<String>periods=new ArrayList<>();
            try {
                // jsonString is a string variable that holds the JSON
                JSONArray itemArray = new JSONArray(arraynya);
                for (int i = 0; i < itemArray.length(); i++) {
                    String value = itemArray.getString(i);
                    Log.e("jsonnya", i + "=" + value);
                    jsonnya.add(value);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            periode.clear();
            pemakaian.clear();
            meterAwal.clear();
            meterAkhir.clear();
            nilaiTagihan.clear();
            penalty.clear();
            admin.clear();
            total.clear();
            tarif.clear();
            alamat.clear();
            fee.clear();
            tagihanlain.clear();
            penalty_data=new StringBuffer();
            meterdata=new StringBuffer();
            tagihan_lain_data=new StringBuffer();
            periode_data=new StringBuffer();
            tagihan_data=new StringBuffer();
            admin_data=new StringBuffer();
            for (String hasiljson:jsonnya
            ) {
                try {
                    JSONObject mainJSONObj = new JSONObject(hasiljson);
                    periode.add(mainJSONObj.getString("periode"));
                    pemakaian.add(mainJSONObj.getString("pemakaian"));
                    int pemakaians=mainJSONObj.getInt("pemakaian");
                    pemakaianquery1=pemakaianquery1+pemakaians;
                    meterAwal.add( mainJSONObj.getString("meterAwal"));
                    meterAkhir .add(mainJSONObj.getString("meterAkhir"));
                    nilaiTagihan .add( mainJSONObj.getString("nilaiTagihan"));
                    tagihanlain .add( mainJSONObj.getString("tagihanLain"));
                    int taglai=mainJSONObj.getInt("tagihanLain");
                    tagihanLainquery1=tagihanLainquery1+taglai;
                    int niltag=mainJSONObj.getInt("nilaiTagihan");
                    nilaiTagihanquery1=nilaiTagihanquery1+niltag;
                    penalty .add( mainJSONObj.getString("penalty"));
                    int pina=mainJSONObj.getInt("penalty");
                    penaltyquery1=penaltyquery1+pina;
                    admin.add( mainJSONObj.getString("admin"));
                    int adds=mainJSONObj.getInt("admin");
                    adminquery1=adminquery1+adds;
                    total.add( mainJSONObj.getString("total"));
                    int tots=mainJSONObj.getInt("total");
                    totalquery1=totalquery1+tots;
                    tarif .add( mainJSONObj.getString("tarif"));
                    int tarifs=0;
                    if(!mainJSONObj.getString("tarif").isEmpty()){
                        tarifs=Integer.parseInt(mainJSONObj.getString("tarif"));
                    }
                    tarifquery1=tarifquery1+tarifs;
                    alamat .add( mainJSONObj.getString("alamat"));
                    fee .add( mainJSONObj.getString("fee"));
                    int fees=mainJSONObj.getInt("fee");
                    feequeryquery1=feequeryquery1+fees;
                    Log.e("feequeryquery1","feequeryquery1: "+feequeryquery1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int x = 0; x < periode.size(); x++) {
                periode_data.append(periode.get(x)).append(" ");
            }
            for (int x = 0; x < penalty.size(); x++) {
                float pd= Float.parseFloat(penalty.get(x));
                penalty_data.append("Rp.").append(decimalFormat.format(pd)).append(" ");
            }
            for (int x = 0; x < meterAkhir.size(); x++) {
                meterdata.append(meterAwal.get(x)).append(" ").append(meterAkhir.get(x)).append(" ");
            }
            for (int x = 0; x < tagihanlain.size(); x++) {
                float tagihanlains= Float.parseFloat(tagihanlain.get(x));
                tagihan_lain_data.append("Rp.").append(decimalFormat.format(tagihanlains)).append(" ");
            }
            for (int x = 0; x < nilaiTagihan.size(); x++) {
                float nilaiTagihans= Float.parseFloat(nilaiTagihan.get(x));
                tagihan_data.append("Rp.").append(decimalFormat.format(nilaiTagihans)).append(" ");
            }
            for (int x = 0; x < admin.size(); x++) {
                float admins= Float.parseFloat(admin.get(x));
                admin_data.append("Rp.").append(decimalFormat.format(admins)).append(" ");
            }
            tvPelangganJudul.setText("NAMA");
            tvidPelanggan.setText(namaquery1);
            tvJudulNama.setText("DENDA");
            tvNamas.setText(penalty_data.toString());
            tvJudullembar.setText("METER AWAL/METER AKHIR");
            tvLembarTagihan.setText(meterdata.toString());
            tvrefidJudul.setText("TAGIHAN LAIN");
            tvRef.setText(tagihan_lain_data.toString());
            tvJudulTag.setText("PERIODE");
            tvTagPln.setText(periode_data.toString());
            tvJumlahTagihan.setText(jmltagihanquery1.toString());
            tvTagihan.setText(tagihan_data.toString());
            tvAdmin.setText(admin_data.toString());
            float totag= Float.parseFloat(totalTagihan);
            totalbayar=totag;
            tvTotalBayar.setText("Rp."+decimalFormat.format(totag));
            int jumtag= Integer.parseInt(jmltagihanquery1);
            float adms= Float.parseFloat(admin_biller);
            float feebils= Float.parseFloat(fee_biller);
            float fap= Float.parseFloat(fee_app);
            float fca= Float.parseFloat(fee_ca);
            float fsca= Float.parseFloat(fee_sub_ca);
            float fsca1= Float.parseFloat(fee_sub_ca_1);
            float fsca2= Float.parseFloat(fee_sub_ca_2);
            float fsca3= Float.parseFloat(fee_sub_ca_3);
            harga_loket = nilaiTagihanquery1 +penaltyquery1+((adms*jumtag) - (feebils*jumtag)) + (fap*jumtag) +
                    (fca*jumtag)+(fsca*jumtag)+(fsca1*jumtag)+(fsca2*jumtag)+(fsca3*jumtag);
            if (FeeSchema.equalsIgnoreCase("DEPOSIT")) {
                if (FeeBulanan.equalsIgnoreCase("OFF")) {
                    tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(harga_loket));
                }
                else {
                    tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(totag));
                }
            }
            else {
                tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(totag));

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
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN TAGIHAN\n"+pilihantipeterpilih)
                .build());
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String formattedDate = df.format(c);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + formattedDate)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + idpel)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA         : " + namaquery1 )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("JML TAGIHAN  : " + jmltagihanquery1 )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PERIODE      :" + periode_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("METER        :" + mtrawal_tagihan_untuk_data+" -"+mtrakhir_tagihan_untuk_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PEMAKAIAN    :" + pemakaianawal )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TAGIHAN      : " + tagihan_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("DENDA        : " + penalty_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Struk Ini Merupakan Bukti")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Pembayaran Yang Sah")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK   : " + admin_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR  : Rp."+decimalFormat.format(totalbayar) )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("Terima Kasih")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+username)
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
        cv.add("STRUK PEMBAYARAN TAGIHAN\n"+pilihantipeterpilih+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(40);
        tgl.setIndentationRight(40);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
        String formattedDate = df.format(c);
        tgl.add("TGL BAYAR        : " + formattedDate + "\n");
        tgl.add("ID PELANGGAN : " + idpel + "\n");
        tgl.add("NAMA                  : " + namaquery1 + "\n\n");
        tgl.add("JML TAGIHAN     : " + jmltagihanquery1 + "\n");
        tgl.add("PERIODE             :" + periode_data + "\n\n");
        tgl.add("METER                :" + mtrawal_tagihan_untuk_data+" -"+mtrakhir_tagihan_untuk_data + "\n");
        tgl.add("PEMAKAIAN       :" + pemakaianawal + "\n\n");
        tgl.add("TAGIHAN             : " + tagihan_data + "\n");
        tgl.add("DENDA                : " + penalty_data + "\n\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.add("Struk ini Merupakan\nBukti Pembayaran Yang Sah");
        document.add(keterangan);
        Paragraph admin = new Paragraph();
        admin.setAlignment(Element.ALIGN_LEFT);
        admin.setIndentationLeft(40);
        admin.setIndentationRight(40);
        admin.add("\n\n");
        admin.add("ADMIN BANK   : " + admin_data + "\n");
        admin.add("TOTAL BAYAR : Rp." +decimalFormat.format(totalbayar)+ "\n");
        admin.add("\n\n");
        document.add(admin);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add((kode_loket.get(0)+ " / "+ nama_loket.get(0))+ " / "+username + "\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
    private static String getHash(final String key, final String data) {
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