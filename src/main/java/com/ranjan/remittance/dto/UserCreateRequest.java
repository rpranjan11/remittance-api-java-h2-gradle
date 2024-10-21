package com.ranjan.remittance.dto;

import com.ranjan.remittance.model.UserDetail;
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;

import java.io.Serializable;
import java.time.LocalDate;

public record UserCreateRequest(

    @Schema(title = "Description", example = "Development Manager")
    String description,

    @Schema(title = "ExpiryDate", example = "2024-12-04")
    LocalDate expireDate,

    @Schema(title = "Locked", example = "false")
    boolean locked

) implements Serializable {

    public static final AccountMapped mapped = Mappers.getMapper(AccountMapped.class);

    @Mapper
    public interface AccountMapped extends Mappable<UserDetail, UserCreateRequest> {
    }
}
