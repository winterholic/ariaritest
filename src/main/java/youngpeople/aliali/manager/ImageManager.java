package youngpeople.aliali.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class ImageManager {

    @Value("${imageFile.uploadDir}")
    private String uploadDir;

    //private static final String UPLOAD_DIR = "C:/Users/대선/Desktop/image test file/";

    @Value("${imageFile.savePath}")
    private String savePath;

    public String imageSave(MultipartFile file) {
        if (file.isEmpty()) {
            return ""; // 기본 이미지 몇개의 주소 중에 return
        }
        try {
            // 원본 파일명 가져오기
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

            // 파일명 유니크하게 만들기
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName = timeStamp + "_" + originalFileName;

            // 파일 저장 경로
            Path uploadPath = Paths.get(uploadDir + fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

            // 저장된 파일의 URL 반환
            return savePath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }

}
