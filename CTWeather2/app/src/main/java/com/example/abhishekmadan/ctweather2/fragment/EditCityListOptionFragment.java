package com.example.abhishekmadan.ctweather2.fragment;

import android.content.ClipData;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.adapter.EditCityListOptionRecyclerCursorAdapter;
import com.example.abhishekmadan.ctweather2.database.DbContract;

/**
 * Fragment which is displayed when the user selects the edit city option
 * from the navigation drawer.
 */
public class EditCityListOptionFragment extends Fragment {

    private RecyclerView mCityListRecyclerView;

    private EditCityListOptionRecyclerCursorAdapter cursorAdapter;

    private Cursor mCursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_window_layout, container, false);
        mCityListRecyclerView = (RecyclerView) view.findViewById(R.id.city_list_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] projection = {DbContract.COL_CITY_CODE,
                DbContract.COL_CITY,
                DbContract.COL_FAVORITE};
        mCursor = getActivity().getContentResolver().query(DbContract.CONTENT_URI, projection, null, null, DbContract.COL_CID + " ASC");
        mCityListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cursorAdapter = new EditCityListOptionRecyclerCursorAdapter(getActivity(), mCursor);
        mCityListRecyclerView.setAdapter(cursorAdapter);

        ItemTouchHelper.Callback callback = new EditItemTouchHelper(cursorAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mCityListRecyclerView);

    }

    public class EditItemTouchHelper extends ItemTouchHelper.SimpleCallback {
        private EditCityListOptionRecyclerCursorAdapter adapter;
        public EditItemTouchHelper(EditCityListOptionRecyclerCursorAdapter myAdapter) {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
            this.adapter = myAdapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (adapter.getItemCount() != 1) {
                adapter.removeItem(viewHolder.getAdapterPosition());
            } else {
                adapter.notifyDataSetChanged();
            }

        }

    }


}
