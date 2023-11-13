package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

   public List<Mpa> getMpaList() {
       return mpaStorage.getMpaList();
   }

   public Mpa getMpaById(int id) {
       if (id < 0) {
           throw new ObjectNotFoundException("Id не может быть меньше 0");
       }
       return mpaStorage.getMpaById(id);
   }
}
