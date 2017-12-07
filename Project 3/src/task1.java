


import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ByteArrayOutputStream;


public class task1 {
    
    public static byte[] hexArrayToByteArray(byte [] array) {
    int len = array.length;
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(array[i], 16) << 4)
                             + Character.digit(array[i+1], 16));
    }
    return data;
    }
    
    public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
       
    public static byte[] cbc_encrypt(byte[] message, byte[] key, byte[] iv) {
		// TODO: Add your code here.
            //Step 1: convert message into puts messageBlocks with bit padding
            //byte [][] messageBlocks = StringToByteBlocksI.toBlocks(message);  // uses StringToByteBlocks method
        System.out.println("BEGIN ENCRYPTION:\n");

        System.out.println("Input message:");
        System.out.println(Arrays.toString(message));
        int blockSize = 8; //in bytes
        int numBlocks = message.length / blockSize + 1; // +1 is to take into account bit padding
        
        byte[][] messageBlocks = new byte[numBlocks][blockSize]; // initialize array to hold the message messageBlocks
        
        int index = 0;
        boolean append1 = true; //enables padding to append a 1 to the beginning
        for (int i = 0; i < numBlocks; i++) {
            for (int j = 0; j < blockSize; j++) {
                if (index >= message.length) { // add padding since index is beyond message
                    if (append1) {
                        messageBlocks[i][j] = (byte) 128; // append the 10000000 in binary
                        append1 = false;
                    } else { 
                        messageBlocks[i][j] = (byte) 0; //append 00000000 in binary
                    }
                    index++;  
                } else {
                    messageBlocks[i][j] = message[index];
                    index++;
                }
            }
        }
            // Step 2:  - Encrypt with CBC mode  (Cipher Block Chaining)
          
            // start with xor'ing initial block with iv
            
            // convert iv into byte array
            byte[] ivBytes = new byte[16];
            ivBytes = hexArrayToByteArray(iv);
                       
            // convert key into byte array
            byte[] keyBytes = new byte[16];
            keyBytes = hexArrayToByteArray(key);
            System.out.println("keyBytes:");
            System.out.println(Arrays.toString(keyBytes));
            
            byte[] initXor = new byte[8];
            for (int i = 0; i < initXor.length; i++) {
                initXor[i] = (byte) (messageBlocks[0][i] ^ ivBytes[i]);
            }
                      
            System.out.println("1st block of message:");
            System.out.println(Arrays.toString(messageBlocks[0]));

            System.out.println("ivBytes: ");
            System.out.println(Arrays.toString(ivBytes));

            System.out.println("initXor: ");
            System.out.println(Arrays.toString(initXor));

            DES des = new DES(); // instantiate des instance before this call
            Object keyInput = null;
        try {
            keyInput = des.makeKey(keyBytes, DES.KEY_SIZE);
        } catch (InvalidKeyException ex) {
            System.out.println("Invalid Key.");
        }
            
            byte[][] cipherTextByteArrayHex = new byte[numBlocks][blockSize]; // initialize array to hold the ciphcipherTextertext messageBlocks in hex for output
            byte[][] cipherTextByteArray = new byte[numBlocks][blockSize]; // initialize array to hold the ciphcipherTextertext messageBlocks
            
            String cipherText1 = des.encrypt(keyInput, initXor);
                        // convert to byte array
            byte[] cipherText1bytes = hexStringToByteArray(cipherText1);
            cipherTextByteArray[0] = cipherText1bytes;

     
            System.out.println("cipherText1 string: " + cipherText1);      
            System.out.println("cipherText1bytes: ");
            System.out.println(Arrays.toString(cipherText1bytes));

            //loop through each block and xor the remaining blocks
           for (int n = 1; n < messageBlocks.length; n++) {
               
                byte[] initXor1 = new byte[8];
                
                for (int i = 0; i < initXor1.length; i++) {
                    initXor1[i] = (byte) (cipherTextByteArray[n-1][i] ^ messageBlocks[n][i]);                
                } // end inner for
                    cipherText1 = des.encrypt(keyInput, initXor1);
                    cipherText1bytes = hexStringToByteArray(cipherText1); // convert to byte array
                    cipherTextByteArray[n] = cipherText1bytes;
                    //cipherTextByteArrayHex[n] = cipherText1;
            } // end outer for

            // chain Ciphertext back together
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

             //begin final output array
            for (int i = 0; i < cipherTextByteArray.length; i++) {
            try {
                outputStream.write(cipherTextByteArray[i] );
                System.out.println("cipherTextArray[i]: " + i + Arrays.toString(cipherTextByteArray[i]));  

                
                
            } catch (IOException ex) {
                Logger.getLogger(task1.class.getName()).log(Level.SEVERE, null, ex);
            }
                } // end inner for
           
            byte cipherTextOutput[] = outputStream.toByteArray( );
            
            System.out.println("Final cipherTextOutput: " + Arrays.toString(cipherTextOutput));  
            System.out.println("Final cipherTextOutput length: " + cipherTextOutput.length);  
//            byte[] cipherTextOutput = cipherTextByteArray[0];
//            int n = 0;
//            for (byte[] cipherBlock: cipherTextByteArray) {
//                //cipherTextOutput[n] = cipherBlock;
//                n++;
//            }
            
            //write the ciphertext in bytes to the output file
            
            //Step 3: Verification
		//test();
		return cipherTextOutput;
	}

	public static byte[] cbc_decrypt(byte[] message, byte[] key, byte[] iv) {
		// TODO: Add your code here.
                
                //    # Step1:	   Verification
        //
    //    # Step2:	   Cipher	   Block	   Chaining
        System.out.println("/nBEGIN DECYPTION:");

	System.out.println("Input ciphertext:");
        System.out.println(Arrays.toString(message));
        int blockSize = 8; //in bytes
        int numBlocks = message.length / blockSize; // +1 is to take into account bit padding
        byte[][] cipherBlocks = new byte[numBlocks][blockSize]; // initialize array to hold the message cipherBlocks
        int index = 0;
        for (int i = 0; i < numBlocks; i++) {
            for (int j = 0; j < blockSize; j++) {
                    cipherBlocks[i][j] = message[index];
                    index++;
                }
            }
        
            // Step 2:  - Decrypt with CBC mode  (Cipher Block Chaining)
          
           
            // convert iv into byte array
            byte[] ivBytes = new byte[16];
            ivBytes = hexArrayToByteArray(iv);
                       
            // convert key into byte array
            byte[] keyBytes = new byte[16];
            keyBytes = hexArrayToByteArray(key);
            System.out.println("keyBytes:");
            System.out.println(Arrays.toString(keyBytes));
            
            DES des = new DES(); // instantiate des instance before this call
            Object keyInput = null;
        try {
            keyInput = des.makeKey(keyBytes, DES.KEY_SIZE);
        } catch (InvalidKeyException ex) {
            System.out.println("Invalid Key.");
        }
        
            System.out.println("1st block of encryption message:");
            System.out.println(Arrays.toString(cipherBlocks[numBlocks-1]));

            System.out.println("ivBytes: ");
            System.out.println(Arrays.toString(ivBytes));
            

            byte[][] plainTextByteArrayHex = new byte[numBlocks][blockSize]; // initialize array to hold the ciphplainTextertext cipherBlocks in hex for output
            byte[][] plainTextByteArray = new byte[numBlocks][blockSize]; // initialize array to hold the ciphplainTextertext cipherBlocks
            
//                        // convert to byte array
//            byte[] plainText1bytes = hexStringToByteArray(plainText1);
//            plainTextByteArray[0] = plainText1bytes;
//
//     
//            System.out.println("plainText1 string: " + plainText1);      
//            System.out.println("plainText1bytes: ");
//            System.out.println(Arrays.toString(plainText1bytes));

            //loop through each block and xor until the final block
           for (int n = 0; n < cipherBlocks.length-1; n++) {
               
                byte[] XOR = new byte[8];
                String decryptText1 = des.decrypt(keyInput, cipherBlocks[numBlocks-1-n]);    
                byte[] decryptText1bytes = hexStringToByteArray(decryptText1); // convert to byte array
                
                // xor decrypted text with with previous ciphertext
                for (int i = 0; i < XOR.length; i++) {
                    XOR[i] = (byte) (decryptText1bytes[i] ^ cipherBlocks[numBlocks-2-n][i]);                
                } // end inner for
                  
                    plainTextByteArray[numBlocks-1-n] = XOR;
                    //plainTextByteArrayHex[n] = plainText1;
            } // end outer for
           
            System.out.println("plainTextByteArray: ");
            System.out.println(Arrays.toString(plainTextByteArray));

            //decript final block and xor with iv
           
            String decryptTextFinal = des.decrypt(keyInput, cipherBlocks[0]);    
            System.out.println("decryptText1 string: " + decryptTextFinal);    
            
            byte[] decryptTextFinalbytes = hexStringToByteArray(decryptTextFinal); // convert to byte arr
            //xor with iv cypherblock
            
            byte[] finalXor = new byte[8];
            for (int i = 0; i < finalXor.length; i++) {
                finalXor[i] = (byte) (decryptTextFinalbytes[i] ^ ivBytes[i]);
            }
            plainTextByteArray[0] = finalXor;
            System.out.println("finalXor: ");
            System.out.println(Arrays.toString(finalXor));
            // chain Ciphertext back together
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(finalXor);
        } catch (IOException ex) {
            Logger.getLogger(task1.class.getName()).log(Level.SEVERE, null, ex);
        }
             //begin final output array
            for (int i = 1; i < plainTextByteArray.length; i++) {
            try {
                outputStream.write(plainTextByteArray[i] );
                System.out.println("plainTextArray[i]: " + i + Arrays.toString(plainTextByteArray[i]));  
                
            } catch (IOException ex) {
                Logger.getLogger(task1.class.getName()).log(Level.SEVERE, null, ex);
            }
                } // end inner for
           
            byte plainTextOutput[] = outputStream.toByteArray( );
            
            System.out.println("Final plainTextOutput: " + Arrays.toString(plainTextOutput));  
            System.out.println("Final plainTextOutput length: " + plainTextOutput.length);  

            //write the ciphertext in bytes to the output file
            
             //    # Step3:	  Remove Padding after decryption
    //    # When you decrypt correctly, the binary data should end 
        //with one 1 and 63 or less 0's. So removing all 0's off the end and then the 1st 1 you encounter from the end
