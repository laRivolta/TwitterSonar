package io.larivolta.twitterSonar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TwitterSonar {

	public static void main(String[] args) {

		int minutes = 30;
		TwitterSearcher ts = new TwitterSearcher();
		String usernames[] = new String[] { "MartaMans0", "fran_mosteiro", "zenekezene", "ziraco", "vgaltes" };
		System.out.println("Activity in the last " + minutes + "\':");

		Calendar cal = Calendar.getInstance();
		Date actualHour = cal.getTime();

		List<String[]> stringifiedActivities = new ArrayList<String[]>(0);
		for (int i = 0; i < usernames.length; i++) {

			stringifiedActivities.add(ts.getUserActivitiesCompilationsSince(usernames[i], actualHour, minutes).clone());

		}
		String[] s = ts.getCompiledData(actualHour, stringifiedActivities);
		ts.writeToCsvFile(s, ",");

	}
}
