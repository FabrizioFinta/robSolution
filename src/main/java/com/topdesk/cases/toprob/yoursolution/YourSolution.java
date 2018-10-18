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

    @Override
    public List<Instruction> solve(Grid grid, int time) {

        if (time < 0) throw new IllegalArgumentException();

        Coordinate kitchenPos = grid.getKitchen();
        Coordinate currentPosition = grid.getRoom();

        List<AdvancedCoordinate> advancedCoordinates = convertGrid(grid, kitchenPos);

        Collections.sort(advancedCoordinates, new CompareAdvancedCoordinates());

        List<Instruction> wayToKitchen = goToTheKitchen(currentPosition, kitchenPos, advancedCoordinates, grid, time);

        List<Instruction> wayToKitchenAndBack = wayToKitchen;

        return wayToKitchenAndBack;
    }

    private List<Instruction> goToTheKitchen(Coordinate currentPosition, Coordinate kitchenPos, List<AdvancedCoordinate> advancedCoordinates, Grid grid, int currentTime) {
        List<Instruction> wayToKitchen = new ArrayList<>();
        List<Coordinate> parentTiles = new ArrayList<>();
        Coordinate forbiddenTile;

        while (!currentPosition.equals(kitchenPos)) {
            forbiddenTile = grid.getBug(currentTime+1);
            List<AdvancedCoordinate> stepList = sortTheTiles(advancedCoordinates, currentPosition);
            Coordinate simplestTile = stepList.get(0).getCoordinate();
            wayToKitchen.add(decideWhereToMove(forbiddenTile, simplestTile, currentPosition));
            if (!forbiddenTile.equals(simplestTile)) currentPosition = simplestTile;
            if (wayToKitchen.iterator().hasNext() && wayToKitchen.get(wayToKitchen.size()-1) != Instruction.PAUSE) parentTiles.add(currentPosition);
            currentTime++;
        }

        wayToKitchen = stopAtTheKitchen(wayToKitchen, currentPosition);
        currentTime += 5;
        Collections.reverse(parentTiles);
        parentTiles.add(grid.getRoom());
        if (parentTiles.size() > 1) parentTiles.remove(0);

        while (!currentPosition.equals(grid.getRoom())){
            forbiddenTile = grid.getBug(currentTime+1);
            Coordinate previousMove = parentTiles.get(0);
            wayToKitchen.add(decideWhereToMove(forbiddenTile, previousMove, currentPosition));
            if (!forbiddenTile.equals(previousMove)) {
                currentPosition = previousMove;
                parentTiles.remove(previousMove);
            }
            currentTime++;
        }
        return wayToKitchen;
    }


    private List<AdvancedCoordinate> sortTheTiles(List<AdvancedCoordinate> advancedCoordinates, Coordinate currentPosition) {
        List<AdvancedCoordinate> stepList = new ArrayList<>();
        for (AdvancedCoordinate examined : advancedCoordinates) {
            if (isAdjacentTile(currentPosition, examined)) {
                stepList.add(examined);
            }
        }
        return stepList;
    }

    private Instruction decideWhereToMove(Coordinate forbiddenTile, Coordinate simplestTile, Coordinate currentPosition) {
        if (!forbiddenTile.equals(simplestTile)) {
            if (simplestTile.getX() > currentPosition.getX()) {
                return move("e", simplestTile, currentPosition);
            } else if (simplestTile.getX() < currentPosition.getX()) {
                return move("w", simplestTile, currentPosition);
            } else if (simplestTile.getY() > currentPosition.getY()) {
                return move("s", simplestTile, currentPosition);
            } else {
                return move("n", simplestTile, currentPosition);
            }
        } else {
            return move("p", simplestTile, currentPosition);
        }
    }

    private Instruction move(String e, Coordinate simplestTile, Coordinate currentPosition) {
        List<Instruction> directions = new ArrayList<>();
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
                directions.add(Instruction.SOUTH);
                Instruction.SOUTH.execute(simplestTile);
                return Instruction.SOUTH;
            default :
                Instruction.PAUSE.execute(currentPosition);
                return Instruction.PAUSE;
        }
    }

    private List<Instruction> stopAtTheKitchen(List<Instruction> moveList, Coordinate currentPosition) {
        for (int i = 0; i < 5; i++) {
            moveList.add(Instruction.PAUSE);
            Instruction.PAUSE.execute(currentPosition);
        }
        return moveList;
    }

    private boolean isAdjacentTile(Coordinate currentPosition, AdvancedCoordinate examined) {
        boolean examination = (currentPosition != examined.getCoordinate() &&
                (currentPosition.getX() == examined.getX() ^ currentPosition.getY() == examined.getY())) &&
                (currentPosition.getX()-1 == examined.getX() || currentPosition.getX()+1 == examined.getX()) ^
                (currentPosition.getY()-1 == examined.getY() || currentPosition.getY()+1 == examined.getY());
        return examination;
    }

    private List<AdvancedCoordinate> convertGrid(Grid grid, Coordinate kitchen) {
        List<AdvancedCoordinate> advancedCoordinates = new ArrayList<>();
        Set <Coordinate> forbiddenCoordinates = grid.getHoles();
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                Coordinate coordinate = new Coordinate(j, i);
                if (!forbiddenCoordinates.contains(coordinate)) {
                    AdvancedCoordinate coord = new AdvancedCoordinate(kitchen, coordinate);
                    advancedCoordinates.add(coord);
                }
            }
        }
        return advancedCoordinates;
    }
}