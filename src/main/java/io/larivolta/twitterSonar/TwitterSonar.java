package io.larivolta.twitterSonar;

public class TwitterSonar {

    public static void main(String[] args){

//        new TwitterSearcher().printUserTweets("fran_mosteiro");
        new TwitterSearcher().printUserLastMinutesActivity("fran_mosteiro", 480);
    }
}
