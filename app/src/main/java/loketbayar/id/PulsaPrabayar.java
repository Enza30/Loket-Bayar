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
import android.view.inputmethod.InputMethodManager;
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

public class PulsaPrabayar extends AppCompatActivity implements PrintingCallback {
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> providelist = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private ArrayList<String> namaproduklist = new ArrayList<>();
    private ArrayList<String> kodeproduklist = new ArrayList<>();
    private ArrayList<String> kode_produk_biller_list = new ArrayList<>();
    private ArrayList<String> denomlist = new ArrayList<>();
    private ArrayList<String> fee_biller_list = new ArrayList<>();
    private ArrayList<String> fee_app_list = new ArrayList<>();
    private ArrayList<String> harga_biller_list = new ArrayList<>();
    private ArrayList<String> admin_biller_list = new ArrayList<>();
    private ArrayList<String> fee_sub_ca_array = new ArrayList<>();
    ArrayList<String> fee_sub_ca_array_1=new ArrayList<>();
    ArrayList<String> fee_sub_ca_array_2=new ArrayList<>();
    ArrayList<String> fee_sub_ca_array_3=new ArrayList<>();
    private ArrayList<String> fee_ca_array = new ArrayList<>();
    private String username,password;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private ImageView ivIcon;
    public static String dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", admin_biller = "", fee_sub_ca = "",harga_billerr = "", fee_sub_ca_1 = "", fee_sub_ca_2 = "", fee_sub_ca_3 = "";
    public static float saldobayar = 0,saldoawal=0;;
    private Button btPrint, btPdf,btshare;
    private LinearLayout llData,llCetak,llManual;
    Printing printing;
    private String voucher,nominal,produk_id,harga,kodeSMS,hargajual;
    private String idpel,pilihantipeterpilih="",kodeprodukterpilih="",datalist,denomterpilih,namaprodukterpilih;
    private PdfWriter writer;
    private Button btSelesai;
    private String datauser, kodeproduk,dari,id,email,namecontact,phoneNo;
    private EditText etHape,etHargaJual;
    ArrayList<String> listtipe = new ArrayList<>();
    private Button btCheck,btBayar;
    private ImageView ivBack, ivRefresh,ivIcon3;
    private Dialog load;
    private float harga_loket=0,kembali=0;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan,tvPelangganJudul,
            tvTagPln, tvTotalBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul, tvJudulNama,tvJuduls;
    private ImageView ivPrint;
    private String datapaska="",namaproduk="";
    private String responseCodeQuery1, messagequery1, kodeAreaquery1, namaquery1, divrequery1,
            keterangan="", datelquery1,jmltagihanquery1,totalTagihanquery1,productCodequery1,refIDquery1,tagihanquey1untukarray,idPelquery1;
    private String periodequeryhaloaray="",nilaiTagihanqueryhaloaray="",adminqueryhaloaray="",tagihanquery1="",adminquery1="",
            totalqueryhaloaray="",feequeryhaloaray="";
    private String responseCodeQuerybayar, messagequeryQuerybayar,idpelQuerybayar,namaquerybayar, periodequerybayar="",
            jmltagihanquerybayar,tagihanquerybayar,totalTagihan,refquerbayar="";
    private float pengurangan=0,totalbayar=0;
    private SharedPreferences boyprefs;
    private String errCode="";
    private Spinner spTipe,spProvider;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int RESULT_PICK_CONTACT = 3;
    private String TAG = "Contacts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_pulsa_prabayar);
        providelist.add("-Silahkan Pilih-");
        providelist.add("VOUCHER TV");
        providelist.add("VOUCHER GOOGLE PLAY");
        providelist.add("GAME");
        providelist.add("TELKOMSEL");
        providelist.add("TELKOMSEL TRANSFER");
        providelist.add("TELKOMSEL DATA");
        providelist.add("TELKOMSEL TELEPON");
        providelist.add("INDOSAT");
        providelist.add("INDOSAT TRANSFER");
        providelist.add("INDOSAT DATA");
        providelist.add("INDOSAT TELEPON");
        providelist.add("XL");
        providelist.add("XL TRANSFER");
        providelist.add("XL DATA");
  //      providelist.add("XL INTERNET");
        providelist.add("THREE");
        providelist.add("THREE TRANSFER");
        providelist.add("THREE INTERNET");
        providelist.add("AXIS");
        providelist.add("AXIS TRANSFER");
        providelist.add("AXIS DATA");
        providelist.add("BYU");
        providelist.add("SMARTFREN");
        providelist.add("SMARTFREN DATA");
      //  providelist.add("SMART ");
        spProvider = findViewById(R.id.spProvider);
        tvJuduls = findViewById(R.id.tvJuduls);
        llCetak = findViewById(R.id.llCetak);
        btshare = findViewById(R.id.btshare);
        //   ivIcon = findViewById(R.id.ivIcon);
        etHargaJual = findViewById(R.id.etHargaJual);
        //tvJuduls.setText("PulsaPrabayar");
        dari=boyprefs.getString("dari","");
        tvJuduls.setText("PULSA & VOUCHER");
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        llManual = findViewById(R.id.llManual);
        ivIcon3 = findViewById(R.id.ivIcon3);
        btPrint = findViewById(R.id.btPrint);
        etHape = findViewById(R.id.etHp);
        tvPelangganJudul = findViewById(R.id.tvPelangganJudul);
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
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
        boyprefs.edit().remove("PulsaPrabayar").apply();
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
        btBayar.setVisibility(View.VISIBLE);
        btshare.setVisibility(View.GONE);
        // tvAdmin.setText("Rp."+format.format(admins));
        datauser = boyprefs.getString("datauser", "");
        Log.e("datauser",datauser);
        nama_pemilik = PojoMion.AmbilArray(datauser, "nama_pemilik", "dataloket");
        nama_loket = PojoMion.AmbilArray(datauser, "nama_loket", "dataloket");
        kode_loket = PojoMion.AmbilArray(datauser, "kode_loket", "dataloket");
        saldo = PojoMion.AmbilArray(datauser, "saldo", "depositloket");
        kode_ca = PojoMion.AmbilArray(datauser, "kode_ca", "dataloket");
        kode_sub_ca = PojoMion.AmbilArray(datauser, "kode_sub_ca", "dataloket");
        btBayar.setVisibility(View.VISIBLE);
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);
        llData.setVisibility(View.GONE);
        saldobayar= Float.parseFloat(saldo.get(0));
        saldoawal= Float.parseFloat(saldo.get(0));
        Log.e("saldobayar","s:"+saldobayar);
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        new AmbilSaldoTask(PulsaPrabayar.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    String username = boyprefs.getString("username", "");
                    String password = boyprefs.getString("password", "");
                    new LoginTask(PulsaPrabayar.this).execute("login", username, password);
                }  else{
                    Toast.makeText(PulsaPrabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                    tvSaldo.setText("");
                new AmbilSaldoTask(PulsaPrabayar.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                }  else{
                    Toast.makeText(PulsaPrabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }  }
        });
        initview();
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, providelist);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvider.setAdapter(dataAdapter2);
        spProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adaInternet()) {
                pilihantipeterpilih=parent.getItemAtPosition(position).toString();
                if(!pilihantipeterpilih.equalsIgnoreCase("-Silahkan Pilih-")){
                    llData.setVisibility(View.GONE);
                    llCetak.setVisibility(View.GONE);
                    btshare.setVisibility(View.GONE);
                    namaproduklist.clear();
                    kodeproduklist.clear();
                    denomlist.clear();
                    kode_produk_biller_list.clear();
                    new AmbilKategoriPulsaTask(PulsaPrabayar.this)
                            .execute("ambil",username,password,pilihantipeterpilih);
                }
                }  else{
                    Toast.makeText(PulsaPrabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    spProvider.setSelection(0);
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
                if(adaInternet()) {
                closekeyboard();
                llData.setVisibility(View.GONE);
                llCetak.setVisibility(View.GONE);
                etHargaJual.setEnabled(true);
                etHargaJual.setText("");
                idpel=etHape.getText().toString();
                btshare.setVisibility(View.GONE);
                    btBayar.setVisibility(View.GONE);
                if(idpel.isEmpty()){
                    llData.setVisibility(View.GONE);
                    btBayar.setVisibility(View.GONE);
                    Dialog dialog=new Dialog(PulsaPrabayar.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Masukkan Nomor Handphone");
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
                        if(!namaproduklist.isEmpty()){
              //          Log.e("kode_produk_biller",kode_produk_biller);
                //        new Amprods(PulsaPrabayar.this).execute("https://mudahbayar.com/loket/pulsa.php?produk="+kode_produk_biller);
                            llData.setVisibility(View.VISIBLE);
//                            llCetak.setVisibility(View.VISIBLE);
                            btBayar.setVisibility(View.VISIBLE);
                            tvPelangganJudul.setText("NOMOR HANDPHONE");
                            tvidPelanggan.setText(idpel);
                            tvNamas.setText(namaprodukterpilih);
                            tvJudulNama.setText("NAMA PRODUK");
                            tvJudullembar.setText("NOMINAL");
                            tvLembarTagihan.setText(denomterpilih);
                            tvrefidJudul.setText("TOTAL BAYAR");
                            float tagihannns= Float.parseFloat(harga_biller);
                            float feaps= Float.parseFloat(fee_app);
                            float fesubca= Float.parseFloat(fee_sub_ca);
                            float fesubca1= Float.parseFloat(fee_sub_ca_1);
                            float fesubca2= Float.parseFloat(fee_sub_ca_2);
                            float fesubca3= Float.parseFloat(fee_sub_ca_3);
                            float feca= Float.parseFloat(fee_ca);
                            totalbayar=tagihannns+feaps+fesubca+feca+fesubca1+fesubca2+fesubca3;
                            tvRef.setText("Rp."+decimalFormat.format(totalbayar));
                            tvJudulTag.setText("HARGA JUAL");
                        }else {
                            llData.setVisibility(View.GONE);
                            btBayar.setVisibility(View.GONE);
                            btshare.setVisibility(View.GONE);
                            Dialog dialog=new Dialog(PulsaPrabayar.this);
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
                    else {
                        llData.setVisibility(View.GONE);
                        btBayar.setVisibility(View.GONE);
                        btshare.setVisibility(View.GONE);
                        Dialog dialog=new Dialog(PulsaPrabayar.this);
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
                }  else{
                    Toast.makeText(PulsaPrabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();

                }

            }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(PulsaPrabayar.this);
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
                            Toast.makeText(PulsaPrabayar.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(PulsaPrabayar.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()) {
                hargajual=etHargaJual.getText().toString();
                if(hargajual.isEmpty()){
                    Dialog dialog=new Dialog(PulsaPrabayar.this);
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
                }
                else{
                    float hg= Float.parseFloat(hargajual);
                    if(hg<totalbayar){
                        Dialog dialog=new Dialog(PulsaPrabayar.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf=dialog.findViewById(R.id.tvIf);
                        TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                        Button btTidak=dialog.findViewById(R.id.btTidak);
                        tvIf.setText("Harga Jual Tidak Boleh Lebih Kecil dari total bayar");
                        tvTanya.setText("Pesan");
                        btTidak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else if(hg >= totalbayar*2){
                        Dialog dialog=new Dialog(PulsaPrabayar.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf=dialog.findViewById(R.id.tvIf);
                        TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                        Button btTidak=dialog.findViewById(R.id.btTidak);
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
                    else{
                        pengurangan = saldobayar - totalbayar;
                        Log.e("pengurangan", "pengurangan:"+String.valueOf(pengurangan));
                        Log.e("saldobayar", "saldobayar:"+String.valueOf(saldobayar));
                        Log.e("totalbayar", "totalbayar:"+String.valueOf(totalbayar));
                        new PulsaPrabayar.BayarTask(PulsaPrabayar.this).execute(getString(R.string.link)+"apis/android/topup");
                    }
                }
            }  else{
                Toast.makeText(PulsaPrabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }
            }
        });
        ivIcon3.setOnClickListener(new View.OnClickListener() {
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
        if(adaInternet()) {
            String username = boyprefs.getString("username", "");
            String password = boyprefs.getString("password", "");
            new LoginTask(PulsaPrabayar.this).execute("login", username, password);
        }  else{
            Toast.makeText(PulsaPrabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
        }
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
            Log.e("produk_id", "sp:" + produk_id);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("errCode", mainJSONObj.getString("errCode"));
                errCode=mainJSONObj.getString("errCode");
                messagequeryQuerybayar=mainJSONObj.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (errCode.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(PulsaPrabayar.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                try {
                    JSONObject mainJSONObj = new JSONObject(s);
                    Log.e("errCode", mainJSONObj.getString("errCode"));
                    errCode=mainJSONObj.getString("errCode");
                    messagequeryQuerybayar=mainJSONObj.getString("msg");
                    refquerbayar=mainJSONObj.getString("trxID");
                    harga_billerr= String.valueOf(mainJSONObj.getInt("price"));
                    productCodequery1=mainJSONObj.getString("VoucherSN");
                    keterangan=s;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                keterangan=s;
                btBayar.setVisibility(View.GONE);
                etHargaJual.setEnabled(false);
                btBayar.setVisibility(View.GONE);
                llData.setVisibility(View.VISIBLE);
                llCetak.setVisibility(View.VISIBLE);
                btshare.setVisibility(View.VISIBLE);
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
                                boyprefs.edit().putBoolean("PulsaPrabayar", true).apply();
                                new LoginTask(PulsaPrabayar.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(PulsaPrabayar.this, ScanningActivity.class),
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
                                        PulsaPrabayar.this,
                                        PulsaPrabayar.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembelian PULSA PRABAYAR");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Pembelian PULSA PRABAYAR "+pilihantipeterpilih+
                                        "NO HANDPHONE  "+idpel);
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
                                String s = "STRUK BUKTI TOPUP \n"+pilihantipeterpilih+"\n\n"+
                                        "TGL BAYAR : " + formattedDate+"\n"+
                                        "LAYANAN   : " + namaprodukterpilih+"\n"+
                                        "TRX ID    : " + refquerbayar+"\n"+
                                        "NO HANDPHONE : " + idpel+"\n"+
                                        "HARGA        : Rp."+decimalFormat.format(Float.parseFloat(hargajual))+"\n\n"+
                                        "Struk Ini merupakan Bukti Pembayaran Yang Sah"+"\n\n"+
                                        "Terima Kasih"+"\n\n"+
                                        kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username;
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembelian PULSA PRABAYAR "+pilihantipeterpilih+
                                        "NO HANDPHONE  "+idpel);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
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
                        boyprefs.edit().putBoolean("PulsaPrabayar", true).apply();
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
    //ambilkategori
    class AmbilKategoriPulsaTask extends AsyncTask<String, Void, String> {
        private Context ctx;
        private String results;
        AmbilKategoriPulsaTask(Context ctx) {
            this.ctx = ctx;
            llData.setVisibility(View.GONE);
            llCetak.setVisibility(View.GONE);
         //   etHape.setText("");
        }
        private Dialog dialog;
        private Dialog load;
        private String base,login_url;
        private SharedPreferences boyprefs;
        @Override
        protected void onPreExecute() {
            boyprefs =ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
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
            String login_url = ctx.getResources().getString(R.string.xyz)+"ambilprodukovodkk2/";
            String method = params[0];
            if (method.equalsIgnoreCase("ambil")) {
                String username=params[1];
                String password=params[2];
                String kategori=params[3];
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&" +
                            URLEncoder.encode("kategori", "UTF-8") + "=" + URLEncoder.encode(kategori, "UTF-8");
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
            if(dialog!=null){
                dialog.dismiss();
            }
            Log.e("hasilambilkategori",result);
            // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            try {
                if(result.contains("datalist")){
                    namaproduklist= PojoMion.AmbilArray(result,"nama_produk","datalist");
                    kodeproduklist= PojoMion.AmbilArray(result,"kode_produk","datalist");
                    denomlist = PojoMion.AmbilArray(result,"denom", "datalist");
                    admin_biller_list= PojoMion.AmbilArray(result,"admin_biller","datalist");
                    harga_biller_list= PojoMion.AmbilArray(result,"harga_biller","datalist");
                    fee_biller_list= PojoMion.AmbilArray(result,"fee_biller","datalist");
                    fee_app_list= PojoMion.AmbilArray(result,"fee_app","datalist");
                    kode_produk_biller_list= PojoMion.AmbilArray(result,"kode_produk_biller","datalist");
                    admin_biller_list.add(0,"-1");
                    harga_biller_list.add(0,"-1");
                    fee_biller_list.add(0,"-1");
                    fee_app_list.add(0,"-1");
                    namaproduklist.add(0,"-1");
                    kodeproduklist.add(0,"-1");
                    denomlist.add(0,"-Silahkan Pilih-");
                    kode_produk_biller_list.add(0,"-1");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx,
                            android.R.layout.simple_spinner_item, denomlist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTipe.setAdapter(dataAdapter);
                    spTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(adaInternet()){
                            llData.setVisibility(View.GONE);
                            llCetak.setVisibility(View.GONE);
                            pilihantipeterpilih= String.valueOf(parent.getItemAtPosition(position));
                            if(!pilihantipeterpilih.equalsIgnoreCase("-Silahkan Pilih-")){
                                int pos=denomlist.indexOf(pilihantipeterpilih);
                                kodeproduk=kodeproduklist.get(pos);
                                kodeprodukterpilih=kodeproduklist.get(pos);
                                denomterpilih=denomlist.get(pos);
                                namaprodukterpilih=namaproduklist.get(pos);
                                kode_produk_biller=kode_produk_biller_list.get(position);
                                admin_biller=admin_biller_list.get(position);
                                harga_biller=harga_biller_list.get(position);
                                fee_biller=fee_biller_list.get(position);
                                fee_app=fee_app_list.get(position);
                                Log.e("kode_loket",kode_loket.get(0));
                                Log.e("kode_produk_biller",kode_produk_biller);
                                new AmbilCATASK(ctx).execute("ambil",username,password,kodeprodukterpilih,kode_loket.get(0));
                            }
                            }
                            else{
                                Toast.makeText(PulsaPrabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                                spTipe.setSelection(0);
                            }
                        }
                        @Override
                       public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
                else{
                    Dialog dialog=new Dialog(ctx);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Produk Belum Ada Silahkan Menghubungi Admin");
                    tvTanya.setText("Pesan");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            boyprefs.edit().putBoolean("PulsaPrabayar", true).apply();
                            String username=boyprefs.getString("username","");
                            String password=boyprefs.getString("password","");
                            new LoginTask(ctx).execute("login",username,password);
                        }
                    });
                    dialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ctx,"eror:"+result, Toast.LENGTH_LONG).show();
            }
        }
        private void runThread() {
            load=new Dialog(ctx);
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
                            ((Activity)ctx).runOnUiThread(new Runnable() {
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
                        Intent intent=new Intent(ctx, Topup.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        ctx.startActivity(intent);
                        ((Activity)ctx).finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }.start();
        }
    }
    class AmbilCATASK extends AsyncTask<String, Void, String> {
        private Context ctx;
        private String results;
        AmbilCATASK(Context ctx) {
            this.ctx = ctx;
        }
        private Dialog dialog;
        private Dialog load;
        private String base,login_url;
        private SharedPreferences boyprefs;
        @Override
        protected void onPreExecute() {
            boyprefs =ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
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
            String login_url = ctx.getResources().getString(R.string.xyz)+"ambilprodukdankategori/";

            String method = params[0];
            if (method.equalsIgnoreCase("ambil")) {
                String username=params[1];
                String password=params[2];
                String kodeproduk=params[3];
                String kodeloket=params[4];
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&" +
                            URLEncoder.encode("kodeproduk", "UTF-8") + "=" + URLEncoder.encode(kodeproduk, "UTF-8")+ "&" +
                            URLEncoder.encode("kodeloket", "UTF-8") + "=" + URLEncoder.encode(kodeloket, "UTF-8");
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
            if(dialog!=null){
                dialog.dismiss();
            }
            Log.e("hasilca",result);
            // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            try {
                if(result.contains("datamargin")){
                    fee_ca_array= PojoMion.AmbilArray(result,"fee_ca","datamargin");
                    fee_sub_ca_array= PojoMion.AmbilArray(result,"fee_sub_ca","datamargin");
                    fee_sub_ca_array_1 = PojoMion.AmbilArray(result, "fee_sub_ca_1", "datamargin");
                    fee_sub_ca_array_2 = PojoMion.AmbilArray(result, "fee_sub_ca_2", "datamargin");
                    fee_sub_ca_array_3 = PojoMion.AmbilArray(result, "fee_sub_ca_3", "datamargin");
                    fee_ca=fee_ca_array.get(0);
                    fee_sub_ca=fee_sub_ca_array.get(0);
                    fee_sub_ca_1=fee_sub_ca_array_1.get(0);
                    fee_sub_ca_2=fee_sub_ca_array_2.get(0);
                    fee_sub_ca_3=fee_sub_ca_array_3.get(0);
                }else{
                    Dialog dialog=new Dialog(ctx);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvIf=dialog.findViewById(R.id.tvIf);
                    TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
                    Button btTidak=dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Produk Belum Aktif Silahkan Menghubungi Admin");
                    tvTanya.setText("Pesan");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            boyprefs.edit().putBoolean("PulsaPrabayar", true).apply();
                            String username=boyprefs.getString("username","");
                            String password=boyprefs.getString("password","");
                            new LoginTask(ctx).execute("login",username,password);
                        }
                    });
                    dialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ctx,"eror:"+result, Toast.LENGTH_LONG).show();
            }
        }
        private void runThread() {
            load=new Dialog(ctx);
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
                            ((Activity)ctx).runOnUiThread(new Runnable() {

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
                        Intent intent=new Intent(ctx, Topup.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        ctx.startActivity(intent);
                        ((Activity)ctx).finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }.start();
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
                .setText("STRUK BUKTI TOPUP \n"+pilihantipeterpilih)
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
                .setText("LAYANAN   : " + namaprodukterpilih )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TRX ID    : " + refquerbayar)
                .build());

        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO HANDPHONE : " + idpel)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("HARGA        : Rp."+decimalFormat.format(Float.parseFloat(hargajual) ))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("Struk Ini Merupakan Bukti Pembayaran Yang Sah")
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
    private void closekeyboard()
    {
        View view = this.getCurrentFocus();
        if (view !=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

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
        tgl.add("LAYANAN     : " + namaprodukterpilih + "\n");
        tgl.add("Trx ID            : " + refquerbayar + "\n");
        tgl.add("NO HANDPHONE : " + idpel + "\n");
        tgl.add("HARGA         : Rp." +decimalFormat.format(Float.parseFloat(hargajual))+ "\n");
        tgl.add("\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("Struk Ini Merupakan\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+ "\n");
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
