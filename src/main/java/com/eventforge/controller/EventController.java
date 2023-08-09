package com.eventforge.controller;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.service.EventService;
import com.eventforge.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EventController {
    private final EventService eventService;

    private final PaginationService paginationService;


    @PostMapping("/filter-by-criteria")
    public Page<CommonEventResponse> getEventsByCriteria(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", required = false) Integer pageSize
            , @RequestParam(value = "sort", required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn", required = false) String sortByColumn, @RequestBody CriteriaFilterRequest filterRequest) {
        PageRequestDto pageRequestDto = new PageRequestDto(pageNo, pageSize, sort, sortByColumn);

        return paginationService.getEventsByCriteriaAndPagination(filterRequest, pageRequestDto);
    }

}
