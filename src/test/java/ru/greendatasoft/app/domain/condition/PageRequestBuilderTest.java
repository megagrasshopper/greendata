package ru.greendatasoft.app.domain.condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class PageRequestBuilderTest {

    @Test
    void getPageRequest() {
        FilterRequest filter = new FilterRequest().setPage(0).setSize(10);

        PageRequest pageRequest = PageRequestBuilder.getPageRequest(filter);
        assertNotNull(pageRequest);
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertNotNull(pageRequest.getSort());
        assertEquals(Sort.unsorted(), pageRequest.getSort());


        LinkedHashMap<String, SortDirection> sort = new LinkedHashMap<>();
        sort.put("field1", SortDirection.ASC);
        sort.put("field2", SortDirection.DESC);

        filter.setSort(sort);

        pageRequest = PageRequestBuilder.getPageRequest(filter);
        assertNotNull(pageRequest);
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertNotNull(pageRequest.getSort());
        assertEquals("field1: ASC,field2: DESC", pageRequest.getSort().toString());
    }

    @Test
    void t() throws JsonProcessingException {

        String json="{\n"
                + "  \"page\": 0,\n"
                + "  \"predicate\": {\n"
                + "    \"condition\": {\n"
                + "      \"key\": \"percent\",\n"
                + "      \"logicalCondition\": \"GT\",\n"
                + "      \"value\": 5,\n"
                + "      \"values\": [\n"
                + "      ]\n"
                + "    },\n"
                + "    \"connectionType\": \"AND\",\n"
                + "    \"predicates\": [\n"
                + "{\n"
                + "\n"
                + "   \"condition\": {\n"
                + "      \"key\": \"bank.name\",\n"
                + "      \"logicalCondition\": \"IN\",\n"
                + "      \"values\": [\"bank1\", \"bank2\", \"bank3\"]\n"
                + "}, \"connectionType\": \"OR\",\n"
                + "\"predicates\":[{\n"
                + "\n"
                + "   \"condition\": {\n"
                + "      \"key\": \"cusomer.name\",\n"
                + "      \"logicalCondition\": \"LIKE\",\n"
                + "\"value\":\"customer%\"\n"
                + "},\n"
                + "\"predicates\":[]\n"
                + "}\n"
                + "]\n"
                + "    }\n"
                + "\n"
                + "    ]\n"
                + "  },\n"
                + "  \"size\": 10,\n"
                + "  \"sort\": {\"percent\": \"ASC\", \"id\": \"DESC\"}\n"
                + "}";


        FilterRequest filters = new ObjectMapper().readValue(json, FilterRequest.class);
        System.out.println(filters);
    }
}