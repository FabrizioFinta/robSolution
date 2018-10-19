package com.topdesk.cases.toprob.yoursolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.topdesk.cases.toprob.Coordinate;
import com.topdesk.cases.toprob.Grid;
import com.topdesk.cases.toprob.Instruction;
import com.topdesk.cases.toprob.Solution;

public class YourSolution implements Solution {

    private int currentTime;
    private Grid grid;

    private Coordinate currentPosition;
    private Coordinate kitchenPos;
    private Coordinate roomPos;
    private Coordinate forbiddenTile;

    private List<Coordinate> parentTiles = new ArrayList<>();
    private List<Instruction> route = new ArrayList<>();

    @Override
    public List<Instruction> solve(Grid grid, int time) {
        if (time < 0) throw new IllegalArgumentException();

        setGrid(grid);
        setCurrentTime(time);
        setKitchenPos(grid.getKitchen());
        setRoomPos(grid.getRoom());
        setCurrentPosition(roomPos);

        List<AdvancedCoordinate> advancedCoordinates = convertGrid();
        advancedCoordinates.sort(new CompareAdvancedCoordinates());

        findTheRoute(advancedCoordinates);

        return route;
    }

    private void findTheRoute(List<AdvancedCoordinate> advancedCoordinates) {
        goToKitchen(advancedCoordinates);
        stopAtTheKitchen();
        goToRoom(parentTiles);
    }

    private void goToKitchen(List<AdvancedCoordinate> advancedCoordinates) {
        while (!currentPosition.equals(kitchenPos)) {
            setForbiddenTile(grid.getBug(currentTime+1));
            List<AdvancedCoordinate> stepList = sortTheTiles(advancedCoordinates);
            Coordinate simplestTile = stepList.get(0).getCoordinate();
            addRoute(decideWhereToMove(simplestTile));
            if (route.iterator().hasNext() && route.get(route.size()-1) != Instruction.PAUSE) parentTiles.add(currentPosition);
            currentTime++;
        }
    }

    private void goToRoom(List<Coordinate> parentTiles) {
        Collections.reverse(parentTiles);
        parentTiles.add(grid.getRoom());
        if (parentTiles.size() > 1) parentTiles.remove(0);
        while (!currentPosition.equals(roomPos)){
            setForbiddenTile(grid.getBug(currentTime+1));
            Coordinate previousMove = parentTiles.get(0);
            route.add(decideWhereToMove(previousMove));
            if (!previousMove.equals(forbiddenTile)) parentTiles.remove(previousMove);
            currentTime++;
        }
    }

    private List<AdvancedCoordinate> sortTheTiles(List<AdvancedCoordinate> advancedCoordinates) {
        List<AdvancedCoordinate> stepList = new ArrayList<>();
        for (AdvancedCoordinate examined : advancedCoordinates) {
            if (isAdjacentTile(examined)) {
                stepList.add(examined);
            }
        }
        return stepList;
    }

    private void stopAtTheKitchen() {
        for (int i = 0; i < 5; i++) {
            addRoute(move("p", currentPosition));
        }
        currentTime += 5;
    }

    private Instruction decideWhereToMove(Coordinate simplestTile) {
        if (!forbiddenTile.equals(simplestTile)) {
            if (isTheTileEast(simplestTile)) {
                setCurrentPosition(simplestTile);
                return move("e", simplestTile);
            } else if (isTheTileWest(simplestTile)) {
                setCurrentPosition(simplestTile);
                return move("w", simplestTile);
            } else if (isTheTileSouth(simplestTile)) {
                setCurrentPosition(simplestTile);
                return move("s", simplestTile);
            } else {
                setCurrentPosition(simplestTile);
                return move("n", simplestTile);
            }
        } else {
            return move("p", simplestTile);
        }
    }

    private boolean isTheTileSouth(Coordinate simplestTile) {
        return simplestTile.getY() > currentPosition.getY();
    }

    private boolean isTheTileWest(Coordinate simplestTile) {
        return simplestTile.getX() < currentPosition.getX();
    }

    private boolean isTheTileEast(Coordinate simplestTile) {
        return simplestTile.getX() > currentPosition.getX();
    }

    private Instruction move(String e, Coordinate simplestTile) {
        switch (e){
            case "e" :
                Instruction.EAST.execute(simplestTile);
                return Instruction.EAST;
            case "w" :
                return Instruction.WEST;
            case "n" :
                Instruction.NORTH.execute(simplestTile);
                return Instruction.NORTH;
            case "s" :
                Instruction.SOUTH.execute(simplestTile);
                return Instruction.SOUTH;
            default :
                Instruction.PAUSE.execute(currentPosition);
                return Instruction.PAUSE;
        }
    }

    private boolean isAdjacentTile(AdvancedCoordinate examined) {
        boolean examination = (currentPosition != examined.getCoordinate() &&
                (currentPosition.getX() == examined.getX() ^ currentPosition.getY() == examined.getY())) &&
                (currentPosition.getX()-1 == examined.getX() || currentPosition.getX()+1 == examined.getX()) ^
                (currentPosition.getY()-1 == examined.getY() || currentPosition.getY()+1 == examined.getY());
        return examination;
    }

    private List<AdvancedCoordinate> convertGrid() {
        List<AdvancedCoordinate> advancedCoordinates = new ArrayList<>();
        Set <Coordinate> forbiddenCoordinates = grid.getHoles();
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                Coordinate coordinate = new Coordinate(j, i);
                if (!forbiddenCoordinates.contains(coordinate)) {
                    AdvancedCoordinate coord = new AdvancedCoordinate(kitchenPos, coordinate);
                    advancedCoordinates.add(coord);
                }
            }
        }
        return advancedCoordinates;
    }

    private void addRoute(Instruction direction) {
        this.route.add(direction);
    }

    private void setCurrentPosition(Coordinate currentPosition) {
        this.currentPosition = currentPosition;
    }

    private void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    private void setKitchenPos(Coordinate kitchenPos) {
        this.kitchenPos = kitchenPos;
    }

    private void setRoomPos(Coordinate roomPos) {
        this.roomPos = roomPos;
    }

    private void setForbiddenTile(Coordinate forbiddenTile) {
        this.forbiddenTile = forbiddenTile;
    }

    private void setGrid(Grid grid) {
        this.grid = grid;
    }
}