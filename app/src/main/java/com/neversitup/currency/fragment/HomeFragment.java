package com.neversitup.currency.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neversitup.currency.R;
import com.neversitup.currency.activity.NoInternetActivity;
import com.neversitup.currency.adapter.CurrencyAdapter;
import com.neversitup.currency.model.CurrencyData;
import com.neversitup.currency.model.CurrencyRate;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class HomeFragment extends Fragment {

    String apiUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
    ListView exchangeListView;

    String[] currency = {"USD","GBP","EUR"};
    List<CurrencyData> currencyDataList;
    private CurrencyRate currencyRate;

    private DatabaseReference historyDatabase;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodManager imm = (InputMethodManager) reqContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        historyDatabase = FirebaseDatabase.getInstance().getReference("history");
        sharedPreferences = reqContext().getSharedPreferences("CURRENCY",Context.MODE_PRIVATE);

        exchangeListView = view.findViewById(R.id.exchange_list_view);
        currencyDataList = new ArrayList<>();
        currencyRate = new CurrencyRate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager internetManager = (ConnectivityManager) reqContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = internetManager.getActiveNetworkInfo();
                if (info != null){
                    new GetExchangeInfo().execute();
                }else {
                    Intent intent = new Intent(getContext(),NoInternetActivity.class);
                    startActivity(intent);
                }
                new  Handler().postDelayed(this,60000);
            }
        },0);
    }

    private class GetExchangeInfo extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder current = new StringBuilder();
            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();

                    while (data != -1){
                        current.append((char) data);
                        data = reader.read();
                    }
                    return current.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return current.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String list = sharedPreferences.getString("DATALIST","");
                JSONObject jsonObject = new JSONObject(s);

                //Currency Update Time
                JSONObject timeObj = jsonObject.getJSONObject("time");
                String time = timeObj.getString("updated");

                //Currency Exchange Info
                JSONObject bpiObject = jsonObject.getJSONObject("bpi");
                currencyDataList.clear();

                for (String value : currency) {
                    JSONObject currencyObj = bpiObject.getJSONObject(value);
                    CurrencyData currencyData = new CurrencyData();
                    currencyData.setCode(currencyObj.getString("code"));
                    currencyData.setRate(currencyObj.getDouble("rate_float"));
                    currencyData.setIcon(getCurrencySymbol(currencyObj.getString("code")));
                    currencyData.setTime(time);
                    currencyDataList.add(currencyData);
                }
                String data = String.valueOf(currencyDataList.get(0).getRate() + currencyDataList.get(1).getRate()+currencyDataList.get(2).getRate());
                currencyRate.setDollar(currencyDataList.get(0).getRate());
                currencyRate.setPound(currencyDataList.get(1).getRate());
                currencyRate.setEuro(currencyDataList.get(0).getRate());
                currencyRate.setTime(time);
                exchangeListView.setAdapter(new CurrencyAdapter(getContext(),currencyDataList));

                if (!list.equals(data)){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("DATALIST",data);
                    editor.apply();
                    String id = historyDatabase.push().getKey();
                    if (id !=null){
                        historyDatabase.child(id).setValue(currencyRate);
                    }
                    //Toast.makeText(getContext(), "Edit" + time, Toast.LENGTH_SHORT).show();

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private int getCurrencySymbol(String code) {
        switch (code){
            case "GBP": return R.drawable.ic_pound;
            case "EUR": return R.drawable.ic_euro;
            default: return R.drawable.ic_dollar;
        }
    }
    public final Context reqContext() {
        Context context = getContext();
        if (context == null) {
            context = requireContext();
        }
        return context;
    }
}
