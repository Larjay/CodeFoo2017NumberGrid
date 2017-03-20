
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Number Grid which handles the logic of finding chains
 * and the background grid data
 */
public class NumberGrid {
	
    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_MAX = 9;

    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE = 1;
    private static final int INDEX_TWO = 2;

    private static final int DEFAULT_GRID_SIZE = 3;
    private static final char ENTER_VAL = 'e';
    private static final String QUIT_VAL = "q";
    
    private int mWidth;
    private int mHeight;
    private int mArea;
    private int mGrid[][];

    private int mMinChainLength;

    private ArrayList<NumberChain> mNumberChains;
    
    public static void main(String[] args) {

    	int width, height, min, max;
    	String input = "";
    	NumberGrid numberGrid;
    	
    	NumberGrid defaultNumberGrid = new NumberGrid(3, 3);
    	
    	System.out.println("Showing Default Grid\n");
    	defaultNumberGrid.printGrid();
    	defaultNumberGrid.findChains();
    	System.out.println("\nShowing Chains");
    	defaultNumberGrid.printChains();
    	
    	System.out.println();
    	Scanner scanner = new Scanner(System.in);

    	while (!input.contains(QUIT_VAL)) {
    		
    		System.out.println("Try it out with different min and max values!");
        	System.out.println("Enter the min value to use:");
        	try {
        		min = scanner.nextInt();        		
        	} catch (InputMismatchException ime) {
        		System.out.println("Invalid input - setting default min");
        		min = DEFAULT_MIN;
        	}
        	
        	System.out.println("Enter the max value to use:");
        	try {
        		max = scanner.nextInt();        		
        	} catch (InputMismatchException ime) {
        		System.out.println("Invalid input - setting default max");
        		max = DEFAULT_MAX;
        	}
        	
        	numberGrid = new NumberGrid(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE);
        	numberGrid.populateGrid(min, max);

        	numberGrid.printGrid();
        	numberGrid.findChains();
        	numberGrid.printChains();
        	
        	System.out.println("To quit, enter q");
        	input = scanner.next();
    	}
    	
    	    	
    	scanner.close();
    }

    public NumberGrid(int width, int height) {

        mWidth = width;
        mHeight = height;
        mMinChainLength = mWidth - 1;
        mArea = mWidth * mHeight;
        mGrid = new int[mHeight][mWidth];
        mNumberChains = new ArrayList<>();
        // Have a default grid for testing purposes
        // can still call populateGrid(int min, int max)
        staticExampleGrid();
    }

    // Testing with the sample grid
    private void staticExampleGrid() {

        mGrid[0][0] = 9;
        mGrid[0][1] = 4;
        mGrid[0][2] = 6;
        mGrid[1][0] = 8;
        mGrid[1][1] = 1;
        mGrid[1][2] = 0;
        mGrid[2][0] = 3;
        mGrid[2][1] = 7;
        mGrid[2][2] = 2;

    }

