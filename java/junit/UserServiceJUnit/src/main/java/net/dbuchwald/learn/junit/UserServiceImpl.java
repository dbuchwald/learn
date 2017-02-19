package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 17.02.2017.
 */
public class UserServiceImpl {

    private final UserDAO userDAO;
    private final SecurityService securityService;

    @SuppressWarnings("RedundantThrows")
    public void assignPassword(User user) throws Exception {
        String passwordMd5 = securityService.md5(user.getPassword());
        user.setPassword(passwordMd5);
        userDAO.updateUser(user);
    }

    public UserServiceImpl(UserDAO dao, SecurityService security) {
        this.userDAO = dao;
        this.securityService = security;
    }
}
