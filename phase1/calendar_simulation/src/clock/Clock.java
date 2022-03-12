package clock;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class Clock {
    static private LocalDateTime lastReal, lastOutput;
    static private double v = 1;
    static private File file;
    static private boolean fileEnable = true;

    /**
     * Initialize the Clock. Should be called exactly once before Clock is used.
     *
     * @param f the file that saves the Clock; null if you don't want to read from / write to a file
     */
    public static void init(File f) {
        file = f;
        lastReal = LocalDateTime.now();
        lastOutput = lastReal;

        if (file == null) {
            fileEnable = false;
        } else {
            try {
                if (!file.createNewFile())
                    read(file);
                write();
            } catch (IOException e) {
                fileEnable = false;
            }
        }
    }

    private static void read(File file) throws IOException {
        try (InputStream in = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            lastReal = LocalDateTime.parse(br.readLine());
            lastOutput = LocalDateTime.parse(br.readLine());
            v = Double.parseDouble(br.readLine());
        }
    }

    private static void write() {
        if (fileEnable) {
            try (PrintWriter out = new PrintWriter(new FileWriter(file, false))) {
                out.println(lastReal + "\n" + lastOutput + "\n" + v);
            } catch (IOException e) {
                fileEnable = false;
            }
        }
    }

    private static void update() {
        LocalDateTime now = LocalDateTime.now();
        long dt = ChronoUnit.NANOS.between(lastReal, now);
        lastOutput = lastOutput.plusNanos((long) (v * dt));
        lastReal = now;
    }

    /**
     * get the current time of the Clock
     *
     * @return the current time
     */
    public static LocalDateTime getTime() {
        update();
        return lastOutput;
    }

    /**
     * Set the speed of the Clock
     *
     * @param speed the speed of the Clock
     */
    public static void setSpeed(double speed) {
        update();
        v = speed;
        write();
    }

    /**
     * Jump to a time
     *
     * @param t the time to jump to
     */
    public static void jumpTo(LocalDateTime t) {
        lastReal = LocalDateTime.now();
        lastOutput = t;
        write();
    }
}
