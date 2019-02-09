package org.jaku8ka.petcompanion;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetsListFragment extends ListFragment {

    ItemSelected activity;
    ListView lvPetsList;
    PetsAdapter adapter;

    public PetsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (ItemSelected) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pets_list, container, false);

        lvPetsList = view.findViewWithTag("lvPetsList");
        adapter = new PetsAdapter(getContext(), ApplicationClass.pets);
        lvPetsList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        activity.onItemSelected(position);

        MainActivity.POSITION = position;
        for(int i = 0; i < ApplicationClass.pets.size(); i++) {
            ApplicationClass.pets.get(i).setSelected(false);
        }
        ApplicationClass.pets.get(position).setSelected(true);
        adapter.notifyDataSetChanged();
    }
}
