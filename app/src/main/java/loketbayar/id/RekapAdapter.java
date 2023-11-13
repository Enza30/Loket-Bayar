package loketbayar.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.pdf.PdfWriter;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.utilities.Printing;
import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RekapAdapter extends RecyclerView.Adapter<RekapAdapter.CustomViewHolder> {
    private SharedPreferences boyprefs;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private PdfWriter writer;
    Printing printing;
    private float subtotal=0,totalsemua=0,hargaloket=0,fees=0,totalhargaloket=0,totalfee=0,feessss=0;
    private int ttltrx=0,ttltrx2=0;
    PreferenceActivity.Header header;
    private ArrayList<String> kode_produk;
    private ArrayList<String> inputs;
    private ArrayList<String> nama_produk;
    private ArrayList<String> nama;
    private ArrayList<String> usernames;
    private ArrayList<String> total;
    private ArrayList<String> keterangan;
    private ArrayList<String> fee_loket;
    private ArrayList<String> harga_loket;
    private ArrayList<String> jumlah_transaksi;
    private ArrayList<String> status;
    private ArrayList<String> fee_bulanan;
    private String username="";
    private TextView tvTotal,tvTotalfee,tvHarlok,tvTotaltrx;
    private DecimalFormat fors=new DecimalFormat("###,###,###.##");
    private final Activity context;
    private String bulan,tahun,statuss;
    private int no = 0;
    RekapAdapter(Activity context, ArrayList<String> kode_produk, ArrayList<String> inputs,
                 ArrayList<String> nama, ArrayList<String> total, String bulan, String tahun,
                 ArrayList<String> nama_produk, ArrayList<String> usernames, ArrayList<String> keterangan,
                 ArrayList<String> fee_loket, ArrayList<String> harga_loket,
                 TextView tvTotal, TextView tvTotalfee, TextView tvHarlok,ArrayList<String> jumlah_transaksi,ArrayList<String> status
            ,ArrayList<String> fee_bulanan) {

        this.fee_loket = fee_loket;
        this.tvTotal = tvTotal;
        this.tvTotalfee = tvTotalfee;
        this.tvHarlok = tvHarlok;
        this.harga_loket = harga_loket;
        this.kode_produk = kode_produk;
        this.keterangan = keterangan;
        this.usernames = usernames;
        this.inputs = inputs;
        this.nama_produk = nama_produk;
        this.bulan = bulan;
        this.tahun = tahun;
        this.nama = nama;
        this.total = total;
        this.jumlah_transaksi = jumlah_transaksi;
        this.fee_bulanan = fee_bulanan;
        this.context = context;
        boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);


    }

    @NonNull
    @Override
    public RekapAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.rowrekap, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new CustomViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final RekapAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Printooth.INSTANCE.init(context);

            float premmmmss = Float.parseFloat(total.get(position));
            float harloksss = Float.parseFloat(harga_loket.get(position));
            float feess = Float.parseFloat(fee_loket.get(position));
        float feesss = Float.parseFloat(fee_bulanan.get(position));
            totalsemua = totalsemua + premmmmss;
            totalhargaloket = totalhargaloket + harloksss;
            totalfee = totalfee + feess+feesss;


            if (position == 0) {
                //           statuss=status.get(position);
                //         if(statuss.equalsIgnoreCase("sukses")) {
                holder.tvUser.setText(usernames.get(position));
                float premmmms = Float.parseFloat(total.get(position));
                float harlok = Float.parseFloat(harga_loket.get(position));
                float fee = Float.parseFloat(fee_loket.get(position));
                float feee = Float.parseFloat(fee_bulanan.get(position));
                subtotal = subtotal + premmmms;
                hargaloket = hargaloket + harlok;
                fees = fees + fee ;
                feessss = feessss + feee ;
                int ttltrx3 = Integer.parseInt(jumlah_transaksi.get(position));
                ttltrx = ttltrx3 + ttltrx;
                //  ttltrx++;
                username = usernames.get(position);
                holder.llSubAtas.setVisibility(View.GONE);
            }
            //}
            else if (position == total.size() - 1) {
//            statuss=status.get(position);
                //          if(statuss.equalsIgnoreCase("sukses")) {
                holder.tvUser.setText("");
                float premmmms = Float.parseFloat(total.get(position));
                float harlok = Float.parseFloat(harga_loket.get(position));
                float fee = Float.parseFloat(fee_loket.get(position));
                float feee = Float.parseFloat(fee_bulanan.get(position));
                int ttltrx3 = Integer.parseInt(jumlah_transaksi.get(position));
                ttltrx = ttltrx3 + ttltrx;
                //  ttltrx++;
//            ttltrx2=ttltrx+ttltrx2;
                subtotal = subtotal + premmmms;
                hargaloket = hargaloket + harlok;
                fees = fees + fee;
                feessss = feessss + feee ;
                holder.llSubAtas.setVisibility(View.GONE);
                holder.tvProduksub2.setText(String.valueOf(ttltrx));
                holder.tvSubtotal2.setText(String.format("Rp.%s", fors.format(subtotal)));
                holder.tvHargaLoketsub2.setText(String.format("Rp.%s", fors.format(hargaloket)));
                holder.tvFeesub2.setText(String.format("Rp.%s", fors.format(fees))+"/"+(String.format("Rp.%s", fors.format(feessss))));
                holder.llSubBawah.setVisibility(View.VISIBLE);
                subtotal = 0;
                hargaloket = 0;
                fees = 0;
                feessss = 0;
                ttltrx = 0;
            }
            //}
            else {
                //        statuss=status.get(position);
                //      if(statuss.equalsIgnoreCase("sukses")) {
                if (username.equalsIgnoreCase(usernames.get(position))) {
                    float premmmms = Float.parseFloat(total.get(position));
                    float harlok = Float.parseFloat(harga_loket.get(position));
                    float fee = Float.parseFloat(fee_loket.get(position));
                    float feee = Float.parseFloat(fee_bulanan.get(position));
                    fees = fees + fee;
                    feessss = feessss + feee ;
                    subtotal = subtotal + premmmms;
                    int ttltrx3 = Integer.parseInt(jumlah_transaksi.get(position));
                    ttltrx = ttltrx3 + ttltrx;
                    //  ttltrx++;
                    hargaloket = hargaloket + harlok;
                    holder.llSubAtas.setVisibility(View.GONE);
                    holder.llSubBawah.setVisibility(View.GONE);
                    holder.tvUser.setText("");

                }
                //}
                else {
                    //      statuss=status.get(position);
                    //    if(statuss.equalsIgnoreCase("sukses")) {
                    username = usernames.get(position);
                    holder.tvUser.setText(username);
                    holder.llSubBawah.setVisibility(View.GONE);
                    holder.tvProduksub.setText(String.valueOf(ttltrx));
                    holder.tvSubtotal.setText(String.format("Rp.%s", fors.format(subtotal)));
                    holder.tvHargaLoketsub.setText(String.format("Rp.%s", fors.format(hargaloket)));
                    holder.tvFeesub.setText(String.format("Rp.%s", fors.format(fees))+"/"+(String.format("Rp.%s", fors.format(feessss))));
                    holder.llSubAtas.setVisibility(View.VISIBLE);
                    subtotal = 0;
                    hargaloket = 0;
                    fees = 0;
                    feessss = 0;
                    ttltrx = 0;
                    float premmmms = Float.parseFloat(total.get(position));
                    float harlok = Float.parseFloat(harga_loket.get(position));
                    subtotal = subtotal + premmmms;
                    hargaloket = hargaloket + harlok;
                    float fee = Float.parseFloat(fee_loket.get(position));
                    float feee = Float.parseFloat(fee_bulanan.get(position));
                    fees = fees + fee;
                    feessss = feessss + feee ;
                    int ttltrx3 = Integer.parseInt(jumlah_transaksi.get(position));
                    ttltrx = ttltrx3 + ttltrx;
                    //  ttltrx++;
                }
                //}
            }


            tvTotal.setText("Rp." + fors.format(totalsemua));
            tvTotalfee.setText("Rp." + fors.format(totalfee));
            tvHarlok.setText("Rp." + fors.format(totalhargaloket));

            HasilRekap.totalsemua = totalsemua;
            HasilRekap.totalhargaloket = totalhargaloket;
            HasilRekap.totalfee = totalfee;
            holder.tvProduk.setText(nama_produk.get(position) + " - " + jumlah_transaksi.get(position));


            float premmmm = Float.parseFloat(total.get(position));
            float hargaloket = Float.parseFloat(harga_loket.get(position));
            float feeloket = Float.parseFloat(fee_loket.get(position));
        float feebulan = Float.parseFloat(fee_bulanan.get(position));
            holder.tvTotal.setText(String.format("Rp.%s", fors.format(premmmm)));
            holder.tvHargaloket.setText(String.format("Rp.%s", fors.format(hargaloket)));
            holder.tvFee.setText(String.format("Rp.%s", fors.format(feeloket))+"/"+(String.format("Rp.%s", fors.format(feebulan))));

            if (position % 2 == 0) {
                Log.e("position", String.valueOf(position));
                holder.llutama.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            }
            else {
                Log.e("positiontidak", String.valueOf(position));
                holder.llutama.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }


    @Override
    public int getItemCount() {
        return kode_produk.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser,tvProduk,tvFee,tvHargaloket,tvTotal,tvSubtotal,tvSubtotal2,tvHargaLoketsub,tvHargaLoketsub2,tvFeesub2,tvFeesub,tvProduksub2,tvProduksub;
        LinearLayout llutama,llSubAtas,llSubBawah;
        CustomViewHolder(View view) {
            super(view);
            this.llSubAtas=view.findViewById(R.id.llSubAtas);
            this.tvFeesub2=view.findViewById(R.id.tvFeesub2);
            this.tvFeesub=view.findViewById(R.id.tvFeesub);
            this.tvProduksub=view.findViewById(R.id.tvProduksub);
            this.tvHargaLoketsub=view.findViewById(R.id.tvHargaLoketsub);
            this.tvHargaLoketsub2=view.findViewById(R.id.tvHargaLoketsub2);
            this.tvProduksub2=view.findViewById(R.id.tvProduksub2);
            this.tvProduk=view.findViewById(R.id.tvProduk);
            this.llSubBawah=view.findViewById(R.id.llSubBawah);
            this.tvFee=view.findViewById(R.id.tvFee);
            this.tvSubtotal=view.findViewById(R.id.tvSubtotal);
            this.tvSubtotal2=view.findViewById(R.id.tvSubtotal2);
            this.tvUser=view.findViewById(R.id.tvUser);
            this.tvHargaloket=view.findViewById(R.id.tvHargaLoket);
            this.llutama=view.findViewById(R.id.llutama);
            this.tvTotal=view.findViewById(R.id.tvTotal);
        }
    }

}






