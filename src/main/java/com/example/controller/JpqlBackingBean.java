package com.example.controller;

import java.io.Serializable;
import java.util.List;

import com.example.dto.AccountDto;
import com.example.dto.TodoDto;
import com.example.service.JqplService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class JpqlBackingBean implements Serializable {

    @Inject
    private JqplService service;

    private TodoDto todoDto;

    // getListメソッド用
    private List<TodoDto> getList;

    //　getListByNamedQueryメソッド用
    private List<TodoDto> getListByNamedQuery;
    
    // aggregateByUserIdメソッド用
    private List<TodoDto> aggregateByUserId;

    // useWindowFunctionメソッド用
    private List<TodoDto> useWindowFunction;

    // useWindowFunction2メソッド用
    private List<TodoDto> useWindowFunction2;

    private List<AccountDto> getAccountList;

    private AccountDto successDto;

    private AccountDto failDto;

    @Inject
    private ExternalContext externalContext;

    @PostConstruct
    public void init() {
        // 諸々new
        this.todoDto = new TodoDto();
        AccountDto accountDto = new AccountDto();
        todoDto.setAccountDto(accountDto);

        this.successDto = new AccountDto();
        this.failDto = new AccountDto();

        // 初期表示用
        this.getList = service.getList();
        this.getListByNamedQuery = service.getListByNamedQuery();
        this.aggregateByUserId = service.aggregateByUserId();
        this.useWindowFunction = service.useWindowFunction();
        this.useWindowFunction2 = service.useWindowFunction2();
        this.getAccountList = service.getAccountList();
        this.successDto = service.successGetSingle();
        this.failDto = service.failGetSingle();
    }

    public void findById() {
        this.todoDto = service.findById(todoDto.getId());
    }

    public String insertTodo() {
        service.insertTodo(todoDto);
        // @ViewScopedだと@PostConstructが呼び出されないため、明示的にinitメソッドを実行
        init();
        return "/queryApi/jpqlHome.xhtml";
    }

    public String updateTodo() {
        service.updateTodo(todoDto);
        init();
        return "/queryApi/jpqlHome.xhtml";
    }

    public String deleteTodo() {
        service.deleteTodo(todoDto.getId());
        init();
        return "/queryApi/jpqlHome.xhtml";
    }

    public String managerUpdate() {
        service.managerUpdate();
        init();
        return "/queryApi/jpqlHome.xhtml";
    }
    
}