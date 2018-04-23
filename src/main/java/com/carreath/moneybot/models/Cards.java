package main.java.com.carreath.moneybot.models;

import java.util.ArrayList;
import java.util.Random;

public class Cards {
    private static Random rand = new Random();
    public static Card getCard() {
        int cardNum = rand.nextInt(12) + 1;

        Card card = new Card();
        switch (cardNum) {
            case 1:
                card.displayName = "A";
                card.value = cardNum;
                card.value2 = 11;
                break;
            case 11:
                card.displayName = "J";
                card.value = 10;
                break;
            case 12:
                card.displayName = "Q";
                card.value = 10;
                break;
            case 13:
                card.displayName = "K";
                card.value = 10;
                break;
            default:
                card.displayName = cardNum + "";
                card.value = cardNum;
        }
        return card;
    }
    public static boolean calculate(ArrayList<Card> cards) {
        int total = 0;
        for (Card c : cards) {
            total += c.value;
        }

        return (total < 22);
    }
}

