package password_generator;

import utils.Utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 12;
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()+_-=}{[]|:;/?.><,~";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        return generatePassword(generateNumber(MIN_LENGTH, MAX_LENGTH));
    }

    public static String generate(int length) {
        if (length < MIN_LENGTH) {
            length = MIN_LENGTH;
        }
        if (length > MAX_LENGTH) {
            length = MAX_LENGTH;
        }
        return generatePassword(length);
    }

    private static String generatePassword(int length) {
        StringBuilder resultString = new StringBuilder();
        Integer[] portions = new Integer[4];
        int left = length;
        for (int i = 0; i < portions.length; i++) {
            if (i + 1 == portions.length) {
                portions[i] = left;
            } else {
                portions[i] = generateNumber(1, (int) Math.ceil((double) length / portions.length) + generateNumber(1, 2));
                left -= portions[i];
            }
        }
        portions = shuffleArray(portions);
        for (int i = 0; i < portions.length; i++) {
            for (int j = 1; j <= portions[i]; j++) {
                String charsStr = getStringByIndex(i);
                resultString.append(charsStr.charAt(RANDOM.nextInt(charsStr.length())));
            }
        }
        System.out.format("Длина сгенерированного пароля: %d %s%n",
                resultString.length(),
                Utils.declOfNum(resultString.length(), new String[]{
                        "символ",
                        "символа",
                        "символов"
                })
        );
        return String.join("", shuffleArray(resultString.toString().split("")));
    }

    private static String getStringByIndex(int index) {
        return switch (index) {
            case 0 -> LOWERCASE;
            case 1 -> UPPERCASE;
            case 2 -> NUMBERS;
            case 3 -> SYMBOLS;
            default -> "";
        };
    }

    private static int generateNumber(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    private static <T> T[] shuffleArray(T[] array) {
        List<T> list = Arrays.asList(array);
        Collections.shuffle(list);
        return list.toArray(array);
    }
}
