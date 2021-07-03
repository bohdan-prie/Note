package ua.com.bohdanprie.notes.domain.manager;

import ua.com.bohdanprie.notes.domain.entity.User;

public interface UserManager {

	public User authorisation(String login, String password);

	public User createAccount(String login, String password);

	public void changeLogin(String newLogin, User user);

	public void changePassword(User user, String newPassword);

	public void deleteUser(User user);
}