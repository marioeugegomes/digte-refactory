package com.digte.factories.whatsapp.integrations.twillio;

import com.digte.factories.whatsapp.IWhatsappIntegration;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwillioIntegration implements IWhatsappIntegration  {

    public void sendMessage(String to, String from, String body){
        Twilio.init(TwillioConstant.ACCOUNT_SID, TwillioConstant.AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber( "whatsapp:+" + to),
                new com.twilio.type.PhoneNumber(
                    "whatsapp:+" + from),
                body).create();

        System.out.println(message.getSid());
    }
}
