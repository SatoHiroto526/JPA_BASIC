package com.example.repository;

import java.util.List;

import com.example.entity.ListA;
import com.example.entity.ListB;
import com.example.entity.ListB_;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
        // like：あいまい検索 ※setParameterで%aaa%のように指定

        // クライテリアクエリのwhereメソッドの引数に作成したPredicateをセット=WHERE句
        query.where(idEqual);
        TypedQuery<ListB> typedQuery = em.createQuery(query);
        // 実際にパラメータをしていするばあいはJPQLと同様にsetParameter
        typedQuery.setParameter("id", id);
        return typedQuery.getResultList();
    }


    
}
