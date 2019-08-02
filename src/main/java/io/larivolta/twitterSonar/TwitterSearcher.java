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

    private Twitter getTwitterInstance() {
        TwitterFactory tf = new TwitterFactory(new ConfigurationBuilder().build());
        return tf.getInstance();
    }

    public void getUserTimeline(String screenName) {

        IPrinter sop = new SystemOutPrinter();

        try {
            getTwitterInstance()
                    .getUserTimeline(screenName)
                    .stream()
                    .forEach(tweet -> {
                        sop.print("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                    });
        } catch (TwitterException te) {
            sop.print("Failed to search tweets: " + te.getMessage());
        }
    }

    public void printUserTweets(String username) {

        IPrinter sop = new SystemOutPrinter();

        try {
            Query query = new Query(username);
            QueryResult result;
            do {
                result = getTwitterInstance().search(query);
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
    
    public List<TwitterActivity> getLastMinutesUserTweets(String username, int minutes) {
    	List<TwitterActivity> userActivities = new ArrayList<TwitterActivity>(0);

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
                result = getTwitterInstance().search(query);
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
