package main.java.com.carreath.moneybot;

public class Main {

	public static void main(String[] args) {
		// Open the config file exit app if it fails
		if(Config.openConfig(new Main())) {
			MoneyBot bot = new MoneyBot();
		}
	}
}
