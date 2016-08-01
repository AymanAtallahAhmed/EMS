package com.work.ammar.ems;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView userNameT;
    ImageView ppI;
    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        userNameT = (TextView) rootView.findViewById(R.id.userName);
        ppI = (ImageView) rootView.findViewById(R.id.pp);
        Realm realm = Realm.getInstance(getContext());
        User user = realm.allObjects(User.class).first();
        userNameT.setText(user.getUserName());
        // TODO uncomment then delete next line
        //Picasso.with(getContext()).load(user.getPhoto()).into(ppI);
        Picasso.with(getContext()).load("http://zerosnones.net/images/0__RFqJmTHG-WRSIiGkZXVsf2EGvVRER_GkUMM07-Q73U4aVAu6IQ4g5cUrxl.jpg").into(ppI);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                Realm realm = Realm.getInstance(getContext());
                realm.beginTransaction();
                realm.clear(User.class);
                realm.commitTransaction();
                Main.fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                MainFragment mainFragment = new MainFragment();
                Main.fragmentTransaction.replace(R.id.fragment, mainFragment);
                Main.fragmentTransaction.commit();
                break;
            case R.id.action_billing:
                Main.fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                BillingFragment billingFragment = new BillingFragment();
                Main.fragmentTransaction.replace(R.id.fragment, billingFragment);
                Main.fragmentTransaction.commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
