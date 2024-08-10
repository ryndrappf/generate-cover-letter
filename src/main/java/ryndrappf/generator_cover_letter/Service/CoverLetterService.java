package ryndrappf.generator_cover_letter.Service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ryndrappf.generator_cover_letter.Parameter.ParameterDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Service
public class CoverLetterService {

    private static final String TEMPLATE_PATH = "/static/template-cover-letter.docx";

    private static final DateTimeFormatter INDONESIAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));


    public byte[] generateCoverLetter(ParameterDTO parameterDTO) throws IOException {
        // Load template from resources
        InputStream templateStream = getClass().getResourceAsStream(TEMPLATE_PATH);
        if (templateStream == null) {
            throw new IOException("Template file not found: " + TEMPLATE_PATH);
        }

        try (XWPFDocument document = new XWPFDocument(templateStream)) {
            // Replace placeholders
            replacePlaceholder(document, "$tanggal", LocalDate.now().format(INDONESIAN_DATE_FORMATTER));
            replacePlaceholder(document, "$namaPerusahaan", parameterDTO.getNamaPerusahaan());
            replacePlaceholder(document, "$posisiPekerjaan", parameterDTO.getPosisiPerusahaan());
            replacePlaceholder(document, "$linkPekerjaan", parameterDTO.getAplikasi());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);

            return outputStream.toByteArray();
        }
    }

    private void replacePlaceholder(XWPFDocument document, String placeholder, String replacement) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            StringBuilder paragraphText = new StringBuilder();
            for (XWPFRun run : paragraph.getRuns()) {
                paragraphText.append(run.getText(0));
            }
            String text = paragraphText.toString();
            if (text.contains(placeholder)) {
                text = text.replace(placeholder, replacement);
                int pos = 0;
                for (XWPFRun run : paragraph.getRuns()) {
                    String runText = run.getText(0);
                    if (runText != null) {
                        int length = runText.length();
                        if (pos + length <= text.length()) {
                            run.setText(text.substring(pos, pos + length), 0);
                            run.setFontFamily("Times New Roman");
                            run.setFontSize(11);
                            pos += length;
                        } else {
                            run.setText("", 0); // Clear remaining runs
                        }
                    }
                }
                if (pos < text.length()) {
                    XWPFRun newRun = paragraph.createRun();
                    newRun.setText(text.substring(pos));
                    newRun.setFontFamily("Times New Roman");
                    newRun.setFontSize(11);
                }
            }
        }
    }
}
