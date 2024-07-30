package hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication;

public class AuthConstants {
    public static String authorizationHeader = "Authorization";
    public static String authTokenPrefix = "Bearer ";
    public static int authTokenPrefixLength = 7;
    public static long authTokenTTL = 1000L * 60 * 60 * 24 * 365; // one year
}
