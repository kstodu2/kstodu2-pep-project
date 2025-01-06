package Service;
import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    
    private MessageDAO messageDAO;


    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO; 

    }
    public Message addMessage(Message message){
        return messageDAO.sendMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    public Message getMessageById(String id){
        return messageDAO.getMessageById(id);
    }
    public Message deleteMessageById(String id){
        return messageDAO.deleteMessageById(id);
    }
    public Message patchMessageById(String id, Message newMessage){
        return messageDAO.patchMessageById(id, newMessage);
    }
    public List<Message> getAllMessagesFromUserId(String id){
        return messageDAO.getAllMessagesFromUserId(id);
    }
 

}
