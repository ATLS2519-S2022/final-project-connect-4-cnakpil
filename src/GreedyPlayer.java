/**
 * A Connect-4 player that makes the best current move.
 * 
 * @author Celine Nakpil (celine.nakpil@colorado.edu)
 *
 */
public class GreedyPlayer implements Player
{
	int id;
	int col;
	
    @Override
    public String name() {
        return "Greedy";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int col) {
    	this.id = id; // opponent's id is 3-id
    	this.col = col;
    }

    @Override
    public void calcMove(Connect4Board board, int oppMoveCol, Arbitrator arb) throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        
        // define variables
        int[] score = new int [col]; //total number
        double max = score[0];
        int maxCol = 0;
        
        //calculate scores for each move, find move with highest number of points
        for (int c = 0; c < col; c++) { 
        	if(board.isValidMove(c)) {
        		board.move(c, id);
        		score[c] = calcScore(board, id) - calcScore(board, 3 - id);
        		board.unmove(c,  id);
        	}
        	else score[c] = -1000;
        }
        
        for(int i = 0; i < score.length; i++) {
        	if(score[i] > max) {
        		maxCol = i;
        		max = score[i];
        	}
        }
        arb.setMove(maxCol);
    }
    
    /**
     * Calculates score of the current possible move
     * 
     * @param board connect 4 game board
     * @param id int of move
     * @return returns int score of move 
     */
	public int calcScore(Connect4Board board, int id)
	{
		final int row = board.numRows();
		final int col = board.numCols();
		int score = 0;
		
		// Look for horizontal connect-4s.
		for (int r = 0; r < row; r++) {
			for (int c = 0; c <= col - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		
		// Look for vertical connect-4s.
		for (int c = 0; c < col; c++) {
			for (int r = 0; r <= row - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		
		// Look for diagonal connect-4s.
		for (int c = 0; c <= col - 4; c++) {
			for (int r = 0; r <= row - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= col - 4; c++) {
			for (int r = row - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}

}
