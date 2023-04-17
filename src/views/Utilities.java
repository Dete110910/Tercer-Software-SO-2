package views;

import javax.swing.*;

public class Utilities {

    public static void showErrorDialog(String message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static int showConfirmationWarning(){
        int response = JOptionPane.showOptionDialog(null, "¿Está seguro?", "Advertencia",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[] {"Sí","No"}, "Sí");
        return response;
    }

    public static void showDoneCPUProcess(){
        JOptionPane.showMessageDialog(null, "Los procesos han sido ejecutados con éxito", "Acción realizada", JOptionPane.INFORMATION_MESSAGE);
    }
}
