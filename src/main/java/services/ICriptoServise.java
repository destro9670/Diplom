package services;

import messages.IMessage;

public interface ICriptoServise {

    IMessage encrypt(String message);


    IMessage decrypt(IMessage message);
}
