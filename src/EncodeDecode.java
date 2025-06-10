import java.util.ArrayList;
import java.util.List;

public class EncodeDecode {
    public static List<Character> cryptographicAlphabet;

    static {
        createCryptographicAlphabet();
    }

    private EncodeDecode() {
    }

    public static String encodeDecode(String input, int key) {
        UT.print(key);
        StringBuilder output = new StringBuilder();
        key = ((key % cryptographicAlphabet.size()) + cryptographicAlphabet.size()) % cryptographicAlphabet.size();
        for (char c : input.toCharArray()) {
            if (cryptographicAlphabet.contains(c)) {
                int index = (cryptographicAlphabet.indexOf(c) + key) % cryptographicAlphabet.size();
                output.append(cryptographicAlphabet.get(index));
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

    public static String brute(String input) {
        UT.print("bruteForce");
        StringBuilder output = new StringBuilder();
        int key = 0;
        while (key < cryptographicAlphabet.size()) {
            output.append(key).append("\t");
            int charCount = 0;
            for (char c : input.toCharArray()) {
                if (cryptographicAlphabet.contains(c) && charCount < 200) {
                    int index = (cryptographicAlphabet.indexOf(c) + key) % cryptographicAlphabet.size();
                    output.append(cryptographicAlphabet.get(index));
                    charCount++;
                } else {
                    output.append("\r\n");
                    break;
                }
            }
            key++;
        }
        return output.toString();
    }

    private static void createCryptographicAlphabet() {
        cryptographicAlphabet = new ArrayList<>();
        "(.,””:-!? )АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя".chars()
                .forEach(c -> cryptographicAlphabet.add((char) c));
        UT.print(cryptographicAlphabet);

    }
}
