import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Box<T extends Fruit> {
    private final List<T> сontent = new ArrayList<>();

    public Box() {
    }

    public Box(T... items) {
        сontent.addAll(Arrays.asList(items));
    }

    public Box(List<T> boxContent) {
        boxContent.addAll(boxContent);
    }

    public List<T> getBoxContent() {
        return сontent;
    }

    public void add(T item) {
        сontent.add(item);
    }

    public void add(List<T> items) {
        сontent.addAll(items);
    }

    public void add(T... items) {
        сontent.addAll(Arrays.asList(items));
    }

    public void remove(T item) {
        сontent.remove(item);
    }

    public void remove(T[] items) {
        сontent.removeAll(Arrays.asList(items));
    }

    public void remove(List<T> items) {
        сontent.removeAll(items);
    }

    public float getWeight() {
        float boxWeight = 0;
        for (T item : сontent) {
            boxWeight += item.getWeight();
        }
        return boxWeight;
    }

    public int getQty() {
        return сontent.size();
    }

    public void fill(Box<T> box) {
        add(box.getBoxContent());
        box.empty();
    }

    public void empty() {
        сontent.clear();
    }

    public boolean compare(Box box) {
        return getWeight() == box.getWeight();
    }

}