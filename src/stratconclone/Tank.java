/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

import java.util.ArrayList;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author matthew
 */
public class Tank extends Unit {

    public void actionNextStepRequest(Grid grid, Cell nextStep, int AttacksLeftToday, boolean isEnemyOrUnoccupiedCity, boolean isEnemyUnit) {

        if (isEnemyOrUnoccupiedCity) {
            System.out.println("isEnemyOrUnoccupiedCity at nextStep? true");
        }

        setMovesLeftToday(getMovesLeftToday() - 1);
        grid.clearFogOfWar(getPlayerId(), nextStep.getRow(), nextStep.getCol());

        if (isEnemyUnit) {
            System.out.println("enemy unit to attack at " + nextStep.getRow() + "," + nextStep.getCol());
            if (AttacksLeftToday > 1) {
                System.out.println("unit has attacks left today");
                setAttacksLeftToday(getAttacksLeftToday() - 1);
                //grid.clearFogOfWar(getPlayerId(), nextStep.getRow(), nextStep.getCol());
                boolean attackSuccessful = UnitList.getAttackOutcome(nextStep);
                System.out.println("enemy unit attack outcome=" + attackSuccessful + " for player " + getPlayerId());
                if (attackSuccessful) {
                    UnitList.kill(nextStep);

                    // move unit to destination
                    setRow(nextStep.getRow());
                    setCol(nextStep.getCol());
                    setMovesLeftToday(0);
                }
            }
        } else if (isEnemyOrUnoccupiedCity) {
            System.out.println("enemy city to attack at " + nextStep.getRow() + "," + nextStep.getCol());
            if (AttacksLeftToday > 1) {
                System.out.println("unit has attacks left today");
                setAttacksLeftToday(getAttacksLeftToday() - 1);
                //grid.clearFogOfWar(getPlayerId(), nextStep.getRow(), nextStep.getCol());
                boolean attackSuccessful = CityList.getAttackOutcome(nextStep);
                System.out.println("city attack outcome=" + attackSuccessful + " for player " + getPlayerId());
                if (attackSuccessful) {

                    CityList.setOwner(getPlayerId(), nextStep);

                    // move unit to destination
                    setRow(nextStep.getRow());
                    setCol(nextStep.getCol());

                    setMovesLeftToday(0);
                    UnitList.kill(nextStep);
                }
            }
        } else {
            // move unit to destination
            setRow(nextStep.getRow());
            setCol(nextStep.getCol());
        }

    }

    @Override
    public void moveAI(Grid grid) {

        if (getRole() == Role.ASLEEP) {
            // TODO - wake up if enemy nearby
            setMovesLeftToday(0);
        } else {

            if ((getMoveToRow() == -1 && getMoveToCol() == -1)
                    || (getMoveToRow() == getRow() && getMoveToCol() == getCol())) {

                Cell moveTowards = this.identifyNextMoveTypeSpecificAI(grid);
                updateMoveTo(moveTowards);

                Cell nextStep = getNextStepMoveTowards(moveTowards);
                actionNextStepRequest(
                        grid,
                        nextStep,
                        getAttacksLeftToday(),
                        CityList.isEnemyOrUnoccupiedCity(getPlayerId(), nextStep),
                        UnitList.isEnemyUnitAt(getPlayerId(), nextStep)
                );

            } else {

                Cell moveTowards = new Cell();
                moveTowards.setRow(getMoveToRow());
                moveTowards.setCol(getMoveToCol());

                Cell nextStep = getNextStepMoveTowards(moveTowards);
                actionNextStepRequest(
                        grid,
                        nextStep,
                        getAttacksLeftToday(),
                        CityList.isEnemyOrUnoccupiedCity(getPlayerId(), nextStep),
                        UnitList.isEnemyUnitAt(getPlayerId(), nextStep)
                );
            }

            //moveTo(grid, moveTo);
            /*
             switch (getTask()) {
             case NONE:
             identifyNextMoveTypeSpecificAI(grid);
             break;
             case MOVETO:
             // TODO
             break;
             case MOVETOTRANSPORT:
             // TODO
             break;
             }
             */
        }
    }

