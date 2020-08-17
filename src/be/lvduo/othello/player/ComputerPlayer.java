package be.lvduo.othello.player;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import be.lvduo.othello.Board;
import be.lvduo.othello.Direction;
import be.lvduo.othello.Piece;

public class ComputerPlayer implements Player {
	
	private static final int INIT_DEPTH = 10;
	
	private Piece color;
	private double difficulty;
	private boolean boss;
	private Piece[] colors;
	
	public ComputerPlayer(Piece color, double difficulty, boolean boss) {
		if(!color.isPiece()) throw new IllegalArgumentException("The color must be a piece not as "+color);
		this.color = color;
		this.colors = new Piece[] {color.getOpposite(), color};
	}

	@Override
	public boolean isHuman() {
		return false;
	}

	@Override
	public Piece getColor() {
		return color;
	}
	
	@Override
	public Point play(Board board) {
		HashMap<Point, List<Direction>> actions = board.getPossibilities(color);
		if(actions.isEmpty())
			return null;
		double maxValue = -200;
		Point maxAction = null;
		for(Entry<Point, List<Direction>> action : actions.entrySet()) {
			Board child = board.clone();
			child.togglePieces(action.getKey(), color, action.getValue());
			double value = this.minimax(child, INIT_DEPTH, -200, 200, false, false);
			if(value > maxValue) {
				maxValue = value;
				maxAction = action.getKey();
			}
		}
		return maxAction;
	}
	

	private double minimax(Board board, int depth, double alpha, double beta, boolean maximizing, boolean isTwice) {
		if(depth == 0)
			return this.evaluate(board);
		
		if(maximizing) {
			double maxEvaluation = -200;
			HashMap<Point, List<Direction>> actions = board.getPossibilities(color);
			// Check if there is no actions available
			if(actions.isEmpty()) {
				if(isTwice) // The other color also can't play
					return this.evaluate(board);
				else // Try with the other color
					return this.minimax(board, depth, alpha, beta, false, true);
			}
			// Else do the usual minimax
			for(Entry<Point, List<Direction>> action : actions.entrySet()) {
				Board child = board.clone();
				child.togglePieces(action.getKey(), this.color, action.getValue());
				double evaluation = this.minimax(child, depth - 1, alpha, beta, false, false);
				maxEvaluation = Math.max(maxEvaluation, evaluation);
				alpha = Math.max(alpha, evaluation);
				if(beta <= alpha)
					break;
			}
			return maxEvaluation;
		} else { // Minimizing
			double minEvaluation = 200;
			HashMap<Point, List<Direction>> actions = board.getPossibilities(color.getOpposite());
			// Check if there is no actions available
			if(actions.isEmpty()) {
				if(isTwice) // The other color also can't play
					return this.evaluate(board);
				else // Try with the other color
					return this.minimax(board, depth, alpha, beta, true, true);
			}
			// Else do the usual minimax
			for(Entry<Point, List<Direction>> action : actions.entrySet()) {
				Board child = board.clone();
				child.togglePieces(action.getKey(), color.getOpposite(), action.getValue());
				double evaluation = this.minimax(child, depth - 1, alpha, beta, true, false);
				minEvaluation = Math.min(minEvaluation, evaluation);
				beta = Math.min(beta, evaluation);
				if(beta <= alpha)
					break;
			}
			return minEvaluation;
		}
	}
	
/*	private double minimax(Board board, int depth, double alpha, double beta, int maximizing, boolean isTwice) {
		if(depth == 0) 
			return this.evaluate(board);
		
		double maxEvaluation = -maximizing * 200;
		HashMap<Point, List<Direction>> actions = board.getPossibilities(colors[(int) ((((double) maximizing) / 2.0) + 0.5)]);
		// Check if there is no actions available
		if(actions.isEmpty()) {
			if(isTwice) // The other color also can't play
				return this.evaluate(board);
			else // Try with the other color
				return this.minimax(board, depth, alpha, beta, -maximizing, true);
		}
		// Else do the usual minimax
		for(Entry<Point, List<Direction>> action : actions.entrySet()) {
			Board child = board.clone();
			child.togglePieces(action.getKey(), colors[(int) ((((double) maximizing) / 2.0) + 0.5)], action.getValue());
			double evaluation = this.minimax(child, depth - 1, alpha, beta, -maximizing, false);
			maxEvaluation = Math.max(maxEvaluation*maximizing, evaluation*maximizing);
			if(maximizing == 1)
				alpha = Math.max(alpha, evaluation);
			else
				beta = Math.min(beta, evaluation);
			if(beta <= alpha)
				break;
		}
		return maxEvaluation;
	}*/
	
	/**
	 * 
	 * @param board
	 * @return the evaluation of the board [-164; 164]
	 */
	private double evaluate(Board board) {
		return board.getNPieces(color) - board.getNPieces(color.getOpposite());
	}
}
