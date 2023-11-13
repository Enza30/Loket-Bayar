package loketbayar.id;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import loketbayar.id.R;

import java.util.Timer;
import java.util.TimerTask;

public class JalanService extends Service {
    private NotificationManager notifManager;
    private static final String TAG = "Location";
    public static final long NOTIFY_INTERVAL = 10000; // 10 seconds
    private String username="",password="",kode_lokets="";
    private int DELAY = 1000;
    public static int saldoakhir=0,hitungan=0;
    private String saldoawal;
    private float saldoawl=0,saldoakhr=0;
    private Handler mHandler = new Handler();
    public static Timer punyatimertrack;
    private int z = 0;
    private String token;
    private SharedPreferences boyprefs;
    private final int Mycode=12;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        boyprefs=getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        if(intent!=null){
            if (intent.getExtras() != null) {
                username = intent.getExtras().getString("username", "");
                password = intent.getExtras().getString("password", "");
                kode_lokets = intent.getExtras().getString("kode_loket", "");
                Log.e("username",username);
                Log.e("password",password);
                Log.e("kode_lokets",kode_lokets);
            }
        }


        return START_STICKY;
    }


    /*  @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = this;
        super.onStartCommand(intent, flags, startId);
            return START_STICKY;


    }*/

    @Override
    public void onCreate() {
      /*  if (!NotificationManagerCompat.getEnabledListenerPackages (getApplicationContext()).contains(getApplicationContext().getPackageName())) {
            Toast.makeText(getApplicationContext(), "Please Enable Notification Access", Toast.LENGTH_LONG).show();
            //service is not enabled try to enabled by calling...
          //  getApplicationContext().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {*/
        if(NotificationsUtils.isNotificationEnabled(getApplicationContext())){
            Notification notification = createNotification("Mudahbayar Service started",
                    "Mudahbayar Service started","Mudahbayar Service started","1");
            startForeground(999999999, notification);
            //notifManager.deleteNotificationChannel("1");
            if (punyatimertrack != null) {
                punyatimertrack.cancel();
                punyatimertrack = new Timer();
            } else {
                punyatimertrack = new Timer();
            }
            punyatimertrack.scheduleAtFixedRate(new trackeruntuktimertrack(), 0, NOTIFY_INTERVAL);
        }else{
            Toast.makeText(getApplicationContext(), "Please Enable Notification Access", Toast.LENGTH_LONG).show();

        }

        /*}
         */

    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public Notification createNotification(String ticker, String message, String subtitle, String id) {
        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "Mudah Bayar";

        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel mChannel = null;
            if (notifManager != null) {
                mChannel = notifManager.getNotificationChannel(id);
            }
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);
            builder.setContentTitle(message)  // required
                    //  .setSmallIcon(R.drawable.logo)
                    //  .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                    .setContentText(ticker)  // required
                    .setDefaults(Notification.VISIBILITY_SECRET)
                    .setAutoCancel(false)
                    .setOngoing(false)
                    //   .setSubText(subtitle)
                    //  .setTicker(ticker)
                    .setPriority(importance);
            //.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(message)
                    /* .setSmallIcon(R.drawable.logo)
                     .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))*/
                    .setContentText(ticker)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(false)
                    .setOngoing(false)
                    /*. .setTicker(ticker)
                     .setSubText(subtitle)
                     .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})*/
                    .setPriority(Notification.PRIORITY_MIN);
        }

        /*notifManager.notify(NOTIFY_ID, notification);*/
        return builder.build();
    }
    @Override
    public void onDestroy() {
        Log.e("aplikasi", "onDestroy");
        saldoakhir=0;
        super.onDestroy();

        if (punyatimertrack != null) {
            punyatimertrack.cancel();
        }


    }
    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        assert alarmService != null;
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +1000, restartServicePI);

    }

    private class trackeruntuktimertrack extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    boolean sve=boyprefs.getBoolean("savelogin",false);
                    if(sve){
                        if(isNetworkAvailable(getApplicationContext())){
                            saldoakhir=boyprefs.getInt("saldoakhir",0);
                            if (username.isEmpty()) {
                                username = boyprefs.getString("username", "");
                                password = boyprefs.getString("password", "");
                                kode_lokets = boyprefs.getString("kode_loket", "");
                                Log.e("username", username);
                                Log.e("password", password);
                                Log.e("kode_lokets", kode_lokets);
                                new AmbilSaldoTaskservice(getApplicationContext())
                                        .execute("ambil", username, password, kode_lokets, String.valueOf(saldoakhir));
                            } else {
                                new AmbilSaldoTaskservice(getApplicationContext())
                                        .execute("ambil", username, password, kode_lokets, String.valueOf(saldoakhir));
                            }

                        }else{
                            Log.e("internet mati","yes");
                        }

                    }else{
                        saldoakhir=0;
                        saldoawal="";
                        saldoakhr=0;
                        saldoawl=0;
                    }


                   /*Ambilmulti(getApplicationContext().getResources().getString(R.string.xyz)+"ambilsaldo/",
                           kode_lokets,username,password);*/
                }

            });
        }
    }
   /* void Ambilmulti(String POST_ORDER,String kode_lokets,String usernames,String passwords) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, ServerResponse -> {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    Log.e("serverresp",ServerResponse);
                    if(ServerResponse.contains("datasaldo")){
                        Log.e("datasaldo",ServerResponse);
                    }else{
                        Log.e("serverresp",ServerResponse);
                    }
                }
            }, 2000);

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.e("errorambilmulti",volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodeloket", kode_lokets);
                params.put("username", usernames);
                params.put("password", passwords);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

*/
}