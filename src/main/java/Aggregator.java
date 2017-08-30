import repository.jdbc.JdbcRepositoryImpl;
import service.fileservice.FileServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main aggregator class.
 * <p>
 * ПОЯСНЕНИЕ.
 * <p>
 * В пункте 4 технического задания присутствует формат тегов, не корректный для .xml
 * В связи с этим, реализованы два решения технического задания:
 * <p>
 * 1. Реализация в формате, указанном в техническом задании, а именно:
 * - файл 1.xml преобразуется с включением тегов формата <entry field="value">.
 * Для реализации этого решения необходимо установить значение impl=tech.task в config.properties
 * (установлено по умолчанию)
 * <p>
 * 2. Реализация в формате, стандартном для XML-файлов.
 * Для реализации этого решения установите значение impl=correct в config.properties
 */
public class Aggregator {

    private static final String CONFIG_PATH = "./src/main/resources/config.properties";
    private static Logger logger = Logger.getLogger(Aggregator.class.getName());
    private static long timer = LocalTime.now().toSecondOfDay();

    private String url;
    private String user;
    private String password;
    private int value;

    public Aggregator() {
    }

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(CONFIG_PATH));

        Aggregator aggregator = new Aggregator();
        aggregator.init(properties);

        JdbcRepositoryImpl jdbcRepository = new JdbcRepositoryImpl();
//        jdbcRepository.init(aggregator.getUrl(), aggregator.getUser(), aggregator.getPassword());
        jdbcRepository.populate(aggregator.getValue(), aggregator.getUrl(), aggregator.getUser(),
                aggregator.getPassword());

        List<Integer> list = jdbcRepository.getAll(aggregator.getUrl(), aggregator.getUser(),
                aggregator.getPassword());

        FileServiceImpl fileService = new FileServiceImpl();
        fileService.create(list);
        fileService.transform(properties.getProperty("impl"));

        int result = "correct".equals(properties.getProperty("impl")) ? fileService.read() : fileService.readAlt();

        System.out.println("TOTAL SUM: " + result);
        logger.log(Level.INFO, ("TOTAL APPLICATION WORK TIME, SEC: " + (LocalTime.now().toSecondOfDay() - timer)));
    }

    private void init(Properties properties) {
        this.setUrl(properties.getProperty("url"));
        this.setUser(properties.getProperty("user"));
        this.setPassword(properties.getProperty("password"));
        this.setValue(Integer.parseInt(properties.getProperty("value")));

        logger.log(Level.INFO, this.toString());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Aggregator {" +
                "url: '" + url + '\'' +
                ", user: '" + user + '\'' +
                ", password: '" + password + '\'' +
                ", value: " + value +
                '}';
    }
}
