package by.niruin.techprocessSystem.domain.service;

import by.niruin.techprocessSystem.exception.ImageSizeOutOfBoundsException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.io.File;

import static by.niruin.techprocessSystem.constant.AlertInfoMessage.INCORRECT_FILE_SIZE_MESSAGE;
import static by.niruin.techprocessSystem.constant.SceneTitle.IMAGE_FILE_CHOOSER_TITLE;

@Service
public class FileChooserService {
    private static final String IMAGE_FILTER = "Изображения";
    private static final String PNG_FILTER = "*.png";
    private static final String JPG_FILTER = "*.jpg";
    private static final String JPEG_FILTER = "*.jpeg";
    private static final int BYTES_IN_MEGABYTES = 1_048_576;

    public File findImage(Stage currentStage, long maxImageSizeInMegabytes) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle(IMAGE_FILE_CHOOSER_TITLE);
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter(IMAGE_FILTER, PNG_FILTER, JPG_FILTER, JPEG_FILTER));
        var selectedFile = fileChooser.showOpenDialog(currentStage);

        if (selectedFile != null) {
            var fileSizeInMb = selectedFile.length() / BYTES_IN_MEGABYTES;

            if (fileSizeInMb > 3) {
                throw new ImageSizeOutOfBoundsException(
                        INCORRECT_FILE_SIZE_MESSAGE.formatted(selectedFile.getName(), maxImageSizeInMegabytes));
            }
        }

        return selectedFile;
    }
}
