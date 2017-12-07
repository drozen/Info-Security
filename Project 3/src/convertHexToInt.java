
import java.math.BigInteger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel Rozen
 */


public class convertHexToInt {
    
  
           // More efficient ZERO PADDING METHOD
               private static String padLeadingZeros1(String nextBstr, int expSize) {
        //nextBstr = PADDED_MAP.get(nextBstr.length()) + nextBstr;
        StringBuilder sb = new StringBuilder(nextBstr);
        while (sb.length() < expSize) {
            sb.insert(0, "0");
        }       nextBstr = sb.toString();
        return nextBstr;
    }
  static String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
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
        
        public static void main (String[] args) {
            BigInteger lowerInt = convertHexToInt("8080803E80808080");
            BigInteger upperInt = convertHexToInt("8080803E7F7F7F7F");
            //BigInteger upperIntAdd1 = lowerInt.add(BigInteger 67108864);
            
            BigInteger diff = upperInt.subtract(lowerInt);
            System.out.println(lowerInt);
            System.out.println(upperInt);
            
            System.out.println("# of tried keys: " + diff);

                        
                        BigInteger lowerInt1 = convertHexToInt("8080803E80808080");
            BigInteger upperInt1 = convertHexToInt("8080803e43da7f01");
            
            BigInteger diff1 = upperInt1.subtract(lowerInt1);
            System.out.println(lowerInt1);
            System.out.println(upperInt1);
                        System.out.println(diff1);

            
            
        }
        
        
}
