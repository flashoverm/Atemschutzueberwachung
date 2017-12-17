package de.thral.draegermanObservation.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class AdminBaseActivity extends AppCompatActivity {

    protected ListView listView;
    protected TextView noEntry;
    protected ProgressBar progressBar;

    protected void setVisibility(boolean entries){
        if(entries){
            noEntry.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            noEntry.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    protected void setMenuItemClickListener(Menu menu, MenuItem.OnMenuItemClickListener listener){
        for(int i=0; i<menu.size(); i++){
            menu.getItem(i).setOnMenuItemClickListener(listener);
        }
    }

    protected void showProgress(final boolean show){
        if(show){
            listView.setVisibility(View.GONE);
            noEntry.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        } else {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    protected void showInfo(int stringId) {

    }
}


