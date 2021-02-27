public class MergingFunction {

    /**
     * This function merges the left and right 48 bits in the second round in order to make a 96 bit value at the end and
     * returns the value
     *
     * @param lefts
     * @param rights
     * @return
     */
    public String Merge(byte[] lefts, byte[] rights) {
        byte[] mergedByteArray = new byte[96];
        StringBuilder merged = new StringBuilder();
        for (int i = 0; i < 96; i++) {
            if (i <= 47) {
                mergedByteArray[i] = lefts[i];
            } else {
                mergedByteArray[i] = rights[i - 48];
            }
        }
        for (byte b : mergedByteArray) {
            merged.append(b);
        }
        return merged.toString();
    }
}
