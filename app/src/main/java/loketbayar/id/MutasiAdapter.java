package loketbayar.id;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MutasiAdapter extends RecyclerView.Adapter<MutasiAdapter.CustomViewHolder> implements PrintingCallback {
    private SharedPreferences boyprefs;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private PdfWriter writer;
    Printing printing;
    StringBuilder hasiltoken;
    StringBuilder lembar_tagihan_untuk_data;
    StringBuilder mtrawal_tagihan_untuk_data;
    StringBuilder mtrakhir_tagihan_untuk_data;
    StringBuilder penalty_tagihan_untuk_data;
    StringBuilder tagihanLain_tagihan_untuk_data;
    StringBuilder admin_tagihan_untuk_data;
    StringBuilder nilaiTagihan_tagihan_untuk_data;

    PreferenceActivity.Header header;
    ArrayList<String> meterAwal = new ArrayList<>();
    ArrayList<String> meterAkhir = new ArrayList<>();
    ArrayList<String> jsonnya = new ArrayList<>();
    private ArrayList<String> tipe;
    private ArrayList<String> debet;
    private ArrayList<String> keterangan;
    private ArrayList<String> kredit;
    private ArrayList<String> saldo;
    private ArrayList<String> nama_loket;
    private ArrayList<String> kode_loket;
    private ArrayList<String> update;
    private DecimalFormat format=new DecimalFormat("###,###,###.##");
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
    private final Activity context;

    private String bulan,tahun,s,namanya = "",apinya = "" ,dayanya = "", tarifnya = "",detilTagihan="",lembar="",data="",msn="",subscriberID="",biayaMeterai="",ppn="",ppj="",angsuran="",rpToken="",kwh="",mtrakhir="",mtrawal="",period="",infotextt="";
    private int no = 0;
    MutasiAdapter(Activity context, ArrayList<String> tipe, ArrayList<String> debet,
                  ArrayList<String> keterangan, ArrayList<String> kredit,ArrayList<String> saldo,ArrayList<String> nama_loket,ArrayList<String> kode_loket,
            ArrayList<String> update) {
        this.tipe = tipe;
        this.debet = debet;
        this.kredit = kredit;
        this.keterangan = keterangan;
        this.saldo = saldo;
        this.nama_loket = nama_loket;
        this.kode_loket = kode_loket;
        this.update = update;
        this.context = context;
        boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);

    }


    @NonNull
    @Override
    public MutasiAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.rowmutasi, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new CustomViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull final MutasiAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        long n=(Integer.parseInt(String.valueOf(update.get(position))));
        java.util.Date time = new java.util.Date((long)n*1000);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String format = df.format(time);

            Printooth.INSTANCE.init(context);
        holder.tvProduk.setText( (kode_loket.get(position) + "\n" + format));
        holder.tvNama.setText((keterangan.get(position).replace("{","").replace(":"," ").replace("}","").replace(","," ")));
            initview();
            float premmmm = Float.parseFloat(debet.get(position));
            holder.tvSaldo.setText(("Debit: ")+decimalFormat.format(Float.parseFloat(debet.get(position))) +  "\n" + (("Kredit: ")
                    + decimalFormat.format(Float.parseFloat(kredit.get(position)))) + "\n" + (("Saldo: ")
                    + decimalFormat.format(Float.parseFloat(saldo.get(position)))));



        //  }

    }

    @Override
    public int getItemCount() {
        return tipe.size();
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
        TextView tvProduk,tvNama,tvSaldo;
   //     private ImageView ivPrint,ivPdf;
        CustomViewHolder(View view) {

            super(view);
            this.tvProduk=view.findViewById(R.id.tvProduk);
            this.tvNama=view.findViewById(R.id.tvNama);
            this.tvSaldo=view.findViewById(R.id.tvSaldo);

        }
    }

    void initview() {
        if (printing != null) {
            printing.setPrintingCallback(this);
        }
    }



    public void addMetaData(Document document) {
        document.addTitle("Faktur");
        document.addSubject("Faktur");
        document.addKeywords("Company");
    }


}
