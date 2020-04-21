package db.dao;

import db.models.Content;

public interface ContentDAO {

    Content findMessageById(long id);

    void save(Content message);

    void update(Content message);

    void delete(Content message);

}
