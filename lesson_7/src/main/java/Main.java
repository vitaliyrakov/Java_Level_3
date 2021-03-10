import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {
    private static final List<Method> beforeMethods = new ArrayList<>();
    private static final List<Method> afterMethods = new ArrayList<>();
    private static final Map<Method, Integer> testMethods = new HashMap<>();
    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 10;

    public static void main(String[] args) {
        start(SuiteTest.class);
    }

    public static void start(Class classTest) {
        for (Method method : classTest.getMethods()) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(BeforeSuite.class)) beforeMethods.add(method);
            if (method.isAnnotationPresent(AfterSuite.class)) afterMethods.add(method);
            if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation(Test.class).priority();
                if (priority < MIN_PRIORITY || priority > MAX_PRIORITY)
                    throw new IllegalArgumentException("Not valid priority:" + priority + " (" + MIN_PRIORITY + "<valid priority<" + MAX_PRIORITY + ")");
                testMethods.put(method, priority);
            }
        }

        if (beforeMethods.size() > 1 || afterMethods.size() > 1)
            throw new RuntimeException("Not unique annotation " + ((beforeMethods.size() > 1) ? "@BeforeSuite" : "@AfterSuite"));

        Map<Method, Integer> result = new LinkedHashMap<>();
        testMethods.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue()))
                .forEach(e -> result.put(e.getKey(), e.getValue()));

        try {
            if (!beforeMethods.isEmpty()) beforeMethods.get(0).invoke(null);
            for (Map.Entry<Method, Integer> entry : result.entrySet())
                entry.getKey().invoke(null);
            if (!afterMethods.isEmpty()) afterMethods.get(0).invoke(null);

        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
