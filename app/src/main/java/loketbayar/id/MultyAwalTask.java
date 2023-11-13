package loketbayar.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import loketbayar.id.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;


class MultyAwalTask extends AsyncTask<String, Void, String> {
   private Context ctx;
    private String username,password,kodeloket;
    MultyAwalTask(Context ctx) {
        this.ctx = ctx;
    }
    private Dialog dialog;
    private Dialog load;
    private String base,login_url;
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
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = ctx.getResources().getString(R.string.xyz)+"asktoin5sec/";
        int timestamp = (int) (new Date().getTime()/1000);
        String authToken = ctx.getResources().getString(R.string.key)+ String.valueOf(timestamp)+ ctx.getResources().getString(R.string.secret);
        String method = params[0];
        if (method.equalsIgnoreCase("login")) {
            username=params[1];
            password=params[2];
            kodeloket=params[3];



            try {
                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestProperty("apikey", ctx.getResources().getString(R.string.key));
                httpsURLConnection.setRequestProperty("timestamp", String.valueOf(timestamp));
                httpsURLConnection.setRequestProperty("Authentication", getHash(ctx.getResources().getString(R.string.secret), authToken));
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
                        + "&" +
                        URLEncoder.encode("kodeloket", "UTF-8") + "=" + URLEncoder.encode(kodeloket, "UTF-8")
                        ;
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
                String response = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();
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
        try {
           if(result.contains("datauser")){
               boyprefs.edit().putString("datakolektif",result).apply();
               dialog.dismiss();
               Intent intent=new Intent(ctx,MultiIdPayment.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               ctx.startActivity(intent);
               ((Activity)ctx).finish();

            }else{

               Dialog dialog=new Dialog(ctx);
               dialog.setContentView(R.layout.dialogok);
               dialog.setCancelable(false);
               dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
               TextView tvIf=dialog.findViewById(R.id.tvIf);
               TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
               Button btTidak=dialog.findViewById(R.id.btTidak);
               tvIf.setText("Tidak Ada Group");
               tvTanya.setText("Pesan");
               // Toast.makeText(ctx,"eror:"+result, Toast.LENGTH_LONG).show();
               btTidak.setText("Oke");
               btTidak.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                       //boyprefs.edit().remove("kodekolektif").apply();
                       String username=boyprefs.getString("username","");
                       String password=boyprefs.getString("password","");
                       new LoginTask(ctx).execute("login",username,password);
                   }
               });
               dialog.show();

            }

        } catch (Exception e) {
            Log.e("error","res:"+result);
           // Toast.makeText(ctx,"eror:"+result, Toast.LENGTH_LONG).show();

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
