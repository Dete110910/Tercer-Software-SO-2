package persistence;

import models.Process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PersistenceManager {
    private BufferedWriter bufferedWriter;
    private FileWriter fileWriter;

    public static void saveReport(String report, ArrayList<Process> reportList) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./Reportes/"+report));
            bufferedWriter.write("|  NOMBRE  |  |  TIEMPO  |  |  BLOQUEO  |  |  SUSPENDER  |  |  REANUDAR  |   ");
            bufferedWriter.newLine();
            bufferedWriter.write("_______________________________________________________________________");
            bufferedWriter.newLine();

            for (int i = 0; i < reportList.size(); i++) {
                bufferedWriter.write("|  ");
                bufferedWriter.write(reportList.get(i).getName());
                bufferedWriter.write("      |  |");
                bufferedWriter.write("  ");
                bufferedWriter.write(String.valueOf(reportList.get(i).getTime()));
                bufferedWriter.write("     |  |");
                bufferedWriter.write("  ");
                bufferedWriter.write(status(reportList.get(i).isBlock()));
                bufferedWriter.write("      |");
                bufferedWriter.write("  ");
                bufferedWriter.write(status(reportList.get(i).isResume()));
                bufferedWriter.write("     |  |");
                bufferedWriter.write("  ");
                bufferedWriter.write(status(reportList.get(i).isSuspend()));
                bufferedWriter.write("     |  |");
                bufferedWriter.write("  ");
                bufferedWriter.newLine();
            }
            bufferedWriter.write("_______________________________________________________________________");
            bufferedWriter.newLine();

            bufferedWriter.close();

        } catch (IOException e) {

        }
    }

    public static String status(Boolean isBlock){
        String block = "";
        if (isBlock){
            block = "Si";
        }else{
            block = "No";
        }
        return block;
    }

}
