import java.io.*;
import java.util.Scanner;

public class TextSteganography {

    static final char ZERO_WIDTH_SPACE = '\u200B';
    static final char ZERO_WIDTH_NON_JOINER = '\u200C';

    public static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();

        for (char ch : text.toCharArray()) {
            String bin = String.format("%8s",
                    Integer.toBinaryString(ch))
                    .replace(' ', '0');

            binary.append(bin);
        }

        return binary.toString();
    }

    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < binary.length(); i += 8) {
            String byteStr = binary.substring(i, i + 8);
            int charCode = Integer.parseInt(byteStr, 2);
            text.append((char) charCode);
        }

        return text.toString();
    }

    public static void encode(String coverFile,
                              String outputFile,
                              String secretMessage) {

        try {
            BufferedReader br =
                    new BufferedReader(new FileReader(coverFile));

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\\n");
            }

            br.close();

            String binary = textToBinary(secretMessage);

            StringBuilder hiddenData = new StringBuilder();

            for (char bit : binary.toCharArray()) {
                if (bit == '0')
                    hiddenData.append(ZERO_WIDTH_SPACE);
                else
                    hiddenData.append(ZERO_WIDTH_NON_JOINER);
            }

            hiddenData.append("###");

            content.append(hiddenData);

            BufferedWriter bw =
                    new BufferedWriter(new FileWriter(outputFile));

            bw.write(content.toString());
            bw.close();

            System.out.println("Message Hidden Successfully!");

        } catch (Exception e) {
            System.out.println("Encoding Error.");
        }
    }

    public static void decode(String encodedFile) {

        try {
            BufferedReader br =
                    new BufferedReader(new FileReader(encodedFile));

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\\n");
            }

            br.close();

            String text = content.toString();

            StringBuilder binary = new StringBuilder();

            for (char ch : text.toCharArray()) {
                if (ch == ZERO_WIDTH_SPACE)
                    binary.append("0");
                else if (ch == ZERO_WIDTH_NON_JOINER)
                    binary.append("1");
            }

            int length = binary.length() - (binary.length() % 8);

            String validBinary = binary.substring(0, length);

            String secretMessage = binaryToText(validBinary);

            System.out.println("Hidden Message: " + secretMessage);

        } catch (Exception e) {
            System.out.println("Decoding Error.");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice;

        do {
            System.out.println("\\n===== Text Steganography System =====");
            System.out.println("1. Encode Message");
            System.out.println("2. Decode Message");
            System.out.println("3. Exit");

            System.out.print("Enter Choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Cover Text File Name: ");
                    String coverFile = sc.nextLine();

                    System.out.print("Enter Output Encoded File Name: ");
                    String outputFile = sc.nextLine();

                    System.out.print("Enter Secret Message: ");
                    String secret = sc.nextLine();

                    encode(coverFile, outputFile, secret);
                    break;

                case 2:
                    System.out.print("Enter Encoded File Name: ");
                    String encodedFile = sc.nextLine();

                    decode(encodedFile);
                    break;

                case 3:
                    System.out.println("Exiting Program...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 3);

        sc.close();
    }
}
