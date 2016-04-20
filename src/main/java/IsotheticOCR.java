import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 4/16/2016.
 */
public class IsotheticOCR {

    //method to check vertical symmetry
    void verticalSymmetry(PgmImage pgmImage, Properties properties, Logger LOGGER){

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        int mid = pgmImage.imgWidth/2; //mirror line

        //variable to keep track of vertices to skip before deciding asymmetry
        int vertexSkipThreshold = Integer.parseInt(properties.getProperty("vertexSkipThreshold"));

        //variable to store the number of grid lines allowed to look for to find a vertex corresponding to the other vertex
        int gridError = Integer.parseInt(properties.getProperty("gridError"));

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
            Vertex currentHeadVertex; //vertex object at the head pointer
            Vertex currentTailVertex; //vertex object at the tail pointer
            Vertex prevHeadVertex = pgmImage.isotheticVertices.get(head); //previous head vertex
            Vertex prevTailVertex = pgmImage.isotheticVertices.get(tail); //previous tail vertex
            int currentHeadJDifference = 0; //to stopre the difference of j between current head vertex and mid
            int currentTailJDifference = 0; //to stopre the difference of j between current tail vertex and mid

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            LOGGER.info("Traversing the cover to check for the symmetry of : " + pgmImage.sourcePgmFile.getName());
            while (counter < pgmImage.isotheticVertices.size()){

                currentHeadVertex = pgmImage.isotheticVertices.get(head);
                currentTailVertex = pgmImage.isotheticVertices.get(tail);

                //distances of head and tail from the mid
                currentHeadJDifference = Math.abs(mid - currentHeadVertex.j);
                currentTailJDifference = Math.abs(currentTailVertex.j - mid);

                //if currentTailVertex is corresponding to currentHeadVertex
                if (currentHeadVertex.angle == currentTailVertex.angle && (currentTailJDifference >= currentHeadJDifference - gridError*gridSize || currentTailJDifference <= currentHeadJDifference + gridError*gridSize)){
                    skipStreak = 0;
                    pgmImage.verticalSymmetricity = 1;
                    head = (head + 1) % pgmImage.isotheticVertices.size();
                    tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                    prevHeadVertex = currentHeadVertex;
                    prevTailVertex = currentTailVertex;
                    counter++;
                }

                //if currentTailVertex is not corresponding to currentHeadVertex
                else {
                    //if the tail is ahead of the head
                    if (edgeLength(currentHeadVertex, prevHeadVertex) < edgeLength(currentTailVertex, prevTailVertex)){
                        prevHeadVertex = pgmImage.isotheticVertices.get((head + 1) % pgmImage.isotheticVertices.size());
                        head = (head + 2) % pgmImage.isotheticVertices.size();
                        skipStreak++;

                        //increase counter twice  here, because we are skipping the vertices
                        counter+=2;

                        pgmImage.totalSkip++;
                        if (skipStreak > vertexSkipThreshold){
                            pgmImage.verticalSymmetricity = 0;
                            break;
                        }
                    }

                    //if head is ahead of the tail
                    else if (edgeLength(currentTailVertex, prevTailVertex) < edgeLength(currentHeadVertex, prevHeadVertex)){
                        prevTailVertex = pgmImage.isotheticVertices.get((tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size());
                        tail = (tail - 2 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                        skipStreak++;
                        pgmImage.totalSkip++;
                        if (skipStreak > vertexSkipThreshold){
                            pgmImage.verticalSymmetricity = 0;
                            break;
                        }
                    }
                    //if both edge lengths are same then skip in both head and tail traversal
                    else if (edgeLength(currentHeadVertex, prevHeadVertex) == edgeLength(currentTailVertex, prevTailVertex)){
                        prevHeadVertex = pgmImage.isotheticVertices.get((head + 1) % pgmImage.isotheticVertices.size());
                        head = (head + 2) % pgmImage.isotheticVertices.size();
                        prevTailVertex = pgmImage.isotheticVertices.get((tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size());
                        tail = (tail - 2 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

                        //increment the counter twice because we are skipping the vertices
                        counter+=2;
                        skipStreak++;
                        pgmImage.totalSkip++;
                        if (skipStreak > vertexSkipThreshold){
                            pgmImage.verticalSymmetricity = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    void horizontalSymmetry(PgmImage pgmImage, Properties properties, Logger LOGGER){
        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        int mid = pgmImage.imgHeight/2; //mirror line

        //variable to keep track of vertices to skip before deciding asymmetry
        int vertexSkipThreshold = Integer.parseInt(properties.getProperty("vertexSkipThreshold"));

        //variable to store the number of grid lines allowed to look for to find a vertex corresponding to the other vertex
        int gridError = Integer.parseInt(properties.getProperty("gridError"));

        //variable to keep track of consecutive skips
        int skipStreak = 0;

        //variable to begin the symmetry match, last element of left most column in isothetic cover
        int head = pgmImage.isotheticVertices.indexOf(pgmImage.jSorted2DList.get(0).get(pgmImage.jSorted2DList.get(0).size() - 1));

        //variable to traverse in reverse direction to find the corresponding element to head
        int tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

        //flag to know if the tail vertex corresponding to starting Head vertex has been found or not
        int found = 0;

        Vertex headVertex = pgmImage.isotheticVertices.get(head);
        int headIDifference = mid - headVertex.i;

        LOGGER.info("Finding the tail vertex corresponding to head in : " + pgmImage.sourcePgmFile.getName());
        //to find the starting value of tail (mirror of head) at <mid + d, j> (if head = mid - d, j)
        if (found == 0) {

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0) {

                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //perfectly mirrored vertex
                if (temp.j == headVertex.j && temp.i == mid + headIDifference && temp.angle == headVertex.angle) {
                    found++;
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
                if (temp.j == headVertex.j && temp.i == (mid + headIDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
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
                if (temp.j == headVertex.j && temp.i == (mid + headIDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
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
                if (temp.j == headVertex.j + gridSize && temp.i == (mid + headIDifference) && temp.angle == headVertex.angle){
                    found++;
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
                if (temp.j == headVertex.j + gridSize && temp.i == (mid + headIDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
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
                if (temp.j == headVertex.j + gridSize && temp.i == (mid + headIDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if corresponding vertex not found
        if(found == 0){
            LOGGER.warning("Tail vertex corresponding to the head vertex not found in : " + pgmImage.sourcePgmFile.getName());
            pgmImage.horizontalSymmetricity = 0; //set symmetricity = 0, i.e. not symmetric
        }

        //if corresponding vertex found
        else {
            LOGGER.info("Tail vertex corresponding to the head vertex found in : " + pgmImage.sourcePgmFile.getName());
            Vertex currentHeadVertex; //vertex object at the head pointer
            Vertex currentTailVertex; //vertex object at the tail pointer
            Vertex prevHeadVertex = pgmImage.isotheticVertices.get(head); //previous head vertex
            Vertex prevTailVertex = pgmImage.isotheticVertices.get(tail); //previous tail vertex
            int currentHeadIDifference = 0; //to stopre the difference of j between current head vertex and mid
            int currentTailIDifference = 0; //to stopre the difference of j between current tail vertex and mid

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            LOGGER.info("Traversing the cover to check for the symmetry of : " + pgmImage.sourcePgmFile.getName());
            while (counter < pgmImage.isotheticVertices.size()){

                currentHeadVertex = pgmImage.isotheticVertices.get(head);
                currentTailVertex = pgmImage.isotheticVertices.get(tail);

                //distances of Head and Tail from the mid
                currentHeadIDifference = Math.abs(mid - currentHeadVertex.i);
                currentTailIDifference = Math.abs(currentTailVertex.i - mid);

                //if currentTailVertex is corresponding to currentHeadVertex
                if (currentHeadVertex.angle == currentTailVertex.angle && (currentTailIDifference >= currentHeadIDifference - gridError*gridSize && currentTailIDifference <= currentHeadIDifference + gridError*gridSize)){
                    skipStreak = 0;
                    pgmImage.horizontalSymmetricity = 1;
                    head = (head + 1) % pgmImage.isotheticVertices.size();
                    tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                    prevHeadVertex = currentHeadVertex;
                    prevTailVertex = currentTailVertex;
                    counter++;
                }

                //if currentTailVertex is not corresponding to currentHeadVertex
                else {
                    //if the tail is ahead of the head
                    if (edgeLength(currentHeadVertex, prevHeadVertex) < edgeLength(currentTailVertex, prevTailVertex)){
                        prevHeadVertex = pgmImage.isotheticVertices.get((head + 1) % pgmImage.isotheticVertices.size());
                        head = (head + 2) % pgmImage.isotheticVertices.size();
                        skipStreak++;

                        //increase counter twice  here, because we are skipping the vertices
                        counter+=2;

                        pgmImage.totalSkip++;
                        if (skipStreak > vertexSkipThreshold){
                            pgmImage.horizontalSymmetricity = 0;
                            break;
                        }
                    }

                    //if head is ahead of the tail
                    else if (edgeLength(currentTailVertex, prevTailVertex) < edgeLength(currentHeadVertex, prevHeadVertex)){
                        prevTailVertex = pgmImage.isotheticVertices.get((tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size());
                        tail = (tail - 2 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                        skipStreak++;
                        pgmImage.totalSkip++;
                        if (skipStreak > vertexSkipThreshold){
                            pgmImage.horizontalSymmetricity = 0;
                            break;
                        }
                    }
                    //if both edge lengths are same then skip in both head and tail traversal
                    else if (edgeLength(currentHeadVertex, prevHeadVertex) == edgeLength(currentTailVertex, prevTailVertex)){
                        prevHeadVertex = pgmImage.isotheticVertices.get((head + 1) % pgmImage.isotheticVertices.size());
                        head = (head + 2) % pgmImage.isotheticVertices.size();
                        prevTailVertex = pgmImage.isotheticVertices.get((tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size());
                        tail = (tail - 2 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

                        //increment the counter twice because we are skipping the vertices
                        counter+=2;
                        skipStreak++;
                        pgmImage.totalSkip++;
                        if (skipStreak > vertexSkipThreshold){
                            pgmImage.horizontalSymmetricity = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    int edgeLength(Vertex v1, Vertex v2){

        if(v1.i == v2.i){
            return Math.abs(v2.j - v1. j);
        }
        else if (v1.j == v2.j){
            return Math.abs(v2.i - v1.i);
        }
        else
            return -1;
    }
}
