import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 3/31/2016.
 */
//class to read and write from the image file (original .pgm file)
public class ImgReaderWriter {

    //method to intialize the parameters in the pgmImage object e.g. imgWidth, imgHeight, etc. from the .pgm source file
    void init(PgmImage pgmImage, Properties properties, Logger LOGGER){
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        try {
            fileInputStream = new FileInputStream(pgmImage.path);
            dataInputStream = new DataInputStream(fileInputStream);

            //reading the type of the image
            pgmImage.imgType = dataInputStream.readLine();
            System.out.println("imgType = " + pgmImage.imgType);
            int parameterCount = 0; //should not be greater than 3 i.e. height, width and maximum Intensity

            // to parse the header: drop the comments and to find out the width, height and maximum Intensity
            LOGGER.info("Finding image width, height and maximum Intensity");
            while(pgmImage.imgWidth==0 || pgmImage.imgHeight==0 || pgmImage.maxIntensity == 0){
                    String line = dataInputStream.readLine();
                    int comIndex = line.indexOf('#'); //index of the starting of the comment

                    //if line is a comment or line is just a new line character i.e. line is empty, drop the line
                    if(comIndex == 0 || line.length() == 0){
                        continue;
                    }

                    //if comment starts from somewhere in the middle of the line or there is no comment in the line and the line is not empty
                    else if(comIndex > 0 || (comIndex == -1 && line.length() != 0)){

                        /*if the comment starts somewhere in the middle of the line
                          then drop part from where the comment starts (i.e. after the # is encountered) and take the string before that
                         */
                        if(comIndex > 0)
                            line = line.substring(0, comIndex-1);

                        //remove any leading or trading white spaces
                        line = line.trim();

                        //finding width, height and max intensity in the lines
                        for(String x : line.split(" ")){

                            //switch case to keep track of the order in which the parameters are being set (width, height, maximum intensity)
                            switch (parameterCount){

                                //setting width
                                case 0: pgmImage.imgWidth = Integer.parseInt(x);
                                    parameterCount++;
                                    break;

                                //setting height
                                case 1: pgmImage.imgHeight = Integer.parseInt(x);
                                    parameterCount++;
                                    break;

                                //setting maximum intensity
                                case 2: pgmImage.maxIntensity = Integer.parseInt(x);
                                    parameterCount++;
                                    break;

                                //if there are more integers than the number of parameters i.e. 3
                                default:System.out.println("Invalid File");
                                    return;
                            }
                        }
                    }
                }

            //matching the img dimensions with the dimensions specified in the properties file, if not then abort
            if(pgmImage.imgWidth != Integer.parseInt(properties.getProperty("imgWidth")) || pgmImage.imgHeight != Integer.parseInt(properties.getProperty("imgHeight")) || pgmImage.maxIntensity != Integer.parseInt(properties.getProperty("maxIntensity"))){
                LOGGER.warning("Invalid Image: One or more parameters found in the image header are not consistent with those specified in the config.properties file");
                LOGGER.warning("Exiting");
                System.exit(-1);
            }

            System.out.println("Image width = " + pgmImage.imgWidth);
            System.out.println("Image height = " + pgmImage.imgHeight);
            System.out.println("Image maximum intensity = " + pgmImage.maxIntensity);

            //initializing the imgMatrix
            pgmImage.imgMatrix = new int[pgmImage.imgHeight][pgmImage.imgWidth];
            //in pgm images we can not have anything after the maximum intensity except for a white space (generally "end of the line"), after that the image intensity matrix starts
            int pixel = 0;

            LOGGER.info("Initializing pgmImage.imgMatrix: reading from source .pgm file");
            //for each row of the imgMatrix
            for(int row = 0; row < pgmImage.imgHeight;  row++){
                //in each column of the imgMatrix
                for(int col = 0; col < pgmImage.imgWidth; col++){
                    //if there is data available in the data input stream
                    if(dataInputStream.available() != 0){
                        //read the unsigned integer (0-255) written as binary (plain) byte in the file
                        pixel = dataInputStream.readUnsignedByte();
                        //and if it is lighter then the threshold value then set the corresponding matrix position white(255) otherwise 0
                        pgmImage.imgMatrix[row][col] = pixel;
                    }

                    //if the data stream ended unexpectedly or doesn't have enough pixel intensities according to the width and the height
                    else {
                        LOGGER.warning("Invalid Image : the no. of pixel intensities given are not consistent with the size specified");
                        LOGGER.warning("Exiting");
                        System.exit(-1);
                    }
                }
            }
            LOGGER.info("Successfully initialized the image matrix");
        } catch (FileNotFoundException e) {
            LOGGER.warning("Source .pgm file not found");
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.warning("Unable to read from source .pgm File");
            e.printStackTrace();
        } finally {
            try {
                LOGGER.info("Closing the source .pgm file dataInputStream and fileInputStream");
                dataInputStream.close();
                LOGGER.info("Successfully closed the streams");
            } catch (IOException e) {
                LOGGER.warning("Unable to close the streams");
                e.printStackTrace();
            }
        }
    }


