package exchanger;

import utils.Utils;

public class Main {
    private static Exchanger exchanger;
    static String[] startMenuOptions = new String[]{
            "Использовать курс по умолчанию",
            "Ввести курс вручную"
    };

    public static void main(String[] args) {
        System.out.println("Обмен валют");
        exchanger = new Exchanger();
        start();
    }

    private static void start() {
        exchanger.selectActiveCurrency();
        switch (Utils.launchMenu(startMenuOptions)) {
            case 1:
                exchanger.useDefaultRates();
                break;
            case 2:
                exchanger.inputRates();
                break;
            default:
                System.out.println("Invalid input provided");
                break;
        }
        exchanger.convert();
    }
}
