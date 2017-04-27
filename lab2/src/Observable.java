import javax.swing.*;
import java.util.List;

/**
 * Created by Alexander on 14/04/2017.
 */
public interface Observable {
    public void addObserver(Observer observer);
    public void removeObserver(Observer observer);
    public abstract void notifyObservers();
}
