package de.thral.draegermanObservation.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Squad;

public class DetailOverviewView extends LinearLayout {

    private View overviews;
    private DetailOverview[] overview;

    private LayoutClickListener layoutClickListener;

    public DetailOverviewView(Context context) {
        super(context);
        init();
    }

    public DetailOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        overviews = inflate(getContext(), R.layout.monitor_detail_overviews, this);
        overview = new DetailOverview[3];
        overview[0] = overviews.findViewById(R.id.overview_small_1);
        overview[1] = overviews.findViewById(R.id.overview_small_2);
        overview[2] = overviews.findViewById(R.id.overview_small_3);
    }

    public void setListener(LayoutClickListener layoutClickListener){
        this.layoutClickListener = layoutClickListener;
    }

    public boolean setSquads(Squad[] squads){
        if(squads.length == 3){
            for(int i=0; i<3; i++){
                if(squads[i] != null){
                    overview[i].setVisibility(VISIBLE);
                    overview[i].setSquad(squads[i]);
                    overview[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layoutClickListener.onLayoutClick(-1);
                        }
                    });
                } else {
                    overview[i].setVisibility(INVISIBLE);
                }
            }
            return true;
        }
        return false;
    }

}
