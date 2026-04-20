package util;

import java.util.*;

public class BoundedSet<T> implements Set<T> {
    private final Set<T> data;
    private int maximumCapacity;

    public BoundedSet(int maximumCapacity) {
        data = new LinkedHashSet<>();
        this.maximumCapacity = maximumCapacity;
    }

    public BoundedSet(Collection<? extends T> c, int maximumCapacity) {
        data = new HashSet<>(c);
        this.maximumCapacity = maximumCapacity;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return data.toArray(a);
    }

    @Override
    public boolean add(T t) {
        if (size() == maximumCapacity) return false;
        return data.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return data.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return data.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return data.removeAll(c);
    }

    @Override
    public void clear() {
        data.clear();
    }

}