    //method to binarize the imgMatrix in the pgmImage object, i.e. if pixels are lighter than a threshold, make white (255) otherwise black (0)
    void binarizeImgMatrix(PgmImage pgmImage, Properties properties){
        //reading the threshold intensity from the config.properties file
        int threshold = Integer.parseInt(properties.getProperty("pixelThreshold"));
        System.out.println("Threshold value = " + threshold);
        for (int i = 0; i < pgmImage.imgHeight; i++){
            for(int j = 0; j < pgmImage.imgWidth; j++){
                pgmImage.imgMatrix[i][j] = (pgmImage.imgMatrix[i][j] > threshold) ? 255 : 0;
            }
        }
    }

    //sets the imin, imax, jmin and jmax variables in the PgmImage object to the corresponding object position values
    void getObjectDimentions(PgmImage pgmImage){

        //to keep track of first non white pixel
        int flag = 0;

        for(int i = 0; i < pgmImage.imgHeight; i++){
            for(int j = 0; j < pgmImage.imgWidth; j++){
                if(pgmImage.imgMatrix[i][j] != 255){

                    //if first non white pixel
                    if(flag == 0){
                        pgmImage.imin = i;
                        pgmImage.jmin = j;
                        pgmImage.imax = i;
                        pgmImage.jmax = j;
                        flag++;
                    }
                    else {
                        if (i < pgmImage.imin)
                            pgmImage.imin = i;
                        if (j < pgmImage.jmin)
                            pgmImage.jmin = j;
                        if (i > pgmImage.imax)
                            pgmImage.imax = i;
                        if (j > pgmImage.jmax)
                            pgmImage.jmax = j;
                    }
                }
            }
        }
    }

    //method to centralize the object in the image
    void centralize(PgmImage pgmImage){
        int objectHeight = 0;
        int objectWidth = 0;
        objectHeight = pgmImage.imax - pgmImage.imin + 1;
        objectWidth = pgmImage.jmax - pgmImage.jmin + 1;

        //variables to keep track of starting i and j of new pgmImage object
        int iStart, jStart;

        /*if((pgmImage.imgHeight - objectHeight)%2 == 0) {
            iStart = (pgmImage.imgHeight - objectHeight) / 2 - 1;
        }
        else {
            iStart = (pgmImage.imgHeight - objectHeight) / 2;
        }
        if((pgmImage.imgWidth - objectWidth)%2 == 0){
            jStart = (pgmImage.imgWidth - objectWidth) / 2 - 1;
        }
        else {
            jStart = (pgmImage.imgWidth - objectWidth) / 2;
        }*/

        iStart = (pgmImage.imgHeight - objectHeight) / 2;
        jStart = (pgmImage.imgWidth - objectWidth) / 2;

        int[][] temp = new int[pgmImage.imgHeight][pgmImage.imgWidth];

        //make the image white
        for(int[] array : temp){
            Arrays.fill(array, 255);
        }

        for(int i = pgmImage.imin, iNew = iStart; i <= pgmImage.imax; i++, iNew++){
            for(int j = pgmImage.jmin, jNew = jStart; j <= pgmImage.jmax; j++, jNew++){
                temp[iNew][jNew] = pgmImage.imgMatrix[i][j];
            }
        }

        pgmImage.imgMatrix = temp;
        pgmImage.imin = iStart;
        pgmImage.imax = iStart + objectHeight - 1;
        pgmImage.jmin = jStart;
        pgmImage.jmax = jStart + objectWidth - 1;

    }


