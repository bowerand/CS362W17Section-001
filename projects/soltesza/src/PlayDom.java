
public class PlayDom {
	public static void main(String args[]) {
		DominionBoard board = new DominionBoard();
		GameState state = new GameState(board);

		while(true) {
			state.TakeTurn(board);
			if(board.GameOver()) {
				break;
			}
		}
		//game is now over. Figure out who won
		System.out.println("Finished game.");
		state.PrintScore();
		
		System.exit(0);
	}
}
