package io.larivolta.twitterSonar;

public class TwitterSonar {

	public static void main(String[] args) {

		int minutes = 60;
		String usernames[] = new String[] { "MartaMans0", "fran_mosteiro", "zenekezene", "ziraco", "vgaltes" };
		TwitterSearcher ts = new TwitterSearcher();
		System.out.println("In the last " + minutes + "\':");
		for (int i = 0; i < usernames.length; i++) {

			ts.printUserTweetsSince(usernames[i], minutes);

		}
	}
}