    //method to write the cover to the imgMatrix using the list of Isothetic Polygon vertices
    void writeCoverToImgMatrix(PgmImage pgmImage, ArrayList<Vertex> vertices, Logger LOGGER){
        if (vertices.isEmpty() == false) {
            Iterator iterator = vertices.iterator();
            Vertex start = (Vertex) iterator.next();
            Vertex vertex = start;
            Vertex nextVertex = null;

            while (nextVertex != start) {

                if (iterator.hasNext()) {
                    nextVertex = (Vertex) iterator.next();
                    writeEdgetoImgMatrix(pgmImage, vertex, nextVertex);
                    vertex = nextVertex;
                } else {
                    nextVertex = start;
                    writeEdgetoImgMatrix(pgmImage, vertex, nextVertex);
                }
            }
        }
        else {
            LOGGER.warning("Vertex list for the Isothetic Polygon is empty");
            System.exit(-1);
        }
    }

    //method to write one edge of the Isothetic polygon from vertex1<iFrom, jFrom> to vertex2 <iTo, jTo> into the imgMatrix of pgmImage
    private void writeEdgetoImgMatrix(PgmImage pgmImage, Vertex vertex, Vertex nextVertex){
        /*vertices' co-ordinates, saving separately because we may need to decrease any one of them because vertex may line on the max edges
        of the image and vertex could come out like <400, 0> and 400 index does not exist so we may need to decrement it.
         */
        int iFrom = vertex.i;
        int jFrom = vertex.j;
        int iTo = nextVertex.i;
        int jTo = nextVertex.j;

        if (iFrom == pgmImage.imgHeight)
            iFrom--;
        if (jFrom == pgmImage.imgWidth)
            jFrom--;
        if (iTo == pgmImage.imgHeight)
            iTo--;
        if (jTo == pgmImage.imgWidth)
            jTo--;

        if(iFrom > iTo){
            int temp = iFrom;
            iFrom = iTo;
            iTo = temp;
        }
        if(jFrom > jTo){
            int temp = jFrom;
            jFrom = jTo;
            jTo = temp;
        }
        for (int i = iFrom; i <= iTo; i++){
            for (int j = jFrom; j <= jTo; j++){
                pgmImage.imgMatrix[i][j] = 0;
            }
        }
    }

    //write pgmImg object to a .pgm file
    void writeImgToFile(PgmImage pgmImage, Logger LOGGER){
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pgmImage.path);
            dataOutputStream = new DataOutputStream(fileOutputStream);

            LOGGER.info("Writing the pgmImage object parameters i.e. header to .pgm file");
            dataOutputStream.writeBytes(pgmImage.imgType + "\n");
            dataOutputStream.writeBytes("# Created for Final Year Project, group 09 - 2016, CSE, NIT SIlchar" + "\n");
            dataOutputStream.writeBytes(String.valueOf(pgmImage.imgWidth) + " " + String.valueOf(pgmImage.imgHeight) + "\n");
            dataOutputStream.writeBytes(String.valueOf(pgmImage.maxIntensity) + "\n");

            LOGGER.info("Writing image pixel intensities from pgmImage object to .pgm file");
            for (int i = 0; i < pgmImage.imgHeight; i++){
                for (int j = 0; j < pgmImage.imgWidth; j++){
                    dataOutputStream.writeByte(pgmImage.imgMatrix[i][j]);
                }
            }

        } catch (FileNotFoundException e) {
            LOGGER.warning("Destination .pgm file not found");
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.warning("Unable to write in the destination .pgm file");
            e.printStackTrace();
        } finally {
            try {
                LOGGER.info("Closing the destination .pgm file dataOutputStream");
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
