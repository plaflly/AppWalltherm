package wallpl.example.vvvlad.walltherm.Adapters;

import java.util.Random;

public class RAndom {


    public static final String DATA = "lm9wxyzNOPQXYZijk45DEv3ABnop8Ugh67abFGHIJKLM2RSTcdef01rstVWuC";
    public static Random random = new Random();

    public static String randomString() {
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            sb.append(DATA.charAt(random.nextInt(DATA.length())));
        }
        return sb.toString();
    }
}
