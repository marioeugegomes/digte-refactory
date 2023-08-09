package com.digte.services;

import com.digte.enums.WhatsappEnum;
import com.digte.factories.whatsapp.WhatsappFactory;

public class WhatsappService {

    public void sendMessage(WhatsappEnum type, String to, String from, String message) {
        try {
            WhatsappFactory.getInstance(type).sendMessage(to, from, message);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
