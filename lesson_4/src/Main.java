public class Main {
    public static void main(String[] args) throws InterruptedException {

        class Sync {
            String curLetter = "";
        }
        Sync s = new Sync();
        s.curLetter = "A";

        Thread tA = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (s) {
                    while (!s.curLetter.equals("A")) {
                        try {
                            s.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("A");
                    s.curLetter = "B";
                    s.notifyAll();
                }
            }
        });

        Thread tB = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (s) {
                    while (!s.curLetter.equals("B")) {
                        try {
                            s.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("B");
                    s.curLetter = "C";
                    s.notifyAll();
                }
            }
        });

        Thread tC = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (s) {
                    while (!s.curLetter.equals("C")) {
                        try {
                            s.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("C");
                    s.curLetter = "A";
                    s.notifyAll();
                }
            }
        });

        tA.start();
        tB.start();
        tC.start();
    }
}
