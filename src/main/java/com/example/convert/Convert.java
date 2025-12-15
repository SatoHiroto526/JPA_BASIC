package com.example.convert;

import com.example.dto.AccountDto;
import com.example.dto.TodoDto;
import com.example.entity.Account;
import com.example.entity.Todo;

import jakarta.enterprise.context.Dependent;

@Dependent
public class Convert {

    // Account（エンティティ）⇒AccontDto
    public AccountDto convertAccountEntityToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setUserId(account.getUserId());
        if(account.getUsername() != null) {
            dto.setUsername(account.getUsername());
        }
        return dto;
    }

    // AccontDto⇒Account（エンティティ）
    public Account convertAccontDtoToEntity (AccountDto dto) {
        Account account = new Account();
        account.setUserId(dto.getUserId());
        if(dto.getUsername() != null) {
            account.setUsername(dto.getUsername());
        }
        return account;
    }

    // Todo（エンティティ）⇒TodoDto
    public TodoDto convertTodoEntityToDto(Todo todo) {
        TodoDto dto = new TodoDto();
        dto.setId(todo.getId());
        if(todo.getTodo() != null) {
            dto.setTodo(todo.getTodo());
        }
        if(todo.getPriority() != null) {
            dto.setPriority(todo.getPriority());
        }
        if(todo.getDetail() != null) {
            dto.setDetail(todo.getDetail());
        }   

        AccountDto accountDto = new AccountDto();
        if (todo.getAccount() != null) {
            accountDto = convertAccountEntityToDto(todo.getAccount());
        }
        dto.setAccountDto(accountDto);

        return dto;
    }

    // TodoDto⇒Todo（エンティティ）
    public Todo convertTodoDtoToEntity(TodoDto dto) {
        Todo todo = new Todo();
        todo.setId(dto.getId());
        if(dto.getTodo() != null) {
            todo.setTodo(dto.getTodo());
        }
        if(dto.getPriority() != null) {
            todo.setPriority(dto.getPriority());
        }
        if(dto.getDetail() != null) {
            todo.setDetail(dto.getDetail());
        }

        Account account = new Account();
        if (dto.getAccountDto() != null) {
            account = convertAccontDtoToEntity(dto.getAccountDto());
        }
        todo.setAccount(account);

        return todo;
    }
    
}
