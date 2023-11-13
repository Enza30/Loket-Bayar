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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class Paskabayar extends AppCompatActivity implements PrintingCallback {
    private ArrayList<String> nama_pemilik = new ArrayList<>();
    private ArrayList<String> nama_loket = new ArrayList<>();
    private ArrayList<String> kode_loket = new ArrayList<>();
    private ArrayList<String> saldo = new ArrayList<>();
    private ArrayList<String> kode_ca = new ArrayList<>();
    private ArrayList<String> kode_sub_ca = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    public static String dataproduk = "", kode_produk_biller = "", fee_biller = "", fee_app = "", fee_ca = "",
            harga_biller = "", admin_biller = "", fee_sub_ca = "",fee_sub_ca_1 = "", fee_sub_ca_2 = "", fee_sub_ca_3 = "";
    public static float saldobayar = 0, saldoawal = 0;
    private Button btPrint, btPdf,btSelesai,btCheck, btBayar,btshare;
    private LinearLayout llCetak, llData;
    Printing printing;
    private String idpel, pilihantipeterpilih = "", kodeprodukterpilih = "",datauser, kodeproduk,id,email,namecontact,phoneNo,
            datapaska = "", namaproduk = "", username, password,responseCodeQuery1, responsemsg, messagequery1, kodeAreaquery1, namaquery1,
            divrequery1,keterangan = "", datelquery1, jmltagihanquery1, totalTagihanquery1, productCodequery1, refIDquery1,
            tagihanquey1untukarray, idPelquery1,periodequeryhaloaray = "", nilaiTagihanqueryhaloaray = "",
            adminqueryhaloaray = "", tagihanquery1 = "", adminquery1 = "",totalqueryhaloaray = "", feequeryhaloaray = "",
            tagihanquery2 = "",responseCodeQuerybayar, messagequeryQuerybayar, idpelQuerybayar, namaquerybayar,
            periodequerybayar = "",jmltagihanquerybayar, tagihanquerybayar, totalTagihan, refquerbayar = "",FeeSchema,FeeBulanan;
    private PdfWriter writer;
    private EditText etHape;
    ArrayList<String> listtipe = new ArrayList<>();
    private ImageView ivBack, ivRefresh,ivPrint,ivIcon;
    private float harga_loket = 0,kembali=0,pengurangan = 0, totalbayar = 0;
    private TextView tvNama, tvNamaLoket, tvKodeloket, tvSaldo, tvidPelanggan, tvNamas, tvLembarTagihan, tvPelangganJudul,
            tvTagPln, tvhrgloket,tvTotalBayar, tvnilaiBayar, tvRef, tvJudullembar, tvJudulTag, tvTotaljudul, tvrefidJudul, tvnilaijudul, tvJudulNama, tvJuduls;
    private SharedPreferences boyprefs;
    private Spinner spTipe;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int RESULT_PICK_CONTACT = 3;
    private String TAG = "Contacts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_paskabayar);
        tvJuduls = findViewById(R.id.tvJuduls);
        tvJuduls.setText("PASCA BAYAR");
        tvJudullembar = findViewById(R.id.tvJudullembar);
        tvJudulTag = findViewById(R.id.tvJudulTag);
        btPrint = findViewById(R.id.btPrint);
        ivIcon = findViewById(R.id.ivIcon);
        btshare = findViewById(R.id.btshare);
        etHape = findViewById(R.id.etHp);
        tvPelangganJudul = findViewById(R.id.tvPelangganJudul);
        username = boyprefs.getString("username", "");
        password = boyprefs.getString("password", "");
        btPdf = findViewById(R.id.btPdf);
        btSelesai = findViewById(R.id.btSelesai);
        Printooth.INSTANCE.init(this);
        tvnilaijudul = findViewById(R.id.tvnilaijudul);
        tvTotaljudul = findViewById(R.id.tvTotaljudul);
        tvrefidJudul = findViewById(R.id.tvrefidJudul);
        tvJudulNama = findViewById(R.id.tvJudulNama);
        llData = findViewById(R.id.llData);
        llCetak = findViewById(R.id.llCetak);
        ivPrint = findViewById(R.id.ivPrint);
        llCetak.setVisibility(View.GONE);
        btshare.setVisibility(View.GONE);
        spTipe = findViewById(R.id.spTipe);
        btBayar = findViewById(R.id.btBayar);
        tvidPelanggan = findViewById(R.id.tvidPelanggan);
        boyprefs.edit().remove("paskabayar").apply();
        tvNama = findViewById(R.id.tvNama);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);
        tvRef = findViewById(R.id.tvRef);
        tvTagPln = findViewById(R.id.tvTagPln);
        tvhrgloket = findViewById(R.id.tvhrgloket);
        tvnilaiBayar = findViewById(R.id.tvnilaiBayar);
        tvNamas = findViewById(R.id.tvNamas);
        ivBack = findViewById(R.id.ivBack);
        tvNamaLoket = findViewById(R.id.tvNamaLoket);
        tvKodeloket = findViewById(R.id.tvKodeloket);
        tvLembarTagihan = findViewById(R.id.tvLembarTagihan);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivRefresh = findViewById(R.id.ivRefresh);
        btCheck = findViewById(R.id.btCheck);
        listtipe.add("-Silahkan Pilih-");
        listtipe.add("TELKOMSEL HALO");
        listtipe.add("INDOSAT / MATRIKS");
        listtipe.add("THREE PASCABAYAR");
        listtipe.add("XL PASCABAYAR");
        listtipe.add("SMARTFREN PASCABAYAR");
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
        tvNamaLoket.setText(nama_loket.get(0)+" - "+kode_loket.get(0));
        tvKodeloket.setText(username);
        llData.setVisibility(View.GONE);
        saldobayar = Float.parseFloat(saldo.get(0));
        saldoawal = Float.parseFloat(saldo.get(0));
        Log.e("saldobayar", "s:" + saldobayar);
        tvSaldo.setText("Rp." + decimalFormat.format(saldobayar));
        tvNama.setText(nama_pemilik.get(0));
        String username = boyprefs.getString("username", "");
        String password = boyprefs.getString("password", "");
        new AmbilSaldoTask(Paskabayar.this, tvSaldo).execute("ambil", username, password, kode_loket.get(0));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                new LoginTask(Paskabayar.this).execute("login", username, password);
                }
                else{
                    Toast.makeText(Paskabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                } }
        });
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    tvSaldo.setText("");
                String username = boyprefs.getString("username", "");
                String password = boyprefs.getString("password", "");
                new AmbilSaldoTask(Paskabayar.this, tvSaldo).execute("ambil", username, password, kode_loket.get(0));
                }
                else{
                    Toast.makeText(Paskabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
                clearall3();
                llData.setVisibility(View.GONE);
                llCetak.setVisibility(View.GONE);
                pilihantipeterpilih = String.valueOf(parent.getItemAtPosition(position));
                if (pilihantipeterpilih.equalsIgnoreCase("TELKOMSEL HALO")) {
                    kodeproduk = "HALO";
                    kodeprodukterpilih = "TELKOMSELLHALO";
                    new AmbilProdukPaskaBayarTask(Paskabayar.this).execute("ambil", kodeproduk, kode_loket.get(0));
                } else if (pilihantipeterpilih.equalsIgnoreCase("INDOSAT / MATRIKS")) {
                    kodeproduk = "MATRIX";
                    kodeprodukterpilih = "MATRIX";
                    new AmbilProdukPaskaBayarTask(Paskabayar.this).execute("ambil", kodeproduk, kode_loket.get(0));
                } else if (pilihantipeterpilih.equalsIgnoreCase("THREE PASCABAYAR")) {
                    kodeproduk = "TRI";
                    kodeprodukterpilih = "THREEPOSTP";
                    new AmbilProdukPaskaBayarTask(Paskabayar.this).execute("ambil", kodeproduk, kode_loket.get(0));
                } else if (pilihantipeterpilih.equalsIgnoreCase("XL PASCABAYAR")) {
                    kodeproduk = "XPLOR";
                    kodeprodukterpilih = "XPLOR";
                    new AmbilProdukPaskaBayarTask(Paskabayar.this).execute("ambil", kodeproduk, kode_loket.get(0));
                } else if (pilihantipeterpilih.equalsIgnoreCase("SMARTFREN PASCABAYAR")) {
                    kodeproduk = "SMARTFREN";
                    kodeprodukterpilih = "SMARTFREN";
                    new AmbilProdukPaskaBayarTask(Paskabayar.this).execute("ambil", kodeproduk, kode_loket.get(0));
                } else {
                    kodeproduk = pilihantipeterpilih;
                    kodeprodukterpilih = pilihantipeterpilih;
                }
            }
                else{
                Toast.makeText(Paskabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                    spTipe.setSelection(0);
            }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    btshare.setVisibility(View.GONE);
                llCetak.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
                idpel = etHape.getText().toString();
                Log.e("dataproduk", dataproduk);
                responsemsg="";
                if (idpel.isEmpty()) {
                    //    Snackbar.make(v, "", Snackbar.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog(Paskabayar.this);
                    dialog.setContentView(R.layout.dialogok);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvIf = dialog.findViewById(R.id.tvIf);
                    TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                    Button btTidak = dialog.findViewById(R.id.btTidak);
                    tvIf.setText("Masukkan No Handphone");
                    tvTanya.setText("Pesan");
                    btTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    if (kodeprodukterpilih.equalsIgnoreCase("TELKOMSELLHALO")) {
                        new AmbilqueryCheckHalo(Paskabayar.this)
                                .execute(getString(R.string.link)+"apis/android/ppob/inquiry");
                    }
                    else if (kodeprodukterpilih.equalsIgnoreCase("-Silahkan Pilih-")) {
                        Dialog dialog = new Dialog(Paskabayar.this);
                        dialog.setContentView(R.layout.dialogok);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvIf = dialog.findViewById(R.id.tvIf);
                        TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                        Button btTidak = dialog.findViewById(R.id.btTidak);
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
                    else {
                        new AmbilqueryCheckUmumTask(Paskabayar.this)
                                .execute(getString(R.string.link)+"apis/android/ppob/inquiry");
                    }
                }
            }
                else{
                Toast.makeText(Paskabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }
            }
        });
        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Dialog dialog = new Dialog(Paskabayar.this);
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
                            Toast.makeText(Paskabayar.this, "Printer Is Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(Paskabayar.this, "No Printer connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                if (kodeprodukterpilih.equalsIgnoreCase("TELKOMSELLHALO")) {
                    Log.e("admin_biller bayar", "adm" + admin_biller);
                    totalbayar = Float.parseFloat(totalqueryhaloaray);
                    int totbay = (int) totalbayar;
                    int a = (int) (Integer.parseInt(admin_biller));
                    int b = (int) (Integer.parseInt(fee_biller));
                    int c = Integer.parseInt(nilaiTagihanqueryhaloaray);
                    int d = (int) (Integer.parseInt(fee_app));
                    int e = (int) (Integer.parseInt(fee_ca));
                    int f = (int) (Integer.parseInt(fee_sub_ca));
                    int fsca1 = (int) (Integer.parseInt(fee_sub_ca_1));
                    int fsca2 = (int) (Integer.parseInt(fee_sub_ca_2));
                    int fsca3 = (int) (Integer.parseInt(fee_sub_ca_3));
                    // int g = (int) c;
                    harga_biller = String.valueOf(c + (a - b));
                    harga_loket = Float.parseFloat(harga_biller) + d + e + f+fsca1+fsca2+fsca3;
                    pengurangan = saldobayar - harga_loket;
                    new Paskabayar.BayarTask(Paskabayar.this).execute(getString(R.string.link)+"apis/android/ppob/payment");
                } else {
                    Log.e("admin_biller bayar", "adm" + admin_biller);
                    totalbayar = Float.parseFloat(totalTagihanquery1);
                    int totbay = (int) totalbayar;
                    int a = (int) (Integer.parseInt(admin_biller));
                    int b = (int) (Integer.parseInt(fee_biller));
                    int c = Integer.parseInt(tagihanquery1);
                    int d = (int) (Integer.parseInt(fee_app));
                    int e = (int) (Integer.parseInt(fee_ca));
                    int f = (int) (Integer.parseInt(fee_sub_ca));
                    int fsca1 = (int) (Integer.parseInt(fee_sub_ca_1));
                    int fsca2 = (int) (Integer.parseInt(fee_sub_ca_2));
                    int fsca3 = (int) (Integer.parseInt(fee_sub_ca_3));
                    //int g = (int) c;
                    harga_biller = String.valueOf(c + (a - b));
                    harga_loket = Float.parseFloat(harga_biller) + d + e + f+fsca1+fsca2+fsca3;
                    pengurangan = saldobayar - harga_loket;
                    new Paskabayar.BayarTask(Paskabayar.this).execute(getString(R.string.link)+"apis/android/ppob/payment");
                }
                }
                else{
                    Toast.makeText(Paskabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivIcon.setOnClickListener(new View.OnClickListener() {
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
            new LoginTask(Paskabayar.this).execute("login", username, password);
        }  else{
            Toast.makeText(Paskabayar.this, "Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
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
    @SuppressLint("Range")
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
    void clearall() {
        etHape.setText("");
        llData.setVisibility(View.GONE);
        idpel = "";
        responseCodeQuery1 = "";
        messagequery1 = "";
        kodeAreaquery1 = "";
        namaquery1 = "";
        divrequery1 = "";
        keterangan = "";
        datelquery1 = "";
        jmltagihanquery1 = "";
        totalTagihanquery1 = "";
        productCodequery1 = "";
        refIDquery1 = "";
        responseCodeQuerybayar = "";
        messagequeryQuerybayar = "";
        idpelQuerybayar = "";
        namaquerybayar = "";
        periodequerybayar = "";
        jmltagihanquerybayar = "";
        tagihanquerybayar = "";
        totalTagihan = "";
        refquerbayar = "";
        saldobayar = Float.parseFloat(saldo.get(0));
        responsemsg="";
    }
    void clearall3() {
//        etHape.setText("");
        llData.setVisibility(View.GONE);
        idpel = "";
        responseCodeQuery1 = "";
        messagequery1 = "";
        kodeAreaquery1 = "";
        namaquery1 = "";
        divrequery1 = "";
        keterangan = "";
        datelquery1 = "";
        jmltagihanquery1 = "";
        totalTagihanquery1 = "";
        productCodequery1 = "";
        refIDquery1 = "";
        responseCodeQuerybayar = "";
        messagequeryQuerybayar = "";
        idpelQuerybayar = "";
        namaquerybayar = "";
        periodequerybayar = "";
        jmltagihanquerybayar = "";
        tagihanquerybayar = "";
        totalTagihan = "";
        refquerbayar = "";
        saldobayar = Float.parseFloat(saldo.get(0));
        responsemsg="";
    }
    void clearall2() {
        llData.setVisibility(View.GONE);
        idpel = "";
        responsemsg="";
        responseCodeQuerybayar = "";
        messagequeryQuerybayar = "";
        idpelQuerybayar = "";
        namaquerybayar = "";
        periodequerybayar = "";
        jmltagihanquerybayar = "";
        tagihanquerybayar = "";
        totalTagihan = "";
        refquerbayar = "";
        saldobayar = Float.parseFloat(saldo.get(0));
    }

    @Override
    public void disconnected() {

    }

    public class AmbilqueryCheckHalo extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private Dialog dialogload;
        AmbilqueryCheckHalo(Context context) {
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
                jsonRequest.put("idpel", idpel.replace("-","").replace("+62","0")
                        .replace(" ",""));
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
                responsemsg = mainJSONObj.getString("message");
                FeeSchema = mainJSONObj.getString("FeeSchema");
                FeeBulanan = mainJSONObj.getString("FeeBulanan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCodeQuery1.equalsIgnoreCase("00")) {
                datapaska = s;
                llData.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.VISIBLE);
                mulai();
            }
            else {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf = dialog.findViewById(R.id.tvIf);
                TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                Button btTidak = dialog.findViewById(R.id.btTidak);
                tvIf.setText(responsemsg);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        llData.setVisibility(View.GONE);
                        boyprefs.edit().putBoolean("paskabayar", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
        void mulai() {
            Log.e("datapaska", datapaska);
            Log.e("dataproduk", dataproduk);
            try {
                JSONObject mainJSONObj = new JSONObject(datapaska);
                responseCodeQuery1 = mainJSONObj.getString("responseCode");
                messagequery1 = mainJSONObj.getString("message");
                idpel = mainJSONObj.getString("idpel");
                kodeAreaquery1 = mainJSONObj.getString("kodeArea");
                divrequery1 = mainJSONObj.getString("divre");
                datelquery1 = mainJSONObj.getString("datel");
                namaquery1 = mainJSONObj.getString("nama");
                jmltagihanquery1 = mainJSONObj.getString("jumlahTagihan");
                totalTagihanquery1 = mainJSONObj.getString("totalTagihan");
                productCodequery1 = mainJSONObj.getString("productCode");
                refIDquery1 = mainJSONObj.getString("refID");
                tagihanquey1untukarray = mainJSONObj.getString("tagihan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                // jsonString is a string variable that holds the JSON
                JSONArray itemArray = new JSONArray(tagihanquey1untukarray);
                for (int i = 0; i < itemArray.length(); i++) {
                    String value = itemArray.getString(i);
                    //   Log.e("json", i+"="+value);
                    periodequerybayar = PojoMion.AmbilArray2(value, "periode");
                    nilaiTagihanqueryhaloaray = PojoMion.AmbilArray2(value, "nilaiTagihan");
                    adminqueryhaloaray = PojoMion.AmbilArray2(value, "admin");
                    totalqueryhaloaray = PojoMion.AmbilArray2(value, "total");
                    feequeryhaloaray = PojoMion.AmbilArray2(value, "fee");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tvPelangganJudul.setText("NOMOR HP");
            tvidPelanggan.setText(idpel);
            tvNamas.setText(namaquery1);
            tvJudullembar.setText("Periode");
            tvLembarTagihan.setText(periodequerybayar);
            tvrefidJudul.setText("Jumlah Tagihan");
            tvRef.setText(jmltagihanquery1);
            tvnilaijudul.setText("Nilai Tagihan");
            float nilaitg = Float.parseFloat(nilaiTagihanqueryhaloaray);
            tvnilaiBayar.setText("Rp." + decimalFormat.format(nilaitg));
            tvJudulTag.setText("ADMIN");
            float addsim = Float.parseFloat(adminqueryhaloaray);
            tvTagPln.setText("Rp." + decimalFormat.format(addsim));
            tvTotaljudul.setText("Total Bayar");
            float otag = Float.parseFloat(totalTagihanquery1);
            tvTotalBayar.setText("Rp." + decimalFormat.format(otag));
            int a = (int) (Integer.parseInt(admin_biller));
            int b = (int) (Integer.parseInt(fee_biller));
            int c = Integer.parseInt(nilaiTagihanqueryhaloaray);
            int d = (int) (Integer.parseInt(fee_app));
            int e = (int) (Integer.parseInt(fee_ca));
            int f = (int) (Integer.parseInt(fee_sub_ca));
            int fsca1 = (int) (Integer.parseInt(fee_sub_ca_1));
            int fsca2 = (int) (Integer.parseInt(fee_sub_ca_2));
            int fsca3 = (int) (Integer.parseInt(fee_sub_ca_3));
            //int g = (int) c;
            harga_biller = String.valueOf(c + (a - b));
            harga_loket = Float.parseFloat(harga_biller) + d + e + f+fsca1+fsca2+fsca3;
            if (FeeSchema.equalsIgnoreCase("DEPOSIT")) {
                if (FeeBulanan.equalsIgnoreCase("OFF")) {
                    tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(harga_loket));
                }
                else {
                    tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(otag));
                }
            }
            else {
                tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(otag));

            }


        }
    }
    public class AmbilqueryCheckUmumTask extends AsyncTask<String, String, String> {
        private Context context;
        private SharedPreferences boyprefs;
        private Dialog dialogload;
        AmbilqueryCheckUmumTask(Context context) {
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
                jsonRequest.put("idpel", idpel.replace("-","").replace("+62","0")
                        .replace(" ",""));
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
                responsemsg = mainJSONObj.getString("message");
                FeeSchema = mainJSONObj.getString("FeeSchema");
                FeeBulanan = mainJSONObj.getString("FeeBulanan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCodeQuery1.equalsIgnoreCase("00")) {
                datapaska = s;
                llData.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.VISIBLE);
                mulai2();
            } else {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf = dialog.findViewById(R.id.tvIf);
                TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                Button btTidak = dialog.findViewById(R.id.btTidak);
                tvIf.setText(responsemsg);
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        llData.setVisibility(View.GONE);
                        boyprefs.edit().putBoolean("paskabayar", true).apply();
                        String username = boyprefs.getString("username", "");
                        String password = boyprefs.getString("password", "");
                        new LoginTask(context).execute("login", username, password);
                    }
                });
                dialog.show();
            }
        }
        void mulai2() {
            Log.e("datapaska", datapaska);
            Log.e("dataproduk", dataproduk);
            try {
                JSONObject mainJSONObj = new JSONObject(datapaska);
                responseCodeQuery1 = mainJSONObj.getString("responseCode");
                periodequerybayar = mainJSONObj.getString("periode");
                messagequery1 = mainJSONObj.getString("message");
                idPelquery1 = mainJSONObj.getString("idPel");
                namaquery1 = mainJSONObj.getString("nama");
                jmltagihanquery1 = mainJSONObj.getString("jumlahTagihan");
                totalTagihanquery1 = mainJSONObj.getString("totalTagihan");
                productCodequery1 = mainJSONObj.getString("productCode");
                refIDquery1 = mainJSONObj.getString("refID");
                tagihanquery1 = mainJSONObj.getString("tagihan");
                adminquery1 = mainJSONObj.getString("admin");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvPelangganJudul.setText("NOMOR HP");
            tvidPelanggan.setText(idPelquery1);
            tvNamas.setText(namaquery1);
            tvJudullembar.setText("Periode");
            tvLembarTagihan.setText(periodequerybayar);
            tvrefidJudul.setText("Jumlah Tagihan");
            tvnilaijudul.setText("Nilai Tagihan");
            float nilaitg = Float.parseFloat(tagihanquery1);
            tvnilaiBayar.setText("Rp." + decimalFormat.format(nilaitg));
            tvRef.setText(jmltagihanquery1);
            tvJudulTag.setText("ADMIN");
            float addsim = Float.parseFloat(adminquery1);
            tvTagPln.setText("Rp." + decimalFormat.format(addsim));
            tvTotaljudul.setText("Total Bayar");
            float otag = Float.parseFloat(totalTagihanquery1);
            tvTotalBayar.setText("Rp." + decimalFormat.format(otag));
            int a = (int) (Integer.parseInt(admin_biller));
            int b = (int) (Integer.parseInt(fee_biller));
            int c = Integer.parseInt(tagihanquery1);
            int d = (int) (Integer.parseInt(fee_app));
            int e = (int) (Integer.parseInt(fee_ca));
            int f = (int) (Integer.parseInt(fee_sub_ca));
            int fsca1 = (int) (Integer.parseInt(fee_sub_ca_1));
            int fsca2 = (int) (Integer.parseInt(fee_sub_ca_2));
            int fsca3 = (int) (Integer.parseInt(fee_sub_ca_3));
            //int g = (int) c;
            harga_biller = String.valueOf(c + (a - b));
            harga_loket = Float.parseFloat(harga_biller) + d + e + f+fsca1+fsca2+fsca3;
            if (FeeSchema.equalsIgnoreCase("DEPOSIT")) {
                if (FeeBulanan.equalsIgnoreCase("OFF")) {
                    tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(harga_loket));
                }
                else {
                    tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(otag));
                }
            }
            else {
                tvhrgloket.setText("Saldo akan terpotong " + decimalFormat.format(otag));

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
                if (kodeprodukterpilih.equalsIgnoreCase("TELKOMSELLHALO")) {
                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("operator", username);
                    jsonRequest.put("kode_produk", kode_produk_biller);
                    jsonRequest.put("refid", refIDquery1);
                    jsonRequest.put("nominal", harga_loket);
                    jsonRequest.put("admin", adminqueryhaloaray);
                    // Write Request to output stream to server.
                    DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                    out.writeBytes(jsonRequest.toString());
                    out.flush();
                    out.close();
                }
                else {
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
                }
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
            Log.e("hasilbayar", "s:" + s);
            try {
                JSONObject mainJSONObj = new JSONObject(s);
                Log.e("responseCode", mainJSONObj.getString("responseCode"));
                responseCodeQuerybayar = mainJSONObj.getString("responseCode");
                messagequeryQuerybayar = mainJSONObj.getString("message");
                refquerbayar = mainJSONObj.getString("ref");
                periodequerybayar = mainJSONObj.getString("periode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (responseCodeQuerybayar.equalsIgnoreCase("00")) {
                new AmbilSaldoTask(Paskabayar.this,tvSaldo).execute("ambil",username,password,kode_loket.get(0));
                llCetak.setVisibility(View.VISIBLE);
                btBayar.setVisibility(View.GONE);
                btshare.setVisibility(View.VISIBLE);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf = dialog.findViewById(R.id.tvIf);
                TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                Button btTidak = dialog.findViewById(R.id.btTidak);
                tvIf.setText("Transaksi Berhasil");
                tvTanya.setText("Pesan");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boyprefs.edit().putBoolean("paskabayar", true).apply();
                                String username = boyprefs.getString("username", "");
                                String password = boyprefs.getString("password", "");
                                new LoginTask(Paskabayar.this).execute("login", username, password);
                            }
                        });
                        btPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Printooth.INSTANCE.hasPairedPrinter()) {
                                    startActivityForResult(new Intent(Paskabayar.this, ScanningActivity.class),
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
                                        Paskabayar.this,
                                        Paskabayar.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Tagihan Paskabayar");
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
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                String formattedDate = df.format(c);
                                if (kodeprodukterpilih.equalsIgnoreCase("TELKOMSELLHALO")) {
                                 //   adminquery1 = adminqueryhaloaray;
                                    float nilaitg = Float.parseFloat(nilaiTagihanqueryhaloaray);
                                    String s = "STRUK PEMBAYARAN TAGIHAN\n" + pilihantipeterpilih + "\n\n" +
                                            "TGL BAYAR  : " + formattedDate + "\n" +
                                            "LAYANAN     : " + pilihantipeterpilih + "\n\n" +
                                            "NO HANDPHONE : " + idpel + "\n" +
                                            "NAMA  : " + namaquery1 + "\n" +
                                            "PERIODE               : " + periodequerybayar + "\n" +
                                            "NILAI TAGIHAN    : Rp." + decimalFormat.format(nilaitg) + "\n\n" +
                                            "Struk Ini Merupakan Bukti Pembayaran Yang Sah" + "\n\n" +
                                            "ADMIN BANK        : " + "Rp." + decimalFormat.format(Float.parseFloat(adminqueryhaloaray)) + "\n" +
                                            "TOTAL BAYAR       : " + "Rp." + decimalFormat.format(totalbayar) + "\n\n" +
                                            "Terima Kasih" + "\n\n" +
                                            kode_loket.get(0) + " / " + nama_loket.get(0) + " / " + username;
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan "+pilihantipeterpilih+
                                            "a.n "+namaquery1);
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                                    startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                                }
                                else{
                                    float nilaitg = Float.parseFloat(tagihanquery1);
                                    String s = "STRUK PEMBAYARAN TAGIHAN\n" + pilihantipeterpilih + "\n\n" +
                                            "TGL BAYAR  : " + formattedDate + "\n" +
                                            "LAYANAN     : " + pilihantipeterpilih + "\n\n" +
                                            "NO HANDPHONE : " + idPelquery1+ "\n" +
                                            "NAMA  : " + namaquery1 + "\n" +
                                            "PERIODE               : " + periodequerybayar + "\n" +
                                            "NILAI TAGIHAN   : Rp." + decimalFormat.format(nilaitg) + "\n\n" +
                                            "Struk Ini Merupakan Bukti Pembayaran Yang Sah" + "\n\n" +
                                            "ADMIN BANK      : " + "Rp." + decimalFormat.format(Float.parseFloat(adminquery1)) + "\n" +
                                            "TOTAL BAYAR     : " + "Rp." + decimalFormat.format(totalbayar) + "\n\n" +
                                            "Terima Kasih" + "\n\n" +
                                            kode_loket.get(0) + " / " + nama_loket.get(0) + " / " + username;
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan "+pilihantipeterpilih+
                                            "a.n "+namaquery1);
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                                    startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
            else {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogok);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvIf = dialog.findViewById(R.id.tvIf);
                TextView tvTanya = dialog.findViewById(R.id.tvTanya);
                Button btTidak = dialog.findViewById(R.id.btTidak);
                tvIf.setText(messagequeryQuerybayar);
                tvTanya.setText("Gagal");
                btTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        boyprefs.edit().putBoolean("paskabayar", true).apply();
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
        document.addSubject("Mudah bayar");
        document.addKeywords("Mudah Bayar");
    }
    private void printdata() {
        if (kodeprodukterpilih.equalsIgnoreCase("TELKOMSELLHALO")) {
            ArrayList<Printable> printables = new ArrayList<>();
            printables.clear();
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setText("STRUK PEMBAYARAN TAGIHAN\n" + pilihantipeterpilih)
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
                    .setText("LAYANAN      : " + pilihantipeterpilih)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("NO Handphone : " + idpel)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("NAMA         : " + namaquery1)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("PERIODE      : " + periodequerybayar)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            float nilaitg = Float.parseFloat(nilaiTagihanqueryhaloaray);
            printables.add(new TextPrintable.Builder()
                    .setText("NILAI TAGIHAN: Rp." + decimalFormat.format(nilaitg))
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
                    .setText("ADMIN BANK   : " + "Rp." + decimalFormat.format(Float.parseFloat(adminqueryhaloaray)))
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("TOTAL BAYAR  : " + "Rp." + decimalFormat.format(totalbayar))
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
            Printooth.INSTANCE.printer(new PairedPrinter(Printooth.INSTANCE.getPairedPrinter().getName(),
                    Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);
        }
        else {
            ArrayList<Printable> printables = new ArrayList<>();
            printables.clear();
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setText("STRUK PEMBAYARAN TAGIHAN\n" + pilihantipeterpilih)
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
                    .setText("LAYANAN      : " + pilihantipeterpilih)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("NO Handphone : " + idPelquery1)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("NAMA         : " + namaquery1)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("PERIODE      : " + periodequerybayar)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            float nilaitg = Float.parseFloat(tagihanquery1);
            printables.add(new TextPrintable.Builder()
                    .setText("NILAI TAGIHAN: Rp." + decimalFormat.format(nilaitg))
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
                    .setText("ADMIN BANK   : " + "Rp." + decimalFormat.format(Float.parseFloat(adminquery1)))
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("TOTAL BAYAR  : " + "Rp." + decimalFormat.format(totalbayar))
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
            Printooth.INSTANCE.printer(new PairedPrinter(Printooth.INSTANCE.getPairedPrinter().getName(),
                    Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);
        }
    }
    public void addTitlePage(Document document) throws DocumentException {
        if (kodeprodukterpilih.equalsIgnoreCase("TELKOMSELLHALO")) {
            Paragraph cv = new Paragraph();
            cv.setAlignment(Element.ALIGN_CENTER);
            cv.add("STRUK PEMBAYARAN TAGIHAN\n" + pilihantipeterpilih + "\n\n");
            cv.setIndentationLeft(50);
            cv.setIndentationRight(50);
            document.add(cv);
            Paragraph tgl = new Paragraph();
            tgl.setAlignment(Element.ALIGN_LEFT);
            tgl.setIndentationLeft(60);
            tgl.setIndentationRight(60);
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String formattedDate = df.format(c);
            tgl.add("TGL BAYAR          : " + formattedDate + "\n");
            tgl.add("LAYANAN              : " + pilihantipeterpilih + "\n\n");
            tgl.add("NO HANDPHONE : " + idpel + "\n");
            tgl.add("NAMA : " + namaquery1 + "\n");
            tgl.add("PERIODE             : " + periodequerybayar + "\n");
            float nilaitg = Float.parseFloat(nilaiTagihanqueryhaloaray);
            tgl.add("NILAI TAGIHAN    : Rp." + decimalFormat.format(nilaitg) + "\n");
            tgl.add("\n");
            document.add(tgl);
            Paragraph keterangan = new Paragraph();
            keterangan.setAlignment(Element.ALIGN_CENTER);
            keterangan.setIndentationLeft(50);
            keterangan.setIndentationRight(50);
            keterangan.add("Struk Ini Merupakan\nBukti Pembayaran Yang Sah");
            keterangan.add("\n\n");
            document.add(keterangan);
            Paragraph admins = new Paragraph();
            admins.setAlignment(Element.ALIGN_LEFT);
            admins.setIndentationLeft(60);
            admins.setIndentationRight(60);
            admins.add("ADMIN BANK        : Rp." + decimalFormat.format(Float.parseFloat(adminqueryhaloaray)) + "\n");
            admins.add("TOTAL BAYAR      : Rp." + decimalFormat.format(totalbayar) + "\n");
            admins.add("\n");
            document.add(admins);
            Paragraph infotext = new Paragraph();
            infotext.setAlignment(Element.ALIGN_CENTER);
            infotext.setIndentationLeft(50);
            infotext.setIndentationRight(50);
            infotext.add("Terima Kasih" + "\n\n");
            infotext.add(kode_loket.get(0) + " / " + nama_loket.get(0) + " / "+ username+ "\n");
            document.add(infotext);
            document.left(20);
            document.right(20);
        }
        else {
            Paragraph cv = new Paragraph();
            cv.setAlignment(Element.ALIGN_CENTER);
            cv.add("STRUK PEMBAYARAN TAGIHAN\n" + pilihantipeterpilih + "\n\n");
            cv.setIndentationLeft(50);
            cv.setIndentationRight(50);
            document.add(cv);
            Paragraph tgl = new Paragraph();
            tgl.setAlignment(Element.ALIGN_LEFT);
            tgl.setIndentationLeft(60);
            tgl.setIndentationRight(60);
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String formattedDate = df.format(c);
            tgl.add("TGL BAYAR          : " + formattedDate + "\n");
            tgl.add("LAYANAN              : " + pilihantipeterpilih + "\n\n");
            tgl.add("NO HANDPHONE : " + idPelquery1 + "\n");
            tgl.add("NAMA : " + namaquery1 + "\n");
            tgl.add("PERIODE              : " + periodequerybayar + "\n");
            float nilaitg = Float.parseFloat(tagihanquery1);
            tgl.add("NILAI TAGIHAN    : Rp." + decimalFormat.format(nilaitg) + "\n");
            tgl.add("\n");
            document.add(tgl);
            Paragraph keterangan = new Paragraph();
            keterangan.setAlignment(Element.ALIGN_CENTER);
            keterangan.setIndentationLeft(50);
            keterangan.setIndentationRight(50);
            keterangan.add("Struk Ini Merupakan\nBukti Pembayaran Yang Sah");
            keterangan.add("\n\n");
            document.add(keterangan);
            Paragraph admins = new Paragraph();
            admins.setAlignment(Element.ALIGN_LEFT);
            admins.setIndentationLeft(60);
            admins.setIndentationRight(60);
            admins.add("ADMIN BANK        : Rp." + decimalFormat.format(Float.parseFloat(adminquery1)) + "\n");
            admins.add("TOTAL BAYAR      : Rp." + decimalFormat.format(totalbayar) + "\n");
            admins.add("\n");
            document.add(admins);
            Paragraph infotext = new Paragraph();
            infotext.setAlignment(Element.ALIGN_CENTER);
            infotext.setIndentationLeft(50);
            infotext.setIndentationRight(50);
            infotext.add("Terima Kasih" + "\n\n");
            infotext.add(kode_loket.get(0) + " / " + nama_loket.get(0)+ " / "+ username + "\n");
            document.add(infotext);
            document.left(50);
            document.right(50);
        }
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