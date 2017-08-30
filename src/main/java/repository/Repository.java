package repository;

import java.util.List;

/**
 * Created by promoscow on 18.08.17.
 */
public interface Repository {

    List<Integer> getAll(String url, String user, String password);

    void init(String url, String user, String password);
    void populate(int range, String url, String user, String password);
}
