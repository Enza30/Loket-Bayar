package loketbayar.id;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

class SimpanExcelTask extends AsyncTask<String, Void, String> {
   private Context ctx;
    private String results;
    SimpanExcelTask(Context ctx) {
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
        String login_url = ctx.getResources().getString(R.string.xyz)+"simpanexcel/";

        String method = params[0];
        if (method.equalsIgnoreCase("simpan")) {
            String datas=params[1];
            String username=params[2];
            String password=params[3];

            try {
                URL url = new URL(login_url);
                HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("datas", "UTF-8") + "=" + URLEncoder.encode(datas, "UTF-8")+ "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&" +
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
        Log.e("hasils",result);
       // Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
        try {
           if(result.trim().equalsIgnoreCase("sukses")){
               Dialog dialog=new Dialog(ctx);
               dialog.setContentView(R.layout.dialogok);
               dialog.setCancelable(false);
               Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

               TextView tvIf=dialog.findViewById(R.id.tvIf);
               TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
               Button btTidak=dialog.findViewById(R.id.btTidak);
               tvIf.setText("Saving Excel Success");
               tvTanya.setText("Success!");
               btTidak.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                       String username = boyprefs.getString("username", "");
                       String password = boyprefs.getString("password", "");
                       new MultyAwalTask2(ctx).execute("login",username,password);

                   }
               });
               dialog.show();
           }else{
               Dialog dialog=new Dialog(ctx);
               dialog.setContentView(R.layout.dialogok);
               dialog.setCancelable(false);
               Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

               TextView tvIf=dialog.findViewById(R.id.tvIf);
               TextView  tvTanya=dialog.findViewById(R.id.tvTanya);
               Button btTidak=dialog.findViewById(R.id.btTidak);
               tvIf.setText("Saving Excel FAILED");
               tvTanya.setText(result);
               btTidak.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();

                   }
               });
               dialog.show();
           }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx,"eror:"+result, Toast.LENGTH_LONG).show();

        }
    }
}
