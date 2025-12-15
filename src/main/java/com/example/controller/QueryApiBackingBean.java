package com.example.controller;

import java.io.Serializable;

import com.example.dto.AccountDto;
import com.example.dto.TodoDto;
import com.example.service.QueryAPIService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@SessionScoped
@Named
@Getter
@Setter
public class QueryApiBackingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private QueryAPIService service;

    private TodoDto dto;

    private boolean exists;

    @PostConstruct
    public void init() {
        this.dto = new TodoDto();
        AccountDto accountDto = new AccountDto();
        dto.setAccountDto(accountDto);
    }

    public void findById() {
        this.exists = false;
        TodoDto tmpDto = service.findById(dto.getId());

        try {
            tmpDto.getId();
            this.exists = true;
            this.dto = tmpDto;
        }catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String update() {
        service.update(dto);
        // Ajaxで呼ぶメソッドはredirectしないほうが良いらしい。
        return "/queryApi/queryApiHome.xhtml";
    }

    public String delete() {
        service.deleteById(dto.getId());
        return "/queryApi/queryApiHome.xhtml";
    }

    public String add() {
        service.add(dto);
        return "/queryApi/queryApiHome.xhtml";
    }
    
}