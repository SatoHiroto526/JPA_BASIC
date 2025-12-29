package com.example.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "listB")
// @IdClassで主キークラス指定
@IdClass(ListBId.class)
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "data"})
public class ListB implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // @IdClassで主キークラス指定した場合でも、@Idは必須。
    @Id
    @Column(name="id")
    private int id;

    @Id
    @Column(name="data")
    private String data;

    public ListB() {}
}
