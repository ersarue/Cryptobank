package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Customer;

import java.util.List;

public interface GenericService<E> {

    public E findById(int id);
    public int storeOne(E e);
    public List<E> getAll();
    public int updateOne(E e);
    public int deleteOne(int id);
}
