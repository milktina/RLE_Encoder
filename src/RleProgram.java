import java.util.Arrays;
import java.util.Scanner;

public class RleProgram {

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        // 1. Display welcome message
        System.out.println("Welcome to the RLE image encoder!");
        System.out.println(" ");

        // 2. Display color test with the message
        System.out.println("Displaying Spectrum Image:");
        System.out.println(" ");
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);
        System.out.println(" ");

        byte[] imageData = null;

        //RLE menu
        System.out.println("RLE Menu");
        System.out.println("--------");
        System.out.println("0. Exit");
        System.out.println("1. Load File");
        System.out.println("2. Load Test Image");
        System.out.println("3. Read RLE String");
        System.out.println("4. Read RLE Hex String");
        System.out.println("5. Read Data Hex String");
        System.out.println("6. Display Image");
        System.out.println("7. Display RLE String");
        System.out.println("8. Display Hex RLE Data");
        System.out.println("9. Display Hex Flat Data");
        System.out.println(" ");
        System.out.println("Select a Menu Option: ");

        //variable for user input as an integer
        int userInput = Integer.parseInt(scnr.next());
        byte[] userInputByte = null;
        String userInputString;

        boolean test = true;

        //loops RLE program
        while (test) {
            switch (userInput) {

                // 3.0 - Option 0
                // exits the program
                case 0:
                    System.exit(0);

                // 3.1 - Option 1
                // load file using ConsoleGFx.loadFile(userInput) and you want to store the returned byte[] into the imageData
                case 1:
                    System.out.println("Enter name of file to load: ");
                    String filename = new String(scnr.next());
                    imageData = ConsoleGfx.loadFile(filename);


                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;

                // 3.2 - Option 2
                // store ConsoleGFx.testImage to the imageData
                case 2:
                    imageData = ConsoleGfx.testImage;
                    System.out.println("Test image data loaded.");

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;

                // 3.3 - Option 3
                // reads RLE data from the user in decimal notation with delimiters (smiley example):
                case 3:
                    System.out.println("Enter an RLE string to be decoded: ");
                    userInputString = scnr.next();
                    userInputByte = stringToRle(userInputString);
                    userInputByte = decodeRle(userInputByte);

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;
                    break;

                // 3.4 - Option 4
                // reads RLE data from the user in hexadecimal notation without delimiters (smiley example):
                case 4:
                    System.out.println("Enter the hex string holding RLE data: ");
                    userInputString = scnr.next();
                    userInputByte = stringToData(userInputString);
                    userInputByte = decodeRle(userInputByte);

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;

                // 3.5 - Option 5
                // reads raw (flat) data from the user in hexadecimal notation (smiley example):
                case 5:
                    System.out.println("Enter the hex string holding flat data: ");
                    userInputString = scnr.next();
                    userInputByte = stringToData(userInputString);

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;

                // 3.6 - Option 6
                // display image stored inside of imageData array
                case 6:
                    System.out.println("Displaying image...");
                    ConsoleGfx.displayImage(imageData);

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;

                // 3.7 - Option 7
                // converts the current data into a human-readable RLE representation (with delimiters)
                case 7:
                    userInputByte = encodeRle(userInputByte);
                    userInputString = toRleString(userInputByte);
                    System.out.println("RLE representation: " + userInputString);

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;

                // 3.8 - Option 8
                // converts the current data into RLE hexadecimal representation (without delimiters)
                // prints:
                case 8:
                    userInputByte = encodeRle(userInputByte);
                    userInputString = toHexString(userInputByte);
                    System.out.println("RLE hex values: " + userInputString);

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;

                // 3.9 - Option 9
                // displays the current raw (flat) data in hexadecimal representation (without delimiters):
                case 9:
                    userInputString = toHexString(userInputByte);
                    System.out.println("Flat hex values: " + userInputString);

                    rleMenu();
                    userInput = Integer.parseInt(scnr.next());
                    test = true;

                    break;
            }
        }
    }

    // method #1: translates data (RLE or raw) to a hexadecimal string (without delimiters). can also aid debugging.
    public static String toHexString(byte[] data) {
        String toHex = "";
        int i;
        for (i = 0; i < data.length; i++) { // loops through byte array
            if (data[i] >= 10 && data[i] <= 15) { // checks to see if value is an alphabetic
                if (data[i] == 10) {
                    toHex += "a";
                }
                if (data[i] == 11) {
                    toHex += "b";
                }
                if (data[i] == 12) {
                    toHex += "c";
                }
                if (data[i] == 13) {
                    toHex += "d";
                }
                if (data[i] == 14) {
                    toHex += "e";
                }
                if (data[i] == 15) {
                    toHex += "f";
                }
                // if value at index is not a character, the value of the digit is added to the byte array
            } else {
                toHex += data[i];
            }
        }

        return toHex; // returns a string with the hexadecimal value
    }

    // method #2: returns number of runs of data in an image data set; double this result for length of encoded (RLE) byte array
    public static int countRuns(byte[] flatData) {
        int count = 1;
        int group = 1;
        for (int i = 0; i < flatData.length - 1; i++) { // loops through the byte array
            if (flatData[i] == flatData[i + 1]) {
                count++;

            } else {
                count = 1; // restarts the count if values change
                group++;
            }

            if (count >= 15) {
                count = 1; // restarts the count if the number of values in an array exceeds 15
                group++;

            }
        }
        return group; // returns the integer number of groups
    }

    // method #3: returns encoding (in RLE) of the raw data passed in; used to generate RLE representation of a data
    public static byte[] encodeRle(byte[] flatData) {
        byte[] rleData = new byte[countRuns(flatData) * 2]; // sets the size of the array equal to the number of groups in method two times two to account for two types of data for one value
        byte count = 1;
        int index = 0;
        for (int i = 0; i < flatData.length - 1; i++) { // loops through the array
            if (flatData[i] == flatData[i + 1]) { // checks if values next to each other are equal
                count++;
                if (count >= 15) { // if there are more than 15 values in an array, the count restarts
                    rleData[index++] = count;
                    rleData[index++] = flatData[i];
                    count = 0;
                }
            } else if (flatData[i] != flatData[i + 1]) { // if two values next to each other are not the same, the count restarts
                rleData[index++] = count;
                rleData[index++] = flatData[i];
                count = 1;

            }

            if ((i + 1) == flatData.length - 1) { // checks for the last value in the array
                rleData[index++] = count;
                rleData[index++] = flatData[i + 1];
                break;

            }
        }
        return rleData;
    }


    // method #4: returns decompressed RLE data; used to generate flat data from RLE encoding
    public static int getDecodedLength(byte[] rleData) {
        int result = 0;
        for (int i = 0; i < rleData.length; i++) { // loops through the array
            if (i % 2 == 0) { // at even indices
                result += rleData[i];
            }
        }
        return result; // returns an integer showing how many values are in an array when decoded

    }

    // method #5: returns the decoded data set from RLE encoded data. this decompresses RLE data for use
    public static byte[] decodeRle(byte[] rleData) {
        int size = getDecodedLength(rleData); // the size of the byte is equal to the decoded length
        int index = 0;

        byte[] result = new byte[size];

        for (int i = 0; i < rleData.length; i++) { // loops through the array
            if (i % 2 == 0) { // checks for number of counts at even values
                int repeats = rleData[i];
                size += rleData[i];
                int value = rleData[i + 1];
                for (int j = 0; j < repeats; j++) { // repetitively adds the same values to the array for the number of times they repeat
                    result[index] = (byte) value;
                    index++;
                }
            }
        }
        return result; // returns the byte with the decoded data
    }


