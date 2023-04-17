package models;

import java.math.BigInteger;
import java.util.ArrayList;

public class ProcessManager {

    private final int PROCESS_TIME = 5;

    private ArrayList<Process> inQueue, ready, dispatch, expiration, execution, wait, block, endIOBlockReady,
                                suspendBlockToSuspendBlock, resumeSuspendBlockToBlock, suspendBlock, endIOSuspendBlockToSuspendReady,
                                suspendReady, resumeSuspendReadyToReady, suspendReadyToSuspendReady, suspendExecutionToSuspendReady, finished, currentList;
    public ProcessManager(){
        this.inQueue = new ArrayList<>();
        this.ready = new ArrayList<>();
        this.dispatch = new ArrayList<>();
        this.expiration = new ArrayList<>();
        this.execution = new ArrayList<>();
        this.wait = new ArrayList<>();
        this.block = new ArrayList<>();
        this.endIOBlockReady = new ArrayList<>();
        this.suspendBlockToSuspendBlock = new ArrayList<>();
        this.resumeSuspendBlockToBlock = new ArrayList<>();
        this.suspendBlock = new ArrayList<>();
        this.endIOSuspendBlockToSuspendReady = new ArrayList<>();
        this.suspendReady = new ArrayList<>();
        this.resumeSuspendReadyToReady = new ArrayList<>();
        this.suspendReadyToSuspendReady = new ArrayList<>();
        this.suspendExecutionToSuspendReady = new ArrayList<>();
        this.finished = new ArrayList<>();
        this.currentList = new ArrayList<>();
    }

