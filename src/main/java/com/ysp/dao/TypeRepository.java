package com.ysp.dao;

import com.ysp.bean.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TypeRepository extends JpaRepository<Type,Long> {


    Type findByName(String name);


    @Query("SELECT t  FROM Type t JOIN t.blogs b where b.published = true group by  t.id")
    List<Type> findTop(Pageable pageable);
}
