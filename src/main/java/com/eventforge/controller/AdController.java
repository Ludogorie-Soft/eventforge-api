package com.eventforge.controller;

import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.EventResponse;
import com.eventforge.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

    private final PaginationService paginationService;

    @GetMapping("/active")
    public Page<EventResponse> showAllActiveAds(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn){
        PageRequestDto pageRequestDto = new PageRequestDto(pageNo , pageSize , sort ,sortByColumn);

        return paginationService.getAllActiveAdsByPagination(pageRequestDto);
    }

    @GetMapping("/expired")
    public Page<EventResponse> showAllExpiredAds(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn){
        PageRequestDto pageRequestDto = new PageRequestDto(pageNo , pageSize , sort ,sortByColumn);

        return paginationService.getAllExpiredAdsByPagination(pageRequestDto);
    }
}
