import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ReadFromFile {
    Base64.Decoder decoder = Base64.getDecoder();
    /* Right 48 bits of the initialization vector*/
    byte[] initializationVectorRight = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    /* Left 48 bits of the initialization vector*/
    byte[] initializationVectorLeft = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    EncryptionFunctions encryptionFunctions = new EncryptionFunctions();
    DecryptionFunctions decryptionFunctions = new DecryptionFunctions();

    /**
     * This function reads the key and input files and saves the required information to the related variables and sends
     * the binary text (whether it is plaintext or ciphertext) and the key according to the the parameter being encryption
     * or decryption and the mode itself whether it is ECB, CBC or OFB.
     *
     * @param keyFile
     * @param inputFile
     * @param encORdec
     * @param mode
     * @param inputFileandRequirements
     * @throws IOException
     */
    public void readFile(String keyFile, String inputFile, String encORdec, String mode, inputFileRequirements inputFileandRequirements) throws IOException {

        for (String line : Files.readAllLines(Paths.get(keyFile))) {
            inputFileandRequirements.setDecodedKey(new String(decoder.decode(line)));
        }

        for (String line : Files.readAllLines(Paths.get(inputFile))) { // read the binary text from the input file
            if (encORdec.equals("enc")) { // Check if the parameter is enc or dec
                inputFileandRequirements.setOriginalBinaryText(ZerosWillBeAdded(line));

                if (mode.equals("ECB")) { // Check if the mode is whether ECB, CBC, or OFB
                    encryptionFunctions.ECBEncryption(inputFileandRequirements.getDecodedKey(), inputFileandRequirements);
                } else if (mode.equals("CBC")) {
                    encryptionFunctions.CBCEncryption(inputFileandRequirements.getDecodedKey(), inputFileandRequirements, initializationVectorLeft, initializationVectorRight);
                } else {
                    encryptionFunctions.OFBEncryption(inputFileandRequirements.getDecodedKey(), inputFileandRequirements, initializationVectorLeft, initializationVectorRight);
                }
            } else if (encORdec.equals("dec")) {
                inputFileandRequirements.setCipherText(ZerosWillBeAdded(line));

                if (mode.equals("ECB")) { // Check if the mode is whether ECB, CBC, or OFB
                    decryptionFunctions.ECBDecryption(inputFileandRequirements.getDecodedKey(), inputFileandRequirements);
                } else if (mode.equals("CBC")) {
                    decryptionFunctions.CBCDecryption(inputFileandRequirements.getDecodedKey(), inputFileandRequirements);
                } else {
                    decryptionFunctions.OFBDecryption(inputFileandRequirements.getDecodedKey(), inputFileandRequirements, initializationVectorLeft, initializationVectorRight);
                }
            }
        }
    }

    /**
     * This function checks if the binary text's length is a product of 96 and if not adds zeros to make it a product of 96
     *
     * @param BinaryText
     * @return
     */
    String ZerosWillBeAdded(String BinaryText) {
        int zeros_will_be_added = BinaryText.length() % 96; // Finding the zeros to be added

        if (zeros_will_be_added != 0) { // if the zeros gonna be added is different from 0 it adds
            StringBuilder final_binary_text = new StringBuilder(BinaryText);
            for (int k = 0; k < 96 - zeros_will_be_added; k++) {
                final_binary_text.append("0");
            }
            return final_binary_text.toString();
        }
        return BinaryText;
    }
}