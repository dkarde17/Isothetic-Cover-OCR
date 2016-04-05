import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by CNOVA on 3/29/2016.
 */

// This is the main class to start the whole library
public class IsotheticMain {
    //properties object to make the code softer and make things easier to change
    static  Properties properties = new Properties();
    public static void main(String[] args) {

        //initializing properties object
        try {
            //file from which to read the properties
            FileInputStream propertiesFile = new FileInputStream("E:\\learn\\academic\\final year project\\isothetic cover\\programs\\isothetic_cover\\src\\main\\resources\\config.properties");
            properties.load(propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Automate this whole procedure */
        //modify this code to become softer. Like ask for path, multiple images, using I/O add them all to Files array and process, use properties file, etc.

        //taking the path of the file from the config.properties file
        String sourceFilePath= properties.getProperty("sourceFilePath");

        //creating the pgmImage object from using the factory, as per the properties configured in the property file
        PgmImage sourcePgmImage = PgmImageFactory.create(sourceFilePath);

        //initializing the parameters (type, width, height, max pixel intensity) and the image matrix which contains the pixel intensities
        ImgReadWrite.init(sourcePgmImage, properties);

        //binarize the imgMatrix
        ImgReadWrite.binarizeImgMatrix(sourcePgmImage, properties);

        //create the raw Me matrix (Unit Edge Matrix)
        int[][] unitEdgeMatrix = IsotheticCoverCalc.createUnitEdgeMatrix(sourcePgmImage, properties);

        //fill up the unitEdgeMatrix according to the image matrix
        IsotheticCoverCalc.setUnitEdgeMatrix(unitEdgeMatrix, sourcePgmImage, properties);

        Test.printMatrix(unitEdgeMatrix);

        //create the raw Ms matrix (Unit Square Matrix)
        int[][] unitSquareMatrix = IsotheticCoverCalc.createUnitSquareMatrix(sourcePgmImage, properties);

        //fill up the unitSquareMatrix according to the image matrix
        IsotheticCoverCalc.setUnitSquareMatrix(unitSquareMatrix, unitEdgeMatrix);

        Test.printMatrix(unitSquareMatrix);

        //get the list of the vertices in the Isothetic cover polygon
        ArrayList<Vertex> isotheticVertices = IsotheticCoverCalc.listVertices(sourcePgmImage, unitSquareMatrix, properties);

        Test.printListContents(isotheticVertices);

        //destination file paths
        String destinationImgWithCover = properties.getProperty("destinationImgWithCover");
        String destinationCover = properties.getProperty("destinationCover");

        //create a copy of the soucePgmImage to create image-with-cover file
        PgmImage destinationPgmImageWithCover = PgmImageFactory.createCopy(sourcePgmImage, destinationImgWithCover);

        //default parameters in the properties file
        String imgType = properties.getProperty("binaryImgType");
        int imgWidth = Integer.parseInt(properties.getProperty("imgWidth"));
        int imgHeight = Integer.parseInt(properties.getProperty("imgHeight"));
        int maxIntensity = Integer.parseInt(properties.getProperty("maxIntensity"));

        //create a blank pgmImage
        PgmImage destinationPgmImageCover = PgmImageFactory.createBlank(destinationCover, imgType, imgWidth, imgHeight, maxIntensity);

        //write cover to img as well as blank image
        ImgReadWrite.writeCoverToImgMatrix(destinationPgmImageWithCover, isotheticVertices);
        ImgReadWrite.writeCoverToImgMatrix(destinationPgmImageCover, isotheticVertices);

        //write the pgmImage to .pgm file
        ImgReadWrite.writeImgToFile(destinationPgmImageWithCover);
        ImgReadWrite.writeImgToFile(destinationPgmImageCover);
    }
}
