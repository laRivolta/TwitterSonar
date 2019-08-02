package io.larivolta.twitterSonar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.larivolta.twitterSonar.model.TwitterActivity;
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
    
    
    public void printUserLastMinutesActivity(String username, int minutes) {

        IPrinter sop = new SystemOutPrinter();
        
        try {
        	List<TwitterActivity> userActivities = getLastMinutesUserTweets(username, minutes);
        	TwitterActivity tweet;
        	sop.print("Tweets para " +username + ":");
        	for (int i = 0; i < userActivities.size(); i++) {
            	tweet = userActivities.get(i);
                sop.print("@" + tweet.getScreen_name() + " - " + tweet.getCreated_at().toString() + " - " + tweet.getText());

            }
        	sop.print("---------------");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            sop.print("Failed to search tweets: " + e.getMessage());
            System.exit(-1);
        }
    }
    
    public List<TwitterActivity> getLastMinutesUserTweets(String username, int minutes) {
    	List<TwitterActivity> userActivities = new ArrayList<TwitterActivity>(0);
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWITTER_SECRET_KEY)
                .setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
        	QueryResult result;
        	Query query = new Query(username);
            Date today = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            String strToday = dateFormat.format(today);
            query.setSince(strToday); //TODO ojo medianoche
            query.count(5);	//TODO ampliar tras finalizar
            
            Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.MINUTE, -minutes);
	        System.out.println("Hace " + minutes + " minutos " + cal.get(Calendar.HOUR_OF_DAY) + ":"+cal.get(Calendar.MINUTE));
	        Date minHour = cal.getTime();
	        
            do {
                result = twitter.search(query);
                result.getTweets()
                        .stream()
                        .filter(s -> s.getCreatedAt().after(minHour))
                        .forEach(tweet -> userActivities.add(new TwitterActivity(tweet.getUser().getScreenName(), tweet.getCreatedAt(), tweet.getText())));

            } while ((query = result.nextQuery()) != null);
    
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get last tweets: " + te.getMessage());
        }

        return userActivities;
    }
    
}
