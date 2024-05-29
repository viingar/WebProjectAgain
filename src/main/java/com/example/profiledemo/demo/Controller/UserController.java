package com.example.profiledemo.demo.Controller;

import com.example.profiledemo.demo.DTO.UserDTO;
import com.example.profiledemo.demo.Model.Image;
import com.example.profiledemo.demo.Service.CustomUserDetail;
import com.example.profiledemo.demo.Service.ImageService;
import com.example.profiledemo.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Controller
@ComponentScan
public class UserController {

    @Autowired
    private ImageService imageService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        return "home";
    }
    @GetMapping("/works")
    public String works(Model model, Principal principal) {

        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        List<Image> imageList = imageService.viewAll();
        model.addAttribute("imageList", imageList);

        return "works";
    }

    @GetMapping("/upload")
    public ModelAndView addWorks(Model model, Principal principal){
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        return new ModelAndView("upload");
    }
    @PostMapping("/upload")
    public String addWorksPost(HttpServletRequest request, @RequestParam("image") MultipartFile file, @RequestParam("text") String text, Principal principal)
            throws IOException, SerialException, SQLException
    {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setImage(blob);
        image.setText(text);

        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        String fullName = ((CustomUserDetail) userDetails).getFullName(); // Получаем полное имя пользователя

        image.setUploaderFullName(fullName);

        imageService.create(image);
        return "redirect:/works" ;
    }
    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws IOException, SQLException
    {
        Image image = imageService.viewById(id);
        byte [] imageBytes = null;
        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @GetMapping("/register")
    public String getRegistrationPage(@ModelAttribute("user") UserDTO userDto) {
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute("user") UserDTO userDto, Model model) {
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfuly!");
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("user-page")
    public String userPage (Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);


        return "user";
    }

    @GetMapping("admin-page")
    public String adminPage (Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "admin";
    }

}