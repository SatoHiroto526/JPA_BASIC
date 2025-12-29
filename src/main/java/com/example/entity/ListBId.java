package com.example.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

// ListBは複合主キーなので、主キー用クラスを作成し、エンティティに@IdClassを指定
@EqualsAndHashCode(of = {"id", "data"})
public class ListBId implements Serializable {

    private int id;
    private String data;

    public ListBId() {}

    public ListBId(int id, String data) {
        this.id = id;
        this.data = data;
    }

    
}