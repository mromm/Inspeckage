package mobi.acpm.inspeckage.unpack;


import java.io.FileOutputStream;

public class FIO {
    public static void writeByte(byte[] arg8, String arg9) {
        try {
            FileOutputStream v2 = new FileOutputStream(arg9);
            v2.write(arg8);
            v2.close();
        }
        catch(Exception v4) {
        }
    }
}