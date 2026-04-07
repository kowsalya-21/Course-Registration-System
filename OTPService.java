import java.util.Random;

public class OTPService {

    private static String generatedOTP;

    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        generatedOTP = String.valueOf(otp);
        return generatedOTP;
    }

    public static boolean verifyOTP(String enteredOTP) {
        return generatedOTP != null && generatedOTP.equals(enteredOTP);
    }
}