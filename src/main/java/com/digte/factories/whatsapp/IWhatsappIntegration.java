package com.digte.factories.whatsapp;

public interface IWhatsappIntegration {
    void sendMessage(String to, String from, String body);
}
