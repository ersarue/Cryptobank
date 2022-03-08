package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Customer;

import java.util.List;

public interface GenericService<E> {

    public E findById(int id);
    public E storeOne(E e) throws Exception;
    public List<E> getAll();
    public E updateOne(E e);
    public E deleteOne(int id);
}
