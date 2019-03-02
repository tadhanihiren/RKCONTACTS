package ml.app.rkcontacts.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Spinner;
import android.widget.TableLayout;

import ml.app.rkcontacts.R;

public class NavFlatFragment extends Fragment {
    Spinner monthlist;
    WebView flatweb;
    TableLayout stk;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nav_fragment_flat, container, false);
        return v;
    }

}