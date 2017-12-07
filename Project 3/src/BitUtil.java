//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * @author Gary S. Weaver
 */
public class BitUtil {

    private static Log log = LogFactory.getLog(BitUtil.class);

    // solution 2
    public static byte[] addParity(byte[] in) {
        if (log.isDebugEnabled()) {
            log.debug("addParity:  in=" + asBinary(in));
        }

        byte[] result = new byte[estimateLengthOfParitizedBytes(in)];
        int resultByteIndex = (result.length * 8) - 1;
        int bitsOnInCurrentByte = 0;
        int numInBits = in.length * 8;
        for (int i = 0; i < numInBits; ++i) {
            int inByteIndex = i / 8;
            // do unsigned right shift
            int unsignedRightShiftAmount = 7 - (i % 8);
            // if after shifting the low bit is on then bitValue is true, otherwise is false
            boolean nonParityResultBitValue = ((in[inByteIndex] >>> unsignedRightShiftAmount) & 1) == 1;
            setBit(result, resultByteIndex, nonParityResultBitValue);

            if (nonParityResultBitValue) {
                bitsOnInCurrentByte++;
            }

            resultByteIndex--;

            if ((i + 1) % 7 == 0) {
                // if bit count even, set the parity bit to true, otherwise set to false
                boolean parityBitValue = bitsOnInCurrentByte % 2 == 0;
                setBit(result, resultByteIndex, parityBitValue);
                resultByteIndex--;
                bitsOnInCurrentByte=0;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("addParity: out=" + asBinary(result) + " (final)");
        }

        return result;
    }

    /* solution 1
    public static byte[] addParity(byte[] in) {
        if (log.isDebugEnabled()) {
            log.debug("addParity:  in=" + asBinary(in));
            //log.debug("addParity: pre=" + asHoleyBinary(in));
        }

        // TODO: clean up

        byte[] result = new byte[estimateLengthOfParitizedBytes(in)];
        int resultIndex = (result.length * 8) - 1;
        int count = 0;
        int bitsOnInCurrentByte = 0;
        boolean bitValue;
        // note that:
        // i = count/8
        // j = 7-(count%8)
        // so you could potentially remove a loop
        for (int i = 0; i < in.length; ++i) {
            for (int j = 7; j >= 0; --j) {

                if (log.isDebugEnabled()) {
                    log.debug("addParity: determining bitValue*i=" + i + "*j=" + j + " count/8=" + (count/8) + " 7-(count%8)=" + (7-(count%8)));
                }
                bitValue = ((in[i] >>> j) & 1) == 1;
                //if (log.isDebugEnabled()) {
                //    log.debug("addParity:   setting result bit i=" + i + " j=" + j + " count=" + count + "*resultIndex=" + resultIndex);
                //}
                setBit(result, resultIndex, bitValue);
                if (bitValue) {
                    bitsOnInCurrentByte++;
                }
                count++;
                resultIndex--;
                if (count % 7 == 0) {
                    // if bit count even, set the parity bit to true, otherwise set to false
                    bitValue = bitsOnInCurrentByte % 2 == 0;
                    //if (log.isDebugEnabled()) {
                    //    log.debug("addParity:   setting parity bit i=" + i + " j=" + j + "*count=" + count + "*resultIndex=" + resultIndex);
                    //}
                    setBit(result, resultIndex, bitValue);
                    resultIndex--;
                    bitsOnInCurrentByte=0;
                }

                //if (log.isDebugEnabled()) {
                //    log.debug("addParity: out=" + asBinary(result));
                //}
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("addParity: out=" + asBinary(result) + " (final)");
        }

        return result;
    }
     */

    // this keeps leading zeroes and inserts space for parity bit. useful for logging to help debug addParity changes.
    // LEAVING THIS HERE BECAUSE ADDPARITY NEEDS WORK TO MAKE IT MORE EFFICIENT
    /*
    public static String asHoleyBinary(byte[] data) {
        StringBuilder out = new StringBuilder();

        int count = 0;

        for (int i = 0; i < data.length; ++i) {
            for (int j = 7; j >= 0; --j) {
                out.append((data[i] >>> j) & 1);                
                count++;
                if (count % 7 == 0) {
                    out.append(" |");
                }
            }
        }

        return out.toString();
    }
    */

    // this keeps leading zeroes
    // copied from toString(byte... ) at http://www.koders.com/java/fidA7BAF6710B21E5BDF6823FCCE3D2B4CE5D92CFAE.aspx?s=idef%3Atree
    public static String asBinary(byte[] data) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < data.length; ++i) {
            if (i != 0) {
                out.append('|');
            }

            for (int j = 7; j >= 0; --j) {
                out.append((data[i] >>> j) & 1);
            }
        }

