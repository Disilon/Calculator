import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String inputString = scanner.nextLine();
//        System.out.println(calc(inputString));
        UnitTest();
    }

    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100);

        private final int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((!romanNumeral.isEmpty()) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (!romanNumeral.isEmpty()) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }

        return result;
    }

    public static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 100)) {
            throw new IllegalArgumentException(number + " is not in range (0,100]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static String calc(String input) {

        String regexpA = "^([0-9]{1,2})\\s?([*+\\-/])\\s?([0-9]{1,2})$";
        Pattern inputPatternA = Pattern.compile(regexpA);
        String regexpR = "^([IVX]{1,4})\\s?([*+\\-/])\\s?([IVX]{1,4})$";
        Pattern inputPatternR = Pattern.compile(regexpR);
        Matcher matcherA = inputPatternA.matcher(input);
        Matcher matcherR = inputPatternR.matcher(input);
        String calcType;
        String operator;
        int operand1;
        int operand2;
        int result;
        if(matcherA.matches()){
            calcType = "arabic";
            operator = matcherA.group(2);
            operand1 = Integer.parseInt(matcherA.group(1));
            operand2 = Integer.parseInt(matcherA.group(3));
        }
        else if(matcherR.matches()){
            calcType = "roman";
            operator = matcherR.group(2);
            operand1 = romanToArabic(matcherR.group(1));
            operand2 = romanToArabic(matcherR.group(3));
        }
        else{
            throw new RuntimeException("Неправильный формат ввода");
        }
        if (operand1<1 || operand1>10 || operand2<1 || operand2>10){
            throw new RuntimeException("Числа должны быть от 1 до 10");
        }
        result = switch (operator) {
            case "*" -> operand1 * operand2;
            case "+" -> operand1 + operand2;
            case "-" -> operand1 - operand2;
            case "/" -> operand1 / operand2;
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
        return switch (calcType) {
            case "roman" -> {
                if (result < 1) {
                    throw new RuntimeException("Результат нельзя представить в формате римских цифр");
                }
                yield arabicToRoman(result);
            }
            case "arabic" -> Integer.toString(result);
            default -> throw new IllegalStateException("Unexpected value: " + calcType);
        };
    }

    public static void UnitTest(){
        String[] arr = {"1+1","10 / 3","X*VIII","1+V","I-V"};
        for (String s : arr) {
            try {
                System.out.println("Input:" + s + ";Output:" + calc(s));
            } catch (Exception ex) {
                System.out.println("Input:" + s + ";Exception:" + ex.getMessage());
            }
        }
    }
}