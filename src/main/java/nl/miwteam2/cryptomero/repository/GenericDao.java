package nl.miwteam2.cryptomero.repository;

import java.util.List;

public interface GenericDao<E> {

    public E findById(int id);
    public void storeOne(E e);
    public List<E> getAll();
}
