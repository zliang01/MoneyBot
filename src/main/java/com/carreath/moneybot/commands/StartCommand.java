package main.java.com.carreath.moneybot.commands;

import java.util.Collections;

import main.java.com.carreath.moneybot.MoneyBot;
import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class StartCommand implements CommandExecutor {

	private MoneyBot moneybot;
	String token;
	
	public StartCommand(MoneyBot moneybot, String token) {
		this.moneybot = moneybot;
		this.token = token;
	}
	
	@Command(aliases = "$start", description = "Starts the game specified")
	public String onStartCommand(String[] args, Message message) {
		if (!moneybot.isEnabledChannel(message)) {
			message.delete();
			return "You cant use MoneyBot here...";
		}

		if (args.length == 0) {
			return "You must provide the game name to start...";
		}

		Long channelId = message.getChannel().getId();
		switch (args[0]) {
			case "blackjack":
				if(this.moneybot.getBlackJackGame().isActive(channelId)) {
					System.out.println(this.moneybot.getBlackJackGame().getCurrentPhase(channelId));
					if (this.moneybot.getBlackJackGame().getCurrentPhase(channelId) == BlackJackPhase.WaitingPhase) {
						this.moneybot.getBlackJackGame().setCurrentPhase(channelId, BlackJackPhase.BettingPhase);
						return "Starting next round of BlackJack. Place your bets! Type \'**$bet blackjack <amount>**\'";
					}
					return "This blackjack game must finish the current hand to start the next round...";
				}
				return "There is no blackjack game playing right now.";
		}

		return "Sorry, " + args[0] + " is not a game I can host... For a list of games Type \'**$games**\'";
	}
}
