package ru.xpendence.test.fileservice;

import java.util.List;

/**
 * Created by promoscow on 19.08.17.
 */
public interface FileService {

    void create(List<Integer> list);
    void transform(String impl);
    int read();
    int readAlt();
}
