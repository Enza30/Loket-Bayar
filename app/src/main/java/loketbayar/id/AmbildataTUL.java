package loketbayar.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

class AmbildataTUL extends AsyncTask<String, Void, String> {
   private Context ctx;
    private String results;
    AmbildataTUL(Context ctx) {
        this.ctx = ctx;
    }
    private Dialog dialog;
    private Dialog load;
    private String base,login_url;
    ArrayList<String> kode_produk_biller=new ArrayList<>();
    ArrayList<String> fee_biller=new ArrayList<>();
    ArrayList<String> fee_app=new ArrayList<>();
    ArrayList<String> fee_ca=new ArrayList<>();
    ArrayList<String> harga_biller=new ArrayList<>();
    ArrayList<String> admin_biller=new ArrayList<>();
    ArrayList<String> fee_sub_ca=new ArrayList<>();
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
        String login_url = ctx.getResources().getString(R.string.xyz)+"ambildatatul/";

        String method = params[0];
        if (method.equalsIgnoreCase("ambil")) {
            String username=params[1];
            String password=params[2];
            String kode_loket=params[3];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&" +
                        URLEncoder.encode("kode_loket", "UTF-8") + "=" + URLEncoder.encode(kode_loket, "UTF-8");
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
        Log.e("datareport",result);

        try {
            if(result.contains("datareport")){
                boyprefs.edit()
                        .putString("datareport",result)

                        .apply();

                runThread();

            }else{
               Dialog dialog=new Dialog(ctx);
               dialog.setContentView(R.layout.dialogok);
               dialog.setCancelable(false);
               dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

               TextView tvIf=dialog.findViewById(R.id.tvIf);
               TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
               Button btTidak=dialog.findViewById(R.id.btTidak);
               tvIf.setText("Produk Tidak Ada Silahkan Hub Admin");
               tvTanya.setText("Gagal");
               btTidak.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                       boyprefs.edit().remove("datareport").apply();
                       ctx.startActivity(new Intent(ctx,Home.class));
                       ((Activity)ctx).finish();

                   }
               });
               dialog.show();

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
                    String dari=boyprefs.getString("dari","");

                        Intent intent=new Intent(ctx, Pln2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        ctx.startActivity(intent);
                        ((Activity)ctx).finish();


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }.start();

    }
}
