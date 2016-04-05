import sun.rmi.runtime.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 3/29/2016.
 */

// This is the main class to start the whole library
public class IsotheticMain {
    //creating a Logger Object to log the messages
    static Logger LOGGER;
    //properties object to make the code softer and make things easier to change
    static  Properties properties;

    public static void main(String[] args) {

        LOGGER = Logger.getLogger(IsotheticMain.class.getName());
        LOGGER.setLevel(Level.ALL);
        properties = new Properties();
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

        /*Automate this whole procedure */
        //modify this code to become softer. Like ask for path, multiple images, using I/O add them all to Files array and process, use properties file, etc.

        //taking the path of the file from the config.properties file
        String sourceFilePath= properties.getProperty("sourceFilePath");
        LOGGER.info("Read source .pgm file address");

        //creating the pgmImage object from using the factory, as per the properties configured in the property file
        LOGGER.info("Creating new pgmImage object to store information from source pgm file");
        PgmImage sourcePgmImage = PgmImageFactory.create(sourceFilePath);
        LOGGER.info("sourcePgmImage Object Successfully Created");

        //initializing the parameters (type, width, height, max pixel intensity) and the image matrix which contains the pixel intensities
        LOGGER.info("Initializing the sourcePgmImage object");
        ImgReadWrite.init(sourcePgmImage, properties, LOGGER);
        LOGGER.info("sourcePgmImage object successfully initialized");

        //binarize the imgMatrix
        LOGGER.info("binarizing the image in sourcePgmImage");
        ImgReadWrite.binarizeImgMatrix(sourcePgmImage, properties, LOGGER);
        LOGGER.info("binarization completed successfully");

        //create the raw Me matrix (Unit Edge Matrix)
        LOGGER.info("Creating the Unit Edge Matrix");
        int[][] unitEdgeMatrix = IsotheticCoverCalc.createUnitEdgeMatrix(sourcePgmImage, properties);
        LOGGER.info("Unit Edge Matrix successfully created");

        //fill up the unitEdgeMatrix according to the image matrix
        LOGGER.info("Setting up the unit square matrix");
        IsotheticCoverCalc.setUnitEdgeMatrix(unitEdgeMatrix, sourcePgmImage, properties);
        LOGGER.info("Unit edge Matrix successfully set up");

        //create the raw Ms matrix (Unit Square Matrix)
        LOGGER.info("Creating unit square matrix");
        int[][] unitSquareMatrix = IsotheticCoverCalc.createUnitSquareMatrix(sourcePgmImage, properties);
        LOGGER.info("unit square matrix successfully created");

        //fill up the unitSquareMatrix according to the image matrix
        LOGGER.info("Setting up the unit square matrix");
        IsotheticCoverCalc.setUnitSquareMatrix(unitSquareMatrix, unitEdgeMatrix);
        LOGGER.info("unit square matrix successfully created");

        //get the list of the vertices in the Isothetic cover polygon
        LOGGER.info("Creating the list of Isothetic polygon vertices");
        ArrayList<Vertex> isotheticVertices = IsotheticCoverCalc.listVertices(sourcePgmImage, unitSquareMatrix, properties, LOGGER);
        LOGGER.info("List of isothetic polygon vertices successfully created");

        LOGGER.warning("hi");
        //destination file paths
        String destinationImgWithCover = properties.getProperty("destinationImgWithCover");
        LOGGER.info("Read file path to write image back with cover");
        String destinationCover = properties.getProperty("destinationCover");
        LOGGER.info("Read file path to write back the cover only");

        //create a copy of the soucePgmImage to create image-with-cover file
        LOGGER.info("Creating the copy of source pgm image to add cover to it and write the image back with cover");
        PgmImage destinationPgmImageWithCover = PgmImageFactory.createCopy(sourcePgmImage, destinationImgWithCover);
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
        PgmImage destinationPgmImageCover = PgmImageFactory.createBlank(destinationCover, imgType, imgWidth, imgHeight, maxIntensity);
        LOGGER.info("Blank pgmImage object successfully created");

        //write cover to img as well as blank image
        LOGGER.info("Writing cover information to the imageMatrix of copy of source pgmImage object to write back image with cover");
        ImgReadWrite.writeCoverToImgMatrix(destinationPgmImageWithCover, isotheticVertices, LOGGER);
        LOGGER.info("Cover information successfully written to the image matrix of copy of source pgm image object");

        LOGGER.info("Writing cover information to the imageMatrix of blank pgmImage object to write back only cover");
        ImgReadWrite.writeCoverToImgMatrix(destinationPgmImageCover, isotheticVertices, LOGGER);
        LOGGER.info("Cover information successfully written to the image matrix of blank pgm image object");

        //write the pgmImage to .pgm file
        LOGGER.info("writing the data back to .pgm file to generate image with Isothetic cover");
        ImgReadWrite.writeImgToFile(destinationPgmImageWithCover, LOGGER);
        LOGGER.info(".pgm file for image with Isothetic Cover successfully created");

        LOGGER.info("writing the data back to .pgm file to generate only the Isothetic cover");
        ImgReadWrite.writeImgToFile(destinationPgmImageCover, LOGGER);
        LOGGER.info(".pgm file for image with Isothetic Cover successfully created");

        System.out.println(destinationPgmImageCover.path + " successfully created");
        System.out.println(destinationPgmImageWithCover.path + " successfully created");
    }
}
