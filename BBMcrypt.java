import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class BBMcrypt {
    public static void main(String[] args) throws IOException {
        PrintStream output;
        String encORdec = args[0], mode = "", keyFile = "", inputFile = "";
        for (int i = 1; i < 9; i += 2) {
            switch (args[i]) {
                case "-K": // checking if the argument is -K and saving the next argument as the key
                    keyFile = args[i + 1];
                    break;
                case "-I": // checking if the argument is -I and saving the next argument as the input
                    inputFile = args[i + 1];
                    break;
                case "-O": // checking if the argument is -O and creating an output file names as the next argument
                    output = new PrintStream(new File(args[i + 1]));
                    System.setOut(output); // setting the system output to the output file created
                    break;
                case "-M": // checking if the argument is -M and saving the next argument as the mode
                    mode = args[i + 1];
                    break;
            }
        }

        inputFileRequirements inputFileRequirements = new inputFileRequirements();
        ReadFromFile ReadFromFile = new ReadFromFile();
        ReadFromFile.readFile(keyFile, inputFile, encORdec, mode, inputFileRequirements);
    }
}