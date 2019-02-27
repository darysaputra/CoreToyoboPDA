package core.trst.com.coretoyobopda;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;

import core.trst.com.coretoyobopda.util.FileHelper;
import core.trst.com.coretoyobopda.util.JsonHelper;
import core.trst.com.coretoyobopda.util.PreferenceHelper;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {
    //Declare Helper
    JsonHelper jsonHelp=new JsonHelper();
    FileHelper fileHelp=new FileHelper();
    PreferenceHelper prefHelp=new PreferenceHelper();
    //Declare Helper

    // UI references.
    private AutoCompleteTextView mUsername;
    private EditText mPasswordView;
    private Button btnLogin;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declare Get Komponen
        mUsername = findViewById(R.id.Username);
        mPasswordView = findViewById(R.id.password);
        mProgressView = findViewById(R.id.login_progress);
        btnLogin = findViewById(R.id.email_sign_in_button);
        //Declare Get Komponen


        //Check Permission And Enable Permission Write Storage
        permissionHandler(this);
        //Check Permission And Enable Permission Write Storage

        try {
            String otorisasi="" +
                    "[{\"username\":\"darysaputra\", \"password\":\"arema310394\"}," +
                    "{\"username\":\"viras\", \"password\":\"arema1987\"}," +
                    "{\"username\":\"aziz\", \"password\":\"arema87\"}" +
                    "]";

            fileHelp.writeFile(otorisasi,"/storage/emulated/0/","CorePlastic","CorePlasticConfig.txt",getApplicationContext());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getFileConfig=fileHelp.readFile("/storage/emulated/0/","CorePlastic","CorePlasticConfig.txt");

                SparseArray sparseArray=validateLogin(getFileConfig,mUsername.getText().toString(),mPasswordView.getText().toString());
                if(sparseArray.get(1).equals("accepted")){
                    //Jika Berhasil Login
                    Toast.makeText(LoginActivity.this,"Login Berhasil",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, mUsername.getText().toString());
                    startActivity(intent);
                }else{
                    //Jika Gagal Login
                    final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LoginActivity.this);
                    dlgAlert.setMessage(sparseArray.get(2).toString());
                    dlgAlert.setTitle("Pemberitahuan");
                    dlgAlert.setCancelable(true);
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dlgAlert.create().show();
                }

            }
        });
    }

    //Fungsi Validasi Login
    private SparseArray validateLogin(String paramListUser,String paramUsername,String paramPassword){
        SparseArray<String> sparseArray = new SparseArray<>();
        try {
            JSONArray jArray = new JSONArray(paramListUser);

            String status="not accepted";
            String keterangan="";
            for(int i=0;i<jArray.length();i++)
            {
                String username=jsonHelp.getStringFromObject(jsonHelp.getObjectFromArray(jArray,i),"username");
                String password=jsonHelp.getStringFromObject(jsonHelp.getObjectFromArray(jArray,i),"password");
                if(username.equals(paramUsername)){
                    if(password.equals(paramPassword)){
                        status="accepted";
                        keterangan="Berhasil";
                    }else{
                        keterangan="Password Salah";
                    }
                    break;
                }else{
                    keterangan="Username Tidak Terdaftar";
                }
            }
            sparseArray.put(1,status);
            sparseArray.put(2,keterangan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sparseArray;
    }
    //Fungsi Validasi Login

    private void permissionHandler(Activity paramActivity){
        if (ContextCompat.checkSelfPermission(paramActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(paramActivity,Manifest.permission.NFC)!= PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(paramActivity,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(paramActivity ,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.NFC}, 1);
            }
        }
    }

    //
}