    /**
     * Print the grid of values
     */
    public void printGrid() {

        for (int rowIndex = 0; rowIndex < mHeight; rowIndex++) {

            for (int colIndex = 0; colIndex < mWidth; colIndex++) {

                System.out.print(mGrid[rowIndex][colIndex] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * Populates a grid with values between the minimum and maximum
     * <b>Note:</b> 'with' repeating values
     * @param min value of beginning of range
     * @param max value of end of range
     */
    public void populateGrid(int min, int max) {

        double randomVal;

        for (int rowIndex = 0; rowIndex < mHeight; rowIndex++) {

            for (int colIndex = 0; colIndex < mWidth; colIndex++) {

                // Get the random value between the min and max inclusive
                randomVal = Math.random() * (max + 1 - min) + min;
                mGrid[rowIndex][colIndex] = (int) randomVal;
            }
        }
    }

    /**
     * Get the adjacent coordinates given a coordinate's rows and cols
     * <br/>
     * <b>Note</b>: is hardcoded for a 3x3 grid.
     * To not be hardcoded: instead of using constants
     * - should manipulate row and col depending on grid size
     * @param row int of the 0 based row in the grid
     * @param col int of the 0 based col in the grid
     * @return ArrayList of Coordinates adjacent to the given
     */
    public ArrayList<Coordinate> getAdjacents(int row, int col) {

        ArrayList<Coordinate> adjacentCoordinates = new ArrayList<>();

        // top left (0, 0)
        if (row == INDEX_ZERO && col == INDEX_ZERO) {
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ONE));     // (0, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ZERO));     // (1, 0)
        // top middle (0, 1)
        } else if (row == INDEX_ZERO && col == INDEX_ONE) {
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ZERO));    // (0, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ZERO));     // (1, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_TWO));     // (0, 2)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_TWO));      // (1, 2)
        // top right (0, 2)
        } else if (row == INDEX_ZERO && col == INDEX_TWO) {
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ONE));     // (0, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_TWO));      // (1, 2)
        // middle left (1, 0)
        } else if (row == INDEX_ONE && col == INDEX_ZERO) {
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ZERO));    // (0, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ONE));     // (0, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ONE));      // (2, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ZERO));     // (2, 0)
        // middle (1, 1)
        } else if (row == INDEX_ONE && col == INDEX_ONE) {
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ZERO));    // (0, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ONE));     // (0, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_TWO));     // (0, 2)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ZERO));     // (1, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_TWO));      // (1, 2)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ZERO));     // (2, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ONE));      // (2, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_TWO));      // (2, 2)
        // middle right (1, 2)
        } else if (row == INDEX_ONE && col == INDEX_TWO) {
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_ONE));     // (0, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ZERO, INDEX_TWO));     // (0, 2)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ONE));      // (2, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_TWO));      // (2, 2)
        // bottom left (2, 0)
        } else if (row == INDEX_TWO && col == INDEX_ZERO) {
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ONE));      // (2, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ZERO));     // (1, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
        // bottom middle (2, 1)
        } else if (row == INDEX_TWO && col == INDEX_ONE) {
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ZERO));     // (1, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_TWO));      // (1, 2)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ZERO));     // (2, 0)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_TWO));      // (2, 2)
        // bottom right (2, 2)
        } else if (row == INDEX_TWO && col == INDEX_TWO) {
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_ONE));      // (1, 1)
            adjacentCoordinates.add(new Coordinate(INDEX_ONE, INDEX_TWO));      // (1, 2)
            adjacentCoordinates.add(new Coordinate(INDEX_TWO, INDEX_ONE));      // (2, 1)
        }

        return adjacentCoordinates;
    }

    /**
     * Gets the value of the coordinate given the row and col
     * @param row int of the row in the grid
     * @param col int of the col in the grid
     * @return value at the given row and col
     */
    public int getValueAt(int row, int col) {
        return mGrid[row][col];
    }

    /**
     * Get the value of the coordinate given a coordinate
     * @param coordinate coordinate to get the value of
     * @return value of the coordinate
     */
    public int getValueAt(Coordinate coordinate) {
        return mGrid[coordinate.getRow()][coordinate.getCol()];
    }

    /**
     * Finds the number chains for the grid
     * <br/> Stores the data in the NumberGrid object <br/>
     * Can see the data via the printChains method
     */
    public void findChains() {

        ArrayList<Coordinate> adjacentCoords;
        ArrayList<Coordinate> chainedCoords;

        int value, sum;

        // iterate through the rows and cols of the grid - checking each one for chains
        for (int rowIndex = 0; rowIndex < mHeight; rowIndex++) {

            for (int colIndex = 0; colIndex < mWidth; colIndex++) {

                value = getValueAt(rowIndex, colIndex);
                // stop if the value is equal to the area of the grid
                if (value == mArea) {
                    continue;
                }
                // get the adjacent coordinates
                adjacentCoords = getAdjacents(rowIndex, colIndex);
                chainedCoords = new ArrayList<>();
                Coordinate baseCoordinate = new Coordinate(rowIndex, colIndex);
                chainedCoords.add(baseCoordinate);
                sum = value;

                // invoke the add and check for continuing the number chain
                addCheck(adjacentCoords, chainedCoords, sum);

            }

        }

    }

    // Adds to the chain of values
    private int addCheck(ArrayList<Coordinate> adjacentCoords,
                         ArrayList<Coordinate> chainValues, int sum) {

        Coordinate zeroCoordinate = new Coordinate();
        int tempSum;
        int checkValue;

        for (int checkIndex = 0; checkIndex < adjacentCoords.size(); checkIndex++) {

            Coordinate checkCoordinate = adjacentCoords.get(checkIndex);

            // If this chain already has this coordinate go to the next adjacent,
            // as we don't want to repeat
            if (chainValues.contains(checkCoordinate)) {
                continue;
            }

            checkValue = getValueAt(checkCoordinate);
            // Store the sum in a temp if needed to be restored
            tempSum = sum;
            sum += checkValue;


            if (sum > mArea) {
                sum = tempSum;

            } else if (sum == mArea) {
                // Sum is now equal to the area so add the coordinate to the chain
                chainValues.add(checkCoordinate);
                // Add a chain to the list of chains
                mNumberChains.add(new NumberChain(chainValues));
                // Get a list of adjacent coordinates to check if there is a zero nearby
                ArrayList<Coordinate> testAdjacent =
                        getAdjacents(checkCoordinate.getRow(), checkCoordinate.getCol());

                // This checks to see if there is a zero that can be chained after sum is already
                // equal to the area to create another chain which is still valid
                if (testAdjacent.contains(zeroCoordinate)
                        && !chainValues.contains(zeroCoordinate)) {

                    ArrayList<Coordinate> chainWithZero = new ArrayList<>();
                    for (int index = 0; index < chainValues.size(); index++) {
                        chainWithZero.add(chainValues.get(index));
                    }
                    chainWithZero.add(zeroCoordinate);
                    mNumberChains.add(new NumberChain(chainWithZero));
                }

                chainValues.remove(checkCoordinate);
                sum = tempSum;

            } else if (sum < mArea) {
                // Get this coordinate's adjacents
                ArrayList<Coordinate> newAdjacentCoords =
                        getAdjacents(checkCoordinate.getRow(), checkCoordinate.getCol());
                // Add the coordinate to the chain
                chainValues.add(checkCoordinate);

                // Since the sum is still less than the area, check around the new coordinates
                // adjacent values to see if the chain can continue
                sum = addCheck(newAdjacentCoords, chainValues, sum);
                chainValues.remove(checkCoordinate);
                // Since the recursive call can add to the sum, reset back to previous value
                if (sum < mArea) {
                    sum = tempSum;
                }
            }
        }
        return sum;

    }


    /**
     * Prints the number chains that are valid in this grid
     */
    public void printChains() {

        for (int index = 0; index < mNumberChains.size(); index++) {
            mNumberChains.get(index).printChain();
            System.out.println("-----");
        }

    }

    /**
     * Coordinate which stores the row, col and value
     */
    public class Coordinate {

        private int mRow;
        private int mCol;
        private int mValue;

        public Coordinate(int row, int col) {
            mRow = row;
            mCol = col;
            mValue = mGrid[row][col];
        }

        public Coordinate() {
            mRow = -1;
            mCol = -1;
            mValue = 0;
        }

        public int getRow() {
            return mRow;
        }

        public int getCol() {
            return mCol;
        }

        public int getValue() {
            return mValue;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof Coordinate) {
                if (mRow == -1 && mCol == -1) {
                    return ((Coordinate) obj).mValue == mValue;
                } else {
                    return ((Coordinate) obj).mRow == mRow && ((Coordinate) obj).mCol == mCol;
                }

            } else {
                return false;
            }
        }
    }

    /**
     * NumberChain which stores a chain of Coordinates
     */
    public class NumberChain {

        private ArrayList<Coordinate> mChain;

        public NumberChain(ArrayList<Coordinate> chain) {

            mChain = new ArrayList<>();

            for (int index = 0; index < chain.size(); index++) {
                mChain.add(chain.get(index));
            }
        }

        /**
         * Print the chain of numbers
         */
        public void printChain() {

        	if (mChain.size() == 0) {
        		System.out.println("There are no chains :(");
        	}
        	
            for (int index = 0; index < mChain.size(); index++) {
            	
            	System.out.print(mChain.get(index).getValue());
            	
            	if (index < mChain.size() - 1) {

            		System.out.print(" + ");
            	} else {
            		System.out.println(" = " + mArea);
            	}
            	
            }
        }

    }


}
