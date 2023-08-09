package com.digte.factories.whatsapp;

import com.digte.enums.WhatsappEnum;
import com.digte.factories.whatsapp.integrations.twillio.TwillioIntegration;

public class WhatsappFactory {

    public static IWhatsappIntegration getInstance(WhatsappEnum type) {
         IWhatsappIntegration _instance;

         switch(type) {
            case TWILLIO:
             _instance = new TwillioIntegration();
            break;
            default:
             return new TwillioIntegration();
        }

        return _instance;
    }
}
