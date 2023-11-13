package loketbayar.id;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import loketbayar.id.R;

import java.util.Objects;

public class BootDeviceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       // context.startActivity(new Intent(context,MainActivity.class));
        /*((Activity)context).finish();*/
        SharedPreferences boyprefs = context.getSharedPreferences(context.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        String username = boyprefs.getString("username", "");
        String password = boyprefs.getString("password", "");
        String kode_loket = boyprefs.getString("kode_loket", "");
        if ("android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, JalanService.class);
            pushIntent.putExtra("username", username);
            pushIntent.putExtra("password", password);
            pushIntent.putExtra("kode_loket", kode_loket);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(pushIntent);
            }else{
                context.startService(pushIntent);
            }

            Log.e("boot_broadcast_poc", "starting service...");

        }else if(Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
            Intent pushIntent = new Intent(context, JalanService.class);
            pushIntent.putExtra("username", username);
            pushIntent.putExtra("password", password);
            pushIntent.putExtra("kode_loket", kode_loket);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(pushIntent);
            }else{
                context.startService(pushIntent);
            }
            Log.e("boot_broadcast_poc", "starting service...");
        }
        else if(intent.getAction().equalsIgnoreCase("android.intent.action.LOCKED_BOOT_COMPLETED")){
            Intent pushIntent = new Intent(context, JalanService.class);
            pushIntent.putExtra("username", username);
            pushIntent.putExtra("password", password);
            pushIntent.putExtra("kode_loket", kode_loket);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(pushIntent);
            }else{
                context.startService(pushIntent);
            }
            Log.e("boot_broadcast_poc", "starting service...");
        }
        else if(intent.getAction().equalsIgnoreCase("android.intent.action.REBOOT")){
            Intent pushIntent = new Intent(context, JalanService.class);
            pushIntent.putExtra("username", username);
            pushIntent.putExtra("password", password);
            pushIntent.putExtra("kode_loket", kode_loket);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(pushIntent);
            }else{
                context.startService(pushIntent);
            }
            Log.e("boot_broadcast_poc", "starting service...");
        }
    }
}