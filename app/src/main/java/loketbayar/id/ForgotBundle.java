package loketbayar.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import loketbayar.id.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class ForgotBundle {
   private Context ctx;
   private SharedPreferences boyprefs;
   private Dialog load;
    ForgotBundle(Context ctx, Dialog load) {
        this.ctx = ctx;
        this.load = load;
    }


    void requestAction(String username,String email,String POST_ORDER) {
        boyprefs =ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                load.dismiss();
                if(ServerResponse.trim().equalsIgnoreCase("sukses")){

                    ctx.startActivity(new Intent(ctx, GantiPass.class));
                    ((Activity)ctx).finish();
                }else{
                    Log.e("error",ServerResponse);
                    Toast.makeText(ctx, ServerResponse, Toast.LENGTH_LONG).show();
                }


            }, 2000);

        },
                volleyError -> {
                    load.dismiss();
                    Toast.makeText(ctx, volleyError.toString(), Toast.LENGTH_LONG).show();

                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String key = "Zb2M#E0T4W$*";
                String secret = "asjkrue*$djasfl134213";
                int timestamp = (int) (new Date().getTime()/1000);
                String authToken = key+ String.valueOf(timestamp)+ secret;
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", key);
                headers.put("timestamp", String.valueOf(timestamp));
                headers.put("Authentication", getHash(secret, authToken));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ctx.getString(R.string.usernbame), username);
                params.put(ctx.getString(R.string.email), email);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
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
