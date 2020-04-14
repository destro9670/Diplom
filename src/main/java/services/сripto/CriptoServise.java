package services.сripto;

import messages.Message;

public interface CriptoServise {

    Message encrypt(Message message);

    String encrypt(String message);

    Message decrypt(Message message);

    String decrypt(String message);
}
