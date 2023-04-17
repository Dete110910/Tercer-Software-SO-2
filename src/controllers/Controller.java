package controllers;

import models.Process;
import models.ProcessManager;
import persistence.PersistenceManager;
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
            case "ReporteActuales":
                this.changeTableToCurrentReports(); //pendiente
                break;
            case "ReporteListos":
                this.changeTableToReadyReports();
                break;
            case "ReporteDespachados":
                this.changeTableToDispatchReports();
                break;
            case "ReporteEjecucion":
                this.setValuesToExecReport();
                break;
            case "ReporteExpirados":
                this.setValuesToExepReport();
                break;
            case "ReporteEspera":
                this.setValuesToWaitReport();
                break;
            case "ReporteBloqueados":
                this.setValuesToBlockReport();
                break;
            case "ReporteTerminacionBloqueadoAListo":
                this.setValuesToEndSuspReport();
                break;
            case "ReporteSuspendidoBloqASusp":
                this.setValuesToSupBlockSuspReport();
                break;
            case "ReporteReanudarSuspABloq":
                this.setValuesToResumeBlockSuspReport();
                break;
            case "ReporteSuspBloq":
                this.setValuesToBlockSuspReport();
                break;
            case "ReporteTermSusBloqASusList":
                this.setValuesToEndBlockReadyReport();
                break;
            case "SuspendidoListo":
                this.setValuesToSuspReadyReport();
                break;
            case "ReporteReanSusLisAList":
                this.setValuesToResumeSuspReadyReport();
                break;
            case "ReporteSuspListASuspList":
                this.setValuesToSuspListSuspReport();
                break;
            case "ReporteSuspEjeASuspList":
                this.setValuesToSuspExecSuspReport();
                break;
            case "ReporteFinalizados":
                this.setValuesToFinishReport();
                break;
            case "Enviar":
                this.initSimulation();
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

    private void initSimulation(){
        int response = Utilities.showConfirmationWarning();
        if(response == 0){
            processManager.initSimulation();
            Utilities.showDoneCPUProcess();
            this.saveReports();
            processManager.copyToCurrentProcess();
           //processManager.cleanQueueList();
            this.cleanMainTableProcess();
            this.loadReportList();
        }
    }

    private void cleanMainTableProcess(){
        this.viewManager.setValuesToTable(processManager.getListAsMatrixObject(processManager.getInQueue()), "Procesos Actuales");
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

    }

    private void changeTableToCurrentReports(){
        this.viewManager.setValuesToCurrentReport();
    }

    private void changeTableToReadyReports(){
        this.viewManager.setValuesToReadyReport();
    }

    private void changeTableToDispatchReports(){
        this.viewManager.setValuesToDispatchReport();
    }

    public void setValuesToExecReport(){
        this.viewManager.setValuesToExecReport();
    }

    public void setValuesToExepReport(){
        this.viewManager.setValuesToExepReport();
    }

    public void setValuesToWaitReport(){
        this.viewManager.setValuesToWaitReport();
    }

    public void setValuesToBlockReport(){
        this.viewManager.setValuesToBlockReport();
    }

    public void setValuesToEndSuspReport(){
        this.viewManager.setValuesToEndBlockReport();
    }

    public void setValuesToSupBlockSuspReport(){
       this.viewManager.setValuesToSupBlockSuspReport();
    }

    public void setValuesToResumeBlockSuspReport(){
        this.viewManager.setValuesToResumeBlockSuspReport();
    }

    public void setValuesToBlockSuspReport(){
        this.viewManager.setValuesToBlockSuspReport();
    }

    public void setValuesToEndBlockReadyReport(){
        this.viewManager.setValuesToEndSuspReadyReport();
    }

    public void setValuesToSuspReadyReport(){
        this.viewManager.setValuesToSuspendReady();
    }

    public void setValuesToResumeSuspReadyReport(){
        this.viewManager.setValuesToResumeSuspReadyReport();
    }

    public void setValuesToSuspListSuspReport(){
        this.viewManager.setValuesToSuspListSuspReport();
    }

    public void setValuesToSuspExecSuspReport(){
        this.viewManager.setValuesToSuspExecSuspReport();
    }

    public void setValuesToFinishReport(){
        this.viewManager.setValuesToFinishReport();
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

    private void loadReportList(){
        viewManager.setInQueue(processManager.getListAsMatrixObject(processManager.getInQueue()));
        viewManager.setReadyProcess(processManager.getListAsMatrixObject(processManager.getReady()));
        viewManager.setDispatch(processManager.getListAsMatrixObject(processManager.getDispatch()));
        viewManager.setExecution(processManager.getListAsMatrixObject(processManager.getExecution()));
        viewManager.setExpiration(processManager.getListAsMatrixObject(processManager.getExpiration()));
        viewManager.setWait(processManager.getListAsMatrixObject(processManager.getWait()));
        viewManager.setBlock(processManager.getListAsMatrixObject(processManager.getBlock()));
        viewManager.setEndIOBlockReady(processManager.getListAsMatrixObject(processManager.getEndIOBlockReady()));
        viewManager.setSuspendBlockToSuspendBlock(processManager.getListAsMatrixObject(processManager.getSuspendBlockToSuspendBlock()));
        viewManager.setResumeSuspendBlockToBlock(processManager.getListAsMatrixObject(processManager.getResumeSuspendBlockToBlock()));
        viewManager.setSuspendBlock(processManager.getListAsMatrixObject(processManager.getSuspendBlock()));
        viewManager.setEndIOSuspendBlockToSuspendReady(processManager.getListAsMatrixObject(processManager.getEndIOSuspendBlockToSuspendReady()));
        viewManager.setResumeSuspendReadyToReady(processManager.getListAsMatrixObject(processManager.getResumeSuspendReadyToReady()));
        viewManager.setSuspendReadyToSuspendReady(processManager.getListAsMatrixObject(processManager.getSuspendReadyToSuspendReady()));
        viewManager.setSuspendExecutionToSuspendReady(processManager.getListAsMatrixObject(processManager.getSuspendExecutionToSuspendReady()));
        viewManager.setSuspendReady(processManager.getListAsMatrixObject(processManager.getSuspendReady()));
        viewManager.setFinished(processManager.getListAsMatrixObject(processManager.getFinished()));
    }

    private void saveReports(){
        PersistenceManager.saveReport("Actuales.txt", processManager.getInQueue());
        PersistenceManager.saveReport("Listos.txt", processManager.getReady());
        PersistenceManager.saveReport("Despachados.txt", processManager.getDispatch());
        PersistenceManager.saveReport("Ejecución.txt", processManager.getExecution());
        PersistenceManager.saveReport("Expirados.txt", processManager.getExpiration());
        PersistenceManager.saveReport("Espera.txt", processManager.getWait());
        PersistenceManager.saveReport("Bloqueados.txt", processManager.getBlock());
        PersistenceManager.saveReport("ReporteTerminacionBloqueadoAListo.txt", processManager.getEndIOBlockReady());
        PersistenceManager.saveReport("ReporteSuspendidoBloqASusp.txt", processManager.getSuspendBlockToSuspendBlock());
        PersistenceManager.saveReport("ReporteReanudarSuspABloq.txt", processManager.getResumeSuspendBlockToBlock());
        PersistenceManager.saveReport("ReporteSuspBloq.txt", processManager.getSuspendBlock());
        PersistenceManager.saveReport("ReporteTermSusBloqASusList.txt", processManager.getEndIOSuspendBlockToSuspendReady());
        PersistenceManager.saveReport("SuspendidoListo.txt", processManager.getResumeSuspendReadyToReady());
        PersistenceManager.saveReport("ReporteReanSusLisAList.txt", processManager.getSuspendReadyToSuspendReady());
        PersistenceManager.saveReport("ReporteSuspListo-susp.Listo.txt", processManager.getSuspendExecutionToSuspendReady());
        PersistenceManager.saveReport("ReporteSuspEjec-susp.Listo.txt.txt", processManager.getSuspendReady());
        PersistenceManager.saveReport("ReporteFinalizados.txt", processManager.getFinished());
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
