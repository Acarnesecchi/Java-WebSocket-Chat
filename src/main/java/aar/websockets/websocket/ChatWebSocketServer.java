package aar.websockets.websocket;

//import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import jakarta.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;


@ApplicationScoped
@ServerEndpoint("/actions")
public class ChatWebSocketServer {

    private static ChatSessionHandler sessionHandler = new ChatSessionHandler();
    private static final String URL_EMPLOYEES =
            "http://localhost:3000/RestWSExample/rest/employees/";
    private static final String URL_CHATS =
            "http://localhost:3000/RestWSExample/rest/chats/employees/";
    private static final String URL_LOGIN =
            "http://localhost:3000/RestWSExample/rest/employees/login/";

    public ChatWebSocketServer() {
        System.out.println("class loaded " + this.getClass());
    }

    @OnOpen
    public void onOpen(Session session) {

        sessionHandler.addSession(session);
        System.out.println("cliente suscrito, sesion activa");

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String[] cases = {"getEmployees", "getChats", "login", "message", "setChat"};
        int i;
        for(i = 0; i < cases.length; i++)
            if(message.contains(cases[i])) break;
        switch (i){
            case 0:
                String employees = getRequest(URL_EMPLOYEES);
                JsonObject addMessage = Json.createObjectBuilder()
                        .add("action", "addEmployees")
                        .add("employees", employees)
                        .build();
                System.out.println(employees);
                employees = addMessage.toString();
                sessionHandler.sendToSession(session, employees);
                break;
            case 1:
                String chatId = message.substring(message.length() - 1);
                String chats = getRequest(URL_CHATS + chatId);
                addMessage = Json.createObjectBuilder()
                        .add("action", "addChats")
                        .add("chats", chats)
                        .build();
                System.out.println(chats);
                chats = addMessage.toString();
                sessionHandler.sendToSession(session, chats);
                break;
            case 2:
                String[] login = message.split(" ");
                System.out.println(message);
                int user = Integer.parseInt(login[1]);
                String password = login[2];
                String loginResponse = postRequest(URL_LOGIN, user, password);
                addMessage = Json.createObjectBuilder()
                        .add("action", "login")
                        .add("login", loginResponse)
                        .build();
                System.out.println(loginResponse);
                loginResponse = addMessage.toString();
                sessionHandler.sendToSession(session, loginResponse);
                break;
            case 3:
                String[] messageArray = message.split(" ");
                System.out.println(message);
                int chatID = Integer.parseInt(messageArray[1]);
                int sender = Integer.parseInt(messageArray[2]);
                int receiver = Integer.parseInt(messageArray[3]);
                String messageText = messageArray[4];
                for (int j = 5; j < messageArray.length; j++) {
                    messageText += " " + messageArray[j];
                }
                addMessage = Json.createObjectBuilder()
                        .add("action", "addMessage")
                        .add("chatID", chatID)
                        .add("sender", sender)
                        .add("receiver", receiver)
                        .add("message", messageText)
                        .build();
                sessionHandler.sendToChatSession(chatID, addMessage);
                break;
            case 4:
                String[] setChat = message.split(" ");
                System.out.println(message);
                int chat = Integer.parseInt(setChat[1]);
                sessionHandler.addChatIdProperty(session, chat);
                System.out.println(session.getUserProperties().get("chatId"));
                break;
            default:
                System.out.println("Mensaje no reconocido");
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessionHandler.removeSession(session);
        System.out.println("cliente cierra conexión, sesion eliminada");

    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(ChatWebSocketServer.class.getName()).
                log(Level.SEVERE, null, error);
    }

    public JsonObject jsonBuilder(String name) {
        JsonObject json = Json.createObjectBuilder().add("name", name).build();
        return json;
    }
    private String postRequest(String URL, int user, String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "id=" +user + "&password=" + password);
        Request request = new Request.Builder()
                .url(URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    private String getFromIDRequest(int id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        //MediaType mediaType = MediaType.parse("text/plain");
        //RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://localhost:3000/RestWSExample/rest/employees/"
                        +Integer.toString(id))
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    private String getRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}