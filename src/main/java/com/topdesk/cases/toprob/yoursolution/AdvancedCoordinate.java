package com.topdesk.cases.toprob.yoursolution;

import com.topdesk.cases.toprob.Coordinate;
import com.topdesk.cases.toprob.Grid;

public class AdvancedCoordinate {

    private Integer hValue;
    private Integer gValue;
    private Integer fValue;
    private Coordinate coordinate;
    private AdvancedCoordinate parent;


    AdvancedCoordinate(Coordinate kitchen, Coordinate tile) {
        this.gValue = 0;
        this.hValue = Math.abs(tile.getX() - kitchen.getX()) + Math.abs(tile.getY() - kitchen.getY());
        this.coordinate = tile;
    }

    void calculateValues(AdvancedCoordinate currentPosition){
        if (fValue == null){
            this.gValue = currentPosition.getgValue() + 10;
            fValue = gValue + hValue;
        }
    }

    boolean checkBetterPath(AdvancedCoordinate currentPosition){
        int gCost = currentPosition.getgValue() + 10;
        if (gCost < getgValue()) {
            setgValue(currentPosition.getgValue() + 10);
            fValue = gValue + hValue;
            addParent(currentPosition);
            return true;
        }
        return false;
    }

    int getHValue() {
        return this.hValue;
    }

    int getgValue(){
        return this.gValue;
    }

    int getfValue(){
        return this.fValue;
    }

    AdvancedCoordinate getParent(){
        return this.parent;
    }

    int getX(){
        return this.coordinate.getX();
    }

    int getY(){
        return this.coordinate.getY();
    }

    Coordinate getCoordinate(){
        return this.coordinate;
    }

    void setgValue(int gValue){
        this.gValue = gValue;
    }

    void addParent(AdvancedCoordinate parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        if (fValue!=null) {
            return this.coordinate.toString() + " H: " + this.getHValue() + " F: " + this.getfValue();
        } else {
            return this.coordinate.toString() + " H: " + this.hValue;
        }
    }
}
