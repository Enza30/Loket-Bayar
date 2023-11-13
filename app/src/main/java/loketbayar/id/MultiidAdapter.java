package loketbayar.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.pdf.PdfWriter;
import com.mazenrashed.printooth.utilities.Printing;
import loketbayar.id.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MultiidAdapter extends RecyclerView.Adapter<MultiidAdapter.CustomViewHolder> {
    private SharedPreferences boyprefs;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private PdfWriter writer;
    Printing printing;
    private Dialog load;
    private ImageView ivX;
    PreferenceActivity.Header header;
    private float penaltyy=0,dendaa=0,feee=0,itungantotal=0,itunganhrgaloket=0;
    private ArrayList<String> pemakaianquerypdamarray = new ArrayList<>();
    private ArrayList<String> tagihanLainquerypdamarray = new ArrayList<>();
    private ArrayList<String> penaltyquerypdamarray = new ArrayList<>();
    private ArrayList<String> tarifquerypdamarray = new ArrayList<>();
    private ArrayList<String> feequeryquerypdamarray = new ArrayList<>();
    private ArrayList<String> dendaqueryPlnarray = new ArrayList<>();
    private ArrayList<String> idnya ;
    private ArrayList<String> kode_multi ;
    private ArrayList<String> kategori_multi ;
    private ArrayList<String> kode_ca_multi ;
    private ArrayList<String> kode_sub_ca_multi ;
    private ArrayList<String> kode_loket_multi ;
    private ArrayList<String> fee_ca_multi ;
    private ArrayList<String> fee_sub_ca_multi ;
    private ArrayList<String> created_on_multi ;
    private ArrayList<String> updated_on_multi ;
    private ArrayList<String> kode_biller_multi ;
    private ArrayList<String> kode_produk_biller_multi ;
    private ArrayList<String> nama_produk_biller_multi ;
    private ArrayList<String> nama_produk_multi ;
    private ArrayList<String> denom_multi ;
    private ArrayList<String> admin_biller_multi ;
    private ArrayList<String> harga_biller_multi ;
    private ArrayList<String> tipe_fee_multi ;
    private ArrayList<String> fee_biller_multi ;
    private ArrayList<String> fee_app_multi ;
    private ArrayList<String> idpel_multi ;
    private ArrayList<String> nama ;
    private ArrayList<String> jumlahTagihan ;
    private ArrayList<String> reff ;
    private ArrayList<String> serverresponses ;
    private ArrayList<String> totalTagihan ;
    private ArrayList<String> productCode ;
    private ArrayList<String> refID ;
    private ArrayList<String> periode_data ;
    private ArrayList<String> penalty_data ;
    private ArrayList<String> meterdata ;
    private ArrayList<String> tagihan_lain_data ;
    private ArrayList<String> nilai_tagihan_data ;
    private ArrayList<String> tagihan_data ;
    private ArrayList<String> admin_data ;
    private ArrayList<String> pemakaianquerypdam ;
    private ArrayList<String> tagihanLainquerypdam ;
    private ArrayList<String> penaltyquerypdam ;
    private ArrayList<String> tarifquerypdam ;
    private ArrayList<String> feequeryquerypdam ;
    private ArrayList<String> alamat ;
    private ArrayList<String> status_multi ;
    private ArrayList<String> subscriberID ;
    private ArrayList<String> tarifPln ;
    private ArrayList<String> daya ;
    private ArrayList<String> lembarTagihanTotal ;
    private ArrayList<String> lembarTagihan ;
    private ArrayList<String> total_data ;
    private ArrayList<String> denda_data ;
    private ArrayList<String> proses ;
    private ArrayList<String> nilaiTagihanquery ;
    private ArrayList<String> dendaqueryPln ;
    private ArrayList<String> adminquery ;
    private ArrayList<String> totalquery ;
    private ArrayList<String> divre ;
    private ArrayList<String> datel ;
    private ArrayList<String> feequeryqueryTelkomArray ;
    private ArrayList<String> fee_data ;
    MultiidAdapter boyiadapter2;
    private ArrayList<String> usernames ;
    private ArrayList<String> idmulti ;
    float adm=0,totsnya=0,totaltag=0,totslkt=0,jml=0;
    private ArrayList<String> kode_produk_multi ;
    private ArrayList<String> tarif ;
    private TextView tvTotalSemua;
    private DecimalFormat decimalFormat=new DecimalFormat("###,###,###.##");
    private final Activity context;
    private String username,password,datamultisemua;
    private int no = 0,lunas=0,gagal=0,siap=0;
    private RecyclerView rvList;
    private float penalty=0,denda=0,fee=0;
    private Button btBayar,btPdf;
    MultiidAdapter(Activity context, ArrayList<String> kode_multi, ArrayList<String> idnya,
                   ArrayList<String> idmulti, ArrayList<String> kode_produk_multi,
                   ArrayList<String> kode_ca_multi, ArrayList<String> kode_sub_ca_multi,
                   ArrayList<String> kode_loket_multi, ArrayList<String> fee_ca_multi,
                   ArrayList<String> fee_sub_ca_multi, ArrayList<String> created_on_multi,
                   ArrayList<String> updated_on_multi, ArrayList<String> kategori_multi,
                   ArrayList<String> kode_biller_multi, ArrayList<String> kode_produk_biller_multi,
                   ArrayList<String> nama_produk_biller_multi, ArrayList<String> nama_produk_multi,
                   ArrayList<String> denom_multi, ArrayList<String> admin_biller_multi,
                   ArrayList<String> harga_biller_multi, ArrayList<String> tipe_fee_multi,
                   ArrayList<String> fee_biller_multi, ArrayList<String> fee_app_multi,
                   ArrayList<String> status_multi, ArrayList<String> idpel_multi,
                   ArrayList<String> nama, ArrayList<String> jumlahTagihan,
                   ArrayList<String> reff, ArrayList<String> serverresponses,
                   ArrayList<String> totalTagihan, ArrayList<String> productCode,
                   ArrayList<String> refID, ArrayList<String> periode_data,
                   ArrayList<String> penalty_data, ArrayList<String> meterdata,
                   ArrayList<String> tagihan_lain_data, ArrayList<String> nilai_tagihan_data,
                   ArrayList<String> tagihan_data, ArrayList<String> admin_data,
                   ArrayList<String> pemakaianquerypdam, ArrayList<String> tagihanLainquerypdam,
                   ArrayList<String> penaltyquerypdam, ArrayList<String> tarifquerypdam,
                   ArrayList<String> feequeryquerypdam, ArrayList<String> alamat, ArrayList<String> subscriberID,
                   ArrayList<String> tarifPln, ArrayList<String> daya, ArrayList<String> lembarTagihanTotal,
                   ArrayList<String> lembarTagihan, ArrayList<String> total_data, ArrayList<String> denda_data,
                   ArrayList<String> proses, ArrayList<String> nilaiTagihanquery, ArrayList<String> dendaqueryPln,
                   ArrayList<String> adminquery, ArrayList<String> totalquery, ArrayList<String> usernames,
                   ArrayList<String> tarif, TextView tvTotalSemua,
                   ArrayList<String> divre, ArrayList<String> datel,
                   ArrayList<String> feequeryqueryTelkomArray, ArrayList<String> fee_data,
                   RecyclerView rvList, Button btPdf, Button btBayar) {
        this.tvTotalSemua=tvTotalSemua;
        this.btPdf=btPdf;
        this.btBayar=btBayar;
        this.divre=divre;
        this.datel=datel;
        this.rvList=rvList;
        this.feequeryqueryTelkomArray=feequeryqueryTelkomArray;
        this.fee_data=fee_data;
        this.idnya=idnya;
        this.kode_multi=kode_multi;
        this.kategori_multi=kategori_multi;
        this.kode_ca_multi=kode_ca_multi;
        this.kode_sub_ca_multi=kode_sub_ca_multi;
        this.kode_loket_multi=kode_loket_multi;
        this.fee_ca_multi=fee_ca_multi;
        this.fee_sub_ca_multi=fee_sub_ca_multi;
        this.created_on_multi=created_on_multi;
        this.updated_on_multi=updated_on_multi;
        this.kode_biller_multi=kode_biller_multi;
        this.kode_produk_biller_multi=kode_produk_biller_multi;
        this.nama_produk_biller_multi=nama_produk_biller_multi;
        this.nama_produk_multi=nama_produk_multi;
        this.denom_multi=denom_multi;
        this.admin_biller_multi=admin_biller_multi;
        this.harga_biller_multi=harga_biller_multi;
        this.tipe_fee_multi=tipe_fee_multi;
        this.fee_biller_multi=fee_biller_multi;
        this.fee_app_multi=fee_app_multi;
        this.idpel_multi=idpel_multi;
        this.nama=nama;
        this.jumlahTagihan=jumlahTagihan;
        this.reff=reff;
        this.serverresponses=serverresponses;
        this.totalTagihan=totalTagihan;
        this.productCode=productCode;
        this.refID=refID;
        this.periode_data=periode_data;
        this.penalty_data=penalty_data;
        this.meterdata=meterdata;
        this.tagihan_lain_data=tagihan_lain_data;
        this.nilai_tagihan_data=nilai_tagihan_data;
        this.tagihan_data=tagihan_data;
        this.admin_data=admin_data;
        this.pemakaianquerypdam=pemakaianquerypdam;
        this.tagihanLainquerypdam=tagihanLainquerypdam;
        this.penaltyquerypdam=penaltyquerypdam;
        this.tarifquerypdam=tarifquerypdam;
        this.feequeryquerypdam=feequeryquerypdam;
        this.alamat=alamat;
        this.status_multi=status_multi;
        this.subscriberID=subscriberID;
        this.tarifPln=tarifPln;
        this.daya=daya;
        this.lembarTagihanTotal=lembarTagihanTotal;
        this.lembarTagihan=lembarTagihan;
        this.total_data=total_data;
        this.denda_data=denda_data;
        this.proses=proses;
        this.nilaiTagihanquery=nilaiTagihanquery;
        this.dendaqueryPln=dendaqueryPln;
        this.adminquery=adminquery;
        this.totalquery=totalquery;
        this.usernames=usernames;
        this.idmulti=idmulti;
        this.kode_produk_multi=kode_produk_multi;
        this.tarif=tarif;
        this.context = context;
        boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public MultiidAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.rowmulti, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MultiidAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        no++;
        username=boyprefs.getString("username","");
        password=boyprefs.getString("password","");
        totsnya=0;adm=0;totaltag=0;penalty=0;denda=0;fee=0;jml=0;
        Log.e("proses",proses.get(position));

if(kategori_multi.get(position).equalsIgnoreCase("PLN")){
     totaltag= Float.parseFloat(nilaiTagihanquery.get(position))+Float.parseFloat(denda_data.get(position));
     jml=Float.parseFloat(lembarTagihan.get(position));
     adm= Float.parseFloat(adminquery.get(position));
     totsnya=Float.parseFloat(totalTagihan.get(position));
     totslkt=Float.parseFloat(fee_data.get(position));

    holder.tvTotal.setText(String.format(decimalFormat.format(totsnya))+"\n"+proses.get(position));
    holder.tvNamaProduk.setText(nama_produk_multi.get(position));
    holder.tvTagihan.setText(String.format(decimalFormat.format(totaltag))+"\n"+nama.get(position));
    holder.tvPeriode.setText(periode_data.get(position));
    holder.tvAdmin.setText(String.format(decimalFormat.format(adm)));
    holder.tvNo.setText(String.valueOf(no));
}
else if(kategori_multi.get(position).equalsIgnoreCase("PDAM")){
    totaltag= Float.parseFloat(nilaiTagihanquery.get(position))+Float.parseFloat(denda_data.get(position));
    jml=Float.parseFloat(lembarTagihan.get(position));
    adm= Float.parseFloat(adminquery.get(position));
    totsnya=Float.parseFloat(totalTagihan.get(position));
    totslkt=Float.parseFloat(fee_data.get(position));

    holder.tvTotal.setText(String.format(decimalFormat.format(totsnya))+"\n"+proses.get(position));
    holder.tvNamaProduk.setText(nama_produk_multi.get(position));
    holder.tvTagihan.setText(String.format(decimalFormat.format(totaltag))+"\n"+nama.get(position));
    holder.tvPeriode.setText(periode_data.get(position));
    holder.tvAdmin.setText(String.format(decimalFormat.format(adm)));
    holder.tvNo.setText(String.valueOf(no));
}
else if(kategori_multi.get(position).equalsIgnoreCase("TELKOM")){
    totaltag= Float.parseFloat(nilaiTagihanquery.get(position))+Float.parseFloat(denda_data.get(position));
    jml=Float.parseFloat(lembarTagihan.get(position));
    adm= Float.parseFloat(adminquery.get(position));
    totsnya=Float.parseFloat(totalTagihan.get(position));
    totslkt=Float.parseFloat(fee_data.get(position));

    holder.tvTotal.setText(String.format(decimalFormat.format(totsnya))+"\n"+proses.get(position));
    holder.tvNamaProduk.setText(nama_produk_multi.get(position));
    holder.tvTagihan.setText(String.format(decimalFormat.format(totaltag))+"\n"+nama.get(position));
    holder.tvPeriode.setText(periode_data.get(position));
    holder.tvAdmin.setText(String.format(decimalFormat.format(adm)));
    holder.tvNo.setText(String.valueOf(no));
        }
else if(kategori_multi.get(position).equalsIgnoreCase("BPJSKES")){
    totaltag= Float.parseFloat(nilaiTagihanquery.get(position))+Float.parseFloat(denda_data.get(position));
    jml=Float.parseFloat(lembarTagihan.get(position));
    adm= Float.parseFloat(adminquery.get(position));
    totsnya=Float.parseFloat(totalTagihan.get(position));
    totslkt=Float.parseFloat(fee_data.get(position));

    holder.tvTotal.setText(String.format(decimalFormat.format(totsnya))+"\n"+proses.get(position));
    holder.tvNamaProduk.setText(nama_produk_multi.get(position));
    holder.tvTagihan.setText(String.format(decimalFormat.format(totaltag))+"\n"+nama.get(position));
    holder.tvPeriode.setText(periode_data.get(position));
    holder.tvAdmin.setText(String.format(decimalFormat.format(adm)));
    holder.tvNo.setText(String.valueOf(no));
}
else if(kategori_multi.get(position).equalsIgnoreCase("PASCABAYAR")){
    totaltag= Float.parseFloat(nilaiTagihanquery.get(position))+Float.parseFloat(denda_data.get(position));
    jml=Float.parseFloat(lembarTagihan.get(position));
    adm= Float.parseFloat(adminquery.get(position));
    totsnya=Float.parseFloat(totalTagihan.get(position));
    totslkt=Float.parseFloat(fee_data.get(position));

    holder.tvTotal.setText(String.format(decimalFormat.format(totsnya))+"\n"+proses.get(position));
    holder.tvNamaProduk.setText(nama_produk_multi.get(position));
    holder.tvTagihan.setText(String.format(decimalFormat.format(totaltag))+"\n"+nama.get(position));
    holder.tvPeriode.setText(periode_data.get(position));
    holder.tvAdmin.setText(String.format(decimalFormat.format(adm)));
    holder.tvNo.setText(String.valueOf(no));
}

    holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proses.get(position).equalsIgnoreCase("BLM LNS")){
                    load = new Dialog(context);
                load.setContentView(R.layout.dialogload2);
                load.setCancelable(false);
                ivX = load.findViewById(R.id.ivX);
                Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                load.show();
                HapusData(context.getResources().getString(R.string.xyz) + "hapusdatasatuan",
                        idnya.get(position), username, password);
            }
                else {

                }

            }
        });
    }

    void HapusData(String POST_ORDER,String id,String usernames,String passwords) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if(ServerResponse.trim().equalsIgnoreCase("sukses")){

                        Ambilmulti(context.getResources().getString(R.string.xyz)+
                                "ambildatamultisemua",kode_multi.get(0),username,password);
                    }else{
                        load.dismiss();
                        Log.e("result",ServerResponse);
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
                params.put("id", id);
                params.put("username", usernames);
                params.put("password", passwords);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void Ambilmulti(String POST_ORDER, String kodemulti, String usernames, String passwords) {
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
                       /* boyiadapter2.notifyDataSetChanged();
                        rvList.setAdapter(boyiadapter2);
                        btPdf.setEnabled(false);
                        btBayar.setEnabled(false);*/
                        datamultisemua=ServerResponse;
                        Pembaruan();
                        // Toast.makeText(MultiIdPayment.this, ServerResponse, Toast.LENGTH_SHORT).show();
                    }
                }
            }, 20);

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    void Pembaruan(){
        Log.e("datamultisemua", datamultisemua);
        kode_multi = PojoMion.AmbilArray(datamultisemua,"kode_unik","datamultisemua");
        idnya = PojoMion.AmbilArray(datamultisemua,"id","datamultisemua");
        idmulti = PojoMion.AmbilArray(datamultisemua,"idpel","datamultisemua");
        kode_produk_multi = PojoMion.AmbilArray(datamultisemua,"kode_produk","datamultisemua");
        kode_ca_multi = PojoMion.AmbilArray(datamultisemua,"kode_ca","datamultisemua");
        kode_sub_ca_multi = PojoMion.AmbilArray(datamultisemua,"kode_sub_ca","datamultisemua");
        kode_loket_multi = PojoMion.AmbilArray(datamultisemua,"kode_loket","datamultisemua");
        fee_ca_multi = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
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
        meterdata = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        tagihan_lain_data = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        nilai_tagihan_data = PojoMion.AmbilArray(datamultisemua,"tagihan","datamultisemua");
        tagihan_data = PojoMion.AmbilArray(datamultisemua,"tagihan","datamultisemua");
        admin_data = PojoMion.AmbilArray(datamultisemua,"admin","datamultisemua");
        pemakaianquerypdamarray = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        tagihanLainquerypdamarray = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        penaltyquerypdamarray = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
        tarifquerypdamarray = PojoMion.AmbilArray(datamultisemua,"lembar","datamultisemua");
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
        ViewCompat.setNestedScrollingEnabled(rvList, false);
        boyiadapter2 = new MultiidAdapter(context,
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
            totsnya=Float.parseFloat(totalTagihan.get(x));
            totslkt=Float.parseFloat(fee_data.get(x));
            itungantotal=itungantotal+totsnya;
            itunganhrgaloket=itunganhrgaloket+totslkt;
//            tvTotalSemua.setText("TOTAL TAGIHAN: Rp. "+decimalFormat.format(itungantotal));
            tvTotalSemua.setText("Total Tagihan: Rp. "+decimalFormat.format(itungantotal)+"\nSaldo akan terpotong: Rp. "+decimalFormat.format(itunganhrgaloket));

        }

        if(idnya.size()>0){
                btPdf.setEnabled(true);
                btBayar.setEnabled(true);

        }else{
            btPdf.setEnabled(false);
            btBayar.setEnabled(false);
            tvTotalSemua.setText("TOTAL TAGIHAN: Rp. 0\nSaldo akan terpotong: Rp. 0");
        }
    }
    @Override
    public int getItemCount() {
        return idpel_multi.size();
    }
    static class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView tvTagihan,tvTotal,tvPeriode,tvAdmin,tvNo,tvNamaProduk;
    private Button btDelete;
    CustomViewHolder(View view) {
        super(view);
        this.tvNo=view.findViewById(R.id.tvNo);
        this.btDelete=view.findViewById(R.id.btDelete);
        this.tvNamaProduk=view.findViewById(R.id.tvNamaProduk);
        this.tvTagihan=view.findViewById(R.id.tvTagihan);
        this.tvTotal=view.findViewById(R.id.tvTotal);
        this.tvPeriode=view.findViewById(R.id.tvPeriode);
        this.tvAdmin=view.findViewById(R.id.tvAdmin);
    }
}
}