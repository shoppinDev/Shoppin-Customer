package com.dealwala.main.dealwala.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.model.DealDataModel;
import com.dealwala.main.dealwala.util.JSONParser;
import com.dealwala.main.dealwala.util.ModuleClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<DealDataModel> dataList = new ArrayList<>();

    RecyclerView recyclerView;

    TextView tvEmpty;

    boolean isQueryRequested = false;

    public WallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WallFragment newInstance(String param1, String param2) {
        WallFragment fragment = new WallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wall, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);

        new GetAllDealTask().execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setBackgroundColor(getResources().getColor(R.color.white));

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Notification", "Search click");
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.v("Notification", "Search close");
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.v("Notification", "On expand");
                //linLaySearch.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.v("Notification", "On Collapse");
                //linLaySearch.setVisibility(View.GONE);
                return true;
            }
        });

        //*** setOnQueryTextListener ***
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                Log.v("Notification","Query Submit :"+query);

                if(dataList != null){
                    dataList.clear();
                }

                if(!isQueryRequested)
                    new GetSearchDealTask(query).execute();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                Log.v("Notification","Query text change :"+newText);
                return false;
            }
        });

        setSearchTextColour(mSearchView);
        setCloseSearchIcon(mSearchView);
    }

    private void setSearchTextColour(SearchView searchView) {
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchPlate.setTextColor(getResources().getColor(R.color.textColor));
        searchPlate.setHintTextColor(getResources().getColor(R.color.textColor));
        searchPlate.setHint("Search by Product,Shop etc.");
        searchPlate.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    private void setCloseSearchIcon(SearchView searchView) {
        try {
            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView closeBtn = (ImageView) searchField.get(searchView);
            closeBtn.setImageResource(R.drawable.icon_cancel);

            ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
            searchButton.setImageResource(R.drawable.icon_search);
        } catch (NoSuchFieldException e) {
            Log.e("SearchView", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e("SearchView", e.getMessage(), e);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            Log.v("Notification", "Search Selected");
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class GetAllDealTask extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError;
        ProgressDialog dialog;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (success) {
                if (dataList.size() > 0) {
                    RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getActivity(), dataList);
                    recyclerView.setAdapter(adapter);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getActivity(), responseError, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "deal_list"));

            double latitude = 0.0000;
            double longitude = 0.0000;
            if(MainActivity.mCurrentLocation != null){
                inputArray.add(new BasicNameValuePair("long",""+MainActivity.mCurrentLocation.getLongitude()));
                inputArray.add(new BasicNameValuePair("lat",""+MainActivity.mCurrentLocation.getLatitude()));
            }else{
                inputArray.add(new BasicNameValuePair("lat",""+latitude));
                inputArray.add(new BasicNameValuePair("long",""+longitude));
            }
            Log.d("Deal List input ", inputArray.toString());
            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH + "index.php", "GET", inputArray);
            Log.d("Deal List ", responseJSON.toString());

            if (responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        getDealListFromJson(dataArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            } else {
                success = false;
                responseError = "There is some problem in server connection";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(dataList != null){
                dataList.clear();
            }

            dialog = new ProgressDialog(getActivity(), R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }

    public void getDealListFromJson(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                Log.v("Notification", "Data object " + object.toString());

                DealDataModel data = new DealDataModel(
                        object.getString("dealid"),
                        object.getString("merchantid"),
                        object.getString("shopid"),
                        object.getString("dealcategory"),
                        object.getString("dealsubcategory"),
                        object.getString("dealtitle"),
                        object.getString("dealdescription"),
                        object.getString("dealamount"),
                        object.getString("dealstartdate"),
                        object.getString("dealenddate"),
                        object.getString("alldays"),
                        object.getString("orignal_value"),
                        object.getString("discountvalue"),
                        object.getString("discounttype"),
                        object.getString("location"),
                        object.getString("dealusage"),
                        object.getString("isactive"),
                        object.getString("addeddate"),
                        object.getString("categoryname"),
                        object.getString("subcategoryname"),
                        object.getString("merchantname"),
                        object.getString("shopname")
                );

                dataList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class GetSearchDealTask extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError;
        String searchString;
        ProgressDialog dialog;

        public GetSearchDealTask(String searchString){
            this.searchString = searchString;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            isQueryRequested = false;
            if (success) {
                if (dataList.size() > 0) {
                    Log.v("Notification","Data List Size : "+dataList.size());
                    RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getActivity(), dataList);
                    recyclerView.setAdapter(adapter);

                    if(tvEmpty.isShown())
                        tvEmpty.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "No deals found on this search query", Toast.LENGTH_LONG).show();
                    //tvEmpty.setVisibility(View.VISIBLE);

                    /*if(recyclerView != null){
                        if(recyclerView.isShown());
                    }*/
                }
            } else {
                Toast.makeText(getActivity(), responseError, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "deal_list"));
            inputArray.add(new BasicNameValuePair("search", searchString));

            double latitude = 0.0000;
            double longitude = 0.0000;
            if(MainActivity.mCurrentLocation != null){
                inputArray.add(new BasicNameValuePair("long",""+MainActivity.mCurrentLocation.getLongitude()));
                inputArray.add(new BasicNameValuePair("lat",""+MainActivity.mCurrentLocation.getLatitude()));
            }else{
                inputArray.add(new BasicNameValuePair("lat",""+latitude));
                inputArray.add(new BasicNameValuePair("long",""+longitude));
            }

            Log.d("Deal List input ", inputArray.toString());
            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH + "index.php", "GET", inputArray);
            Log.d("Deal List ", responseJSON.toString());

            if (responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        getDealListFromJson(dataArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            } else {
                success = false;
                responseError = "There is some problem in server connection";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            isQueryRequested = true;

            dialog = new ProgressDialog(getActivity(), R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }


}
