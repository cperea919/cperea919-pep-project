package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }


    public Message addMessage(Message message) {
        if (message.getMessage_text().isBlank()) return null;
        if (message.getMessage_text().length() > 255) return null;
        if (accountDAO.getAccountById(message.getPosted_by()) == null) return null;
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        Message message = messageDAO.getMessageById(messageId);
        if (message == null) return null;
        messageDAO.deleteMessage(messageId);
        return message;
    }

    public Message updateMessageText(int messageId, String messageText) {
        if (messageText.isBlank()) return null;
        if (messageText.length() > 255) return null;
        Message message = messageDAO.getMessageById(messageId);
        if (message == null) return null;
        messageDAO.updateMessageText(messageId, messageText);
        Message updatedMessage = messageDAO.getMessageById(messageId);
        return updatedMessage;
    }
}
