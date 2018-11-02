package com.topdesk.cases.toprob.yoursolution;

import java.util.*;

import com.topdesk.cases.toprob.Coordinate;
import com.topdesk.cases.toprob.Grid;
import com.topdesk.cases.toprob.Instruction;
import com.topdesk.cases.toprob.Solution;

public class YourSolution implements Solution {

    private int counter;
    private int bugTime;
    private Grid grid;

    private Coordinate currentPosition;
    private Coordinate kitchenPos;
    private Coordinate roomPos;
    private Coordinate forbiddenTile;

    private List<AdvancedCoordinate> advancedCoordinates;
    private List<Instruction> route = new ArrayList<>();

    @Override
    public List<Instruction> solve(Grid grid, int time) {
        if (time < 0) throw new IllegalArgumentException();

        if (counter > 0) {
            YourSolution copy = new YourSolution();
            return copy.solve(grid, time);
        } else {
            setBugTime(time + 1);
            setGrid(grid);
            setKitchenPos(grid.getKitchen());
            setRoomPos(grid.getRoom());
            setCurrentPosition(roomPos);

            advancedCoordinates = convertGrid();

            findTheRoute();

            counter ++;
            return route;
        }
    }


    private void findTheRoute() {
        AStarCalculation aStarCalculation = new AStarCalculation(advancedCoordinates, roomPos, kitchenPos);
        aStarCalculation.aStarCalculate();

        List<AdvancedCoordinate> parentTiles = aStarCalculation.getRoute();

        goToKitchen(parentTiles);
        stopAtTheKitchen();
        goToRoom(parentTiles);
    }

    private void goToKitchen(List<AdvancedCoordinate> parentTiles) {
        int loopCount = 1;
        while (!amIInTheKitchen(currentPosition)) {
            setForbiddenTile(grid.getBug(bugTime));
            if (addRoute(decideWhereToMove(parentTiles.get(loopCount).getCoordinate()))) {
                loopCount++;
            }
            bugTime++;
        }
    }

    private void goToRoom(List<AdvancedCoordinate> parentTiles) {
        Collections.reverse(parentTiles);
        parentTiles.remove(0);
        while (!amIInTheRoom(currentPosition)) {
            setForbiddenTile(grid.getBug(bugTime));
            Coordinate previousMove = parentTiles.get(0).getCoordinate();
            if (addRoute(decideWhereToMove(previousMove))) {
                parentTiles.remove(0);
            }
            bugTime++;
        }
    }

    private void stopAtTheKitchen() {
        for (int i = 0; i < 5; i++) {
            addRoute(move("p", currentPosition));
        }
        bugTime += 5;
    }

    private Instruction decideWhereToMove(Coordinate targetTile) {
        if (forbiddenTile.equals(targetTile)) {
            return move("p", targetTile);
        } else {
            if (isTheTileEast(targetTile)) {
                currentPosition = targetTile;
                return move("e", targetTile);
            } else if (isTheTileWest(targetTile)) {
                currentPosition = targetTile;
                return move("w", targetTile);
            } else if (isTheTileSouth(targetTile)) {
                currentPosition = targetTile;
                return move("s", targetTile);
            } else {
                currentPosition = targetTile;
                return move("n", targetTile);
            }
        }
    }

    private boolean isTheTileSouth(Coordinate targetTile) {
        return targetTile.getY() > currentPosition.getY();
    }

    private boolean isTheTileWest(Coordinate targetTile) {
        return targetTile.getX() < currentPosition.getX();
    }

    private boolean isTheTileEast(Coordinate targetTile) {
        return targetTile.getX() > currentPosition.getX();
    }

    private Instruction move(String e, Coordinate targetTile) {
        switch (e) {
            case "e":
                Instruction.EAST.execute(targetTile);
                return Instruction.EAST;
            case "w":
                Instruction.WEST.execute(targetTile);
                return Instruction.WEST;
            case "n":
                Instruction.NORTH.execute(targetTile);
                return Instruction.NORTH;
            case "s":
                Instruction.SOUTH.execute(targetTile);
                return Instruction.SOUTH;
            default:
                Instruction.PAUSE.execute(currentPosition);
                return Instruction.PAUSE;
        }
    }

    private boolean amIInTheKitchen(Coordinate currentPosition) {
        return currentPosition.equals(kitchenPos);
    }

    private boolean amIInTheRoom(Coordinate currentPosition) {
        return currentPosition.equals(roomPos);
    }

    private List<AdvancedCoordinate> convertGrid() {
        List<AdvancedCoordinate> advancedCoordinates = new ArrayList<>();
        Set<Coordinate> forbiddenCoordinates = grid.getHoles();
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                Coordinate coordinate = new Coordinate(j, i);
                if (!forbiddenCoordinates.contains(coordinate) && !coordinate.equals(currentPosition)) {
                    AdvancedCoordinate tile = new AdvancedCoordinate(kitchenPos, coordinate);
                    advancedCoordinates.add(tile);
                }
            }
        }
        return advancedCoordinates;
    }

    private boolean addRoute(Instruction direction) {
        this.route.add(direction);
        if (direction == Instruction.PAUSE) {
            return false;
        } else {
            return true;
        }
    }

    private void setCurrentPosition(Coordinate currentPosition) {
        this.currentPosition = currentPosition;
    }

    private void setBugTime(int bugTime) {
        this.bugTime = bugTime;
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