package io.larivolta.twitterSonar;

import io.larivolta.twitterSonar.application.TwitterSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TwitterSonar {

	private static final Logger logger = LoggerFactory.getLogger(TwitterSonar.class);

	public static void main(String[] args) {

		int minutes = 30;
		TwitterSearcher ts = new TwitterSearcher();
		String usernames[] = new String[] { "diana_aceves_", "ziraco", "eferro", "vgaltes", "alfredodev",
				"adelatorrefoss", "merybere", "LauraLacarra", "jmbeas", "kinisoftware", "flopezluis", "nabaroa",
				"aartiles24" };
		logger.info("Activity in the last " + minutes + "\':");

		Calendar cal = Calendar.getInstance();
		Date actualHour = cal.getTime();

		List<String[]> stringifiedActivities = new ArrayList<String[]>(0);
		/*for (int i = 0; i < usernames.length; i++) {

			stringifiedActivities.add(ts.getUserActivitiesCompilationsSince(usernames[i], actualHour, minutes).clone());

		}*/
		String[] s = ts.getCompiledData(actualHour, stringifiedActivities);
		ts.writeToCsvFile(s, ",");

	}
}
