package com.topdesk.cases.toprob.yoursolution;

import com.topdesk.cases.toprob.Coordinate;
import com.topdesk.cases.toprob.Grid;

public class AdvancedCoordinate {

    private Integer hValue;
    private Integer gValue;
    private Integer fValue;
    private Coordinate coordinate;

    public AdvancedCoordinate(Coordinate kitchen, Coordinate tile) {
        this.hValue = Math.abs(tile.getX() - kitchen.getX()) + Math.abs(tile.getY() - kitchen.getY());
        this.coordinate = tile;
    }

    public int getHValue() {
        return this.hValue;
    }

    public int getX(){
        return this.coordinate.getX();
    }

    public int getY(){
        return this.coordinate.getY();
    }

    public Coordinate getCoordinate(){
        return this.coordinate;
    }

    @Override
    public String toString() {
        return this.coordinate + " H: " + this.getHValue();
    }
}
