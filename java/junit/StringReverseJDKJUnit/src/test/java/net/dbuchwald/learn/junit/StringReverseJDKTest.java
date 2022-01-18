package net.dbuchwald.learn.junit;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by dawidbuchwald on 05.03.2017.
 */
public class StringReverseJDKTest {

    @Test
    public void shouldReturnIAEForNull() {
        assertNull(StringUtils.reverse(null));
    }

    @Test
    public void shouldReturnEmptyForEmptyString() {
        assertTrue(StringUtils.reverse("").isEmpty());
    }

}
