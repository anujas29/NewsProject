package anuja.project.finalproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anuja.project.finalproject.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by USER on 17-10-2017.
 */

public class FavouriteFragment extends Fragment {

    @BindView(R.id.fab_recycler_view)
    RecyclerView recyclerView;

    public FavouriteFragment(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fab_recycler_view, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
