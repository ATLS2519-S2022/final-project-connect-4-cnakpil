/**
 * A Connect-4 player that makes the best move looking ahead in the time available.
 * 
 * @author Celine Nakpil (celine.nakpil@colorado.edu)
 *
 */
public class MinMaxPlayer implements Player
{
	/**
	 * integer identifier for the player
	 */
	int id;
	
	/**
	 * integer identifier for opponent
	 */
	int opp_id;
	
	/**
	 * number of columns on the game board
	 */
	int col;
	
    @Override
    public String name() {
        return "MinMax";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int col) {
    	this.id = id; // opponent's id is 3-id
    	this.col = col;
    	opp_id = 3 - id;
    }

    @Override
    public void calcMove(Connect4Board board, int oppMoveCol, Arbitrator arb) throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }

        int maxDepth = 1;
        int[] score = new int [col];
        
        while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
        	for (int c = 0; c < col; c++) { 
            	if(board.isValidMove(c)) {
            		board.move(c, id);
            		score[c] = minmax(board, maxDepth-1, false, arb);
            		board.unmove(c, id);
            	}else score[c] = -1000;
            }
        	
        	int bestCol = 0;
            double max = score[0];
            for(int i = 0; i < col; i++) {
            	if(score[i] > max) {
            		bestCol = i;
            		max = score[i];
            	}
            }
        	arb.setMove(bestCol);
        	
        	maxDepth++;
        }
    }
    
    /**
     * Calculates the best move, taking into account future possible moves via recursion
     * 
     * @param board Connect-4 game board
     * @param depth how far into the future
     * @param isMaxing boolean controlling recursive function
     * @param arb handles communication between board and player
     * @return best possible score (column) as int value
     */
    public int minmax(Connect4Board board, int depth, boolean isMaxing, Arbitrator arb) {
    	
    	// if depth is 0, the board is full, or decision time length is up, return the score of the current move
    	if(depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
    		return score(board);
    	}
    	
    	if (isMaxing) {
    		int bestScore = -1000;
    		for(int c = 0; c < col; c++) { //for each possible next move
    			if (board.isValidMove(c)) {
    				board.move(c, id);
    				bestScore = Math.max(bestScore, minmax(board, depth - 1, false, arb));
    				board.unmove(c, id);
    			}
    		}
    		return bestScore;
    	}else {
			int bestScore = 1000;
			for(int c = 0; c < col; c++) { //for each possible next move
				if (board.isValidMove(c)) {
					board.move(c, opp_id);
					bestScore = Math.min(bestScore, minmax(board, depth - 1, true, arb));
					board.unmove(c, opp_id);
				}
			}
		return bestScore;
		}
    }
    
    /**
     * Returns the score of the current move being tested using class global var id and opp_id.
     * 
     * @param board Connect-4 game board 
     * @return score of current move
     */
    public int score(Connect4Board board) {
    	return calcScore(board, id) - calcScore(board, opp_id);
    }
    
    /**
     * Calculates score of the current possible move.
     * 
     * @param board Connect-4 game board
     * @param id identifying int of this player
     * @return returns int score of move 
     */
    public int calcScore(Connect4Board board, int id) {
    	final int row = board.numRows();
    	final int col = board.numCols();
    	int score = 0;
    	
    	// Look for horizontal connect-4s.
    	for (int r = 0; r < row; r++) {
    		for (int c = 0; c<= col -4; c++) {
    			if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
    		}
    	}
    	
    	// Look for vertical connect-4s.
    	for (int c = 0; c < col; c++) {
    		for (int r = 0; r <= row -4; r++) {
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
    
   
