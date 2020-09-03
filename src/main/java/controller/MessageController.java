package controller;

import config.PageMap;
import config.SessionKey;
import framework.WebController;
import framework.annotation.MVCRouteMethod;
import model.Ad;
import model.Message;
import model.system.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class MessageController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute message/index");
    }

    @MVCRouteMethod(path = "/message/details", method = "GET")
    public void details(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute message/details");
        showRequestParam(req);

        int messageId = getMessageId(req);
        setSession(req, SessionKey.MESSAGE_ID, messageId);

        String messageCollectionSQL = "SELECT id, ad_id, employee_id, value" +
                " FROM messages" +
                " WHERE is_active=1" +
                " AND id=" + messageId;

        ArrayList<Message> messageCollection = Message.fetchMessageSQL(messageCollectionSQL);
        if (!messageCollection.isEmpty()) {
            setSession(req, SessionKey.MESSAGE, messageCollection);
        }



        display(req, resp, PageMap.DETAILS_MESSAGE_PAGE);
    }

    private int getMessageId(HttpServletRequest req) {
        int messageId = 0;
        if (getSession(req, SessionKey.MESSAGE_ID) != null) {
            messageId = (int) getSession(req, SessionKey.MESSAGE_ID);
        }
        messageId = (hasQuery(req, SessionKey.MESSAGE_ID)) ? Integer.parseInt(getQueryValue(req, SessionKey.MESSAGE_ID)) : messageId;
        return messageId;
    }


}
