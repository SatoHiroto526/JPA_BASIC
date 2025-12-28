package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.dto.TodoDto;
import com.example.entity.Account;
import com.example.entity.Account_;
import com.example.entity.ListA;
import com.example.entity.ListB;
import com.example.entity.ListB_;
import com.example.entity.Todo;
import com.example.entity.Todo_;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

// クライテリアAPI用クラス
@RequestScoped
public class CriteriaApi {

    // メタデータ：エンティティクラス情報
    // メタモデル：エンティティのフィールド情報（クラス名_.フィールド名で取得可能）
    // メタモデル取得例：ListAのidの場合⇒ListA_.id

    // 実際に実行する際はJPQLと同じ感覚。クエリの作成をクエリ言語ではなくメソッドの組み合わせで行う。

    @PersistenceContext
    private EntityManager em;

    // ListA全件取得
    public List<ListA> getListAAll() {

        // クライテリアビルダー作成
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // クライテリアクエリ作成
        // CriteriaQuery<T>、createQuery(T.class)⇒Tは戻り値で使用するエンティティクラス
        CriteriaQuery<ListA> query = builder.createQuery(ListA.class);

        // クライテリアクエリのfromメソッドの戻り値として、引数で指定したエンティティのメタデータ（Rootオブジェクト）を得る。
        Root<ListA> listARoot = query.from(ListA.class);

        // 結果として取得したい式を指定
        // クライテリアクエリのselectメソッドにRootオブジェクトを引数として渡す。
        // ※メタデータ.get(クラス名_.フィールド名)でフィールドのメタモデルを取得可能（特定のフィールドのみ取得）
        query.select(listARoot);

        // ↑上記までがJPQLの定義のようなイメージ

        //　↓以降はJPQLと一緒
        TypedQuery<ListA> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
        // ※JPQLと同じく、1件のみ取得の場合はgetSingleResult()
    }

    // ListB全件取得
    public List<ListB> getListBAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ListB> query = builder.createQuery(ListB.class);

        Root<ListB> listBRoot = query.from(ListB.class);

        query.select(listBRoot);

        TypedQuery<ListB> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    // ListB条件検索
    public List<ListB> getListBByIdd (int id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListB> query = builder.createQuery(ListB.class);
        Root<ListB> listBRoot = query.from(ListB.class);
        query.select(listBRoot);

        // 以下、パラメータ設定
        // ParameterExpression<パラメータで指定するデータの型（ラッパークラス）>
        // parameter(第一引数：パラメータで指定するデータの型（ラッパークラス）, 第二引数："クエリに埋め込むパラメータ")
        // JPQLと異なり、クエリに埋め込むパラメータは「:」不要
        ParameterExpression<Integer> param = builder.parameter(Integer.class, "id");

        // equal 第一引数と第二引数が一致する場合
        // メタモデルのidとパラメータがイコールである場合（要は=）
        Predicate idEqual = builder.equal(listBRoot.get(ListB_.id), param);
        // equal：=
        // gt：>
        // lt：>
        // ge：>=
        // le：<=
        // notEqual：<>

        // クライテリアクエリのwhereメソッドの引数に作成したPredicateをセット=WHERE句
        query.where(idEqual);
        TypedQuery<ListB> typedQuery = em.createQuery(query);
        // 実際にパラメータをしていするばあいはJPQLと同様にsetParameter
        typedQuery.setParameter("id", id);
        return typedQuery.getResultList();
    }

    // Like（あいまい検索）
    public List<ListB> getListBByLike() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListB> query = builder.createQuery(ListB.class);
        Root<ListB> listBRoot = query.from(ListB.class);
        query.select(listBRoot);

        ParameterExpression<String> param = builder.parameter(String.class, "data");
        // LikeのPredicateを作成
        Predicate like = builder.like(listBRoot.get(ListB_.data), param);

        // WHEREにパラメータセット
        query.where(like);

        TypedQuery<ListB> typedQuery = em.createQuery(query);

