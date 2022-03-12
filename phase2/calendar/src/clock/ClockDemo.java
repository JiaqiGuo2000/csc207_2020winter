package clock;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * A demo that shows the usage of the Clock
 */
class ClockDemo {
    private static void sleep(long sec) throws InterruptedException {
        TimeUnit.SECONDS.sleep(sec);
    }

    /**
     * a setting up
     * @param args the argument
     * @throws InterruptedException an exception
     */
    public static void main(String[] args) throws InterruptedException {
        Clock.init(null);

        System.out.println(Clock.getTime());

        sleep(15);
        System.out.println(Clock.getTime()); // + 15 s

        Clock.setSpeed(2);

        sleep(15);
        System.out.println(Clock.getTime()); // + 30 s

        Clock.jumpTo(LocalDateTime.parse("1926-08-17T00:00"));
        System.out.println(Clock.getTime()); // 1926-08-17T00:00

        sleep(15);
        System.out.println(Clock.getTime()); // + 30 s

        Clock.setSpeed(0.5);
        sleep(15);
        System.out.println(Clock.getTime()); // + 7.5 s
    }
}
