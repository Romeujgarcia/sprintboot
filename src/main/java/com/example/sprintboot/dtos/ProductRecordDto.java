package com.example.sprintboot.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRecordDto(@NotBlank String name,@NotNull BigDecimal value) {
	// ProductRecordDto
	@Override
	public String toString() {
	    return "ProductRecordDto{" +
	            "name='" + name + '\'' +
	            ", value=" + value +
	            '}';
	}
}
