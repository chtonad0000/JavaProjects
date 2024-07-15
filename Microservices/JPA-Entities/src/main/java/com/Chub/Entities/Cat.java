package com.Chub.Entities;

import com.Chub.Models.CatBreed;
import com.Chub.Models.CatColor;
import lombok.Getter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Cats")
public class Cat {
    public Cat(Long id, String name, LocalDate birthDate, CatBreed breed, CatColor color, Owner owner) {
        this.id = id;
        this.name = name;
        this.birth_date = birthDate;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
    }
    public Cat() {
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private final Set<Cat> _friends = new HashSet<>();
    @Getter
    @jakarta.persistence.Id
    private Long id;
    @Getter
    private String name;
    @Getter
    private LocalDate birth_date;
    @Getter
    @Enumerated(EnumType.ORDINAL)
    private CatBreed breed;
    @Getter
    @Enumerated(EnumType.ORDINAL)
    private CatColor color;

    @Getter
    @ManyToOne
    @JoinColumn(name = "owner")
    private Owner owner;

    public Set<Cat> GetFriendsList() {
        return _friends;
    }

    public void MakeFriend(Cat cat) {
        _friends.add(cat);
    }
}
