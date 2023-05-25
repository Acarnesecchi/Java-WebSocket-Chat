package aar.websockets.model;

public class Chat {

    private int id;
    private String name;
    private String user1;
    private String user2;
    private String whoAmI;

    public Chat() {
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }
    
    public String getWhoAmI() {
        return whoAmI;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
    
    public void setWhoAmI(String whoAmI) {
        this.whoAmI = whoAmI;
    }
}