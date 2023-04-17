package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.math.BigInteger;

public class ViewManager extends JFrame {

    private PanelMenu panelMenu;
    private PanelTable panelTable;
    private DialogCreateProcess dialogCreateProcess;
    private PanelMenuReport panelMenuReport;

    private Object[][] inQueue, readyProcess, dispatch, expiration, execution, wait, block, endIOBlockReady,
    suspendBlockToSuspendBlock, resumeSuspendBlockToBlock, suspendBlock, endIOSuspendBlockToSuspendReady,
    suspendReady, resumeSuspendReadyToReady, suspendReadyToSuspendReady, suspendExecutionToSuspendReady, finished, currentProcess;

    public ViewManager(ActionListener actionListener, KeyListener keyListener){
        this.setLayout(new BorderLayout());
        this.setTitle("Tercer Software");
        this.setFont(ConstantsGUI.MAIN_MENU);
        this.setSize(700, 500);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.getContentPane().setBackground(Color.decode("#f2e9e4"));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.initComponents(actionListener, keyListener);
        this.setVisible(true);
    }

    private void initComponents(ActionListener actionListener, KeyListener keyListener){
        this.panelMenu = new PanelMenu(actionListener);
        this.add(panelMenu, BorderLayout.WEST);

        this.panelTable = new PanelTable();
        this.add(panelTable, BorderLayout.CENTER);

        this.dialogCreateProcess = new DialogCreateProcess(actionListener, keyListener);
        this.panelMenuReport = new PanelMenuReport(actionListener);

        this.inQueue = new Object[0][0];
        this.readyProcess = new Object[0][0];

    }


