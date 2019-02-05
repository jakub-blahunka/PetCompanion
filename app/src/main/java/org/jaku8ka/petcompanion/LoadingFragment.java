package org.jaku8ka.petcompanion;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingFragment extends Fragment implements ISetTextInFragment {

    TextView tvLoad;

    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        tvLoad = view.findViewById(R.id.tvLoadF);

        return view;
    }

    @Override
    public void showText(String text) {
        tvLoad.setText(text);
    }
}
