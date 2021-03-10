import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private final Semaphore SEMAPHORE;
    private static final int LIMIT_CARS = Main.CARS_COUNT < 2 ? 1 : Main.CARS_COUNT / 2;

    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        SEMAPHORE = new Semaphore(LIMIT_CARS);
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                SEMAPHORE.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                SEMAPHORE.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}