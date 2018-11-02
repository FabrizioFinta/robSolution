package com.topdesk.cases.toprob.yoursolution;

import com.topdesk.cases.toprob.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class AStarCalculation {

    private List<AdvancedCoordinate> advancedCoordinates;
    private PriorityQueue<AdvancedCoordinate> openedList;
    private List<AdvancedCoordinate> closedList;
    private List<AdvancedCoordinate> parentTiles;

    private AdvancedCoordinate startPos;
    private AdvancedCoordinate targetPos;
    private AdvancedCoordinate currentPosition;


    public AStarCalculation(List<AdvancedCoordinate> advancedCoordinates, Coordinate startPos, Coordinate targetPos) {
        this.advancedCoordinates = advancedCoordinates;
        this.closedList = new ArrayList<>();
        this.openedList = new PriorityQueue<>(new CompareAdvancedCoordinates());
        this.parentTiles = new ArrayList<>();

        this.startPos = new AdvancedCoordinate(targetPos, startPos);
        this.targetPos = new AdvancedCoordinate(targetPos, targetPos);
    }

    void aStarCalculate() {

        currentPosition = startPos;
        openedList.add(currentPosition);

        while (!openedList.isEmpty()) {
            currentPosition = openedList.poll();
            closedList.add(currentPosition);
            if (isTargetReached()) {
                parentTiles.addAll(getPath(currentPosition));
                break;
            } else {
                addAdjacentNodes(currentPosition);
            }
        }
    }

    private boolean isTargetReached() {
        return currentPosition.getCoordinate().equals(targetPos.getCoordinate());
    }

    private void addAdjacentNodes(AdvancedCoordinate fakeCurrentPosition) {
        for (AdvancedCoordinate examinedTile : advancedCoordinates) {
            if (isAdjacentTile(fakeCurrentPosition.getCoordinate(), examinedTile) && !closedList.contains(examinedTile)) {
                if (!openedList.contains(examinedTile)) {
                    examinedTile.calculateValues(fakeCurrentPosition);
                    examinedTile.addParent(fakeCurrentPosition);
                    openedList.add(examinedTile);
                } else {
                    if (examinedTile.checkBetterPath(fakeCurrentPosition)) {
                        refreshOpenedListItem(examinedTile);
                    }
                }
            }
        }
    }

    private List<AdvancedCoordinate> getPath(AdvancedCoordinate fakeCurrentPosition) {
        List<AdvancedCoordinate> path = new ArrayList<>();
        path.add(fakeCurrentPosition);
        AdvancedCoordinate parent;
        while ((parent = fakeCurrentPosition.getParent()) != null){
            path.add(0, parent);
            fakeCurrentPosition = parent;
        }
        return path;
    }

    List<AdvancedCoordinate> getRoute(){
        return parentTiles;
    }

    private void refreshOpenedListItem(AdvancedCoordinate examinedTile) {
        openedList.remove(examinedTile);
        openedList.add(examinedTile);
    }

    private boolean isAdjacentTile(Coordinate currentPosition,AdvancedCoordinate examined) {
        boolean examination = (!currentPosition.equals(examined.getCoordinate()) &&
                (currentPosition.getX() == examined.getX() ^ currentPosition.getY() == examined.getY())) &&
                (currentPosition.getX()-1 == examined.getX() || currentPosition.getX()+1 == examined.getX()) ^
                        (currentPosition.getY()-1 == examined.getY() || currentPosition.getY()+1 == examined.getY());
        return examination;
    }

}
