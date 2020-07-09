package ru.greendatasoft.app.domain.condition;

import java.util.List;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder {

    public static <T> Specification<T> getSpecification(Predicate predicate) {
        if (predicate == null) {
            return null;
        }
        return getSpecification(predicate, predicate.getPredicates());
    }

    private static <T> Specification<T> getSpecification(Predicate parent, List<Predicate> predicates) {

        Specification<T> spec = getSpecification(parent.getCondition());

        if (parent.getConnectionType() == null || parent.getPredicates() == null
                || parent.getPredicates().isEmpty()) {
            return spec;
        }

        if (predicates != null) {
            for (Predicate p : predicates) {
                if (p == null) {
                    continue;
                }
                Specification sp = getSpecification(p, p.getPredicates());
                if (sp != null) {
                    if (parent.getConnectionType() == ConnectionType.AND) {
                        spec = spec.and(sp);
                    } else {
                        spec = spec.or(sp);
                    }
                }
            }
        }

        return spec;
    }

    private static <T> Specification<T> getSpecification(Condition condition) {
        if (condition == null) {
            return Specification.where(null);
        }

        Object value = condition.getValue();

        return
                (root, cq, cb) -> {

                    Path path = getPath(root, condition.getKey());

                    switch (condition.getLogicalCondition()) {
                        case EQ:
                            return cb.equal(path, value);
                        case NE:
                            return cb.not(cb.equal(path, value));
                        case LT:
                            if (value instanceof Comparable) {
                                Comparable c = (Comparable) value;
                                return cb.lessThan(path, c);
                            } else {
                                throw new IllegalArgumentException(value + " is not comparable");
                            }
                        case LE:
                            if (value instanceof Comparable) {
                                Comparable c = (Comparable) value;
                                return cb.lessThanOrEqualTo(path, c);
                            } else {
                                throw new IllegalArgumentException(value + " is not comparable");
                            }
                        case GT:
                            if (value instanceof Comparable) {
                                Comparable c = (Comparable) value;
                                return cb.greaterThan(path, c);
                            } else {
                                throw new IllegalArgumentException(value + " is not comparable");
                            }
                        case GE:
                            if (value instanceof Comparable) {
                                Comparable c = (Comparable) value;
                                return cb.greaterThanOrEqualTo(path, c);
                            } else {
                                throw new IllegalArgumentException(value + " is not comparable");
                            }
                        case LIKE:
                            if (value instanceof String) {
                                return cb.like(path, (String) value);
                            } else {
                                throw new IllegalArgumentException(value + " is not string");
                            }
                        case NOT_LIKE:
                            if (value instanceof String) {
                                return cb.not(cb.like(path, (String) value));
                            } else {
                                throw new IllegalArgumentException(value + " is not string");
                            }
                        case IN:
                            return path.in(condition.getValues());
                        case IS_NULL:
                            return path.isNull();
                        case IS_NOT_NULL:
                            return path.isNotNull();
                        default:
                            throw new IllegalArgumentException("Unsupported LogicalConditionType "
                                    + condition.getLogicalCondition());
                    }
                };
    }

    private static <T> Path getPath(Root<T> root, String key) {

        if (key == null) {
            return root;
        }

        String[] keys = key.split("\\.");
        if (keys.length == 1) {
            return root.get(key);
        }

        Path path = null;
        for (String k : keys) {
            if (path == null) {
                path = root.get(k);
            } else {
                path = path.get(k);
            }
        }

        return path;
    }
}