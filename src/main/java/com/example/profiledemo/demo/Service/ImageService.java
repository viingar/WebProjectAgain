package com.example.profiledemo.demo.Service;


import com.example.profiledemo.demo.Model.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    public Image create(Image image);
    public List<Image> viewAll();
    public Image viewById(long id);


}