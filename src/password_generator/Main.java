package password_generator;

import utils.Utils;

public class Main {
    public static void main(String[] args) {
        System.out.println("Генератор паролей");
        if (Utils.launchMenu(new String[]{
                "Ввести длину пароля",
                "Длина по умолчанию (8-12 символов)"
        }) == 1) {
            int length = Utils.scanNumber("Введите желаемую длину пароля (8-12 символов):");
            System.out.println(PasswordGenerator.generate(length));
        } else {
            System.out.println(PasswordGenerator.generate());
        }
    }
}
