public class inputFileRequirements {
    private String decodedKey; // Variable for the decoded key and it's setter and getter below
    private String originalBinaryText; // Variable for the original binary text key and it's setter and getter below
    private String cipherText; // Variable for the cipher text key and it's setter and getter below

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public String getDecodedKey() {
        return decodedKey;
    }

    public void setDecodedKey(String decodedKey) {
        this.decodedKey = decodedKey;
    }

    public String getOriginalBinaryText() {
        return originalBinaryText;
    }

    public void setOriginalBinaryText(String originalBinaryText) {
        this.originalBinaryText = originalBinaryText;
    }
}