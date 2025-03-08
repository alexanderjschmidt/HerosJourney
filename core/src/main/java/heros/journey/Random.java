package heros.journey;

public class Random extends java.util.Random {

    private static Random seededRandom;

    public static Random get() {
        if (seededRandom == null)
            seededRandom = new Random();
        return seededRandom;
    }

    private Random(){}

}
