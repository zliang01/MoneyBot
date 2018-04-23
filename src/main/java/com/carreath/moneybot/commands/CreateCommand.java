package main.java.com.carreath.moneybot.commands;

import main.java.com.carreath.moneybot.MoneyBot;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import org.javacord.api.entity.message.Message;

public class CreateCommand implements CommandExecutor {
	
	private MoneyBot moneybot;
	
	public CreateCommand(MoneyBot moneybot) {
		this.moneybot = moneybot;
	}
	
	@Command(aliases = "$create", description = "Starts a new game.", usage = "$create [game name]")
	public String onCreateCommand(String[] args, Message message) {
		if (!moneybot.isEnabledChannel(message)) {
            message.delete();
			return "You cant use MoneyBot here...";
		}

		if (args.length == 0) {
			return "You must provide the game name to be created...";
		}

		Long channelId = message.getChannel().getId();
		switch (args[0]) {
			case "blackjack":
				if(!moneybot.createBlackJackGame(message.getChannel().getId())) {
					return "There is already a game of BlackJack going on here... If this is a mistake type \'**$reset**\' to stop all games in this channel";
				}
				return "A new game of BlackJack has been started! Type \'**$join blackjack**\' to join!";
		}

		return "Sorry, " + args[0] + " is not a game i can host... For a list of games Type \'**$games**\'";
	}
	
}
