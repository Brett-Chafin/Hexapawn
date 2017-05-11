/**
 * Created by brettchafin on 4/19/17.
 */

import java.util.*;
import java.io.*;

public class Hexapawn {
    char[][] initBoard;
    char sideOnMove;
    int boardWidth;
    int boardHeight;
    int recursionLevel = 0;
    int movesTried = 0;

    Hexapawn(char[][] board, char sideonMove){
        initBoard = board;
        sideOnMove = sideonMove;
        boardWidth = initBoard.length;
        boardHeight = initBoard[0].length;

        //error check
        if(boardHeight != boardWidth) {
            System.err.println("Width/Height mismatch");
            System.exit(-1);
        }

        //print out board for inspection
        System.err.println("Board created with: ");
        System.err.println(sideOnMove);
        printBoard(initBoard);

    }


    /******************* The big daddy function ****************/
    int solve(char[][] currentBoard) {

        System.err.println();
        System.err.println("R-Level: " + recursionLevel);
        System.err.println(sideOnMove);
        printBoard(currentBoard);

        //generate all possible moves for the current board
        ArrayList<Move> moveList = genLegalMoves(currentBoard);
        //printMoveList(moveList);

        //if i dont have any moves, i've lost
        if(moveList.size() == 0) {
            System.err.println(sideOnMove + " Ran out of moves");
            return -1;
        }

        int maxValue = -1;
        int val;

        //run through all moves in the mostList
        for(int i = 0; i < moveList.size(); i++){

            //try this move
            char[][] newBoard = makeMove(currentBoard, moveList.get(i));
            System.err.println();
            System.err.println("Trying move: ");
            System.err.println(sideOnMove);
            printBoard(newBoard);
            System.err.println();

            //if this board is a winner
            if(winningBoard(newBoard)){
                System.err.println("We found a win for: " + sideOnMove);
                //printBoard(newBoard);
                return 1;
            }
            else {
                //toggles sideOnMove
                switchSides();

                //to to solve this new board
                recursionLevel++;
                val = - solve(newBoard);
                switchSides();
                recursionLevel--;
                if(val == 1) return 1;

                if(i != moveList.size() - 1) {
                    System.err.println(sideOnMove + " Is trying a new approach");
                }
            }

            maxValue = Math.max(maxValue, val);

        }


        return maxValue;
    }



    /******************** Move Maker ********************/
    private char[][] makeMove(char[][] oldBoard, Move move) {

        movesTried++;

        char[][] newBoard = new char[oldBoard.length][oldBoard[0].length];

        //deep copy
        for(int i = 0; i < oldBoard.length; i++ ){
            for(int j = 0; j < oldBoard[0].length; j++) {
                newBoard[i][j] = oldBoard[i][j];
            }
        }

        int sX = move.src[0];
        int sY = move.src[1];
        int dX = move.dest[0];
        int dY = move.dest[1];

        char toMove = newBoard[sX][sY];
        char dest = newBoard[dX][dY];

        //sanity check
        if(toMove != 'p' && toMove != 'P'){
            System.err.println("Invalid source for move");
            System.exit(-1);
        }

        if(toMove == dest) {
            System.err.println("Tried to attack your own piece");
            System.exit(-1);
        }

        //remove piece from source
        newBoard[sX][sY] = '.';

        //replace destination with the moving piece
        newBoard[dX][dY] = toMove;

        return newBoard;
    }


    /**************** Big dadddy helper ******************/
    private ArrayList<Move> genLegalMoves(char[][] currentBoard) {
        ArrayList<Move> moveList = new ArrayList<Move>();

        //if its whites turn
        if(sideOnMove == 'W') {

            //run through the board and find the white pieces
            for(int i = 0; i < boardHeight; i++){
                for(int j = 0; j < boardWidth; j++){
                    if(currentBoard[i][j] == 'P'){
                        /**White will always be on the bottom trying to move up,
                         * so we only have three options for moves. Move up, attack right, attack left
                         */
                        //move up
                        if(i > 0 && currentBoard[i-1][j] == '.'){

                            //create this new possible move with src/dest cords
                            Move moveUp = new Move(i, j, i-1, j);
                            moveList.add(moveUp);
                        }

                        //attack right
                        if (i > 0 && j < boardWidth - 1 && currentBoard[i - 1][j + 1] == 'p') {
                            Move attackRight = new Move(i, j, i - 1, j + 1);
                            moveList.add(attackRight);
                        }


                        //attack left
                        if (i > 0 && j > 0  && currentBoard[i - 1][j - 1] == 'p') {
                            Move attackLeft = new Move(i, j, i - 1, j - 1);
                            moveList.add(attackLeft);
                        }
                    }
                }
            }

            return moveList;
        }

        if(sideOnMove == 'B') {

            //run through the board and find the black pieces
            for(int i = 0; i < boardHeight; i++){
                for(int j = 0; j < boardWidth; j++){
                    if(currentBoard[i][j] == 'p'){
                        //move down
                        if(i < boardHeight && currentBoard[i+1][j] == '.'){

                            //create this new possible move with src/dest cords
                            Move moveDown = new Move(i, j, i+1, j);
                            moveList.add(moveDown);
                        }

                        //attack right
                        if (i < boardHeight && j < boardWidth - 1 && currentBoard[i + 1][j + 1] == 'P') {
                            Move attackRight = new Move(i, j, i + 1, j + 1);
                            moveList.add(attackRight);
                        }

                        //attack left
                        if (i < boardHeight && j > 0  && currentBoard[i + 1][j - 1] == 'P') {
                            Move attackLeft = new Move(i, j, i + 1, j - 1);
                            moveList.add(attackLeft);
                        }
                    }
                }
            }
            return moveList;
        }
        //TODO: Else Statment Incorrect
        else
            System.out.println("sideOnMove incorrect value! ");
        System.exit(-1);

        return null;

    }



