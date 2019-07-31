package io.larivolta.twitterSonar;

public class SystemOutPrinter implements IPrinter {

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
