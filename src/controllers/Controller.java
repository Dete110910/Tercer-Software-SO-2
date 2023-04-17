package controllers;

import models.Process;
import models.ProcessManager;
import views.ViewManager;
import views.Utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;

public class Controller implements ActionListener, KeyListener {

    private ViewManager viewManager;
    private ProcessManager processManager;
    public Controller(){
        this.viewManager = new ViewManager(this, this);
        this.processManager = new ProcessManager();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "CrearProceso":
                this.showCreateProcessDialog();
                break;
            case "AñadirProceso":
                this.addProcess();
                break;
            case "CancelarAñadirProceso":
                this.hideCreateProcessDialog();
                break;
            case "ModificarProceso":
                this.showModifyProcessDialog();
                break;
            case "ConfirmarModificacion":
                this.modifyProcess();
                break;
            case "NoSuspende":
                this.disableResumeButton();
                break;
            case "Suspende":
                this.enableResumeButton();
                break;
            case "EliminarProceso":
                this.deleteProcess();
                break;
            case "Reportes":
                this.changeToReportsMenu();
                break;
            case "ReporteListos":
                this.changeTableToReadyReports();
                break;
            case "Atras":
                this.changeToMainMenu();
                break;
            case "ManualUsuario":
                this.openManual();
                break;
            case "Salir":
                System.exit(0);
                break;

        }
    }


    private void showCreateProcessDialog(){
        this.viewManager.showCreateProcessDialog();
    }

    private void addProcess(){
        String nameProcess = this.viewManager.getNameProcess();
        BigInteger timeProcess = this.viewManager.getTimeProcess();
        boolean isBlocked = this.viewManager.getIsBlocked();
        boolean isSuspended = this.viewManager.getIsSuspended();
        boolean isResume = this.viewManager.getIsResume();

        if(!processManager.isAlreadyName(nameProcess) && !nameProcess.trim().isEmpty() && !timeProcess.toString().equals("-1")){
            Process newProcess = new Process(nameProcess, timeProcess, isBlocked, isSuspended, isResume);
            processManager.addToInQueue(newProcess);
            viewManager.setValuesToTable(processManager.getListAsMatrixObject(processManager.getInQueue()), "Procesos Existentes");
            viewManager.hideCreateAndModifyProcessDialog();
        }
        else if(processManager.isAlreadyName(nameProcess)){
            Utilities.showErrorDialog("Ya existe un proceso con este nombre", "Error");
        }
        else if(nameProcess.trim().isEmpty()){
            Utilities.showErrorDialog("El nombre del proceso está vacío. Ingrese algún valor", "Error");
        }
        else if(timeProcess.toString().equals("-1")){
            Utilities.showErrorDialog("El tiempo del proceso está vacío. Ingrese un valor numérico entero", "Error");
        }


    }

    private void hideCreateProcessDialog() {
        this.viewManager.hideCreateAndModifyProcessDialog();
    }

    private void showModifyProcessDialog(){
        if(this.viewManager.getIndexDataInTable() == -1){
            Utilities.showErrorDialog("Debe seleccionar un proceso", "Error");
        }
        else {
            Process processToModify = processManager.getProcessInQueue(viewManager.getIndexDataInTable());
             this.viewManager.setNameProcess(processToModify.getName());
             this.viewManager.setTimeProcess(processToModify.getTime());
             this.viewManager.setIsBlock(processToModify.isBlock());
             this.viewManager.setIsSuspended(processToModify.isSuspend());
             if(!processToModify.isSuspend()){
                 this.viewManager.disableResumeButton();
             }
             this.viewManager.setIsResume(processToModify.isResume());
             this.viewManager.showModifyProcessDialog();
        }

    }

    private void modifyProcess(){
        Process processToModify = processManager.getProcessInQueue(viewManager.getIndexDataInTable());
        String modifyNameProcess = viewManager.getNameProcess();

        if(viewManager.getNameProcess().trim().equals("")){
            Utilities.showErrorDialog("El nombre del proceso está vacío. Ingrese algún valor", "Error");
        }
        else if(!processToModify.getName().equals(modifyNameProcess) && processManager.isAlreadyName(modifyNameProcess)){
            Utilities.showErrorDialog("Ya existe  un proceso con este nombre", "Error");
        }
        else if(processToModify.getTime().toString().equals("-1")){
            Utilities.showErrorDialog("El tiempo del proceso está vacío. Ingrese un valor numérico entero", "Error");
        }
        else {
            Process newProcess = new Process(viewManager.getNameProcess(), viewManager.getTimeProcess(), viewManager.getIsBlocked(), viewManager.getIsSuspended(), viewManager.getIsResume());
            this.processManager.updateProcessInQueue(newProcess, viewManager.getIndexDataInTable());
            this.viewManager.hideCreateAndModifyProcessDialog();
            this.viewManager.setValuesToTable(processManager.getListAsMatrixObject(processManager.getInQueue()), "Procesos Existentes");

        }

    }

    private void disableResumeButton(){
        this.viewManager.disableResumeButton();
    }

    private void enableResumeButton(){
        this.viewManager.enableResumeButton();
    }

    private void deleteProcess(){
        if(this.viewManager.getIndexDataInTable() == -1){
            Utilities.showErrorDialog("Debe seleccionar un proceso", "Error");
        }
        else {
            /* Lógica para eliminar un proceso */
            int confirmation = Utilities.showConfirmationWarning();
            if(confirmation == 0){
                this.processManager.deleteProcessFromInQueue(viewManager.getIndexDataInTable());
                this.viewManager.setValuesToTable(processManager.getListAsMatrixObject(processManager.getInQueue()), "Procesos Existentes");
            }

        }
    }

    private void changeToReportsMenu(){
        if(this.viewManager.getReadyProcessListLength() == 0){
            Utilities.showErrorDialog("Debe iniciar la simulación primero", "Error");
        }
        else {
            this.viewManager.setValuesToCurrentProcess();
            this.viewManager.changeToReportsMenu();
        }
        this.viewManager.changeToReportsMenu();

    }

    private void changeTableToReadyReports(){
        this.viewManager.setValuesToReadyReport();
    }

    private void changeToMainMenu(){
        this.viewManager.changeToMainMenu();
        this.viewManager.setValuesToTable(this.processManager.getListAsMatrixObject(this.processManager.getInQueue()), "Procesos Existentes");
    }

    private void openManual(){
        try{
            java.lang.Process p = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+"C:\\Users\\Usuario\\Desktop\\SO\\Software\\Renovar - ICETEX 2023-1.pdf");
        } catch (Exception e){
            System.out.println("El archivo no se puede abrir");
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c)) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        new Controller();
    }
}
