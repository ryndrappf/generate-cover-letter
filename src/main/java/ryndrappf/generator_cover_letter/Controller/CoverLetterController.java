package ryndrappf.generator_cover_letter.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ryndrappf.generator_cover_letter.Parameter.ParameterDTO;
import ryndrappf.generator_cover_letter.Service.CoverLetterService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;

    @PostMapping(value ="/generate-cover-letter", headers = "Content-Type= multipart/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void generateCoverLetter(@ModelAttribute ParameterDTO parameterDTO, HttpServletResponse response) throws IOException {
        byte[] coverLetter = coverLetterService.generateCoverLetter(parameterDTO);

        String namaFile = "cover-letter-" + parameterDTO.getNamaPerusahaan() + ".docx";
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + namaFile + "\"");
        response.getOutputStream().write(coverLetter);
//        response.getOutputStream().flush();
//        response.getOutputStream().close();
    }
}
