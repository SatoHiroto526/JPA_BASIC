package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {

    private int userId;

    private String username;

    // 引数なしコンストラクタ
    public AccountDto () {

    }

    // コンストラクタ式用コンストラクタ
    public AccountDto (int userId,
                    String username) {
        this.userId = userId;
        this.username = username;
    }
    
}
