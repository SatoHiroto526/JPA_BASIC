package com.example.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
@NamedQueries(
    {
        @NamedQuery(name = "account.getAccountList",
            query = " SELECT a FROM Account a "
        )
    }
)
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "userId")
    private int userId;

    @Column(name = "username")
    private String username;

    // AccountとTodoは一対多の関係であるため、@OneToManyアノテーションを付与して関連付けを行う。
    // 一側にはListなどのコレクションで多側を保持する形にする。
    // 一側にはmappedBy属性を指定して、多側に指定した一側のクラスのフィールド名を明記する。
    @OneToMany(mappedBy = "account")
    private List<Todo> todoList;
    // 一対多の場合：@OneToMany
    // 多対一の場合：@ManyToOne
    // 多対多の場合：@ManyToMany 
    // 一対一の場合：@OneToOne

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + userId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (userId != other.userId)
            return false;
        return true;
    }

}
