package com.example.entity;

import java.io.Serializable;

import com.example.dto.TodoDto;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// エンティティクラスであることを表す@Entityアノテーションを付与
// 一応、Serializableインターフェースをimplementsしたほうが良いらしい。
// @Tableでマッピングする物理テーブル名を明記
@Entity
@Table(name = "todo")
@Getter
@Setter
// NamedQuery用
// @NamedQueriesの中に@NamedQueryで複数SQLを指定可能
@NamedQueries(
    {
        // 第一引数にクエリ名（アプリ内で一意）、第二引数にJPQLを記述
        @NamedQuery(name = "todo.getListByNamedQuery",
            query = " SELECT t " 
                    + " FROM Todo t INNER JOIN t.account a "
                    + " ORDER BY t.id "),
        @NamedQuery(name = "todo.update",
            query = " UPDATE Todo t "
                + " SET t.todo = :todo, t.priority = :priority, t.account = :account "
                + " WHERE t.id = :id "),
        @NamedQuery(name = "todo.delete",
            query = " DELETE FROM Todo t WHERE t.id = :id ")
    }
)
// ネイティブSQL（useWindowFunctioメソッド）用
@SqlResultSetMapping(
    name = "TodoDtoMapping",
    classes = @ConstructorResult(
        targetClass = TodoDto.class,
        columns = {
            @ColumnResult(name = "id", type = Integer.class),
            @ColumnResult(name = "todo", type = String.class),
            @ColumnResult(name = "priority", type = String.class),
            @ColumnResult(name = "detail", type = String.class),
            @ColumnResult(name = "userId", type = Integer.class),
            @ColumnResult(name = "username", type = String.class),
            @ColumnResult(name = "userrank", type = Integer.class)
        }
    )
)
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    // エンティティクラスでは、テーブルの主キーを明記似なければならない。
    // @Idで明記。複合主キーの場合は当然、複数のフィールドに@Idを付与する。
    @Id
    // 物理カラム名を明記
    @Column(name = "id")
    private int id;

    @Column(name = "todo")
    private String todo;

    @Column(name = "priority")
    private String priority;

    @Column(name = "detail")
    private String detail;

    // todoテーブルはuserIdを持っているが、外部キーでありTodo.javaに持たせると冗長のため書かない。（Account.javaが持っている。）

    // 大きいサイズのデータのマッピングには@Lobをつける。

    // TodoとAccountは多対一の関係であるため、@ManyToOneアノテーションを付与して関連付けを行う。
    @ManyToOne
    // 外部キーを明記
    @JoinColumn(name = "userId")
    private Account account;
    // 一対多の場合：@OneToMany
    // 多対一の場合：@ManyToOne
    // 多対多の場合：@ManyToMany 
    // 一対一の場合：@OneToOne

    // エンティティクラスは引数なしコンストラクタが必須であるため、一応明記。
    public Todo() {
        super();
        // accountの情報を扱うためaccountをnew
        this.account = new Account();
    }
    
    // エンティティクラスでは、@Idを定義したフィールドを使用してhashCode()とequals()をオーバーライドしなければならない。
    // 基本的には自動生成されたコードを使用すれば良い。
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        Todo other = (Todo) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
}
