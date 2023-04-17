package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelMenuReport extends JPanel {

    private Button existingProcesses, readyReport, dispatchedReport, executionReport, expirationReport, waitReport,
            blockReport, endIOBlockReady, suspendBlockToSuspendBlock, resumeSuspendBlockToBlock, suspendBlock,
            endIOSuspendBlockToSuspendReady, suspendReady,  resumeSuspendReadyToReady,
            suspendReadyToSuspendReady, suspendExecutionToSuspendReady,
            finishedReport, destroyedReport, suspendedReport, restartedReport, back, related;


    private JLabel titleMenuReports;

    public PanelMenuReport(ActionListener listener){
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode("#4a4e69"));
        this.setPreferredSize(new Dimension(380,80));
        this.setMaximumSize(new Dimension(380,80));
        this.setMinimumSize(new Dimension(380,80));
        initComponents(listener);
        this.setVisible(true);
    }

    private void initComponents(ActionListener actionListener){
        titleMenuReports = new JLabel("Reportes");
        titleMenuReports.setForeground(Color.WHITE);
        titleMenuReports.setFont(ConstantsGUI.FONT_MENU_TITLE);
        addComponent(titleMenuReports, 0, 1);

        existingProcesses = new Button("Procesos actuales");
        existingProcesses.addActionListener(actionListener);
        existingProcesses.setActionCommand("ReporteActuales");
        addComponent(existingProcesses, 0, 2);

        readyReport = new Button("Listos");
        readyReport.addActionListener(actionListener);
        readyReport.setActionCommand("ReporteListos");
        addComponent(readyReport, 0, 3);

        dispatchedReport = new Button("Despachados");
        dispatchedReport.addActionListener(actionListener);
        dispatchedReport.setActionCommand("ReporteDespachados");
        addComponent(dispatchedReport, 0, 4);

        executionReport = new Button("Ejecución");
        executionReport.addActionListener(actionListener);
        executionReport.setActionCommand("ReporteEjecucion");
        addComponent(executionReport, 0, 5);

        expirationReport = new Button("Expirados");
        expirationReport.addActionListener(actionListener);
        expirationReport.setActionCommand("ReporteExpirados");
        addComponent(expirationReport, 0, 6);

        this.waitReport = new Button("Espera E/S");
        this.waitReport.addActionListener(actionListener);
        this.waitReport.setActionCommand("ReporteEspera");
        this.addComponent(this.waitReport, 0, 7);

        blockReport = new Button("Bloqueados");
        blockReport.addActionListener(actionListener);
        blockReport.setActionCommand("ReporteBloqueados");
        this.addComponent(this.blockReport, 0, 8);

        this.endIOBlockReady = new Button("Term. Bloq-List");
        this.endIOBlockReady.addActionListener(actionListener);
        this.endIOBlockReady.setActionCommand("ReporteTerminacionBloqueadoAListo");
        this.addComponent(endIOBlockReady, 0, 9);

        this.suspendBlockToSuspendBlock = new Button("Susp. Bloq-Susp");
        this.suspendBlockToSuspendBlock.addActionListener(actionListener);
        this.suspendBlockToSuspendBlock.setActionCommand("ReporteSuspendidoBloqASusp");
        this.addComponent(this.suspendBlockToSuspendBlock, 0, 10);

        this.resumeSuspendBlockToBlock = new Button("Rean. Susp-Bloq.");
        this.resumeSuspendBlockToBlock.addActionListener(actionListener);
        this.resumeSuspendBlockToBlock.setActionCommand("ReporteReanudarSuspABloq");
        this.addComponent(this.resumeSuspendBlockToBlock, 0, 11);

        this.suspendBlock = new Button("Susp-Bloq");
        this.suspendBlock.addActionListener(actionListener);
        this.suspendBlock.setActionCommand("ReporteSuspBloq");
        this.addComponent(this.suspendBlock, 0, 12);

        this.endIOSuspendBlockToSuspendReady = new Button("Term. Susp.Bloq-Sus.List");
        this.endIOSuspendBlockToSuspendReady.addActionListener(actionListener);
        this.endIOSuspendBlockToSuspendReady.setActionCommand("ReporteTermSusBloqASusList");
        this.addComponent(this.endIOSuspendBlockToSuspendReady, 0, 13);

        this.suspendReady = new Button("Susp-List");
        this.suspendReady.addActionListener(actionListener);
        this.suspendReady.setActionCommand("SuspendidoListo");
        this.addComponent(this.suspendReady, 0,14);

        this.resumeSuspendReadyToReady = new Button("Rean. Susp-List");
        this.resumeSuspendReadyToReady.addActionListener(actionListener);
        this.resumeSuspendReadyToReady.setActionCommand("ReporteReanSusLisAList");
        this.addComponent(this.resumeSuspendReadyToReady, 0, 15);

        this.suspendReadyToSuspendReady = new Button("Susp. List-Susp.List");
        this.suspendReadyToSuspendReady.addActionListener(actionListener);
        this.suspendReadyToSuspendReady.setActionCommand("ReporteSuspListASuspList");
        this.addComponent(this.suspendReadyToSuspendReady, 0, 16);

        this.suspendExecutionToSuspendReady = new Button("Susp. Eje-Susp.List");
        this.suspendExecutionToSuspendReady.addActionListener(actionListener);
        this.suspendExecutionToSuspendReady.setActionCommand("ReporteSuspEjeASuspList");
        this.addComponent(this.suspendExecutionToSuspendReady, 0, 17);

        finishedReport = new Button("Finalizados");
        finishedReport.addActionListener(actionListener);
        finishedReport.setActionCommand("ReporteFinalizados");
        addComponent(finishedReport, 0, 18);

        back = new Button("Atrás");
        back.addActionListener(actionListener);
        back.setActionCommand("Atras");
        addComponent(back, 0, 19);

    }

    public void addComponent(JComponent component, int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        this.add(component, gbc);
    }

}
