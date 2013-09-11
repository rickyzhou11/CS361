import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class SecureSystem {

}


public static void main (String args[]) throws IOException {

        SecurityLevel low  = SecurityLevel.LOW;
        SecurityLevel high = SecurityLevel.HIGH;

        // We add two subjects, one high and one low.

        sys.createSubject("Lyle", low);
        sys.createSubject("Hal", high);

        // We add two objects, one high and one low.

        sys.getReferenceMonitor().createNewObject("Lobj", low);
        sys.getReferenceMonitor().createNewObject("Hobj", high);


}