        return out.toString();
    }

    public static byte[] removeParity(byte[] in) {

        if (log.isDebugEnabled()) {
            log.debug("removeParity:  in=" + asBinary(in));
        }

        byte[] result = new byte[estimateLengthOfUnparitizedBytes(in)];

        int resultBitIndex = 0;
        int numBitsIn = in.length * 8;

        for (int i = 0; i < numBitsIn; i++) {
            boolean bit = getBit(in, i);

            // if not the low-order parity bit
            if (i % 8 != 0) {
                // If set, set that bit in the result
                setBit(result, resultBitIndex, bit);
                resultBitIndex++;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("removeParity: out=" + asBinary(result));
        }

        return result;
    }

    public static int estimateLengthOfParitizedBytes(byte[] in) {
        int result = 0;
        if (in != null) {
            result = in.length + (in.length / 7);
            // need an extra byte for remainder
            if (in.length % 7 > 0) {
                result++;
            }

            if (log.isDebugEnabled()) {
                log.debug("" + in.length + " byte array will be " + result + " byte array after parity added.");
            }
        }

        return result;
    }

    public static int estimateLengthOfUnparitizedBytes(byte[] in) {
        int result = -1;
        if (in != null) {
            result = in.length - (in.length / 8);
            // no need to add if remainder like addition when doing subraction

            if (log.isDebugEnabled()) {
                log.debug("" + in.length + " byte array will be " + result + " byte array after parity removed.");
            }
        }

        return result;
    }

    // heavily based on org.jscience.util.BitUtils v1.1 by Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
    // http://www.koders.com/java/fidA7BAF6710B21E5BDF6823FCCE3D2B4CE5D92CFAE.aspx?s=idef%3Atree
    public static byte[] setBit(byte[] data, int index, boolean value) {
        int pos = data.length - (index / 8) - 1;
        int bitPos = index % 8;

        int d = data[pos] & 0xFF;
        if (value) {
            d = d | (1 << bitPos);
        } else {
            d = d & ~(1 << bitPos);
        }
        data[pos] = (byte) d;

        return data;
    }

    // heavily based on org.jscience.util.BitUtils v1.1 by Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
    // http://www.koders.com/java/fidA7BAF6710B21E5BDF6823FCCE3D2B4CE5D92CFAE.aspx?s=idef%3Atree
    public static boolean getBit(byte[] data, int index) {
        int pos = data.length - (index / 8) - 1;
        int bitPos = index % 8;
        int d = data[pos] & 0xFF;
        return (d & (1 << bitPos)) != 0;
    }
}
BitUtilTest.java
import junit.framework.TestCase;

/**
 * @author Gary S. Weaver
 */
public class BitUtilTest extends TestCase {

    private static final String ADD_PARITY_EXPECTED =
            "00000010|10000000|11000001|01100001|00110001|00011001|00001101|00000111";

    private static final byte[] ADD_PARITY_IN = {
            0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03
    };

    private static final String REMOVE_PARITY_EXPECTED =
            "00000010|00000100|00001000|00010000|00100000|01000000|10000001";

    private static final byte[] REMOVE_PARITY_IN = {
            0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03
    };

    private static final String AS_BINARY_EXPECTED =
            "00000000|00000001|01111001";

    private static final byte[] AS_BINARY_IN = {
            0x00, 0x01, 0x79
    };

    public void testAddParity() {
        assertEquals("addParity failed", ADD_PARITY_EXPECTED, BitUtil.asBinary(BitUtil.addParity(ADD_PARITY_IN)));
    }

    public void testAsBinary() {
        assertEquals("asBinary failed", AS_BINARY_EXPECTED, BitUtil.asBinary(AS_BINARY_IN));
    }

    public void testRemoveParity() {
        assertEquals("removeParity failed", REMOVE_PARITY_EXPECTED, BitUtil.asBinary(BitUtil.removeParity(REMOVE_PARITY_IN)));
    }

    public void testEstimateLengthOfParitizedBytes() {
        assertEquals("a 7 byte array should require 8 bytes after parity added (7 + 7/7 ceil)", 8, BitUtil.estimateLengthOfParitizedBytes(new byte[7]));
        assertEquals("an 8 byte array should require 9 bytes after parity added (8 + 8/7 ceil)", 10, BitUtil.estimateLengthOfParitizedBytes(new byte[8]));
        assertEquals("a 1 byte array should require 2 bytes after parity added (1 + 1/7 ceil)", 2, BitUtil.estimateLengthOfParitizedBytes(new byte[1]));
    }

    public void testEstimateLengthOfUnparitizedBytes() {
        assertEquals("an 8 byte array should require 7 bytes after parity removed (8 - 8/8 ceil)", 7, BitUtil.estimateLengthOfUnparitizedBytes(new byte[8]));
        assertEquals("a 9 byte array should require 8 bytes after parity removed (10 - 10/8 ceil)", 9, BitUtil.estimateLengthOfUnparitizedBytes(new byte[10]));
        assertEquals("a 1 byte array should require 1 bytes after parity removed (1 - 1/8 ceil)", 2, BitUtil.estimateLengthOfUnparitizedBytes(new byte[2]));
    }
}