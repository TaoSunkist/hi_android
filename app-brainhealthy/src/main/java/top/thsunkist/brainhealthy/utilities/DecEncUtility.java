package top.thsunkist.brainhealthy.utilities;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecEncUtility {

    public static String encodeToString(byte[] input) { return java.util.Base64.getEncoder().encodeToString(input);
    }

    public static byte[] decode(String str) {
        return java.util.Base64.getDecoder().decode(str);
    }

    /**
     * 解密：对加密后的十六进制字符串(hex)进行解密，并返回字符串
     *
     * @param encryptedStr 需要解密的，加密后的十六进制字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String encryptedStr, String key, String iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);


            byte[] decode = decode(encryptedStr);

            //  byte[] bytes = hexStr2Bytes(encryptedStr);
            byte[] original = cipher.doFinal(decode);

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}