    public void initSimulation(){
        this.copyToCurrentProcess();
        this.cleanAllLists();
        this.initLoadToReady();
        int i = 0;
        if(this.ready.size() > 0){
            boolean canContinue = true;
            while (canContinue) {
                this.loadToDispatchQueue(new Process(ready.get(i)));
                if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1){
                    this.loadToExecQueue(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
                }else{
                    this.loadToExecQueue(new Process(ready.get(i)));
                }
                if(!(ready.get(i).getTime().compareTo(BigInteger.valueOf(0)) == 0)){
                    //bloqueado
                    if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && ready.get(i).isBlock() && !ready.get(i).isResume() && !ready.get(i).isSuspend()){
                        this.loadBlockProcess(i);
                        //bloq susp reanud
                    }else if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && ready.get(i).isBlock() && ready.get(i).isResume() && ready.get(i).isSuspend()){
                        this.loadBlockSuspendResumeProcess(i);
                        //bloq susp
                    }else if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && ready.get(i).isBlock() && !ready.get(i).isResume() && ready.get(i).isSuspend()){
                        this.loadBlockSuspendProcess(i);
                        //bloq reanud
                    }else if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && ready.get(i).isBlock() && ready.get(i).isResume() && !ready.get(i).isSuspend()){
                        //this.loadBlockProcess(i);

                        // susp reanud
                    }else if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && !ready.get(i).isBlock() && ready.get(i).isResume() && ready.get(i).isSuspend()){
                        this.loadSuspendResumeProcess(i);
                        //susp
                    }else if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && !ready.get(i).isBlock() && !ready.get(i).isResume() && ready.get(i).isSuspend()){
                        this.loadSuspendResumeProcess(i);
                        //reanud
                    }else if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && !ready.get(i).isBlock() && ready.get(i).isResume() && !ready.get(i).isSuspend()){
                        //this.loadSuspendResumeProcess(i);

                        // no bloq no susp no reanud
                    }else if(ready.get(i).getTime().compareTo(BigInteger.valueOf(PROCESS_TIME)) == 1 && !ready.get(i).isBlock() && !ready.get(i).isResume() && !ready.get(i).isSuspend()){
                        this.loadToExpQueue(new Process(ready.get(i).getName(),BigInteger.valueOf(0), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
                        this.loadToReadyQueue(new Process(ready.get(i).getName(),BigInteger.valueOf(0), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
                    }else {
                        this.loadToFinishedQueue(new Process(ready.get(i).getName(),BigInteger.valueOf(0), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
                    }
                }else
                    this.loadToFinishedQueue(new Process(ready.get(i)));
                i++;
                if((ready.size() <= i))
                    canContinue = false;
            }
        }

    }

    private void loadBlockProcess(int i) {
        this.loadToWaitEvent(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToBlock(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToTerminateEventBlockList(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToReadyQueue(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
    }

    private void loadBlockSuspendResumeProcess(int i){
        this.loadToWaitEvent(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToBlock(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToSuspenQueueBlockSuspend(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToBlockSusp(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToResumeQueueBlockSusp(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToTerminateEventBlockList(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToReadyQueue(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
    }

    private void loadBlockSuspendProcess(int i){
        this.loadToWaitEvent(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToBlock(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToSuspenQueueBlockSuspend(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToBlockSusp(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToTerminateEventBlockListBlockSuspend(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loaToResumeQueueReadySusp(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToReadySusp(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToReadyQueue(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
    }

    private void loadSuspendResumeProcess(int i){
        this.loadToSuspendQueueReadySuspReady(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToSuspendQueueExecSuspReady(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToReadySusp(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loaToResumeQueueReadySusp(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
        this.loadToReadyQueue(new Process(ready.get(i).getName(), this.consumeTimeProcess(ready.get(i)), ready.get(i).isBlock(), ready.get(i).isSuspend(), ready.get(i).isResume()));
    }

    private BigInteger consumeTimeProcess(Process process) {
        return (process.getTime().subtract(BigInteger.valueOf(PROCESS_TIME)));
    }
    private void loadToReadyQueue(Process process) {
        this.ready.add(process);
        for (int i = 0; i < ready.size(); i++) {
            System.out.println(ready.get(i).getName() + "  " + ready.get(i).getTime() + "Listos");
        }
    }
    private void loadToDispatchQueue(Process process) {
        this.dispatch.add(process);
        for (int i = 0; i < dispatch.size(); i++) {
            System.out.println(dispatch.get(i).getName() + "  " + dispatch.get(i).getTime() + "Despachados");
        }
    }
    private void loadToExecQueue(Process process) {
        this.execution.add(process);
        for (int i = 0; i < execution.size(); i++) {
            System.out.println(execution.get(i).getName() + "  " + execution.get(i).getTime() + "Ejecución");
        }
    }

    private void loadToExpQueue(Process process) {
        this.expiration.add(process);
        for (int i = 0; i < expiration.size(); i++) {
            System.out.println(expiration.get(i).getName() + "  " + expiration.get(i).getTime() + "Expiración");
        }
    }

    private void loadToWaitEvent(Process process) {
        this.wait.add(process);
        for (int i = 0; i < wait.size(); i++) {
            System.out.println(wait.get(i).getName() + "  " + wait.get(i).getTime() + "Espera");
        }
    }

    private void loadToBlock(Process process) {
        this.block.add(process);
        for (int i = 0; i < block.size(); i++) {
            System.out.println(block.get(i).getName() + "  " + block.get(i).getTime() + "Bloqueados");
        }
    }

    private void loadToTerminateEventBlockList(Process process) {
        this.endIOBlockReady.add(process);
        for (int i = 0; i < endIOBlockReady.size(); i++) {
            System.out.println(endIOBlockReady.get(i).getName() + "  " + endIOBlockReady.get(i).getTime() + "Terminación bloqueado");
        }
    }

    private void loadToSuspenQueueBlockSuspend(Process process) {
        this.suspendBlockToSuspendBlock.add(process);
        for (int i = 0; i < suspendBlockToSuspendBlock.size(); i++) {
            System.out.println(suspendBlockToSuspendBlock.get(i).getName() + "  " + suspendBlockToSuspendBlock.get(i).getTime() + "suspender suspendido bloqueado");
        }
    }

    private void loadToResumeQueueBlockSusp(Process process) {
        this.resumeSuspendBlockToBlock.add(process);
        for (int i = 0; i < resumeSuspendBlockToBlock.size(); i++) {
            System.out.println(resumeSuspendBlockToBlock.get(i).getName() + "  " + resumeSuspendBlockToBlock.get(i).getTime() + " reanudar suspendido bloqueado");
        }
    }

    private void loadToBlockSusp(Process process) {
        this.suspendBlock.add(process);
        for (int i = 0; i < suspendBlock.size(); i++) {
            System.out.println(suspendBlock.get(i).getName() + "  " + suspendBlock.get(i).getTime() + "suspendido bloqueado");
        }
    }

    private void loadToTerminateEventBlockListBlockSuspend(Process process) {
        this.endIOSuspendBlockToSuspendReady.add(process);
        for (int i = 0; i < endIOSuspendBlockToSuspendReady.size(); i++) {
            System.out.println(endIOSuspendBlockToSuspendReady.get(i).getName() + "  " + endIOSuspendBlockToSuspendReady.get(i).getTime() + "Termiinación suspendido bloqueado susp listo");
        }
    }

    private void loadToReadySusp(Process process) {
        this.suspendReady.add(process);
        for (int i = 0; i < suspendReady.size(); i++) {
            System.out.println(suspendReady.get(i).getName() + "  " + suspendReady.get(i).getTime() + "susp listo");
        }
    }

    private void loaToResumeQueueReadySusp(Process process) {
        this.resumeSuspendReadyToReady.add(process);
        for (int i = 0; i < resumeSuspendReadyToReady.size(); i++) {
            System.out.println(resumeSuspendReadyToReady.get(i).getName() + "  " + resumeSuspendReadyToReady.get(i).getTime() + "reanudar susp listo");
        }
    }

    private void loadToSuspendQueueReadySuspReady(Process process) {
        this.suspendReadyToSuspendReady.add(process);
        for (int i = 0; i < suspendReadyToSuspendReady.size(); i++) {
            System.out.println(suspendReadyToSuspendReady.get(i).getName() + "  " + suspendReadyToSuspendReady.get(i).getTime() + "Suspender susp listo");
        }
    }

    private void loadToSuspendQueueExecSuspReady(Process process) {
        this.suspendExecutionToSuspendReady.add(process);
        for (int i = 0; i < suspendExecutionToSuspendReady.size(); i++) {
            System.out.println(suspendExecutionToSuspendReady.get(i).getName() + "  " + suspendExecutionToSuspendReady.get(i).getTime() + "Suspender ejecución susp listo");
        }
    }

    private void loadToFinishedQueue(Process process) {
        this.finished.add(process);
        for (int i = 0; i < finished.size(); i++) {
            System.out.println(finished.get(i).getName() + "  " + finished.get(i).getTime() + "Finalizar");
        }
    }

    private void cleanAllLists(){
        this.ready.clear();
        this.dispatch.clear();
        this.execution.clear();
        this.expiration.clear();
        this.wait.clear();
        this.block.clear();
        this.endIOBlockReady.clear();
        this.suspendBlockToSuspendBlock.clear();
        this.resumeSuspendBlockToBlock.clear();
        this.suspendBlock.clear();
        this.endIOSuspendBlockToSuspendReady.clear();
        this.suspendReady.clear();
        this.resumeSuspendReadyToReady.clear();
        this.suspendReadyToSuspendReady.clear();
        this.suspendExecutionToSuspendReady.clear();
        this.finished.clear();
    }

    private void initLoadToReady() {
        ready.addAll(inQueue);
        for (int i = 0; i < ready.size(); i++) {
            System.out.println(ready.get(i).getName() + "  " + ready.get(i).getTime() + "listo");
        }
    }

    public void copyToCurrentProcess(){
        currentList.addAll(inQueue);
    }
    public void cleanQueueList(){
        inQueue.clear();
    }

    public void addToInQueue(Process process){
        this.inQueue.add(process);
    }

    public boolean isAlreadyName(String nameProcess) {
        for (Process process : inQueue) {
            if (process.getName().equals(nameProcess))
                return true;
        }
        return false;
    }

    public Object[][] getListAsMatrixObject(ArrayList<Process> list){
        return this.parseArrayListToMatrixObject(list);
    }

    private Object[][] parseArrayListToMatrixObject(ArrayList<Process> list){
        int sizeQueue = list.size();
        Object[][] processList = new Object[sizeQueue][5];

        for(int i = 0; i < sizeQueue; i++){
            processList[i][0] = list.get(i).getName();
            processList[i][1] = list.get(i).getTime();
            processList[i][2] = list.get(i).isBlock();
            processList[i][3] = list.get(i).isSuspend();
            processList[i][4] = list.get(i).isResume();
        }

        return processList;
    }

    public void deleteProcessFromInQueue(int indexProcess) {
        inQueue.remove(indexProcess);
    }

    public Process getProcessInQueue(int indexProcess) {
        return inQueue.get(indexProcess);
    }

    public void updateProcessInQueue(Process process, int index) {
        inQueue.set(index, process);
    }





    public ArrayList<Process> getInQueue() {
        return inQueue;
    }

    public ArrayList<Process> getReady() {
        return ready;
    }

    public ArrayList<Process> getDispatch() {
        return dispatch;
    }

    public ArrayList<Process> getExpiration() {
        return expiration;
    }

    public ArrayList<Process> getExecution() {
        return execution;
    }

    public ArrayList<Process> getWait() {
        return wait;
    }

    public ArrayList<Process> getBlock() {
        return block;
    }

    public ArrayList<Process> getEndIOBlockReady() {
        return endIOBlockReady;
    }

    public ArrayList<Process> getSuspendBlockToSuspendBlock() {
        return suspendBlockToSuspendBlock;
    }

    public ArrayList<Process> getResumeSuspendBlockToBlock() {
        return resumeSuspendBlockToBlock;
    }

    public ArrayList<Process> getSuspendBlock() {
        return suspendBlock;
    }

    public ArrayList<Process> getEndIOSuspendBlockToSuspendReady() {
        return endIOSuspendBlockToSuspendReady;
    }

    public ArrayList<Process> getSuspendReady() {
        return suspendReady;
    }

    public ArrayList<Process> getResumeSuspendReadyToReady() {
        return resumeSuspendReadyToReady;
    }

    public ArrayList<Process> getSuspendReadyToSuspendReady() {
        return suspendReadyToSuspendReady;
    }

    public ArrayList<Process> getSuspendExecutionToSuspendReady() {
        return suspendExecutionToSuspendReady;
    }

    public ArrayList<Process> getFinished() {
        return finished;
    }
}
