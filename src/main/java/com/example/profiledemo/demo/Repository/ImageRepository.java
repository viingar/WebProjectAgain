package com.example.profiledemo.demo.Repository;

import com.example.profiledemo.demo.Model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {

}