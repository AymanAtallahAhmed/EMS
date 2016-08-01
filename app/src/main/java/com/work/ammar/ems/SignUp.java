package com.work.ammar.ems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static String base = "http://196.205.93.181:22355/api";
    private static String verify = "/user/register/check_mail.php";
    private static String register = "/user/register/import_user.php";
    RequestQueue requestQueue;
    Button verifyMail;
    EditText mailE;
    EditText passE;
    EditText nameE;
    EditText phoneE;
    RadioGroup typeR;
    String type;
    Button regB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        typeR = (RadioGroup) findViewById(R.id.typeR);
        requestQueue = Volley.newRequestQueue(this);
        passE = (EditText) findViewById(R.id.pass);
        nameE = (EditText) findViewById(R.id.name);
        phoneE = (EditText) findViewById(R.id.phone);
        mailE = (EditText) findViewById(R.id.mail);
        mailE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyMail.setBackgroundColor(getColor(getBaseContext(),R.color.noAction));
                verifyMail.setText("Verify");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        verifyMail = (Button) findViewById(R.id.verifyMail);
        verifyMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify(mailE.getText().toString());
            }
        });
        regB = (Button) findViewById(R.id.register);
        regB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = getType();
                register();
            }
        });
    }
    private void verify(String mail) {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, base + verify + "?email=" + mail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("no")) {
                            verifyMail.setBackgroundColor(getColor(getBaseContext(),R.color.verifiedColor));
                            verifyMail.setText("Verified");
                        }
                        if(response.contains("yes")) {
                            verifyMail.setBackgroundColor(getColor(getBaseContext(),R.color.wrongColor));
                            verifyMail.setText("used");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void register() {
        StringRequest stringRequest =new StringRequest(Request.Method.POST, base + register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("true")) {
                            Intent intent = new Intent(getBaseContext(), Main.class);
                            intent.putExtra("mail",mailE.getText().toString());
                            intent.putExtra("pass",passE.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                        if(response.contains("false")) {
                            Toast.makeText(getBaseContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_name",nameE.getText().toString());
                params.put("email",mailE.getText().toString());
                params.put("password",passE.getText().toString());
                params.put("type",type);
                params.put("photo","imgs/pic.png");
                params.put("phone_num",phoneE.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String getType() {
        String result="";
        int index = typeR.indexOfChild(typeR.findViewById(typeR.getCheckedRadioButtonId()));
        switch (index) {
            case 0:
                result = "Industrial";
                break;
            case 1:
                result = "Commercial";
                break;
            case 2:
                result = "Domestic";
                break;
        }
        return result;
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
