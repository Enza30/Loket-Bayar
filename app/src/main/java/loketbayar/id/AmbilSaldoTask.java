package loketbayar.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
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

class AmbilSaldoTask extends AsyncTask<String, Void, String> {
    private Context ctx;
    private String results;
    ArrayList<String> saldonya=new ArrayList<>();
    private TextView tvSaldo;
    AmbilSaldoTask(Context ctx, TextView tvSaldo) {
        this.ctx = ctx;
        this.tvSaldo = tvSaldo;
    }
    private Dialog dialog;
    private Dialog load;
    private String base,login_url;

    ArrayList<String> saldo=new ArrayList<>();
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
        String login_url = ctx.getResources().getString(R.string.xyz)+"ambilsaldosec/";
        int timestamp = (int) (new Date().getTime()/1000);
        String authToken = ctx.getResources().getString(R.string.key)+ String.valueOf(timestamp)+ ctx.getResources().getString(R.string.secret);
        String method = params[0];
        if (method.equalsIgnoreCase("ambil")) {
            String username=params[1];
            String password=params[2];
            String kode_loket=params[3];


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
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&" +
                        URLEncoder.encode("kode_loket", "UTF-8") + "=" + URLEncoder.encode(kode_loket, "UTF-8")+ "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
        Log.e("datasaldoditasklain",result);
        try {
           if(result.contains("datasaldo")){
               results=result;

               saldonya= PojoMion.AmbilArray(results,"saldo","datasaldo");
               float s= Float.parseFloat(saldonya.get(0));
               DecimalFormat decimalFormat=new DecimalFormat("###,###,###.##");
               tvSaldo.setText("Rp."+decimalFormat.format(s));
               Pln.saldobayar=s;
               Pln.saldoawal=s;
               Paskabayar.saldobayar=s;
               Paskabayar.saldoawal=s;
               Topup.saldobayar=s;
               Topup.saldoawal=s;
               Bpjs.saldobayar=s;
               Bpjs.saldoawal=s;
               PulsaPrabayar.saldobayar=s;
               PulsaPrabayar.saldoawal=s;
            }else{
               Log.e("resilt",result);
               Toast.makeText(ctx,"User / Password Salah", Toast.LENGTH_LONG).show();
               Intent intent=new Intent(ctx, Loginpage.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               ctx.startActivity(intent);
               ((Activity)ctx).finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx,"Gagal Login / Tidak Ada Sinyal", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ctx, Loginpage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
        }
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
