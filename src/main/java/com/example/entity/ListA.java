package com.example.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "listA")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class ListA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    private int id;

    @Column(name="data")
    private String data;

    // 引数なしコンストラクタ
    public ListA() {}
    
}
