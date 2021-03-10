
public class SuiteTest {

    @BeforeSuite
    public static void start() {
        System.out.println("Test start");
    }

    @Test(priority = 4)
    public static void getArrFromLast4() {
        System.out.println("тест getArrFromLast4");
    }

    @Test(priority = 2)
    public static void checkArrIncl_1_4() {
        System.out.println("тест checkArrIncl_1_4");
    }

    @AfterSuite
    public static void finish() {
        System.out.println("Test end");
    }
}