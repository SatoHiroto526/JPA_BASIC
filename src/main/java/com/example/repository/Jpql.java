package com.example.repository;

import java.util.List;

import com.example.dto.TodoDto;
import com.example.entity.Account;
import com.example.entity.Todo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

// JPQLを用いた永続化
// JPQL：Java Persistence Query Language
// SQLはデータベースへのクエリを記述するが、JPQLはエンティティへのクエリを記述する。
// JPQLの基本構文：SELECT [取得するエンティティ or 式] FROM [エンティティクラス名] [エイリアス]
@RequestScoped
public class Jpql {

    // WHEREで使える関係演算子
    // =
    // <>
    // <
    // <=
    // >
    // >=

    // その他BETWEENやIN、IN NULLやNOT、LIKE等も標準SQLと同じように使用可能

    // エンティティマネージャクラスのフィールドには@PersistenceContextを付与
    @PersistenceContext
    private EntityManager em;

    Todo todo = new Todo();

    // 1件のみ取得（Joinして1件取得）
    public Todo findById(int id) {
        // パラメータとして埋め込む部分は「:変数名」で設定
        // SELECT句でt.id、t.detail...のように指定するとエラーになるため、エンティティのエイリアスのみ指定
        String jpql = " SELECT t "
                    + " FROM Todo t INNER JOIN t.account a "
                    + " WHERE t.id = :id ";
        
        // TypedQuery<Todo>
        // JPQLを使う際はcreateQueryメソッド第一引数にjpql、第二引数にクエリ実行結果として受け取りたいエンティティクラス.classを指定
        TypedQuery<Todo> query = em.createQuery(jpql, Todo.class);
        //setParameterメソッドの第一引数にJPQLに埋め込んだ変数名、第二引数に値をセット
        query.setParameter("id", id);

        // 1件のみ取得する場合はgetSingleResultメソッドを使用
        return query.getSingleResult();
    }

    // 全件取得（INNER Joinして全件取得）
    public List<Todo> getList() {

        // @ManyToOn等でリレーションは記述しているためONは書かない。
        // ONで結合条件をかけないため、どうしても必要な場合はネイティブクエリを使う。
        String jpql = " SELECT t " 
                    + " FROM Todo t INNER JOIN t.account a "
                    + " ORDER BY t.id ";

        // 複数件取得する場合はgetResultListメソッドを使用
        return em.createQuery(jpql, Todo.class).getResultList();
    }

    // 全件取得（NamedQuery使用版）
    public List<Todo> getListByNamedQuery() {
        // NamedQueryを使用する場合はCreateNamedQueryメソッドを利用
        // 第一引数に実行するNamedQuery、第二引数に実行結果として受け取るエンティティのクラス名を指定
        return em.createNamedQuery("todo.getListByNamedQuery", Todo.class).getResultList();
    }

    // 集約関数／GROUP BYを使用
    // ユーザーごとのTodo件数を取得
    // コンストラクタ式を使用
    public List<TodoDto> aggregateByUserId() {
        // SELECTの後ろにコンストラクタ式で用意したクラスをNEW パッケージ.クラス(引数)で指定
        String jpql = " SELECT "
                + " NEW com.example.dto.TodoDto( "
                + " a.userId, a.username, "
                + " min(t.id), max(t.id), count(t.id)) "
                + " FROM Todo t INNER JOIN t.account a " 
                + " GROUP BY a.userId, a.username "
                + " ORDER BY a.userId ";
        TypedQuery<TodoDto> query = em.createQuery(jpql, TodoDto.class);
        return query.getResultList();
    }
    // 集約関数の戻り値
    // COUNT：LONG
    // MAX：引数に指定したフィールドと同じ型
    // MIN：引数に指定したフィールドと同じ型
    // SUM：Long、Double、BigInteger、BigDecimal
    // AVG：Double


    // ネイティブSQLを使う場合：createNativeQuery
    // ただし、JPQLはRDBMSごとのSQLの差異を吸収できるため、なるべくJPQLを使う。
    // ウィンドウ関数は標準JPQLではサポートされていないためネイティブSQLを使う。
    // Dto + Dtoのコンストラクタ + エンティティに付与したSqlResultSetMappingを使う。
    public List<TodoDto> useWindowFunction() {
        // ネイティブSQL（ウィンドウ関数）
        String sql = " SELECT t.id, t.todo, t.priority, t.detail, a.userId, a.username, "
                    + " RANK() OVER (PARTITION BY a.userId ORDER BY t.id) AS userrank " 
                    + " FROM jakartaee.Todo t INNER JOIN jakartaee.Account a ON t.userId = a.userId ";
        // 戻り値はDtoのList型、creatNativeQueryの第二引数に円てぃてに付与した@SqlResultSetMappingの名前をセット
        return (List<TodoDto>)em.createNativeQuery(sql, "TodoDtoMapping").getResultList();
    }

    // INSERTはINSERT文を書かず、persistメソッドを使うのが正解らしい
    public void insertTodo(Todo todo) {
        // idは最大値+1、存在しなければ1をセットする。
        Integer maxId = em.createQuery(" SELECT MAX(t.id) FROM Todo t ", Integer.class)
                   .getSingleResult();
        if(maxId == null) {
            todo.setId(1);
        } else {
            todo.setId(maxId + 1);
        } 
        em.persist(todo);
    }

    // UPDATE用
    public int updateTodo(Todo todo) {
        return em.createNamedQuery("todo.update")
                                .setParameter("todo", todo.getTodo())
                                .setParameter("priority", todo.getPriority())
                                .setParameter("account", todo.getAccount())
                                .setParameter("id", todo.getId())
                                .executeUpdate();
    }

    // DELETE用
    public int deleteTodo(int id) {
        return em.createNamedQuery("todo.delete")
                    .setParameter("id", id)
                    .executeUpdate();
    }


    // Account取得用
    public List<Account> getAccountList() {
        return em.createNamedQuery("account.getAccountList", Account.class).getResultList();
    }
    
}
