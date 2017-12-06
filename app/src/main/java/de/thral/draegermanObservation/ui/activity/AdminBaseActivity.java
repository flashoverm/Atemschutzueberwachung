package de.thral.draegermanObservation.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class AdminBaseActivity extends AppCompatActivity {

    protected ListView listView;
    protected TextView noEntry;
    protected RelativeLayout progressBar;

    /** Makes no-entry-hint visible if the listCount is 0 otherwise shows the list
     *
     * @param listCount count of entries in the list
     */
    protected void setVisibility(int listCount){
        if(listCount == 0){
            noEntry.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            noEntry.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    protected void setMenuItemClickListener(Menu menu, MenuItem.OnMenuItemClickListener listener){
        for(int i=0; i<menu.size(); i++){
            menu.getItem(i).setOnMenuItemClickListener(listener);
        }
    }

    protected void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}


