package main.java.com.carreath.moneybot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import main.java.com.carreath.moneybot.MoneyBot;
import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.nio.channels.Channel;

public class BetCommand implements CommandExecutor {

	private MoneyBot moneybot;

	public BetCommand(MoneyBot moneybot) {
		this.moneybot = moneybot;
	}
	
	@Command(aliases = "$bet", description = "Allows a user to bet money on games in this channel")
	public String onBetCommand(String[] args, Message message) {
		if (!this.moneybot.isEnabledChannel(message)) {
			message.delete();
			return "You cant use MoneyBot here...";
		}

		if (args.length < 2) {
			return "You must provide the game name to bet on and the bet amount...";
		}

		User user = message.getAuthor().asUser().get();
		Long channelId = message.getChannel().getId();
		switch (args[0]) {
			case "blackjack":
				if (this.moneybot.getBlackJackGame().isActive(channelId)) {
					if (this.moneybot.getBlackJackGame().getCurrentPhase(channelId) == BlackJackPhase.BettingPhase) {
						if (Integer.parseInt(args[1]) > 0) {
							this.moneybot.getBlackJackGame().addBet(channelId, user, Integer.parseInt(args[1]));
							if (this.moneybot.getBlackJackGame().readyForDeal(channelId)) {
								return this.moneybot.getBlackJackGame().playGame(message.getChannel());
							}
						} else {
							return "Your bet must be a positive integer.";
						}
					}
					return "You cannot bet until the next game starts";
				}
				return "There is no blackjack game being played... Type \'**$create blackjack**\' to create one.";
		}

		return "Sorry, " + args[0] + " is not a game I can host... For a list of games Type \'**$games**\'";
	}
}
