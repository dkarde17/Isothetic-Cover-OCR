import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by CNOVA on 3/31/2016.
 */
//class to read and write from the image file (original .pgm file)
public class ImgReadWrite {

    //method to intialize the parameters in the pgmImage object e.g. imgWidth, imgHeight, etc. from the .pgm source file
    public static void init(PgmImage pgmImage, Properties properties){
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        try {

            fileInputStream = new FileInputStream(pgmImage.path);
            dataInputStream = new DataInputStream(fileInputStream);

            //reading the type of the image
            pgmImage.imgType = dataInputStream.readLine();
            int parameterCount = 0; //should not be greater than 3 i.e. height, width and maximum Intensity

            // to parse the header: drop the comments and to find out the width, height and maximum Intensity
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
            if(pgmImage.imgWidth != Integer.parseInt(properties.getProperty("imgWidth")) || pgmImage.imgHeight != Integer.parseInt(properties.getProperty("imgHeight"))){
                System.out.println("Invalid Image: The dimensions found in the image header are not consistent with those specified in the config.properties file");
                System.exit(-1);
            }

            //initializing the imgMatrix
            pgmImage.imgMatrix = new int[pgmImage.imgHeight][pgmImage.imgWidth];
            //in pgm images we can not have anything after the maximum intensity except for a white space (generally "end of the line"), after that the image intensity matrix starts
            int pixel = 0;

            //reading the threshold intensity from the config.properties file
            int threshold = Integer.parseInt(properties.getProperty("pixelThreshold"));

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
                        System.out.println("Invalid Image : the no. of pixel intensities given are not consistent with the size specified");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close(); //not needed, closing dataInputStream (Wrapper) will close fileInputStream also
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //method to binarize the imgMatrix in the pgmImage object, i.e. if pixels are lighter than a threshold, make white (255) otherwise black (0)
    public static void binarizeImgMatrix(PgmImage pgmImage, Properties properties){
        for (int i = 0; i < pgmImage.imgHeight; i++){
            for(int j = 0; j < pgmImage.imgWidth; j++){
                pgmImage.imgMatrix[i][j] = (pgmImage.imgMatrix[i][j] > Integer.parseInt(properties.getProperty("pixelThreshold"))) ? 255 : 0;
            }
        }
    }


    //method to write the cover to the imgMatrix using the list of Isothetic Polygon vertices
    public static void writeCoverToImgMatrix(PgmImage pgmImage, ArrayList<Vertex> vertices){
        Iterator iterator = vertices.iterator();
        Vertex start = (Vertex) iterator.next();
        Vertex vertex = start;
        Vertex nextVertex = null;

        while (nextVertex != start){

            if (iterator.hasNext()){
                nextVertex = (Vertex) iterator.next();
                writeEdgetoImgMatrix(pgmImage, vertex, nextVertex);
                vertex = nextVertex;
            }
            else {
                nextVertex = start;
                writeEdgetoImgMatrix(pgmImage, vertex, nextVertex);
            }
        }
    }

    //method to write one edge of the Isothetic polygon from vertex1<iFrom, jFrom> to vertex2 <iTo, jTo> into the imgMatrix of pgmImage
    private static void writeEdgetoImgMatrix(PgmImage pgmImage, Vertex vertex, Vertex nextVertex){

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
    public static void writeImgToFile(PgmImage pgmImage){
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pgmImage.path);
            dataOutputStream = new DataOutputStream(fileOutputStream);

            dataOutputStream.writeBytes(pgmImage.imgType + "\n");
            dataOutputStream.writeBytes("# Created for Final Year Project, group 09 - 2016, CSE, NIT SIlchar" + "\n");
            dataOutputStream.writeBytes(String.valueOf(pgmImage.imgWidth) + " " + String.valueOf(pgmImage.imgHeight) + "\n");
            dataOutputStream.writeBytes(String.valueOf(pgmImage.maxIntensity) + "\n");

            for (int i = 0; i < pgmImage.imgHeight; i++){
                for (int j = 0; j < pgmImage.imgWidth; j++){
                    dataOutputStream.writeByte(pgmImage.imgMatrix[i][j]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
