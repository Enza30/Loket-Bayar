package loketbayar.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MultiIdPayment extends AppCompatActivity implements PrintingCallback {
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvJuduls,tvTotalSemua;
    private ImageView ivPrint,ivBack, ivRefresh,ivX,ivIcon;
    DatabaseLocal myDb;
    StringBuilder lembar_tagihan_untuk_data;
    StringBuilder mtrawal_tagihan_untuk_data;
    StringBuilder mtrakhir_tagihan_untuk_data;
    StringBuilder penalty_tagihan_untuk_data;
    StringBuilder tagihanLain_tagihan_untuk_data;
    StringBuilder admin_tagihan_untuk_data;
    StringBuilder nilaiTagihan_tagihan_untuk_data;
    private String bulan,tahun,s,namanya = "",apinya = "" ,dayanya = "", tarifnya = "",detilTagihan="",lembar="",data="",
            msn="",biayaMeterai="",ppn="",ppj="",angsuran="",rpToken="",kwh="",mtrakhir="",mtrawal="",period=""
            ,responseCode, message,kodemulti,username,password,datauser,idpel,datakolektif,
            adminterpilihuser="",namaprodukterpilihuser="",produkterpilihuser="",adminprodukterpilihuser="",
            feebillerprodukterpilihuser="",feeappprodukterpilihuser="",produkbillerterpilihuser="",kategoriterpilih,
            datamulti,datamultisemua;
    private RecyclerView rvList;
    private Button btLagi,btKosongkan,btCheck,btPrint,btPdf,btBayar;
    private Dialog load;
    Printing printing;
    private PdfWriter writer;
    private EditText etIdpel;
    private SharedPreferences boyprefs;
    private int pemakaianquerypdam=0,tagihanLainquerypdam=0,nilaiTagihanquerypdam=0,penaltyquerypdam=0,
            adminquerypdam=0,totalquerypdam=0,tarifquerypdam=0,feequeryquerypdam=0,nilaiTagihanqueryPLn=0,
            dendaqueryPln=0,adminqueryPln=0,totalqueryPln=0,adminqueryTelkom=0, totalqueryTelkom=0,
            nilaiTagihanqueryTelkom=0, feequeryqueryTelkom=0, no=0,pengpdfan=0,idpelcek=0;
    float adm=0,totsnya=0,totaltag=0,totslkt=0,jml=0,totsnya2=0;
    private float penaltyy=0,dendaa=0,feee=0,itungantotal=0,itungantotal2=0,itunganhrgaloket=0,pengurangan=0,totalbayar=0,hrglkt=0,
            kembali = 0;
    private Spinner spAdmin,spProduk;
    private LinearLayout llAdmin;
    public static float saldobayar = 0,saldoawal=0,totalharusbayar=0;
    ArrayList<String> jsonnyaa = new ArrayList<>();
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private ArrayList<String> periode=new ArrayList<>();
    private ArrayList<String> kode_multi=new ArrayList<>();
    private ArrayList<String> idnya=new ArrayList<>();
    private ArrayList<String> pemakaian=new ArrayList<>();
    private ArrayList<String> meterAwal=new ArrayList<>();
    private ArrayList<String> meterAkhir=new ArrayList<>();
    private ArrayList<String> nilaiTagihan=new ArrayList<>();
    private ArrayList<String> penalty=new ArrayList<>();
    private ArrayList<String> tagihanLain=new ArrayList<>();
    private ArrayList<String> tarif=new ArrayList<>();
    private ArrayList<String> admin=new ArrayList<>();
    private ArrayList<String> total=new ArrayList<>();
    private ArrayList<String> alamat=new ArrayList<>();
    private ArrayList<String> fee=new ArrayList<>();
    private ArrayList<String> denda=new ArrayList<>();
    private ArrayList<String> jsonnya=new ArrayList<>();
    private ArrayList<String> idmulti = new ArrayList<>();
    private ArrayList<String> kode_produk_multi = new ArrayList<>();
    private ArrayList<String> kode_ca_multi = new ArrayList<>();
    private ArrayList<String> kode_sub_ca_multi = new ArrayList<>();
    private ArrayList<String> kode_loket_multi = new ArrayList<>();
    private ArrayList<String> fee_ca_multi = new ArrayList<>();
    private ArrayList<String> fee_sub_ca_multi = new ArrayList<>();
    private ArrayList<String> created_on_multi = new ArrayList<>();
    private ArrayList<String> updated_on_multi = new ArrayList<>();
    private ArrayList<String> kategori_multi = new ArrayList<>();
    private ArrayList<String> kode_biller_multi = new ArrayList<>();
    private ArrayList<String> kode_produk_biller= new ArrayList<>();
    private ArrayList<String> nama_produk_biller_multi= new ArrayList<>();
    private ArrayList<String> kode_produk_biller_multi= new ArrayList<>();
    private ArrayList<String> kode_produk = new ArrayList<>();
    private ArrayList<String> nama_produk_multi = new ArrayList<>();
    private ArrayList<String> denom_multi = new ArrayList<>();
    private ArrayList<String> admin_biller = new ArrayList<>();
    private ArrayList<String> admin_biller_multi = new ArrayList<>();
    private ArrayList<String> harga_biller_multi = new ArrayList<>();
    private ArrayList<String> tipe_fee_multi = new ArrayList<>();
    private ArrayList<String> fee_biller = new ArrayList<>();
    private ArrayList<String> fee_biller_multi = new ArrayList<>();
    private ArrayList<String> fee_app = new ArrayList<>();
    private ArrayList<String> fee_app_multi = new ArrayList<>();
    private ArrayList<String> status_multi = new ArrayList<>();
    private ArrayList<String> idpel_multi = new ArrayList<>();
    private ArrayList<String> nama = new ArrayList<>();
    private ArrayList<String> jumlahTagihan = new ArrayList<>();
    private ArrayList<String> reff = new ArrayList<>();
    private ArrayList<String> serverresponses = new ArrayList<>();
    private ArrayList<String> totalTagihan = new ArrayList<>();
    private ArrayList<String> productCode = new ArrayList<>();
    private ArrayList<String> refID = new ArrayList<>();
    private ArrayList<String> periode_data = new ArrayList<>();
    private ArrayList<String> penalty_data = new ArrayList<>();
    private ArrayList<String> meterdata = new ArrayList<>();
    private ArrayList<String> tagihan_lain_data = new ArrayList<>();
    private ArrayList<String> nilai_tagihan_data = new ArrayList<>();
    private ArrayList<String> tagihan_data = new ArrayList<>();
    private ArrayList<String> admin_data = new ArrayList<>();
    private ArrayList<String> pemakaianquerypdamarray = new ArrayList<>();
    private ArrayList<String> tagihanLainquerypdamarray = new ArrayList<>();
    private ArrayList<String> penaltyquerypdamarray = new ArrayList<>();
    private ArrayList<String> tarifquerypdamarray = new ArrayList<>();
    private ArrayList<String> feequeryquerypdamarray = new ArrayList<>();
    private ArrayList<String> divre = new ArrayList<>();
    private ArrayList<String> datel = new ArrayList<>();
    private ArrayList<String> feequeryqueryTelkomArray = new ArrayList<>();
    private ArrayList<String> fee_data = new ArrayList<>();
    private ArrayList<String> subscriberID = new ArrayList<>();
    private ArrayList<String> tarifPln = new ArrayList<>();
    private ArrayList<String> daya = new ArrayList<>();
    private ArrayList<String> lembarTagihanTotal = new ArrayList<>();
    private ArrayList<String> lembarTagihan = new ArrayList<>();
    private ArrayList<String> total_data = new ArrayList<>();
    private ArrayList<String> denda_data = new ArrayList<>();
    private ArrayList<String> proses = new ArrayList<>();
    private ArrayList<String> nilaiTagihanquery = new ArrayList<>();
    private ArrayList<String> dendaqueryPlnarray = new ArrayList<>();
    private ArrayList<String> adminquery = new ArrayList<>();
    private ArrayList<String> totalquery = new ArrayList<>();
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> adminterpilih=new ArrayList<>();
    private ArrayList<String> pilproduk=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_multi_id_payment);
        Printooth.INSTANCE.init(this);
        btCheck= findViewById(R.id.btcheck);
        btKosongkan= findViewById(R.id.btKosongkan);
        btPdf= findViewById(R.id.btPdf);
        btBayar= findViewById(R.id.btBayar);
        tvNama = findViewById(R.id.tvNama);
        tvTotalSemua = findViewById(R.id.tvTotalSemua);
        etIdpel = findViewById(R.id.etIdpel);
        btPrint = findViewById(R.id.btPrint);
        rvList = findViewById(R.id.rvList);
        ivX = findViewById(R.id.ivX);
        llAdmin = findViewById(R.id.llAdmin);
    //    btLagi = findViewById(R.id.btLagi);
        spAdmin = findViewById(R.id.spAdmin);
        spProduk = findViewById(R.id.spProduk);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivRefresh = findViewById(R.id.ivRefresh);
        ivBack = findViewById(R.id.ivBack);
        ivPrint = findViewById(R.id.ivPrint);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvJuduls.setText("MULTIPAYMENT");
        datakolektif=boyprefs.getString("datakolektif","");
        Log.e("datakolektif",datakolektif);
        datauser = boyprefs.getString("datauser", "");
        nama_pemilik = PojoMion.AmbilArray(datakolektif, "nama_pemilik", "dataloket");
        nama_loket = PojoMion.AmbilArray(datakolektif, "nama_loket", "dataloket");
        kode_loket = PojoMion.AmbilArray(datakolektif, "kode_loket", "dataloket");
        saldo = PojoMion.AmbilArray(datakolektif, "saldo", "depositloket");
        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        kode_ca = PojoMion.AmbilArray(datakolektif, "kode_ca", "dataloket");
        kode_sub_ca = PojoMion.AmbilArray(datakolektif, "kode_sub_ca", "dataloket");
        pilproduk=PojoMion.AmbilArray(datakolektif,"nama_produk","datakolektif");
        kode_produk_biller=PojoMion.AmbilArray(datakolektif,"kode_produk_biller","datakolektif");
        kode_produk=PojoMion.AmbilArray(datakolektif,"kode_produk","datakolektif");
        admin_biller=PojoMion.AmbilArray(datakolektif,"admin_biller","datakolektif");
        fee_biller=PojoMion.AmbilArray(datakolektif,"fee_biller","datakolektif");
        fee_app=PojoMion.AmbilArray(datakolektif,"fee_app","datakolektif");
        kode_produk_biller.add(0,"-1");
        kode_produk.add(0,"-1");
        admin_biller.add(0,"-1");
        fee_biller.add(0,"-1");
        fee_app.add(0,"-1");
        pilproduk.add(0,"-Silahkan Pilih-");
