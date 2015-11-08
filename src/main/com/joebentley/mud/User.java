package main.com.joebentley.mud;

public class User {
    public static final User testUser = new User("test");

    static {
        testUser.setID("1");
        testUser.group = Group.OWNER;
    }

    public Group group;
    private String ID;
    private String username;

    public User(String username) {
        this.username = username;

        group = Group.PUBLIC;
    }

    public User() {
        this(null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void getNewID(GameDatabaseConnection connection) {
        ID = connection.getNextUserID();
    }

    /**
     * Class to build a new User instance (handles getting new user ID, etc.
     */
    public static class Builder {
        private User user;

        public Builder(GameDatabaseConnection connection) {
            user = new User();
            user.getNewID(connection);
        }

        public Builder setUsername(String username) {
            user.setUsername(username);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
