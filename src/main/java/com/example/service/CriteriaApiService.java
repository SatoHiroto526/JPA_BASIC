package com.example.service;

import java.util.ArrayList;
import java.util.List;

import com.example.convert.Convert;
import com.example.dto.ListDto;
import com.example.dto.TodoDto;
import com.example.entity.ListA;
import com.example.entity.ListB;
import com.example.entity.Todo;
import com.example.repository.CriteriaApi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
@Transactional
public class CriteriaApiService {

    @Inject
    private CriteriaApi criteriaApi;

    @Inject
    private Convert convert;

    public List<ListDto> getListAAll() {
        List<ListDto> list = new ArrayList<>();
        List<ListA> listAList = criteriaApi.getListAAll();
        for(ListA listA : listAList) {
            list.add(convert.convertListAtToListDto(listA));
        }
        return list;
    }

    public List<ListDto> getListBAll() {
        List<ListDto> list = new ArrayList<>();
        List<ListB> listBList = criteriaApi.getListBAll();
        for(ListB listB : listBList) {
            list.add(convert.convertListBToListDto(listB));
        }
        return list;
    }

    public List<ListDto> getListBByIdd(int id) {
        List<ListDto> list = new ArrayList<>();
        List<ListB> listBList = criteriaApi.getListBByIdd(id);
        for(ListB listB : listBList) {
            list.add(convert.convertListBToListDto(listB));
        }
        return list;
    }

    public List<ListDto> getListBByLike() {
        List<ListDto> list = new ArrayList<>();
        List<ListB> listBList = criteriaApi.getListBByLike();
        for(ListB listB : listBList) {
            list.add(convert.convertListBToListDto(listB));
        }
        return list;
    }

    public List<ListDto> getListBByIn() {
        List<ListDto> list = new ArrayList<>();
        List<ListB> listBList = criteriaApi.getListBByIn();
        for(ListB listB : listBList) {
            list.add(convert.convertListBToListDto(listB));
        }
        return list;
    }

    public List<ListDto> getListBByBetween() {
        List<ListDto> list = new ArrayList<>();
        List<ListB> listBList = criteriaApi.getListBByBetween();
        for(ListB listB : listBList) {
            list.add(convert.convertListBToListDto(listB));
        }
        return list;
    }

    public List<ListDto> getListBByAnd() {
        List<ListDto> list = new ArrayList<>();
        List<ListB> listBList = criteriaApi.getListBByAnd();
        for(ListB listB : listBList) {
            list.add(convert.convertListBToListDto(listB));
        }
        return list;
    }
    
    public List<ListDto> getListBByOrder() {
        List<ListDto> list = new ArrayList<>();
        List<ListB> listBList = criteriaApi.getListBByOrder();
        for(ListB listB : listBList) {
            list.add(convert.convertListBToListDto(listB));
        }
        return list;
    }

    public List<TodoDto> getListByJoin() {
        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = criteriaApi.getListByJoin();
        for(Todo todo : todoList) {
            todoDtoList.add(convert.convertTodoEntityToDto(todo));
        }
        return todoDtoList;
    }

    public List<TodoDto> getListByAggregate() {
        return criteriaApi.getListByAggregate();
    }
    
}
