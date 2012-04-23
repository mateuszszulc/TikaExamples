package tikainaction.chapter11;

import java.security.Key;

public class Pharmacy {

    private static Key key = null;

    public static Key getKey() {
        return key;
    }

    public static void setKey(Key key) {
        Pharmacy.key = key;
    }

}
