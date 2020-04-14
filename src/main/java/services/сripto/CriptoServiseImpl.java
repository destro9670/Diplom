package services.сripto;

import criptography.Criptography;
import criptography.CriptographyAlghorytm;
import criptography.Strumock;
import messages.Message;

public class CriptoServiseImpl implements CriptoServise {

    private Criptography cripto ;


    public CriptoServiseImpl(CriptographyAlghorytm alghorytm) {
        if(alghorytm == CriptographyAlghorytm.STRUMOCK)
            cripto = new Strumock();
    }

    @Override
    public Message encrypt(Message message) {
        return cripto.encript(message);
    }

    @Override
    public String encrypt(String message) {

        return cripto.encript(message);
    }

    @Override
    public Message decrypt(Message message) {
        return cripto.decript(message);

    }

    @Override
    public String decrypt(String message) {
        return cripto.decript(message);
    }
}
