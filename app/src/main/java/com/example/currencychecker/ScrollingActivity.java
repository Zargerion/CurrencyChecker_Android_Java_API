package com.example.currencychecker;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.currencychecker.databinding.ActivityScrollingBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private EditText searchEdt;
    private RecyclerView currenciesView;
    private ProgressBar loadingPB;
    private ArrayList<CurrencyViewModel> currencyViewModelArrayList;
    private CurrencyViewAdapter currencyViewAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        searchEdt = findViewById(R.id.idSearch);
        View rootView = findViewById(android.R.id.content);

        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    searchEdt.clearFocus();
                }
                return false;
            }
        });

        InputFilter filter1 = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter filter2 = new InputFilter.LengthFilter(3);
        searchEdt.setFilters(new InputFilter[] { filter1, filter2 });

        currenciesView = findViewById(R.id.idCurrencies);
        loadingPB = findViewById(R.id.idLoading);
        this.currencyViewModelArrayList = new ArrayList<>();
        currencyViewAdapter = new CurrencyViewAdapter(currencyViewModelArrayList, this);
        currenciesView.setLayoutManager(new LinearLayoutManager(this));
        currenciesView.setAdapter(currencyViewAdapter);
        getCurrentData(null);

        //searchEdt.addTextChangedListener(new TextWatcher() {
        //    @Override
        //    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //    }
        //    @Override
        //    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //    }
        //    @Override
        //    public void afterTextChanged(Editable editable) {
        //        filterCurrencies(editable.toString());
        //    }
        //});

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrencies(s.toString());
            }
        };

        searchEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchEdt.addTextChangedListener(textWatcher);
                } else {
                    searchEdt.removeTextChangedListener(textWatcher);
                }
            }
        });
    }

    private void filterCurrencies(String currency) {
        ArrayList<CurrencyViewModel> filteredList = new ArrayList<>();
        for(CurrencyViewModel item: currencyViewModelArrayList) {
            if(item.getSymbol().toUpperCase().contains(currency.toUpperCase())) {
                filteredList.add(item);
            }
        } if(filteredList.isEmpty()) {
            Toast.makeText(this, "No currency found for searched query.", Toast.LENGTH_SHORT).show();
        } else {
            currencyViewAdapter.filterList(filteredList);
        }
    }
    public void getCurrentData(View view) {
        loadingPB.setVisibility(View.VISIBLE);
        String currencies = "AED,AFN,ALL,AMD,ANG,AOA,ARS,AUD,AWG,AZN,BAM,BBD,BDT,BGN,BHD,BIF,BMD,BND,BOB,BRL,BSD,BTC,BTN,BWP,BYN,BYR,BZD,CAD,CDF,CHF,CLF,CLP,CNY,COP,CRC,CUC,CUP,CVE,CZK,DJF,DKK,DOP,DZD,EGP,ERN,ETB,EUR,FJD,FKP,GBP,GEL,GGP,GHS,GIP,GMD,GNF,GTQ,GYD,HKD,HNL,HRK,HTG,HUF,IDR,ILS,IMP,INR,IQD,IRR,ISK,JEP,JMD,JOD,JPY,KES,KGS,KHR,KMF,KPW,KRW,KWD,KYD,KZT,LAK,LBP,LKR,LRD,LSL,LTL,LVL,LYD,MAD,MDL,MGA,MKD,MMK,MNT,MOP,MRO,MUR,MVR,MWK,MXN,MYR,MZN,NAD,NGN,NIO,NOK,NPR,NZD,OMR,PAB,PEN,PGK,PHP,PKR,PLN,PYG,QAR,RON,RSD,RUB,RWF,SAR,SBD,SCR,SDG,SEK,SGD,SHP,SLE,SLL,SOS,SRD,STD,SVC,SYP,SZL,THB,TJS,TMT,TND,TOP,TRY,TTD,TWD,TZS,UAH,UGX,USD,UYU,UZS,VEF,VES,VND,VUV,WST,XAF,XAG,XAU,XCD,XDR,XOF,XPF,YER,ZAR,ZMK,ZMW,ZWL";
        String url = String.format("https://api.apilayer.com/currency_data/live?source=USD&currencies=%s", currencies);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                currencyViewModelArrayList.clear();
                try {
                    JSONObject quotesObj = response.getJSONObject("quotes");
                    Iterator<String> keys = quotesObj.keys();
                    while(keys.hasNext()) {
                        String originalString = keys.next();
                        String newString = originalString.substring(3);
                        String symbol = newString;
                        int length = originalString.length();
                        String name = originalString.substring(0, length-3);
                        double price = quotesObj.getDouble(originalString);
                        currencyViewModelArrayList.add(new CurrencyViewModel(name, symbol, price));
                    }
                    currencyViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ScrollingActivity.this, "Fail to extract json data =(", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                loadingPB.setVisibility(View.GONE);
                e.printStackTrace();
                Toast.makeText(ScrollingActivity.this, "Fail to get the data from api =(", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("apikey", "UspQXaUXMKbJzW0b88VmaYZhPNp1GWHm");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}