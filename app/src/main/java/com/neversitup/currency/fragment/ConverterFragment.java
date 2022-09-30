package com.neversitup.currency.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.neversitup.currency.R;
import com.neversitup.currency.model.CurrencyRate;

import java.util.ArrayList;
import java.util.List;

public class ConverterFragment extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener {

    EditText inputEdt;
    DatabaseReference currencyDatabase;
    CurrencyRate currencyRate;
    Float initBtcValue,initDollarValue,initPoundValue,initEuroValue;
    SharedPreferences sharedPreferences;
    String inputString;
    Spinner spinner;
    CardView cardView;
    ImageView firstIcon,secondIcon,thirdIcon;
    TextView firstValue,secondValue,thirdValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coverter_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodManager imm = (InputMethodManager) reqContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        spinner = view.findViewById(R.id.currency_spinner);
        cardView = view.findViewById(R.id.output_card_view);
        firstIcon = view.findViewById(R.id.first_icon);
        firstValue = view.findViewById(R.id.first_value);
        secondIcon = view.findViewById(R.id.second_icon);
        secondValue = view.findViewById(R.id.second_value);
        thirdIcon = view.findViewById(R.id.third_icon);
        thirdValue = view.findViewById(R.id.third_value);

        inputEdt = view.findViewById(R.id.currency_input_value);
        List<String> type = new ArrayList<>();
        type.add("BTC");
        type.add("USD");
        type.add("GBP");
        type.add("EUR");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        inputEdt.addTextChangedListener(this);

        spinner.setOnItemSelectedListener(this);

        sharedPreferences = reqContext().getSharedPreferences("INITIAL_VALUE", Context.MODE_PRIVATE);
        currencyDatabase = FirebaseDatabase.getInstance().getReference("history");
        Query lastQuery = currencyDatabase.orderByKey().limitToLast(1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    currencyRate = dataSnapshot.getValue(CurrencyRate.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("BITCOIN",1);
                    editor.putFloat("DOLLAR", (float) currencyRate.getDollar());
                    editor.putFloat("POUND", (float) currencyRate.getDollar());
                    editor.putFloat("EURO", (float) currencyRate.getDollar());
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initBtcValue = sharedPreferences.getFloat("BITCOIN",0);
        initDollarValue = sharedPreferences.getFloat("DOLLAR",0);
        initPoundValue = sharedPreferences.getFloat("POUND",0);
        initEuroValue = sharedPreferences.getFloat("EURO",0);
                new  Handler().postDelayed(this,5000);
            }
        },0);

    }

    private Context reqContext() {
        Context context = getContext();
        if (context == null) {
            context = requireContext();
        }
        return context;
    }

    private void CovertCurrencyFromEUR(String euro) {
        cardView.setVisibility(View.VISIBLE);
        double value = Double.parseDouble(euro);
        String bitcoin = String.valueOf(value/initEuroValue);
        firstIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bitcoin));
        firstValue.setText(bitcoin);
        String dollar = String.valueOf((value/initEuroValue)*initDollarValue);
        secondIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_dollar));
        secondValue.setText(dollar);
        String pound = String.valueOf((value/initEuroValue)*initPoundValue);
        thirdIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pound));
        thirdValue.setText(pound);
    }

    private void CovertCurrencyFromGBP(String pound) {
        cardView.setVisibility(View.VISIBLE);
        double value = Double.parseDouble(pound);
        String bitcoin= String.valueOf(value/initPoundValue);
        firstIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bitcoin));
        firstValue.setText(bitcoin);
        String dollar = String.valueOf((value/initPoundValue)*initDollarValue);
        secondIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_dollar));
        secondValue.setText(dollar);
        String euro = String.valueOf((value/initPoundValue)*initEuroValue);
        thirdIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_euro));
        thirdValue.setText(euro);
    }

    private void CovertCurrencyFromUSD(String dollar) {
        cardView.setVisibility(View.VISIBLE);
        double value = Double.parseDouble(dollar);
        String bitcoin = String.valueOf(value/initDollarValue);
        firstIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bitcoin));
        firstValue.setText(bitcoin);
        String pound = String.valueOf((value/initDollarValue)*initPoundValue);
        secondIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pound));
        secondValue.setText(pound);
        String euro = String.valueOf((value/initDollarValue)*initEuroValue);
        thirdIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_euro));
        thirdValue.setText(euro);
    }

    private void CovertCurrencyFromBTC(String bitcoin) {
        cardView.setVisibility(View.VISIBLE);
        double value = Double.parseDouble(bitcoin);
        String dollar = String.valueOf(value * initDollarValue);
        firstIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_dollar));
        firstValue.setText(dollar);
        String pound = String.valueOf(value * initPoundValue);
        secondIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pound));
        secondValue.setText(pound);
        String euro = String.valueOf(value * initEuroValue);
        thirdIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_euro));
        thirdValue.setText(euro);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputString = inputEdt.getText().toString();
        if (!inputString.isEmpty()) {
            if (spinner.getSelectedItem() == "BTC") {
                CovertCurrencyFromBTC(inputString);
            } else if (spinner.getSelectedItem() == "USD") {
                CovertCurrencyFromUSD(inputString);
            } else if (spinner.getSelectedItem() == "GBP") {
                CovertCurrencyFromGBP(inputString);
            } else if (spinner.getSelectedItem() == "EUR") {
                CovertCurrencyFromEUR(inputString);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        switch (item) {
            case "BTC":
                inputEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_bitcoin, 0);
                cardView.setVisibility(View.GONE);
                inputEdt.setText(null);
                break;
            case "USD":
                inputEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dollar, 0);
                cardView.setVisibility(View.GONE);
                inputEdt.setText(null);
                break;
            case "GBP":
                inputEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pound, 0);
                cardView.setVisibility(View.GONE);
                inputEdt.setText(null);
                break;
            case "EUR":
                inputEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_euro, 0);
                cardView.setVisibility(View.GONE);
                inputEdt.setText(null);
                break;
        }
        inputEdt.addTextChangedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