        typedQuery.setParameter("data", "%A%");
        return typedQuery.getResultList();
    }

    // IN（複数項目の検索）
    public List<ListB> getListBByIn() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListB> query = builder.createQuery(ListB.class);
        Root<ListB> listBRoot = query.from(ListB.class);
        query.select(listBRoot);

        // INを使う際はInを使う
        In contains = builder.in(listBRoot.get(ListB_.data));
        ParameterExpression<String> param1 = builder.parameter(String.class, "data1");
        ParameterExpression<String> param2 = builder.parameter(String.class, "data2");
        contains.value(param1);
        contains.value(param2);

        // WHEREにInをパラメータセット
        query.where(contains);

        TypedQuery<ListB> typedQuery = em.createQuery(query);

        typedQuery.setParameter("data1", "B-1");
        typedQuery.setParameter("data2", "C-1");
        return typedQuery.getResultList();
    }

    // Between
    public List<ListB> getListBByBetween() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListB> query = builder.createQuery(ListB.class);
        Root<ListB> listBRoot = query.from(ListB.class);
        query.select(listBRoot);

        // Betweenの検索範囲（FROM/TO）のパラメータをParameterExpressionで定義
        ParameterExpression<Integer> from = builder.parameter(Integer.class, "from");
        ParameterExpression<Integer> to = builder.parameter(Integer.class, "to");
        // BetweenのPredicateを作成（メタモデル, from, to）
        Predicate between = builder.between(listBRoot.get(ListB_.id), from, to);

        // WHEREにパラメータセット
        query.where(between);

        TypedQuery<ListB> typedQuery = em.createQuery(query);

        typedQuery.setParameter("from", 2);
        typedQuery.setParameter("to", 3);
        return typedQuery.getResultList();
    }

    // 複数条件検索（AND／OR）
    public List<ListB> getListBByAnd() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListB> query = builder.createQuery(ListB.class);
        Root<ListB> listBRoot = query.from(ListB.class);
        query.select(listBRoot);

        ParameterExpression<Integer> idParam = builder.parameter(Integer.class, "id");
        ParameterExpression<String> dataParam = builder.parameter(String.class, "data");
        // 条件の数だけPredicateオブジェクトを作成
        Predicate idPre = builder.equal(listBRoot.get(ListB_.id), idParam);
        Predicate dataPre = builder.like(listBRoot.get(ListB_.data), dataParam);

        // CriteriaBuilderオブジェクトのandメソッドで結んだPredicateオブジェクトを作成（orの場合はorメソッド）
        Predicate orPre = builder.and(idPre,dataPre);

        // WHEREにパラメータセット
        query.where(orPre);

        TypedQuery<ListB> typedQuery = em.createQuery(query);

        typedQuery.setParameter("id", 2);
        typedQuery.setParameter("data", "%3%");
        return typedQuery.getResultList();
    }

    // Order By
    public List<ListB> getListBByOrder() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListB> query = builder.createQuery(ListB.class);
        Root<ListB> listBRoot = query.from(ListB.class);
        query.select(listBRoot);

        // Order Byに使うOrderオブジェクトを作成
        // CriteriaBuilderオブジェクトのメソッド（昇順：asc／降順：desc）を指定
        Order order = builder.desc(listBRoot.get(ListB_.id));

        // CriteriaQueryオブジェクトにorderByメソッドでOrderをセット
        // ※複数セットする場合はorderBy(List<Order>)
        query.orderBy(order);

        TypedQuery<ListB> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }

    // Join
    public List<Todo> getListByJoin() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Todo> query = builder.createQuery(Todo.class);
        Root<Todo> todoRoot = query.from(Todo.class);
        query.select(todoRoot);

        // Joinインターフェースのオブジェクトを取得
        // 取得するにはRootオブジェクトのjoinメソッドを使う。
        // 第一引数：メタモデルのうち、リレーションを表すフィールド
        // 第二引数：結合タイプ
        // 結合タイプは次の二択：JoinType.INNER、JoinType.LEFT
        // Joinインターフェースのオブジェクトが、Accountのメタデータとして扱うことができるようになる。
        Join<Todo, Account> accountJoin = todoRoot.join(Todo_.account, JoinType.INNER);

        // Joinフェッチをするときは、Rootオブジェクトのfetchメソッドにリレーションを表すフィールドのメタモデルを指定。
        //　1回の通信ででJoin対象のオブジェクトを取得する。
        // 以下を記述しない場合、EagerフェッチもしくはLazyフェッチになる。（デフォルトはリレーションのアノテーション次第）
        todoRoot.fetch(Todo_.account);

        // 以下、ORDER BY Todo.id、Account.userIdを表す。
        Order order1 = builder.desc(todoRoot.get(Todo_.id));
        Order order2 = builder.desc(accountJoin.get(Account_.userId));
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        query.orderBy(orders);

        TypedQuery<Todo> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }

    // 集約関数
    // JPQLと同じくコンストラクタ式を使う。
    public List<TodoDto> getListByAggregate() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        // エンティティではなくDtoを指定
        CriteriaQuery<TodoDto> query = builder.createQuery(TodoDto.class);
        // Rootはエンティティの概念なのでエンティティを指定
        Root<Todo> todoRoot = query.from(Todo.class);

        Join<Todo, Account> accountJoin = todoRoot.join(Todo_.account, JoinType.INNER);

        // エンティティのメタモデルでGROUP BY 
        query.groupBy(accountJoin.get(Account_.userId), accountJoin.get(Account_.username));

        // 集約関数部分の記述
        // CriteriaBuilder.集約関数のメソッドをExpression<>形で受け取る。 ※<>はラッパークラスを指定
        //　戻り値のデータ型の制約はJPQL同様
        Expression<Integer> min = builder.min(todoRoot.get(Todo_.id));
        Expression<Integer> max = builder.max(todoRoot.get(Todo_.id));
        Expression<Long> count = builder.count(todoRoot.get(Todo_.id));

        // コンストラクタに結果をマッピング
        // CriteriaBuilder.constructメソッドの結果をCompoundSelection<DTO>に代入
        // constructメソッドの第一引数にはDTO.class、第二引数以降にコンストラクタに代入したい結果（列）を記述。
        CompoundSelection<TodoDto> todo = builder.construct(TodoDto.class, accountJoin.get(Account_.userId),
                                            accountJoin.get(Account_.username),
                                            min, max, count);
        
        // selectには上記で作成したCompoundSelection<DTO>オブジェクトを代入
        query.select(todo);

        TypedQuery<TodoDto> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }
    


    
}
