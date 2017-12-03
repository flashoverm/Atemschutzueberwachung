package de.thral.atemschutzueberwachung.ui.observation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ViewFlipper;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Squad;

public class OverviewView extends GridLayout {

    private View overviews;
    private ViewFlipper[] overviewFlipper;
    private Overview[] overview;
    private Button register, endOperation;

    private LayoutClickListener layoutClickListener;
    private OnClickListener registerListener;
    private OnClickListener endOperationListener;

    public OverviewView(Context context) {
        super(context);
        init();
    }

    public OverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        overviews = inflate(getContext(), R.layout.monitor_overviews, this);
        overviewFlipper = new ViewFlipper[4];
        overviewFlipper[0] = overviews.findViewById(R.id.flipper_1);
        overviewFlipper[1] = overviews.findViewById(R.id.flipper_2);
        overviewFlipper[2] = overviews.findViewById(R.id.flipper_3);
        overviewFlipper[3] = overviews.findViewById(R.id.flipper_4);
        overview = new Overview[4];
        overview[0] = overviews.findViewById(R.id.overview_1);
        overview[1] = overviews.findViewById(R.id.overview_2);
        overview[2] = overviews.findViewById(R.id.overview_3);
        overview[3] = overviews.findViewById(R.id.overview_4);
    }

    public void setListener(LayoutClickListener layoutClickListener,
                       OnClickListener registerListener, OnClickListener endOperationListener){
        this.layoutClickListener = layoutClickListener;
        this.registerListener = registerListener;
        this.endOperationListener = endOperationListener;
    }

    public boolean setSquads(Squad[] squads){
        if(squads.length==4){
            boolean registerDisplayed = false;
            for(int i=0; i<4; i++) {
                if (squads[i] != null) {
                    overview[i].setVisibility(View.VISIBLE);
                    overviewFlipper[i].setDisplayedChild(0);
                    overview[i].setSquad(squads[i]);
                    final int overviewNumber = i;
                    overview[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layoutClickListener.onLayoutClick(overviewNumber);
                        }
                    });
                } else if (!registerDisplayed) {
                    overviewFlipper[i].setDisplayedChild(1);
                    register = overviewFlipper[i].findViewById(R.id.buttonRegister);
                    register.setOnClickListener(registerListener);
                    registerDisplayed = true;
                } else {
                    overviewFlipper[i].setDisplayedChild(0);
                    overview[i].setVisibility(View.INVISIBLE);
                }
                if(i==3 && squads[0]==null){
                    overviewFlipper[i].setDisplayedChild(2);
                    endOperation = overviewFlipper[i].findViewById(R.id.buttonEndOperation);
                    endOperation.setOnClickListener(endOperationListener);
                }
            }
            return true;
        }
        return false;
    }
}
