package com.example.repository;

import com.example.entity.Todo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// クエリAPI（エンティティマネージャの用意されているメソッド）を用いた永続化
// ここでは、JPQLを使うcreateQuery以外のメソッドToDoに対するCRUD処理を行う。
@RequestScoped
public class QueryAPI {

    // エンティティマネージャクラスのフィールドには@PersistenceContextを付与
    @PersistenceContext
    private EntityManager em;

    // 参照
    public Todo findById(int id) {
        // persistするとMANAGED状態になる。
        // ※トランザクション終了時にデタッチ
        return em.find(Todo.class, id);
    }

    // 追加
    public void add(Todo todo) {
        // persistするとMANAGED状態になる。
        // ※トランザクション終了時にデタッチ
        em.persist(todo);
    }

    // 更新
    public void update(Todo todo) {
        // 通常、更新対象はDETACHEDなのでそのままmergeする。
        // ※1トランザクションの中で直前にfindやpersistするとMANAGEDになるのでmergeできなくなる。
        em.merge(todo);
    }

    // 削除
    public void deleteById(int id) {
        // removeはMANAGED状態でのみ可能なので、fineでMANAGEDにしたうえでremoveする。
        Todo todo = em.find(Todo.class, id);
        if (todo != null) {
            em.remove(todo);
        }
    }
    
}
