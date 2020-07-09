package ru.greendatasoft.app.domain.condition;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.greendatasoft.app.domain.entity.Deposit;

@ExtendWith(MockitoExtension.class)
class SpecificationBuilderTest {

    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery criteriaQuery;
    @Mock
    private Root<Deposit> root;


    @DisplayName("Test null predicate")
    @Test
    void getSpecification() {
        assertNull(SpecificationBuilder.getSpecification(null));
    }

    @DisplayName("Test complex predicate")
    @Test
    void getSpecification1() {

        Condition c = new Condition()
                .setKey("id")
                .setLogicalCondition(LogicalConditionType.GT)
                .setValue(10);

        Predicate p = new Predicate().setCondition(c);

        Specification<Deposit> specification = SpecificationBuilder.getSpecification(p);

        assertNotNull(specification);

        Path path = mock(Path.class);
        when(root.get("id")).thenReturn(path);

        javax.persistence.criteria.Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(root, times(1)).get("id");
        verifyNoMoreInteractions(root);
        verify(criteriaBuilder, times(1)).greaterThan(path, 10);
        verifyNoMoreInteractions(criteriaBuilder);
    }
}