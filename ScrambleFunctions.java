public class ScrambleFunctions {
    XORFunction xorFunction = new XORFunction();

    /**
     * This function substitutes the 6 bits into 4 bits and then preforms the swap operation and returns 48 character string
     *
     * @param s
     * @return
     */
    String PermuteAfterXOR(String s) {
        StringBuilder finalString = new StringBuilder();
        String[] permuteAfterXOR = new String[12];
        String[] splitXOR = s.split("(?<=\\G.{6})");
        System.arraycopy(splitXOR, 0, permuteAfterXOR, 0, 8);

        permuteAfterXOR[8] = xorFunction.XORFunction(stringToByteArray(permuteAfterXOR[0]), stringToByteArray(permuteAfterXOR[1])); // XOR to create the last 4 boxes required
        permuteAfterXOR[9] = xorFunction.XORFunction(stringToByteArray(permuteAfterXOR[2]), stringToByteArray(permuteAfterXOR[3]));
        permuteAfterXOR[10] = xorFunction.XORFunction(stringToByteArray(permuteAfterXOR[4]), stringToByteArray(permuteAfterXOR[5]));
        permuteAfterXOR[11] = xorFunction.XORFunction(stringToByteArray(permuteAfterXOR[6]), stringToByteArray(permuteAfterXOR[7]));

        for (int k = 0; k < 12; k++) {
            String temp = Integer.toBinaryString(swapBits(binaryToDecimal(Integer.parseInt(sbox(permuteAfterXOR[k])))));
            if (temp.length() % 4 == 1) {
                finalString.append("000");
            } else if (temp.length() % 4 == 2) {
                finalString.append("00");
            } else if (temp.length() % 4 == 3) {
                finalString.append('0');
            }
            finalString.append(temp);
        }
        return finalString.toString();
    }

    /**
     * This function swaps the even bits with previous odd bits of the result of the scramble function
     *
     * @param x
     * @return
     */
    int swapBits(int x) {
        int even_bits = x & 0xAAAAAAAA;
        int odd_bits = x & 0x55555555;
        even_bits >>= 1;
        odd_bits <<= 1;
        return (even_bits | odd_bits);
    }

    /**
     * This function checks the corresponding 4 bits to the 6 bits need to be substituted and substitutes them with each other.
     *
     * @param s
     * @return
     */
    String sbox(String s) {
        StringBuilder sb = new StringBuilder();
        char outer1 = s.charAt(0);
        char outer2 = s.charAt(5);
        sb.append(outer1);
        sb.append(outer2);
        String outerBits = sb.toString();
        String middle4bit = s.substring(1, 5);
        return subBox(binaryToDecimal(Integer.parseInt(middle4bit)), binaryToDecimal(Integer.parseInt(outerBits)));
    }

    /**
     * This function includes the substitution box in a matrix form and returns the corresponding 4 bits value
     *
     * @param i1
     * @param i2
     * @return
     */
    String subBox(int i1, int i2) {
        String[][] substitution_box = {
                {"0010", "1100", "0100", "0001", "0111", "1010", "1011", "0110", "1000", "0101", "0011", "1111", "1101", "0000", "1110", "1001"},
                {"1110", "1011", "0010", "1100", "0100", "0111", "1101", "0001", "0101", "0000", "1111", "1010", "0011", "1001", "1000", "0110"},
                {"0100", "0010", "0001", "1011", "1010", "1101", "0111", "1000", "1111", "1001", "1100", "0101", "0110", "0011", "0000", "1110"},
                {"1011", "1000", "1100", "0111", "0001", "1110", "0010", "1101", "0110", "1111", "0000", "1001", "1010", "0100", "0101", "0011"}};
        return substitution_box[i2][i1];
    }

    /**
     * This function turns binary value to a decimal value and returns it as an integer
     *
     * @param n
     * @return
     */
    int binaryToDecimal(int n) {
        int dec_value = 0;
        int base = 1;
        int temp = n;
        while (temp > 0) {
            int last_digit = temp % 10;
            temp = temp / 10;
            dec_value += last_digit * base;
            base = base * 2;
        }
        return dec_value;
    }

    /**
     * This function turns a string to a byte array
     *
     * @param s
     * @return
     */
    byte[] stringToByteArray(String s) {
        byte[] b1 = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            b1[i] = Byte.parseByte(s.substring(i, i + 1));
        }
        return b1;
    }
}