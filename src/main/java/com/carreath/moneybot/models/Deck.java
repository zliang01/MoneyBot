package main.java.com.carreath.moneybot.models;

import main.java.com.carreath.moneybot.enums.CardFace;
import main.java.com.carreath.moneybot.enums.CardSuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Deck {
    private LinkedList<Card> cards = new LinkedList<Card>();
    public Deck() {
        ArrayList<Card> orderedDeck = new ArrayList<>();
        for(int i=0; i<13; i++) {
            orderedDeck.add(new Card(CardFace.values()[i], CardSuit.Spades));
            orderedDeck.add(new Card(CardFace.values()[i], CardSuit.Clubs));
            orderedDeck.add(new Card(CardFace.values()[i], CardSuit.Hearts));
            orderedDeck.add(new Card(CardFace.values()[i], CardSuit.Diamonds));
        }
        Collections.shuffle(orderedDeck);
        for (Card c : orderedDeck) {
            cards.push(c);
        }
    }
    public Card getCard() {
        return cards.poll();
    }
}
