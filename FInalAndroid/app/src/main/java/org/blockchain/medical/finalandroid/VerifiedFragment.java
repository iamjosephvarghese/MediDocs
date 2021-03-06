package org.blockchain.medical.finalandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VerifiedFragment extends Fragment {
    // Store instance variables
//    private String title;
//    private int page;

   String classs , recordId ,owner ,verified1;
   String[] value , doctorId;
   int j;

   ProgressBar pd;


    private List<Verified> verifiedList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VerifiedAdapter mAdapter;
    SwipeRefreshLayout pullToRefresh;
    private List<Verified> unverifiedList = new ArrayList<>();

    Button bt1,bt2;
    int page;



    // newInstance constructor for creating fragment with arguments
    public static VerifiedFragment newInstance(int page, String title) {
        VerifiedFragment fragmentFirst = new VerifiedFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
//        title = getArguments().getString("someTitle");

        Log.d("xxx", String.valueOf(page));

        if (page == 1){
            getRecord1();
            Log.d("xxx", "1");
        }
        if (page == 2){
            getRecord2();
            Log.d("xxx","2");
        }






    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verified, container, false);


        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        bt1 = view.findViewById(R.id.bt1);
        bt2 = view.findViewById(R.id.bt2);

        pd = view.findViewById(R.id.pd);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());

        if (page == 1) {

            mAdapter = new VerifiedAdapter(verifiedList);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        }

        if (page == 2) {
            mAdapter = new VerifiedAdapter(unverifiedList);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }



        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                verifiedList.clear();
                unverifiedList.clear();
                mAdapter.notifyDataSetChanged();
                getRecord1();
                getRecord2();


            }
        });


//        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);


        return view;
    }


    void getRecord1(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);



        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        Call<List<RecordResponse>> call = api.getRecord(pref.getString("name","2001"),"verified");

        call.enqueue(new Callback<List<RecordResponse>>() {
            @Override
            public void onResponse(Call<List<RecordResponse>> call, Response<List<RecordResponse>> response) {

                List<RecordResponse> docs = response.body();

                pd.setVisibility(View.INVISIBLE);
                pullToRefresh.setRefreshing(false);

                //Log.d("qweerty",docs.get(0).owner);

                for (j =0 ; j < response.body().size() ; j++){
                    classs = docs.get(j).classs;
                    recordId = docs.get(j).recordId;
                    owner = docs.get(j).owner;
                    value = docs.get(j).value;
                    doctorId = docs.get(j).doctorId;
                    verified1 = docs.get(j).verified;

                    Log.d("qweee",classs);

                    if (verified1.equals("true")){


                        Verified verified = new Verified(classs,recordId,owner,value,doctorId,verified1);
                        verifiedList.add(verified);

                        mAdapter.notifyDataSetChanged();




                    }
                }





            }

            @Override
            public void onFailure(Call<List<RecordResponse>> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }



    void getRecord2(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);


        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

        Call<List<RecordResponse>> call = api.getRecord(pref.getString("name","2001"),"unverified");

        call.enqueue(new Callback<List<RecordResponse>>() {
            @Override
            public void onResponse(Call<List<RecordResponse>> call, Response<List<RecordResponse>> response) {

                List<RecordResponse> docs = response.body();
                //Log.d("qweerty",docs.get(0).owner);

                pd.setVisibility(View.INVISIBLE);

                pullToRefresh.setRefreshing(false);

                for (j =0 ; j < response.body().size() ; j++){
                    classs = docs.get(j).classs;
                    recordId = docs.get(j).recordId;
                    owner = docs.get(j).owner;
                    value = docs.get(j).value;
                    doctorId = docs.get(j).doctorId;
                    verified1 = docs.get(j).verified;

                    Log.d("qweee",classs);

                    if (verified1.equals("false")){


                        Verified verified = new Verified(classs,recordId,owner,value,doctorId,verified1);
                        unverifiedList.add(verified);

                        mAdapter.notifyDataSetChanged();




                    }
                }




            }

            @Override
            public void onFailure(Call<List<RecordResponse>> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });






    }


}