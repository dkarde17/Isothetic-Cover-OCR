import java.util.Arrays;
import java.util.Properties;

/**
 * Created by CNOVA on 4/2/2016.
 */

//This class creates the pgmImage objects
public class PgmImageFactory {

    //method to create PgmImage object and initializing the filePath variable
    PgmImage create(String filePath){
        PgmImage pgmImage = new PgmImage(filePath);

        return pgmImage;
    }


    //method to create a copy of a PgmImage object
    PgmImage createCopy(PgmImage pgmImage, String filePath){
        PgmImage temp = new PgmImage(filePath);

        //copy the type
        temp.imgType = new String(pgmImage.imgType);

        //Copy other parameters: width, height and intensity
        temp.imgWidth = pgmImage.imgWidth;
        temp.imgHeight = pgmImage.imgHeight;
        temp.maxIntensity = pgmImage.maxIntensity;

        temp.imgMatrix = new int[temp.imgHeight][];

        //copy the imgMatrix
        int rowCounter = 0;
        for(int[] array: pgmImage.imgMatrix)
            temp.imgMatrix[rowCounter++] = Arrays.copyOf(array, array.length);

        return temp;
    }

    PgmImage createCopy(PgmImage pgmImage, String filePath, int greyValue){
        PgmImage temp = new PgmImage(filePath);

        //copy the type
        temp.imgType = new String(pgmImage.imgType);

        //Copy other parameters: width, height and intensity
        temp.imgWidth = pgmImage.imgWidth;
        temp.imgHeight = pgmImage.imgHeight;
        temp.maxIntensity = pgmImage.maxIntensity;

        temp.imgMatrix = new int[temp.imgHeight][temp.imgWidth];

        //copy the imgMatrix
        for (int i = 0; i < pgmImage.imgHeight; i++){
            for (int j = 0; j < pgmImage.imgWidth; j++){
                //if pgmImage pixel is white then temp pixel is also white otherwise grey
                temp.imgMatrix[i][j] = pgmImage.imgMatrix[i][j] == 255 ? 255 : greyValue;
            }
        }

        return temp;
    }


    //creates white images
    PgmImage createBlank(String filePath, String imgType, int imgWidth, int imgHeight, int maxIntensity){
        PgmImage temp = new PgmImage(filePath);

        temp.imgType = imgType;
        temp.imgWidth = imgWidth;
        temp.imgHeight = imgWidth;
        temp.maxIntensity = maxIntensity;
        temp.imgMatrix = new int[imgHeight][imgWidth];

        //make every pixel white
        for(int i = 0; i < temp.imgHeight; i++){
            for(int j = 0; j < temp.imgWidth; j++){
                temp.imgMatrix[i][j] = 255;
            }
        }

        return temp;
    }

}
