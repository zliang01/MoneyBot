package main.java.com.carreath.moneybot;

import java.util.*;

import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import main.java.com.carreath.moneybot.models.Card;
import main.java.com.carreath.moneybot.models.Cards;
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
    private HashMap<Long, Integer> currentPlayerIndex;
	
	public BlackJackGame() {
		this.players = new HashMap<Long, HashSet<User>>();
        this.isActive = new HashMap<Long, Boolean>();
        this.phase = new HashMap<Long, BlackJackPhase>();
        this.bets = new HashMap<Long, HashMap<User, Integer>>();
        this.currentPlayers = new HashMap<Long, ArrayList<User>>();
        this.currentPlayerIndex = new HashMap<Long, Integer>();
        this.currentHands = new HashMap<Long, HashMap<User, ArrayList<Card>>>();
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

        Card newCard = Cards.getCard();
        hand.add(newCard);

        return newCard;
    }

    public Long getCurrentPlayerId(long channelId) {
	    return this.currentPlayers.get(channelId).get(this.currentPlayerIndex.get(channelId)).getId();
    }

    public User nextPlayer(long channelId) {
	    this.currentPlayerIndex.put(channelId, this.currentPlayerIndex.get(channelId));
	    return this.currentPlayers.get(channelId).get(this.currentPlayerIndex.get(channelId));
    }

    public boolean hit(long channelId, User user) {
        addCard(channelId, user);
        return Cards.calculate(this.currentHands.get(channelId).get(user));
    }

    public String finishGame(long channelId) {
        return "";
    }

    public String playGame(TextChannel channel) {
        Long channelId = channel.getId();
        this.setCurrentPhase(channelId, BlackJackPhase.DealingPhase);

        String gameSetup = "Current Hands:";

        ArrayList<User> currentPlayers = new ArrayList<>();

        for (User user : this.getPlayers(channelId)) {
            String userMessage = "Your hand: ";

            Card card1 = this.addCard(channelId, user);
            Card card2 = this.addCard(channelId, user);

            userMessage += card1 + " | " + card2;
            user.sendMessage(userMessage);

            gameSetup += "\n\t" + user.getName() + " - <?> | " + card2;

            currentPlayers.add(user);
        }

        gameSetup += "\n\nIt is " + currentPlayers.get(0).getName() + "s turn. You can \'**$hit**\' or \'**$stay**\'.";

        this.setCurrentPhase(channelId, BlackJackPhase.PlayPhase);
        this.currentPlayers.put(channelId, currentPlayers);
        this.currentPlayerIndex.put(channelId, 0);

        return gameSetup;
    }
}
