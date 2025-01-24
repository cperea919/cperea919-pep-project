package Controller;

import java.util.List;
import java.util.Map;

import org.h2.util.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    public AccountService accountService;
    public MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postCreateAccount);
        app.post("/login", this::postVerifyLogin);
        app.post("/messages", this::postCreateMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getSpecificMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::patchUpdateMessageText);
        app.get("/accounts/{account_id}/messages", this::getAccountMessages);
        return app;
    }
    
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postCreateAccount(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);

        if(addedAccount==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void postVerifyLogin(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyLogin(account);

        if(verifiedAccount==null){
            context.status(401);
        }else{
            context.json(mapper.writeValueAsString(verifiedAccount));
        }
    }

    private void postCreateMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);

        if(addedMessage==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(addedMessage));
        }

    }

    private void getAllMessages(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        context.json(mapper.writeValueAsString(messageService.getAllMessages()));
    }

    private void getSpecificMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Integer message_id = context.pathParamAsClass("message_id", Integer.class).get();

        Message message = messageService.getMessage(message_id);

        if (message != null) context.json(mapper.writeValueAsString(message));
    }

    private void deleteMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Integer message_id = context.pathParamAsClass("message_id", Integer.class).get();

        Message message = messageService.deleteMessage(message_id);

        if (message != null) context.json(mapper.writeValueAsString(message));
    }

    private void patchUpdateMessageText(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Integer message_id = context.pathParamAsClass("message_id", Integer.class).get();
        Map<String, String> bodyJson = mapper.readValue(context.body(), new TypeReference<Map<String,String>>(){});
        Message message = messageService.updateMessageText(message_id, bodyJson.get("message_text"));

        if (message != null) context.json(mapper.writeValueAsString(message));
        else context.status(400);
    }

    private void getAccountMessages(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Integer accountId = context.pathParamAsClass("account_id", Integer.class).get();

        List<Message> messages = accountService.getAccountMessages(accountId);

        if (messages != null)
            context.json(mapper.writeValueAsString(messages));
    }

}