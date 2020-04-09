package criptography;

import messages.Message;

public interface Criptography {

    String encript(String message);

    Message encript(Message message);

    String decript(String message);

    Message decript(Message message);

}
