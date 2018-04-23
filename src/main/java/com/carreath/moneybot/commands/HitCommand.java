package main.java.com.carreath.moneybot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import main.java.com.carreath.moneybot.MoneyBot;
import main.java.com.carreath.moneybot.enums.BlackJackPhase;
import main.java.com.carreath.moneybot.models.Card;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class HitCommand implements CommandExecutor {

	private MoneyBot moneybot;

	public HitCommand(MoneyBot moneybot) {
		this.moneybot = moneybot;
	}
	
	@Command(aliases = "$hit", description = "Allows a user to bet money on games in this channel")
	public String onHitCommand(String[] args, Message message) {
		if (!this.moneybot.isEnabledChannel(message)) {
			message.delete();
			return "You cant use MoneyBot here...";
		}

		User user = message.getAuthor().asUser().get();
		Long channelId = message.getChannel().getId();
		if (this.moneybot.getBlackJackGame().getCurrentPhase(channelId) == BlackJackPhase.PlayPhase) {
			if (this.moneybot.getBlackJackGame().getCurrentPlayerId(channelId) == user.getId()) {
				if (this.moneybot.getBlackJackGame().hit(channelId, user)) {
					String result = user.getName() + "s Hand:";
					int index = 0;
					for (Card c : this.moneybot.getBlackJackGame().getHand(channelId, user)) {
						if (index++ == 0) {
							result += "\n - [?]";
						} else {
							result += "\n - " + c;
						}
					}
					return result + "\n\nIt is " + user.getName() + "s turn. You can \'**$hit**\' or \'**$stay**\'.";
				} else {
					String hand = user.getName() + "s hand:";
					for (Card c : this.moneybot.getBlackJackGame().getHand(channelId, user)) {
						hand += "\n - " + c;
					}

					User nextUser = this.moneybot.getBlackJackGame().nextPlayer(channelId);
					if (nextUser == null) {
						this.moneybot.getBlackJackGame().setCurrentPhase(channelId, BlackJackPhase.FinishPhase);
						return user.getName() + " Bust...\n" + hand + "\n\n" + this.moneybot.getBlackJackGame().finishGame(channelId);
					}
					return user.getName() + " Bust...\n" + hand + "\n\nIt is " + nextUser.getName() + "s turn. You can \'**$hit**\' or \'**$stay**\'.";
				}
			}
			return "It is not your turn to Stay.";
		}

		return "It is not time to do that.";
	}
}
