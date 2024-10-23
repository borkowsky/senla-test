package hangman;

import utils.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Hangman {
    private final static int MIN_TRIES = 3;
    private static String word;
    private static String category;
    private static List<String> hangmanPic;
    private static List<Integer> hangmanPicSteps = new ArrayList<>();
    private static int totalTries;
    private static int triesLeft;
    private static char[] originalChars;
    private static char[] outputChars;

    public static void start() {
        try {
            prepareWords(getWordsFile());
            prepareWord();
            prepareHangmanPic();
            prepareHangmanPicSteps();
            printGreetings();
            printImage();
            printWord();
            makeTry(false);
        } catch (Exception e) {
            System.out.println("Возникла ошибка при выполнении программы: " + e.getMessage());
        }
    }

    private static void prepareWord() {
        originalChars = word.toCharArray();
        outputChars = new char[originalChars.length];
        totalTries = Math.max((int) (double) (word.length() / 3), MIN_TRIES);
        triesLeft = totalTries;
        Arrays.fill(outputChars, '_');
    }

    private static void prepareWords(String path) throws IOException {
        List<String> words;
        try {
            words = getLinesFromFile(path);
            Optional<String> categoryFound = words
                    .stream()
                    .filter(line -> line.startsWith("category:::"))
                    .findFirst();
            category = categoryFound.map(s -> s.replace("category:::", "")).orElse("Неизвестная категория");
            word = Utils.getRandomArrayElement(words.toArray()).toString().toLowerCase();
        } catch (IOException e) {
            System.out.format("Ошибка чтения файла %s (%s)%n",
                    path,
                    e.getMessage()
            );
            throw e;
        }
    }

    private static List<String> getLinesFromFile(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(Path.of(path).toUri()), StandardCharsets.UTF_8)) {
            stream.forEach(lines::add);
        } catch (IOException e) {
            System.out.format("Ошибка чтения файла  %s (%s)%n",
                    path,
                    e.getMessage()
            );
            throw e;
        }
        return lines;
    }

    private static void prepareHangmanPic() {
        List<String> lines;
        try {
            hangmanPic = getLinesFromFile(Paths.get("src/hangman/files/hangman.txt").toAbsolutePath().toString());
        } catch (IOException e) {
            System.out.format("Ошибка чтения файла hangman.txt (%s)%n",
                    e.getMessage()
            );
        }
    }

    private static void prepareHangmanPicSteps() {
        if (hangmanPic.isEmpty()) {
            return;
        }
        for (int i = 0; i < hangmanPic.size(); i++) {
            String line = hangmanPic.get(i);
            if (line.contains(":d")) {
                hangmanPicSteps.add(i);
            }
        }
    }

    private static String getWordsFile() throws IOException {
        String result = "";
        try (Stream<Path> stream = Files.list(Paths.get("src/hangman/files").toAbsolutePath())) {
            List<String> filesList = stream
                    .filter(Files::isRegularFile)
                    .filter(file -> !file.getFileName().toString().endsWith("hangman.txt"))
                    .map(Path::toString)
                    .toList();
            result = Utils.getRandomArrayElement(filesList.toArray()).toString();
        } catch (IOException e) {
            System.out.println("Ошибка открытия папки для чтения списка файлов: " + e.getMessage());
            throw e;
        }
        return !result.isEmpty() ? result : null;
    }

    private static void printGreetings() {
        System.out.println("ВИСЕЛИЦА");
        System.out.println("Правила игры: Загадано слово из определенного количества букв. " +
                "Ваша задача - отгадать слово за отведенное количество попыток. " +
                "Допускается ввод слова целиком либо одной буквы. " +
                "Удачи :)");
    }

    private static void makeTry(boolean printInfo) {
        if (printInfo) {
            printWord();
        }
        System.out.format("Категория слова: %s%nОсталось %d %s из %d%n",
                category,
                triesLeft,
                Utils.declOfNum(triesLeft, new String[]{
                        "попытка",
                        "попытки",
                        "попыток"
                }),
                totalTries
        );
        String str = Utils.scanString("Введите букву или слово целиком:");
        str = str.trim();
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

    private static void printInput(String str) {
        System.out.format("%s %s%n",
                str.length() == 1 ? "Введена буква" : "Введено слово",
                str
        );
    }

    private static void printInputResult(boolean isCorrect) {
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

    private static void gameOver() {
        System.out.format("Игра окончена!%nПравильный ответ был: %s%n", word);
    }

    private static void printImage() {
        int linesToDrawIndex = (int) Math.round((double) hangmanPicSteps.size() / totalTries * (totalTries - triesLeft)) - 1;
        for (int i = 0; i < hangmanPic.size(); i++) {
            String replaceExp = triesLeft < 1 || linesToDrawIndex > -1 && hangmanPicSteps.get(linesToDrawIndex) >= i ?
                    "(:go)|(:d)" :
                    "(:go)(.*)|(:d)(.*)";
            System.out.println(hangmanPic.get(i).replaceAll(replaceExp, ""));
        }
    }

    private static boolean compareChars(char[] chars) {
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

    private static void printWord() {
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
