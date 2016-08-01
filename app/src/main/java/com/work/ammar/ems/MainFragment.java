package com.work.ammar.ems;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private static String base = "http://196.205.93.181:22355/api";
    private static String login = "/user/login/get_user.php";
    TextView errorT;
    EditText mailE;
    EditText passE;
    Button logInBtn;
    String mail;
    String pass;
    RequestQueue requestQueue;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        requestQueue = Volley.newRequestQueue(getContext());
        errorT = (TextView) rootView.findViewById(R.id.errorText);
        mailE = (EditText) rootView.findViewById(R.id.mail);
        passE = (EditText) rootView.findViewById(R.id.pass);
        logInBtn = (Button) rootView.findViewById(R.id.logInBtn);
        if(mail != null) {
            mailE.setText(getArguments().getString("mail"));
        }
        if(pass != null) {
            passE.setText(getArguments().getString("pass"));
        }

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest =new StringRequest(Request.Method.POST, base + login,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.contains("\"status\":\"wrong email\"")) {
                                    errorT.setText("Mail not found. please register");
                                } else if(response.contains("\"status\":\"wrong_pass\"")) {
                                    errorT.setText("Incorrect Password");
                                } else if(response.contains("\"status\":\"true\"")) {
                                    Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                                        @Override
                                        public boolean shouldSkipField(FieldAttributes f) {
                                            return f.getDeclaringClass().equals(RealmObject.class);
                                        }

                                        @Override
                                        public boolean shouldSkipClass(Class<?> clazz) {
                                            return false;
                                        }
                                    }).create();
                                    User user = gson.fromJson(response, User.class);
                                    openDetailsFragment(user);
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
                        params.put("email",mailE.getText().toString());
                        params.put("password",passE.getText().toString());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

        return rootView;
    }

    void openDetailsFragment(User user) {
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
        HomeFragment homeFragment = new HomeFragment();
        Main.fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Main.fragmentTransaction.replace(R.id.fragment,homeFragment);
        Main.fragmentTransaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_signup:
                startActivity(new Intent(getContext(), SignUp.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
