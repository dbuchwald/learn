package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dawidbuchwald on 17.02.2017.
 */
public class UserServiceImplTest {

    private final UserDAO userDAO = mock(UserDAO.class);
    private final SecurityService securityService = mock(SecurityService.class);
    private final UserServiceImpl userService = new UserServiceImpl(userDAO, securityService);
    private final User user = mock(User.class);

    private static final String USER_PASSWORD = "Password";
    private static final String USER_PASSWORD_MD5 = "dc647eb65e6711e155375218212b3964";

    @Test
    public void assignPasswordShouldUpdateUserPassword() throws Exception {

        when(user.getPassword()).thenReturn(USER_PASSWORD);
        when(securityService.md5(USER_PASSWORD)).thenReturn(USER_PASSWORD_MD5);

        assertEquals(USER_PASSWORD, user.getPassword());
        userService.assignPassword(user);

        verify(securityService).md5(USER_PASSWORD);
        verify(user).setPassword(USER_PASSWORD_MD5);
        verify(userDAO).updateUser(user);
    }
}
