package com.ysp.dao;

import com.ysp.bean.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true and b.published = true ")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where  (b.title like ?1 or b.content like ?1) and b.published = true ")
    Page<Blog> findByQuery(String query,Pageable pageable);


    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b where b.published = true group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 and b.published = true ")
    List<Blog> findByYear(String year);


    @Override
    @Query("select b from Blog b where  b.published = true")
    Page<Blog> findAll(Pageable pageable);

    @Override
    @Query("select count(b) from Blog b where  b.published = true")
    long count();

    @Override
    @Query("select b from Blog b")
    List<Blog> findAll();

}
