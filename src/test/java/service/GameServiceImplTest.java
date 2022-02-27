package service;

import com.google.gson.Gson;
import config.HibernateConfig;
import model.SpaceLanderState;
import model.data.GameStoryDto;
import service.repository.GameStoryDao;
import service.repository.GameStoryDaoImpl;
import org.junit.*;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import model.request.GameDataRequestMessage;
import service.validator.GameDataRequestMessageValidator;
import view.ViewData;

@RunWith(Theories.class)
public class GameServiceImplTest extends Assert {
    private static GameStoryDao gameStoryDao;
    private static Gson gson;
    private GameServiceImpl service;
    private GameDataRequestMessage requestMessage;

    @BeforeClass
    public static void beforeClass() {
        // При выполнении тестов создаются записи в БД
        // Для корректной работы тестов Hibernate должен быть конфигурирован - DDL = create-drop
        // Значение DDL присваивается в "./src/resources/db.properties"
        HibernateConfig.initSessionFactory();
        gameStoryDao = new GameStoryDaoImpl();
        gson = new Gson();
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
        String response = service.createNewGame();

        ViewData recoveryData = gson.fromJson(response, ViewData.class);
        long id = recoveryData.getId();
        String jsonState = gson.toJson(recoveryData.getState());

        String expectedState = "{\"flyHeight\":100.0,\"fuelReserve\":140.0,\"currentFuelUsage\":0.0,\"fallSpeed\":0.0,\"isDead\":false}";
        assertEquals(jsonState, expectedState);

        hasBeenSavedEntryToDatabaseTest(jsonState, id);

        // проверка успешного повторного выполнения и инкрементирования id
        String anotherResponse = service.createNewGame();
        ViewData recoveryAnotherData = gson.fromJson(anotherResponse, ViewData.class);
        long newId = recoveryAnotherData.getId();
        long incDifference = newId - id;
        assertEquals(incDifference, 1);
    }

    @Test
    public void executeGameStepTest() {
        // для проведения теста необходимо создать запись в БД
        service.createNewGame();

        requestMessage.setId(1);
        requestMessage.setStep(1);
        requestMessage.setFuelUsage(5);
        String response = service.executeGameStep(requestMessage);

        ViewData recoveryData = gson.fromJson(response, ViewData.class);
        long id = recoveryData.getId();
        String jsonState = gson.toJson(recoveryData.getState());

        String expectedState = "{\"flyHeight\":97.6,\"fuelReserve\":135.0,\"currentFuelUsage\":5.0,\"fallSpeed\":4.8,\"isDead\":false}";
        assertEquals(expectedState, jsonState);

        hasBeenSavedEntryToDatabaseTest(jsonState, id);
    }

    @Test
    public void nonExistentDataRequestTest() {
        // для проведения теста необходимо создать запись в БД
        service.createNewGame();

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

    private void hasBeenSavedEntryToDatabaseTest(String jsonState, long id) {
        GameStoryDto recoveryDtoFromDb = GameStoryDto.getInstance(gameStoryDao.getById(id).get());
        int lastIndex = recoveryDtoFromDb.getGameStoryList().size() - 1;
        SpaceLanderState lastState = recoveryDtoFromDb.getGameStoryList().get(lastIndex);
        assertEquals(jsonState, gson.toJson(lastState));
    }
}