package loketbayar.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import loketbayar.id.R;

import java.util.HashMap;
import java.util.Map;

class UpdatepasBundle {
   private Context ctx;
   private SharedPreferences boyprefs;
   private Dialog load;
    UpdatepasBundle(Context ctx, Dialog load) {
        this.ctx = ctx;
        this.load = load;
    }


    void requestAction(String usernames, String email,String passbaru,String konfirmpas,String POST_ORDER) {
        boyprefs =ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                load.dismiss();
                if(ServerResponse.trim().equalsIgnoreCase("sukses")){
                    Toast.makeText(ctx, "Ganti Password Sukses Silahkan Login Kembali", Toast.LENGTH_LONG).show();
                    String token=boyprefs.getString("token","");
                    boyprefs.edit().clear().apply();
                    boyprefs.edit().putString("token",token).apply();
                    ctx.startActivity(new Intent(ctx, MainActivity.class));
                    ((Activity)ctx).finish();
                }else{
                    Log.e("error",ServerResponse);
                    Toast.makeText(ctx, R.string.overload, Toast.LENGTH_LONG).show();
                }


            }, 2000);

        },
                volleyError -> {
                    load.dismiss();
                    Toast.makeText(ctx, volleyError.toString(), Toast.LENGTH_LONG).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ctx.getString(R.string.usernbame), usernames);
                params.put(ctx.getString(R.string.email), email);
                params.put(ctx.getString(R.string.passbaru), passbaru);
                params.put(ctx.getString(R.string.konfirmpas), konfirmpas);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

}
