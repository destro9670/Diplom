package ua.gabz.dm.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.gabz.dm.database.models.Messages;

public interface MessagesRepository extends JpaRepository<Messages,Long> {
    
    
}
