package com.example.avis_utilisateur.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import java.util.Objects;

@Getter
@Setter
public class PageRequestDto {
    private Integer pageNo = 0;
    private  Integer pageSize = 10;

    public Pageable getPageable(PageRequestDto dto){
        Integer page = Objects.nonNull(dto.getPageNo()) ? dto.pageNo : this.pageNo;
        Integer size =  Objects.nonNull(dto.getPageSize()) ? dto.pageNo : this.pageSize;
        return PageRequest.of(page,size);
    }
}
