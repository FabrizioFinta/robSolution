package com.topdesk.cases.toprob.yoursolution;

import java.util.Comparator;

public class CompareAdvancedCoordinates implements Comparator<AdvancedCoordinate> {
    @Override
    public int compare(AdvancedCoordinate a1, AdvancedCoordinate a2) {
        return Integer.compare(a1.getHValue(), a2.getHValue());
    }
}
