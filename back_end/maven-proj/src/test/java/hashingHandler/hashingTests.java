package hashingHandler;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static hashingHandler.PasswordHashing.generateHash;
import static hashingHandler.PasswordHashing.validatePassword;
import static org.junit.Assert.*;

public class hashingTests {
    @Test
    public void correctPasswd() {
        String  originalPassword = "password";

        String passHash = generateHash(originalPassword);

        boolean matched = validatePassword("password", passHash);

        assertTrue(matched);

    }
    @Test
    public void wrongPasswd() {
        String  originalPassword = "password";

        String passHash = generateHash(originalPassword);

        boolean matched = validatePassword("password22", passHash);

        assertFalse(matched);

    }


}
