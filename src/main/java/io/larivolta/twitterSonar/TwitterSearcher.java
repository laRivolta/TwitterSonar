package io.larivolta.twitterSonar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.larivolta.twitterSonar.model.TwitterActivity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSearcher {
	Twitter twitterInstance;

	public TwitterSearcher() {
		twitterInstance = this.getTwitterInstance();
	}

	private Twitter getTwitterInstance() {
		TwitterFactory tf = new TwitterFactory(new ConfigurationBuilder().build());
		return tf.getInstance();
	}

	/**
	 * The userActivities list is composed of: - A list of tweets, retweets and
	 * replies (max 20) + a list of likes (max 20)
	 */
	public void printUserTweetsSince(String screenName, int minutes) {
		try {
			getUserActivitiesSince(screenName, minutes).forEach(tweet -> {
				System.out.println("@" + tweet.getScreen_name() + " - " + tweet.getCreated_at().toString() + " - "
						+ tweet.getText());

			});
		} catch (Exception e) {
			System.out.println("Failed to search tweets: " + e.getMessage());
		}
	}

	public List<TwitterActivity> getUserActivitiesSince(String screenName, int minutes) {
		List<TwitterActivity> userActivities = new ArrayList<TwitterActivity>(0);
		userActivities.addAll(getUserTweetsSince(screenName, minutes));
		userActivities.addAll(getUserLikesSince(screenName, minutes));
		System.out.println(screenName + " == " + userActivities.size());
		return userActivities;
	}

	/**
	 * It obtains the last tweets, retweets and replies since the specified amount
	 * of minutes. Max: 20 tweets
	 */
	public List<TwitterActivity> getUserTweetsSince(String screenName, int minutes) {
		List<TwitterActivity> userTweets = new ArrayList<TwitterActivity>(0);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -minutes);
		Date minHour = cal.getTime();

		try {
			this.twitterInstance.getUserTimeline(screenName).stream().filter(s -> s.getCreatedAt().after(minHour))
					.forEach(tweet -> userTweets.add(new TwitterActivity(tweet.getUser().getScreenName(),
							tweet.getCreatedAt(), tweet.getText())));

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get last tweets: " + te.getMessage());
		}

		return userTweets;
	}

	/**
	 * It obtains the last tweets, retweets and replies since the specified amount
	 * of minutes. Max: 20 tweets
	 */
	public List<TwitterActivity> getUserLikesSince(String screenName, int minutes) {
		List<TwitterActivity> userLikes = new ArrayList<TwitterActivity>(0);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -minutes);
		Date minHour = cal.getTime();

		try {
			this.twitterInstance.getFavorites(screenName).stream().filter(s -> s.getCreatedAt().after(minHour))
					.forEach(tweet -> userLikes.add(new TwitterActivity(tweet.getUser().getScreenName(),
							tweet.getCreatedAt(), tweet.getText())));
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get last tweets: " + te.getMessage());
		}

		return userLikes;
	}

}
