import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;
import java.util.Random;


public class GameState implements Cloneable{
    public List<Player> players = new ArrayList<Player>();
    public List<Card> cards;
	public HashMap<Card, Integer> gameBoard = new HashMap<Card, Integer>();
	public List<Card> embargoTokens;
	Random gen = new Random();

	public GameState(List<Card> cards) {
		   this.cards = cards;
	   }

	public void addPlayer(Player player) {
		      players.add(player);
	   }
	   
	public void initializeGame(){

//CHECK NUMBER OF PLAYERS
        if (players.size() > 4 || players.size() < 2) {
		    System.err.println("the number of players must be between 2 and 4 ");
			return ;
        }
//INITIALIZE SUPPLY
		int selectedKingdom = 0;
		int Kingdom_Cards_Selected = 10;

//INITIALIZE ACTION CARDS
        while (selectedKingdom < Kingdom_Cards_Selected) {
		    int random = gen.nextInt(cards.size());//
			Card tmp = cards.get(random);
			if(tmp.getType() != Card.Type.ACTION) continue;
			if(gameBoard.containsKey(tmp)) continue;
			gameBoard.put(tmp, 10);
			selectedKingdom++;
        }

    //set number of Curse cards the default number of players is 2
		gameBoard.put(Card.getCard(cards, Card.CardName.Curse), 10);
		      
//INITIALIZE VICTORY CARDS
		gameBoard.put(Card.getCard(cards, Card.CardName.Province), 8);
		gameBoard.put(Card.getCard(cards, Card.CardName.Duchy), 8);
		gameBoard.put(Card.getCard(cards, Card.CardName.Estate), 8);

//INITIALIZE TREASURE CARDS
		gameBoard.put(Card.getCard(cards, Card.CardName.Gold), 30);
		gameBoard.put(Card.getCard(cards, Card.CardName.Silver), 40);
		gameBoard.put(Card.getCard(cards, Card.CardName.Copper), 46);

//INITIALIZE PLAYER'S CARDS
		for (Player player : players) {
		    System.out.println("\n" + player.player_username + "'s Initial Card Draw: ");
		    for (int i = 0; i < 7; i++)
			    player.gain(Card.getCard(cards, Card.CardName.Copper));
            for (int i = 0; i < 3; i++)
			    player.gain(Card.getCard(cards,Card.CardName.Estate));
        }
    }
	   
	public HashMap<Player, Integer>  play() {
			 
		   	  int turn =0;	
		      while (!isGameOver()) {
		    	  turn++;
		         for (Player player : players) {
		        	 	System.out.println("\n--- " + player.player_username + " IS PLAYING ---");
		        	 	player.initializePlayerTurn();
		   				//player plays action card
		        	 	player.playKingdomCard();
		        	 	//player plays treasure card
		   			    player.playTreasureCard();
		   			    //player buy cards
		        	    player.buyCard(this);
		        	  //player ends turn
		        	    player.endTurn();
		         }
		         if(turn == 3)
		        	 break;
		      }
		      return this.getWinners();
		   }

	public boolean isGameOver() {
		 //if stack of Province cards is empty, the game ends
		   if((this.gameBoard.get(Card.getCard(cards, Card.CardName.Province))==null)||(this.gameBoard.get(Card.getCard(cards, Card.CardName.Province))== 0))
			   return true;
		 //if three supply pile are at 0, the game ends
		      int emptySupplyPile = 0;
		      for (int i : gameBoard.values()){
		         if (i == 0)
		        	 emptySupplyPile++;
		         if ( emptySupplyPile >= 3)
		         {
		           return true;
		         }
		      }
		         return false;
		   }

	public HashMap<Player, Integer>  getWinners() {
    //Set HashMap  of each player and the score (remember ties!)
	    HashMap<Player, Integer> playerScore = new HashMap<Player, Integer>();

		//get score for each player
		for (Player p : players) {
		    int score = p.scoreFor();
		    playerScore.put(p, score);
        }

        return playerScore;
    }

	public static void addEmbargo()
    {
        System.out.println("Token Added.");
    }

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		if (gameBoard.isEmpty())
			sb.append("The board game is empty you need to intialize the game!!!!");
		else {
			for (Player player : players)
				sb.append(" --- " + player.toString() + "\n");
			sb.append(" --- gameBoard --- \n");
			sb.append("Cards on the table: \n");
			sb.append("Card Name: \t\t NumberCards: \n");
			Map<Card, Integer> treeMap = new TreeMap<Card, Integer>(gameBoard);
			for (Card card : treeMap.keySet())
				sb.append("\t" + card.getCardName() + "\t\t\t"
						+ treeMap.get(card) + "\n");
		}
		return sb.toString();
	}   
	   
	private GameState(List<Card> cards, List<Player> players, HashMap<Card, Integer> gameBoard) {
		   this.cards=cards;
		   this.players=players;
		   this.gameBoard=gameBoard;
	   }

	public GameState clone() throws CloneNotSupportedException {
		   List<Player> clonePlayers = new ArrayList<Player>();
		   List<Card> cloneCards = new ArrayList<Card>();
		   HashMap<Card, Integer> cloneGmeBoard = new HashMap<Card, Integer>();	
		   
		    for (Player player : players) 
		    	clonePlayers.add((Player) player.clone());
		    for (Card card : cards) 
		    	cloneCards.add((Card) card.clone());
		    for (Card card : gameBoard.keySet())
		    	cloneGmeBoard.put((Card) card.clone(),gameBoard.get(card));

		   final GameState cloneState= new GameState(cloneCards,clonePlayers,cloneGmeBoard);
	        return  cloneState;
	    }
}






/*
struct gameState {
    int numPlayers; //number of players
	int supplyCount[treasure_map+1];  //this is the amount of a specific type of card given a specific number.
	int embargoTokens[treasure_map+1];
	int outpostPlayed;
	int outpostTurn;
	int whoseTurn;
	int phase;
	int numActions;  Starts at 1 each turn
    int coins; /* Use as you see fit!
    int numBuys; /* Starts at 1 each turn
    int hand[MAX_PLAYERS][MAX_HAND];
    int handCount[MAX_PLAYERS];
    int deck[MAX_PLAYERS][MAX_DECK];
    int deckCount[MAX_PLAYERS];
    int discard[MAX_PLAYERS][MAX_DECK];
    int discardCount[MAX_PLAYERS];
    int playedCards[MAX_DECK];
    int playedCardCount;
};
*/
