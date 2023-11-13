package loketbayar.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class LoginTask extends AsyncTask<String, Void, String> {
   private Context ctx;
   boolean pln,bpjs,paskabayar,Topup,prabayar,Telkom,pdam,Angsuran,transferkerek,MultiIdPayment,
           Samsat,kirimloket,Pbb,Tvkabel,MultiIdPayment2,daftaragen,editmarkup,cairsaldo,Profiling2,transfer;
    private String username,password,spg,slid;
    LoginTask(Context ctx) {
        this.ctx = ctx;
    }
    private Dialog dialog;
    private Dialog load;
    private int notiftext=0,notiftextawal=0;
    private int notifimage=0,notifimageawal=0;
    ArrayList<String> jmlnotiftext=new ArrayList<>();
    ArrayList<String> jmlnotifimage=new ArrayList<>();
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
        dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.dialogload2);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ivX = dialog.findViewById(R.id.ivX);
        dialog.show();
        pln=boyprefs.getBoolean("pln",false);
        Topup=boyprefs.getBoolean("Topup",false);
        bpjs=boyprefs.getBoolean("bpjs",false);
        Samsat=boyprefs.getBoolean("Samsat",false);
        transferkerek=boyprefs.getBoolean("transferkerek",false);
        kirimloket=boyprefs.getBoolean("kirimloket",false);
        paskabayar=boyprefs.getBoolean("paskabayar",false);
        Telkom=boyprefs.getBoolean("Telkom",false);
        pdam=boyprefs.getBoolean("Pdam",false);
        prabayar=boyprefs.getBoolean("PulsaPrabayar",false);
        Angsuran=boyprefs.getBoolean("Angsuran",false);
        MultiIdPayment=boyprefs.getBoolean("MultiIdPayment",false);
        MultiIdPayment2=boyprefs.getBoolean("MultiIdPayment2",false);
        Pbb=boyprefs.getBoolean("Pbb",false);
        daftaragen=boyprefs.getBoolean("daftaragen",false);
        editmarkup=boyprefs.getBoolean("editmarkup",false);
        cairsaldo=boyprefs.getBoolean("cairsaldo",false);
        Profiling2=boyprefs.getBoolean("Profiling2",false);
        transfer=boyprefs.getBoolean("transfer",false);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = ctx.getResources().getString(R.string.xyz)+"asktoin2/";
        int timestamp = (int) (new Date().getTime()/1000);
        String authToken = ctx.getResources().getString(R.string.key)+ String.valueOf(timestamp)+ ctx.getResources().getString(R.string.secret);
        String method = params[0];
        if (method.equalsIgnoreCase("login")) {
            username=params[1];
            password=params[2];


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
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
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
       // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
        boyprefs.edit().remove("datauser").apply();
        try {
           if(result.contains("datauser")){
               boyprefs.edit().putString("datauser",result).apply();
               boyprefs.edit().putString("username",username)
                       .putString("password",password)
                       .apply();
               boyprefs.edit().putBoolean("savelogin",true).apply();
               jmlnotiftext=PojoMion.AmbilArray(result,"jmlnotiftext","ceknotifteks");
               jmlnotifimage=PojoMion.AmbilArray(result,"jmlnotifimage","ceknotifimage");
               notifimage= Integer.parseInt(jmlnotifimage.get(0));
               notiftext= Integer.parseInt(jmlnotiftext.get(0));
               boyprefs.edit().putInt("notifimage",notifimage).apply();
               boyprefs.edit().putInt("notiftext",notiftext).apply();
            //   dialog.dismiss();
               if(pdam){
                   Intent intent=new Intent(ctx, Pdam.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(Telkom){
                   Intent intent=new Intent(ctx, loketbayar.id.Telkom.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(prabayar){
                   Intent intent=new Intent(ctx, PulsaPrabayar.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(Topup){
                   Intent intent=new Intent(ctx, loketbayar.id.Topup.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(paskabayar){
                   Intent intent=new Intent(ctx, Paskabayar.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(bpjs){
                   Intent intent=new Intent(ctx, Bpjs.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }
               else
               if(Samsat){
                   Intent intent=new Intent(ctx, Samsat.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(transferkerek){
                   Intent intent=new Intent(ctx, transferkerek.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(kirimloket){
                   Intent intent=new Intent(ctx, kirimloket.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }
               else
               if(pln){
                   Intent intent=new Intent(ctx, Pln.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }else
               if(Angsuran) {
                   Intent intent = new Intent(ctx, Angsuran.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity) ctx).finish();
               }
               else
               if(MultiIdPayment) {
                   Intent intent = new Intent(ctx, MultiIdPayment.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity) ctx).finish();
               }
               else
               if(MultiIdPayment2) {
                   Intent intent = new Intent(ctx, MultiIdPayment2.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity) ctx).finish();
               }
               else
               if(Pbb) {
                   Intent intent = new Intent(ctx, Pbb.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity) ctx).finish();
               }
               else
               if(Tvkabel) {
                   Intent intent = new Intent(ctx, Tvkabel.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity) ctx).finish();
               }
               else
               if(Profiling2) {
                   Intent intent = new Intent(ctx, Profiling2.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity) ctx).finish();
               }
               else
               if(transfer) {
                   Intent intent = new Intent(ctx, Transfer.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   ctx.startActivity(intent);
                   ((Activity) ctx).finish();
               }
               else{
                   Intent intent=new Intent(ctx,Home.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   ctx.startActivity(intent);
                   ((Activity)ctx).finish();
               }

            }
           else{
               Toast.makeText(ctx,"User / Password Salah", Toast.LENGTH_LONG).show();
               Intent intent=new Intent(ctx, Loginpage.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               ctx.startActivity(intent);
               ((Activity)ctx).finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if(load!=null){
                load.dismiss();
            }
            Toast.makeText(ctx,"Gagal Login / Tidak Ada Sinyal", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ctx, Loginpage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
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
                while (jh <5) {
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


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }.start();

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
