package com.eventforge.controller;

import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.service.EventService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unauthorized")
public class UnauthorizedUserController {

    private final EventService eventService;

    private final OrganisationService organisationService;

    private final PaginationService paginationService;

    @GetMapping
    public Page<OrganisationResponse> showAllOrganisationsForUnauthorizedUser(@RequestParam(name = "search", required = false) String search
            , @RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", required = false) Integer pageSize
            , @RequestParam(value = "sort", required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn", required = false) String sortByColumn) {
        PageRequestDto pageRequestDto = new PageRequestDto(pageNo , pageSize , sort ,sortByColumn);
        return paginationService.getAllOrganisationsForUnauthorizedUser(pageRequestDto, search);
    }

    @GetMapping("/organisation/details/{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisationDetails(@PathVariable("organisationId") Long id) {
        return new ResponseEntity<>(organisationService.getOrganisationDetailsByIdWithCondition(id), HttpStatus.OK);
    }

    @GetMapping("/event/details/{id}")
    public ResponseEntity<CommonEventResponse> showEventDetailsWithCondition(@PathVariable("id") Long id) {
        return new ResponseEntity<>(eventService.getEventDetailWithConditionsById(id), HttpStatus.OK);
    }
}
