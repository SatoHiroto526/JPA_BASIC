package com.example.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "listB")
// @IdClassで主キークラス指定
@IdClass(ListBId.class)
@Getter
@Setter
public class ListB implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    private int id;

    @Id
    @Column(name="data")
    private String data;

    public ListB() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListB)) return false;
        ListB other = (ListB) o;
        return id == other.id &&
               Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }

}
