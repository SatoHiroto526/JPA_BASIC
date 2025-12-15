package com.example.service;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import com.example.convert.Convert;
import com.example.dto.AccountDto;
import com.example.dto.TodoDto;
import com.example.entity.Account;
import com.example.entity.Todo;
import com.example.repository.Jpql;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
@Transactional
public class JqplService {

    @Inject
    private Jpql jpql;

    @Inject
    private Convert convert;

    private static final Logger logger = Logger.getLogger(JqplService.class.getName());

    public TodoDto findById(int id) {
        return convert.convertTodoEntityToDto(jpql.findById(id));
    }

    public List<TodoDto> getList() {
        List<TodoDto> todoDtoList = new ArrayList<>();

        List<Todo> todoList = jpql.getList();
        for(Todo todo : todoList) {
            todoDtoList.add(convert.convertTodoEntityToDto(todo));
        }

        return todoDtoList;
    }

    public List<TodoDto> getListByNamedQuery() {
        List<TodoDto> todoDtoList = new ArrayList<>();

        List<Todo> todoList = jpql.getListByNamedQuery();
        for(Todo todo : todoList) {
            todoDtoList.add(convert.convertTodoEntityToDto(todo));
        }

        return todoDtoList;
    }

    public List<TodoDto> aggregateByUserId() {
        return jpql.aggregateByUserId();
    }

    public List<TodoDto> useWindowFunction() {
        return jpql.useWindowFunction();
    }

    public void insertTodo(TodoDto todoDto) {
        jpql.insertTodo(convert.convertTodoDtoToEntity(todoDto));
    }

    public void updateTodo(TodoDto todoDto) {
        logger.info(jpql.updateTodo(convert.convertTodoDtoToEntity(todoDto)) + "件のTodoを更新しました。");
    }

    public void deleteTodo(int id) {
        logger.info(jpql.deleteTodo(id) + "件のTodoを削除しました。");
    }

    public List<AccountDto> getAccountList() {
        List<AccountDto> accountDtoList = new ArrayList<>();

        List<Account> accountList = jpql.getAccountList();
        for(Account account : accountList) {
            accountDtoList.add(convert.convertAccountEntityToDto(account));
        }
        return accountDtoList;
    }
    
}