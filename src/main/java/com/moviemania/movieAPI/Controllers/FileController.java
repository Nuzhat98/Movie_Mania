package com.moviemania.movieAPI.Controllers;

import com.moviemania.movieAPI.Service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
public class FileController {

    private final FileService fileService;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart(name= "file") MultipartFile file) throws IOException{
        String uploadedFileName= fileService.uploadFile(path,file);
        return ResponseEntity.ok("File Uploaded:" + uploadedFileName);
    }

    @GetMapping("/{fileName}")
    public void  serveFileHandler(@PathVariable(name = "fileName") String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile= fileService.getResourceFile(path, fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resourceFile,response.getOutputStream());
    }
}
