package Assignment3;

/**
 *
 * @author Khanh Tran
 */
import java.util.Comparator;

/** The default comparator to be used */
public class DefaultComparator<E> implements Comparator<E> {
    public int compare(E a, E b) throws ClassCastException {
        return ((Comparable<E>)a).compareTo(b);
    }
}
