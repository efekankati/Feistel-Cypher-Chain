public class XORFunction {

    /**
     * This function 'xor'es two byte arrays and returns the 'xor'ed array as a string
     *
     * @param b1 first byte array to be xored
     * @param b2 second byte array to be xored
     * @return byte array converted to string
     */
    String XORFunction(byte[] b1, byte[] b2) {
        byte[] b3 = new byte[b1.length];
        int i = 0;
        for (byte b : b1) {
            b3[i] = (byte) (b ^ b2[i++]);
        }
        StringBuilder afterXORString = new StringBuilder();
        for (byte b : b3) {
            afterXORString.append(b);
        }
        return afterXORString.toString();
    }

}
