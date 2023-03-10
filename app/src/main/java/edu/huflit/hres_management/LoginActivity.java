package edu.huflit.hres_management;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.huflit.hres_management.API.APIService;
import edu.huflit.hres_management.API.model.LoginRequest;
import edu.huflit.hres_management.API.model.LoginResponse;
import edu.huflit.hres_management.Database.DBHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText lusername, lpassword , lrestaurantID;
    private CheckBox lcbRemember;
    private Button lbtnlogin;
    private TextView ltvSignup;
    private DBHelper DB;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if(!token.equals("")) {
            Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lrestaurantID = (EditText) findViewById(R.id.lrestaurantID);
        lusername = (EditText) findViewById(R.id.ltvSignup);
        lpassword =(EditText) findViewById(R.id.lpassword);
        lcbRemember = (CheckBox) findViewById(R.id.lcbRemember);
        lbtnlogin = (Button) findViewById(R.id.lbtnSignin);
        ltvSignup =(TextView) findViewById(R.id.ltvSignup);
        lbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantID = lrestaurantID.getText().toString();
                String userName = lusername.getText().toString();
                String password = lpassword.getText().toString();
                login(restaurantID,userName,password);
            }
        });

    }
    private void login (String restaurantID, String userName, String password) {
        LoginRequest loginRequest = new LoginRequest(restaurantID, userName, password);
        APIService.apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call,@NonNull Response<LoginResponse> response) {
                if(response.code() != 200) {
                    Toast.makeText(LoginActivity.this, "Vui l??ng ki???m tra l???i t??i kho???n", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginResponse loginResponse = response.body();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", loginResponse.getToken());
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(i);
                finish();
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponse> call,@NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "C?? l???i trong qu?? tr??nh ????ng nh???p vui l??ng th??? l???i sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}