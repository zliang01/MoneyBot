package main.java.com.carreath.moneybot;

import main.java.com.carreath.moneybot.commands.*;
import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import org.javacord.api.entity.message.Message;
import org.json.simple.JSONObject;

import java.util.HashSet;

public class MoneyBot {

	private JSONObject config = null;

	BlackJackGame blackJackGame;
	HashSet<Long> enabledChannels;
	
	public MoneyBot() {
		String token = Config.getToken();

		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		
		// Create command handler and register all commands
		CommandHandler handler = new JavacordHandler(api);
		handler.registerCommand(new CreateCommand(this));
		handler.registerCommand(new JoinCommand(this));
		handler.registerCommand(new QuitCommand(this));
		handler.registerCommand(new StartCommand(this, token));
		handler.registerCommand(new BetCommand(this));
		
		api.addMessageCreateListener(event -> {
			
		});

		enabledChannels = Config.getEnabledChannels();
	}
	
	public boolean createBlackJackGame(Long channelId) {
		if (this.blackJackGame == null) {
			this.blackJackGame = new BlackJackGame();
		}
		boolean success = this.blackJackGame.setActive(channelId, true);
		if (success) {
			this.blackJackGame.setCurrentPhase(channelId, BlackJackPhase.WaitingPhase);
		}
		return success;
	}
	
	public BlackJackGame getBlackJackGame() {
		return this.blackJackGame;
	}

	// Returns if the originating channel of the message is permitted to be used
	public boolean isEnabledChannel(Message message) {
		if (enabledChannels.contains(message.getChannel().getId())) {
			return true;
		}
		return false;
	}
}
