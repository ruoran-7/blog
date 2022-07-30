package com.ysp.dao;

import com.ysp.bean.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag findByName(String name);

    @Query("select t  from Tag t join t.blogs b where b.published = true group by  t.id")
    List<Tag> findTop(Pageable pageable);
}