////        
//         if message.endswith('1'):
         
            int index1 = plainTextOutput.length-1;
          if (plainTextOutput[index1] == (byte) -128) {
               byte[] plainTextOutput1 = Arrays.copyOf(plainTextOutput, plainTextOutput.length-1);
//               System.out.println("Final plainTextOutput with 1 element removed: " + Arrays.toString(plainTextOutput1));  
//            System.out.println("Final plainTextOutput length with 1 element removed: " + plainTextOutput1.length);  
            plainTextOutput = plainTextOutput1;
          }
//        message = message[:-1]
//    else:
          else {
              while (plainTextOutput[index1] == (byte) 0) {
                  index1 --;
              }
              if (plainTextOutput[index1] == (byte) -128) {
              byte[] plainTextOutput1 = Arrays.copyOf(plainTextOutput, index1);
//               System.out.println("Final plainTextOutput with 1 element removed: " + Arrays.toString(plainTextOutput1));  
//            System.out.println("Final plainTextOutput length with 1 element removed: " + plainTextOutput1.length);  
            plainTextOutput = plainTextOutput1;
          }
          }

            System.out.println("Final plainTextOutput with PADDING REMOVED: ");  
            System.out.println("Final plainTextOutput: " + Arrays.toString(plainTextOutput));  
            System.out.println("Final plainTextOutput length: " + plainTextOutput.length);  
        
		test();
		return plainTextOutput;
	}

	public static void main (String[] args) {
		if (args.length != 5) {
			System.out.println("Wrong number of arguments!\njava task1 $MODE $INFILE $KEYFILE $IVFILE $OUTFILE.");
			System.exit(1);
		} else {
			String mode = args[0];
			String infile = args[1];
			String keyfile = args[2];
			String ivfile = args[3];
			String outfile = args[4];
			byte[] input = readFromFile(infile);
			byte[] key = readFromFile(keyfile);
			byte[] iv = readFromFile(ivfile);
			byte[] output = null;

			double start = getCpuTime();
			// Calculate the CPU cycles.
			if (mode.equals("enc")) {
				output = cbc_encrypt(input, key, iv);
			} else if (mode.equals("dec")) {
				output = cbc_decrypt(input, key, iv);
			} else {
				System.out.println(mode);
				System.out.println("Wrong mode!");
				System.exit(1);
			}
			double end = getCpuTime();
			System.out.printf("Consumed CPU time=%f\n", end - start);
			writeToFile(outfile, output);
		}
	}
    
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
	
	static void test() {
		// This function is for test and illustration purpose.
		char[] chars1 = new char[8];
		char[] chars2 = new char[8];
		Arrays.fill(chars1, '\0');
		Arrays.fill(chars2, '\0');
		String key = new String(chars1);
		String message = new String(chars2);
		testDES(key, message);
		chars2[7] = '\1';
		message = new String(chars2);
		testDES(key, message);
		chars1[7] = '\2';
		chars2[7] = '\0';
		key = new String(chars1);
		message = new String(chars2);
		testDES(key, message);
		chars2[7] = '\1';
		message = new String(chars2);
		testDES(key, message);		
	}
}
