package com.example.entity;

import java.io.Serializable;
import java.util.Objects;

// ListBは複合主キーなので、主キー用クラスを作成し、エンティティに@IdClassを指定
public class ListBId implements Serializable {

    private int id;
    private String data;

    public ListBId() {}

    public ListBId(int id, String data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListBId)) return false;
        ListBId other = (ListBId) o;
        return id == other.id &&
               Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }

    
}
