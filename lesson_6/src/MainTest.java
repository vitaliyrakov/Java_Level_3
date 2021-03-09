import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value = Parameterized.class)
public class MainTest extends TestCase {
    private int[] inArray;
    private int[] outArray;
    private int[] inArray2;
    private boolean outBool;

    public MainTest(int[] inArray, int[] outArray, int[] inArray2, boolean outBool) {
        this.inArray = inArray;
        this.outArray = outArray;
        this.inArray2 = inArray2;
        this.outBool = outBool;
    }

    @Parameterized.Parameters
    public static Collection inArrays() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 2, 3, 4, 5, 6}, new int[]{5, 6}, new int[]{1, 1, 4}, true},
                {new int[]{1, 2, 3, 4, 5, 4}, new int[]{}, new int[]{4, 4, 1}, true},
                {new int[]{4, 2, 1, 3, 5, 6}, new int[]{2, 1, 3, 5, 6}, new int[]{1, 1, 1}, false},
                {new int[]{4, 4, 4, 4, 4, 4}, new int[]{}, new int[]{4, 4, 4}, false}
        });
    }

    @BeforeClass
    public static void start() {
        System.out.println("Test start");
    }

    @Test
    public void getArrFromLast4() {
        Assert.assertArrayEquals(outArray, Main.getArrFromLast4(inArray));
    }

    @Test(expected = RuntimeException.class)
    public void getArrFromLast4_Exception() {
        Main.getArrFromLast4(new int[]{1, 2, 3, 5, 6, 7});
        Main.getArrFromLast4(new int[]{});
    }

    @Test
    public void checkArrIncl_1_4() {
        Assert.assertEquals(outBool, Main.checkArrIncl_1_4(inArray2));
    }

    @AfterClass
    public static void finish() {
        System.out.println("Test end");
    }
}
