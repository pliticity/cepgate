package pl.iticity.dbfds.util;

import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;

/**
 * Created by dacho on 27.03.2016.
 */
public class PrincipalUtils {

    public static Principal getCurrentPrincipal(){
        Subject currentUser = SecurityUtils.getSubject();
        Object principal = currentUser.getPrincipal();
        return (Principal) principal;
    }

    public static Domain getCurrentDomain(){
        return getCurrentPrincipal().getDomain();
    }

    public static boolean isAuthenticated(){
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }

    public static String generateSalt(){
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        return rng.nextBytes().toBase64();
    }

    public static String hashPassword(String password,String salt){
        String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
        return hashedPasswordBase64;
    }

}
