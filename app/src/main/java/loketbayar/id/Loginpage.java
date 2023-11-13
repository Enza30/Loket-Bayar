package loketbayar.id;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import loketbayar.id.R;

public class Loginpage extends AppCompatActivity {
    private Button btLogin;
    private ImageView ivWhats;
    private EditText etUsername,etPassword;
    private TextView etForgot;
    private TextView etForgot4;
    private SharedPreferences boyprefs;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        etForgot=findViewById(R.id.etForgot);
        etForgot4=findViewById(R.id.etForgot4);
        btLogin=findViewById(R.id.btLogin);
        ivWhats=findViewById(R.id.ivWhats);
//        ivWhats.setVisibility(View.GONE);

        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
  //      new LoginTask(Loginpage.this).execute("login","cobaaja","cobaaja");
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                    username=etUsername.getText().toString();
                    password=etPassword.getText().toString();
                    if(username.isEmpty()){
                        Snackbar.make(v,"Masukan Username", Snackbar.LENGTH_SHORT).show();
                    } else if(password.isEmpty()){
                        Snackbar.make(v,"Masukan Password", Snackbar.LENGTH_SHORT).show();
                    }else{
                        new LoginTask(Loginpage.this).execute("login",username,password);
                    }
                }else{
                    Toast.makeText(Loginpage.this, "Gagal Login / Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
                }


            }
        });
        ivWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaInternet()){
                String phoneNumberWithCountryCode = "+6285848191444";
                String message = "Halo MudahBayar mau tanya nih..";

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                        phoneNumberWithCountryCode, message))));
            }
            else{
                Toast.makeText(Loginpage.this, "Gagal Login / Tidak Ada Sinyal", Toast.LENGTH_SHORT).show();
            }}
        });
        etForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginpage.this,ForgotPassword.class));
                finish();
            }

        });
        etForgot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginpage.this,pendaftaran.class));
                finish();
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
}
