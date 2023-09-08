package com.example.loatradelife.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Result<T> {
    private T data;
}
