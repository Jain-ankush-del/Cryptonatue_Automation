package framework.utils;

public class VarargsUtils {

    private VarargsUtils() {}

    public static boolean isNotEmpty(Object[] args) {
        return args.length > 0;
    }

    public static boolean isTrue(boolean... args) {
        return args.length > 0 && args[0];
    }

    public static boolean isFalse(boolean... args) {
        return !isTrue(args);
    }
}