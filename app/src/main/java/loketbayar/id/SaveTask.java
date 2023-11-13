package loketbayar.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import loketbayar.id.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class SaveTask extends AsyncTask<String, Void, String> {
   private Context ctx;
   private DecimalFormat decimalFormat=new DecimalFormat("###,###,###.##");
   @SuppressLint("StaticFieldLeak")
   private LinearLayout llCetak;
   private TextView tvSaldo;
    SaveTask(Context ctx, LinearLayout llCetak, TextView tvSaldo) {
        this.ctx = ctx;
        this.llCetak = llCetak;
        this.tvSaldo = tvSaldo;
    }
    private Dialog dialog;
    private Dialog load;
    private String base,login_url;
    private ArrayList<String> roleText=new ArrayList<>();
    private ArrayList<String> jam=new ArrayList<>();
    private ArrayList<String> tanggal=new ArrayList<>();
    private ArrayList<String> status=new ArrayList<>();
    private String sebagai,tglabsen="";
    private SharedPreferences boyprefs;
    @Override
    protected void onPreExecute() {
        boyprefs =ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        dialog=new Dialog(ctx);
        dialog.setContentView(R.layout.dialogload2);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ivX=dialog.findViewById(R.id.ivX);
        dialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = ctx.getResources().getString(R.string.xyz)+"savingplndllsec/";
        int timestamp = (int) (new Date().getTime()/1000);
        String authToken = ctx.getResources().getString(R.string.key)+ String.valueOf(timestamp)+ ctx.getResources().getString(R.string.secret);
        String method = params[0];
        if (method.equalsIgnoreCase("save")) {

          String   kode_ca=params[1];
            String kode_sub_ca=params[2];
            String kode_loket =params[3];
            String kode_produk =params[4];
            String kode_unik=params[5];
            String username =params[6];
            String inputs=params[7];
            String ref_id =params[8];
            String harga_biller =params[9];
            String harga_loket =params[10];
            String tagihan =params[11];
            String admin =params[12];
            String denda=params[13];
            String jumlah_transaksi =params[14];
            String periode =params[15];
            String fee_app =params[16];
            String fee_ca  =params[17];
            String fee_sub_ca  =params[18];
            String fee_loket  =params[19];
            String total  =params[20];
            String no_reff   =params[21];
            String status   =params[22];
            String keterangan   =params[23];
            String saldo   =params[24];
            String saldoawal   =params[25];
            String saldoakhir   =params[26];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("apikey", ctx.getResources().getString(R.string.key));
                httpURLConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                httpURLConnection.setRequestProperty("Authentication", getHash(ctx.getResources().getString(R.string.secret), authToken));
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("kode_ca", "UTF-8") + "=" + URLEncoder.encode(kode_ca, "UTF-8") + "&" +
                        URLEncoder.encode("kode_sub_ca", "UTF-8") + "=" + URLEncoder.encode(kode_sub_ca, "UTF-8") + "&" +
                        URLEncoder.encode("kode_loket", "UTF-8") + "=" + URLEncoder.encode(kode_loket, "UTF-8") + "&" +
                        URLEncoder.encode("kode_produk", "UTF-8") + "=" + URLEncoder.encode(kode_produk, "UTF-8") + "&" +
                        URLEncoder.encode("kode_unik", "UTF-8") + "=" + URLEncoder.encode(kode_unik, "UTF-8") + "&" +
                        URLEncoder.encode("inputs", "UTF-8") + "=" + URLEncoder.encode(inputs, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("ref_id", "UTF-8") + "=" + URLEncoder.encode(ref_id, "UTF-8") + "&" +
                        URLEncoder.encode("harga_biller", "UTF-8") + "=" + URLEncoder.encode(harga_biller, "UTF-8") + "&" +
                        URLEncoder.encode("harga_loket", "UTF-8") + "=" + URLEncoder.encode(harga_loket, "UTF-8") + "&" +
                        URLEncoder.encode("tagihan", "UTF-8") + "=" + URLEncoder.encode(tagihan, "UTF-8") + "&" +
                        URLEncoder.encode("admin", "UTF-8") + "=" + URLEncoder.encode(admin, "UTF-8") + "&" +
                        URLEncoder.encode("denda", "UTF-8") + "=" + URLEncoder.encode(denda, "UTF-8") + "&" +
                        URLEncoder.encode("jumlah_transaksi", "UTF-8") + "=" + URLEncoder.encode(jumlah_transaksi, "UTF-8") + "&" +
                        URLEncoder.encode("periode", "UTF-8") + "=" + URLEncoder.encode(periode, "UTF-8") + "&" +
                URLEncoder.encode("fee_app", "UTF-8") + "=" + URLEncoder.encode(fee_app, "UTF-8") + "&" +
                URLEncoder.encode("fee_ca", "UTF-8") + "=" + URLEncoder.encode(fee_ca, "UTF-8") + "&" +
                        URLEncoder.encode("fee_sub_ca", "UTF-8") + "=" + URLEncoder.encode(fee_sub_ca, "UTF-8") + "&" +
                        URLEncoder.encode("fee_loket", "UTF-8") + "=" + URLEncoder.encode(fee_loket, "UTF-8") + "&" +
                        URLEncoder.encode("total", "UTF-8") + "=" + URLEncoder.encode(total, "UTF-8") + "&" +
                        URLEncoder.encode("no_reff", "UTF-8") + "=" + URLEncoder.encode(no_reff, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&" +
                        URLEncoder.encode("saldo", "UTF-8") + "=" + URLEncoder.encode(saldo, "UTF-8") + "&" +
                        URLEncoder.encode("keterangan", "UTF-8") + "=" + URLEncoder.encode(keterangan, "UTF-8") + "&" +
                URLEncoder.encode("saldoawal", "UTF-8") + "=" + URLEncoder.encode(saldoawal, "UTF-8") + "&" +
                        URLEncoder.encode("saldoakhir", "UTF-8") + "=" + URLEncoder.encode(saldoakhir, "UTF-8");
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
        Log.e("res",result);
       // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
        try {
           if(result.trim().equalsIgnoreCase("sukses")){

              runThread();

            }else{
                Toast.makeText(ctx,result, Toast.LENGTH_LONG).show();

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
                    llCetak.setVisibility(View.VISIBLE);
                 /*   Intent intent=new Intent(ctx,Pln.class);
                    ctx.startActivity(intent);
                    ((Activity)ctx).finish();*/
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }.start();

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
