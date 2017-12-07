
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.String;

public class task2 {
    
    static byte[] readFromFile(String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return encoded;
        } catch (IOException e) {
            System.out.println("File Not Found.");
            return null;
        }
    }
    
    static void writeToFile(String path, byte[] data) {
        try {
            Files.write(Paths.get(path), data);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
        } catch (IOException e) {
            System.out.println("File Not Found.");
        }
    }

	// Helper functions.
	private static double getCpuTime () {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		// getCurrentThreadCpuTime() returns the total CPU time for the current thread in nanoseconds.
		return bean.isCurrentThreadCpuTimeSupported() ? ((double)bean.getCurrentThreadCpuTime() / 1000000000): 0L;
	}

        static String hexToString(String hex) {
        StringBuilder output = new StringBuilder();
    for (int i = 0; i < hex.length(); i+=2) {
        String str = hex.substring(i, i+2);
        output.append((char)Integer.parseInt(str, 16));
    } // end for
        return output.toString();

        }
 
        
        
	static void testDES(String key, String message) {
		DES des = new DES();
		Object k;
		try {
			k = des.makeKey(key.getBytes(), des.KEY_SIZE);
			String output = des.encrypt(k, message.getBytes());
			// suppress output
			//System.out.println(output);
		} catch (InvalidKeyException e) {
			System.out.println("Invalid Key.");
		}
	}
    
        static String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
            }
        
        private static String binToHex(String bin) {
            return Integer.toHexString(Integer.parseInt(bin, 2));
        }
       
        
        private static BigInteger convertHexToInt(String current){
    
        String currentBin = hexToBin(current);
        currentBin = padLeadingZeros1(currentBin, 64);
        //currentBin = PADDED_MAP.get(currentBin.length()) + currentBin;
        
//        System.out.println("currentBin before parity removal:");
//        System.out.println(currentBin);
//        System.out.println(currentBin.length());

        //System.out.println("currentBin after parity removal:");
        String currentBin7 = removeParity(currentBin);
        //System.out.println(currentBin7);
        //System.out.println("length of currentBin7: " + currentBin7.length());

      //Increment (add 1) to that number.
     
      BigInteger bigInt = new BigInteger(currentBin7, 2);
      
      return bigInt;
    
}
        static String byteToBin(byte s) {
            return String.format("%8s", Integer.toBinaryString(s & 0xFF)).replace(' ', '0');
        }
        
        private static byte[] hexStringToByteArray(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                     + Character.digit(s.charAt(i+1), 16));
                }
            return data;
            }
        
        private static String padLeadingZeros(String nextBstr, int expSize) {
        //pad leading zeroes after you convert your incremented BigInteger to binary string
        while(nextBstr.length() < expSize) nextBstr = "0" + nextBstr;
        return nextBstr;
    }
           // More efficient ZERO PADDING METHOD
        private static String padLeadingZeros1(String nextBstr, int expSize) {
        //nextBstr = PADDED_MAP.get(nextBstr.length()) + nextBstr;
        StringBuilder sb = new StringBuilder(nextBstr);
        while (sb.length() < expSize) {
            sb.insert(0, "0");
        }       nextBstr = sb.toString();
        return nextBstr;
    }
           
           // MORE EFFICIENT ZERO PADDING METHOD
        private static final int MAX_LENGTH = 64;

        private static Map<Integer, String> PADDED_MAP = new HashMap<>(MAX_LENGTH);
        static {
            String padding = "";
            for (int i = MAX_LENGTH; i >= 0; i--) {
                PADDED_MAP.put(i, padding);
                padding += "0";
            }

}
        
        private static String removeParity(String string8) {
        // take the lowest 7 bits of each of the first eight bytes of the key
        String string7 = ""; // string with parity bits removed
        for(int i=0; i<string8.length(); i++){
            if((i%8) == 0) continue; // ignore parity bit
            string7 += string8.charAt(i);
        }
        return string7;
        }
        
        private static String restoreParity(String string7) {
        // take the lowest 7 bits of each of the first eight bytes of the key
        String string8 = ""; // string with parity bits removed
        for(int i=0; i<string7.length(); i++){
            if((i%7) == 0) {
               String stringAdd = "";
               int ones = 0;
               for (int j = i; j < i+7; j++) {
               // add up ones in bits:
                    stringAdd += string7.charAt(j); // build addition string                 
                    if (string7.charAt(j) == '1' )  {
                    ones ++; } 
               } // end for
                   // if odd: add 0 to the beginning
               if (ones%2 == 1){
                    string8 += '0';
                    string8+=stringAdd;
               }
               else {                // if even, change the 1st digit to 1
                    string8 += '1';     
                    string8+=stringAdd;}
              
           // ignore parity bit
               } // end if(i%8)
               } // end for
            return string8;
        } // end restoreParity
        
	static String enumKey(String current) {
        /*Return the next key based on the current key as hex string.
        Step1: Valid Key Extraction and Enumeration
            
        2. Enumerate all the 56-bit valid keys within [K1, K2^28]
        2a convert them to corresponding 64-bit keys by setting parity key bits
         3. compute the next key from previous key

            */
           	      
        //System.out.println("Compare binary value to comptuer binary value below:");
         
        // 1. Extract the valid part of the key :
        //obtain the binary representation of the corresopnding 64-bit number 
                //System.out.println("Inputted string: \n" + current);

        String currentBin = hexToBin(current);
        currentBin = padLeadingZeros1(currentBin, 64);
        //currentBin = PADDED_MAP.get(currentBin.length()) + currentBin;
        
//        System.out.println("currentBin before parity removal:");
//        System.out.println(currentBin);
//        System.out.println(currentBin.length());

        //System.out.println("currentBin after parity removal:");
        String currentBin7 = removeParity(currentBin);
        //System.out.println(currentBin7);
        //System.out.println("length of currentBin7: " + currentBin7.length());

      //Increment (add 1) to that number.
     
      BigInteger bigInt = new BigInteger(currentBin7, 2);
      //System.out.println("bigInt: " + bigInt);

      BigInteger nextB = bigInt.add(BigInteger.ONE);   
      //System.out.println("nextB: " + nextB);
      
       String nextBstr = nextB.toString(2); // convert to String
        //nextBstr = padLeadingZeros1(nextBstr, 56); 
        nextBstr = padLeadingZeros1(nextBstr, 56);


      //System.out.println("nextBin before parity addition:");
        //System.out.println(nextBstr);
        //System.out.println("length of nextBin: " + nextBstr.length());
        
              //add "parity bits" 
        
         //System.out.println("nextBin after parity restoration:");
         String nextBstrP = restoreParity(nextBstr);
        //System.out.println(nextBstrP);
        //System.out.println("length of nextBin: " + nextBstrP.length());
        //System.out.println("in hex:");
       // BigInteger toHex=new BigInteger(nextBstrP,2);
            
        BigInteger b7 = new BigInteger(nextBstrP, 2);
        String finalHex = b7.toString(16);
        finalHex = padLeadingZeros1(finalHex, 16);
        //finalHex = PADDED_MAP.get(finalHex.length()) + finalHex;
        

        //System.out.println("Inputted key: \n" + current);
        //System.out.println("Inputted key length: " + current.length());
        //System.out.println("Final hex length: " + finalHex.length());

//      int nextBin7 = Integer.parseInt(currentBin7, 2); 
//      //System.out.println("nextBin before parity addition:");
//      //System.out.println(Integer.toBinaryString(nextBin7));

      // convert it back to the string of hexadec charcaters and return this string as a next key
	     return finalHex;
	}


        
	public static void main (String[] args) {
		String mode = args[0];
		if (mode.equals("enum_key")) {
			System.out.println(enumKey(args[1]));
		} else if (mode.equals("crack")) {
			// TODO: Add your own code and do whatever you do.
                    if (args.length != 5) {
			System.out.println("Wrong number of arguments!\njava task2 $mode $plainTextFile $cipherTextFile $startKeyString $endKeyString.");
			System.exit(1);
                    } else {
                        //  read the contents of  plaintext and  cipher text blocks from corresponding files, 
			String plainTextFile = args[1];
                        String plainTextReadIn = "";
                        try {
                            plainTextReadIn = new String(Files.readAllBytes(Paths.get(args[1])));
                        } catch (IOException ex) {
                            Logger.getLogger(task2.class.getName()).log(Level.SEVERE, null, ex);
                        }


                        String cipherTextFile = args[2];
                        
                                 String cipherTextReadIn = "";
                        try {
                            cipherTextReadIn = new String(Files.readAllBytes(Paths.get(args[2])));
                        } catch (IOException ex) {
                            Logger.getLogger(task2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        byte[] plainText = readFromFile(plainTextFile);
                        System.out.println("Plain text input: " + plainTextReadIn);
                        System.out.println("ciphertext input: " + cipherTextReadIn);
                        
                        
                        byte[] cipherText = readFromFile(cipherTextFile);
                        
                        //then you assign your key to specified to you first value (right of the two hex string given to you). 
			String startKeyS = args[3];
                        String endKeyS = args[4];	
                        byte[] startKey = hexStringToByteArray(startKeyS);
                        byte[] endKey = hexStringToByteArray(endKeyS);
		        double start = getCpuTime();

//Then you decrypt the ciphertext block using this key, 
                        String currentKeyS = startKeyS;
                        byte[] currentKey = startKey;
                        DES des = new DES();
                        Object keyObj = null;
                        boolean samePlain = false;
                        int n = 0; // loop counter
                        do {
                        try {
                            keyObj = des.makeKey(currentKey, des.KEY_SIZE);
                        } catch (InvalidKeyException ex) {
                            Logger.getLogger(task2.class.getName()).log(Level.SEVERE, null, ex);
                        }
			String decryptedOutput = hexToString(des.decrypt(keyObj, cipherText));
                         byte[] decryptText1bytes = hexStringToByteArray(decryptedOutput);
                        
        
                        //then compare the result with the decrypted text.  
                        samePlain = decryptedOutput.equals(plainTextReadIn);
                        if (samePlain) {
                            //return the correct key hex string 
                            System.out.println("Correct Key Found!:");
                            System.out.println(currentKeyS);
                            System.out.println("lower key: " + startKeyS + "\n upper key: " + endKeyS);
                                           System.out.println("Decrypted plaintext output (and correct plaintext output below): \n" + decryptedOutput + "\n" + plainTextReadIn);
                            break;
                        }
                        else {
                        //check if current key hex string != to upper limit
                            //if (Objects.equals(currentKey,endKeyS)){
                             if (currentKeyS.equalsIgnoreCase(endKeyS)){
                                //  return from your process stating that you did not find the key in your key space area.                
                                System.out.println("Key not found within key space area:");
                                System.out.println("lower key: " + startKeyS + "\n upper key: " + endKeyS);
                                                System.out.println("Decrypted plaintext output (and correct plaintext output below): \n" + decryptedOutput + "\n" + plainTextReadIn);
                                break;
                            } // end if
                            else {                
                            //feed current key to  enumKey() function and produce hex string for the next key. And begin the process again with the new key in the same way. 
                            currentKeyS = enumKey(currentKeyS);
                            currentKey =  hexStringToByteArray(currentKeyS);
                            } // end inner else
                            
                        } // end else
                        
                        int interval = 1000000;
                        if (n%interval == 0) { // print every cycle million cycles
                            System.out.println("currentKey:" +currentKeyS);                       double end = getCpuTime();
                            System.out.printf("Consumed CPU time=%f\n", end - start);
                                        System.out.println("Decrypted plaintext output (and correct plaintext output below): \n" + decryptedOutput + "\n" + plainTextReadIn);
                        } // end if
                        
                        // print output to a file
//                        PrintStream out = null;
//                            try {
//                                out = new PrintStream(new FileOutputStream("output.txt"));
//                            } catch (FileNotFoundException ex) {
//                                Logger.getLogger(task2.class.getName()).log(Level.SEVERE, null, ex);
//                                }
////System.setOut(out);
    
                        n++;
                        
                        } while(!samePlain);  // end do while
                     
			// Calculate the CPU cycles.
			double end = getCpuTime();
			System.out.printf("Consumed CPU time=%f\n", end - start);
			//writeToFile(outfile, output);
                        
                        BigInteger lowerInt = convertHexToInt(startKeyS);
            BigInteger upperInt = convertHexToInt(endKeyS);
            //BigInteger upperIntAdd1 = lowerInt.add(BigInteger 67108864);
            
            BigInteger diff = upperInt.subtract(lowerInt);
            System.out.println("Lower key in int: " + lowerInt);
            System.out.println("Upper key in int: " + upperInt);
            
            System.out.println("# of tried keys: " + n);
                    }  
		} else {
			System.out.println("Wrong mode!");
		}

	}
}
