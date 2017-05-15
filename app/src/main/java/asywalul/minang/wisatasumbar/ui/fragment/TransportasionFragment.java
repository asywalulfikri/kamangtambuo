package asywalul.minang.wisatasumbar.ui.fragment;

import asywalul.minang.wisatasumbar.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Toshiba on 3/12/2016.
 */
public class TransportasionFragment extends Fragment {


    public static TransportasionFragment newInstance() {
        return new TransportasionFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);


        return root;
    }
}

