import java.util.Arrays;

public class Main {
    private static final int SEARCH_NUMBER = 4;

    public static void main(String[] args) {
        int[] arr;
        arr = new int[]{1, 2, 4, 4, 2, 3, 4, 5, 7};
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(getArrFromLast4(arr)));

        arr = new int[]{1, 2, 1, 2, 2, 3, 1, 5, 7};
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(getArrFromLast4(arr)));

        System.out.println(checkArrIncl_1_4(new int[]{1, 4, 1}));
        System.out.println(checkArrIncl_1_4(new int[]{1, 1, 1}));
        System.out.println(checkArrIncl_1_4(new int[]{4, 4, 4}));
    }

    //2. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
    // Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов, идущих после последней четверки.
    //		Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException.
    //		Написать набор тестов для этого метода (по 3-4 варианта входных данных). Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
    public static int[] getArrFromLast4(int[] inArr) throws RuntimeException {
        int pos = -1;
        for (int i = inArr.length - 1; i >= 0; i--) {
            if (inArr[i] == SEARCH_NUMBER) {
                pos = i + 1;
                break;
            }
        }
        if (pos == -1) {
            throw new RuntimeException("Массив не содержит ни одной " + SEARCH_NUMBER);
        }
        return Arrays.copyOfRange(inArr, pos, inArr.length);
    }

    //		3. Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки или единицы,
    //		то метод вернет false;
    //		Написать набор тестов для этого метода (по 3-4 варианта входных данных).
    public static boolean checkArrIncl_1_4(int[] inArr) {
        boolean isThere1 = false, isThere4 = false;
        for (int i = 0; i < inArr.length; i++) {
            if (inArr[i] == 1) isThere1 = true;
            if (inArr[i] == 4) isThere4 = true;
        }
        return (isThere1 && isThere4);
    }
}

