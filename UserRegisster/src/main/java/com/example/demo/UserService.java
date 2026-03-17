package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserService {
	@Autowired
	public UserRepo repo;
	
	@Autowired
	private JwtService jwtService;
	
	public User saveNewUser(User user) {
		return repo.save(user);
	}
	public ResponseEntity<?> FindByEmailAndPass(User user) {
	    User user2 = repo.findByEmailAndPassword(
	        user.getEmail(), user.getPassword()
	    );

	    if (user2 == null) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid");
	    }

	    String token = jwtService.generateToken(user2.getEmail());

	    Map<String, Object> response = new HashMap<>();
	    response.put("token", token);
	    response.put("user", user2);

	    return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	public void deleteUserById(Long id) {
		repo.deleteById(id);
	}
	public List<User> getAllUsers() {
		return repo.findAll();
	}
	public User updateUserById(Long id, User updatedUser) {
	    Optional<User> optionalUser = repo.findById(id);

	    if(optionalUser.isEmpty()) {
	        throw new RuntimeException("User not found with id " + id);
	    }

	    User existingUser = optionalUser.get();

	    existingUser.setUsername(updatedUser.getUsername());
	    existingUser.setEmail(updatedUser.getEmail());
	    existingUser.setPassword(updatedUser.getPassword());

	    return repo.save(existingUser);
	}
	
	public Page<User> getUsersByPages(int page,int size) {
		Pageable pageable=PageRequest.of(page,size);
		return repo.findAll(pageable);
	}
	
	@Value("${file.uploads.images.path}")
	public String ALLOWED_PATH;
	
	public long ALLOWED_MAX_SIZE=5*1024*1024;
	
	public void uploadImages(MultipartFile inputFile) throws IOException {
		
		String fileName=StringUtils.cleanPath(inputFile.getOriginalFilename());
		String fileType=StringUtils.getFilenameExtension(fileName);
		System.out.println(fileName+" "+fileType);
		
		String[] isAllowedTypes= {"jpg","jpeg","png","pdf","gif"};
		boolean isAllowedFile=Arrays.stream(isAllowedTypes).anyMatch(fileType::equals);
		System.out.println(isAllowedFile);
		
		if(isAllowedFile==false) {
			throw new RuntimeException(fileType+" File type not allowed!");
		}
		
		System.out.println(inputFile.getSize());
		
		if(inputFile.getSize()>ALLOWED_MAX_SIZE) {
			throw new IllegalArgumentException("Maximum allowed file size is 5MB!");
		}
			
			String newFileName=UUID.randomUUID().toString()+"."+fileType;
			Path uploadPath=Paths.get(ALLOWED_PATH+newFileName);
			System.out.println(uploadPath);
			Files.copy(inputFile.getInputStream(),uploadPath);
		}
		
	}