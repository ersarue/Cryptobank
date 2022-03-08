package nl.miwteam2.cryptomero.repository;

import java.util.List;

public interface GenericDao<E> {

    //todo vind overeenstemming of en hoe we dit gaan gebruiken
    public E findById(int id);
    public void storeOne(E e);
    public List<E> getAll();
//    public void updateOne(E e);
//    public void deleteOne(int id);
}

