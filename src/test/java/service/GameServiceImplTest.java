package service;

import config.HibernateConfig;
import model.data.GameStoryDto;
import model.data.dao.GameStoryDao;
import model.data.dao.GameStoryDaoImpl;
import org.junit.*;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import service.model.GameDataRequestMessage;
import service.validator.GameDataRequestMessageValidator;

@RunWith(Theories.class)
public class GameServiceImplTest extends Assert {
    private static GameStoryDao gameStoryDao;
    private GameServiceImpl service;
    private GameDataRequestMessage requestMessage;

    @BeforeClass
    public static void beforeClass() {
        // При выполнении тестов создаются записи в БД
        // Для корректной работы тестов Hibernate должен быть конфигурирован - DDL = create-drop
        // Значение DDL присваивается в "./src/resources/db.properties"
        HibernateConfig.initSessionFactory();
        gameStoryDao = new GameStoryDaoImpl();
    }

    @Before
    public void setUp() {
        service = new GameServiceImpl(new GameStoryDaoImpl(), new GameDataRequestMessageValidator());
        requestMessage = new GameDataRequestMessage();
    }

    @DataPoints
    public static Object[][] badRequestTestData = new Object[][]{
            {0L, 0, 0.0},
            {1L, 0, 0.0}, {0L, 1, 0.0}, {0L, 0, 1.0},
            {0L, 1, 1.0}, {1L, 0, 1.0}
    };

    @Theory
    public void invalidRequestTest(final Object... testData) {
        requestMessage.setId((long) testData[0]);
        requestMessage.setStep((int) testData[1]);
        requestMessage.setFuelUsage((double) testData[2]);

        String result = service.executeGameStep(requestMessage);
        assertNull(result);
    }

    @Test
    public void createNewGameTest() {
        String response = service.executeGameStep(null);
        GameStoryDto recoveryDto = GameStoryDto.getInstance(response);

        long newId = recoveryDto.getId();
        String jsonList = recoveryDto.getGameStoryListToJson();
        String expectedList = "[{\"flyHeight\":100.0,\"fuelReserve\":140.0,\"currentFuelUsage\":0.0,\"fallSpeed\":0.0,\"isDead\":false}]";
        assertEquals(jsonList, expectedList);

        hasEntryInDatabaseTest(jsonList, newId);

        // проверка успешного повторного выполнения с инкрементированием id
        String anotherResult = service.executeGameStep(null);
        GameStoryDto anotherDto = GameStoryDto.getInstance(anotherResult);
        long incDifference = anotherDto.getId() - recoveryDto.getId();
        assertEquals(incDifference, 1);
    }

    @Test
    public void executeGameStepTest() {
        // для проведения теста необходимо создать запись в БД
        service.executeGameStep(null);

        requestMessage.setId(1);
        requestMessage.setStep(1);
        requestMessage.setFuelUsage(5);

        String response = service.executeGameStep(requestMessage);
        GameStoryDto recoveryDto = GameStoryDto.getInstance(response);

        long newId = recoveryDto.getId();
        String jsonList = recoveryDto.getGameStoryListToJson();
        String expectedList = "{\"id\":1,\"gameStoryList\":[{\"flyHeight\":100.0,\"fuelReserve\":140.0,\"currentFuelUsage\":0.0,\"fallSpeed\":0.0,\"isDead\":false},{\"flyHeight\":97.6,\"fuelReserve\":135.0,\"currentFuelUsage\":5.0,\"fallSpeed\":4.8,\"isDead\":false}]}";
        assertEquals(expectedList, response);

        hasEntryInDatabaseTest(jsonList, newId);
    }

    @Test
    public void nonExistentDataRequestTest() {
        // для проведения теста необходимо создать запись в БД
        service.executeGameStep(null);

        requestMessage.setId(100);
        requestMessage.setStep(1);
        requestMessage.setFuelUsage(5);
        String result = service.executeGameStep(requestMessage);
        assertNull(result);

        requestMessage.setId(1);
        requestMessage.setStep(100);
        result = service.executeGameStep(requestMessage);
        assertNull(result);
    }

    @AfterClass
    public static void afterClass() {
        HibernateConfig.close();
    }

    private void hasEntryInDatabaseTest(String jsonList, long newId) {
        assertEquals(jsonList, gameStoryDao.getById(newId)
                .get()
                .getGameStoryData());
    }
}