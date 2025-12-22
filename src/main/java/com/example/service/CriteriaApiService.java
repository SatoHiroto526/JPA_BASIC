package com.example.service;

import java.util.ArrayList;
import java.util.List;

import com.example.convert.Convert;
import com.example.dto.ListDto;
import com.example.entity.ListA;
import com.example.entity.ListB;
import com.example.repository.CriteriaApi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
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
    
}
