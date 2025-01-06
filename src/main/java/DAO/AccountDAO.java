package DAO;
import Model.Account;
import Util.ConnectionUtil;
import io.javalin.http.HttpResponseException;

import java.sql.*;

public class AccountDAO {
    

    public Account insertAccount(Account account) {
            /*  
              #### DELETE THIS BEFORE SUBMITTING ########## 
              account_id integer primary key auto_increment,
              username varchar(255) unique,
              password varchar(255) 
            */
        
        Connection connection = ConnectionUtil.getConnection();

        //Check registration information before proceeding.
        if(getAccount(account.getUsername()) != null ||
            account.getPassword().length() < 4  ||
            account.username.length() < 1 ){
                
            throw new HttpResponseException(400);
        }
       
        try{
        String sql = "INSERT INTO ACCOUNT(USERNAME, PASSWORD) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    public Account getAccount(String accountName){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM ACCOUNT WHERE USERNAME = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, accountName);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
