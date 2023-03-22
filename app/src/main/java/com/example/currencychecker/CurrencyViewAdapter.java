package com.example.currencychecker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyViewAdapter extends RecyclerView.Adapter<CurrencyViewAdapter.ViewHolder> {

    private ArrayList<CurrencyViewModel> currencyViewModelArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.#########");

    public CurrencyViewAdapter(ArrayList<CurrencyViewModel> currencyViewModelArrayList, Context context) {
        this.currencyViewModelArrayList = currencyViewModelArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<CurrencyViewModel> filterList) {
        currencyViewModelArrayList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_item, viewGroup, false);
        return new CurrencyViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewAdapter.ViewHolder viewHolder, int i) {
        CurrencyViewModel currencyViewModel = currencyViewModelArrayList.get(i);
        viewHolder.currencyName.setText(currencyViewModel.getName());
        viewHolder.currencySymbol.setText(currencyViewModel.getSymbol());
        viewHolder.currencyPrice.setText("$ " + df2.format(currencyViewModel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return currencyViewModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView currencyName, currencySymbol, currencyPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.idCurrencyName);
            currencySymbol = itemView.findViewById(R.id.idCurrencySymbol);
            currencyPrice = itemView.findViewById(R.id.idCurrencyPrice);
        }
    }
}
