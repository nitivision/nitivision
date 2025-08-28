package com.niti.vision.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niti.vision.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByOrderByPublishedDateDesc();
    List<Blog> findTop3ByClusterAndIdNotOrderByPublishedDateDesc(String cluster, Long excludeId);

}
