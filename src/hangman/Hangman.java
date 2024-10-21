package hangman;

import utils.Utils;

import java.util.Arrays;

public class Hangman {
    private final int TRIES = 3;
    private final String[] WORDS = new String[]{
            "самовозгорание",
            "заедание",
            "солнцепёк",
            "засорение",
            "химснаряд",
            "отталкивание",
            "фотографирование",
            "скучноватость",
            "окантовка",
            "текущее",
            "облитерация",
            "флик",
            "застройка",
            "сглаживание",
            "вандализм",
            "детализация",
            "брезгливость",
            "натуралистичность",
            "креветка",
            "обезоружение",
            "полутьма"
    };
    private String word;
    private int triesLeft;
    private char[] originalChars;
    private char[] outputChars;

    public Hangman() {
        word = Utils.getRandomArrayElement(WORDS);
        originalChars = word.toCharArray();
        outputChars = new char[originalChars.length];
        triesLeft = TRIES;
        Arrays.fill(outputChars, '_');
        printGreetings();
        printImage();
        printWord();
        makeTry(false);
    }

    private void printGreetings() {
        System.out.println("ВИСЕЛИЦА");
        System.out.println("Правила игры: Загадано слово из определенного количества букв. " +
                "Ваша задача - отгадать слово за отведенное количество попыток. " +
                "Допускается ввод слова целиком либо одной буквы. " +
                "Удачи :)");
    }

    private void makeTry(boolean printInfo) {
        if (printInfo) {
            printWord();
        }
        String str = Utils.scanString("Введите букву или слово целиком:");
        str = str.trim();
        System.out.println(str);
        if (compareChars(str.toCharArray())) {
            printImage();
            printInput(str);
            printInputResult(true);
        } else {
            triesLeft--;
            printImage();
            printInput(str);
            printInputResult(false);
        }
        if (triesLeft != 0) {
            makeTry(true);
        } else {
            gameOver();
        }
    }

    private void printInput(String str) {
        System.out.format("%s %s%n",
                str.length() == 1 ? "Введена буква" : "Введено слово",
                str
        );
    }

    private void printInputResult(boolean isCorrect) {
        System.out.format("Это %s ответ. У Вас %s %d %s %n",
                isCorrect ? "правильный" : "неправильный",
                Utils.declOfNum(triesLeft, new String[]{
                        "осталась",
                        "осталось",
                        "осталось"
                }),
                triesLeft,
                Utils.declOfNum(triesLeft, new String[]{
                        "попытка",
                        "попытки",
                        "попыток"
                })
        );
    }

    private void gameOver() {
        System.out.println("Игра окончена!");
    }

    private void printImage() {
        String outString =
                """
                         ___________________
                        | /       |         \s
                        |         |       \s
                        """;
        if (triesLeft < 3) {
            outString +=
                    """
                            |       (x_x)\s
                            """
            ;
        } else {
            outString +=
                    """
                            |\s
                            """
            ;
        }
        if (triesLeft < 2) {
            outString +=
                    """
                            |       /[:]\\\s
                            """
            ;
        } else {
            outString +=
                    """
                            |\s
                            """
            ;
        }
        if (triesLeft < 1) {
            outString +=
                    """
                            |       _| |_\s
                            |                ***\s
                            |__\\/____________\\|/
                            """
            ;
        } else {
            outString +=
                    """
                            |
                            |                ***\s
                            |__\\/____________\\|/
                            """
            ;
        }
        System.out.println(outString);
    }

    private boolean compareChars(char[] chars) {
        boolean result = false;
        for (char aChar : chars) {
            for (int j = 0; j < originalChars.length; j++) {
                if (aChar == originalChars[j] && outputChars[j] == '_') {
                    result = true;
                    outputChars[j] = aChar;
                }
            }
        }
        return result;
    }

    private void printWord() {
        System.out.format("Загадано слово из %d %s %n",
                word.length(),
                Utils.declOfNum(
                        word.length(),
                        new String[]{
                                "буквы",
                                "букв",
                                "букв"
                        }
                )
        );
        System.out.println(new String(outputChars));
    }
}
