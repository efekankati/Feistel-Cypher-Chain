import java.io.IOException;

public class EncryptionFunctions {
    byte[] rightBits = new byte[48];
    byte[] leftBits = new byte[48];
    String key = "", newRightBits;
    XORFunction xorFunction = new XORFunction();
    MergingFunction mergingFunction = new MergingFunction();
    ScrambleFunctions scrambleFunctions = new ScrambleFunctions();
    SetKey setKey = new SetKey();

    void ECBEncryption(String firstKey, inputFileRequirements inputFileRequirements) {
        int big_round = inputFileRequirements.getOriginalBinaryText().length() / 96;
        for (int m = 0; m < big_round; m++) {
            int temp = m;
            if (m != 0) {
                inputFileRequirements.setDecodedKey(firstKey);
                m = m * 96;
            }
            for (int i = m; i < (m + 48); i++) {
                rightBits[i % 48] = Byte.parseByte(inputFileRequirements.getOriginalBinaryText().substring(i + 48, i + 49));
                leftBits[i % 48] = Byte.parseByte(inputFileRequirements.getOriginalBinaryText().substring(i, i + 1));

                if ((i == 47) || ((i + 1) % 48 == 0)) {
                    for (int round = 0; round < 10; round++) {
                        char deneme = inputFileRequirements.getDecodedKey().charAt(0);
                        inputFileRequirements.setDecodedKey(inputFileRequirements.getDecodedKey().substring(1) + deneme);

                        String finalString = scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(rightBits, setKey.setKey(round,inputFileRequirements,"enc")));
                        byte[] finalByteArr = scrambleFunctions.stringToByteArray(scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(rightBits, setKey.setKey(round,inputFileRequirements,"enc"))));
                        //System.out.println("SCRAMBLE " + finalString);

                        newRightBits = xorFunction.XORFunction(leftBits, finalByteArr);

                        for (int p = 0; p < 48; p++) {
                            leftBits[p] = rightBits[p];
                            rightBits[p] = Byte.parseByte(newRightBits.substring(p, p + 1));
                        }
                        if (round == 9) {
                            String cipherText = mergingFunction.Merge(leftBits, rightBits);
                            System.out.print(cipherText);
                        }
                    }
                }
            }
            m = temp;
        }
    }

    void CBCEncryption(String firstKey, inputFileRequirements inputFileRequirements, byte[] initializationVectorLeft, byte[] initializationVectorRight){
        String cipherText = "";
        int big_round = inputFileRequirements.getOriginalBinaryText().length() / 96;
        for (int m = 0; m < big_round; m++) {
            int temp = m;
            if (m != 0) {
                inputFileRequirements.setDecodedKey(firstKey);
                for(int p = 0 ; p < 48 ; p++){
                    initializationVectorLeft[p] = Byte.parseByte(cipherText.substring(p,p+1));
                    initializationVectorRight[p] = Byte.parseByte(cipherText.substring(p+48,p+49));
                }
                m = m * 96;
            }
            for (int i = m; i < (m + 48); i++) {
                rightBits[i % 48] = Byte.parseByte(inputFileRequirements.getOriginalBinaryText().substring(i + 48, i + 49));
                leftBits[i % 48] = Byte.parseByte(inputFileRequirements.getOriginalBinaryText().substring(i, i + 1));

                if ((i == 47) || ((i + 1) % 48 == 0)) {
                    String afterInitializationRight = xorFunction.XORFunction(rightBits,initializationVectorRight);
                    String afterInitializationLeft = xorFunction.XORFunction(leftBits,initializationVectorLeft);
                    byte[] afterInitializationByteRight = new byte[48];
                    byte[] afterInitializationByteLeft = new byte[48];

                    for(int p = 0; p < 48 ; p++){
                        afterInitializationByteRight[p] = Byte.parseByte(afterInitializationRight.substring(p,p+1));
                        afterInitializationByteLeft[p] = Byte.parseByte(afterInitializationLeft.substring(p,p+1));
                    }
                    for (int round = 0; round < 10; round++) {
                        char deneme = inputFileRequirements.getDecodedKey().charAt(0);
                        inputFileRequirements.setDecodedKey(inputFileRequirements.getDecodedKey().substring(1) + deneme);

                        String finalString = scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(afterInitializationByteRight, setKey.setKey(round,inputFileRequirements,"enc")));
                        byte[] finalByteArr = scrambleFunctions.stringToByteArray(scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(afterInitializationByteRight, setKey.setKey(round,inputFileRequirements,"enc"))));
                        //System.out.println("SCRAMBLE " + finalString);

                        newRightBits = xorFunction.XORFunction(afterInitializationByteLeft, finalByteArr);

                        for (int p = 0; p < 48; p++) {
                            afterInitializationByteLeft[p] = afterInitializationByteRight[p];
                            afterInitializationByteRight[p] = Byte.parseByte(newRightBits.substring(p, p + 1));
                        }
                        if (round == 9) {
                            cipherText = mergingFunction.Merge(afterInitializationByteLeft, afterInitializationByteRight);
                            System.out.print(cipherText);
                        }
                    }
                }
            }
            m = temp;
        }
    }

    void OFBEncryption(String firstKey, inputFileRequirements inputFileRequirements, byte[] initializationVectorLeft, byte[] initializationVectorRight){
        int big_round = inputFileRequirements.getOriginalBinaryText().length() / 96;
        byte[] plainText = new byte[96];
        for (int m = 0; m < big_round; m++) {
            int temp = m;
            if (m != 0) {
                inputFileRequirements.setDecodedKey(firstKey);
                m = m * 96;
            }
            for (int i = m; i < (m + 48); i++) {
                rightBits[i % 48] = Byte.parseByte(inputFileRequirements.getOriginalBinaryText().substring(i + 48, i + 49));
                leftBits[i % 48] = Byte.parseByte(inputFileRequirements.getOriginalBinaryText().substring(i, i + 1));

                if ((i == 47) || ((i + 1) % 48 == 0)) {
                    for(int k = 0 ; k < 96 ; k++){
                        if (k < 48) {
                            plainText[k] = leftBits[k];
                        }else{
                            plainText[k] = rightBits[k-48];
                        }
                    }
                    for (int round = 0; round < 10; round++) {
                        char deneme = inputFileRequirements.getDecodedKey().charAt(0);
                        inputFileRequirements.setDecodedKey(inputFileRequirements.getDecodedKey().substring(1) + deneme);

                        String finalString = scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(initializationVectorRight, setKey.setKey(round,inputFileRequirements,"enc")));
                        byte[] finalByteArr = scrambleFunctions.stringToByteArray(scrambleFunctions.PermuteAfterXOR(xorFunction.XORFunction(initializationVectorRight, setKey.setKey(round,inputFileRequirements,"enc"))));
                        //System.out.println("SCRAMBLE " + finalString);

                        newRightBits = xorFunction.XORFunction(initializationVectorLeft, finalByteArr);

                        for (int p = 0; p < 48; p++) {
                            initializationVectorLeft[p] = initializationVectorRight[p];
                            initializationVectorRight[p] = Byte.parseByte(newRightBits.substring(p, p + 1));
                        }
                        if (round == 9) {
                            String cipherTextTemporary = mergingFunction.Merge(initializationVectorLeft, initializationVectorRight);
                            byte[] cipherTextTemporar_Byte = scrambleFunctions.stringToByteArray(cipherTextTemporary);
                            String afterXOR_withPlainText = xorFunction.XORFunction(cipherTextTemporar_Byte,plainText);
                            System.out.print(afterXOR_withPlainText);
                        }
                    }
                }
            }
            m = temp;
        }
    }
}
