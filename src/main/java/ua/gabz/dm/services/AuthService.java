package ua.gabz.dm.services;

import org.jboss.logging.Logger;
import ua.gabz.dm.clientThread.IClientThread;

public class AuthService implements IAuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class);

    private final IClientThread client;

    public AuthService(IClientThread client) {
        this.client = client;
    }

    @Override
    public boolean toAuthorize() throws IllegalArgumentException {
        ///TODO(2) finish auth method


        return false;
    }
}
