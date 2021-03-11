import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] arr = new String[]{"Зима", "Весна", "Лето"};
        System.out.println(Arrays.toString(arr));
        chgElemArray(arr, 0, 2);
        System.out.println(Arrays.toString(arr));

        System.out.println(Arrays.toString(arr));
        List<String> list = transArrToList(arr);
        System.out.println(list.toString());

        Box<Apple> appleBox = new Box<>(new Apple(), new Apple(1.1f), new Apple(2.5f));
        Box<Orange> orangeBox = new Box<>(new Orange(), new Orange(2.1f), new Orange(2.5f));
        System.out.println(appleBox.getQty() + "шт. " + appleBox.getWeight());
        System.out.println(orangeBox.getQty() + "тш. " + orangeBox.getWeight());

        appleBox.add(new Apple(2.0f));
        appleBox.add(new Apple(), new Apple());
        System.out.println(appleBox.getQty() + "шт.");
        Box<Apple> newAppleBox = new Box();
        newAppleBox.fill(appleBox);
        System.out.println(appleBox.getQty() + "шт.");
        System.out.println("Коробка с яблоками новая " + newAppleBox.getQty() + "шт." + orangeBox.getWeight());

        orangeBox.add(new Orange(2.1f));
        orangeBox.remove(new Orange());
        System.out.println("Коробка с апельсинами " + orangeBox.getQty() + "шт. " + orangeBox.getWeight());

        if (newAppleBox.compare(orangeBox)) {
            System.out.println("Веса равны");
        } else {
            System.out.println("Веса отличны");
        }

    }


    // 1. Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
    public static <T> void chgElemArray(T[] arr, int el1, int el2) {
        if (el1 < 0 || el2 < 0 || el1 >= arr.length || el2 >= arr.length)
            throw new ArrayIndexOutOfBoundsException("Указанные элемены больше размерности массива");

        T tmp = arr[el1];
        arr[el1] = arr[el2];
        arr[el2] = tmp;
    }

    // 2. Написать метод, который преобразует массив в ArrayList;
    public static <T> List<T> transArrToList(T[] arr) {
        if (arr == null) return new ArrayList<>();

        List<T> arrList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            arrList.add(arr[i]);
        }
        return arrList;
    }

}
