package service;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
 
public class HashPassword {
 
    // 文字列をSHA-256でハッシュ化するメソッド
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("ハッシュ化エラー: " + e.getMessage(), e);
        }
    }
 
    // 例として、Saltを使ったハッシュ化（より安全）
    public static String hashStringWithSalt(String input, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] saltBytes = salt.getBytes("UTF-8");
            digest.update(saltBytes);
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("ハッシュ化エラー: " + e.getMessage(), e);
        }
    }
 
    // Salt生成メソッド
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
 
    public static void main(String[] args) {
        // テストケース
        String originalString = "password123";
        String hashedString = hashString(originalString);
 
        System.out.println("Original: " + originalString);
        System.out.println("Hashed: " + hashedString);
 
        // Saltを使ったハッシュ化の例
        String salt = generateSalt();
        String saltedHash = hashStringWithSalt(originalString, salt);
 
        System.out.println("Salt: " + salt);
        System.out.println("Salted Hash: " + saltedHash);
    }
}
