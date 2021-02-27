public class SetKey {
    /**
     * This function sets the key to be used as for the scramble function from the original key's odd or even digit bits
     * according to the round.
     *
     * @param round
     * @param inputFileRequirements
     * @param enc_or_dec
     * @return
     */
    byte[] setKey(int round, inputFileRequirements inputFileRequirements, String enc_or_dec) {
        StringBuilder keyForEvenRounds = new StringBuilder();
        StringBuilder keyForOddRounds = new StringBuilder();
        String key;

        for (int p = 0; p < inputFileRequirements.getDecodedKey().length(); p++) {
            if (p % 2 == 0) {
                keyForEvenRounds.append(inputFileRequirements.getDecodedKey().charAt(p)); // Append the even digit bits to an array
            } else {
                keyForOddRounds.append(inputFileRequirements.getDecodedKey().charAt(p)); // Append the odd digit bits to an array
            }
        }
        if (enc_or_dec.equals("enc")) { // Check if it is an encryption process
            if (round % 2 == 0) {
                key = keyForEvenRounds.toString(); // Set the key to be used from the even bits
            } else {
                key = keyForOddRounds.toString(); // Set the key to be used from the odd bits
            }
        } else { // Check if it is an decryption process
            if (round % 2 != 0) {
                key = keyForEvenRounds.toString(); // Set the key to be used from the even bits
            } else {
                key = keyForOddRounds.toString(); // Set the key to be used from the odd bits
            }
        }

        byte[] keyByteArr = new byte[48];
        for (int p = 0; p < 48; p++) {
            keyByteArr[p] = Byte.parseByte(key.substring(p, p + 1)); // Turn string array to a byte array
        }
        return keyByteArr;
    }
}
