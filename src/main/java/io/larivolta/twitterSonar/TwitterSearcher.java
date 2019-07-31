package io.larivolta.twitterSonar;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSearcher implements ITwitterSearcher {



    public void printUserTweets(String username) {

        IPrinter sop = new SystemOutPrinter();
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWITTER_SECRET_KEY)
                .setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            Query query = new Query(username);
            QueryResult result;
            do {
                result = twitter.search(query);
                result.getTweets()
                        .stream()
                        .forEach(tweet -> sop.print("@" + tweet.getUser().getScreenName() + " - " + tweet.getText()));

            } while ((query = result.nextQuery()) != null);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            sop.print("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
}
