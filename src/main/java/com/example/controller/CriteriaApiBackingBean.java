package com.example.controller;

import java.io.Serializable;
import java.util.List;

import com.example.service.CriteriaApiService;
import com.example.dto.ListDto;
import com.example.dto.TodoDto;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
@Getter
@Setter
public class CriteriaApiBackingBean implements Serializable {

    private List<ListDto> listA;

    private List<ListDto> listB;

    private int listAId;

    private int listBId;

    private List<ListDto> listBLike;

    private List<ListDto> listBIn;

    private List<ListDto> listBBetween;

    private List<ListDto> listBAnd;

    private List<ListDto> listBOrder;

    private List<TodoDto> todoListJoin;

    private List<TodoDto> todoListAggregate;

    @Inject
    private CriteriaApiService service;

    @PostConstruct
    public void init () {
        getListAAll();
        getListBAll();
        getListBByLike();
        getListBByIn();
        getListBByBetween();
        getListBByAnd();
        getListBByOrder();
        getListByJoin();
        getListByAggregate();
    }

    public void getListAAll() {
        this.listA = service.getListAAll();
    }

    public void getListBAll() {
        this.listB = service.getListBAll();
    }

    public void getListBById() {
        this.listB = service.getListBByIdd(listAId);
    }

    public void printSelectedId() {
        System.out.println("listAId：" + listAId);
        System.out.println("listBId：" + listBId);
    }

    public void getListBByLike() {
        this.listBLike = service.getListBByLike();
    }

    public void getListBByIn() {
        this.listBIn = service.getListBByIn();
    }

    public void getListBByBetween() {
        this.listBBetween = service.getListBByBetween();
    }

    public void getListBByAnd() {
        this.listBAnd = service.getListBByAnd();
    }

    public void getListBByOrder() {
        this.listBOrder = service.getListBByOrder();
    }

    public void getListByJoin() {
        this.todoListJoin = service.getListByJoin();
    }

    public void getListByAggregate() {
        this.todoListAggregate = service.getListByAggregate();
    }

}
