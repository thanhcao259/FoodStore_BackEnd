package com.example.store.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationAndSortingUtils {
    public static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir){
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        return PageRequest.of(pageNo,pageSize,sort);
    }
}