// method #6: translates a string in hexadecimal form into byte data (can be raw or RLE)(inverse of method #1)
    public static byte[] stringToData(String dataString) {
        byte result[] = new byte[dataString.length()]; // the length of the byte is that of the byte passed through
        int i;
        for (i = 0; i < dataString.length(); i++) { // loops through the byte for the length of the string
            if (Character.isAlphabetic((dataString.charAt(i)))) { // checks if a character at an index is an alphabetic character
                switch (dataString.charAt(i)) {// converts hexadecimal values past 9 to their decimal equivalents
                    case 'a':
                        result[i] = 10;
                        break;

                    case 'A': // checks uppercase
                        result[i] = 10;
                        break;

                    case 'b':
                        result[i] = 11;
                        break;

                    case 'B': // checks uppercase
                        result[i] = 11;
                        break;

                    case 'c':
                        result[i] = 12;
                        break;

                    case 'C': // checks uppercase
                        result[i] = 12;
                        break;

                    case 'd':
                        result[i] = 13;
                        break;

                    case 'D': // checks uppercase
                        result[i] = 13;
                        break;

                    case 'e':
                        result[i] = 14;
                        break;

                    case 'E': // checks uppercase
                        result[i] = 14;
                        break;

                    case 'f':
                        result[i] = 15;
                        break;

                    case 'F': // checks uppercase
                        result[i] = 15;
                        break;
                }
            }
            else { // if the character is not an alphabetic character, then the numeric value of the character will be added to the byte array
                    result[i] = (byte) Character.getNumericValue(dataString.charAt(i));
                }
            }
        return result; // returns the byte holding the decimal equivalent of a hexadecimal
    }

