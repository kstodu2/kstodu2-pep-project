package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMassageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessagesByIdHandler);
        app.get("/accounts/{message_id}/messages", this::getAllMessagesFromUserIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            context.json(mapper.writeValueAsString(addedAccount));
        }else{
            context.status(400);
        }
    }
    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.getAccount(account);
        if(addedAccount!=null){
            context.json(mapper.writeValueAsString(addedAccount));
        }else{
            context.status(401);
        }
    }
    private void postMassageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(context.body(), Message.class);
    

        Message addedMessage = messageService.addMessage(message);
            if(addedMessage!=null){
                context.json(mapper.writeValueAsString(addedMessage));
            }
        
        else{
            context.status(400);
        }
    }
    public void getAllMessagesHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
    public void getMessageByIdHandler(Context context){
        String id = context.pathParam("message_id");
        Message message = messageService.getMessageById(id);
        if(message!=null){
            context.json(message);
        } else{
         context.status(200);
        }
    }
    public void deleteMessageByIdHandler(Context context){
        String id = context.pathParam("message_id");
        Message message = messageService.deleteMessageById(id);
        if(message!=null){
            context.json(message);
        } else{
         context.status(200);
        }
    }
    public void patchMessagesByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String id = context.pathParam("message_id");
        Message message = mapper.readValue(context.body(), Message.class);
        Message updatedMessage = messageService.patchMessageById(id, message);
        if(updatedMessage!=null){
            context.json(updatedMessage);
        } else{
         context.status(400);
        }
    }
    public void getAllMessagesFromUserIdHandler(Context context){
        String id = context.pathParam("message_id");
        List<Message> messages = messageService.getAllMessagesFromUserId(id);
        context.json(messages);
    }


}