package de.thral.atemschutzueberwachung.ui.administration.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class AdminBaseActivity extends AppCompatActivity {

    protected ListView listView;
    protected TextView noEntry;
    protected RelativeLayout progressBar;

    protected void setVisibility(int listCount){
        if(listCount == 0){
            noEntry.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            noEntry.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }


}


