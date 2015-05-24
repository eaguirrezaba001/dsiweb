package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class FragmentMain extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        createTabContent(view);


        return view;
    }


    private void createTabContent(View view) {

        Resources res = getResources();

        TabHost tabs = (TabHost) view.findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.restaurants), res.getDrawable(android.R.drawable.ic_btn_speak_now));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);


        tabs.setCurrentTab(0);

    }

}