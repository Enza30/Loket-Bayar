package loketbayar.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class AmbilProfilRequest3 {
    private ProgressDialog progressDialog;
   private Context ctx;
   private SharedPreferences boyprefs;
    AmbilProfilRequest3(Context ctx) {
        this.ctx = ctx;
    }
    private ProgressDialog dialog;
    private Dialog load, dialogload;

    void requestAction(String usernames, String passwords,String kodeloket,String namaloket,String POST_ORDER) {

        dialogload = new Dialog(ctx);
        dialogload.setContentView(R.layout.dialogload2);
        dialogload.setCancelable(false);
        Objects.requireNonNull(dialogload.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ivX = dialogload.findViewById(R.id.ivX);
        dialogload.show();
        boyprefs =ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(final String ServerResponse) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        dialogload.dismiss();
                        if(ServerResponse.contains("dataprofile")){
                            boyprefs.edit().putString("dataprofile",ServerResponse).apply();
                            ctx.startActivity(new Intent(ctx,Profiling.class));
                            ((Activity)ctx).finish();
                        }else{
                            Toast.makeText(ctx,ServerResponse, Toast.LENGTH_LONG).show();
                        }
                    }
                }, 10);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(ctx, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", usernames);
                params.put("password", passwords);
                params.put("kode_loket", kodeloket);
                params.put("nama_loket", namaloket);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

}
