package main.java.com.carreath.moneybot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import org.javacord.api.entity.user.User;

public class BlackJackGame {
	private HashMap<Long, HashSet<User>> players;
	private HashMap<Long, Boolean> isActive;
	private HashMap<Long, BlackJackPhase> phase;
	
	public BlackJackGame() {
		this.players = new HashMap<Long, HashSet<User>>();
        this.isActive = new HashMap<Long, Boolean>();
        this.phase = new HashMap<Long, BlackJackPhase>();
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
}
