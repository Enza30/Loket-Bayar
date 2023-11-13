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

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class Topup extends AppCompatActivity implements PrintingCallback {
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private ArrayList<String> namaproduklist = new ArrayList<>();
    private ArrayList<String> kodeproduklist = new ArrayList<>();
    private ArrayList<String> kode_produk_biller_list = new ArrayList<>();
    private ArrayList<String> denomlist = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private ImageView ivIcon,ivIcontop;
    public static String dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", harga_billerr = "", admin_biller = "", fee_sub_ca = "", fee_sub_ca_1 = "", fee_sub_ca_2 = "", fee_sub_ca_3 = "";
    public static float saldobayar = 0,saldoawal=0;
    private Button btPrint, btPdf;
    private LinearLayout llData,llCetak,llManual;
    Printing printing;
    private String idpel,pilihantipeterpilih="",kodeprodukterpilih="",datalist,denomterpilih,hargajual;
    private PdfWriter writer;
    private Button btSelesai;
    private String datauser, kodeproduk,dari,username,password,id,email,namecontact,phoneNo;
    private EditText etHape,etHargaJual2;
    ArrayList<String> listtipe = new ArrayList<>();
    private Button btCheck,btBayar,btshare;
    private ImageView ivBack, ivRefresh;
    private Dialog load;
    private float harga_loket=0,kembali=0;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvPelangganJudul,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul, tvJudulNama,tvJuduls,textView16;
    private ImageView ivPrint;
    private String datapaska="",namaproduk="";
    private String responseCodeQuery1, messagequery1, kodeAreaquery1, namaquery1, divrequery1,
            keterangan="", datelquery1,jmltagihanquery1,totalTagihanquery1,productCodequery1,refIDquery1,tagihanquey1untukarray,idPelquery1;
    private String periodequeryhaloaray="",nilaiTagihanqueryhaloaray="",adminqueryhaloaray="",tagihanquery1="",adminquery1="",
            totalqueryhaloaray="",feequeryhaloaray="";
    private String responseCodeQuerybayar, messagequeryQuerybayar,idpelQuerybayar,namaquerybayar, periodequerybayar="",
            jmltagihanquerybayar,tagihanquerybayar,totalTagihan,refquerbayar="",trxid="";
    private float pengurangan=0,totalbayar=0;
    private SharedPreferences boyprefs;
    private Spinner spTipe;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int RESULT_PICK_CONTACT = 3;
    private String TAG = "Contacts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_topup);
        tvJuduls = findViewById(R.id.tvJuduls);
        llCetak = findViewById(R.id.llCetak);
        btshare = findViewById(R.id.btshare);
        ivIcon = findViewById(R.id.ivIcon);
        tvJuduls.setText("TOPUP");
        dari=boyprefs.getString("dari","");
        tvJuduls.setText(dari);
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        btPrint = findViewById(R.id.btPrint);
        etHape = findViewById(R.id.etHp);
        etHargaJual2 = findViewById(R.id.etHargaJual2);
        tvPelangganJudul = findViewById(R.id.tvPelangganJudul);
        llManual = findViewById(R.id.llManual);
        btPdf = findViewById(R.id.btPdf);
        btSelesai = findViewById(R.id.btSelesai);
        Printooth.INSTANCE.init(this);
        tvTotaljudul = findViewById(R.id.tvTotaljudul);
        tvrefidJudul = findViewById(R.id.tvrefidJudul);
        tvJudulNama = findViewById(R.id.tvJudulNama);
        ivIcontop = findViewById(R.id.ivIcontop);
        llData = findViewById(R.id.llData);
        ivPrint = findViewById(R.id.ivPrint);
        spTipe = findViewById(R.id.spTipe);
        btBayar = findViewById(R.id.btBayar);
        tvidPelanggan = findViewById(R.id.tvidPelanggan);
        boyprefs.edit().remove("Topup").apply();
        tvNama = findViewById(R.id.tvNama);
        textView16 = findViewById(R.id.textView16);
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
        textView16.setVisibility(View.GONE);
        if(dari.equalsIgnoreCase("GOPAY")){
            etHape.setHint("Nomor Handphone");
            ivIcon.setImageResource(R.drawable.gopay);
        }
        if(dari.equalsIgnoreCase("SHOPEEPAY")){
            etHape.setHint("Nomor Handphone");
            ivIcon.setImageResource(R.drawable.shopee);
        }
        else if(dari.equalsIgnoreCase("DANA")){
            ivIcon.setImageResource(R.drawable.dana);
            etHape.setHint("Nomor Handphone");
        }
        else if(dari.equalsIgnoreCase("OVO")){
            ivIcon.setImageResource(R.drawable.ovo);
            etHape.setHint("Nomor Handphone");
        }
        else if(dari.equalsIgnoreCase("GAME")){
            ivIcon.setImageResource(R.drawable.game);
            etHape.setHint("Id Pelanggan");
        }
        else if(dari.equalsIgnoreCase("ETOLL")){
            etHape.setHint("Id Pelanggan");
            ivIcon.setImageResource(R.drawable.etoll);
            textView16.setVisibility(View.VISIBLE);
        }
        else if(dari.equalsIgnoreCase("LinkAja")){
            etHape.setHint("Nomor Handphone");
            ivIcon.setImageResource(R.drawable.link);
        }
        datalist=boyprefs.getString("datalist","");
        namaproduklist= PojoMion.AmbilArray(datalist,"nama_produk","datalist");
        kodeproduklist= PojoMion.AmbilArray(datalist,"kode_produk","datalist");
        denomlist= PojoMion.AmbilArray(datalist,"denom","datalist");
        kode_produk_biller_list= PojoMion.AmbilArray(datalist,"kode_produk_biller","datalist");
        namaproduklist.add(0,"-Silahkan Pilih-");
        kodeproduklist.add(0,"-1");
        denomlist.add(0,"-1");
        kode_produk_biller_list.add(0,"-1");
        btBayar.setVisibility(View.VISIBLE);
        btshare.setVisibility(View.GONE);
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
        Log.e("saldobayar","s:"+saldobayar);
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        new AmbilSaldoTask(Topup.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        tvNama.setText(nama_pemilik.get(0));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                new LoginTask(Topup.this).execute("login", username, password);
            }
                else{
                Toast.makeText(Topup.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
                new AmbilSaldoTask(Topup.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }
                else{
                    Toast.makeText(Topup.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
                if(adaInternet()){
                    btshare.setVisibility(View.GONE);
                clearall3();
                llData.setVisibility(View.GONE);
                llCetak.setVisibility(View.GONE);
                pilihantipeterpilih= String.valueOf(parent.getItemAtPosition(position));
                int pos=namaproduklist.indexOf(pilihantipeterpilih);
                kodeproduk=kodeproduklist.get(pos);
                kodeprodukterpilih=kodeproduklist.get(pos);
                denomterpilih=denomlist.get(pos);
                kode_produk_biller=kode_produk_biller_list.get(pos);
            }
                else{
                Toast.makeText(Topup.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    clearall3();
                    spTipe.setSelection(0);
                    llData.setVisibility(View.GONE);
                    llCetak.setVisibility(View.GONE);
                    pilihantipeterpilih= String.valueOf(parent.getItemAtPosition(position));
                    int pos=namaproduklist.indexOf(pilihantipeterpilih);
                    kodeproduk=kodeproduklist.get(pos);
                    kodeprodukterpilih=kodeproduklist.get(pos);
                    denomterpilih=denomlist.get(pos);
                    kode_produk_biller=kode_produk_biller_list.get(pos);
            }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btCheck.setText("CEK HARGA");
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    btshare.setVisibility(View.GONE);
                etHargaJual2.setEnabled(true);
                hargajual="";
                etHargaJual2.setText("");
                idpel=etHape.getText().toString();
                llData.setVisibility(View.GONE);
                llCetak.setVisibility(View.GONE);
                if(idpel.isEmpty()){
                    llData.setVisibility(View.GONE);
                    btBayar.setVisibility(View.GONE);
                    Dialog dialog=new Dialog(Topup.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Masukan No Handphone");
                    tvTanya.setText("Pesan");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else{
                    if(!pilihantipeterpilih.equalsIgnoreCase("-Silahkan Pilih-")){
                        new AProdatmarOvodkkTasks(Topup.this).execute("ambil", kodeproduk, kode_ca.get(0),kode_loket.get(0));
                    }else {
                        llData.setVisibility(View.GONE);
                        btBayar.setVisibility(View.GONE);
                        Dialog dialog=new Dialog(Topup.this);
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
                    Toast.makeText(Topup.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(Topup.this);
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
                            Toast.makeText(Topup.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(Topup.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(adaInternet()){
                hargajual=etHargaJual2.getText().toString();
                if(hargajual.isEmpty()){
                    Dialog dialog=new Dialog(Topup.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Harga Jual Tidak Boleh Kosong");
                    tvTanya.setText("Pesan");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else {
                    float hg = Float.parseFloat(hargajual);
                    if (hg < totalbayar) {
                        Dialog dialog = new Dialog(Topup.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf = dialog.findViewById(R.id.tvIf);
                        TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                        Button btTidak = dialog.findViewById(R.id.btTidak);
                        tvIf.setText("Harga Jual Lebih Kecil Dari Harga");
                        tvTanya.setText("Pesan");
                        btTidak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else if (hg >= totalbayar*2) {
                        Dialog dialog = new Dialog(Topup.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf = dialog.findViewById(R.id.tvIf);
                        TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                        Button btTidak = dialog.findViewById(R.id.btTidak);
                        tvIf.setText("Harga Jual Terlalu tinggi");
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
                        username = boyprefs.getString("username", "");
                        password = boyprefs.getString("password", "");
                        pengurangan = saldobayar - totalbayar;
                        new BayarTask(Topup.this).execute(getString(R.string.link)+"apis/android/topup");
                    }
                }}
                else{
                Toast.makeText(Topup.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }
            }
        });
        ivIcontop.setOnClickListener(new View.OnClickListener() {
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
        new LoginTask(Topup.this).execute("login", username, password);
        }
        else{
            Toast.makeText(Topup.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
    }
    void clearall(){
        etHape.setText("");
        llData.setVisibility(View.GONE);
        idpel="";hargajual="";
        responseCodeQuery1=""; messagequery1=""; kodeAreaquery1=""; namaquery1=""; divrequery1="";
        keterangan=""; datelquery1="";jmltagihanquery1="";totalTagihanquery1="";productCodequery1="";refIDquery1="";
        responseCodeQuerybayar=""; messagequeryQuerybayar="";idpelQuerybayar="";namaquerybayar=""; periodequerybayar="";
        jmltagihanquerybayar="";tagihanquerybayar="";totalTagihan="";denomterpilih="";kodeproduk="";
        refquerbayar="";trxid="";
        saldobayar= Float.parseFloat(saldo.get(0));
    }
    void clearall3(){
     //   etHape.setText("");
        llData.setVisibility(View.GONE);
        idpel="";hargajual="";
        responseCodeQuery1=""; messagequery1=""; kodeAreaquery1=""; namaquery1=""; divrequery1="";
        keterangan=""; datelquery1="";jmltagihanquery1="";totalTagihanquery1="";productCodequery1="";refIDquery1="";
        responseCodeQuerybayar=""; messagequeryQuerybayar="";idpelQuerybayar="";namaquerybayar=""; periodequerybayar="";
        jmltagihanquerybayar="";tagihanquerybayar="";totalTagihan="";denomterpilih="";kodeproduk="";
        refquerbayar="";trxid="";
        saldobayar= Float.parseFloat(saldo.get(0));
    }
    void clearall2(){
        etHape.setText("");
        llData.setVisibility(View.GONE);
        idpel="";hargajual="";
        responseCodeQuery1=""; messagequery1=""; kodeAreaquery1=""; namaquery1=""; divrequery1="";
        keterangan=""; datelquery1="";jmltagihanquery1="";totalTagihanquery1="";productCodequery1="";refIDquery1="";
        responseCodeQuerybayar=""; messagequeryQuerybayar="";idpelQuerybayar="";namaquerybayar=""; periodequerybayar="";
        jmltagihanquerybayar="";tagihanquerybayar="";totalTagihan="";denomterpilih="";kodeproduk="";
        refquerbayar="";trxid="";
        saldobayar= Float.parseFloat(saldo.get(0));
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
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
                    etHape.setText(phoneNo);
                } else {
                    phoneNo=phoneNo.replace("-","").replace("+62","0")
                            .replace(" ","");;
                    phoneNo=phoneNo.trim();
                    etHape.setText(phoneNo);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
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
                jsonRequest.put("kode_produk", kodeproduk);
                jsonRequest.put("idpel", idpel.replace("-","").replace("+62","0")
                        .replace(" ",""));
                jsonRequest.put("nominal", hargajual);
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
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
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
                messagequeryQuerybayar=mainJSONObj.getString("msg");
                responseCodeQuerybayar=mainJSONObj.getString("errCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCodeQuerybayar.equalsIgnoreCase("00")) {
                try {
                    JSONObject mainJSONObj = new JSONObject(s);
                    Log.e("errCode", mainJSONObj.getString("errCode"));
                    refquerbayar=mainJSONObj.getString("VoucherSN");
                    trxid=mainJSONObj.getString("trxID");
                    harga_billerr= String.valueOf(mainJSONObj.getInt("price"));
                    keterangan=s;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new AmbilSaldoTask(Topup.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                etHargaJual2.setEnabled(false);
                btBayar.setVisibility(View.GONE);
                btshare.setVisibility(View.VISIBLE);
                llData.setVisibility(View.VISIBLE);
                llCetak.setVisibility(View.VISIBLE);
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf=dialog.findViewById(R.id.tvIf);
                TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                Button btTidak=dialog.findViewById(R.id.btTidak);
                tvIf.setText("Pembelian Berhasil");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                boyprefs.edit().putBoolean("Topup", true).apply();
  //                              String username = boyprefs.getString("username", "");
    //                            String password = boyprefs.getString("password", "");
      //                          new LoginTask(Topup.this).execute("login", username, password);
                                if(dari.equalsIgnoreCase("GOPAY")){
                                    boyprefs.edit().putString("dari","GOPAY").apply();
                                    new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"GOPAY",kode_loket.get(0));
                                }
                                else if(dari.equalsIgnoreCase("DANA")){
                                    boyprefs.edit().putString("dari","DANA").apply();
                                    new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"DANA",kode_loket.get(0));
                                }
                                else if(dari.equalsIgnoreCase("OVO")){
                                    boyprefs.edit().putString("dari","OVO").apply();
                                    new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"OVO",kode_loket.get(0));
                                }
                                else if(dari.equalsIgnoreCase("GAME")){
                                    boyprefs.edit().putString("dari","GAME").apply();
                                    new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"GAME",kode_loket.get(0));
                                }
                                else if(dari.equalsIgnoreCase("ETOLL")){
                                    boyprefs.edit().putString("dari","ETOLL").apply();
                                    new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"ETOLL",kode_loket.get(0));
                                }
                                else if(dari.equalsIgnoreCase("SHOPEEPAY")){
                                    boyprefs.edit().putString("dari","SHOPEEPAY").apply();
                                    new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"SHOPEE",kode_loket.get(0));
                                }
                                else if(dari.equalsIgnoreCase("LinkAja")){
                                    boyprefs.edit().putString("dari","LinkAja").apply();
                                    new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"LINK",kode_loket.get(0));
                                }
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Topup.this, ScanningActivity.class),
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
                                        + "/PDF/" + idpel + ".pdf");
                                Document document = new Document(PageSize.A5);
                                Uri path = FileProvider.getUriForFile(
                                        Topup.this,
                                        Topup.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "STRUK BUKTI TOPUP");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "STRUK BUKTI TOPUP "+pilihantipeterpilih+
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
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                String formattedDate = df.format(c);
                                if(dari.equalsIgnoreCase("DANA")||
                                        dari.equalsIgnoreCase("GOPAY")||
                                        dari.equalsIgnoreCase("OVO")||
                                        dari.equalsIgnoreCase("SHOPEEPAY")||
                                        dari.equalsIgnoreCase("LinkAja")) {
                                    Log.e("dari",dari);
                                    String y = "STRUK BUKTI TOPUP\n"+pilihantipeterpilih +"\n\n"+
                                            "TGL BAYAR : " + formattedDate+"\n"+
                                            "LAYANAN    : " + pilihantipeterpilih+"\n"+
                                            "NO HANDPHONE : " + idpel+"\n"+
                                            "HARGA        : Rp."+decimalFormat.format(Float.parseFloat(hargajual))+"\n\n"+
                                            "Struk Ini Merupakan Bukti Pembayaran Yang Sah"+"\n\n"+
                                            "Terima Kasih"+"\n\n"+
                                            kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "STRUK BUKTI TOPUP "+pilihantipeterpilih+
                                            "a.n "+namaquery1);
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, y);
                                    startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                                 }
                                else{
                                    String y = "STRUK BUKTI TOPUP\n"+pilihantipeterpilih +"\n"+
                                            "TGL BAYAR : " + formattedDate+"\n"+
                                            "LAYANAN   : " + pilihantipeterpilih+"\n"+
                                            "ID PELANGGAN : " + idpel+"\n"+
                                            "HARGA        : Rp."+decimalFormat.format(Float.parseFloat(hargajual))+"\n"+
                                            "Struk Ini Merupakan Bukti Pembayaran Yang Sah"+"\n"+
                                            "Terima Kasih"+"\n"+
                                            kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "STRUK BUKTI TOPUP "+pilihantipeterpilih+
                                            "a.n "+namaquery1);
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, y);
                                    startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                                }
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
//                        boyprefs.edit().putBoolean("Topup", true).apply();
  //                      String username = boyprefs.getString("username", "");
    //                    String password = boyprefs.getString("password", "");
      //                  new LoginTask(context).execute("login", username, password);
                        if(dari.equalsIgnoreCase("GOPAY")){
                            boyprefs.edit().putString("dari","GOPAY").apply();
                            new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"GOPAY",kode_loket.get(0));
                        }
                        else if(dari.equalsIgnoreCase("DANA")){
                            boyprefs.edit().putString("dari","DANA").apply();
                            new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"DANA",kode_loket.get(0));
                        }
                        else if(dari.equalsIgnoreCase("OVO")){
                            boyprefs.edit().putString("dari","OVO").apply();
                            new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"OVO",kode_loket.get(0));

                        }
                        else if(dari.equalsIgnoreCase("GAME")){
                            boyprefs.edit().putString("dari","GAME").apply();
                            new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"GAME",kode_loket.get(0));
                        }
                        else if(dari.equalsIgnoreCase("ETOLL")){
                            boyprefs.edit().putString("dari","ETOLL").apply();
                            new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"ETOLL",kode_loket.get(0));
                        }
                        else if(dari.equalsIgnoreCase("SHOPEEPAY")){
                            boyprefs.edit().putString("dari","SHOPEEPAY").apply();
                            new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"SHOPEE",kode_loket.get(0));
                        }
                        else if(dari.equalsIgnoreCase("LinkAja")){
                            boyprefs.edit().putString("dari","LinkAja").apply();
                            new AmbilProdukOVOdkkTask(Topup.this).execute("ambil",username,password,"LINK",kode_loket.get(0));
                        }
                    }
                });
                dialog.show();
            }
        }
    }
    public class AProdatmarOvodkkTasks extends AsyncTask<String, Void, String> {
        private Context ctx;
        private String results;
        AProdatmarOvodkkTasks(Context ctx) {
            this.ctx = ctx;
        }
        private Dialog dialog;
        private Dialog load;
        private String base, login_url;
        ArrayList<String> kode_produk_billers = new ArrayList<>();
        ArrayList<String> fee_billers = new ArrayList<>();
        ArrayList<String> fee_apps = new ArrayList<>();
        ArrayList<String> fee_cas = new ArrayList<>();
        ArrayList<String> harga_billers = new ArrayList<>();
        ArrayList<String> admin_billers = new ArrayList<>();
        ArrayList<String> fee_sub_cas = new ArrayList<>();
        ArrayList<String> fee_sub_cas_1 = new ArrayList<>();
        ArrayList<String> fee_sub_cas_2 = new ArrayList<>();
        ArrayList<String> fee_sub_cas_3 = new ArrayList<>();
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
            String login_url = ctx.getResources().getString(R.string.xyz) + "ambilproduk3/";
            String method = params[0];
            if (method.equalsIgnoreCase("ambil")) {
                String tag = params[1];
                String kodeca = params[2];
                String kodeloket = params[3];
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("tag", "UTF-8") + "=" + URLEncoder.encode(tag, "UTF-8") + "&" +
                            URLEncoder.encode("kodeloket", "UTF-8") + "=" + URLEncoder.encode(kodeloket, "UTF-8") + "&" +
                            URLEncoder.encode("kodeca", "UTF-8") + "=" + URLEncoder.encode(kodeca, "UTF-8");
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
            Log.e("dataproduk", result);
            // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            try {
                if (result.contains("dataproduk")) {
                    results = result;
                    llData.setVisibility(View.VISIBLE);
                    btBayar.setVisibility(View.VISIBLE);
                    Log.e("dataproduk", result);
                    kode_produk_billers = PojoMion.AmbilArray(results, "kode_produk_biller", "dataproduk");
                    fee_billers = PojoMion.AmbilArray(results, "fee_biller", "dataproduk");
                    fee_apps = PojoMion.AmbilArray(results, "fee_app", "dataproduk");
                    fee_cas = PojoMion.AmbilArray(results, "fee_ca", "datamargin");
                    fee_sub_cas = PojoMion.AmbilArray(results, "fee_sub_ca", "datamargin");
                    fee_sub_cas_1 = PojoMion.AmbilArray(results, "fee_sub_ca_1", "datamargin");
                    fee_sub_cas_2 = PojoMion.AmbilArray(results, "fee_sub_ca_2", "datamargin");
                    fee_sub_cas_3 = PojoMion.AmbilArray(results, "fee_sub_ca_3", "datamargin");
                    harga_billers = PojoMion.AmbilArray(results, "harga_biller", "dataproduk");
                    saldo = PojoMion.AmbilArray(results, "saldo", "datasaldo");
                    saldobayar= Float.parseFloat(saldo.get(0));
                    DecimalFormat decimalFormat=new DecimalFormat("###,###,###.##");
                    tvSaldo.setText("Rp."+decimalFormat.format(saldobayar));
                    admin_billers = PojoMion.AmbilArray(results, "admin_biller", "dataproduk");
                    Log.e("admin_biller amprod", admin_billers.get(0));
                    Topup.kode_produk_biller = kode_produk_billers.get(0);
                    Topup.harga_biller = harga_billers.get(0);
                    Topup.admin_biller = admin_billers.get(0);
                if (fee_cas.isEmpty()) {
                        fee_ca = "0";
                        llData.setVisibility(View.GONE);
                        btBayar.setVisibility(View.GONE);
                        Dialog dialog = new Dialog(ctx);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf = dialog.findViewById(R.id.tvIf);
                        TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                        Button btTidak = dialog.findViewById(R.id.btTidak);
                        tvIf.setText("Produk Belum Aktif Silahkan Menghubungi Admin");
                        tvTanya.setText("Pesan");
                        btTidak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                boyprefs.edit().putBoolean("Topup", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(ctx).execute("login", username, password);
                            }
                        });
                        dialog.show();
                    } else {
                        llData.setVisibility(View.VISIBLE);
                        btBayar.setVisibility(View.VISIBLE);
                        fee_ca = fee_cas.get(0);
                        fee_sub_ca = fee_sub_cas.get(0);
                    fee_sub_ca_1 = fee_sub_cas_1.get(0);
                    fee_sub_ca_2 = fee_sub_cas_2.get(0);
                    fee_sub_ca_3 = fee_sub_cas_3.get(0);
                    }
                    fee_app = fee_apps.get(0);
                    fee_biller = fee_billers.get(0);
                    dataproduk = results;
                    runThread();
                    if(dari.equalsIgnoreCase("DANA")||
                            dari.equalsIgnoreCase("GOPAY")||
                            dari.equalsIgnoreCase("OVO")||
                            dari.equalsIgnoreCase("SHOPEEPAY")||
                            dari.equalsIgnoreCase("LinkAja")) {
                        tvPelangganJudul.setText("NOMOR HANDPHONE");
                    }else{
                        tvPelangganJudul.setText("ID PELANGGAN");
                    }
                    tvidPelanggan.setText(idpel);
                    tvJudulNama.setText("NAMA PRODUK");
                    tvNamas.setText(pilihantipeterpilih);
                    tvJudullembar.setText("NOMINAL");
                    float den= Float.parseFloat(denomterpilih);
                    tvLembarTagihan.setText("Rp."+decimalFormat.format(den));
                    float harbil= Float.parseFloat(harga_biller);
                    float feap= Float.parseFloat(fee_app);
                    float feca= Float.parseFloat(fee_ca);
                    float fesubca= Float.parseFloat(fee_sub_ca);
                    float fesubca1= Float.parseFloat(fee_sub_ca_1);
                    float fesubca2= Float.parseFloat(fee_sub_ca_2);
                    float fesubca3= Float.parseFloat(fee_sub_ca_3);
                    totalbayar=harbil+feap+feca+fesubca+fesubca1+fesubca2+fesubca3;
                    tvrefidJudul.setText("HARGA");
                    tvRef.setText("Rp."+decimalFormat.format(totalbayar));
                    tvJudulTag.setText("HARGA JUAL");
                    tvTagPln.setVisibility(View.GONE);
                    tvTotalBayar.setVisibility(View.GONE);
                    tvTotaljudul.setVisibility(View.GONE);
                } else {
                    llData.setVisibility(View.GONE);
                    btBayar.setVisibility(View.GONE);
                    Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                    boyprefs.edit().putBoolean("Topup", true).apply();
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(ctx).execute("login", username, password);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ctx, "eror:" + result, Toast.LENGTH_LONG).show();
            }
        }
    }
    private void runThread() {
        load=new Dialog(Topup.this);
        load.setContentView(R.layout.dialogload2);
        load.setCancelable(false);
        Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ivX=load.findViewById(R.id.ivX);
        load.show();
        new Thread() {
            public void run() {
                int jh = 0;
                while (jh <20) {
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
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    load.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }.start();
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
                .setText("STRUK BUKTI TOPUP")
                .build());
         printables.add(new TextPrintable.Builder()
                 .setText("\n")
                 .build());
         printables.add(new TextPrintable.Builder()
                 .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                 .setText(pilihantipeterpilih)
                 .build());
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String formattedDate = df.format(c);
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR : " + formattedDate)
                .build());
         printables.add(new TextPrintable.Builder()
                .setText("\n")
                 .build());
        printables.add(new TextPrintable.Builder()
                .setText("LAYANAN   : " + pilihantipeterpilih )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        if(dari.equalsIgnoreCase("DANA")||
                dari.equalsIgnoreCase("GOPAY")||
                dari.equalsIgnoreCase("OVO")||
                dari.equalsIgnoreCase("SHOPEEPAY")||
                dari.equalsIgnoreCase("LinkAja")) {
            Log.e("dari",dari);
            printables.add(new TextPrintable.Builder()
                    .setText("NO HANDPHONE : " + idpel)
                    .build());
        }
        else{
            printables.add(new TextPrintable.Builder()
                    .setText("ID PELANGGAN : " + idpel)
                    .build());
        }
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("HARGA        : Rp."+decimalFormat.format(Float.parseFloat(hargajual)))
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
        cv.add("STRUK BUKTI TOPUP\n"+pilihantipeterpilih+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(70);
        tgl.setIndentationRight(70);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String formattedDate = df.format(c);
        tgl.add("TGL BAYAR  : " + formattedDate + "\n");
        tgl.add("LAYANAN     : " + pilihantipeterpilih + "\n");
        if(dari.equalsIgnoreCase("DANA")||
                dari.equalsIgnoreCase("GOPAY")||
                dari.equalsIgnoreCase("OVO")||
                dari.equalsIgnoreCase("SHOPEEPAY")||
                dari.equalsIgnoreCase("LinkAja")) {
            tgl.add("NO HANDPHONE : " + idpel + "\n");
        }else{
            tgl.add("ID PELANGGAN      : " + idpel + "\n");
        }
        tgl.add("HARGA                  : Rp." +decimalFormat.format(Float.parseFloat(hargajual))+ "\n");
        tgl.add("\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("Struk ini Merupakan\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0) + " / "+ username+ "\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
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