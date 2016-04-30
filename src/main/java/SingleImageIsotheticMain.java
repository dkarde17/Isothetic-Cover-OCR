import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 3/29/2016.
 */

// This is the main class to start the whole library
public class SingleImageIsotheticMain {

    public static void main(String[] args) {

        //creating a Logger Object to log the messages
        Logger LOGGER = Logger.getLogger(SingleImageIsotheticMain.class.getName());
        LOGGER.setLevel(Level.ALL);
        //properties object to make the code softer and make things easier to change
        Properties properties = new Properties();
        //creating PgmImageFactory object to create pgmImage objects when and as required
        PgmImageFactory pgmImageFactory = new PgmImageFactory();
        //creating ImgReaderWriter object to read and write the image and pgmImage object
        ImgReaderWriter imgReaderWriter = new ImgReaderWriter();
        //creating an object of IsotheticCoverCalc class
        IsotheticCoverCalc isotheticCoverCalc = new IsotheticCoverCalc();
        //creating an object of IsotheticOCR class
        IsotheticOCR isotheticOCR = new IsotheticOCR();

        String configFile = "E:\\learn\\academic\\final year project\\isothetic cover\\programs\\isothetic_cover\\src\\main\\resources\\config.properties";
        //initializing properties object
        try {
            //file from which to read the properties
            FileInputStream propertiesFile = new FileInputStream(configFile);
            properties.load(propertiesFile);
        } catch (FileNotFoundException e) {
            LOGGER.warning("config.properties FILE NOT FOUND: " + configFile);
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.warning("Unable to read from config.properties file: " + configFile);
            e.printStackTrace();
        }

        //taking the path of the file from the config.properties file
        String sourceFilePath= properties.getProperty("sourceFilePath");
        LOGGER.info("Read source .pgm file address");

        File sourePgmFile = new File(sourceFilePath);

        //creating the pgmImage object from using the factory, as per the properties configured in the property file
        LOGGER.info("Creating new pgmImage object to store information from source pgm file");
        PgmImage sourcePgmImage = pgmImageFactory.create(sourePgmFile.getPath());
        sourcePgmImage.sourcePgmFile = sourePgmFile;
        LOGGER.info("sourcePgmImage Object Successfully Created");

        //initializing the parameters (type, width, height, max pixel intensity) and the image matrix which contains the pixel intensities
        LOGGER.info("Initializing the sourcePgmImage object");
        imgReaderWriter.init(sourcePgmImage, properties, LOGGER);
        LOGGER.info("sourcePgmImage object successfully initialized");

        //binarize the imgMatrix
        LOGGER.info("binarizing the image in sourcePgmImage");
        imgReaderWriter.binarizeImgMatrix(sourcePgmImage, properties);
        LOGGER.info("binarization completed successfully");

        //get image dimensions
        LOGGER.info("Getting object dimensions in the image");
        imgReaderWriter.getObjectDimentions(sourcePgmImage);
        LOGGER.info("Object dimensions successfully fetched from the image");

        //centralize the object in the image
        LOGGER.info("Centralizing the object in the image...");
        imgReaderWriter.centralize(sourcePgmImage);
        LOGGER.info("Object successfully centralized");


        //create the raw Me matrix (Unit Edge Matrix)
        LOGGER.info("Creating the Unit Edge Matrix");
        int[][] unitEdgeMatrix = isotheticCoverCalc.createUnitEdgeMatrix(sourcePgmImage, properties);
        LOGGER.info("Unit Edge Matrix successfully created");

        //fill up the unitEdgeMatrix according to the image matrix
        LOGGER.info("Setting up the unit square matrix");
        isotheticCoverCalc.setUnitEdgeMatrix(unitEdgeMatrix, sourcePgmImage, properties);
        LOGGER.info("Unit edge Matrix successfully set up");

        //create the raw Ms matrix (Unit Square Matrix)
        LOGGER.info("Creating unit square matrix");
        int[][] unitSquareMatrix = isotheticCoverCalc.createUnitSquareMatrix(sourcePgmImage, properties);
        LOGGER.info("unit square matrix successfully created");

        //fill up the unitSquareMatrix according to the image matrix
        LOGGER.info("Setting up the unit square matrix");
        isotheticCoverCalc.setUnitSquareMatrix(unitSquareMatrix, unitEdgeMatrix);
        LOGGER.info("unit square matrix successfully created");

        LOGGER.info("Creating iSortedList");
        sourcePgmImage.iSortedList = isotheticCoverCalc.getISortedList(sourcePgmImage, unitSquareMatrix, properties, LOGGER);
        LOGGER.info("iSortedList successfully created");

        LOGGER.info("Creating jSortedList");
        sourcePgmImage.jSortedList = isotheticCoverCalc.getJSortedList(sourcePgmImage.iSortedList, LOGGER);
        LOGGER.info("iSortedList successfully created");

        //get the list of the vertices in the Isothetic cover polygon
        LOGGER.info("Creating the list of Isothetic polygon vertices");
        sourcePgmImage.isotheticVertices = isotheticCoverCalc.listVertices(sourcePgmImage.iSortedList, sourcePgmImage.jSortedList, LOGGER);
        LOGGER.info("List of isothetic polygon vertices successfully created");

        //destination file paths
        String destinationImgWithCover = properties.getProperty("destinationImgWithCover");
        LOGGER.info("Read file path to write image back with cover");
        String destinationCover = properties.getProperty("destinationCover");
        LOGGER.info("Read file path to write back the cover only");

        //create a copy of the soucePgmImage to create image-with-cover file
        LOGGER.info("Creating the copy of source pgm image to add cover to it and write the image back with cover");
        int greyValue = Integer.parseInt(properties.getProperty("greyValue"));
        PgmImage destinationPgmImageWithCover = pgmImageFactory.createCopy(sourcePgmImage, destinationImgWithCover, greyValue);
        LOGGER.info("Copy successfully created");

        //default parameters in the properties file
        String imgType = properties.getProperty("binaryImgType");
        int imgWidth = Integer.parseInt(properties.getProperty("imgWidth"));
        int imgHeight = Integer.parseInt(properties.getProperty("imgHeight"));
        int maxIntensity = Integer.parseInt(properties.getProperty("maxIntensity"));

        //create a blank pgmImage
        LOGGER.info("Creating a blank pgmImage object with the following parameters to write only the cover information in it:");
        System.out.println("Image width = " + imgWidth);
        System.out.println("Image height = " + imgHeight);
        System.out.println("Maximum intensity = " + maxIntensity);
        PgmImage destinationPgmImageCover = pgmImageFactory.createBlank(destinationCover, imgType, imgWidth, imgHeight, maxIntensity);
        LOGGER.info("Blank pgmImage object successfully created");

        //write cover to img as well as blank image
        LOGGER.info("Writing cover information to the imageMatrix of copy of source pgmImage object to write back image with cover");
        imgReaderWriter.writeCoverToImgMatrix(destinationPgmImageWithCover, sourcePgmImage.isotheticVertices, LOGGER);
        LOGGER.info("Cover information successfully written to the image matrix of copy of source pgm image object");

        LOGGER.info("Writing cover information to the imageMatrix of blank pgmImage object to write back only cover");
        imgReaderWriter.writeCoverToImgMatrix(destinationPgmImageCover, sourcePgmImage.isotheticVertices, LOGGER);
        LOGGER.info("Cover information successfully written to the image matrix of blank pgm image object");

        //write the pgmImage to .pgm file
        LOGGER.info("writing the data back to .pgm file to generate image with Isothetic cover");
        imgReaderWriter.writeImgToFile(destinationPgmImageWithCover, LOGGER);
        LOGGER.info(".pgm file for image with Isothetic Cover successfully created");

        LOGGER.info("writing the data back to .pgm file to generate only the Isothetic cover");
        imgReaderWriter.writeImgToFile(destinationPgmImageCover, LOGGER);
        LOGGER.info(".pgm file for image with Isothetic Cover successfully created");

        System.out.println(destinationPgmImageCover.path + " successfully created");
        System.out.println(destinationPgmImageWithCover.path + " successfully created");


        //creating 2D list
        sourcePgmImage.iSorted2DList = isotheticCoverCalc.getISorted2DList(sourcePgmImage);
        sourcePgmImage.jSorted2DList = isotheticCoverCalc.getJSorted2DList(sourcePgmImage);

        /*LOGGER.info("Checking vertical symmetry for : " + sourcePgmImage.sourcePgmFile.getName());
        isotheticOCR.verticalSymmetry(sourcePgmImage, properties, LOGGER);
        LOGGER.info("Vertical symmetry check done for : " + sourcePgmImage.sourcePgmFile.getName());*/

        Test.print2DListcontents(sourcePgmImage.jSorted2DList);

        LOGGER.info("Checking horizontal symmetry for : " + sourcePgmImage.sourcePgmFile.getName());
        isotheticOCR.horizontalSymmetry(sourcePgmImage, properties, LOGGER);
        LOGGER.info("Horizontal symmetry check done for : " + sourcePgmImage.sourcePgmFile.getName());



        /*if (sourcePgmImage.verticalSymmetricity == 1)
            System.out.println(sourcePgmImage.sourcePgmFile.getName() + " is vertically symmetric");
        else
            System.out.println(sourcePgmImage.sourcePgmFile.getName() + " is vertically asymmetric");*/

        if (sourcePgmImage.horizontalSymmetricity == 1)
            System.out.println(sourcePgmImage.sourcePgmFile.getName() + " is horizontally symmetric");
        else
            System.out.println(sourcePgmImage.sourcePgmFile.getName() + " is horizontally asymmetric");
    }
}
