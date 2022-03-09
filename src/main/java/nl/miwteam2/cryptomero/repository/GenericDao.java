package nl.miwteam2.cryptomero.repository;

import java.util.List;

public interface GenericDao<E> {

    //todo vind overeenstemming of en hoe we dit gaan gebruiken
    public E findById(int id);
    public int storeOne(E e);
    public List<E> getAll();
    public int updateOne(E e);
    public int deleteOne(int id);
}

