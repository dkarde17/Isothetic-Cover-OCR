import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 4/16/2016.
 */
public class IsotheticOCR {

    private int headIDirection;
    private int tailIDirection;
    private int headJDirection;
    private int tailJDirection;

    //method to check vertical symmetry
    void verticalSymmetry(PgmImage pgmImage, Properties properties, Logger LOGGER){

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        int mid = pgmImage.imgWidth/2; //mirror line

        //variable to keep track of vertices to skip before deciding asymmetry
        int vertexSkipThreshold = Integer.parseInt(properties.getProperty("vertexSkipThresholdVertical"));

        //variable to store the number of grid lines allowed to look for to find a vertex corresponding to the other vertex
        int iGridErrorVertical = Integer.parseInt(properties.getProperty("iGridErrorVertical"));
        int jGridErrorVertical = Integer.parseInt(properties.getProperty("jGridErrorVertical"));

        //variable to keep track of consecutive skips
        int skipStreak = 0;

        //variable to begin the symmetry match, first element of top most row in isothetic cover
        int head = pgmImage.isotheticVertices.indexOf(pgmImage.iSorted2DList.get(0).get(0));

        //variable to traverse in reverse direction to find the corresponding element to head
        int tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

        //flag to know if the tail vertex corresponding to starting Head vertex has been found or not
        int found = 0;

        Vertex headVertex = pgmImage.isotheticVertices.get(head);

        int headJDifference = mid - headVertex.j;

        LOGGER.info("Finding the tail vertex corresponding to head in : " + pgmImage.sourcePgmFile.getName());
        //to find the starting value of tail (mirror of head) at i, mid + d (if head = i, mid - d)
        if (found == 0) {

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0) {

                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //perfectly mirrored vertex
                if (temp.i == headVertex.i && temp.j == mid + headJDifference && temp.angle == headVertex.angle) {
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if mirror of head not found then look for <i, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){

                Vertex temp = pgmImage.isotheticVertices.get(tail);
                //vertex one grid size before the perfectly mirrored vertex
                if (temp.i == headVertex.i && temp.j == (mid + headJDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;
            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.i == headVertex.i && temp.j == (mid + headJDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }

        }

        //if still not found then look for <i - 1, mid + d>
        if(found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex on i+g, j mirror image
                if (temp.i == headVertex.i + gridSize && temp.j == (mid + headJDifference) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.i == headVertex.i + gridSize && temp.j == (mid + headJDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.i == headVertex.i + gridSize && temp.j == (mid + headJDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if corresponding vertex not found
        if(found == 0){
            LOGGER.warning("Tail vertex corresponding to the head vertex not found in : " + pgmImage.sourcePgmFile.getName());
            //set symmetricity = 0, i.e. not symmetric
            pgmImage.verticalSymmetricity = 0;
        }

        //if corresponding vertex found
        else {
            LOGGER.info("Tail vertex corresponding to the head vertex found in : " + pgmImage.sourcePgmFile.getName());
            Vertex tailVertex = pgmImage.isotheticVertices.get(tail);
            Vertex currentHeadVertex = pgmImage.isotheticVertices.get(head); //vertex object at the head pointer
            Vertex currentTailVertex = pgmImage.isotheticVertices.get(tail); //vertex object at the tail pointer
            Vertex prevHeadVertex = currentHeadVertex; //previous head vertex
            Vertex prevTailVertex = currentTailVertex; //previous tail vertex

            int currentHeadJDifference = 0; //to stopre the difference of j between current head vertex and mid
            int currentTailJDifference = 0; //to stopre the difference of j between current tail vertex and mid

            //to keep track of who is leading
            int headIDistance = 0;
            int headJDistance = 0;
            int tailIDistance = 0;
            int tailJDistance = 0;

            headIDirection = 0;
            tailIDirection = 0;
            headJDirection = 1;//doesn't matter
            tailJDirection = 0;//doesn't matter

            int headMoved = 0;
            int tailMoved = 0;

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            LOGGER.info("Traversing the cover to check for the symmetry of : " + pgmImage.sourcePgmFile.getName());
            do{

                System.out.println("found : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle + " and " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);

                //to keep track if some point has moved or not
                if (headMoved == 1) {
                    if (prevHeadVertex.i == currentHeadVertex.i) {
                        headJDistance = headJDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                    if (prevHeadVertex.j == currentHeadVertex.j) {
                        headIDistance = headIDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                }
                if (tailMoved == 1) {
                    if (prevTailVertex.i == currentTailVertex.i) {
                        tailJDistance = tailJDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                    if (prevTailVertex.j == currentTailVertex.j) {
                        tailIDistance = tailIDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                }

                //if currentTail.i is almost the same i level as currentHead.i
                if (currentTailVertex.i >= currentHeadVertex.i - iGridErrorVertical*gridSize && currentTailVertex.i <= currentHeadVertex.i + iGridErrorVertical*gridSize) {
                    System.out.println("both on the same level");

                    //distances of head and tail from the mid
                    currentHeadJDifference = mid - currentHeadVertex.j;
                    currentTailJDifference = currentTailVertex.j - mid;

                    //if currentTailVertex is corresponding to currentHeadVertex
                    if (currentTailJDifference >= currentHeadJDifference - jGridErrorVertical * gridSize && currentTailJDifference <= currentHeadJDifference + jGridErrorVertical * gridSize) {
                        System.out.println("almost same j distance");
                        if (currentHeadVertex.angle == currentTailVertex.angle) {
                            skipStreak = 0;
                            System.out.println("accepted");
                            pgmImage.verticalSymmetricity = 1;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 1;
                            prevHeadVertex = currentHeadVertex;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            prevTailVertex = currentTailVertex;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);

                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            updateTailDirection(currentTailVertex, prevTailVertex);

                            counter++;
                        }
                        else {
                            if (headJDistance > tailJDistance){
                                System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                                headMoved = 0;
                                tailMoved = 1;
                                prevTailVertex = currentTailVertex;
                                currentTailVertex = pgmImage.isotheticVertices.get(tail);
                                updateTailDirection(currentTailVertex, prevTailVertex);
                                skipStreak++;
                                pgmImage.totalTailSkip++;
                                if (skipStreak > vertexSkipThreshold) {
                                    pgmImage.verticalSymmetricity = 0;
                                    break;
                                }
                            }
                            else if (tailJDistance > headJDistance){
                                System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                                prevHeadVertex = currentHeadVertex;
                                head = (head + 1) % pgmImage.isotheticVertices.size();
                                headMoved = 1;
                                tailMoved = 0;
                                currentHeadVertex = pgmImage.isotheticVertices.get(head);
                                updateHeadDirection(currentHeadVertex, prevHeadVertex);
                                skipStreak++;
                                //increase counter twice  here, because we are skipping the vertices
                                counter++;
                                pgmImage.totalHeadSkip++;
                                if (skipStreak > vertexSkipThreshold) {
                                    pgmImage.verticalSymmetricity = 0;
                                    break;
                                }
                            }
                            else {
                                System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle + " and " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                                head = (head + 1) % pgmImage.isotheticVertices.size();
                                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                                headMoved = 1;
                                tailMoved = 1;
                                prevHeadVertex = currentHeadVertex;
                                currentHeadVertex = pgmImage.isotheticVertices.get(head);
                                prevTailVertex = currentTailVertex;
                                currentTailVertex = pgmImage.isotheticVertices.get(tail);
                                updateHeadDirection(currentHeadVertex, prevHeadVertex);
                                updateTailDirection(currentTailVertex, prevTailVertex);
                                counter++;
                                skipStreak++;
                                pgmImage.totalHeadSkip++;
                                pgmImage.totalTailSkip++;
                                if (skipStreak > vertexSkipThreshold) {
                                    pgmImage.verticalSymmetricity = 0;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("j distance not almost same");
                        if (headJDistance > tailJDistance){
                            System.out.println("head j distance greater");
                            System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            headMoved = 0;
                            tailMoved = 1;
                            prevTailVertex = currentTailVertex;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);
                            updateTailDirection(currentTailVertex, prevTailVertex);
                            skipStreak++;
                            pgmImage.totalTailSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.verticalSymmetricity = 0;
                                break;
                            }
                        }
                        else {
                            System.out.println("tail j distance greater");
                            System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                            prevHeadVertex = currentHeadVertex;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 0;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            skipStreak++;
                            //increase counter twice  here, because we are skipping the vertices
                            counter++;
                            pgmImage.totalHeadSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.verticalSymmetricity = 0;
                                break;
                            }
                        }
                    }
                }

                else {
                    System.out.println(headIDirection);
                    System.out.println(tailIDirection);
                    if (headIDirection == tailIDirection){
                        System.out.println("head directions same");
                        if ((headIDirection == 0 && currentHeadVertex.i > currentTailVertex.i) || (headIDirection == 1 && currentHeadVertex.i < currentTailVertex.i)){
                            System.out.println("first if");
                            System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            headMoved = 0;
                            tailMoved = 1;
                            prevTailVertex = currentTailVertex;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);
                            updateTailDirection(currentTailVertex, prevTailVertex);
                            skipStreak++;
                            pgmImage.totalTailSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.verticalSymmetricity = 0;
                                break;
                            }
                        }
                        else if ((headIDirection == 0 && currentHeadVertex.i < currentTailVertex.i) || (headIDirection == 1 && currentHeadVertex.i > currentTailVertex.i)){
                            System.out.println("second if");
                            System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                            prevHeadVertex = currentHeadVertex;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 0;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            skipStreak++;
                            //increase counter twice  here, because we are skipping the vertices
                            counter++;
                            pgmImage.totalHeadSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.verticalSymmetricity = 0;
                                break;
                            }
                        }
                    }
                    else {
                        if (headIDistance > tailIDistance){
                            System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            prevTailVertex = currentTailVertex;
                            headMoved = 0;
                            tailMoved = 1;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);
                            updateTailDirection(currentTailVertex, prevTailVertex);
                            skipStreak++;
                            pgmImage.totalTailSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.verticalSymmetricity = 0;
                                break;
                            }
                        }
                        else if (headIDistance < tailIDistance){
                            System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                            prevHeadVertex = currentHeadVertex;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 0;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            skipStreak++;
                            //increase counter twice  here, because we are skipping the vertices
                            counter++;
                            pgmImage.totalHeadSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.verticalSymmetricity = 0;
                                break;
                            }
                        }
                    }
                }
            }while (counter < pgmImage.isotheticVertices.size() && currentHeadVertex != headVertex && currentTailVertex != tailVertex);
        }
    }

    void horizontalSymmetry(PgmImage pgmImage, Properties properties, Logger LOGGER){

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        int mid = pgmImage.imgHeight/2; //mirror line

        //variable to keep track of vertices to skip before deciding asymmetry
        int vertexSkipThreshold = Integer.parseInt(properties.getProperty("vertexSkipThresholdHorizontal"));

        //variable to store the number of grid lines allowed to look for to find a vertex corresponding to the other vertex
        int iGridErrorHorizontal = Integer.parseInt(properties.getProperty("iGridErrorHorizontal"));
        int jGridErrorHorizontal = Integer.parseInt(properties.getProperty("jGridErrorHorizontal"));

        //variable to keep track of consecutive skips
        int skipStreak = 0;

        //variable to begin the symmetry match, first element of top most row in isothetic cover
        int head = pgmImage.isotheticVertices.indexOf(pgmImage.jSorted2DList.get(0).get(pgmImage.jSorted2DList.get(0).size() - 1));

        //variable to traverse in reverse direction to find the corresponding element to head
        int tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

        //flag to know if the tail vertex corresponding to starting Head vertex has been found or not
        int found = 0;

        Vertex headVertex = pgmImage.isotheticVertices.get(head);

        int headIDifference = headVertex.i - mid;

        LOGGER.info("Finding the tail vertex corresponding to head in : " + pgmImage.sourcePgmFile.getName());
        //to find the starting value of tail (mirror of head) at i, mid + d (if head = i, mid - d)
        if (found == 0) {

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0) {

                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //perfectly mirrored vertex
                if (temp.j == headVertex.j && temp.i == mid - headIDifference && temp.angle == headVertex.angle) {
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if mirror of head not found then look for <i, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){

                Vertex temp = pgmImage.isotheticVertices.get(tail);
                //vertex one grid size before the perfectly mirrored vertex
                if (temp.j == headVertex.j && temp.i == (mid - headIDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;
            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.j == headVertex.j && temp.i == (mid - headIDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }

        }

        //if still not found then look for <i - 1, mid + d>
        if(found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex on i+g, j mirror image
                if (temp.j == headVertex.j + gridSize && temp.i == (mid - headIDifference) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.j == headVertex.j + gridSize && temp.i == (mid - headIDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.j == headVertex.j + gridSize && temp.i == (mid - headIDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if corresponding vertex not found
        if(found == 0){
            LOGGER.warning("Tail vertex corresponding to the head vertex not found in : " + pgmImage.sourcePgmFile.getName());
            //set symmetricity = 0, i.e. not symmetric
            pgmImage.horizontalSymmetricity = 0;
        }

        //if corresponding vertex found
        else {
            LOGGER.info("Tail vertex corresponding to the head vertex found in : " + pgmImage.sourcePgmFile.getName());
            Vertex tailVertex = pgmImage.isotheticVertices.get(tail);
            Vertex currentHeadVertex = pgmImage.isotheticVertices.get(head); //vertex object at the head pointer
            Vertex currentTailVertex = pgmImage.isotheticVertices.get(tail); //vertex object at the tail pointer
            Vertex prevHeadVertex = currentHeadVertex; //previous head vertex
            Vertex prevTailVertex = currentTailVertex; //previous tail vertex

            int currentHeadIDifference = 0; //to stopre the difference of j between current head vertex and mid
            int currentTailIDifference = 0; //to stopre the difference of j between current tail vertex and mid

            //to keep track of who is leading
            int headIDistance = 0;
            int headJDistance = 0;
            int tailIDistance = 0;
            int tailJDistance = 0;

            headIDirection = 1;//doesn't matter
            tailIDirection = 0;//doesn't matter
            headJDirection = 0;
            tailJDirection = 0;

            int headMoved = 0;
            int tailMoved = 0;

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            LOGGER.info("Traversing the cover to check for the symmetry of : " + pgmImage.sourcePgmFile.getName());
            do{

                System.out.println("found : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle + " and " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);

                if (headMoved == 1) {
                    if (prevHeadVertex.i == currentHeadVertex.i) {
                        headJDistance = headJDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                    if (prevHeadVertex.j == currentHeadVertex.j) {
                        headIDistance = headIDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                }
                if (tailMoved == 1) {
                    if (prevTailVertex.i == currentTailVertex.i) {
                        tailJDistance = tailJDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                    if (prevTailVertex.j == currentTailVertex.j) {
                        tailIDistance = tailIDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                }

                //if currentTail.i is almost the same i level as currentHead.i
                if (currentTailVertex.j >= currentHeadVertex.j - jGridErrorHorizontal*gridSize && currentTailVertex.j <= currentHeadVertex.j + jGridErrorHorizontal*gridSize) {
                    System.out.println("both on the same level");

                    //distances of head and tail from the mid
                    currentHeadIDifference = currentHeadVertex.i - mid;
                    currentTailIDifference = mid - currentTailVertex.i;

                    //if currentTailVertex is corresponding to currentHeadVertex
                    if (currentTailIDifference >= currentHeadIDifference - iGridErrorHorizontal * gridSize && currentTailIDifference <= currentHeadIDifference + iGridErrorHorizontal * gridSize) {
                        System.out.println("almost same i distance");
                        if (currentHeadVertex.angle == currentTailVertex.angle) {
                            skipStreak = 0;
                            System.out.println("accepted");
                            pgmImage.horizontalSymmetricity = 1;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 1;
                            prevHeadVertex = currentHeadVertex;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            prevTailVertex = currentTailVertex;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);

                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            updateTailDirection(currentTailVertex, prevTailVertex);

                            counter++;
                        }
                        else {
                            if (headIDistance > tailIDistance){
                                System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                                headMoved = 0;
                                tailMoved = 1;
                                prevTailVertex = currentTailVertex;
                                currentTailVertex = pgmImage.isotheticVertices.get(tail);
                                updateTailDirection(currentTailVertex, prevTailVertex);
                                skipStreak++;
                                pgmImage.totalTailSkip++;
                                if (skipStreak > vertexSkipThreshold) {
                                    pgmImage.horizontalSymmetricity = 0;
                                    break;
                                }
                            }
                            else if (tailIDistance > headIDistance){
                                System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                                prevHeadVertex = currentHeadVertex;
                                head = (head + 1) % pgmImage.isotheticVertices.size();
                                headMoved = 1;
                                tailMoved = 0;
                                currentHeadVertex = pgmImage.isotheticVertices.get(head);
                                updateHeadDirection(currentHeadVertex, prevHeadVertex);
                                skipStreak++;
                                //increase counter twice  here, because we are skipping the vertices
                                counter++;
                                pgmImage.totalHeadSkip++;
                                if (skipStreak > vertexSkipThreshold) {
                                    pgmImage.horizontalSymmetricity = 0;
                                    break;
                                }
                            }
                            else {
                                System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle + " and " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                                head = (head + 1) % pgmImage.isotheticVertices.size();
                                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                                headMoved = 1;
                                tailMoved = 1;
                                prevHeadVertex = currentHeadVertex;
                                currentHeadVertex = pgmImage.isotheticVertices.get(head);
                                prevTailVertex = currentTailVertex;
                                currentTailVertex = pgmImage.isotheticVertices.get(tail);
                                updateHeadDirection(currentHeadVertex, prevHeadVertex);
                                updateTailDirection(currentTailVertex, prevTailVertex);
                                counter++;
                                skipStreak++;
                                pgmImage.totalHeadSkip++;
                                pgmImage.totalTailSkip++;
                                if (skipStreak > vertexSkipThreshold) {
                                    pgmImage.horizontalSymmetricity = 0;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("i distance not almost same");
                        if (headIDistance > tailIDistance){
                            System.out.println("head i distance greater");
                            System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            headMoved = 0;
                            tailMoved = 1;
                            prevTailVertex = currentTailVertex;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);
                            updateTailDirection(currentTailVertex, prevTailVertex);
                            skipStreak++;
                            pgmImage.totalTailSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.horizontalSymmetricity = 0;
                                break;
                            }
                        }
                        else {
                            System.out.println("tail i distance greater");
                            System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                            prevHeadVertex = currentHeadVertex;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 0;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            skipStreak++;
                            //increase counter twice  here, because we are skipping the vertices
                            counter++;
                            pgmImage.totalHeadSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.horizontalSymmetricity = 0;
                                break;
                            }
                        }
                    }
                }

                else {
                    System.out.println(headJDirection);
                    System.out.println(tailJDirection);
                    if (headJDirection == tailJDirection){
                        System.out.println("head directions same");
                        if ((headJDirection == 0 && currentHeadVertex.j > currentTailVertex.j) || (headJDirection == 1 && currentHeadVertex.j < currentTailVertex.j)){
                            System.out.println("first if");
                            System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            headMoved = 0;
                            tailMoved = 1;
                            prevTailVertex = currentTailVertex;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);
                            updateTailDirection(currentTailVertex, prevTailVertex);
                            skipStreak++;
                            pgmImage.totalTailSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.horizontalSymmetricity = 0;
                                break;
                            }
                        }
                        else if ((headJDirection == 0 && currentHeadVertex.j < currentTailVertex.j) || (headJDirection == 1 && currentHeadVertex.j > currentTailVertex.j)){
                            System.out.println("second if");
                            System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                            prevHeadVertex = currentHeadVertex;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 0;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            skipStreak++;
                            //increase counter twice  here, because we are skipping the vertices
                            counter++;
                            pgmImage.totalHeadSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.horizontalSymmetricity = 0;
                                break;
                            }
                        }
                    }
                    else {
                        if (headJDistance > tailJDistance){
                            System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
                            tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                            prevTailVertex = currentTailVertex;
                            headMoved = 0;
                            tailMoved = 1;
                            currentTailVertex = pgmImage.isotheticVertices.get(tail);
                            updateTailDirection(currentTailVertex, prevTailVertex);
                            skipStreak++;
                            pgmImage.totalTailSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.horizontalSymmetricity = 0;
                                break;
                            }
                        }
                        else if (headJDistance < tailJDistance){
                            System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
                            prevHeadVertex = currentHeadVertex;
                            head = (head + 1) % pgmImage.isotheticVertices.size();
                            headMoved = 1;
                            tailMoved = 0;
                            currentHeadVertex = pgmImage.isotheticVertices.get(head);
                            updateHeadDirection(currentHeadVertex, prevHeadVertex);
                            skipStreak++;
                            //increase counter twice  here, because we are skipping the vertices
                            counter++;
                            pgmImage.totalHeadSkip++;
                            if (skipStreak > vertexSkipThreshold) {
                                pgmImage.horizontalSymmetricity = 0;
                                break;
                            }
                        }
                    }
                }
            }while (counter < pgmImage.isotheticVertices.size() && currentHeadVertex != headVertex && currentTailVertex != tailVertex);
        }
    }

    void scoreCalculator(PgmImage pgmImage){

        int prevEdgeType = 0; //0 for horizontal, 1 for vertical

        Iterator<Vertex> traversor = pgmImage.isotheticVertices.iterator();

        //starting vertex
        Vertex start = traversor.next();

        Vertex prevVertex = start;

        Vertex currentVertex;

        while (traversor.hasNext()){
            currentVertex = traversor.next();


        }

    }

    private int edgeLength(Vertex v1, Vertex v2){

        if(v1.i == v2.i){
            return Math.abs(v2.j - v1. j);
        }
        else if (v1.j == v2.j){
            return Math.abs(v2.i - v1.i);
        }
        else
            return -1;
    }

    private void updateHeadDirection(Vertex currentHead, Vertex previousHead){
        if (currentHead.i == previousHead.i){
            if ((headJDirection == 0 && currentHead.angle == 1) || (headJDirection == 1 && currentHead.angle == 0))
                headIDirection = 0;
            else
                headIDirection = 1;
        }
        else if (currentHead.j == previousHead.j){
            if ((headIDirection == 0 && currentHead.angle == 0) || (headIDirection == 1 && currentHead.angle == 1))
                headJDirection = 0;
            else
                headJDirection = 1;
        }
    }

    private void updateTailDirection(Vertex currentTail, Vertex previousTail){
        if (currentTail.i == previousTail.i){
            if ((tailJDirection == 0 && currentTail.angle == 1) || (tailJDirection == 1 && currentTail.angle == 0))
                tailIDirection = 0;
            else
                tailIDirection = 1;
        }
        else if (currentTail.j == previousTail.j){
            if ((tailIDirection == 0 && currentTail.angle == 0) || (tailIDirection == 1 && currentTail.angle == 1))
                tailJDirection = 0;
            else
                tailJDirection = 1;
        }
    }
}
