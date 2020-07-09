package ru.greendatasoft.app.domain.condition;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestBuilder {

    public static PageRequest getPageRequest(FilterRequest filter) {

        if (filter.getSort() == null || filter.getSort().isEmpty()) {
            return PageRequest.of(filter.getPage(), filter.getSize());
        }

        Sort s = null;

        for (String key : filter.getSort().keySet()) {
            if (s == null) {
                if (filter.getSort().get(key) == SortDirection.ASC) {
                    s = Sort.by(key).ascending();
                } else {
                    s = Sort.by(key).descending();
                }
            } else {
                if (filter.getSort().get(key) == SortDirection.ASC) {
                    s = s.and(Sort.by(key).ascending());
                } else {
                    s = s.and(Sort.by(key).descending());
                }
            }
        }
        
        return PageRequest.of(filter.getPage(), filter.getSize(), s);
    }
}