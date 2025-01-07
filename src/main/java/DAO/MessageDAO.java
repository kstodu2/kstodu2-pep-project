package DAO;
import Model.Message;
import Util.ConnectionUtil;
import io.javalin.http.HttpResponseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message sendMessage(Message message) {

        Connection connection = ConnectionUtil.getConnection();

        //check validity of a message
        if(message.getMessage_text().length()< 1 || message.getMessage_text().length() > 255){
            throw new HttpResponseException(400);
          }
       
        try{
            
        String sql = "INSERT INTO MESSAGE(POSTED_BY,MESSAGE_TEXT, TIME_POSTED_EPOCH) VALUES (?,?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MESSAGE";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                        messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    public Message getMessageById( String id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM MESSAGE WHERE MESSAGE_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return message;
 }
        
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById( String id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            Message messageDeleted = getMessageById(id);
            String sql = "DELETE FROM MESSAGE WHERE MESSAGE_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);
            int rs = preparedStatement.executeUpdate();

            if(rs>0) return messageDeleted;        
        
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message patchMessageById( String id, Message newMessage){
        
        Connection connection = ConnectionUtil.getConnection();
        if(newMessage.getMessage_text().length() < 1 ||
            newMessage.getMessage_text().length() > 255 ||
            getMessageById(id) == null){
            throw new HttpResponseException(400);
          }
        try {
            String sql = "UPDATE MESSAGE SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            
            preparedStatement.setString(1, newMessage.getMessage_text());
            preparedStatement.setString(2, id);

            int rs = preparedStatement.executeUpdate();

            if(rs>0) return getMessageById(id);        
        
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesFromUserId(String id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MESSAGE WHERE POSTED_BY = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                messages.add(message);
 }
        
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
}