// method 7: translates RLE data into a human-readable representation. for each run, in order, it should display the run
//length in decimal (1-2 digits); the run value in hexadecimal (1 digit); and a delimiter, ‘:’, between runs.
    public static String toRleString(byte[] rleData) {
        String hrRepresent = "";
        for (int i = 0; i < rleData.length; i++) { // loops through the array
            if (i % 2 == 0) { // checks if values next to each other are equal
                hrRepresent += rleData[i];
            }
            if (i % 2 != 0) {
                if (rleData[i] == 10) {// converts decimal values past 9 to their hexadecimal equivalents
                    hrRepresent += 'a';
                } else if (rleData[i] == 11) {
                    hrRepresent += 'b';
                } else if (rleData[i] == 12) {
                    hrRepresent += 'c';
                } else if (rleData[i] == 13) {
                    hrRepresent += 'd';
                } else if (rleData[i] == 14) {
                    hrRepresent += 'e';
                } else if (rleData[i] == 15) {
                    hrRepresent += 'f';
                } else {
                    hrRepresent += rleData[i]; // if the value is not alphabetic, then it prints out the value already there
                }
                    if (i != rleData.length - 1) {
                        hrRepresent += ':';
                    }
                    if (i == rleData.length - 1) { // if it is the last value, the case breaks
                        break;
                    }
                }
            }
        return hrRepresent; // returns the byte hrRepresent
        }

// method 8: translates a string in human-readable RLE format (with delimiters) into RLE byte data. (Inverse of #7)
    public static byte[] stringToRle(String rleString) {
        byte[] rleFormat = {}; // output byte
        int count = 0;
        int var3 = 3;
        int var2 = 2;
        int index2 = 0;
        int index3 = 0;
        int i;

        for (i = 0; i < rleString.length(); i++) { // loops through the byte for the length of the string

            if (rleString.charAt(i) == ':') {
                count++;
                rleFormat = new byte[(count * 2) + 2]; // gets size for byte
            }
        }
        for (i = 0; i < rleString.length(); i++) { // loops through byte array
            for (int j = 0; j < rleString.length(); j++) { // loops through byte array
                if (rleString.charAt(j) == ':' && j == (2 + index2)) { // checks if there are 2 values to check for one digit numbers
                    byte oneDigs = (byte) Character.getNumericValue(rleString.charAt(j - 2));

                    rleFormat[j - (var2)] = oneDigs;

                    byte[] convertedHex2 = {};
                    convertedHex2 = stringToData(String.valueOf(rleString.charAt(j - 1))); // converts hexadecimal value into its decimal equivalent

                    int hexValue2 = convertedHex2[0];
                    rleFormat[j - (var2 - 1)] = (byte) hexValue2;
                    index2 += 3;
                    index3 += 3;
                    var2++;
                    var3++;
                }

                if (rleString.charAt(j) == ':' && j == (3 + index3)) { // checks if there are 3 values to check for two digit numbers
                    byte twoDigs = (byte) Character.getNumericValue(rleString.charAt(j - 2));
                    twoDigs += 10;

                    rleFormat[j - (var3)] = twoDigs;

                    byte[] convertedHex3 = {};
                    convertedHex3 = stringToData(String.valueOf(rleString.charAt(j - 1))); // converts hexadecimal value into its decimal equivalent
                    int hexValue3 = convertedHex3[0];
                    rleFormat[j - (var3 - 1)] = (byte) hexValue3;
                    var3 += 2;
                    var2 += 2;
                    index2 += 4;
                    index3 += 4;
                }
            }
        }
        if (rleString.charAt(rleString.length() - 3)==':') { // edge case to see if there is a two digit decimal number
            rleFormat[rleFormat.length-2] = (byte) Character.getNumericValue(rleString.charAt(rleString.length() - 2));
            rleFormat[rleFormat.length-1] = stringToData(String.valueOf(rleString.charAt(rleString.length() - 1)))[0];
        } else { // edge case to see if there is a one digit number
            rleFormat[rleFormat.length-2] =  (byte) (Character.getNumericValue(rleString.charAt(rleString.length() - 2)) + 10);
            rleFormat[rleFormat.length-1] = stringToData(String.valueOf(rleString.charAt(rleString.length() - 1)))[0];
        }

        return rleFormat;
        }

// method to print out rleMenu
        private static void rleMenu () {
            System.out.println(" ");
            System.out.println("RLE Menu");
            System.out.println("--------");
            System.out.println("0. Exit");
            System.out.println("1. Load File");
            System.out.println("2. Load Test Image");
            System.out.println("3. Read RLE String");
            System.out.println("4. Read RLE Hex String");
            System.out.println("5. Read Data Hex String");
            System.out.println("6. Display Image");
            System.out.println("7. Display RLE String");
            System.out.println("8. Display Hex RLE Data");
            System.out.println("9. Display Hex Flat Data");
            System.out.println(" ");
            System.out.println("Select a Menu Option: ");
        }
    }





