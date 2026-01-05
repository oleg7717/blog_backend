package ru.goncharenko.blog.post.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.goncharenko.blog.exception.ResourceNotFoundException;
import ru.goncharenko.blog.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FilesService {
	public static final String UPLOAD_DIR = "uploads"  + File.separator;

	public void upload(long postId, MultipartFile file) {
		try {
			Path uploadDir = Paths.get(UPLOAD_DIR + postId);
			if (!Files.exists(uploadDir)) {
				Files.createDirectories(uploadDir);
			}

			// Сохраняем файл
			String extension = FileUtils.getExtension(file);
			Path filePath = uploadDir.resolve("post" + postId + "_image." + extension);
			file.transferTo(filePath);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public Resource download(long postId) {
		try {
			Path uploadDir = Paths.get(UPLOAD_DIR + postId);
			return FileUtils.findFile(uploadDir, postId);
		} catch (IOException e) {
			throw new ResourceNotFoundException("Image file for post not fount");
		}
	}
}
