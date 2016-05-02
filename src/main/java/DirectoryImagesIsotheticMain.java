import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 4/6/2016.
 */
public class DirectoryImagesIsotheticMain {



    public static void main(String[] args) {

        //creating a Logger Object to log the messages
        Logger LOGGER = Logger.getLogger(DirectoryImagesIsotheticMain.class.getName());
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

        //arraylist to store all the pgmImage objects in a directory
        ArrayList<PgmImage> pgmImages = new ArrayList();

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

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));

        String sourceImgDirectoryPath = properties.getProperty("sourcePgmImgDirectory");
        String destinationImgWithCoverDirectoryPath = properties.getProperty("destinationImgWithCoverDirectory");
        String destinationCoverOnlyDirectoryPath = properties.getProperty("destinationCoverOnlyDirectory");

        LOGGER.info("Read source directory path");

        File sourceImgDirectory = new File(sourceImgDirectoryPath);
        File destinationImgWithCoverDirectory = new File(destinationImgWithCoverDirectoryPath);
        File destinationCoverOnlyDirectory = new File(destinationCoverOnlyDirectoryPath);
        if(!sourceImgDirectory.isDirectory()){
            LOGGER.warning("Not a directory : " + sourceImgDirectoryPath);
            LOGGER.warning("Exiting");
            System.exit(-1);
        }
        else if(!destinationImgWithCoverDirectory.isDirectory()){
            LOGGER.warning("Not a directory : " + destinationImgWithCoverDirectoryPath);
            LOGGER.warning("Exiting");
            System.exit(-1);
        }
        else if(!destinationCoverOnlyDirectory.isDirectory()){
            LOGGER.warning("Not a directory : " + destinationCoverOnlyDirectoryPath);
            LOGGER.warning("Exiting");
            System.exit(-1);
        }

        LOGGER.info("Getting the file list in " + sourceImgDirectory.getPath());
        String[] sourcePgmFileList = sourceImgDirectory.list();

        //Checking if the directory is empty
        if (sourcePgmFileList.length == 0){
            LOGGER.warning("No files in " + sourceImgDirectory.getPath());
            LOGGER.warning("Exiting");
            System.exit(-1);
        }

        //for each file in the folder
        for(String fileName: sourcePgmFileList) {
            File sourcePgmFile = new File(sourceImgDirectory.getPath() + File.separatorChar + fileName);

            //Checking whether the file is a .pgm file or not
            if (!fileName.substring(fileName.indexOf('.') + 1).equals("pgm")) {
                LOGGER.warning("Not a pgm file : " + fileName);
                LOGGER.info("Moving to next file");
                continue;
            }

            //creating the pgmImage object from using the factory, as per the properties configured in the property file
            LOGGER.info("Creating new pgmImage object to store information from source pgm file");
            PgmImage temp = pgmImageFactory.create(sourcePgmFile.getPath());
            temp.sourcePgmFile = sourcePgmFile;
            pgmImages.add(temp);
            LOGGER.info("sourcePgmImage Object Successfully Created");

        }


        for (PgmImage sourcePgmImage : pgmImages) {
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
            String destinationImgWithCover = destinationImgWithCoverDirectoryPath + File.separatorChar + sourcePgmImage.sourcePgmFile.getName().substring(0, sourcePgmImage.sourcePgmFile.getName().indexOf('.')) + "_withCover_" + gridSize + ".pgm";
            LOGGER.info("Read file path to write image back with cover");
            String destinationCover = destinationCoverOnlyDirectoryPath + File.separatorChar + sourcePgmImage.sourcePgmFile.getName().substring(0, sourcePgmImage.sourcePgmFile.getName().indexOf('.')) + "_Cover_" + gridSize + ".pgm";
            LOGGER.info("Read file path to write back the cover only");

            //create a light copy (image is lighter like grey, instead of black)copy of the soucePgmImage to create image-with-cover file
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

            LOGGER.info("Checking Symmetry score");

            //creating 2D lists
            sourcePgmImage.iSorted2DList = isotheticCoverCalc.getISorted2DList(sourcePgmImage);
            sourcePgmImage.jSorted2DList = isotheticCoverCalc.getJSorted2DList(sourcePgmImage);

            LOGGER.info("Checking vertical symmetry for : " + sourcePgmImage.sourcePgmFile.getName());
            isotheticOCR.verticalSymmetry(sourcePgmImage, properties, LOGGER);
            LOGGER.info("Vertical symmetry check done for : " + sourcePgmImage.sourcePgmFile.getName());

            LOGGER.info("Checking horizontal symmetry for : " + sourcePgmImage.sourcePgmFile.getName());
            isotheticOCR.horizontalSymmetry(sourcePgmImage, properties, LOGGER);
            LOGGER.info("Horizontal symmetry check done for : " + sourcePgmImage.sourcePgmFile.getName());
        }

        //print symmetry status
        for (PgmImage pgmImage : pgmImages){
            System.out.printf(pgmImage.sourcePgmFile.getName() + " is " + ((pgmImage.verticalSymmetry == 1)?"vertically symmetric": "vertically assymetric") + " - " + pgmImage.verticalMaxSkipStreak + " | " + pgmImage.verticalTotalHeadSkip + "\t" + pgmImage.verticalTotalTailSkip + "\t" + pgmImage.isotheticVertices.size() + "\t" + pgmImage.verticalSymmetryProbability + "\t" + pgmImage.verticalAsymmetryProbability + "\n");
            System.out.printf(pgmImage.sourcePgmFile.getName() + " is " + ((pgmImage.horizontalSymmetry == 1)?"horizontally symmetric": "horizontally assymetric") + " - " + pgmImage.horizontalMaxSkipStreak + " | " + pgmImage.horizontalTotalHeadSkip + "\t" + pgmImage.horizontalTotalTailSkip + "\t" + pgmImage.isotheticVertices.size() + "\t" + pgmImage.horizontalSymmetryProbability + "\t" + pgmImage.horizontalAsymmetryProbability + "\n");
        }
    }
}
