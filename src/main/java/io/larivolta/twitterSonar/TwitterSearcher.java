package io.larivolta.twitterSonar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	public void printUserTweetsSince(String screenName, Date actualHour, int minutes) {
		try {
			List<TwitterActivity> userActivities = getUserTweetsSince(screenName, minutes);
			userActivities.forEach(tweet -> {
				System.out.println("@" + tweet.getScreen_name() + " - " + tweet.getCreated_at().toString() + " - "
						+ tweet.getText());
			});

		} catch (Exception e) {
			System.out.println("Failed to search tweets: " + e.getMessage());
		}
	}

	public String[] getUserActivitiesCompilationsSince(String screenName, Date actualHour, int minutes) {
		String[] stringifiedUserActivities = new String[3];
		List<TwitterActivity> userActivities = new ArrayList<TwitterActivity>(0);
		userActivities.addAll(getUserTweetsSince(screenName, minutes));
		userActivities.addAll(getUserLikesSince(screenName, minutes));

		System.out.println(screenName + " == " + userActivities.size());
		stringifiedUserActivities[0] = screenName;
		stringifiedUserActivities[1] = actualHour.toString();
		stringifiedUserActivities[2] = String.valueOf(userActivities.size());

		return stringifiedUserActivities;
	}

	/**
	 * Generates a file with the needed format for graphs: [Datetime, user1, user2,
	 * user3] [04/08/19 13:00, 2, 0, 6]
	 * 
	 * @param actualHour
	 * @param stringifiedActivities
	 * @return
	 */
	public String[] getCompiledData(Date actualHour, List<String[]> stringifiedActivities) {
		String[] compiledData = new String[stringifiedActivities.size() + 1];
		String[] headers = new String[stringifiedActivities.size() + 1];
		String strDate = new SimpleDateFormat("dd/MM/yy HH:mm").format(actualHour);
		headers[0] = "Fecha";
		compiledData[0] = strDate;
		for (int i = 0; i < stringifiedActivities.size(); i++) {
			headers[i + 1] = stringifiedActivities.get(i)[0]; // username
			compiledData[i + 1] = stringifiedActivities.get(i)[2]; // userActivities
		}
		System.out.println(Arrays.toString(headers));
		System.out.println(Arrays.toString(compiledData));
		return compiledData;
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

	public void writeToCsvFile(String[] strings, String separator) {

		File csvFile = new File("/Users/martamanso/twittersonar.csv");
		if (csvFile.isFile()) {

			try (FileWriter writer = new FileWriter(csvFile, true)) {
				writer.append(System.lineSeparator());
				for (int i = 0; i < strings.length; i++) {
					writer.append(strings[i]);
					if (i < (strings.length - 1))
						writer.append(separator);
				}

				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("ERROR: No Existe fichero");
		}

	}

}