//        btKosongkan.setVisibility(View.GONE);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        tvNamaLoket.setText("Loket " + nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);

        saldobayar= Float.parseFloat(saldo.get(0));
        saldoawal= Float.parseFloat(saldo.get(0));
        new AmbilSaldoTask(MultiIdPayment.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                boyprefs.edit().putBoolean("MultiIdPayment", false).apply();
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                new LoginTask(MultiIdPayment.this).execute("login", username, password);
                }else{
                    Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }    }
        });
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    tvSaldo.setText("");
                username=boyprefs.getString("username","");
                password=boyprefs.getString("password","");
                new AmbilSaldoTask(MultiIdPayment.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }else{
                    Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }  }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(MultiIdPayment.this);
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
                            Toast.makeText(MultiIdPayment.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(MultiIdPayment.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adminterpilih.add("-Silahkan Pilih-");
        adminterpilih.add("3000");
        adminterpilih.add("3500");
        adminterpilih.add("5000");
        myDb=new DatabaseLocal(this);
        initview();
        ArrayAdapter<String> adminadapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_itemss3, adminterpilih);
        spAdmin.setAdapter(adminadapter);
        spAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adaInternet()) {
                adminterpilihuser=parent.getItemAtPosition(position).toString();
                if(adminterpilihuser.equalsIgnoreCase("-Silahkan Pilih-")){
                    adminterpilihuser="";
                }else{
                    adminterpilihuser=parent.getItemAtPosition(position).toString();
                }
                }else{
                    Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                  //  spProduk.setSelection(0);
                    spAdmin.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> produkadapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_itemss3, pilproduk);
        spProduk.setAdapter(produkadapter);
        spProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adaInternet()) {
                namaprodukterpilihuser=String.valueOf(parent.getItemAtPosition(position));
                    if (!namaprodukterpilihuser.equalsIgnoreCase("-Silahkan Pilih-")) {
                        load=new Dialog(MultiIdPayment.this);
                        load.setContentView(R.layout.dialogload2);
                        load.setCancelable(false);
                        ivX=load.findViewById(R.id.ivX);
                        Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
//                        load.show();

                        if (namaprodukterpilihuser.equalsIgnoreCase("Tagihan Listrik")) {
                            spAdmin.setSelection(0);
                            llAdmin.setVisibility(View.VISIBLE);
                        }
                        else if (!namaprodukterpilihuser.equalsIgnoreCase("Tagihan Listrik")) {
                            spAdmin.setSelection(0);
                            llAdmin.setVisibility(View.GONE);
                        }
                        int pos = pilproduk.indexOf(namaprodukterpilihuser);
                        produkterpilihuser = kode_produk.get(pos);
                        produkbillerterpilihuser = kode_produk_biller.get(pos);
                        adminprodukterpilihuser = admin_biller.get(pos);
                        feebillerprodukterpilihuser = fee_biller.get(pos);
                        feeappprodukterpilihuser = fee_app.get(pos);
                    }
            }else{
                Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    spProduk.setSelection(0);
                    spAdmin.setSelection(0);
            }             }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        kodemulti= RandomString.getRandomString(6);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adaInternet()) {
                    idpel = etIdpel.getText().toString();
                    if (namaprodukterpilihuser.equalsIgnoreCase("-Silahkan Pilih-")) {
                        Toast.makeText(MultiIdPayment.this, "Silahkan Pilih Produk", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (namaprodukterpilihuser.equalsIgnoreCase("Tagihan Listrik")) {
                            if (adminterpilihuser == "") {
                                Toast.makeText(MultiIdPayment.this, "Silahkan Pilih Admin", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (idpel.isEmpty()) {
                                    Toast.makeText(MultiIdPayment.this, "Masukkan Id Pelanggan", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    new MultiIdPayment.AmbilPlnTasks(MultiIdPayment.this).execute(getString(R.string.link)+"apis/android/multipay/inquiry");
                                    idpel = etIdpel.getText().toString();
                                    datamultisemua = "";
                                }
                            }
                        }
                        else {
                            if (idpel.isEmpty()) {
                                Toast.makeText(MultiIdPayment.this, "Masukkan Id Pelanggan", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                new MultiIdPayment.AmbilPlnTasks(MultiIdPayment.this).execute(getString(R.string.link)+"apis/android/multipay/inquiry");
                                idpel = etIdpel.getText().toString();
                                datamultisemua = "";
                            }
                        }

                    }
                    }
                else {
                    Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btKosongkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                load=new Dialog(MultiIdPayment.this);
                load.setContentView(R.layout.dialogload2);
                load.setCancelable(false);
                ivX=load.findViewById(R.id.ivX);
                Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                load.show();
                HapusData(getResources().getString(R.string.xyz)+"hapusdata",kodemulti,username,password);
            }
                else{
                Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }  }
        });
        btPdf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(pengpdfan==0){

                //    String root = Environment.getExternalStorageDirectory().toString();
                    String dest = MultiIdPayment.this.getExternalFilesDir(null) + "/RTS";
                    File myDir = new File(dest + "/PDF");
                    myDir.mkdirs();
                    if (!myDir.exists())
                        myDir.mkdirs();
                    File h = new File(dest
                            + "/PDF/" + "Daftar" + ".pdf");
                 //   Document document = new Document(PageSize.A4);
                    Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 10f, 10f);
                    Uri path = FileProvider.getUriForFile(
                            MultiIdPayment.this,
                            MultiIdPayment.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
                            h);
// Create Pdf Writer for Writting into New Created Document
                    try {
                        writer = PdfWriter.getInstance(document, new FileOutputStream(h));

                    } catch (DocumentException | FileNotFoundException e) {
                        e.printStackTrace();
                    }

// Open Document for Writting into document
                    document.open();
                    addMetaData1(document);

                    try {
                        addTitlePage1(document);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
// Close Document after writting all content
                    document.close();


                    // send email
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello...");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Daftar");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                    emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    emailIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(emailIntent);
                }
                else{
                    //    String root = Environment.getExternalStorageDirectory().toString();
                    String dest = MultiIdPayment.this.getExternalFilesDir(null) + "/RTS";
                    File myDir = new File(dest + "/PDF");
                    myDir.mkdirs();
                    if (!myDir.exists())
                        myDir.mkdirs();
                    File h = new File(dest
                            + "/PDF/" + "Selesai.pdf");
                    Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 10f, 10f);
                    Uri path = FileProvider.getUriForFile(
                            MultiIdPayment.this,
                            MultiIdPayment.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
                            h);
