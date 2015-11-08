package main.com.joebentley.mud;

public class Game {
    private Users onlineUsers;

    public Game() {
        onlineUsers = new Users();
    }

    /**
     * Get list of users that are currently logged in
     *
     * @return users logged in
     */
    public Users getOnlineUsers() {
        return onlineUsers;
    }

    /**
     * Add user to list of online users
     *
     * @param user user to add
     */
    public void addOnlineUser(User user) {
        onlineUsers.add(user);
    }
}
