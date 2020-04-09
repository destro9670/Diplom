package criptography;

import messages.Message;

public class Strumock implements Criptography  {

    ///TODO(1) write Strumock

    @Override
    public String encript(String message) {
        return message;
    }

    @Override
    public Message encript(Message message) {
        return message;
    }

    @Override
    public String decript(String message) {
        return message;
    }


    @Override
    public Message decript(Message message) {
        return message;
    }
}