// Create Pdf Writer for Writting into New Created Document
                    try {
                        writer = PdfWriter.getInstance(document, new FileOutputStream(h));

                    } catch (DocumentException | FileNotFoundException e) {
                        e.printStackTrace();
                    }

// Open Document for Writting into document
                    document.open();
                    addMetaData2(document);

                    try {
                        addTitlePage1(document);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
// Close Document after writting all content
                    document.close();


                    // send email
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello...");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Selesai");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                    emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    emailIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(emailIntent);
                }


// User Define Method

            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                btBayar.setEnabled(false);
                btPdf.setEnabled(false);
                    new MultiIdPayment.AmbilPlnTasks2(MultiIdPayment.this).execute(getString(R.string.link)+"apis/android/multipay/payment");}
                else{
                Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }
            }
        });
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                    startActivityForResult(new Intent(MultiIdPayment.this, ScanningActivity.class),
                            ScanningActivity.SCANNING_FOR_PRINTER);
                    initview();
                } else {
                    initview();
                    printdata();
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
        if(adaInternet()) {
            boyprefs.edit().putBoolean("MultiIdPayment", false).apply();
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(MultiIdPayment.this).execute("login", username, password);
        }else{
            Toast.makeText(MultiIdPayment.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();
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


    //****************************************************************************************************************
    //****************************************************************************************************************
    public class AmbilPlnTasks extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password, token;
        private Dialog load, dialogload;
        AmbilPlnTasks(Context context) {
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
     //       String key2 = "bd8446eaa574ab00ab72249f8b06a759";
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
                jsonRequest.put("kode_produk", produkterpilihuser);
                jsonRequest.put("idpel", idpel);
                jsonRequest.put("kode_unik", kodemulti);
                jsonRequest.put("admin", adminterpilihuser);
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
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {
                pengpdfan=0;
                mulai();
            } else {
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
                            pengpdfan=0;


                    }
                });
                dialog.show();
            }

        }

    }
    void mulai(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                load.dismiss();
                load=new Dialog(MultiIdPayment.this);
                load.setContentView(R.layout.dialogload2);
                load.setCancelable(false);
                ivX=load.findViewById(R.id.ivX);
                Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                load.show();
                runThread();
            }
        });
    }
    private void runThread() {
        new Thread() {
            public void run() {
                int jh = 0;
                while (jh <idmulti.size()*10) {
                    jh++;
                    try {
                        final int finalJh = jh;
                        int finalJh1 = jh;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ivX.setRotation(finalJh);
                            }
                        });
                        Thread.sleep(0);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                        Ambilmulti(getResources().getString(R.string.xyz)+"ambildatamultisemua",
                        kodemulti,username,password);
            }

        }.start();

    }
    void Ambilmulti(String POST_ORDER,String kodemulti,String usernames,String passwords) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if(ServerResponse.contains("datamultisemua")){
                        load.dismiss();
                        datamultisemua=ServerResponse;
                        Pembaruan();
                    }else{
                        load.dismiss();
                        Log.e("hasil",ServerResponse);
                        // Toast.makeText(MultiIdPayment.this, ServerResponse, Toast.LENGTH_SHORT).show();
                    }
                }
            }, 0);

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        load.dismiss();
                        Log.e("errorambilmulti",volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodemulti", kodemulti);
                params.put("username", usernames);
                params.put("password", passwords);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MultiIdPayment.this);
        requestQueue.add(stringRequest);
    }
    void Pembaruan(){
        produkterpilihuser="";
        adminterpilihuser="";
        spProduk.setSelection(0);
        spAdmin.setSelection(0);
        llAdmin.setVisibility(View.GONE);
        idpel="";
        etIdpel.setText("");
        Log.e("datamultisemua", datamultisemua);
        kode_multi = PojoMion.AmbilArray(datamultisemua,"kode_unik","datamultisemua");
        idnya = PojoMion.AmbilArray(datamultisemua,"id","datamultisemua");
        idmulti = PojoMion.AmbilArray(datamultisemua,"idpel","datamultisemua");
        kode_produk_multi = PojoMion.AmbilArray(datamultisemua,"kode_produk","datamultisemua");
        kode_ca_multi = PojoMion.AmbilArray(datamultisemua,"kode_ca","datamultisemua");
        kode_sub_ca_multi = PojoMion.AmbilArray(datamultisemua,"kode_sub_ca","datamultisemua");
        kode_loket_multi = PojoMion.AmbilArray(datamultisemua,"kode_loket","datamultisemua");
        fee_ca_multi = PojoMion.AmbilArray(datamultisemua,"peserta","datamultisemua");
        fee_sub_ca_multi = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        created_on_multi = PojoMion.AmbilArray(datamultisemua,"created_at","datamultisemua");
        updated_on_multi = PojoMion.AmbilArray(datamultisemua,"updated_at","datamultisemua");
        kategori_multi = PojoMion.AmbilArray(datamultisemua,"kategori","datamultisemua");
        kode_biller_multi = PojoMion.AmbilArray(datamultisemua,"kode_biller","datamultisemua");
        kode_produk_biller_multi = PojoMion.AmbilArray(datamultisemua,"kode_produk_biller","datamultisemua");
        nama_produk_biller_multi = PojoMion.AmbilArray(datamultisemua,"nama_produk","datamultisemua");
        nama_produk_multi = PojoMion.AmbilArray(datamultisemua,"nama_produk","datamultisemua");
        denom_multi = PojoMion.AmbilArray(datamultisemua,"harga_biller","datamultisemua");
        admin_biller_multi = PojoMion.AmbilArray(datamultisemua,"admin","datamultisemua");
        harga_biller_multi = PojoMion.AmbilArray(datamultisemua,"harga_biller","datamultisemua");
        tipe_fee_multi = PojoMion.AmbilArray(datamultisemua,"tipe_fee","datamultisemua");
        fee_biller_multi = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        fee_app_multi = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        status_multi = PojoMion.AmbilArray(datamultisemua,"status","datamultisemua");
        idpel_multi = PojoMion.AmbilArray(datamultisemua,"idpel","datamultisemua");
        nama = PojoMion.AmbilArray(datamultisemua,"nama","datamultisemua");
        jumlahTagihan = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        reff = PojoMion.AmbilArray(datamultisemua,"reff","datamultisemua");
        serverresponses = PojoMion.AmbilArray(datamultisemua,"serverresponses","datamultisemua");
        totalTagihan = PojoMion.AmbilArray(datamultisemua,"total","datamultisemua");
        productCode = PojoMion.AmbilArray(datamultisemua,"kode_produk","datamultisemua");
        refID = PojoMion.AmbilArray(datamultisemua,"ref","datamultisemua");
        periode_data = PojoMion.AmbilArray(datamultisemua,"periode","datamultisemua");
        penalty_data = PojoMion.AmbilArray(datamultisemua,"denda","datamultisemua");
        meterdata = PojoMion.AmbilArray(datamultisemua,"stand_meter","datamultisemua");
        tagihan_lain_data = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        nilai_tagihan_data = PojoMion.AmbilArray(datamultisemua,"tagihan","datamultisemua");
        tagihan_data = PojoMion.AmbilArray(datamultisemua,"tagihan","datamultisemua");
        admin_data = PojoMion.AmbilArray(datamultisemua,"admin","datamultisemua");
        pemakaianquerypdamarray = PojoMion.AmbilArray(datamultisemua,"pemakaian","datamultisemua");
        tagihanLainquerypdamarray = PojoMion.AmbilArray(datamultisemua,"detail_tagihan","datamultisemua");
        penaltyquerypdamarray = PojoMion.AmbilArray(datamultisemua,"detail_denda","datamultisemua");
        tarifquerypdamarray = PojoMion.AmbilArray(datamultisemua,"detail_admin","datamultisemua");
        feequeryquerypdamarray = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        alamat = PojoMion.AmbilArray(datamultisemua,"fee_ca","datamultisemua");
        subscriberID =  PojoMion.AmbilArray(datamultisemua,"idpel","datamultisemua");
        tarifPln =  PojoMion.AmbilArray(datamultisemua,"tarif","datamultisemua");
        daya =  PojoMion.AmbilArray(datamultisemua,"daya","datamultisemua");
        lembarTagihanTotal =  PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        lembarTagihan =  PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        total_data =  PojoMion.AmbilArray(datamultisemua,"total","datamultisemua");
        denda_data =  PojoMion.AmbilArray(datamultisemua,"denda","datamultisemua");
        proses =  PojoMion.AmbilArray(datamultisemua,"status","datamultisemua");
        nilaiTagihanquery =  PojoMion.AmbilArray(datamultisemua,"tagihan","datamultisemua");
        dendaqueryPlnarray =  PojoMion.AmbilArray(datamultisemua,"denda","datamultisemua");
        adminquery =  PojoMion.AmbilArray(datamultisemua,"admin","datamultisemua");
        totalquery =  PojoMion.AmbilArray(datamultisemua,"total","datamultisemua");
        divre =  PojoMion.AmbilArray(datamultisemua,"divre","datamultisemua");
        datel =  PojoMion.AmbilArray(datamultisemua,"datel","datamultisemua");
        fee_data =  PojoMion.AmbilArray(datamultisemua,"harga_loket","datamultisemua");
        feequeryqueryTelkomArray =  PojoMion.AmbilArray(datamultisemua,"fee_ca","datamultisemua");
        usernames =  PojoMion.AmbilArray(datamultisemua,"username","datamultisemua");
        tarif =  PojoMion.AmbilArray(datamultisemua,"tarif","datamultisemua");
        MultiidAdapter boyiadapter2 = new MultiidAdapter(MultiIdPayment.this,
                kode_multi ,
                idnya ,
                idmulti ,
                kode_produk_multi ,
                kode_ca_multi ,
                kode_sub_ca_multi ,
                kode_loket_multi ,
                fee_ca_multi ,
                fee_sub_ca_multi ,
                created_on_multi ,
                updated_on_multi ,
                kategori_multi ,
                kode_biller_multi ,
                kode_produk_biller_multi ,
                nama_produk_biller_multi ,
                nama_produk_multi ,
                denom_multi ,
                admin_biller_multi ,
                harga_biller_multi ,
                tipe_fee_multi ,
                fee_biller_multi ,
                fee_app_multi ,
                status_multi ,
                idpel_multi ,
                nama ,
                jumlahTagihan ,
                reff ,
                serverresponses ,
                totalTagihan ,
                productCode ,
                refID ,
                periode_data ,
                penalty_data ,
                meterdata ,
                tagihan_lain_data ,
                nilai_tagihan_data ,
                tagihan_data ,
                admin_data ,
                pemakaianquerypdamarray ,
                tagihanLainquerypdamarray ,
                penaltyquerypdamarray ,
                tarifquerypdamarray ,
                feequeryquerypdamarray ,
                alamat ,
                subscriberID ,
                tarifPln ,
                daya ,
                lembarTagihanTotal ,
                lembarTagihan ,
                total_data ,
                denda_data ,
                proses ,
                nilaiTagihanquery ,
                dendaqueryPlnarray ,
                adminquery ,
                totalquery ,
                usernames ,
                tarif,tvTotalSemua,divre,datel,feequeryqueryTelkomArray,fee_data,rvList,btPdf,btBayar);
        boyiadapter2.notifyDataSetChanged();
        rvList.setAdapter(boyiadapter2);
        totsnya=0;adm=0;totaltag=0;penaltyy=0;dendaa=0;feee=0;jml=0;totslkt=0;itungantotal=0;itunganhrgaloket=0;
        for(int x=0;x<idnya.size();x++){
            no++;
            if(kategori_multi.get(x).equalsIgnoreCase("PLN")){
                totsnya=Float.parseFloat(totalTagihan.get(x));
                totslkt=Float.parseFloat(fee_data.get(x));
            }
            else if(kategori_multi.get(x).equalsIgnoreCase("PDAM")){
                totsnya=Float.parseFloat(totalTagihan.get(x));
                totslkt=Float.parseFloat(fee_data.get(x));            }
            else if(kategori_multi.get(x).equalsIgnoreCase("TELKOM")){
                totsnya=Float.parseFloat(totalTagihan.get(x));
                totslkt=Float.parseFloat(fee_data.get(x));            }
            else if(kategori_multi.get(x).equalsIgnoreCase("BPJSKES")){
                totsnya=Float.parseFloat(totalTagihan.get(x));
                totslkt=Float.parseFloat(fee_data.get(x));            }
            else if(kategori_multi.get(x).equalsIgnoreCase("PASCABAYAR")){
                totsnya=Float.parseFloat(totalTagihan.get(x));
                totslkt=Float.parseFloat(fee_data.get(x));            }
            itungantotal=itungantotal+totsnya;
            itunganhrgaloket=itunganhrgaloket+totslkt;
            tvTotalSemua.setText("Total Tagihan: Rp. "+decimalFormat.format(itungantotal)+"\nSaldo akan terpotong: Rp. "+decimalFormat.format(itunganhrgaloket));


        }

        if(idnya.size()>0){
            if(pengpdfan==0){
                btPdf.setEnabled(true);
                btBayar.setEnabled(true);
                spProduk.setEnabled(true);
                etIdpel.setEnabled(true);
                btPrint.setEnabled(false);
            }else{
                btPdf.setEnabled(true);
                btBayar.setEnabled(false);
                spProduk.setEnabled(false);
                etIdpel.setEnabled(false);
                btCheck.setEnabled(false);
                btPrint.setEnabled(true);
            }

        }else{
            spProduk.setEnabled(true);
            etIdpel.setEnabled(true);
            btCheck.setEnabled(true);
            btPdf.setEnabled(false);
            btBayar.setEnabled(false);
            btPrint.setEnabled(false);
        }


    }
    public class AmbilPlnTasks2 extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private String result, username, password, token;
        private Dialog load, dialogload;
        AmbilPlnTasks2(Context context) {
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
      //      String key2 = "bd8446eaa574ab00ab72249f8b06a759";
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
                jsonRequest.put("kode_unik", kodemulti);

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
                responseCode = mainJSONObj.getString("responseCode");
                message = mainJSONObj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCode.equalsIgnoreCase("00")) {

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
                        mulai();
                      pengpdfan=1;
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

   //****************************************************************************************************************
   //****************************************************************************************************************

    void HapusData(String POST_ORDER,String kodemulti,String usernames,String passwords) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if(ServerResponse.trim().equalsIgnoreCase("sukses")){
                        load.dismiss();
                        boyprefs.edit().putBoolean("MultiIdPayment", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(MultiIdPayment.this).execute("login", username, password);
                    }else{
                        load.dismiss();
                        boyprefs.edit().putBoolean("MultiIdPayment", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(MultiIdPayment.this).execute("login", username, password);

                    }
                }
            }, 0);

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        load.dismiss();
                        Toast.makeText(MultiIdPayment.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("errorambilmulti",volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodemulti", kodemulti);
                params.put("username", usernames);
                params.put("password", passwords);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MultiIdPayment.this);
        requestQueue.add(stringRequest);
    }

    //****************************************************************************************************************
    //****************************************************************************************************************


    public void addMetaData1(Document document) {
        document.addTitle("Faktur");
        document.addSubject("Faktur");
        document.addKeywords("Company");
    }
    public void addMetaData2(Document document) {
        document.addTitle("Faktur");
        document.addSubject("Faktur");
        document.addKeywords("Company");
    }
    public void addTitlePage1(Document document) throws DocumentException {
        // Creating a table object

        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("DAFTAR TAGIHAN PELANGGAN"+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        PdfPTable table = new PdfPTable(6);
        table.setWidths(new int[]{1, (int) 2, 4, 2, 3, 4});
        PdfPCell cell = new PdfPCell(new Phrase("No."));
        PdfPCell cell2 = new PdfPCell(new Phrase("PRODUK"));
        PdfPCell cell3 = new PdfPCell(new Phrase("TAGIHAN"));
        PdfPCell cell4 = new PdfPCell(new Phrase("PERIODE"));
        PdfPCell cell5 = new PdfPCell(new Phrase("ADMIN"));
        PdfPCell cell6 = new PdfPCell(new Phrase("TOTAL"));
        table.addCell(cell);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        document.add(table);
        float totsnya2=0;
        float itungantotal2=0;
        int nomer=0;
        for(int x=0;x<idmulti.size();x++){
            nomer++;
            float adm = Float.parseFloat(adminquery.get(x));
            float niltags = Float.parseFloat(nilaiTagihanquery.get(x)) + Float.parseFloat(dendaqueryPlnarray.get(x));
            float tot = niltags + adm;
                PdfPTable table2 = new PdfPTable(6);
                table2.setWidths(new int[]{1, 2, 4, 2, 3, 4});
                PdfPCell cells = new PdfPCell(new Phrase(String.valueOf(nomer)));
                PdfPCell cells2 = new PdfPCell(new Phrase(nama_produk_multi.get(x)));
                PdfPCell cells3 = new PdfPCell(new Phrase("Rp."+decimalFormat.format(niltags)+"\n\n"+nama.get(x)+"\n"));
                PdfPCell cells4 = new PdfPCell(new Phrase(periode_data.get(x)));
                PdfPCell cells5 = new PdfPCell(new Phrase("Rp."+decimalFormat.format(adm)));
                PdfPCell cells6 = new PdfPCell(new Phrase("Rp."+decimalFormat.format(tot)+"\n\n"+proses.get(x)+"\n"));
                table2.addCell(cells);
                table2.addCell(cells2);
                table2.addCell(cells3);
                table2.addCell(cells4);
                table2.addCell(cells5);
                table2.addCell(cells6);
                document.add(table2);

            totsnya2=tot;
            itungantotal2=itungantotal2+totsnya2;

        }
        Paragraph cv2 = new Paragraph();
        cv2.setAlignment(Element.ALIGN_LEFT);
        cv2.add("\nTOTAL TAGIHAN : "+"Rp. "+decimalFormat.format(itungantotal2)+"\n\n");
        cv2.setIndentationLeft(80);
        cv2.setIndentationRight(80);
        document.add(cv2);

    }
    private void printdata() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
//        printables.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build());
        for (int x = 0; x < idmulti.size(); x++) {

            if (proses.get(x).equalsIgnoreCase("SUKSES")) {
                if (kategori_multi.get(x).equalsIgnoreCase("PLN")) {
                   printables.add(new TextPrintable.Builder()
                            .setText("STRUK PEMBAYARAN TAGIHAN LISTRIK")
                            .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("ID PELANGGAN : " + idmulti.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NAMA         : " + nama.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("TARIF/DAYA   : " + tarif.get(x) + " / " + daya.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("BL/TH        : " + periode_data.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("STAND METER  : " + meterdata.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    float adm = Float.parseFloat(adminquery.get(x));
                    float niltags = Float.parseFloat(nilaiTagihanquery.get(x)) + Float.parseFloat(dendaqueryPlnarray.get(x));
                    float tot = niltags + adm;
                    printables.add(new TextPrintable.Builder()
                            .setText("RP TAG PLN   : " + "Rp." + decimalFormat.format(niltags))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NO REF       : " + reff.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                            .setText("PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("ADMIN BANK   : " + "Rp." + decimalFormat.format(adm))
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
                    int lemsisa = 0;

                    if (lemsisa > 1) {
                        printables.add(new TextPrintable.Builder()
                                .setText("Lembar Tagihan Sisa : " + lemsisa)
                                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                    }
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
                            .setText("Informasi Hubungi Call Center\n123 Atau Hub PLN Terdekat")
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
                            .setText(kode_loket.get(0) + " / " + nama_loket.get(0) + " / " + username)
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

                }
                else if (kategori_multi.get(x).equalsIgnoreCase("PDAM")) {
                    float adm = Float.parseFloat(adminquery.get(x));
                    float niltags = Float.parseFloat(nilaiTagihanquery.get(x));
                    float den = Float.parseFloat(dendaqueryPlnarray.get(x));
                    float tot = niltags + adm+den;
                    printables.add(new TextPrintable.Builder()
                            .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                            .setText("STRUK PEMBAYARAN TAGIHAN\n" + nama_produk_multi.get(x))
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
                            .setText("TGL BAYAR    : " + formattedDate)
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("ID PELANGGAN : " + idmulti.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NAMA         : " + nama.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("JML TAGIHAN  : " + jumlahTagihan.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("PERIODE      :" + periode_data.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("METER        :" + meterdata.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("PEMAKAIAN    :" +  pemakaianquerypdamarray.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("TAGIHAN      : "+ tagihanLainquerypdamarray.get(x))
 //                           .setText("TAGIHAN      : "+ "Rp. "  + decimalFormat.format(niltags))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("DENDA        : " + penaltyquerypdamarray.get(x))
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
                            .setText("ADMIN BANK   : "+ tarifquerypdamarray.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("TOTAL BAYAR  : Rp." + decimalFormat.format(tot))
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
                            .setText(kode_loket.get(0) + " / " + nama_loket.get(0) + " / " + username)
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
                  }
                else if (kategori_multi.get(x).equalsIgnoreCase("TELKOM")) {
                    float adm = Float.parseFloat(adminquery.get(x));
                    float niltags = Float.parseFloat(nilaiTagihanquery.get(x))+ Float.parseFloat(dendaqueryPlnarray.get(x));
                    float tot = niltags + adm;
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    String kd = nama_produk_multi.get(x);
                    if (kd.equalsIgnoreCase("KARTU HALO")) {
                        printables.add(new TextPrintable.Builder()
                                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                                .setText("STRUK PEMBAYARAN TAGIHAN\n" + kd)
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
                                .setText("TGL BAYAR    : " + formattedDate)
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("LAYANAN      : " + kd)
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("NO Handphone : " + idmulti.get(x))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("NAMA         : " + nama.get(x))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("PERIODE      : " + periode_data.get(x))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("NILAI TAGIHAN: Rp." + decimalFormat.format(niltags))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                                .setText("Struk Ini merupakan Bukti")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                                .setText("Pembayaran Yang Sah")
                                .build());
                    }
                    else {
                        printables.add(new TextPrintable.Builder()
                                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                                .setText("STRUK PEMBAYARAN TAGIHAN\n" + kd)
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("NO PELANGGAN : " + idmulti.get(x))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("Nama         : " + nama.get(x))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("DIVRE/DATEL  : " + divre.get(x) + "/" + datel.get(x))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        float jumtags = Float.parseFloat(jumlahTagihan.get(x));
                        printables.add(new TextPrintable.Builder()
                                .setText("JML TAGIHAN  : " + decimalFormat.format(jumtags))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());

                        printables.add(new TextPrintable.Builder()
                                .setText("PERIODE      : " + periode_data.get(x))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("RP TAGIHAN   : Rp." + decimalFormat.format(niltags))
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
                    }
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("ADMIN BANK   : Rp." + decimalFormat.format(adm))
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("\n")
                                .build());
                        printables.add(new TextPrintable.Builder()
                                .setText("TOTAL BAYAR  : Rp." + decimalFormat.format(tot))
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
                                .setText(kode_loket.get(0) + " / " + nama_loket.get(0) + " / " + username)
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

                }
                else if (kategori_multi.get(x).equalsIgnoreCase("BPJSKES")) {
                    float adm = Float.parseFloat(adminquery.get(x));
                    float niltags = Float.parseFloat(nilaiTagihanquery.get(x))+ Float.parseFloat(dendaqueryPlnarray.get(x));
                    float tot = niltags + adm;
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
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                    String formattedDate = df.format(c);
                    printables.add(new TextPrintable.Builder()
                            .setText("TGL BAYAR    : " + formattedDate)
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NO REFERENSI : " + reff.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NO VA   : " + idmulti.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NAMA    : " +nama.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("JUMLAH PESERTA : "+fee_ca_multi.get(x)+" ORANG" )
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("PERIODE        : "+periode_data.get(x)+" BULAN" )
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("JUMLAH TAGIHAN : Rp."+decimalFormat.format(niltags) )
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
                            .setText("ADMIN BANK     : " + "Rp." + decimalFormat.format(adm))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("TOTAL BAYAR    : " + "Rp." + decimalFormat.format(tot))
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

                }
                else if (kategori_multi.get(x).equalsIgnoreCase("PASCABAYAR")) {
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                            .setText("STRUK PEMBAYARAN TAGIHAN\n" + nama_produk_multi.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
                    String formattedDate = df.format(c);
                    printables.add(new TextPrintable.Builder()
                            .setText("TGL BAYAR    : " + formattedDate)
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("LAYANAN      : " + nama_produk_multi.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NO Handphone : " + idmulti.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("NAMA         : " + nama.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("PERIODE      : " + periode_data.get(x))
                            .build());
                    printables.add(new TextPrintable.Builder()
                            .setText("\n")
                            .build());
                    float adm = Float.parseFloat(adminquery.get(x));
                    float niltags = Float.parseFloat(nilaiTagihanquery.get(x)) + Float.parseFloat(dendaqueryPlnarray.get(x));
                    float tot = niltags + adm;
                    printables.add(new TextPrintable.Builder()
                            .setText("NILAI TAGIHAN: Rp." + decimalFormat.format(niltags))
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
                            .setText("ADMIN BANK   : " + "Rp." + decimalFormat.format(adm))
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
                            .setText(kode_loket.get(0) + " / " + nama_loket.get(0)+ " / "+ username)
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
                }
            }
            else {
                //   Context context = null;
                //    Toast.makeText(context, "Transaksi Gagal/Pending Id : "+idpel_multi.get(x), Toast.LENGTH_SHORT).show();
            }

            if (x == idmulti.size() - 1) {
                break;
            }


        }

        Printooth.INSTANCE.printer(new PairedPrinter(Printooth.INSTANCE.getPairedPrinter().getName(),
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);

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