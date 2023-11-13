package loketbayar.id;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import loketbayar.id.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

class AmbilSaldoTaskservice extends AsyncTask<String, Void, String> {
    private Context ctx;
    private String results,username,password,kode_loket;
    ArrayList<String> saldonya=new ArrayList<>();
    private String saldoawal;
    private int saldoakhir=0,hitungan=0;
    private NotificationManager notifManager;
    AmbilSaldoTaskservice(Context ctx) {
        this.ctx = ctx;
    }
    private String base,login_url;

    ArrayList<String> saldo=new ArrayList<>();
    ArrayList<String> keterangan=new ArrayList<>();
    ArrayList<String> jmlnotiftext=new ArrayList<>();
    ArrayList<String> text=new ArrayList<>();
    ArrayList<String> judul=new ArrayList<>();
    ArrayList<String> foto=new ArrayList<>();
    ArrayList<String> textimage=new ArrayList<>();
    ArrayList<String> jmlnotifimage=new ArrayList<>();
    private SharedPreferences boyprefs;
    private int id=1;
    private int notiftext=0,notiftextawal=0;
    private int notifimage=0,notifimageawal=0;
    @Override
    protected void onPreExecute() {
        boyprefs =ctx.getSharedPreferences(ctx.getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = ctx.getResources().getString(R.string.xyz)+"ambilnotif/";
        int timestamp = (int) (new Date().getTime()/1000);
        String authToken = ctx.getResources().getString(R.string.key)+ String.valueOf(timestamp)+ ctx.getResources().getString(R.string.secret);

        String method = params[0];
        if (method.equalsIgnoreCase("ambil")) {
            username=params[1];
            password=params[2];
            kode_loket=params[3];
            saldoakhir= boyprefs.getInt("saldoakhir",0);
            Log.e("usernameditask",":"+username);
            try {
                URL url = new URL(login_url);
                HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String result) {

        try {
            id=boyprefs.getInt("id",0);
            results=result;
            Log.e("res:","hasil:"+result);
            if(result.contains("datasaldo")){

                saldonya=PojoMion.AmbilArray(results,"saldo","datasaldo");
                saldoawal=saldonya.get(0);
                keterangan=PojoMion.AmbilArray(results,"keterangan","datamutasi");
                jmlnotiftext=PojoMion.AmbilArray(results,"jmlnotiftext","ceknotifteks");
                jmlnotifimage=PojoMion.AmbilArray(results,"jmlnotifimage","ceknotifimage");
                textimage=PojoMion.AmbilArray(results,"judul","datanotifimage");
                foto=PojoMion.AmbilArray(results,"gambar","datanotifimage");
                text=PojoMion.AmbilArray(results,"teks","datanotiftext");
                judul=PojoMion.AmbilArray(results,"judul","datanotiftext");

                int sald= Integer.parseInt(saldoawal);
                notiftext= Integer.parseInt(jmlnotiftext.get(0));
                notiftextawal=boyprefs.getInt("notiftext",0);


                notifimage= Integer.parseInt(jmlnotifimage.get(0));
                notifimageawal=boyprefs.getInt("notifimage",0);
                Log.e("notifimage", String.valueOf(notifimage));
                if(notifimage>notifimageawal){
                    new sendNotification(ctx)
                            .execute(textimage.get(0), foto.get(0));
                    notifimageawal=notifimage;
                    boyprefs.edit().putInt("notifimage",notifimage).apply();

                }
                else{

                    boyprefs.edit().putInt("notifimage",notifimage).apply();

                }
                Log.e("notiftext", String.valueOf(notiftext));
                Log.e("notiftextawal", String.valueOf(notiftextawal));

                if(notiftext>notiftextawal){
                    id++;
                    boyprefs.edit().putInt("id",id).apply();
                    createNotificationtext("",
                            text.get(0),
                            text.get(0),String.valueOf(id));
                    notiftextawal=notiftext;
                    boyprefs.edit().putInt("notiftext",notiftextawal).apply();

                }
                else{

                    boyprefs.edit().putInt("notiftext",notiftext).apply();

                }

                if(saldoakhir!=0){
                    if(sald>saldoakhir){
                        id++;
                        boyprefs.edit().putInt("id",id).apply();
                        float a= Float.parseFloat(saldoawal);
                        float b=saldoakhir;
                        float saldotambah=a-b;
                        DecimalFormat decimalFormat=new DecimalFormat("###,###,###.##");
                        Log.e("salpls Refresh","Rp."+decimalFormat.format(saldotambah));
                        createNotification("Topup Rp."+decimalFormat.format(saldotambah)+".\n",
                                keterangan.get(0),
                                "Rp."+decimalFormat.format(saldotambah),String.valueOf(id));


                    }

                }

                JalanService.saldoakhir=sald;
                boyprefs.edit().putInt("saldoakhir",sald).apply();
                saldoakhir=sald;
                Log.e("saldoakhir", String.valueOf(saldoakhir));
                id++;
                boyprefs.edit().putInt("id",id).apply();
            }
            else{
                Log.e("gagal","gagal");
            }


        } catch (Exception e) {
            e.printStackTrace();
            new AmbilSaldoTaskservice(ctx).execute("ambil",username,password,kode_loket,String.valueOf(saldoakhir));
        }
    }
    public void createNotification(String ticker, String message, String subtitle, String id) {
        final int NOTIFY_ID = 1002;
        // There are hardcoding only for show it's just strings
        String name = "Mudah Bayar";

        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
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
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(Html
                    .fromHtml(message));
            builder = new NotificationCompat.Builder(ctx, id);

            intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
            Drawable drawable=ctx.getApplicationInfo().loadIcon(ctx.getPackageManager());
            builder           // required
                    .setSmallIcon(R.drawable.logonotif)
                    .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logonotif))
                    // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(ticker+message)))
                    .setContentText(ticker+message)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {

            builder = new NotificationCompat.Builder(ctx);

            intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);

            builder
                    .setSmallIcon(R.drawable.logonotif)
                    .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logonotif))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(ticker+message)))
                    .setContentText(ticker+message)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }


        Notification notification = builder.build();
        notifManager.notify(Integer.parseInt(id), notification);
    }
    public void createNotificationtext(String ticker, String message, String title, String id) {
        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "Mudah Bayar";

        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
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
            RemoteViews notificationLayout = new RemoteViews(ctx.getPackageName(), R.layout.notification_small);
            RemoteViews notificationLayoutExpanded = new RemoteViews(ctx.getPackageName(), R.layout.notification_large);
            notificationLayout.setTextViewText(R.id.notification_title,Html.fromHtml( title));
            notificationLayoutExpanded.setTextViewText(R.id.notification_title, Html.fromHtml( message));
            // Apply the layouts to the notification

            builder = new NotificationCompat.Builder(ctx, id);

            intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
            Drawable drawable=ctx.getApplicationInfo().loadIcon(ctx.getPackageManager());
            builder         // required
                    .setSmallIcon(R.drawable.logonotif)
                    .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logonotif))
                    // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(false)
                    .setOngoing(false)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setCustomBigContentView(notificationLayoutExpanded)
                    .setContent(notificationLayout)
                    .setContentText(Html.fromHtml(title+message));
        } else {

            builder = new NotificationCompat.Builder(ctx);

            intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
            RemoteViews notificationLayout = new RemoteViews(ctx.getPackageName(), R.layout.notification_small);
            RemoteViews notificationLayoutExpanded = new RemoteViews(ctx.getPackageName(), R.layout.notification_large);
            notificationLayout.setTextViewText(R.id.notification_title,Html.fromHtml( title+message));
            notificationLayoutExpanded.setTextViewText(R.id.notification_title, Html.fromHtml( message));
            builder
                    .setSmallIcon(R.drawable.logonotif)
                    .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logonotif))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(false)
                    .setOngoing(false)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setCustomBigContentView(notificationLayoutExpanded)
                    .setContent(notificationLayout)
                    .setContentText(Html.fromHtml(title+message));
        }

        Notification notification = builder.build();
        notifManager.notify(Integer.parseInt(id), notification);
    }
    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message;

        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            message = params[0];
            try {

                URL url = new URL(params[1]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {
                NotificationManager notificationManager = (NotificationManager) ctx
                        .getSystemService(Context.NOTIFICATION_SERVICE);

              /*  Intent intent = new Intent(ctx, NotificationsActivity.class);
                intent.putExtra("isFromBadge", false);
*/

                id++;
                boyprefs.edit().putInt("id",id).apply();
                createNotificationimage(result,message,message,String.valueOf(id));




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void createNotificationimage(Bitmap foto, String message, String title, String id) {
        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "Mudah Bayar";

        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
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
            RemoteViews notificationLayout = new RemoteViews(ctx.getPackageName(), R.layout.notification_small);
            RemoteViews notificationLayoutExpanded = new RemoteViews(ctx.getPackageName(), R.layout.notification_large);
            notificationLayout.setTextViewText(R.id.notification_title,Html.fromHtml( title));
            notificationLayoutExpanded.setTextViewText(R.id.notification_title, Html.fromHtml( message));
            // Apply the layouts to the notification

            builder = new NotificationCompat.Builder(ctx, id);

            intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
            Drawable drawable=ctx.getApplicationInfo().loadIcon(ctx.getPackageManager());
            builder         // required
                    .setContentTitle(
                            ctx.getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logonotif)
                    .setLargeIcon(foto)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(foto)
                            .bigLargeIcon(null))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(ctx);
            intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
            RemoteViews notificationLayout = new RemoteViews(ctx.getPackageName(), R.layout.notification_small);
            RemoteViews notificationLayoutExpanded = new RemoteViews(ctx.getPackageName(), R.layout.notification_large);
            notificationLayout.setTextViewText(R.id.notification_title,Html.fromHtml( title));
            notificationLayoutExpanded.setTextViewText(R.id.notification_title, Html.fromHtml( message));
            builder         // required
                    .setContentTitle(
                            ctx.getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logonotif)
                    .setLargeIcon(foto)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(foto)
                            .bigLargeIcon(null))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(Integer.parseInt(id), notification);
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