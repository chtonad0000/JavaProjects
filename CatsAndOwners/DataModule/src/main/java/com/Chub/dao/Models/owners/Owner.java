package com.Chub.dao.Models.owners;

import com.Chub.dao.Models.cats.Cat;
import lombok.Getter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owners")
public class Owner {
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private final Set<Cat> _cats = new HashSet<>();

    public Owner(Long id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birth_date = birthDate;
    }

    public Owner() {
    }

    @Id
    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private LocalDate birth_date;


    public Set<Cat> GetCatsList() {
        return _cats;
    }

    public void AddCat(Cat cat) {
        _cats.add(cat);
    }

    public void RemoveCat(Cat cat) {
        _cats.remove(cat);
    }
}
