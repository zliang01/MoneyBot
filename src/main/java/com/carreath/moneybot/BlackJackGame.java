package main.java.com.carreath.moneybot;

import java.lang.reflect.Array;
import java.util.*;

import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import main.java.com.carreath.moneybot.models.Card;
import main.java.com.carreath.moneybot.models.Deck;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

public class BlackJackGame {
    private Random rand = new Random();

    private HashMap<Long, HashSet<User>> players;
    private HashMap<Long, HashMap<User, Integer>> bets;
	private HashMap<Long, Boolean> isActive;
	private HashMap<Long, BlackJackPhase> phase;

    private HashMap<Long, ArrayList<User>> currentPlayers;
    private HashMap<Long, HashMap<User, ArrayList<Card>>> currentHands;
    private HashMap<Long, ArrayList<Card>> dealersHand;
    private HashMap<Long, Integer> currentPlayerIndex;
    private HashMap<Long, Deck> currentDeck;
	
	public BlackJackGame() {
		this.players = new HashMap<Long, HashSet<User>>();
        this.isActive = new HashMap<Long, Boolean>();
        this.phase = new HashMap<Long, BlackJackPhase>();
        this.bets = new HashMap<Long, HashMap<User, Integer>>();
        this.currentPlayers = new HashMap<Long, ArrayList<User>>();
        this.currentPlayerIndex = new HashMap<Long, Integer>();
        this.currentHands = new HashMap<Long, HashMap<User, ArrayList<Card>>>();
        this.currentDeck = new HashMap<Long, Deck>();
        this.dealersHand = new HashMap<Long , ArrayList<Card>>();
	}

    public boolean addPlayer(Long channelId, User user) {
	    HashSet<User> channel = this.players.get(channelId);
	    if (channel == null) {
	        this.players.put(channelId, new HashSet<User>());
            channel = this.players.get(channelId);
        }
	    if (!channel.contains(user)) {
	        channel.add(user);
	        return true;
        }
        return false;
    }
    public boolean removePlayer(Long channelId, User user) {
        HashSet<User> channel = this.players.get(channelId);
        if (channel != null && channel.contains(user)) {
            channel.remove(user);
            return true;
        }
        return false;
    }
    public HashSet<User> getPlayers(Long channelId) {
        return this.players.get(channelId);
    }

    public boolean setActive(Long channelId, boolean isActive) {
	    if(isActive) {
	        if (this.isActive.get(channelId) != null) {
                return false;
            }
            this.isActive.put(channelId, true);
        } else if (this.isActive.get(channelId) != null) {
	        this.isActive.remove(channelId);
        }
        return true;
    }

    public boolean isActive(Long channelId) {
        if (this.isActive.get(channelId) != null) {
            return true;
        }
        return false;
    }

    public BlackJackPhase getCurrentPhase(Long channelId) {
        return this.phase.get(channelId);
    }

    public void setCurrentPhase(Long channelId, BlackJackPhase phase) {
        this.phase.put(channelId, phase);
    }

    public boolean addBet(Long channelId, User user, int betAmount) {
	    HashMap<User, Integer> channel = this.bets.get(channelId);
	    if (channel == null) {
	        this.bets.put(channelId, new HashMap<User, Integer>());
            channel = this.bets.get(channelId);
        }
        if (!channel.containsKey(user)) {
	        channel.put(user, betAmount);
	        return true;
        }
        return false;
    }

    public boolean readyForDeal(Long channelId) {
        HashMap<User, Integer> channelBets = this.bets.get(channelId);
        HashSet<User> channelPlayers = this.players.get(channelId);

        if (channelBets.size() == channelPlayers.size()) {
            return true;
        }
        return false;
    }

    public void resetBets(Long channelId) {
	    this.bets.put(channelId, new HashMap<User, Integer>());
    }

    public Card addCard(Long channelId, User user) {
        HashMap<User, ArrayList<Card>> channel = this.currentHands.get(channelId);
        if (channel == null) {
            this.currentHands.put(channelId, new HashMap<User, ArrayList<Card>>());
            channel = this.currentHands.get(channelId);
            channel.put(user, new ArrayList<Card>());
        }
        ArrayList<Card> hand = channel.get(user);
        if (hand == null) {
            channel.put(user, new ArrayList<Card>());
        }

        Card newCard = this.currentDeck.get(channelId).getCard();
        hand.add(newCard);

        return newCard;
    }

