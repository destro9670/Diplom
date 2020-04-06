package ua.gabz.dm.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.gabz.dm.database.models.Users;

/**
 * Created by destr on 04.04.2020.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    Users save(Users user);

    void deleteByNick(String nick);

    Users findUsersByLogin(String login);

    Users findUsersByNick(String nick);

}
