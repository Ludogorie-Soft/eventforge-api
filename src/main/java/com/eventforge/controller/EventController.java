package com.eventforge.controller;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.service.EventService;
import com.eventforge.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EventController {
    private final EventService eventService;

    private final PaginationService paginationService;


    @PostMapping("/filter-by-criteria")
    public Page<CommonEventResponse>getEventsByCriteria(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn, @RequestBody CriteriaFilterRequest filterRequest){
        PageRequestDto pageRequestDto = new PageRequestDto(pageNo , pageSize , sort ,sortByColumn);

        return paginationService.getEventsByCriteriaAndPagination(filterRequest , pageRequestDto);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEventById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id){
        eventService.deleteEventByIdAndUserIdForOrganisation(id , authHeader);
        return new ResponseEntity<>("Успешно изтрихте събитие" , HttpStatus.OK);
    }

}
