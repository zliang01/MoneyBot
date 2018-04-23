package main.java.com.carreath.moneybot.commands;

import main.java.com.carreath.moneybot.MoneyBot;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class QuitCommand implements CommandExecutor {

	private MoneyBot moneybot;
	
	public QuitCommand(MoneyBot moneybot) {
		this.moneybot = moneybot;
	}

	@Command(aliases = "~quit", description = "Lets a user leave any or all games in the channel. User does lose if this happens")
	public String onQuitCommand(String[] args, Message message) {
		if (!this.moneybot.isEnabledChannel(message)) {
			message.delete();
			return "You cant use MoneyBot here...";
		}

		User user = message.getAuthor().asUser().get();
		Long channelId = message.getChannel().getId();
		if (args.length == 0) {
			this.moneybot.getBlackJackGame().removePlayer(channelId, user);
			return user.getName() + " has left all games.";
		} else {
			switch (args[0]) {
				case "blackjack":
					this.moneybot.getBlackJackGame().removePlayer(channelId, user);
					return user.getName() + " has left blackjack.";
			}

			return "Sorry, " + args[0] + " is not a game I can host... For a list of games Type \'**$games**\'";
		}
	}
}