    public Long getCurrentPlayerId(long channelId) {
	    return this.currentPlayers.get(channelId).get(this.currentPlayerIndex.get(channelId)).getId();
    }

    public User nextPlayer(long channelId) {
	    this.currentPlayerIndex.put(channelId, this.currentPlayerIndex.get(channelId) + 1);
	    if (this.currentPlayers.get(channelId).size() > this.currentPlayerIndex.get(channelId)) {
            return this.currentPlayers.get(channelId).get(this.currentPlayerIndex.get(channelId));
        }
        return null;
    }

    public boolean hit(long channelId, User user) {
        addCard(channelId, user);
        int total = 0;
        for (Card c : this.currentHands.get(channelId).get(user)) {
            total += c.getValue();
        }
        System.out.println(1);
        if (total > 21) {
            return false;
        }
        return true;
    }

    public String userWon() {
        return "WIN";
    }
    public String userLost() {
        return "LOSE";
    }

    public int calculate(ArrayList<Card> cards) {
        int[] values = new int[cards.size()];
        int i = 0;
        for (Card c : cards) {
            switch (c.face) {
                case Ace:
                    values[i++] = 11;
                    break;
                default:
                    values[i++] = c.getValue();
                    break;
            }
        }
        int total = 0;
        int lastAce = -1;
        do {
            lastAce = -1;
            total = 0;
            for(i=0; i<values.length; i++) {
                total += values[i];
                if (lastAce == -1 && values[i] == 11) {
                    lastAce = i;
                    values[i] = 1;
                }
            }
        } while(total > 21 && lastAce != -1);

        return total;
    }

    public String finishGame(long channelId) {
        String result = "Dealers hand:";
        for (Card c : dealersHand.get(channelId)) {
            result += "\n - " + c;
        }
        while(calculate(dealersHand.get(channelId)) < 17) {
            Card card = this.currentDeck.get(channelId).getCard();
            dealersHand.get(channelId).add(card);
            result += "\n - " + card;
        }
        System.out.println(1);
        int dealersScore = calculate(dealersHand.get(channelId));
        System.out.println(1);
        result += "\n\nDealer Score: " + dealersScore;
        for(User user : currentPlayers.get(channelId)) {
            System.out.println(2);
            int userScore = calculate(currentHands.get(channelId).get(user));
            result += "\n" + user.getName() + "s Score: " + userScore + (((dealersScore > 21 || dealersScore < userScore) && userScore <= 21)? userWon(): userLost());
        }
        result += "\n\nTo play again Type \'**$start blackjack**\'";
        setCurrentPhase(channelId, BlackJackPhase.WaitingPhase);
        return result;
    }

    public ArrayList<Card> getHand(Long channelId, User user) {
	    return this.currentHands.get(channelId).get(user);
    }

    public String playGame(TextChannel channel) {

        Long channelId = channel.getId();
        this.setCurrentPhase(channelId, BlackJackPhase.DealingPhase);
        this.currentDeck.put(channel.getId(), new Deck());
        this.currentHands.put(channelId, new HashMap<User, ArrayList<Card>>());

        String gameSetup = "Current Hands:";

        ArrayList<User> currentPlayers = new ArrayList<>();

        for (User user : this.getPlayers(channelId)) {
            String userMessage = "Your Facedown Card: ";

            this.currentHands.get(channelId).put(user, new ArrayList<Card>());

            Card card1 = this.addCard(channelId, user);
            Card card2 = this.addCard(channelId, user);

            userMessage += card1;
            user.sendMessage(userMessage);

            gameSetup += "\n\t" + user.getName() + " - [?] | " + card2;

            currentPlayers.add(user);
        }
        ArrayList<Card> dealerHand = new ArrayList<>();
        dealerHand.add(this.currentDeck.get(channelId).getCard());
        Card card2 = this.currentDeck.get(channelId).getCard();
        dealerHand.add(card2);
        this.dealersHand.put(channelId, dealerHand);

        gameSetup += "\n\tDealer - [?] | " + card2;

        gameSetup += "\n\nIt is " + currentPlayers.get(0).getName() + "s turn. You can \'**$hit**\' or \'**$stay**\'.";

        this.setCurrentPhase(channelId, BlackJackPhase.PlayPhase);
        this.currentPlayers.put(channelId, currentPlayers);
        this.currentPlayerIndex.put(channelId, 0);

        return gameSetup;
    }
}
