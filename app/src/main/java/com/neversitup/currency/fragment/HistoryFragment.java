package com.neversitup.currency.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neversitup.currency.R;
import com.neversitup.currency.model.CurrencyRate;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    List<CurrencyRate> fetchData;
    DatabaseReference historyDatabase;
    HistoryAdapter historyAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment,container,false);
        return view;
    }

    private Context reqContext() {
        Context context = getContext();
        if (context == null) {
            context = requireContext();
        }
        return context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodManager imm = (InputMethodManager) reqContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        recyclerView = view.findViewById(R.id.history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        fetchData = new ArrayList<>();

        historyDatabase = FirebaseDatabase.getInstance().getReference("history");
        historyDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fetchData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CurrencyRate currencyRate = dataSnapshot.getValue(CurrencyRate.class);
                    fetchData.add(currencyRate);
                    historyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        historyAdapter = new HistoryAdapter(getContext(),fetchData);
        recyclerView.setAdapter(historyAdapter);

    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

        Context context;
        List<CurrencyRate> fetchData;

        public HistoryAdapter(Context context, List<CurrencyRate> fetchData) {
            this.context = context;
            this.fetchData =fetchData;
        }

        @NonNull
        @Override
        public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.history_item,parent,false);
            return new HistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
            CurrencyRate currencyRate = fetchData.get(position);
            holder.dollarValue.setText(String.valueOf(currencyRate.getDollar()));
            holder.euroValue.setText(String.valueOf(currencyRate.getEuro()));
            holder.poundValue.setText(String.valueOf(currencyRate.getPound()));
            holder.historyTime.setText(currencyRate.getTime());
        }

        @Override
        public int getItemCount() {
            return fetchData.size();
        }

        public class HistoryViewHolder extends RecyclerView.ViewHolder {

            TextView dollarValue,poundValue,euroValue;
            TextView historyTime;

            public HistoryViewHolder(@NonNull View itemView) {
                super(itemView);

                dollarValue = itemView.findViewById(R.id.dollar_value);
                poundValue = itemView.findViewById(R.id.pound_value);
                euroValue = itemView.findViewById(R.id.euro_value);
                historyTime = itemView.findViewById(R.id.history_time);
            }
        }
    }

}