    /******************** Main function *******************/
    public static void main(String[] args) {

       System.err.println("Test");

       char[][] inputBoard;
       char sideOnMove;

        /*********** for stdn inpud************

        Scanner scan = new Scanner(System.in);
        String lineOne = scan.nextLine();
        String lineTwo = scan.nextLine();
        int boardSize = lineTwo.length();
        inputBoard = new char[boardSize][boardSize];
        char[] firstRow = lineTwo.toCharArray();

        for(int i = 0; i < boardSize; i++){
            inputBoard[i][0] = firstRow[i];
        }

        for(int i = 1; i < boardSize; i++){
            char[] input = scan.nextLine().toCharArray();
            inputBoard[i][0] = firstRow[i];
        }


        sideOnMove = lineOne.split("")[0].toCharArray()[0];

        Hexapawn hexSolver = new Hexapawn(inputBoard, sideOnMove);
        int result = hexSolver.solve(hexSolver.initBoard);
        System.err.println("Result: " + result);
        System.out.println(result);
        System.err.println("Moves Tried: " + hexSolver.movesTried);


         *************/


       try {
           FileInputStream stream = new FileInputStream("tests.txt");
           int input = 0;

           int counter = 0;
           int squares = 6; //(stream.available() - 2) / 3;
           int boardWidth = squares;
           int boardHeight = squares;
           inputBoard = new char[squares][squares];
           if((input = stream.read()) != -1){
               //System.out.print((char)input);

               sideOnMove = (char)input;

               for(int i = 0; i < boardHeight; i++ ){
                   for(int j = 0; j < boardWidth; j++) {
                       if((input = stream.read()) != -1) {
                           if((char)input == '\n'){
                               j--;
                               continue;
                           }
                           inputBoard[i][j] = (char)input;
                       }
                   }
               }

               Hexapawn hexSolver = new Hexapawn(inputBoard, sideOnMove);
               int result = hexSolver.solve(hexSolver.initBoard);
               System.err.println("Result: " + result);
               System.out.println(result);
               System.err.println("Moves Tried: " + hexSolver.movesTried);
           }

       }catch(Exception e){System.out.println(e);}

    }

    public void printMoveList(ArrayList<Move> moveList) {
        if(moveList.size() == 0) {
            System.err.println("MoveList empty");
        }

        for(int i = 0; i < moveList.size(); i++){
            moveList.get(i).printMove();
        }
    }

    private void switchSides(){
        if(sideOnMove == 'W') {
            sideOnMove = 'B';
        }
        else if(sideOnMove == 'B'){
            sideOnMove = 'W';
        }
        else{
            System.err.println("Side on move incorrect!");
            System.exit(-1);
        }
    }
    /**************** End of Main ****************/


    //checks to see if someone was won this board
    private boolean winningBoard(char[][] board){
        for(int i = 0; i < boardHeight; i++ ){
            for(int j = 0; j < boardWidth; j++) {

                //if white was made it to the other side
                if(board[i][j] == 'P' && i == 0){
                    return true;
                }

                //if black has made it to the other side
                if(board[i][j] == 'p' && i == boardHeight -1){
                    return true;
                }
            }
        }
        //no winners
        return false;
    }

    public static void printBoard(char[][] board){
        int width = board.length;
        int height  = board[0].length;

        for(int i = 0; i < height; i++ ){
            for(int j = 0; j < width; j++) {
                System.err.print(board[i][j]);
            }
            System.err.println();
        }
    }

}
