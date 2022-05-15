package com.library.libraryAPI.api.exception;

import com.library.libraryAPI.exception.BusinessException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {
    private List<String> errors;
    public ApiErrors(BindingResult bindingResult) {
        errors = new ArrayList<>();
        bindingResult.getAllErrors().stream().forEach(err->this.errors.add(err.getDefaultMessage()));
    }

    public ApiErrors(BusinessException ex) {
        errors = Arrays.asList(ex.getMessage());
    }

    public List<String> getErrors(){
        return errors;
    }
}
