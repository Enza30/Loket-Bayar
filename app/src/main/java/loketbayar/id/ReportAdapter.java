package loketbayar.id;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.Calendar;
import java.util.Date;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.CustomViewHolder> implements PrintingCallback {
    private SharedPreferences boyprefs;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private PdfWriter writer;
    Printing printing;
    StringBuilder hasiltoken,lembar_tagihan_untuk_data,mtrawal_tagihan_untuk_data,mtrakhir_tagihan_untuk_data,pemakaianawal,penalty_tagihan_untuk_data
            ,tagihanLain_tagihan_untuk_data,admin_tagihan_untuk_data,nilaiTagihan_tagihan_untuk_data;
    PreferenceActivity.Header header;
    ArrayList<String> meterAwal = new ArrayList<>();
    ArrayList<String> meterAkhir = new ArrayList<>();
    ArrayList<String> jsonnya = new ArrayList<>();
    private ArrayList<String> kode_produk,kategori2,inputs,total,tagihan,admin,denda,jumlah_transaksi,periode,no_reff,username
            ,hargaloket,update,keterangan,kode_loket,nama_loket,status,namalap,fee,feebulanan;
    private DecimalFormat format=new DecimalFormat("###,###,###.##");
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private final Activity context;
    private String bulan,tahun,s,namanya = "",apinya = "" ,dayanya = "", tarifnya = "",detilTagihan="",lembar="",data="",msn=""
            ,subscriberID="",biayaMeterai="",ppn="",ppj="",angsuran="",rpToken="",kwh="",mtrakhir="",mtrawal="",period="",
            infotextt="",banknya = "",tglawal="",tglakhir="";
    private int no = 0;
    ReportAdapter(Activity context, ArrayList<String> kode_produk, ArrayList<String> kategori2, ArrayList<String> inputs,
                  ArrayList<String> total, ArrayList<String> tagihan, ArrayList<String> admin, ArrayList<String> denda,
                  ArrayList<String> jumlah_transaksi,ArrayList<String> periode, ArrayList<String> no_reff,
                  ArrayList<String> username,ArrayList<String> hargaloket,ArrayList<String> update,ArrayList<String> keterangan,
                  ArrayList<String> kode_loket,ArrayList<String> nama_loket,ArrayList<String> status,ArrayList<String> namalap
            ,ArrayList<String> fee,ArrayList<String> feebulanan)
    {
        this.kode_produk = kode_produk;this.kategori2 = kategori2;this.inputs = inputs;this.bulan = bulan;this.tahun = tahun;
        this.total = total;this.admin = admin;this.denda = denda;this.username = username;this.jumlah_transaksi = jumlah_transaksi;
        this.no_reff = no_reff;this.periode = periode;this.tagihan = tagihan;this.hargaloket = hargaloket;this.update = update;
        this.keterangan = keterangan;this.kode_loket = kode_loket;this.nama_loket = nama_loket;this.status = status;
        this.namalap = namalap;this.fee = fee;this.feebulanan = feebulanan;this.context = context;
        boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);

    }
    @NonNull
    @Override
    public ReportAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.rowreport, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ReportAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        long n=(Integer.parseInt(String.valueOf(update.get(position))));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
            Printooth.INSTANCE.init(context);
            holder.tvProduk.setText(kode_produk.get(position) + "  " + format+ " / " + status.get(position)
                    + " / " + namalap.get(position));
            initview();
            float premmmm = Float.parseFloat(total.get(position));
            holder.tvNama.setText(inputs.get(position) +  "\n" + (("Hrg Total: ")
                    + decimalFormat.format(Float.parseFloat(total.get(position)))) + "\n" + (("Hrg Loket: ")
                    + decimalFormat.format(Float.parseFloat(hargaloket.get(position))))+ "\n" + (("Fee: ")
                    + decimalFormat.format(Float.parseFloat(fee.get(position))))+ "\n" + (("Fee Bulanan: ")
                    + decimalFormat.format(Float.parseFloat(feebulanan.get(position)))));

            holder.ivPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Printooth.INSTANCE.hasPairedPrinter()) {
                        context.startActivityForResult(new Intent(context, ScanningActivity.class),
                                ScanningActivity.SCANNING_FOR_PRINTER);
                        initview();
                    } else {
                        initview();
                        if (kategori2.get(position).equals("PDAM")) {
                            printdata7a(kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position), denda.get(position),
                                    periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                    no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));

                        }
                        else if (kategori2.get(position).equals("ANGSURAN")) {
                            printdata12(kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position), denda.get(position),
                                    periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                    no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));
                        }
                        else if (kategori2.get(position).equals("PBB")) {
                            printdata11(kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position), denda.get(position),
                                    periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                    no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));
                        }
                        else if (kategori2.get(position).equals("TELKOM")) {
                            printdata8(kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position), denda.get(position),
                                    periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                    no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));
                        }
                        else if (kategori2.get(position).equals("TRANSFER")) {
                            printdata9(kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position), denda.get(position),
                                    periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                    no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));
                        }
                        else {
                            if (kode_produk.get(position).equals("TAGIHAN LISTRIK")) {
                                printdata(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), username.get(position),update.get(position));
                            }
                            else if (kode_produk.get(position).equals("PLN PREPAID"))
                            {
                                printdata2(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), username.get(position), update.get(position));
                            }
                            else if (kode_produk.get(position).equals("Non Tagihan Listrik")) {
                                printdata3(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), username.get(position), update.get(position));
                            }
                            else if (kode_produk.get(position).equals("BPJS Kesehatan II")) {
                                printdata4(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }
                            else if (kode_produk.get(position).equals("BPJS Kesehatan")) {
                                printdata4(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }
                            else if (kode_produk.get(position).equals("BPJS Ketenagakerjaan")) {
                                printdata4a(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }
                            else if (kode_produk.get(position).equals("INDOSAT MATRIX")||
                                    (kode_produk.get(position).equals("Tri Pascabayar"))||
                                    (kode_produk.get(position).equals("Smartfren Pascabayar"))||
                                    (kode_produk.get(position).equals("XL Xplor")))
                            {
                                printdata6(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }
                            else if (kode_produk.get(position).equals("Kirim Saldo Loket")) {
                                printdata10(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));
                            }
                            else {
                                printdata1(kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));
                            }
                        }
                    }
                }
            });
            holder.ivPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // String root = Environment.getExternalStorageDirectory().toString();
                    String dest = context.getExternalFilesDir(null) + "/RTS";
                    File myDir = new File(dest + "/PDF");
                    if (!myDir.exists())
                        myDir.mkdirs();
                    File h = new File(dest
                            + "/PDF/" + inputs.get(position) + ".pdf");
                    Document document = new Document(PageSize.A5);
                    Uri path = FileProvider.getUriForFile(
                            context,
                            context.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
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
                        initview();
                        if (kategori2.get(position).equals("PDAM")) {
                            addTitlePage7a(document, kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position),
                                    nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position));
                        }
                        else if (kategori2.get(position).equals("ANGSURAN")) {
                            addTitlePage12(document, kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position),
                                    nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position), periode.get(position));
                        }
                        else if (kategori2.get(position).equals("PBB")) {
                            addTitlePage11(document, kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position),
                                    nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position), periode.get(position));
                        }
                        else if (kategori2.get(position).equals("TELKOM"))  {
                            addTitlePage8(document, kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position),
                                    nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position), periode.get(position));
                        }
                        else if (kategori2.get(position).equals("TRANSFER")) {
                            addTitlePage9(document, kode_produk.get(position), inputs.get(position),
                                    total.get(position),
                                    tagihan.get(position), admin.get(position),
                                    nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position), periode.get(position));
                        }
                        else {
                            if (kode_produk.get(position).equals("Tagihan Listrik")) {
                                addTitlePage(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position), denda.get(position),
                                        periode.get(position), nama_loket.get(0), jumlah_transaksi.get(position),
                                        no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), username.get(position), update.get(position));
                            }
                            else if (kode_produk.get(position).equals("Token Listrik")) {
                                addTitlePage2(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), username.get(position), update.get(position));
                            }
                            else if (kode_produk.get(position).equals("Non Tagihan Listrik")) {
                                addTitlePage3(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), username.get(position), update.get(position));
                            }
                            else if (kode_produk.get(position).equals("BPJS Kesehatan II")) {
                                addTitlePage4(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }
                            else if (kode_produk.get(position).equals("BPJS Kesehatan")) {
                                addTitlePage4(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }
                            else if (kode_produk.get(position).equals("BPJS Ketenagakerjaan")) {
                                addTitlePage4a(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }
                            else if (kode_produk.get(position).equals("INDOSAT MATRIX")||
                                    (kode_produk.get(position).equals("Tri Pascabayar"))||
                                    (kode_produk.get(position).equals("Smartfren Pascabayar"))||
                                    (kode_produk.get(position).equals("XL Xplor")))
                            {
                                addTitlePage6(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position));
                            }

                            else if (kode_produk.get(position).equals("Kirim Saldo Loket")) {
                                addTitlePage10(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position), periode.get(position));
                            }
                            else {
                                addTitlePage1(document, kode_produk.get(position), inputs.get(position),
                                        total.get(position),
                                        tagihan.get(position), admin.get(position),
                                        nama_loket.get(0), jumlah_transaksi.get(position), no_reff.get(position), bulan, tahun, keterangan.get(position), kode_loket.get(position), update.get(position), username.get(position), denda.get(position), periode.get(position));
                            }
                        }
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    // Close Document after writting all content
                    document.close();
                    // send email
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pembayaran Tagihan");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "kodeproduk " + kode_produk.get(position));
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                    context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
            holder.ivText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kategori2.get(position).equals("PDAM")) {
                    lembar_tagihan_untuk_data = new StringBuilder();
                    mtrawal_tagihan_untuk_data = new StringBuilder();
                    mtrakhir_tagihan_untuk_data = new StringBuilder();
                    penalty_tagihan_untuk_data = new StringBuilder();
                    tagihanLain_tagihan_untuk_data = new StringBuilder();
                    admin_tagihan_untuk_data = new StringBuilder();
                    nilaiTagihan_tagihan_untuk_data = new StringBuilder();
                    pemakaianawal = new StringBuilder();
                    namanya = "";
                    apinya = "";
                    tarifnya = "";
                    dayanya = "";
                    detilTagihan = "";
                    lembar = "";
                    meterAwal.clear();
                    meterAkhir.clear();
                    mtrawal = "";
                    mtrakhir = "";
                    try {
                        JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                        namanya = mainJSONObj.getString("nama");
                        //   nama.add(namanya);
                    } catch (JSONException as) {
                        as.printStackTrace();
                    }
                    if (namanya.isEmpty()) {
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            apinya = mainJSONObj.getString("api");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        try {
                            JSONObject mainJSONObj = new JSONObject(apinya);
                            namanya = mainJSONObj.getString("nama");
                            lembar = mainJSONObj.getString("jumlahTagihan");
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
                                lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                                mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                                mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAkhir"));
                                float pd= Float.parseFloat(mainJSONObj.getString("penalty"));
                                penalty_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(pd)).append(" ");
                                tagihanLain_tagihan_untuk_data.append("Rp.").append(mainJSONObj.getString("tagihanLain"));
                                float admins= Float.parseFloat(mainJSONObj.getString("admin"));
                                admin_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(admins)).append(" ");
                                float nilaiTagihans= Float.parseFloat(mainJSONObj.getString("nilaiTagihan"));
                                nilaiTagihan_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(nilaiTagihans)).append(" ");
                                pemakaianawal.append(" ").append(mainJSONObj.getString("pemakaian"));
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                        }
                    }
                    float tot = Float.parseFloat(total.get(position));
                    String s = "STRUK PEMBAYARAN TAGIHAN\n" + kode_produk.get(position) + " (CU)" + "\n\n" +
                            "TGL BAYAR : " + format + "\n" +
                            "ID PELANGGAN : " + inputs.get(position) + "\n" +
                            "NAMA          : " + namanya + "\n\n" +
                            "JML TAGIHAN  : " + lembar + "\n" +
                            "PERIODE        :" + lembar_tagihan_untuk_data + "\n\n" +
                            "METER           :" + mtrawal_tagihan_untuk_data + " -" + mtrakhir_tagihan_untuk_data + "\n" +
                            "PEMAKAIAN :" + pemakaianawal + "\n\n" +
                            "TAGIHAN      : " + nilaiTagihan_tagihan_untuk_data + "\n" +
                            "DENDA          : " + penalty_tagihan_untuk_data + "\n\n" +
                            "Struk Ini Merupakan Bukti Pembayaran Yang Sah" + "\n\n" +
                            "ADMIN BANK    : " + admin_tagihan_untuk_data + "\n" +
                            "TOTAL BAYAR   : Rp." + decimalFormat.format(tot) + "\n\n" +
                            "Terima Kasih" + "\n\n" +
                            kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan " + kode_produk.get(position) +
                            "a.n " + namanya);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                }
                else if (kategori2.get(position).equals("ANGSURAN")) {
                    float adm = Float.parseFloat(admin.get(position));
                    float tot = Float.parseFloat(total.get(position));
                    float nilaitg = Float.parseFloat(tagihan.get(position));
                    float den = Float.parseFloat(denda.get(position));
                    namanya = "";
                    tarifnya = "";
                    apinya = "";
                    try {
                        JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                        namanya = mainJSONObj.getString("nama");
                        //   nama.add(namanya);
                    }
                    catch (JSONException as) {
                        as.printStackTrace();
                    }
                    if (namanya.isEmpty()) {
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            apinya = mainJSONObj.getString("api");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        try {
                            JSONObject mainJSONObj = new JSONObject(apinya);
                            namanya = mainJSONObj.getString("customerName");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                    }
                    String s = "STRUK PEMBAYARAN TAGIHAN\n" + kode_produk.get(position) + " (CU)" + "\n\n" +
                            "TGL BAYAR     : " + format + "\n" +
                            "NO KONTRAK : " + inputs.get(position) + "\n" +
                            "LAYANAN  : " + kode_produk.get(position) + "\n" +
                            "NAMA        : " + namanya + "\n" +
                            "PERIODE/TENOR : " + periode.get(position) + "\n\n" +
                            "TAGIHAN    : Rp." + decimalFormat.format(nilaitg)  + "\n" +
                            "PINALTI       : Rp." + decimalFormat.format(den) + "\n\n" +
                            "Struk Ini merupakan Bukti Pembayaran Yang Sah" + "\n\n" +
                            "ADMIN BANK    : Rp." + decimalFormat.format(adm)  + "\n" +
                            "TOTAL BAYAR   : Rp." + decimalFormat.format(tot) + "\n\n" +
                            "Terima Kasih" + "\n\n" +
                            kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan " + kode_produk.get(position) +
                            "a.n " + namanya);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                }
                else if (kategori2.get(position).equals("PBB")) {
                    float adm = Float.parseFloat(admin.get(position));
                    float tot = Float.parseFloat(total.get(position));
                    float nilaitg = Float.parseFloat(tagihan.get(position));
                    float den = Float.parseFloat(denda.get(position));
                    namanya = "";
                    tarifnya = "";
                    apinya = "";
                    try {
                        JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                        namanya = mainJSONObj.getString("nama");
                        //   nama.add(namanya);
                    }
                    catch (JSONException as) {
                        as.printStackTrace();
                    }
                    if (namanya.isEmpty()) {
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            apinya = mainJSONObj.getString("api");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        try {
                            JSONObject mainJSONObj = new JSONObject(apinya);
                            namanya = mainJSONObj.getString("nama");
                            tarifnya = mainJSONObj.getString("lokasiOP");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                    }
                   String s = "STRUK PEMBAYARAN TAGIHAN\n" + kode_produk.get(position) + " (CU)" + "\n\n" +
                            "TGL BAYAR : " + format + "\n" +
                            "NOP             : " + inputs.get(position) + "\n" +
                            "NAMA          : " + namanya + "\n" +
                            "ALAMAT      : " + tarifnya + "\n" +
                            "TAHUN PAJAK : " + periode.get(position) + "\n\n" +
                            "TAGIHAN    : Rp." + decimalFormat.format(nilaitg)  + "\n" +
                            "DENDA        : Rp." + decimalFormat.format(den) + "\n\n" +
                            "Struk Ini merupakan Bukti Pembayaran Yang Sah" + "\n\n" +
                            "ADMIN BANK    : Rp." + decimalFormat.format(adm)  + "\n" +
                            "TOTAL BAYAR   : Rp." + decimalFormat.format(tot) + "\n\n" +
                            "Terima Kasih" + "\n\n" +
                            kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan " + kode_produk.get(position) +
                            "a.n " + namanya);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));


                }
                else if (kategori2.get(position).equals("TELKOM")) {
                    lembar_tagihan_untuk_data = new StringBuilder();
                    mtrawal_tagihan_untuk_data = new StringBuilder();
                    mtrakhir_tagihan_untuk_data = new StringBuilder();
                    penalty_tagihan_untuk_data = new StringBuilder();
                    tagihanLain_tagihan_untuk_data = new StringBuilder();
                    admin_tagihan_untuk_data = new StringBuilder();
                    nilaiTagihan_tagihan_untuk_data = new StringBuilder();
                    namanya = "";
                    apinya = "";
                    tarifnya = "";
                    dayanya = "";
                    detilTagihan = "";
                    lembar = "";
                    meterAwal.clear();
                    meterAkhir.clear();
                    mtrawal = "";
                    mtrakhir = "";
                    try {
                        JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                        namanya = mainJSONObj.getString("nama");
                        //   nama.add(namanya);
                    } catch (JSONException as) {
                        as.printStackTrace();
                    }
                    if (namanya.isEmpty()) {
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            apinya = mainJSONObj.getString("api");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        try {
                            JSONObject mainJSONObj = new JSONObject(apinya);
                            namanya = mainJSONObj.getString("nama");
                            tarifnya = mainJSONObj.getString("divre");
                            dayanya = mainJSONObj.getString("datel");
                            lembar = mainJSONObj.getString("jumlahTagihan");
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
                                lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                                mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                                mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAkhir"));
                                penalty_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString("penalty"));
                                tagihanLain_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString("tagihanLain"));
                                admin_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString("admin"));
                                nilaiTagihan_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString("nilaiTagihan"));
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                        }
                    }
                    float jum = Float.parseFloat(lembar);
                    float adm = Float.parseFloat(admin.get(position));
                    float tot = Float.parseFloat(total.get(position));
                    float nilaitg = Float.parseFloat(tagihan.get(position));
                    if (kode_produk.get(position).equals("KARTU HALO")) {
                        String s = "STRUK PEMBAYARAN TAGIHAN\n" + kode_produk.get(position) + " (CU)" + "\n\n" +
                                "TGL BAYAR  : " + format + "\n" +
                                "LAYANAN     : " + kode_produk.get(position) + "\n\n" +
                                "NO HANDPHONE : " + inputs.get(position) + "\n" +
                                "NAMA  : " + namanya + "\n" +
                                "PERIODE               : " + periode.get(position) + "\n" +
                                "NILAI TAGIHAN    : Rp." + decimalFormat.format(nilaitg) + "\n\n" +
                                "Struk Ini merupakan Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK        : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR       : " + "Rp." + decimalFormat.format(tot) + "\n\n" +
                                "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan " + kode_produk.get(position) +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else {
                    String s = "STRUK PEMBAYARAN TAGIHAN\n" + kode_produk.get(position) + " (CU)" + "\n\n" +
                           "TGL BAYAR : " + format + "\n" +
                            "NO PELANGGAN : " + inputs.get(position) + "\n" +
                            "NAMA          : " + namanya + "\n\n" +
                            "DIVRE/DATEL   : " + tarifnya + "/" + dayanya + "\n" +
                            "JML TAGIHAN  : " + decimalFormat.format(jum) + "\n" +
                            "PERIODE            : " + periode.get(position) + "\n" +
                            "RP TAGIHAN     : Rp." + decimalFormat.format(nilaitg) + "\n\n" +
                            "Telkom Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                            "ADMIN BANK   : Rp." + decimalFormat.format(adm) + "\n" +
                            "TOTAL BAYAR  : Rp." + decimalFormat.format(tot) + "\n\n" +
                            "Terima Kasih" + "\n\n" +
                            kode_loket.get(0) + " / " + nama_loket.get(0) + " / " + username.get(position);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan " + kode_produk.get(position) +
                            "a.n " + namanya);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));}
                }
                else if (kategori2.get(position).equals("TRANSFER")) {
                    namanya = "";
                    apinya = "";
                    tarifnya = "";
                    banknya = "";
                    try {
                        JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                        namanya = mainJSONObj.getString("nama");
                        //   nama.add(namanya);
                    } catch (JSONException as) {
                        as.printStackTrace();
                    }
                    if (namanya.isEmpty()) {
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            apinya = mainJSONObj.getString("api");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        try {
                            JSONObject mainJSONObj = new JSONObject(apinya);
                            namanya = mainJSONObj.getString("nama_penerima");
                            tarifnya = mainJSONObj.getString("berita");
                            banknya = mainJSONObj.getString("bank");
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                    }
                    float adm = Float.parseFloat(admin.get(position));
                    float tot = Float.parseFloat(total.get(position));
                    float nilaitg = Float.parseFloat(tagihan.get(position));
                    String s = "STRUK BUKTI TRANSFER\nKE REKENING (CU)" + "\n\n" +
                            "TGL TRANSFER  : " + format + "\n" +
                            "BANK                 : " + banknya + "\n" +
                            "NO REKENING  : " + inputs.get(position) + "\n" +
                            "NM PENERIMA : " + namanya + "\n" +
                            "NOMINAL          : Rp." + decimalFormat.format(nilaitg) + "\n\n" +
                            "ADMIN               : " + "Rp." + decimalFormat.format(adm) + "\n" +
                            "TOTAL BAYAR   : " + "Rp." + decimalFormat.format(tot) + "\n\n" +
                            "Terima Kasih" + "\n\n" +
                            kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position)+"\n"+
                            "PT. SINERGI INTI KUALITAS" + "\n" +
                            "Mudah Bayar" ;
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TRANSFER KE REKENING " +
                            "NO REKENING : " + inputs.get(position));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                }
                else {
                    if (kode_produk.get(position).equals("TAGIHAN LISTRIK")) {
                        lembar_tagihan_untuk_data = new StringBuilder();
                        mtrawal_tagihan_untuk_data = new StringBuilder();
                        mtrakhir_tagihan_untuk_data = new StringBuilder();
                        namanya = "";
                        apinya = "";
                        tarifnya = "";
                        dayanya = "";
                        detilTagihan = "";
                        lembar = "";
                        meterAwal.clear();
                        meterAkhir.clear();
                        mtrawal = "";
                        mtrakhir = "";
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            namanya = mainJSONObj.getString("nama");
                            //   nama.add(namanya);
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        if (namanya.isEmpty()) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                apinya = mainJSONObj.getString("api");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            try {
                                JSONObject mainJSONObj = new JSONObject(apinya);
                                namanya = mainJSONObj.getString("nama");
                                tarifnya = mainJSONObj.getString("tarif");
                                dayanya = mainJSONObj.getString("daya");
                                lembar = mainJSONObj.getString("lembarTagihanSisa");
                                detilTagihan = mainJSONObj.getString("detilTagihan");
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
                                    lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                                    mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                                    mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAkhir"));
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                            }
                        }
                        float niltags = Float.parseFloat(tagihan.get(position)) + Float.parseFloat(denda.get(position));
                        float adm = Float.parseFloat(admin.get(position));
                        float tot = Float.parseFloat(total.get(position));
                        String s = "STRUK PEMBAYARAN TAGIHAN LISTRIK (CU)\n\n" +
                                "TGL BAYAR : " + format + "\n" +
                                "ID PELANGGAN  : " + inputs.get(position) + "\n" +
                                "NAMA   : " + namanya + "\n\n" +
                                "TARIF/DAYA     : " + tarifnya + "/" + dayanya + "\n" +
                                "BL/TH                : " + lembar_tagihan_untuk_data + "\n" +
                                "STAND METER : " + mtrawal_tagihan_untuk_data + " -" + mtrakhir_tagihan_untuk_data + "\n\n" +
                                "RP TAG PLN     : " + "Rp." + decimalFormat.format(niltags) + "\n" +
                                "NO REF             : " + no_reff.get(position) + "\n\n" +
                                "PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK    : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR   : " + "Rp." + decimalFormat.format(tot) + "\n\n" +
                                "Informasi Hubungi Call Center" + "\n" + "123 Atau Hub PLN Terdekat" + "\n" + "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan Listrik " + "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else if (kode_produk.get(position).equals("PLN PREPAID")) {
                        namanya = "";
                        apinya = "";
                        tarifnya = "";
                        dayanya = "";
                        detilTagihan = "";
                        lembar = "";
                        data = "";
                        msn = "";
                        subscriberID = "";
                        biayaMeterai = "";
                        ppn = "";
                        ppj = "";
                        angsuran = "";
                        rpToken = "";
                        kwh = "";
                        infotextt = "";
                        ArrayList<Printable> printables = new ArrayList<>();
                        printables.clear();
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            namanya = mainJSONObj.getString("nama");
                            //   nama.add(namanya);

                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        if (namanya.isEmpty()) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                apinya = mainJSONObj.getString("api");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            try {
                                JSONObject mainJSONObj = new JSONObject(apinya);
                                infotextt = mainJSONObj.getString("infotext");
                                data = mainJSONObj.getString("data");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            try {
                                JSONObject mainJSONObj = new JSONObject(data);
                                namanya = mainJSONObj.getString("nama");
                                tarifnya = mainJSONObj.getString("tarif");
                                dayanya = mainJSONObj.getString("daya");
                                msn = mainJSONObj.getString("msn");
                                subscriberID = mainJSONObj.getString("subscriberID");
                                biayaMeterai = mainJSONObj.getString("biayaMeterai");
                                ppn = mainJSONObj.getString("ppn");
                                ppj = mainJSONObj.getString("ppj");
                                angsuran = mainJSONObj.getString("angsuran");
                                rpToken = mainJSONObj.getString("rpToken");
                                kwh = mainJSONObj.getString("kwh");
                                lembar = mainJSONObj.getString("tokenNumber");
                                hasiltoken = new StringBuilder();
                                for (int y = 0; y < lembar.length(); y++) {
                                    if (y >= 4) {
                                        if (y % 4 == 0) {
                                            hasiltoken.append(" ").append(lembar.charAt(y));
                                        } else {
                                            hasiltoken.append(lembar.charAt(y));
                                        }
                                    } else {
                                        hasiltoken.append(lembar.charAt(y));
                                    }
                                }
                                lembar = hasiltoken.toString();
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                        }
                        float nominaltokenn = Float.parseFloat(tagihan.get(position));
                        float adm = Float.parseFloat(admin.get(position));
                        float tot = Float.parseFloat(total.get(position));
                        String s = "STRUK PEMBELIAN TOKEN LISTRIK PRABAYAR (CU)\n\n" +
                                "TGL BAYAR          : " + format + "\n" +
                                "NOMOR METER  : " + msn + "\n" +
                                "ID PELANGGAN  : " + subscriberID + "\n" +
                                "NAMA            : " + namanya + "\n" +
                                "TARIF/DAYA : " + tarifnya + "/" + dayanya + "\n\n" +
                                "RP TAG PLN : " + "Rp." + decimalFormat.format((nominaltokenn)) + "\n" +
                                "JUMLAH KWH : " + kwh + "\n" +
                                "NO REF         : " + no_reff.get(position) + "\n\n" +
                 //               "MATERAI          : " + "Rp." + biayaMeterai + "\n" +
                   //             "PPN                   : Rp." + ppn + "\n" +
                     //           "PPJ                    : Rp." + ppj + "\n" +
                       //         "ANGSURAN      : Rp." + angsuran + "\n" +
                     //           "RP STROOM     : Rp." + rpToken + "\n" +

                                "TOKEN               :        " + "\n" + lembar + "\n\n" +
                                "PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK    : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR   : " + "Rp." + decimalFormat.format(tot) + "\n\n" +
                                infotextt + "\n\n" + "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembelian TOKEN Listrik " +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else if (kode_produk.get(position).equals("Non Tagihan Listrik")) {
                        namanya = "";
                        apinya = "";
                        tarifnya = "";
                        dayanya = "";
                        detilTagihan = "";
                        lembar = "";
                        data = "";
                        msn = "";
                        subscriberID = "";
                        biayaMeterai = "";
                        ppn = "";
                        ppj = "";
                        angsuran = "";
                        rpToken = "";
                        kwh = "";
                        ArrayList<Printable> printables = new ArrayList<>();
                        printables.clear();
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            namanya = mainJSONObj.getString("nama");
                            //   nama.add(namanya);
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        if (namanya.isEmpty()) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                apinya = mainJSONObj.getString("api");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            try {
                                JSONObject mainJSONObj = new JSONObject(apinya);
                                data = mainJSONObj.getString("data");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            try {
                                JSONObject mainJSONObj = new JSONObject(data);
                                namanya = mainJSONObj.getString("nama");
                                tarifnya = mainJSONObj.getString("registrationDate");
                                dayanya = mainJSONObj.getString("transactionName");
                                subscriberID = mainJSONObj.getString("subscriberID");
                                kwh = mainJSONObj.getString("registrationNumber");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                        }
                        float totas = Float.parseFloat(total.get(position));
                        float niltag = Float.parseFloat(tagihan.get(position));
                        float adm = Float.parseFloat(admin.get(position));
                        String s = "STRUK PEMBAYARAN\nNON TAGIHAN LISTRIK (CU)\n\n" +
                                "TGL BAYAR          : " + format + "\n" +
                                "ID PELANGGAN  : " + subscriberID + "\n" +
                                "NAMA : " + namanya + "\n" +
                                "TRANSAKSI : " + dayanya + "\n\n" +
                                "NO REGISTRASI   : " + kwh + "\n" +
                                "TGL REGISTRASI : " + tarifnya + "\n\n" +
                                "RP TAG PLN         : " + "Rp." + decimalFormat.format(niltag) + "\n" +
                                "NO REF                  : " + no_reff.get(position) + "\n\n" +
                                "PLN Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK      : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR     : " + "Rp." + decimalFormat.format(totas) + "\n\n" +
                                "Informasi Hubungi Call Center\n123 Atau Hub PLN Terdekat" + "\n" + "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Non Tagihan Listrik " +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else if (kode_produk.get(position).equals("BPJS Kesehatan II")) {
                        namanya = "";
                        apinya = "";
                        tarifnya = "";
                        dayanya = "";
                        lembar = "";
                        String kd = kode_produk.get(position);
                        if (kd.equalsIgnoreCase("BPJS Kesehatan II")) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                namanya = mainJSONObj.getString("nama");
                                //   nama.add(namanya);
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            if (namanya.isEmpty()) {
                                try {
                                    JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                    apinya = mainJSONObj.getString("api");
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                                try {
                                    JSONObject mainJSONObj = new JSONObject(apinya);
                                    namanya = mainJSONObj.getString("nama");
                                    tarifnya = mainJSONObj.getString("jumlahPeriode");
                                    dayanya = mainJSONObj.getString("jumlahPeserta");
                                    lembar = mainJSONObj.getString("noVA");
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                            }
                        } else if (kd.equalsIgnoreCase("BPJS Kesehatan")) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                namanya = mainJSONObj.getString("namaPeserta");
                                tarifnya = mainJSONObj.getString("jumlahBulan");
                                dayanya = mainJSONObj.getString("jumlahPeserta");
                                lembar = mainJSONObj.getString("noVA");
                                //   nama.add(namanya);
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            if (namanya.isEmpty()) {
                                try {
                                    JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                    apinya = mainJSONObj.getString("api");

                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                                try {
                                    JSONObject mainJSONObj = new JSONObject(apinya);
                                    namanya = mainJSONObj.getString("namaPeserta");
                                    tarifnya = mainJSONObj.getString("jumlahBulan");
                                    dayanya = mainJSONObj.getString("jumlahPeserta");
                                    lembar = mainJSONObj.getString("noVA");
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                            }
                        }
                        float totas = Float.parseFloat(total.get(position));
                        float niltag = Float.parseFloat(tagihan.get(position));
                        float adm = Float.parseFloat(admin.get(position));
                        String s = "STRUK PEMBAYARAN IURAN\nBPJS KESEHATAN (CU)" + "\n\n" +
                                "TGL BAYAR       : " + format + "\n" +
                                "NO REFERENSI : " + no_reff.get(position) + "\n\n" +
                                "NO VA    : " + lembar + "\n" +
                                "NAMA    : " + namanya + "\n" +
                                "JML PESERTA   : " + dayanya + " ORANG" + "\n" +
                                "PERIODE             : " + tarifnya + " BULAN" + "\n" +
                                "RP TAGIHAN      : Rp." + decimalFormat.format(niltag) + "\n\n" +
                                "BPJS Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK     : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR    : " + "Rp." + decimalFormat.format(totas) + "\n\n" +
                                "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan BPJS KESEHATAN " +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else if (kode_produk.get(position).equals("BPJS Kesehatan")) {
                        namanya = "";
                        apinya = "";
                        tarifnya = "";
                        dayanya = "";
                        lembar = "";
                        String kd = kode_produk.get(position);
                        if (kd.equalsIgnoreCase("BPJS Kesehatan II")) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                namanya = mainJSONObj.getString("nama");
                                //   nama.add(namanya);
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            if (namanya.isEmpty()) {
                                try {
                                    JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                    apinya = mainJSONObj.getString("api");
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                                try {
                                    JSONObject mainJSONObj = new JSONObject(apinya);
                                    namanya = mainJSONObj.getString("nama");
                                    tarifnya = mainJSONObj.getString("jumlahPeriode");
                                    dayanya = mainJSONObj.getString("jumlahPeserta");
                                    lembar = mainJSONObj.getString("noVA");
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                            }
                        }
                        else if (kd.equalsIgnoreCase("BPJS Kesehatan")) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                namanya = mainJSONObj.getString("namaPeserta");
                                tarifnya = mainJSONObj.getString("jumlahBulan");
                                dayanya = mainJSONObj.getString("jumlahPeserta");
                                lembar = mainJSONObj.getString("noVA");
                                //   nama.add(namanya);
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            if (namanya.isEmpty()) {
                                try {
                                    JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                    apinya = mainJSONObj.getString("api");

                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                                try {
                                    JSONObject mainJSONObj = new JSONObject(apinya);
                                    namanya = mainJSONObj.getString("namaPeserta");
                                    tarifnya = mainJSONObj.getString("jumlahBulan");
                                    dayanya = mainJSONObj.getString("jumlahPeserta");
                                    lembar = mainJSONObj.getString("noVA");
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                            }
                        }
                        float totas = Float.parseFloat(total.get(position));
                        float niltag = Float.parseFloat(tagihan.get(position));
                        float adm = Float.parseFloat(admin.get(position));
                        String s = "STRUK PEMBAYARAN IURAN\nBPJS KESEHATAN (CU)" + "\n\n" +
                                "TGL BAYAR       : " + format + "\n" +
                                "NO REFERENSI : " + no_reff.get(position) + "\n\n" +
                                "NO VA    : " + lembar + "\n" +
                                "NAMA    : " + namanya + "\n" +
                                "JML PESERTA   : " + dayanya + " ORANG" + "\n" +
                                "PERIODE             : " + tarifnya + " BULAN" + "\n" +
                                "RP TAGIHAN      : Rp." + decimalFormat.format(niltag) + "\n\n" +
                                "BPJS Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK     : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR    : " + "Rp." + decimalFormat.format(totas) + "\n\n" +
                                "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan BPJS KESEHATAN " +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else if (kode_produk.get(position).equals("BPJS Ketenagakerjaan")) {
                        namanya = "";
                        apinya = "";
                        tarifnya = "";
                        dayanya = "";
                        lembar = "";
                        tglawal = "";
                        tglakhir = "";
                        String kd = kode_produk.get(position);

                         if (kd.equalsIgnoreCase("BPJS Ketenagakerjaan")) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                namanya = mainJSONObj.getString("nama");
                                tarifnya = mainJSONObj.getString("program");
                        //        dayanya = mainJSONObj.getString("jumlahPeserta");
                                lembar = mainJSONObj.getString("noKepesertaan");
                                //   nama.add(namanya);
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            if (namanya.isEmpty()) {
                                try {
                                    JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                    apinya = mainJSONObj.getString("api");

                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                                try {
                                    JSONObject mainJSONObj = new JSONObject(apinya);
                                    namanya = mainJSONObj.getString("nama");
                                    tarifnya = mainJSONObj.getString("program");
                                    dayanya = mainJSONObj.getString("kodeIuran");
                                    lembar = mainJSONObj.getString("noKepesertaan");
                                    tglawal = mainJSONObj.getString("tglEfektif");
                                    tglakhir= mainJSONObj.getString("tglExpired");
                                } catch (JSONException as) {
                                    as.printStackTrace();
                                }
                            }
                        }
                        float totas = Float.parseFloat(total.get(position));
                        float niltag = Float.parseFloat(tagihan.get(position));
                        float adm = Float.parseFloat(admin.get(position));
                        String s = "STRUK PEMBAYARAN IURAN\nBPJS KETENAGAKERJAAN (CU)" + "\n\n" +
                                "TGL BAYAR   : " + format + "\n" +
                                "KODE IURAN : " + dayanya + "\n" +
                                "NIK      : " + lembar + "\n" +
                                "NAMA : " + namanya + "\n" +
                                "KODE PROGRAM : " +tarifnya+ "\n" +
                                "RP TAGIHAN        : Rp." + decimalFormat.format(niltag) + "\n\n" +
                                "BPJS Menyatakan Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK     : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR    : " + "Rp." + decimalFormat.format(totas) + "\n\n" +
                                "TGL BERLAKU    : "  + tglawal + "\n" +
                                "TGL BERAKHIR   : " + tglakhir + "\n\n" +
                                "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan BPJS KETENAGAKERJAAN " +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else if (kode_produk.get(position).equals("INDOSAT MATRIX")||
                            (kode_produk.get(position).equals("Tri Pascabayar"))||
                            (kode_produk.get(position).equals("Smartfren Pascabayar"))||
                            (kode_produk.get(position).equals("XL Xplor")))
                    {
                        namanya = "";
                        apinya = "";
                        tarifnya = "";
                        try {
                            JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                            namanya = mainJSONObj.getString("nama");
                            //   nama.add(namanya);
                        } catch (JSONException as) {
                            as.printStackTrace();
                        }
                        if (namanya.isEmpty()) {
                            try {
                                JSONObject mainJSONObj = new JSONObject(keterangan.get(position));
                                apinya = mainJSONObj.getString("api");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                            try {
                                JSONObject mainJSONObj = new JSONObject(apinya);
                                namanya = mainJSONObj.getString("nama");
                                tarifnya = mainJSONObj.getString("periode");
                            } catch (JSONException as) {
                                as.printStackTrace();
                            }
                        }
                        float adm = Float.parseFloat(admin.get(position));
                        float tot = Float.parseFloat(total.get(position));
                        float nilaitg = Float.parseFloat(tagihan.get(position));
                        String s = "STRUK BUKTI PEMBAYARAN\n" + kode_produk.get(position) + " (CU)" + "\n\n" +
                                "TGL BAYAR  : " + format + "\n" +
                                "LAYANAN     : " + kode_produk.get(position) + "\n\n" +
                                "NO HANDPHONE : " + inputs.get(position) + "\n" +
                                "NAMA  : " + namanya + "\n" +
                                "PERIODE               : " + tarifnya + "\n" +
                                "NILAI TAGIHAN   : Rp." + decimalFormat.format(nilaitg) + "\n\n" +
                                "Struk Ini merupakan Bukti Pembayaran Yang Sah" + "\n\n" +
                                "ADMIN BANK      : " + "Rp." + decimalFormat.format(adm) + "\n" +
                                "TOTAL BAYAR     : " + "Rp." + decimalFormat.format(tot) + "\n\n" +
                                "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan " + kode_produk.get(position) +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else if (kode_produk.get(position).equals("Kirim Saldo Loket")) {
                        float nilaitg = Float.parseFloat(tagihan.get(position));
                        String s = "STRUK KIRIM\nSALDO ANTAR LOKET" + "\n\n" +
                                "TGL KIRIM    : " + format + "\n" +
                                "TUJUAN LOKET : " + inputs.get(position) + "\n" +
                                "NOMINAL     : Rp." + decimalFormat.format(nilaitg) + "\n\n" +
                                "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "KIRIM SALDO" +
                                "ANTAR LOKET : " + inputs.get(position));
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                    else {
                        float tot = Float.parseFloat(total.get(position));
                        String s = "STRUK BUKTI TOPUP\n" + kode_produk.get(position) + " (CU)" + "\n\n" +
                                "TGL BAYAR : " + format + "\n" +
                                "LAYANAN    : " + kode_produk.get(position) + "\n" +
                                "ID PELANGGAN  : " + inputs.get(position) + "\n" +
                                "HARGA        : Rp." + decimalFormat.format(tot) + "\n\n" +
                                "Struk Ini Sebagai Bukti Pembayaran Yang Sah" + "\n\n" +
                                "Terima Kasih" + "\n\n" +
                                kode_loket.get(position) + " / " + nama_loket.get(0) + " / " + username.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pembayaran Tagihan " + kode_produk.get(position) +
                                "a.n " + namanya);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                    }
                }
                }
            });
    }
    @Override
    public int getItemCount() {
        return kode_produk.size();
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

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduk,tvNama;
        private ImageView ivPrint,ivPdf,ivText;
        CustomViewHolder(View view) {
            super(view);
            this.tvProduk=view.findViewById(R.id.tvProduk);
            this.tvNama=view.findViewById(R.id.tvNama);
            this.ivPrint=view.findViewById(R.id.ivPrint);
            this.ivPdf=view.findViewById(R.id.ivPdf);
            this.ivText=view.findViewById(R.id.ivText);
        }
    }
    void initview() {
        if (printing != null) {
            printing.setPrintingCallback(this);
        }
    }

    private void printdata(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String username,String updat)
    {
        lembar_tagihan_untuk_data = new StringBuilder();
        mtrawal_tagihan_untuk_data = new StringBuilder();
        mtrakhir_tagihan_untuk_data = new StringBuilder();
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        meterAwal .clear();
        meterAkhir .clear();
        mtrawal="";
        mtrakhir="";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");

            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("tarif");
                dayanya = mainJSONObj.getString("daya");
                lembar = mainJSONObj.getString("lembarTagihanSisa");
                detilTagihan = mainJSONObj.getString("detilTagihan");
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
                    lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                    mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                    mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                }
               catch(JSONException as){
                    as.printStackTrace();
                }
            }
        }
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat dn = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = dn.format(time);
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("STRUK PEMBAYARAN TAGIHAN LISTRIK (CU)")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + input)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA         : " + namanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TARIF/DAYA   : " + tarifnya+" / "+dayanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("BL/TH        :" + lembar_tagihan_untuk_data)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("STAND METER  :" + mtrawal_tagihan_untuk_data + " -" + mtrakhir_tagihan_untuk_data)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float niltags = Float.parseFloat(taiwan)+Float.parseFloat(denna);
        printables.add(new TextPrintable.Builder()
                .setText("RP TAG PLN   : " + "Rp." + decimalFormat.format(niltags))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REF       : " + nore)
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
        float adm = Float.parseFloat(admi);
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK   : " + "Rp." + decimalFormat.format(adm))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float tot = Float.parseFloat(tota);
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR  : " + "Rp." + decimalFormat.format(tot))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        int lemsisa= Integer.parseInt(lembar);
        if (lemsisa>1) {
            printables.add(new TextPrintable.Builder()
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setText("Lembar Tagihan Sisa : " +  lembar)
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
                .setText(kolkt+ " / "+ user+ " / "+username)
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
    private void printdata2(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String username,String updat)
    {   namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        data="";
        msn="";
        subscriberID="";
        biayaMeterai="";
        ppn="";
        ppj="";
        angsuran="";
        rpToken="";
        kwh="";
        infotextt="";
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                infotextt= mainJSONObj.getString("infotext");
                data = mainJSONObj.getString("data");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(data);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("tarif");
                dayanya = mainJSONObj.getString("daya");
                msn= mainJSONObj.getString("msn");
                subscriberID= mainJSONObj.getString("subscriberID");
                biayaMeterai= mainJSONObj.getString("biayaMeterai");
                ppn= mainJSONObj.getString("ppn");
                ppj= mainJSONObj.getString("ppj");
                angsuran= mainJSONObj.getString("angsuran");
                rpToken= mainJSONObj.getString("rpToken");
                kwh= mainJSONObj.getString("kwh");
                lembar= mainJSONObj.getString("tokenNumber");
                hasiltoken=new StringBuilder();
                for(int y=0;y<lembar.length();y++){
                    if(y>=4){
                        if(y%4 ==0){
                            hasiltoken.append(" ").append(lembar.charAt(y));
                        }else{
                            hasiltoken.append(lembar.charAt(y));
                        }
                    }else{
                        hasiltoken.append(lembar.charAt(y));
                    }
                }
                lembar=hasiltoken.toString();
            }catch (JSONException as) {
                as.printStackTrace();
            }  }
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat dn = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = dn.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("STRUK PEMBELIAN TOKEN LISTRIK")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("PRABAYAR(CU)")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NOMOR METER  : " + msn)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + subscriberID)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA         : " + namanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TARIF/DAYA   : " + tarifnya+" / "+dayanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float niltags = Float.parseFloat(taiwan);
        printables.add(new TextPrintable.Builder()
                .setText("RP TAG PLN   : " + "Rp." + decimalFormat.format(niltags))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("JUMLAH KWH   : " + kwh)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REF       : " + nore)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("MATERAI      : " + "Rp." + biayaMeterai)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("PPN          : Rp." + ppn)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("PPJ          : Rp." + ppj)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("ANGSURAN     : Rp." + angsuran)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("RP STROOM    : Rp." + rpToken)
//                .build());
//        printables.add(new TextPrintable.Builder()
//                .setText("\n")
//                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOKEN        :         "+"\n" + lembar)
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
        float adm = Float.parseFloat(admi);
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK   : " + "Rp." + decimalFormat.format(adm))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float tot = Float.parseFloat(tota);
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
                .setText(infotextt)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kolkt+ " / "+ user+ " / "+username)
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
    private void printdata3(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String username,String updat)
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        data="";
        msn="";
        subscriberID="";
        biayaMeterai="";
        ppn="";
        ppj="";
        angsuran="";
        rpToken="";
        kwh="";
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);

        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                data = mainJSONObj.getString("data");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(data);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("registrationDate");
                dayanya = mainJSONObj.getString("transactionName");
                subscriberID= mainJSONObj.getString("subscriberID");
                kwh= mainJSONObj.getString("registrationNumber");
            }catch (JSONException as) {
                as.printStackTrace();
            }  }
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN\nNON TAGIHAN LISTRIK (CU)")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat dn = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = dn.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + subscriberID)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA      : " + namanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TRANSAKSI : " + dayanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REGISTRASI : " + kwh)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TGL REGISTRASI: " + tarifnya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float niltags = Float.parseFloat(taiwan);
        printables.add(new TextPrintable.Builder()
                .setText("RP TAG PLN    : " + "Rp." + decimalFormat.format(niltags))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REF        : " + nore)
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
        float adm = Float.parseFloat(admi);
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK    : " + "Rp." + decimalFormat.format(adm))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float totas = Float.parseFloat(tota);
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR   : " + "Rp." + decimalFormat.format(totas))
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
                .setText("Informasi Hubungi Call Center 123 Atau Hub PLN Terdekat")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kolkt+ " / "+ user+ " / "+username)
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
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);    }
    private void printdata4(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username)
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        lembar="";
        String kd = kobe_podunk;
        if (kd.equalsIgnoreCase("BPJS Kesehatan II")) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                namanya = mainJSONObj.getString("nama");
                //   nama.add(namanya);
            }
            catch (JSONException as) {
                as.printStackTrace();
            }
            if (namanya.isEmpty()) {
                try {
                    JSONObject mainJSONObj = new JSONObject(ket);
                    apinya = mainJSONObj.getString("api");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
                try {
                    JSONObject mainJSONObj = new JSONObject(apinya);
                    namanya = mainJSONObj.getString("nama");
                    tarifnya = mainJSONObj.getString("jumlahPeriode");
                    dayanya = mainJSONObj.getString("jumlahPeserta");
                    lembar= mainJSONObj.getString("noVA");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
            }
        }
        else if (kd.equalsIgnoreCase("BPJS Kesehatan")) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                namanya = mainJSONObj.getString("namaPeserta");
                tarifnya = mainJSONObj.getString("jumlahBulan");
                dayanya = mainJSONObj.getString("jumlahPeserta");
                lembar= mainJSONObj.getString("noVA");
                //   nama.add(namanya);
            }
            catch (JSONException as) {
                as.printStackTrace();
            }
            if (namanya.isEmpty()) {
                try {
                    JSONObject mainJSONObj = new JSONObject(ket);
                    apinya = mainJSONObj.getString("api");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
                try {
                    JSONObject mainJSONObj = new JSONObject(apinya);
                    namanya = mainJSONObj.getString("namaPeserta");
                    tarifnya = mainJSONObj.getString("jumlahBulan");
                    dayanya = mainJSONObj.getString("jumlahPeserta");
                    lembar= mainJSONObj.getString("noVA");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
            }
        }
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN IURAN\nBPJS KESEHATAN (CU)")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REFERENSI : " + nore)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO VA   : " + input )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA    : " +namanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("JUMLAH PESERTA : "+dayanya+" ORANG" )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PERIODE        : "+tarifnya+" BULAN" )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);        float niltags = Float.parseFloat(taiwan);
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
                .setText(kolkt+ " / "+ user+ " / "+username)
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
    private void printdata4a(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username)
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        lembar="";
        String kd = kobe_podunk;
        if (kd.equalsIgnoreCase("BPJS Ketenagakerjaan")) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                namanya = mainJSONObj.getString("namaPeserta");
                tarifnya = mainJSONObj.getString("jumlahBulan");
                dayanya = mainJSONObj.getString("jumlahPeserta");
                lembar= mainJSONObj.getString("noVA");
                //   nama.add(namanya);
            }
            catch (JSONException as) {
                as.printStackTrace();
            }
            if (namanya.isEmpty()) {
                try {
                    JSONObject mainJSONObj = new JSONObject(ket);
                    apinya = mainJSONObj.getString("api");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
                try {
                    JSONObject mainJSONObj = new JSONObject(apinya);
                    namanya = mainJSONObj.getString("nama");
                    tarifnya = mainJSONObj.getString("program");
                    dayanya = mainJSONObj.getString("kodeIuran");
                    lembar = mainJSONObj.getString("noKepesertaan");
                    tglawal = mainJSONObj.getString("tglEfektif");
                    tglakhir= mainJSONObj.getString("tglExpired");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
            }
        }
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN IURAN\nBPJS KETENAGAKERJAAN (CU)")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("KODE IURAN   : " + dayanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NIK     : " + lembar )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA    : " +namanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("KODE PROGRAM   : "+tarifnya )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);        float niltags = Float.parseFloat(taiwan);
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
                .setText("TGL BERLAKU    : " + "" + tglawal)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC437())
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TGL BERAKHIR   : " + "" + tglakhir)
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
                .setText(kolkt+ " / "+ user+ " / "+username)
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
    private void printdata6(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username)
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("periode");
            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        String kd = kobe_podunk;
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);        float niltags = Float.parseFloat(taiwan);
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
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
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
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
                .setText("NO Handphone : " + input)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA         : " + namanya)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PERIODE      : " + tarifnya)
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
                .setText(kolkt + " / " + user +" / " +username)
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
    private void printdata7a(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena)
    {
        lembar_tagihan_untuk_data = new StringBuilder();
        mtrawal_tagihan_untuk_data = new StringBuilder();
        mtrakhir_tagihan_untuk_data = new StringBuilder();
        pemakaianawal = new StringBuilder();
        penalty_tagihan_untuk_data = new StringBuilder();
        tagihanLain_tagihan_untuk_data = new StringBuilder();
        admin_tagihan_untuk_data = new StringBuilder();
        nilaiTagihan_tagihan_untuk_data = new StringBuilder();
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        meterAwal .clear();
        meterAkhir .clear();
        mtrawal="";
        mtrakhir="";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);

        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                lembar = mainJSONObj.getString("jumlahTagihan");

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
                    pemakaianawal.append(" ").append(mainJSONObj.getString( "pemakaian"));
                    lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                    mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                    mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                    float pd= Float.parseFloat(mainJSONObj.getString("penalty"));
                    penalty_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(pd)).append(" ");
                    tagihanLain_tagihan_untuk_data.append("Rp.").append(mainJSONObj.getString("tagihanLain"));
                    float admins= Float.parseFloat(mainJSONObj.getString("admin"));
                    admin_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(admins)).append(" ");
                    float nilaiTagihans= Float.parseFloat(mainJSONObj.getString("nilaiTagihan"));
                    nilaiTagihan_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(nilaiTagihans)).append(" ");
                }
                catch(JSONException as){
                    as.printStackTrace();
                }
            }
        }
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);float niltags = Float.parseFloat(taiwan);float den = Float.parseFloat(dena);
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        String kd = kobe_podunk;
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat dn = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = dn.format(time);
        printables.add(new TextPrintable.Builder()
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setText("STRUK PEMBAYARAN TAGIHAN\n"+kd+"(CU)")
                    .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + input)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA         : " + namanya )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("JML TAGIHAN  : " + lembar )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PERIODE      :" + lembar_tagihan_untuk_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("METER        :" +  mtrawal_tagihan_untuk_data+" -"+mtrakhir_tagihan_untuk_data)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PEMAKAIAN    :" +  pemakaianawal)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TAGIHAN      : " + nilaiTagihan_tagihan_untuk_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("DENDA        : " + penalty_tagihan_untuk_data )
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
                .setText("ADMIN BANK   : " + admin_tagihan_untuk_data )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR  : Rp."+decimalFormat.format(tot) )
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
                .setText(kolkt + " / " + user +" / " +username)
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
    private void printdata8(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena)
    {
        lembar_tagihan_untuk_data = new StringBuilder();
        mtrawal_tagihan_untuk_data = new StringBuilder();
        mtrakhir_tagihan_untuk_data = new StringBuilder();
        penalty_tagihan_untuk_data = new StringBuilder();
        tagihanLain_tagihan_untuk_data = new StringBuilder();
        admin_tagihan_untuk_data = new StringBuilder();
        nilaiTagihan_tagihan_untuk_data = new StringBuilder();
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        meterAwal .clear();
        meterAkhir .clear();
        mtrawal="";
        mtrakhir="";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);

        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");

            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("divre");
                dayanya = mainJSONObj.getString("datel");
                lembar = mainJSONObj.getString("jumlahTagihan");
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
                    lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));

                    mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                    mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                    penalty_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "penalty"));
                    tagihanLain_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "tagihanLain"));
                    admin_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "admin"));
                    nilaiTagihan_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "nilaiTagihan"));
                }
                catch(JSONException as){
                    as.printStackTrace();
                }
            }
        }
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);float niltags = Float.parseFloat(taiwan);float den = Float.parseFloat(dena);
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        String kd = kobe_podunk;
        printables.add(new TextPrintable.Builder()
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setText("STRUK PEMBAYARAN TAGIHAN\n"+kd+" (CU)")
                    .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat dn = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = dn.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        if (kd.equals("KARTU HALO")) {
            printables.add(new TextPrintable.Builder()
                    .setText("LAYANAN      : " + kd)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("NO Handphone : " + input)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("NAMA         : " + namanya)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("PERIODE      : " + peiod)
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
                    .setText("NO PELANGGAN : " + input)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("NAMA         : " + namanya)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("DIVRE/DATEL  : " + tarifnya + "/" + dayanya)
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            float jumtags = Float.parseFloat(lembar);
            printables.add(new TextPrintable.Builder()
                    .setText("JML TAGIHAN  : " + decimalFormat.format(jumtags))
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("\n")
                    .build());
            printables.add(new TextPrintable.Builder()
                    .setText("PERIODE      : " + peiod)
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
    private void printdata9(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena)
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        banknya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama_penerima");
                tarifnya = mainJSONObj.getString("berita");
                banknya = mainJSONObj.getString("bank");
            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        float tot = Float.parseFloat(String.valueOf(tota));
        float niltags = Float.parseFloat(taiwan);
        float adm = Float.parseFloat(String.valueOf(admi));
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK BUKTI TRANSFER\nKE REKENING (CU)")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL TRANSFER : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA BANK    : " + banknya )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO REKENING  : " + input )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA PENERIMA: "+ namanya)
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
                .setText("Mudah Bayar")
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
    private void printdata10(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena)
    {
        float niltags = Float.parseFloat(taiwan);
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK KIRIM\nSALDO ANTAR LOKET (CU)")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL KIRIM    : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TUJUAN LOKET : " + input)
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
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);    }
    private void printdata11(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena)
    {
        float niltags = Float.parseFloat(taiwan);
        float tot = Float.parseFloat(tota);
        float adm = Float.parseFloat(admi);
        float den = Float.parseFloat(dena);
        namanya = "";
        tarifnya = "";
        apinya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
             //   nama.add(namanya);
        }
        catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");

            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("lokasiOP");
            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN TAGIHAN")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kobe_podunk)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NOP       : " + input)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA        : " + namanya )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ALAMAT      : " + tarifnya )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TAHUN PAJAK : " + peiod )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TAGIHAN     : Rp."+decimalFormat.format(niltags) )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("DENDA       : Rp."+decimalFormat.format(den))
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
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK  : Rp."+decimalFormat.format(adm))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR : Rp."+decimalFormat.format(tot) )
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
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);    }
    private void printdata12(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena)
    {
        float niltags = Float.parseFloat(taiwan);
        float tot = Float.parseFloat(tota);
        float adm = Float.parseFloat(admi);
        float den = Float.parseFloat(dena);
        namanya = "";
        tarifnya = "";
        apinya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("customerName");
            //   nama.add(namanya);
        }
        catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("customerName");
            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText("STRUK PEMBAYARAN TAGIHAN")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setText(kobe_podunk)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR  : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NO KONTRAK : " + input)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("LAYANAN  : " + kobe_podunk )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("NAMA     : " + namanya )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PERIODE/TENOR : " + peiod )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TAGIHAN       : Rp."+decimalFormat.format(niltags) )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("PINALTI       : Rp."+decimalFormat.format(den))
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
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ADMIN BANK    : Rp."+decimalFormat.format(adm))
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TOTAL BAYAR   : Rp."+decimalFormat.format(tot) )
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
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);    }
    private void printdata1(String kobe_podunk,String input,String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena)
    {
        float niltags = Float.parseFloat(tota);
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
                .setText(kobe_podunk+ " (CU)")
                .build());
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("TGL BAYAR : " + format)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("LAYANAN   : " + kobe_podunk )
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("ID PELANGGAN : " + input)
                    .build());
        printables.add(new TextPrintable.Builder()
                .setText("\n")
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("HARGA        : Rp."+decimalFormat.format(niltags))
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
                Printooth.INSTANCE.getPairedPrinter().getAddress())).print(printables);    }

    public void addMetaData(Document document) {
        document.addTitle("Struk MB");
        document.addSubject("Mudah Bayar");
        document.addKeywords("Mudah Bayar");
    }
    public void addTitlePage(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String denna,String peiod,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String username,String updat) throws DocumentException
    {
        lembar_tagihan_untuk_data = new StringBuilder();
        mtrawal_tagihan_untuk_data = new StringBuilder();
        mtrakhir_tagihan_untuk_data = new StringBuilder();
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        meterAwal .clear();
        meterAkhir .clear();
        mtrawal="";
        mtrakhir="";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("tarif");
                dayanya = mainJSONObj.getString("daya");
                lembar = mainJSONObj.getString("lembarTagihanSisa");
                detilTagihan = mainJSONObj.getString("detilTagihan");
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
                    lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                    mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                    mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                }
                catch(JSONException as){
                    as.printStackTrace();
                }
            }
        }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN TAGIHAN LISTRIK\n(CU)\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR         : " + format + "\n");
        tgl.add("ID PELANGGAN");
        tgl.add("  : " + input + "\n");
        tgl.add("NAMA");
        tgl.add("                   : " + namanya + "\n");
        tgl.add("TARIF/DAYA");
        tgl.add("        : " + tarifnya + "/" + dayanya + "\n");
        document.add(tgl);
        Paragraph tgll = new Paragraph();
        tgll.setAlignment(Element.ALIGN_LEFT);
        tgll.setIndentationLeft(60);
        tgll.setIndentationRight(60);
        tgll.add("BL/TH");
        tgll.add("                   :" + lembar_tagihan_untuk_data + "\n");
        tgll.add("STAND METER");
        tgll.add("   :" + mtrawal_tagihan_untuk_data+ " -");
        tgll.add( mtrakhir_tagihan_untuk_data + "\n\n");
        float niltags = Float.parseFloat(taiwan)+Float.parseFloat(denna);
        tgll.add("RP TAG PLN");
        tgll.add("        : Rp." + decimalFormat.format(niltags) + "\n");
        tgll.add("NO REFF             :");
        tgll.add(nore + "\n");
        tgll.add("\n");
        document.add(tgll);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("PLN Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph admin = new Paragraph();
        admin.setAlignment(Element.ALIGN_LEFT);
        admin.setIndentationLeft(60);
        admin.setIndentationRight(60);        float totas= Float.parseFloat(tota);        float adm = Float.parseFloat(admi);
        admin.add("ADMIN BANK       : Rp." + decimalFormat.format(adm) + "\n");
        admin.add("TOTAL BAYAR        : Rp." + decimalFormat.format(totas) + "\n");
        admin.add("\n");
        document.add(admin);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        int lemsisa= Integer.parseInt(lembar);
        if (lemsisa>1) {
                    infotext.add("Lembar Tagihan Sisa : " +  lembar+ "\n\n");
        }
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add("Informasi Hubungi Call Center\n123 Atau Hub PLN Terdekat" + "\n\n");
        infotext.add(kolkt+ " / "+ user + " / "+username+"\n\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage2(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String username,String updat) throws DocumentException
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        data="";
        msn="";
        subscriberID="";
        biayaMeterai="";
        ppn="";
        ppj="";
        angsuran="";
        rpToken="";
        kwh="";
        infotextt="";
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                infotextt= mainJSONObj.getString("infotext");
                data = mainJSONObj.getString("data");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(data);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("tarif");
                dayanya = mainJSONObj.getString("daya");
                msn= mainJSONObj.getString("msn");
                subscriberID= mainJSONObj.getString("subscriberID");
                biayaMeterai= mainJSONObj.getString("biayaMeterai");
                ppn= mainJSONObj.getString("ppn");
                ppj= mainJSONObj.getString("ppj");
                angsuran= mainJSONObj.getString("angsuran");
                rpToken= mainJSONObj.getString("rpToken");
                kwh= mainJSONObj.getString("kwh");
                lembar= mainJSONObj.getString("tokenNumber");
                hasiltoken=new StringBuilder();
                for(int y=0;y<lembar.length();y++){
                    if(y>=4){
                        if(y%4 ==0){
                            hasiltoken.append(" ").append(lembar.charAt(y));
                        }else{
                            hasiltoken.append(lembar.charAt(y));
                        }
                    }else{
                        hasiltoken.append(lembar.charAt(y));
                    }
                }
                lembar=hasiltoken.toString();
            }catch (JSONException as) {
                as.printStackTrace();
            }  }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBELIAN TOKEN LISTRIK\nPRABAYAR (CU)\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(57);
        tgl.setIndentationRight(57);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR         : " + format + "\n");
        tgl.add("NO METER");
        tgl.add("          : " + msn + "\n");
        tgl.add("ID PELANGGAN");
        tgl.add("  : " + subscriberID + "\n");
        tgl.add("NAMA");
        tgl.add("                   : " + namanya + "\n");
        tgl.add("TARIF/DAYA");
        tgl.add("        : " + tarifnya + "/" + dayanya + "\n");
        float niltags = Float.parseFloat(taiwan);
        tgl.add("RP TAG PLN");
        tgl.add("        : Rp." + decimalFormat.format(niltags) + "\n");
        tgl.add("No REFF              :");
        tgl.add("" + nore + "\n");
        tgl.add("MATERAI");
        tgl.add("             : Rp." + biayaMeterai + "\n");
        tgl.add("PPN");
        tgl.add("                      : Rp." + ppn + "\n");
        tgl.add("PPJ");
        tgl.add("                       : Rp." + ppj + "\n");
        tgl.add("ANGSURAN");
        tgl.add("         : Rp." + angsuran + "\n");
        tgl.add("RP STROOM");
        tgl.add("        : Rp." + rpToken + "\n");
        tgl.add("JUMLAH KWH");
        tgl.add("      : " + kwh + "\n");
        tgl.add("TOKEN                 : "+"\n");
        tgl.add("" + lembar + "\n\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("PLN Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah\n\n");
        document.add(keterangan);
        Paragraph tgll = new Paragraph();
        tgll.setAlignment(Element.ALIGN_LEFT);
        tgll.setIndentationLeft(57);
        tgll.setIndentationRight(57);
        float totas= Float.parseFloat(tota);        float adm = Float.parseFloat(admi);
        tgll.add("ADMIN BANK");
        tgll.add("       : Rp." + decimalFormat.format(adm) + "\n");
        tgll.add("TOTAL BAYAR");
        tgll.add("     : Rp." + decimalFormat.format(totas) + "\n");
        tgll.add("\n");
        document.add(tgll);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n");
        infotext.add(infotextt + "\n\n");
        infotext.add(kolkt+ " / "+ user+ " / "+username+ "");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage3(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String username,String updat) throws DocumentException
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        data="";
        msn="";
        subscriberID="";
        biayaMeterai="";
        ppn="";
        ppj="";
        angsuran="";
        rpToken="";
        kwh="";
        ArrayList<Printable> printables = new ArrayList<>();
        printables.clear();
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                data = mainJSONObj.getString("data");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(data);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("registrationDate");
                dayanya = mainJSONObj.getString("transactionName");
                subscriberID= mainJSONObj.getString("subscriberID");
                kwh= mainJSONObj.getString("registrationNumber");
            }catch (JSONException as) {
                as.printStackTrace();
            }  }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN\nNON TAGIHAN LISTRIK (CU)\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(50);
        tgl.setIndentationRight(50);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR         : " + format + "\n");
        tgl.add("ID PELANGGAN");
        tgl.add("   : " + subscriberID + "\n");
        tgl.add("NAMA");
        tgl.add("            : " + namanya + "\n");
        tgl.add("TRANSAKSI");
        tgl.add("  : " + dayanya + "\n\n");
        tgl.add("NO REGISTRASI");
        tgl.add("   : " + kwh + "\n");
        tgl.add("TGL REGISTRASI");
       tgl.add(" : " + tarifnya + "\n");
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);
        float niltags = Float.parseFloat(taiwan);
        tgl.add("RP TAG PLN");
        tgl.add("         : Rp." + decimalFormat.format(niltags) + "\n");
        tgl.add("NO REFF               :");
        tgl.add("" + nore + "\n");
        tgl.add("\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("PLN Menyatakan Struk Ini Sebagai\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph admins = new Paragraph();
        admins.setAlignment(Element.ALIGN_LEFT);
        admins.setIndentationLeft(50);
        admins.setIndentationRight(50);
        admins.add("ADMIN BANK        : Rp." + decimalFormat.format(adm) + "\n");
        admins.add("TOTAL BAYAR      : Rp." + decimalFormat.format(tot) + "\n");
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add("Informasi Hubungi Call Center 123 Atau Hub PLN Terdekat" + "\n\n");
        infotext.add(kolkt+ " / "+ user + " / "+username+ "\n\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage4(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username) throws DocumentException
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        lembar="";
        String kd = kobe_podunk;
        if (kd.equalsIgnoreCase("BPJS Kesehatan II")) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                namanya = mainJSONObj.getString("nama");
                //   nama.add(namanya);
            }
            catch (JSONException as) {
                as.printStackTrace();
            }
            if (namanya.isEmpty()) {
                try {
                    JSONObject mainJSONObj = new JSONObject(ket);
                    apinya = mainJSONObj.getString("api");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
                try {
                    JSONObject mainJSONObj = new JSONObject(apinya);
                    namanya = mainJSONObj.getString("nama");
                    tarifnya = mainJSONObj.getString("jumlahPeriode");
                    dayanya = mainJSONObj.getString("jumlahPeserta");
                    lembar= mainJSONObj.getString("noVA");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
            }
        }
        else if (kd.equalsIgnoreCase("BPJS Kesehatan")) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                namanya = mainJSONObj.getString("namaPeserta");
                tarifnya = mainJSONObj.getString("jumlahBulan");
                dayanya = mainJSONObj.getString("jumlahPeserta");
                lembar= mainJSONObj.getString("noVA");
                //   nama.add(namanya);
            }
            catch (JSONException as) {
                as.printStackTrace();
            }
            if (namanya.isEmpty()) {
                try {
                    JSONObject mainJSONObj = new JSONObject(ket);
                    apinya = mainJSONObj.getString("api");

                } catch (JSONException as) {
                    as.printStackTrace();
                }
                try {
                    JSONObject mainJSONObj = new JSONObject(apinya);
                    namanya = mainJSONObj.getString("namaPeserta");
                    tarifnya = mainJSONObj.getString("jumlahBulan");
                    dayanya = mainJSONObj.getString("jumlahPeserta");
                    lembar= mainJSONObj.getString("noVA");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
            }
        }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN IURAN\nBPJS KESEHATAN (CU)\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR        : " + format + "\n");
        tgl.add("NO REFERENSI : " + nore + "\n\n");
        tgl.add("NO VA       : " + lembar + "\n");
        tgl.add("NAMA        : "+namanya+"\n");
        tgl.add("JUMLAH PESERTA : "+dayanya+" ORANG\n");
        tgl.add("PERIODE                 : " +tarifnya+" BULAN\n");
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);        float niltags = Float.parseFloat(taiwan);
        tgl.add("JUMLAH TAGIHAN  : RP." +decimalFormat.format(niltags)+ "\n");
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
        admins.setIndentationLeft(60);
        admins.setIndentationRight(60);
        admins.add("ADMIN BANK         : Rp." + decimalFormat.format(adm) + "\n");
        admins.add("TOTAL BAYAR       : Rp." + decimalFormat.format(tot) + "\n");
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        // infotext.add(infoText + "\n\n");
        infotext.add(kolkt+ " / "+ user + " / "+username+ "\n\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage4a(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username) throws DocumentException
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        lembar="";

        tglawal = "";
        tglakhir = "";
        String kd = kobe_podunk;

        if (kd.equalsIgnoreCase("BPJS Ketenagakerjaan")) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                namanya = mainJSONObj.getString("namaPeserta");
                tarifnya = mainJSONObj.getString("jumlahBulan");
                dayanya = mainJSONObj.getString("jumlahPeserta");
                lembar= mainJSONObj.getString("noVA");
                //   nama.add(namanya);
            }
            catch (JSONException as) {
                as.printStackTrace();
            }
            if (namanya.isEmpty()) {
                try {
                    JSONObject mainJSONObj = new JSONObject(ket);
                    apinya = mainJSONObj.getString("api");

                } catch (JSONException as) {
                    as.printStackTrace();
                }
                try {
                    JSONObject mainJSONObj = new JSONObject(apinya);
                    namanya = mainJSONObj.getString("nama");
                    tarifnya = mainJSONObj.getString("program");
                    dayanya = mainJSONObj.getString("kodeIuran");
                    lembar = mainJSONObj.getString("noKepesertaan");
                    tglawal = mainJSONObj.getString("tglEfektif");
                    tglakhir= mainJSONObj.getString("tglExpired");
                } catch (JSONException as) {
                    as.printStackTrace();
                }
            }
        }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN IURAN\nBPJS KETENAGAKERJAAN (CU)\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR     : " + format + "\n");
        tgl.add("KODE IURAN   : " + dayanya + "\n\n");
        tgl.add("NIK         : " + lembar + "\n");
        tgl.add("NAMA     : "+namanya+"\n");
        tgl.add("KODE PROGRAM    : "+tarifnya+"\n");
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);        float niltags = Float.parseFloat(taiwan);
        tgl.add("JUMLAH TAGIHAN  : RP." +decimalFormat.format(niltags)+ "\n");
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
        admins.setIndentationLeft(60);
        admins.setIndentationRight(60);
        admins.add("ADMIN BANK         : Rp." + decimalFormat.format(adm) + "\n");
        admins.add("TOTAL BAYAR       : Rp." + decimalFormat.format(tot) + "\n");
        admins.add("\n");
        admins.add("TGL BERLAKU       : " + tglawal + "\n");
        admins.add("TGL BERAKHIR     : " + tglakhir + "\n");
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        // infotext.add(infoText + "\n\n");
        infotext.add(kolkt+ " / "+ user + " / "+username+ "\n\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage6(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username) throws DocumentException
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("periode");
            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        String kd = kobe_podunk;
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN TAGIHAN\n" +kd + "\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);        float niltags = Float.parseFloat(taiwan);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR          : " + format + "\n");
        tgl.add("LAYANAN              : " + kd + "\n\n");
        tgl.add("NO HANDPHONE : " + input + "\n");
        tgl.add("NAMA : " + namanya + "\n");
        tgl.add("PERIODE              : " + tarifnya + "\n");
        tgl.add("NILAI TAGIHAN    : Rp." + decimalFormat.format(niltags) + "\n");
        tgl.add("\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("Struk ini Merupakan\nBukti Pembayaran Yang Sah");
        keterangan.add("\n\n");
        document.add(keterangan);
        Paragraph admins = new Paragraph();
        admins.setAlignment(Element.ALIGN_LEFT);
        admins.setIndentationLeft(60);
        admins.setIndentationRight(60);
        admins.add("ADMIN BANK        : Rp." + decimalFormat.format(adm) + "\n");
        admins.add("TOTAL BAYAR      : Rp." + decimalFormat.format(tot) + "\n");
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kolkt + " / " + user +" / " +username + "\n\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage7a(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena) throws DocumentException
    {
        lembar_tagihan_untuk_data = new StringBuilder();
        mtrawal_tagihan_untuk_data = new StringBuilder();
        mtrakhir_tagihan_untuk_data = new StringBuilder();
        pemakaianawal = new StringBuilder();
        penalty_tagihan_untuk_data = new StringBuilder();
        tagihanLain_tagihan_untuk_data = new StringBuilder();
        admin_tagihan_untuk_data = new StringBuilder();
        nilaiTagihan_tagihan_untuk_data = new StringBuilder();
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        meterAwal .clear();
        meterAkhir .clear();
        mtrawal="";
        mtrakhir="";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);

        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");

            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                lembar = mainJSONObj.getString("jumlahTagihan");
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
                    lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                    mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                    mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                    pemakaianawal.append(" ").append(mainJSONObj.getString( "pemakaian"));
                    float pd= Float.parseFloat(mainJSONObj.getString("penalty"));
                    penalty_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(pd)).append(" ");
                    tagihanLain_tagihan_untuk_data.append("Rp.").append(mainJSONObj.getString("tagihanLain"));
                    float admins= Float.parseFloat(mainJSONObj.getString("admin"));
                    admin_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(admins)).append(" ");
                    float nilaiTagihans= Float.parseFloat(mainJSONObj.getString("nilaiTagihan"));
                    nilaiTagihan_tagihan_untuk_data.append("Rp.").append(decimalFormat.format(nilaiTagihans)).append(" ");
                }
                catch(JSONException as){
                    as.printStackTrace();
                }
            }
        }
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);float niltags = Float.parseFloat(taiwan);float den = Float.parseFloat(dena);
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        String kd = kobe_podunk;
        cv.add("STRUK PEMBAYARAN TAGIHAN\n"+kd+" (CU)"+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(40);
        tgl.setIndentationRight(40);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR        : " + format + "\n");
        tgl.add("ID PELANGGAN : " + input + "\n");
        tgl.add("NAMA                  : " + namanya + "\n\n");
        tgl.add("JML TAGIHAN     : " + lembar + "\n");
        tgl.add("PERIODE             :" + lembar_tagihan_untuk_data + "\n\n");
        tgl.add("METER                :" + mtrawal_tagihan_untuk_data +" -"+ mtrakhir_tagihan_untuk_data+"\n");
        tgl.add("PEMAKAIAN       :" + pemakaianawal + "\n\n");
        tgl.add("TAGIHAN             : " + nilaiTagihan_tagihan_untuk_data+ "\n");
        tgl.add("DENDA                : " + penalty_tagihan_untuk_data + "\n\n");
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
        admin.add("ADMIN BANK   : " + admin_tagihan_untuk_data+ "\n");
        admin.add("TOTAL BAYAR : Rp." +decimalFormat.format(tot)+ "\n");
        admin.add("\n\n");
        document.add(admin);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kolkt + " / " + user +" / " +username + "\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage8(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena,String peiod) throws DocumentException
    {
        lembar_tagihan_untuk_data = new StringBuilder();
        mtrawal_tagihan_untuk_data = new StringBuilder();
        mtrakhir_tagihan_untuk_data = new StringBuilder();
        penalty_tagihan_untuk_data = new StringBuilder();
        tagihanLain_tagihan_untuk_data = new StringBuilder();
        admin_tagihan_untuk_data = new StringBuilder();
        nilaiTagihan_tagihan_untuk_data = new StringBuilder();
        namanya = "";
        apinya = "";
        tarifnya = "";
        dayanya = "";
        detilTagihan="";
        lembar="";
        meterAwal .clear();
        meterAkhir .clear();
        mtrawal="";
        mtrakhir="";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("divre");
                dayanya = mainJSONObj.getString("datel");
                lembar = mainJSONObj.getString("jumlahTagihan");
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
                    lembar_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("periode"));
                    mtrawal_tagihan_untuk_data.append(" ").append(mainJSONObj.getString("meterAwal"));
                    mtrakhir_tagihan_untuk_data.append(" ").append(mainJSONObj.getString( "meterAkhir"));
                    penalty_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "penalty"));
                    tagihanLain_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "tagihanLain"));
                    admin_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "admin"));
                    nilaiTagihan_tagihan_untuk_data.append(" Rp. ").append(mainJSONObj.getString( "nilaiTagihan"));
                }
                catch(JSONException as){
                    as.printStackTrace();
                }
            }
        }
        float adm = Float.parseFloat(admi);float tot = Float.parseFloat(tota);float niltags = Float.parseFloat(taiwan);float den = Float.parseFloat(dena);
       String kd = kobe_podunk;
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN TAGIHAN\n "+kd+" (CU)"+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR          : " + format + "\n");
        if (kd.equals("KARTU HALO")) {
            tgl.add("LAYANAN              : " + kd + "\n\n");
            tgl.add("NO HANDPHONE : " + input + "\n");
            tgl.add("NAMA : " + namanya + "\n");
            tgl.add("PERIODE             : " + peiod + "\n");
            tgl.add("NILAI TAGIHAN    : Rp." + decimalFormat.format(niltags) + "\n");
        }
        else {
        tgl.add("NO PELANGGAN : " + input + "\n");
        tgl.add("NAMA : " + namanya + "\n\n");
        tgl.add("DIVRE/DATEL      : " + tarifnya + "/" + dayanya + "\n");
        float jumtags= Float.parseFloat(lembar);
        tgl.add("JML TAGIHAN     : " + decimalFormat.format(jumtags)+ "\n");
        tgl.add("PERIODE             : " + peiod + "\n");
        tgl.add("RP TAGIHAN       : Rp." + decimalFormat.format(niltags) + "\n");
        }
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
        admin.setIndentationLeft(60);
        admin.setIndentationRight(60);
        admin.add("ADMIN BANK        : Rp." + decimalFormat.format(adm) + "\n");
        admin.add("TOTAL BAYAR      : Rp." + decimalFormat.format(tot) + "\n");
        admin.add("\n");
        document.add(admin);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+"\n");
        document.add(infotext);
        document.left(20);
        document.right(20);
    }
    public void addTitlePage9(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena,String peiod) throws DocumentException
    {
        namanya = "";
        apinya = "";
        tarifnya = "";
        banknya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        } catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");
            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama_penerima");
                tarifnya = mainJSONObj.getString("berita");
                banknya = mainJSONObj.getString("bank");
            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK BUKTI TRANSFER\nKE REKENING (CU)\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        Date c = Calendar.getInstance().getTime();
        float tot = Float.parseFloat(String.valueOf(tota));
        float niltags = Float.parseFloat(taiwan);
        float adm = Float.parseFloat(String.valueOf(admi));
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL TRANSFER : " + format + "\n");
        tgl.add("NAMA BANK       : " + banknya + "\n");
        tgl.add("NO REKENING   : " + input +"\n");
        tgl.add("NAMA                  : "+namanya+"\n");
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
        infotext.add("Mudah Bayar" + "\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage10(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena,String peiod) throws DocumentException
    {
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK KIRIM\nSALDO ANTAR LOKET\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL KIRIM          : " + format + "\n");
        tgl.add("TUJUAN LOKET : " + input + "\n");
        float niltags = Float.parseFloat(taiwan);
        tgl.add("NOMINAL            : Rp." +decimalFormat.format(niltags)+ "\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.add("\n");
        Paragraph admins = new Paragraph();
        admins.setAlignment(Element.ALIGN_LEFT);
        admins.setIndentationLeft(60);
        admins.setIndentationRight(60);
        admins.add("\n");
        document.add(admins);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+"\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage11(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena,String peiod) throws DocumentException
    {
        float niltags = Float.parseFloat(taiwan);
        float tot = Float.parseFloat(tota);
        float adm = Float.parseFloat(admi);
        float den = Float.parseFloat(dena);
        namanya = "";
        tarifnya = "";
        apinya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("nama");
            //   nama.add(namanya);
        }
        catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");

            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("nama");
                tarifnya = mainJSONObj.getString("lokasiOP");

            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN TAGIHAN\n");
        cv.add(kobe_podunk+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(60);
        tgl.setIndentationRight(60);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR : " + format + "\n");
        tgl.add("NOP             : " + input + "\n");
        tgl.add("NAMA               : " + namanya + "\n");
        tgl.add("ALAMAT           : " + tarifnya + "\n");
        tgl.add("TAHUN PAJAK : " + peiod + "\n\n");
        tgl.add("TAGIHAN          : Rp." + decimalFormat.format(niltags) + "\n");
        tgl.add("DENDA             : Rp." + decimalFormat.format(den) + "\n\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.add("Struk ini Merupakan Bukti\nPembayaran Yang Sah");
        document.add(keterangan);
        Paragraph admin = new Paragraph();
        admin.setAlignment(Element.ALIGN_LEFT);
        admin.setIndentationLeft(60);
        admin.setIndentationRight(60);
        admin.add("\n\n");
        admin.add("ADMIN BANK   : Rp." + decimalFormat.format(adm) + "\n");
        admin.add("TOTAL BAYAR : RP." +decimalFormat.format(tot)+ "\n\n");
        document.add(admin);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+"\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage12(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena,String peiod) throws DocumentException
    {
        float niltags = Float.parseFloat(taiwan);
        float tot = Float.parseFloat(tota);
        float adm = Float.parseFloat(admi);
        float den = Float.parseFloat(dena);
        namanya = "";
        tarifnya = "";
        apinya = "";
        try {
            JSONObject mainJSONObj = new JSONObject(ket);
            namanya = mainJSONObj.getString("customerName");
            //   nama.add(namanya);
        }
        catch (JSONException as) {
            as.printStackTrace();
        }
        if (namanya.isEmpty()) {
            try {
                JSONObject mainJSONObj = new JSONObject(ket);
                apinya = mainJSONObj.getString("api");

            } catch (JSONException as) {
                as.printStackTrace();
            }
            try {
                JSONObject mainJSONObj = new JSONObject(apinya);
                namanya = mainJSONObj.getString("customerName");
            } catch (JSONException as) {
                as.printStackTrace();
            }
        }
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK PEMBAYARAN TAGIHAN\n");
        cv.add(kobe_podunk+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(50);
        tgl.setIndentationRight(50);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR    : " + format + "\n");
        tgl.add("NO KONTRAK : " + input + "\n");
        tgl.add("LAYANAN  : " + kobe_podunk + "\n");
        tgl.add("NAMA         : " + namanya + "\n");
        tgl.add("PERIODE/TENOR  : " + peiod + "\n\n");
        tgl.add("TAGIHAN         : Rp." + decimalFormat.format(niltags) + "\n");
        tgl.add("PINALTI            : Rp." + decimalFormat.format(den) + "\n\n");
        document.add(tgl);
        Paragraph keterangan = new Paragraph();
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.setIndentationLeft(50);
        keterangan.setIndentationRight(50);
        keterangan.setAlignment(Element.ALIGN_CENTER);
        keterangan.add("Struk ini Merupakan Bukti\nPembayaran Yang Sah");
        document.add(keterangan);
        Paragraph admin = new Paragraph();
        admin.setAlignment(Element.ALIGN_LEFT);
        admin.setIndentationLeft(50);
        admin.setIndentationRight(50);
        admin.add("\n\n");
        admin.add("ADMIN BANK   : Rp." + decimalFormat.format(adm) + "\n");
        admin.add("TOTAL BAYAR : RP." +decimalFormat.format(tot)+ "\n\n");
        document.add(admin);
        Paragraph infotext = new Paragraph();
        infotext.setAlignment(Element.ALIGN_CENTER);
        infotext.setIndentationLeft(50);
        infotext.setIndentationRight(50);
        infotext.add("Terima Kasih" + "\n\n");
        infotext.add(kode_loket.get(0)+ " / "+ nama_loket.get(0)+ " / "+ username+"\n");
        document.add(infotext);
        document.left(50);
        document.right(50);
    }
    public void addTitlePage1(Document document,String kobe_podunk,String input, String tota,String taiwan,String admi,String user,String jml,String nore,String blan,String thun,String ket,String kolkt,String updat,String username,String dena,String peiod) throws DocumentException
    {float niltags = Float.parseFloat(tota);
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("STRUK BUKTI TOPUP\n"+kobe_podunk+" (CU)"+"\n\n");
        cv.setIndentationLeft(50);
        cv.setIndentationRight(50);
        document.add(cv);
        Paragraph tgl = new Paragraph();
        tgl.setAlignment(Element.ALIGN_LEFT);
        tgl.setIndentationLeft(70);
        tgl.setIndentationRight(70);
        long n=(Integer.parseInt(String.valueOf(updat)));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);
        tgl.add("TGL BAYAR  : " + format + "\n");
        tgl.add("LAYANAN     : " + kobe_podunk + "\n");
        tgl.add("ID PELANGGAN : " + input + "\n");
        tgl.add("HARGA         : Rp." +decimalFormat.format(niltags)+ "\n");
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
}