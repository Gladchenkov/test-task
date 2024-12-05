package com.example.demo.repository;

import com.example.demo.model.Timestamp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimestampRepository extends CrudRepository<Timestamp, Long>, PagingAndSortingRepository<Timestamp, Long> {
    @Override
    List<Timestamp> findAll();
}
