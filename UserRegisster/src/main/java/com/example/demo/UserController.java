package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins="http://localhost:5173")
public class UserController {

	@Autowired	
	public JwtService jwtService;
	
    @Autowired
    private UserRepo repo;

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> save(@RequestBody User user) {
        repo.save(user);
        return ResponseEntity.ok("Registration successfull!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> AuthEmailAndPass(@RequestBody User user) {
        return service.FindByEmailAndPass(user);
    }
    
    @DeleteMapping("/deleteUser/{id}")
    void deleteUserByID(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        jwtService.validateToken(token.replace("Bearer ", ""));
        service.deleteUserById(id);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
    	try {
    		jwtService.validateToken(token.replace("Bearer ", ""));
    		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Valid");
    	} catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired");
    	}
    }
    
    @GetMapping("/allUsers")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String token) {
        try {
            jwtService.validateToken(token.replace("Bearer ", ""));
            return ResponseEntity.ok(service.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired");
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User user,
            @RequestHeader("Authorization") String token) {

        try {
            jwtService.validateToken(token.replace("Bearer ", ""));
            return ResponseEntity.ok(service.updateUserById(id, user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired");
        }
    }
    @GetMapping("/userPages")
    public Page<User> getByUserPage(int page,int size) {
    	return service.getUsersByPages(page,size);
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile inputFile) {

        Map<String, Object> response = new HashMap<>();

        try {
            service.uploadImages(inputFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        response.put("result", "success");
        response.put("fileName", inputFile.getOriginalFilename());
        
        response.put("message", inputFile);

        return ResponseEntity.ok(response);
    }

}
