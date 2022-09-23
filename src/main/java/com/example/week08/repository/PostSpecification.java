package com.example.week08.repository;

import com.example.week08.domain.Post;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostSpecification {

    public static Specification<Post> searchPost(Map<String, Object> searchKey) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String key : searchKey.keySet()) {
                predicates.add(criteriaBuilder.equal(root.get(key), searchKey.get(key)));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
