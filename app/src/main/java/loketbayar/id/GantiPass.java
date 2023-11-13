package loketbayar.id;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import loketbayar.id.R;

import java.util.Objects;

public class GantiPass extends AppCompatActivity {
    private EditText etPassbaru,etKonfirm;
    private Button btSubmit;
    private String username,email,pasbaru,konfirmpas;
    private SharedPreferences boyprefs;
    private ImageView ivBack;
    private Dialog load;
    private ImageView ivX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs =getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_ganti_pass);
        username=boyprefs.getString("username","");
        email=boyprefs.getString("email","");
        etPassbaru=findViewById(R.id.etPassbaru);
        etKonfirm=findViewById(R.id.etKonfirm);
        btSubmit=findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasbaru=etPassbaru.getText().toString();
                konfirmpas=etKonfirm.getText().toString();
                if(pasbaru.isEmpty()){
                    Snackbar.make(v,"Masukkan Password Baru Anda", Snackbar.LENGTH_SHORT).show();
                }else if(konfirmpas.isEmpty()){
                    Snackbar.make(v,"Masukkan Konfirmasi Password", Snackbar.LENGTH_SHORT).show();
                }else{
                    load=new Dialog(GantiPass.this);
                    load.setContentView(R.layout.dialogload2);
                    load.setCancelable(false);
                    ivX=load.findViewById(R.id.ivX);
                    Objects.requireNonNull(load.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                    load.show();
                    new UpdatepasBundle(GantiPass.this,load).requestAction(username,email,
                            pasbaru,konfirmpas,
                            getResources().getString(R.string.xyz)+getString(R.string.updatepas));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GantiPass.this, Loginpage.class));
        finish();
    }
}