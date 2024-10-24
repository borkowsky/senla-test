package utils;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Utils {
    private static final Scanner SCANNER = new Scanner(System.in).useLocale(Locale.US);
    private static final Random RANDOM = new Random();

    public static String declOfNum(int count, String[] labels) {
        int[] cases = {2, 0, 1, 1, 1, 2};
        if (count < 1) {
            return labels[2];
        }
        return labels[(count % 100 > 4 && count % 100 < 20) ? 2 : cases[Math.min(count % 10, 5)]];
    }

    public static Integer scanNumber(String title) {
        System.out.println(title);
        return scanNumber();
    }

    public static Integer scanNumber() {
        Integer result = null;
        do {
            try {
                result = SCANNER.nextInt();
            } catch (Throwable e) {
                System.out.println("Введен некорректный формат данных, повторите ввод");
                SCANNER.nextLine();
            }
        } while (result == null);
        return result;
    }

    public static String scanString(String title) {
        System.out.println(title);
        return scanString();
    }

    public static String scanString() {
        String result = null;
        do {
            try {
                result = SCANNER.next();
            } catch (Throwable e) {
                System.out.println("Введен некорректный формат данных, повторите ввод");
                SCANNER.nextLine();
            }
        } while (result == null);
        return result;
    }

    public static Double scanDouble(String title) {
        System.out.println(title);
        return scanDouble();
    }

    public static double scanDouble() {
        Double result = null;
        do {
            try {
                result = SCANNER.nextDouble();
            } catch (Throwable e) {
                System.out.println("Введен некорректный формат данных, повторите ввод");
                SCANNER.nextLine();
            }
        } while (result == null);
        return result;
    }

    public static int getRandomNumber(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static <T> T getRandomArrayElement(T[] array) {
        return array[getRandomNumber(1, array.length) - 1];
    }

    public static int launchMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.format("%d. %s%n", i + 1, options[i]);
        }
        Integer num = Utils.scanNumber("Выберите действие:");
        if (num < 1 || num > options.length) {
            System.out.format("Введите корректное значение (%d-%d)%n",
                    1,
                    options.length
            );
            return launchMenu(options);
        }
        return num;
    }
}

