package main.java.com.carreath.moneybot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import main.java.com.carreath.moneybot.MoneyBot;
import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class StayCommand implements CommandExecutor {

	private MoneyBot moneybot;

	public StayCommand(MoneyBot moneybot) {
		this.moneybot = moneybot;
	}
	
	@Command(aliases = "$stay", description = "Allows a user to bet money on games in this channel")
	public String onStayCommand(String[] args, Message message) {
		if (!this.moneybot.isEnabledChannel(message)) {
			message.delete();
			return "You cant use MoneyBot here...";
		}

		User user = message.getAuthor().asUser().get();
		Long channelId = message.getChannel().getId();
		if (this.moneybot.getBlackJackGame().getCurrentPhase(channelId) == BlackJackPhase.PlayPhase) {
			if (this.moneybot.getBlackJackGame().getCurrentPlayerId(channelId) == user.getId()) {
				User nextUser = this.moneybot.getBlackJackGame().nextPlayer(channelId);
				if (nextUser == null) {
					this.moneybot.getBlackJackGame().setCurrentPhase(channelId, BlackJackPhase.FinishPhase);
					return this.moneybot.getBlackJackGame().finishGame(channelId);
				}
				return "It is " + nextUser.getName() + "s turn. You can \'**$hit**\' or \'**$stay**\'.";
			}
			return "It is not your turn to Stay.";
		}

		return "It is not time to do that.";
	}
}
