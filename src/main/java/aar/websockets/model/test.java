package aar.websockets.model;

import java.util.ArrayList;

public class test {
    ArrayList<Chat> chats = new ArrayList<>();

    public void getChat(int id) {
        for (Chat chat : chats) {
            if (chat.getId() == id) {
                System.out.println(chat.getName());
            }
        }
    }
    public void addChat(Chat chat) {
        chats.add(chat);
    }
    public void deleteChat(int id) {
        for (Chat chat : chats) {
            if (chat.getId() == id) {
                chats.remove(chat);
            }
        }
    }
    public Chat getChatObject(int id) {
        for (Chat chat : chats) {
            if (chat.getId() == id) {
                return chat;
            }
        }
        return null;
    }
    ArrayList<String> coches = new ArrayList<String>();

    public void addCoches(String coche) {
        coches.add(coche);
    }
    public void removeCoches(String coche) {
        coches.remove(coche);
    }
    public String getCoches(int index) {
        return coches.get(index);
    }
    public boolean containsCoches(String coche) {
        return coches.contains(coche);
    }
}
