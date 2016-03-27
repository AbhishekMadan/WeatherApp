package com.example.abhishekmadan.ctweather2.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.adapter.AddCityToListOptionAdapter;
import com.example.abhishekmadan.ctweather2.database.DbContract;
import com.example.abhishekmadan.ctweather2.modal.DbModalCity;
import com.example.abhishekmadan.ctweather2.util.CityList;

/**
 * Fragment for the add city option visible in the Navigation Drawer.
 */
public class AddCityToListFragment extends Fragment implements SearchView.OnQueryTextListener,AdapterView.OnItemClickListener,
        DialogInterface.OnClickListener{

    private SearchView mSearchView;

    private ListView mListView;

    private AddCityToListOptionAdapter mAdapter;

    private long mCityCodeToAdd = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.add_city_to_list_option_layout,container,false);
        mSearchView = (SearchView) view.findViewById(R.id.search_view);
        mListView = (ListView) view.findViewById(R.id.list_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint("Enter the name of the city");
        mSearchView.setOnQueryTextListener(this);

        mAdapter = new AddCityToListOptionAdapter(getActivity(), CityList.CITY_LIST);
        mListView.setAdapter(mAdapter);
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(newText.length()==0)
           mListView.clearTextFilter();
        else
           mListView.setFilterText(newText);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView cityCode = (TextView) view.findViewById(R.id.city_code_textview);
        mCityCodeToAdd = Long.parseLong(cityCode.getText().toString());
        getDialogConfirmationForAdding();

    }

    /**
     * Method to generate a dialog to the user to confirm addition of the city to the DB.
     */
    public void getDialogConfirmationForAdding(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_confirm);
        builder.setTitle("Confirm");
        builder.setMessage("Confirm Add City To List?");
        builder.setPositiveButton("Yes",this);
        builder.setNegativeButton("No",this);
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which){
            case DialogInterface.BUTTON_POSITIVE:
                 putDataToDb();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mCityCodeToAdd = -1;
                break;
        }
    }

    /**
     * Method to add the selected city to the database if the user
     * confirms.
     */
    public void putDataToDb(){

          Cursor cursor = getActivity().getContentResolver().query(DbContract.CONTENT_URI,null,DbContract.COL_CITY_CODE+"=?",new String[]{String.valueOf(mCityCodeToAdd)},null);
          if(cursor.getCount()!=0){
              Toast.makeText(getActivity(),"City already exist in the List!",Toast.LENGTH_LONG).show();
          }else {

              DbModalCity selectedCity = null;
              for(DbModalCity obj : CityList.CITY_LIST){
                  if(obj.getmCityId()==mCityCodeToAdd){
                      selectedCity = obj;
                  }
              }
              int count = -1;
              if(selectedCity!=null) {
                  ContentValues values = new ContentValues();
                  values.put(DbContract.COL_CITY_CODE, selectedCity.getmCityId());
                  values.put(DbContract.COL_CITY,selectedCity.getmCity());
                  values.put(DbContract.COL_COUNTRY,selectedCity.getmCountry());
                  Uri uri = getActivity().getContentResolver().insert(DbContract.CONTENT_URI, values);
                  count = Integer.parseInt(uri.getLastPathSegment());
                  if(count>0){
                      Toast.makeText(getActivity(),"City Inserted Successfully",Toast.LENGTH_SHORT).show();
                  }else{
                      Toast.makeText(getActivity(),"Problem Inserting Data To List",Toast.LENGTH_SHORT).show();
                  }

              }

          }

    }
}
