public class DecryptionFunctions {
    byte[] rightBits = new byte[48];
    byte[] leftBits = new byte[48];
    String newLeftBits;
    String newRightBits;
    XORFunction xorFunction = new XORFunction();
    MergingFunction mergingFunction = new MergingFunction();
    ScrambleFunctions scrambleFunctions = new ScrambleFunctions();
    SetKey setKey = new SetKey();

    /**
     * This function applies the ECB decryption mode
     *
     * @param firstKey
     * @param inputFileRequirements
     */
    void ECBDecryption(String firstKey, inputFileRequirements inputFileRequirements) {

        /* Calculating the amount of 96 bit pieces in the binary text */
        int big_round = inputFileRequirements.getCipherText().length() / 96;

        for (int m = 0; m < big_round; m++) {
            int temp = m;
            if (m != 0) {
                inputFileRequirements.setDecodedKey(firstKey); // keeping the first key in order to use in the other rounds
                m = m * 96;
            }

            /* Shifting the bits of the key to left 10 times to be able to start from the last round*/
            for (int p = 0; p < 10; p++) {
                char deneme = inputFileRequirements.getDecodedKey().charAt(0);
                inputFileRequirements.setDecodedKey(inputFileRequirements.getDecodedKey().substring(1) + deneme);
            }

            for (int i = m; i < (m + 48); i++) {
                /* Converting and dividing the binary text string into byte arrays */
                rightBits[i % 48] = Byte.parseByte(inputFileRequirements.getCipherText().substring(i + 48, i + 49));
                leftBits[i % 48] = Byte.parseByte(inputFileRequirements.getCipherText().substring(i, i + 1));

                if ((i == 47) || ((i + 1) % 48 == 0)) {
                    for (int round = 0; round < 10; round++) {
                        byte[] finalByteArr = scrambleFunctions.stringToByteArray(scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(leftBits, setKey.setKey(round, inputFileRequirements, "dec"))));
                        newLeftBits = xorFunction.XORFunction(rightBits, finalByteArr);

                        /* Assigning the string to a byte array*/
                        for (int p = 0; p < 48; p++) {
                            rightBits[p] = leftBits[p];
                            leftBits[p] = Byte.parseByte(newLeftBits.substring(p, p + 1));
                        }

                        /*Shifting the bits of the key to right*/
                        char deneme = inputFileRequirements.getDecodedKey().charAt(95);
                        inputFileRequirements.setDecodedKey(deneme + inputFileRequirements.getDecodedKey().substring(0, 95));

                        if (round == 9) {

                            /* Printing the end merged result of single 96 bits piece */
                            System.out.print(mergingFunction.Merge(leftBits, rightBits));
                        }
                    }
                }
            }
            m = temp;
        }
    }


    /**
     * This function applies the ECB decryption mode
     *
     * @param firstKey
     * @param inputFileRequirements
     */
    void CBCDecryption(String firstKey, inputFileRequirements inputFileRequirements) {
        byte[] cipherTextForNextRound = new byte[96];
        byte[] initializationVector96bit = new byte[96];
        for (int j = 0; j < 96; j++) {
            initializationVector96bit[j] = 1;
        }

        /* Calculating the amount of 96 bit pieces in the binary text */
        int big_round = inputFileRequirements.getCipherText().length() / 96;

        for (int m = 0; m < big_round; m++) {
            int temp = m;
            if (m != 0) {
                inputFileRequirements.setDecodedKey(firstKey); // keeping the first key in order to use in the other rounds
                System.arraycopy(cipherTextForNextRound, 0, initializationVector96bit, 0, 96);
                m = m * 96;
            }

            /* Shifting the bits of the key to left 10 times to be able to start from the last round*/
            for (int p = 0; p < 10; p++) {
                char deneme = inputFileRequirements.getDecodedKey().charAt(0);
                inputFileRequirements.setDecodedKey(inputFileRequirements.getDecodedKey().substring(1) + deneme);
            }

            for (int i = m; i < (m + 48); i++) {
                /* Converting and dividing the binary text string into byte arrays */
                rightBits[i % 48] = Byte.parseByte(inputFileRequirements.getCipherText().substring(i + 48, i + 49));
                leftBits[i % 48] = Byte.parseByte(inputFileRequirements.getCipherText().substring(i, i + 1));
                cipherTextForNextRound[(i % 96)] = leftBits[i % 48];
                cipherTextForNextRound[(i % 96) + 48] = rightBits[i % 48];

                if ((i == 47) || ((i + 1) % 48 == 0)) {

                    for (int round = 0; round < 10; round++) {

                        byte[] finalByteArr = scrambleFunctions.stringToByteArray(scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(leftBits, setKey.setKey(round, inputFileRequirements, "dec"))));

                        newLeftBits = xorFunction.XORFunction(rightBits, finalByteArr);
                        for (int p = 0; p < 48; p++) {
                            rightBits[p] = leftBits[p];
                            leftBits[p] = Byte.parseByte(newLeftBits.substring(p, p + 1));
                        }

                        /* Shifting the bits of the key to right */
                        char deneme = inputFileRequirements.getDecodedKey().charAt(95);
                        inputFileRequirements.setDecodedKey(deneme + inputFileRequirements.getDecodedKey().substring(0, 95));

                        if (round == 9) {
                            byte[] lastRoundPlainText = new byte[96];
                            for (int j = 0; j < 96; j++) {
                                if (j < 48) {
                                    lastRoundPlainText[j] = leftBits[j];
                                } else {
                                    lastRoundPlainText[j] = rightBits[j - 48];
                                }
                            }
                            /* Printing the end merged result of single 96 bits piece */
                            System.out.print(xorFunction.XORFunction(initializationVector96bit, lastRoundPlainText));
                        }
                    }
                }
            }
            m = temp;
        }
    }


    /**
     * This function applies the ECB decryption mode
     *
     * @param firstKey
     * @param inputFileRequirements
     * @param initializationVectorLeft
     * @param initializationVectorRight
     */
    void OFBDecryption(String firstKey, inputFileRequirements inputFileRequirements, byte[] initializationVectorLeft, byte[] initializationVectorRight) {
        byte[] cipherTextTemporar_Byte = new byte[96];
        byte[] plainText = new byte[96];
        int big_round = inputFileRequirements.getCipherText().length() / 96;
        for (int m = 0; m < big_round; m++) {
            int temp = m;
            if (m != 0) {
                for (int j = 0; j < 96; j++) {
                    if (j < 48) {
                        initializationVectorLeft[j] = cipherTextTemporar_Byte[j];
                    } else {
                        initializationVectorRight[j - 48] = cipherTextTemporar_Byte[j];
                    }
                }
                inputFileRequirements.setDecodedKey(firstKey); // keeping the first key in order to use in the other rounds
                m = m * 96;
            }
            for (int i = m; i < (m + 48); i++) {

                rightBits[i % 48] = Byte.parseByte(inputFileRequirements.getCipherText().substring(i + 48, i + 49));
                leftBits[i % 48] = Byte.parseByte(inputFileRequirements.getCipherText().substring(i, i + 1));

                if ((i == 47) || ((i + 1) % 48 == 0)) {

                    for (int k = 0; k < 96; k++) {
                        if (k < 48) {
                            plainText[k] = leftBits[k];
                        } else {
                            plainText[k] = rightBits[k - 48];
                        }
                    }
                    for (int round = 0; round < 10; round++) {
                        char deneme = inputFileRequirements.getDecodedKey().charAt(0);
                        inputFileRequirements.setDecodedKey(inputFileRequirements.getDecodedKey().substring(1) + deneme);

                        byte[] finalByteArr = scrambleFunctions.stringToByteArray(scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(initializationVectorRight, setKey.setKey(round, inputFileRequirements, "dec"))));

                        newRightBits = xorFunction.XORFunction(initializationVectorLeft, finalByteArr);

                        for (int p = 0; p < 48; p++) {
                            initializationVectorLeft[p] = initializationVectorRight[p];
                            initializationVectorRight[p] = Byte.parseByte(newRightBits.substring(p, p + 1));
                        }
                        if (round == 9) {
                            /* Printing the end merged result of single 96 bits piece */
                            System.out.print(xorFunction.XORFunction(scrambleFunctions.stringToByteArray(mergingFunction.Merge(initializationVectorLeft, initializationVectorRight)), plainText));
                        }
                    }
                }
            }
            m = temp;
        }
    }
}