    private Cell identifyNextMoveTypeSpecificAI(Grid grid) {

        ArrayList possibleMoves = new ArrayList();
        int r, c;
        Cell moveTo, location;
        location = new Cell();

        // any enemy units to attack?
        for (r = max(0, getRow() - 2); r < min(getRow() + 2, grid.getRows() - 1); r++) {
            for (c = max(0, getCol() - 2); c < min(getCol() + 2, grid.getCols() - 1); c++) {
                if (r != getRow() && c != getCol()) {
                    location.setRow(r);
                    location.setCol(c);
                    if (grid.isLand(r, c) && UnitList.isEnemyUnitAt(getPlayerId(), location)) {
                        Cell cell = new Cell();
                        cell.setRow(r);
                        cell.setCol(c);
                        possibleMoves.add(cell);
                        System.out.println("player " + getPlayerId() + " can attack unit");
                    }
                }
            }
        }

        // can unit attack a city?
        if (possibleMoves.isEmpty()) {
            for (r = max(0, getRow() - 2); r < min(getRow() + 2, grid.getRows() - 1); r++) {
                for (c = max(0, getCol() - 2); c < min(getCol() + 2, grid.getCols() - 1); c++) {
                    if (r != getRow() && c != getCol()) {
                        location.setRow(r);
                        location.setCol(c);
                        if (grid.isLand(r, c) && CityList.isEnemyOrUnoccupiedCity(getPlayerId(), location)) {
                            Cell cell = new Cell();
                            cell.setRow(r);
                            cell.setCol(c);
                            possibleMoves.add(cell);
                            System.out.println("player " + getPlayerId() + " can attack city");
                        }
                    }
                }
            }
        }

        switch (getRole()) {

            case EXPLORE:

                //TODO should AI search all island cells?
                // can unit clear fog?
                if (possibleMoves.isEmpty()) {
                    for (r = max(0, getRow() - 3); r < min(getRow() + 3, grid.getRows() - 1); r++) {
                        for (c = max(0, getCol() - 3); c < min(getCol() + 3, grid.getCols() - 1); c++) {
                            if (r != getRow() && c != getCol()) {
                                location.setRow(r);
                                location.setCol(c);
                                if (grid.isLand(r, c) && grid.isFogOfWar(getPlayerId(), location)) {
                                    Cell cell = new Cell();
                                    cell.setRow(r);
                                    cell.setCol(c);
                                    possibleMoves.add(cell);
                                }
                            }
                        }
                    }
                }

                // else can unit move at random?
                if (possibleMoves.isEmpty()) {
                    for (r = max(0, getRow() - 2); r < min(getRow() + 2, grid.getRows() - 1); r++) {
                        for (c = max(0, getCol() - 2); c < min(getCol() + 2, grid.getCols() - 1); c++) {
                            if (r != getRow() && c != getCol()) {

                                if (grid.isLand(r, c)) {
                                    Cell cell = new Cell();
                                    cell.setRow(r);
                                    cell.setCol(c);
                                    possibleMoves.add(cell);
                                }
                            }
                        }
                    }
                }

                break;
            case DEFEND:
                // TODO
                break;
            case PATROL:
                // TODO
                break;
            case TRANSPORTABLE:
                // TODO
                break;
        }

        if (possibleMoves.size() > 0) {
            //println("unit AI move #1");
            int possibleMoveIndex;
            if (possibleMoves.size() == 1) {
                possibleMoveIndex = 0;
            } else {
                possibleMoveIndex = Util.random(0, possibleMoves.size() - 1);
            }
            //println("unit AI move #2");
            moveTo = (Cell) possibleMoves.get(possibleMoveIndex);
            //println("unit AI move #3");
            //moveTo(grid, cell.getRow(), cell.getCol());
            return moveTo;
            //println("unit AI move #4");
        } else {
            System.out.println("note: move AI has not identified a move for unit at " + getRow() + "," + getCol() + ".");

            this.setMovesLeftToday(0);
            moveTo = new Cell();
            moveTo.setRow(-1);
            moveTo.setCol(-1);
            return moveTo;
        }
    }

}
