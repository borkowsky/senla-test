package exchanger;

import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Exchanger {
    private final String[] CURRENCY_CODES = new String[]{
            "BYN",
            "USD",
            "EUR",
            "JPY",
            "GBP",
            "AUD"
    };
    private final String[] CURRENCY_LABELS = new String[]{
            "Белорусский рубль",
            "Американский доллар",
            "Евро",
            "Японская йена",
            "Фунт стерлингов",
            "Австралийский доллар"
    };
    private final Double[] CURRENCY_DEFAULT_RATES = new Double[]{
            1.0,
            3.27,
            3.53,
            0.021,
            4.25,
            2.17
    };
    private Currency activeCurrency;
    private List<Currency> currencies = new ArrayList<>();

    public Exchanger() {
        prepareCurrencies();
    }

    private void prepareCurrencies() {
        for (int i = 0; i < CURRENCY_CODES.length; i++) {
            Currency currency = new Currency(CURRENCY_CODES[i], CURRENCY_LABELS[i], CURRENCY_DEFAULT_RATES[i]);
            currencies.add(currency);
        }
    }

    public void selectActiveCurrency() {
        String[] options = currencies
                .stream()
                .map(Currency::getName)
                .toArray(String[]::new);
        System.out.println("Выберите валюту для конвертации в другие валюты:");
        int num = Utils.launchMenu(options);
        activeCurrency = currencies.get(num - 1);
        System.out.format("Выбрана валюта для конвертации: %s%n", activeCurrency.getName());
    }

    public void useDefaultRates() {
        System.out.println("Выбраны курсы валют по умолчанию:");
        String activeCurrName = activeCurrency.getName();
        for (Currency currency : currencies) {
            if (currency.getName().equals(activeCurrName)) {
                continue;
            }
            System.out.format("1 %s = %,.2f %s%n",
                    activeCurrName,
                    currency.getRate(),
                    currency.getCode()
            );
        }
    }

    public void inputRates() {
        String activeCurrCode = activeCurrency.getCode();
        for (Currency currency : currencies) {
            if (currency.getCode().equals(activeCurrCode)) {
                continue;
            }
            Double rate = Utils.scanDouble(String.format("Введите курс конвертации %s к %s:",
                    activeCurrCode,
                    currency.getCode()
            ));
            currency.setRate(rate);
        }
    }

    public void convert() {
        String workingCurrCode = activeCurrency.getCode();
        Double amount = Utils.scanDouble(String.format("Введите количество %s для конвертации в другие валюты:",
                workingCurrCode
        ));
        for (Currency currency : currencies) {
            if (currency.getCode().equals(workingCurrCode)) {
                continue;
            }
            System.out.format("%,.2f %s = %,.2f %s (%s)%n",
                    amount,
                    workingCurrCode,
                    currency.convert(amount),
                    currency.getCode(),
                    currency.getName()
            );
        }
    }
}
