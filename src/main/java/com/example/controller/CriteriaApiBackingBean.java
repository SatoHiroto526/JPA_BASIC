package com.example.controller;

import java.io.Serializable;
import java.util.List;

import com.example.service.CriteriaApiService;
import com.example.dto.ListDto;

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

    @Inject
    private CriteriaApiService service;

    @PostConstruct
    public void init () {
        getListAAll();
        getListBAll();
    }

    public void getListAAll() {
        this.listA = service.getListAAll();
    }

    public void getListBAll() {
        this.listB = service.getListBAll();
    }

    public void getListBByIdd() {
        this.listB = service.getListBByIdd(listAId);
    }

    public void printSelectedId() {
        System.out.println("listAId：" + listAId);
        System.out.println("listBId：" + listBId);
    }
    
}
