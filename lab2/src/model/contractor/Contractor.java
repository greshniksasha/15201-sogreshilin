package model.contractor;

import java.util.ArrayList;
import java.util.List;

public class Contractor {
    protected List<TransactionCounterObserver> observers = new ArrayList<>();

    public void addTransactionCounterObserver(TransactionCounterObserver observer) {
        observers.add(observer);
    }

    protected void notifyTransactionCounterObserver(int count) {
        for (TransactionCounterObserver observer : observers) {
            observer.transactionsMade(count);
        }
    }
}
