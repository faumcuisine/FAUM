package com.faum.faum_rider;

import java.util.List;

/**
 * Created by M.UZAIR on 4/6/2018.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}

