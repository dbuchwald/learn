package net.dbuchwald.learn.junit;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
