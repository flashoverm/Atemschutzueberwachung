package de.thral.atemschutzueberwachung.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Squad;

public class DetailOverviewView extends LinearLayout {

    private View overviews;
    private DetailOverview[] overview;

    private LayoutClickListener layoutClickListener;

    public DetailOverviewView(Context context) {
        super(context);
        init(context);
    }

    public DetailOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        overviews = inflate(context, R.layout.monitor_detail_overviews, this);
        overview = new DetailOverview[3];
        overview[0] = (DetailOverview) overviews.findViewById(R.id.overview_small_1);
        overview[1] = (DetailOverview) overviews.findViewById(R.id.overview_small_2);
        overview[2] = (DetailOverview) overviews.findViewById(R.id.overview_small_3);
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
