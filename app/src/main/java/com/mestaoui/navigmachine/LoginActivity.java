package com.mestaoui.navigmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.mestaoui.navigmachine.beans.User;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputLayout username, motdepasse;
    private Button connect;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameEdt);
        motdepasse = findViewById(R.id.passwordEdt);
        connect = findViewById(R.id.connectBtn);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getEditText().getText().toString().isEmpty() || motdepasse.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs vides !", Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User(username.getEditText().getText().toString(), motdepasse.getEditText().getText().toString());
                    String uri = String.format("http://10.0.2.2:8090/login?username=%1$s&password=%2$s",
                            user.getUsername(),
                            user.getPassword());

                    StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error",error.toString());
                            Toast.makeText(LoginActivity.this, "Nom d'utilisateur ou mot de passe invalides !", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(request);
                }
            }
        });
    }
}