    public void showCreateProcessDialog() {
        this.dialogCreateProcess.changeButtonToCreate();
        this.dialogCreateProcess.setVisible(true);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void showModifyProcessDialog(){
        this.dialogCreateProcess.changeButtonToModify();
        this.dialogCreateProcess.setVisible(true);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void hideCreateAndModifyProcessDialog(){
        this.dialogCreateProcess.setVisible(false);
        this.dialogCreateProcess.cleanAllFields();
        SwingUtilities.updateComponentTreeUI(this);
    }

    public int getIndexDataInTable(){
        return this.panelTable.getIndexDataProcess();
    }

    public void setNameProcess(String nameProcess){
        this.dialogCreateProcess.setNameProcess(nameProcess);
    }
    public String getNameProcess(){
        return this.dialogCreateProcess.getNameProcess();
    }


    public void setTimeProcess(BigInteger timeProcess){
        this.dialogCreateProcess.setTimeProcess(timeProcess);
    }
    public BigInteger getTimeProcess(){
        return this.dialogCreateProcess.getTimeProcess();
    }

    public void setIsBlock(boolean isBlock){
        this.dialogCreateProcess.setIsBlock(isBlock);
    }
    public boolean getIsBlocked() {
        return this.dialogCreateProcess.getIsBlocked();
    }

    public void setIsSuspended(boolean isSuspended){
        this.dialogCreateProcess.setIsSuspended(isSuspended);
    }

    public boolean getIsSuspended(){
        return this.dialogCreateProcess.getIsSuspended();
    }


    public void setIsResume(boolean isResume){
        this.dialogCreateProcess.setIsResume(isResume);
    }
    public boolean getIsResume(){
        return this.dialogCreateProcess.getIsResume();
    }

    public int getReadyProcessListLength(){
        return this.readyProcess.length;
    }

    public void changeToReportsMenu(){
        this.remove(panelMenu);
        this.add(panelMenuReport, BorderLayout.WEST);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void changeToMainMenu(){
        this.remove(panelMenuReport);
        this.add(panelMenu, BorderLayout.WEST);

        SwingUtilities.updateComponentTreeUI(this);
    }


    public void setValuesToTable(Object[][] list, String title) {
        Object[][] newQueueList =  this.parseValuesIsBlockAndIsSuspended(list);
        DefaultTableModel defaultTableModel = new DefaultTableModel(newQueueList, ConstantsGUI.HEADERS);
        this.panelTable.changeTitle(title);
        this.panelTable.setTableProcess(defaultTableModel);
    }

    private Object[][] parseValuesIsBlockAndIsSuspended(Object[][] queueList){
        int size = queueList.length;
        for(int i = 0; i < size; i++){
            queueList[i][2] = queueList[i][2].equals(true) ? "Sí" : "No";
            queueList[i][3] = queueList[i][3].equals(true) ? "Sí" : "No";
            queueList[i][4] = queueList[i][4].equals(true) ? "Sí" : "No";

        }
        return queueList;
    }

    public void setValuesToCurrentProcess(){
        this.setValuesToTable(this.inQueue, "Procesos Existentes");
    }

    public void setValuesToCurrentReport(){
        this.setValuesToTable(this.inQueue, "Procesos Actuales");
    }

    public void setValuesToReadyReport(){
        this.setValuesToTable(this.readyProcess, "Procesos Listos");
    }

    public void setValuesToDispatchReport(){
        this.setValuesToTable(this.dispatch, "Procesos Despachados");
    }

    public void setValuesToExecReport(){
        this.setValuesToTable(this.execution, "Procesos en Ejecución");
    }

    public void setValuesToExepReport(){
        this.setValuesToTable(this.expiration, "Procesos Expirados");
    }

    public void setValuesToWaitReport(){
        this.setValuesToTable(this.wait, "Procesos en Espera");
    }

    public void setValuesToBlockReport(){
        this.setValuesToTable(this.block, "Procesos Bloqueados");
    }

    public void setValuesToEndBlockReport(){
        this.setValuesToTable(this.endIOBlockReady, "Procesos Terminados");
    }

    public void setValuesToSupBlockSuspReport(){
        this.setValuesToTable(this.suspendBlockToSuspendBlock, "Procesos Suspendidos");
    }

    public void setValuesToResumeBlockSuspReport(){
        this.setValuesToTable(this.resumeSuspendBlockToBlock, "Procesos reanudados");
    }

    public void setValuesToBlockSuspReport(){
        this.setValuesToTable(this.suspendBlock, "Procesos Bloqueado-Suspendido");
    }

    public void setValuesToEndSuspReadyReport(){
        this.setValuesToTable(this.endIOSuspendBlockToSuspendReady, "Procesos Term. Susp.Bloq-Sus.List");
    }

    public void setValuesToResumeSuspReadyReport(){
        this.setValuesToTable(this.resumeSuspendReadyToReady, "Procesos Reanudados");
    }

    public void setValuesToSuspListSuspReport(){
        this.setValuesToTable(this.suspendReadyToSuspendReady, "Procesos Suspendidos-Listos");
    }

    public void setValuesToSuspExecSuspReport(){
        this.setValuesToTable(this.suspendExecutionToSuspendReady, "Procesos Suspendidos-Ejecución");
    }

    public void setValuesToFinishReport(){
        this.setValuesToTable(this.finished, "Procesos FInalizados");
    }

    public void setInQueue(Object[][] inQueue) {
        this.inQueue = inQueue;
    }

    public void setReadyProcess(Object[][] readyProcess) {
        this.readyProcess = readyProcess;
    }

    public void setDispatch(Object[][] dispatch) {
        this.dispatch = dispatch;
    }

    public void setExpiration(Object[][] expiration) {
        this.expiration = expiration;
    }

    public void setExecution(Object[][] execution) {
        this.execution = execution;
    }

    public void setWait(Object[][] wait) {
        this.wait = wait;
    }

    public void setBlock(Object[][] block) {
        this.block = block;
    }

    public void setEndIOBlockReady(Object[][] endIOBlockReady) {
        this.endIOBlockReady = endIOBlockReady;
    }

    public void setSuspendBlockToSuspendBlock(Object[][] suspendBlockToSuspendBlock) {
        this.suspendBlockToSuspendBlock = suspendBlockToSuspendBlock;
    }

    public void setResumeSuspendBlockToBlock(Object[][] resumeSuspendBlockToBlock) {
        this.resumeSuspendBlockToBlock = resumeSuspendBlockToBlock;
    }

    public void setSuspendBlock(Object[][] suspendBlock) {
        this.suspendBlock = suspendBlock;
    }

    public void setEndIOSuspendBlockToSuspendReady(Object[][] endIOSuspendBlockToSuspendReady) {
        this.endIOSuspendBlockToSuspendReady = endIOSuspendBlockToSuspendReady;
    }

    public void setSuspendReady(Object[][] suspendReady) {
        this.suspendReady = suspendReady;
    }

    public void setResumeSuspendReadyToReady(Object[][] resumeSuspendReadyToReady) {
        this.resumeSuspendReadyToReady = resumeSuspendReadyToReady;
    }

    public void setSuspendReadyToSuspendReady(Object[][] suspendReadyToSuspendReady) {
        this.suspendReadyToSuspendReady = suspendReadyToSuspendReady;
    }

    public void setSuspendExecutionToSuspendReady(Object[][] suspendExecutionToSuspendReady) {
        this.suspendExecutionToSuspendReady = suspendExecutionToSuspendReady;
    }

    public void setFinished(Object[][] finished) {
        this.finished = finished;
    }

    public void disableResumeButton() {
        this.dialogCreateProcess.disableResumeButtons();
    }

    public void enableResumeButton(){
        this.dialogCreateProcess.enableResumeButtons();
    }
}
