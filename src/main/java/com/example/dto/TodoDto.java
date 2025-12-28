package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoDto {

    private int id;

    private String todo;

    private String priority;

    private String detail;

    // Accout情報
    private AccountDto accountDto;

    // 以下、集約関数用
    private int min;

    private int max;

    private Long count;

    // 以下、ウィンドウ関数用
    private int userrank;

    // 引数なしコンストラクタ
    public TodoDto() {
        super();
    }

    // コンストラクタ式用コンストラクタ
    // aggregateByUserIdメソッド用
    // コンストラクタ式ではJPQLとコンストラクタのシグネチャが一致している必要がある。
    // そのため、todoやpriority、detailは含めていない
    public TodoDto(int userId,
                String username,
                int min,
                int max,
                Long count) {
        this.accountDto = new AccountDto(userId, username);
        this.min = min;
        this.max = max;
        this.count = count;
    }

    // ネイティブクエリ用コンストラクタ
    // useWindowFunctionメソッド用
    public TodoDto(int id,
                String todo,
                String priority,
                String detail,
                int userId,
                String username,
                int userrank) {
        this.id = id;
        this.todo = todo;
        this.priority = priority;
        this.detail = detail; 
        this.accountDto = new AccountDto(userId, username);
        this.userrank = userrank;
    }
    
}
