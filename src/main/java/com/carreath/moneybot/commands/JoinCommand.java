package main.java.com.carreath.moneybot.commands;

import java.util.ArrayList;

import main.java.com.carreath.moneybot.MoneyBot;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class JoinCommand implements CommandExecutor {

	private MoneyBot moneybot;
	
	public JoinCommand(MoneyBot moneybot) {
		this.moneybot = moneybot;
	}
	
	@Command(aliases = "$join", description = "Allows a user to join new games in this channel")
	public String onJoinCommand(String[] args, Message message) {
		if (!this.moneybot.isEnabledChannel(message)) {
			message.delete();
			return "You cant use MoneyBot here...";
		}

		if (args.length == 0) {
			return "You must provide the game name to join...";
		}

		User user = message.getAuthor().asUser().get();
		Long channelId = message.getChannel().getId();
		switch (args[0]) {
			case "blackjack":
				if (this.moneybot.getBlackJackGame().isActive(channelId)) {
					if (!this.moneybot.getBlackJackGame().addPlayer(channelId, user)) {
						return "You are already in this game.";
					}
					return "You have joined the next hand of blackjack.";
				}
				return "There is no blackjack game being played... Type \'**$create blackjack**\' to create one.";
		}

		return "Sorry, " + args[0] + " is not a game I can host... For a list of games Type \'**$games**\'";
	}
}
