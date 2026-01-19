package com.example.repository;

import java.util.List;
import java.util.Optional;

import com.example.dto.TodoDto;
import com.example.entity.Account;
import com.example.entity.Todo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

// JPQLを用いた永続化
// JPQL：Java Persistence Query Language
// SQLはデータベースへのクエリを記述するが、JPQLはエンティティへのクエリを記述する。
// JPQLの基本構文：SELECT [取得するエンティティ or 式] FROM [エンティティクラス名] [エイリアス]

// 基本的な方針を考えてみた。
// エンティティには必ず@Table、@Column、@JoinColumnを明記
// エンティティに記述するリレーションは@ManyToOne
// 優先度はJPQL（エンティティにマッピング⇒DTOにコンバート）>コンストラクタ式>ネイティブSQL
// エンティティ全体ではなく、カラムを明示してSELECTしたい場合はコンストラクタ式を使う。
// ネイティブSQLを使うサイナ@SqlResultSetMappingと@ConstructorResultでDTOのコンストラクタにマッピング
// N + 1問題があるため、なるべくJOINフェッチ使う。
@RequestScoped
public class Jpql {

    // WHEREで使える関係演算子
    // =
    // <>
    // <
    // <=
    // >
    // >=

    // その他BETWEENやIN、IS NULLやNOT、LIKE等も標準SQLと同じように使用可能

    // エンティティマネージャクラスのフィールドには@PersistenceContextを付与
    @PersistenceContext
    private EntityManager em;

    Todo todo = new Todo();

    // 1件のみ取得（Joinして1件取得）
    public Todo findById(int id) {
        // パラメータとして埋め込む部分は「:変数名」で設定
        // SELECT句でt.id、t.detail...のように指定するとエラーになるため、エンティティのエイリアスのみ指定
        // ⇒SELECT句には戻り値として設定したエンティティのクラス（のエイリアス）を指定⇒指定しないとキャストできないのでClassCastException
        // カスタムが必要な場合はコンストラクタ式を使う。
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
        String jpql = " SELECT t  " 
                    + " FROM Todo t INNER JOIN FETCH t.account "
                    + " ORDER BY t.id ";
        
        // フェッチ：取得するエンティティにリレーションが定義されている場合、リレーション先のエンティティをどのタイミングでデータベースから
        // 取得するかを決めることができる。これをフェッチ戦略と呼ぶ。
        // 設定可能なフェッチ戦略は以下3つ。
        // ①Eagerフェッチ：@OneToOne、@ManyToOneのデフォルトのフェッチ戦略。
        //  @ManyToOne(fetch = FetchType.EAGER)で明示可能
        //  エンティティを取得する際に、リレーション先のエンティティもそれぞれ取得する。
        //  ⇒TodoとAccountの場合、それぞれを取得するために2回クエリを発行する。
        // ②Lazyフェッチ：@OneToMany、@ManyToManyのデフォルトのフェッチ戦略。
        //  @ManyToOne(fetch = FetchType.LAZY)で明示可能
        //  取得処理時にリレーション構築は行わず、リレーション先に初めてアクセスする際にエンティティを取得する。
        // ③JOINフェッチ：結合元／結合先エンティティを一撃で取得する。（1発のクエリで済む。）
        //  構文は以下。
        //  [INNER][LEFT OUTER] JOIN FETCH リレーションのフィールと エイリアス
        //  JOINフェッチを使う際は、リレーション先となるフィールドにはエイリアスを付けない
        //  例）FROM Todo t INNER JOIN FETCH t.account ⇒OK、FROM Todo t INNER JOIN FETCH t.account a ⇒NG
        //  ※コンストラクタ式を使う場合はそもそもフェッチという概念がないため注意！

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
        // 戻り値はDtoのList型、creatNativeQueryの第二引数にエンティティに付与した@SqlResultSetMappingの名前をセット
        return (List<TodoDto>)em.createNativeQuery(sql, "TodoDtoMapping").getResultList();
    }

    // ネイティブSQLを使う場合②
    public List<TodoDto> useWindowFunction2() {
        // ネイティブSQL（ウィンドウ関数）
        String sql = " SELECT t.id, t.todo, t.priority, t.detail, a.userId, a.username, "
                    + " RANK() OVER (PARTITION BY a.userId ORDER BY t.id) AS userrank " 
                    + " FROM jakartaee.Todo t INNER JOIN jakartaee.Account a ON t.userId = a.userId "
                    + " WHERE a.userId = :userId ";

        // Query生成
        Query query = em.createNativeQuery(sql, "TodoDtoMapping");

        // パラメーターセット
        query.setParameter("userId", 1);

        // ② SQL実行
        List<?> result = query.getResultList();

        // ③ DTO型へキャスト
        List<TodoDto> todoList = (List<TodoDto>) result;

        return todoList;
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
        int result = em.createNamedQuery("todo.update")
                                .setParameter("todo", todo.getTodo())
                                .setParameter("priority", todo.getPriority())
                                .setParameter("account", todo.getAccount())
                                .setParameter("id", todo.getId())
                                .executeUpdate();
        // JPQL UPDATEではエンティティマネージャにはupdateが反映されず、永続化コンテキスト由来の不整合が発生する可能性があるため明示的にクリアする。
        // ただし、キャッシュは消えてしまう。
        // また、コミット前にクリアすると変更がDBに反映されないため明示的にflushしては反映したうえでclearする。
        // mergeメソッドの使用も検討
        em.flush();
        em.clear();
        return result;
    }

    // DELETE用
    public int deleteTodo(int id) {
        int result = em.createNamedQuery("todo.delete")
                    .setParameter("id", id)
                    .executeUpdate();
        // JPQL DELETEではエンティティマネージャにはdeleteが反映されず、永続化コンテキスト由来の不整合が発生する可能性があるため明示的にクリアする。
        // ただし、キャッシュは消えてしまう。
        // また、コミット前にクリアすると変更がDBに反映されないため明示的にflushしては反映したうえでclearする。
        // removeメソッドの使用も検討
        em.flush();
        em.clear();
        return result;
    }

    // マネージド状態のエンティティをSetterで更新し、そのままトランザクション終了までもっていくことで更新
    // 画面で編集して更新する場合、デタッチドのエンティティを使うことになるためmerge()メソッドでマネージドにして更新
    // SELECT⇒UPDATEの場合、SELECTの時点でマネージドになるためmerge()は不要
    public void managerUpdate() {
        // JPQLでマネージド状態にする。
        Account account = em.createQuery(" SELECT a FROM Account a WHERE a.userId = :userId ", Account.class)
                            .setParameter("userId", 1)
                            .getSingleResult();

        // Setterで更新
        account.setUsername("佐藤");

        // トランザクション終了時にDBに反映

    }

    // 安全な1件取得：取得できる場合
    public Optional<Account> successGetSingle() {
        List<Account> list = em.createQuery(" SELECT a FROM Account a WHERE a.userId = :userId ", Account.class)
                            .setParameter("userId", 1)
                            // 悲観ロックをかける
                            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                            .getResultList();
        
        if(list.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(0));
        }
        
    }

    // 安全な1件取得：取得できない場合
    public Optional<Account> failGetSingle() {
        List<Account> list = em.createQuery(" SELECT a FROM Account a WHERE a.userId = :userId ", Account.class)
                            // 悲観ロックをかける
                            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                            .setParameter("userId", 100)
                            .getResultList();
        
        if(list.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(0));
        }
        
    }

    // Account取得用
    public List<Account> getAccountList() {
        return em.createNamedQuery("account.getAccountList", Account.class).getResultList();
    }
    
}