package services;

import criptography.CriptographyAlghorytm;
import messages.IMessage;

public class CriptoServise implements ICriptoServise {

    private final CriptographyAlghorytm alghorytm;

    CriptoServise(CriptographyAlghorytm alghorytm) {
        this.alghorytm = alghorytm;
    }

    @Override
    public IMessage encrypt(String message) {
        if(alghorytm == CriptographyAlghorytm.STRUMOCK)
            return encrypt(message);

        return null;
    }

    @Override
    public IMessage decrypt(IMessage message) {
        if(alghorytm == CriptographyAlghorytm.STRUMOCK)
            return decrypt(message);

        return null;
    }
}
