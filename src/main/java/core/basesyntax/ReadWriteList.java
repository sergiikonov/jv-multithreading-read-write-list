package core.basesyntax;

import core.basesyntax.thread.Reader;
import core.basesyntax.thread.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteList<E> {
    private List<E> list = new ArrayList<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void add(E element) {
        lock.writeLock().lock();
        try {
            list.add(element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public E get(int index) {
        lock.readLock().lock();
        try {
            return list.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return list.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteList<Integer> list = new ReadWriteList<>();

        Writer writer = new Writer(list);
        Reader reader = new Reader(list);

        for (int i = 0; i < 5; i++) {
            new Thread(writer).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(reader).start();
        }
    }
}
