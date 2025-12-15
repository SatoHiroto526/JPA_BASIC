package com.example.service;

import com.example.convert.Convert;
import com.example.dto.TodoDto;
import com.example.repository.QueryAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
// @Transactional：トランザクションの開始と終了の境界を設定
// クラスに指定することで、そのクラスのすべてのメソッドがトランザクションの境界となる。
@Transactional
public class QueryAPIService {

    @Inject
    private QueryAPI queryApi;

    @Inject
    private Convert convert;

    // 参照
    public TodoDto findById(int id) {
        return convert.convertTodoEntityToDto(queryApi.findById(id));
    }

    // 追加
    public void add(TodoDto dto) {
        queryApi.add(convert.convertTodoDtoToEntity(dto));
    }

    // 更新
    public void update(TodoDto dto) {
        queryApi.update(convert.convertTodoDtoToEntity(dto));
    }

    // 削除
    public void deleteById(int id) {
        queryApi.deleteById(id);
    }
    
}
