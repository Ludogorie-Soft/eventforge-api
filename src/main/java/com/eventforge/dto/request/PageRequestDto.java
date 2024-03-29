package com.eventforge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PageRequestDto {

    private Integer pageNo = 0;

    private Integer pageSize = 10;

    private Sort.Direction sort = Sort.Direction.ASC;

    private String sortByColumn = "name";

    public Pageable getPageable(PageRequestDto pageRequestDto) {
        Integer page = Objects.nonNull(pageRequestDto.getPageNo()) ? pageRequestDto.getPageNo() : this.pageNo;
        Integer size = Objects.nonNull(pageRequestDto.getPageSize()) ? pageRequestDto.getPageSize() : this.pageSize;
        Sort.Direction sortDirection = Objects.nonNull(pageRequestDto.getSort()) ? pageRequestDto.getSort() : this.sort;
        String column = Objects.nonNull(pageRequestDto.getSortByColumn()) ? pageRequestDto.getSortByColumn() : this.sortByColumn ;


        return PageRequest.of(page, size , sortDirection , column);
    }


    public PageRequestDto(Integer pageNo, Integer pageSize, Sort.Direction sort, String sortByColumn) {

        this.pageNo = pageNo;


        this.pageSize = pageSize;


        this.sort = sort;


        this.sortByColumn = sortByColumn;


    }
}
