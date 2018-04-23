package main.java.com.carreath.moneybot.models;

import main.java.com.carreath.moneybot.enums.CardFace;
import main.java.com.carreath.moneybot.enums.CardSuit;

import java.util.Random;

public class Card {
    public CardFace face;
    public CardSuit suit;

    public Card(CardFace face, CardSuit suit) {
        this.face = face;
        this.suit = suit;
    }
    public String toString() {
        switch (face) {
            case Two: return "[2 of " + suit.name() + "]";
            case Three: return "[3 of " + suit.name() + "]";
            case Four: return "[4 of " + suit.name() + "]";
            case Five: return "[5 of " + suit.name() + "]";
            case Six: return "[6 of " + suit.name() + "]";
            case Seven: return "[7 of " + suit.name() + "]";
            case Eight: return "[8 of " + suit.name() + "]";
            case Nine: return "[9 of " + suit.name() + "]";
            case Ten: return "[10 of " + suit.name() + "]";
            default: return "[" + face.name() + " of " + suit.name() + "]";
        }
    }
    public int getValue() {
        switch (face) {
            case Ace: return 1;
            case Two: return 2;
            case Three: return 3;
            case Four: return 4;
            case Five: return 5;
            case Six: return 6;
            case Seven: return 7;
            case Eight: return 8;
            case Nine: return 9;
            default: return 10;
        }
    }
}
