import java.util.Vector;
import java.util.Stack;
import java.util.Collections;

public class Deck {
	private Stack<Card> drawPile;
	private Stack<Card> discardPile;
	private Vector<Card> hand;
	
	private int cardCount = 0;
	private int gardenCount = 0;
	private int score = 0;
	
	public Deck() {
		drawPile = new Stack<Card>();
		discardPile = new Stack<Card>();
		hand = new Vector<Card>();
		
		//add starting cards to the deck
		for(int i=0; i<7; i++) {
			AddCard(new TreasureCard(TreasureCard.TreasureType.COPPER));
		}
		for(int i=0; i<3; i++) {
			AddCard(new VictoryCard(VictoryCard.VictoryType.ESTATE));
		}
		
		Shuffle();
	}
	
	public int GetScore() {
		return score + ((int)(cardCount/10))*gardenCount;
	}
	
	private void Shuffle() { //shuffle the deck
		while(!discardPile.empty()) {
			drawPile.push(discardPile.pop());
		}
		for(int i = (drawPile.size()-1); i > 0; i--) {
			int rand = (int)(Math.random() * i);
			Collections.swap(drawPile, i, rand);
		}
		
		
	}
	
	public void AddCard(Card card) { //add a card to the deck
		discardPile.push(card);
		cardCount++;
		
		if(card.GetName() == "Gardens") {
			gardenCount++;
		}
		else if(card instanceof VictoryCard) {
			score += ((VictoryCard)card).GetVPs();
		}
	}
	
	public Vector<Card> GetHand() { return hand; }
	
	public int PlayTreasures() {
		int total = 0;
		
		for(int i=0; i<hand.size(); i++) {
			if(hand.elementAt(i) instanceof TreasureCard) {
				total += hand.elementAt(i).GetValue();
			}
		}
		
		return total;
	}
	
	public Vector<Card> DrawCards(int numCards) { //will probably return a vector containing the hand
		//draw numCards cards from drawPile into hand, return cards drawn to player
		if(drawPile.size()<numCards) {
			Shuffle();
		}
		else {
			for(int i=0; i<numCards; i++) {
				hand.addElement(drawPile.pop());
			}
		}
		
		return hand;
	}
	
	public void TrashCard(Card card) {
		cardCount--;
		
		if(card.GetName() == "Gardens") {
			gardenCount--;
		}
		else if(card instanceof VictoryCard) {
			score -= ((VictoryCard)card).GetVPs();
		}
	}
	
	public void DiscardHand() {
		for(int i=0; i<hand.size(); i++) {
			discardPile.push(hand.get(i));
		}
		
		hand.clear();
	}
	
	public void DiscardFromHand(Card card) {
		if(hand.remove(card)) {
			discardPile.push(card);
		}
	}
}
