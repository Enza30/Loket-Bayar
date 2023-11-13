package loketbayar.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class PesertaAdapter extends RecyclerView.Adapter<PesertaAdapter.CustomViewHolder> {
    private SharedPreferences boyprefs;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    PreferenceActivity.Header header;
    private ArrayList<String> noPeserta;
    private ArrayList<String> namaquerydatabpjs;
    private ArrayList<String> premidatabpjs;
    private ArrayList<String> saldodariquerydatabpjs;
    private DecimalFormat fors=new DecimalFormat("###,###,###.##");
    private final Activity context;
    private int no = 0;
    PesertaAdapter(Activity context, ArrayList<String> noPeserta, ArrayList<String> namaquerydatabpjs,
                   ArrayList<String> premidatabpjs, ArrayList<String> saldodariquerydatabpjs) {
        this.noPeserta = noPeserta;
        this.namaquerydatabpjs = namaquerydatabpjs;
        this.premidatabpjs = premidatabpjs;
        this.saldodariquerydatabpjs = saldodariquerydatabpjs;
        this.context = context;
        boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public PesertaAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.rowpeserta, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new CustomViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final PesertaAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") final int position) {
    holder.tvNomer.setText(noPeserta.get(position));
    holder.tvNampes.setText(namaquerydatabpjs.get(position));
    float premmmm= Float.parseFloat(premidatabpjs.get(position));
        holder.tvPrem.setText("Rp."+fors.format(premmmm));


    }

    @Override
    public int getItemCount() {
        return namaquerydatabpjs.size();
    }

class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView tvNomer,tvNampes,tvPrem;
    private ImageView ivMore;
    private LinearLayout llMain;
    CustomViewHolder(View view) {
        super(view);
        this.tvNomer=view.findViewById(R.id.tvNomer);
        this.tvPrem=view.findViewById(R.id.tvPrem);
        this.tvNampes=view.findViewById(R.id.tvNampes);
        this.llMain=view.findViewById(R.id.llMain);




    }
}




}






