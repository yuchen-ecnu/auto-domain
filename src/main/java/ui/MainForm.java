package ui;

import domain.ModifiedRecord;
import domain.RecordList;
import domain.RequestParams;
import utils.IpUtils;
import utils.TencentDnsAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Michael Chen
 * @date 2018-12-26 11:14
 */
public class MainForm implements ActionListener, ItemListener {

    private Timer timer = new Timer();

    private JPanel mainPanel;
    private JButton btnConfirm;
    private JTextField fieldSecretId;
    private JButton btnExit;
    private JTextField fieldSecretKey;
    private JTextField fieldRegion;
    private JTextField fieldDomain;
    private JComboBox comboFrequency;
    private JTextField fieldIP;
    private JLabel signatureMethod;
    private JTextField fieldRecord;
    private JTextField fieldSignatureMethod;
    private JTextField timeChecked;
    private JTextField timeUpdated;
    private JTextField fieldStatus;
    private JButton btnGetRecords;
    private JTextField fieldError;
    private JComboBox comboRecords;

    public static void main(String[] args) {
        JFrame frame = new JFrame("IP Tracker && Domain Auto Resolution");
        MainForm mainForm = new MainForm();
        frame.setContentPane(mainForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initView(mainForm);
        frame.pack();
        //设置frame居于屏幕中央方式1
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        frame.setLocation((int) (screenWidth - frame.getWidth()) / 2, (int) (screenHeight - frame.getHeight()) / 2);
        frame.setVisible(true);
    }

    private static void initView(MainForm mainForm) {
        mainForm.btnConfirm.addActionListener(mainForm);
        mainForm.btnExit.addActionListener(mainForm);
        mainForm.btnGetRecords.addActionListener(mainForm);
        mainForm.comboRecords.addItemListener(mainForm);
    }

    public RequestParams getData() {
        RequestParams data = new RequestParams();
        data.setSecretId(fieldSecretId.getText());
        data.setRegion(fieldRegion.getText());
        data.setSignatureMethod(fieldSignatureMethod.getText());
        data.setSecretKey(fieldSecretKey.getText());
        data.setDomain(fieldDomain.getText());
        data.setIp(fieldIP.getText());
        data.setRecord(fieldRecord.getText());
        return data;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Start":
                if (!checkData()) {
                    JOptionPane.showMessageDialog(null, "Please input all field above.", "Empty Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (fieldRecord.getText().contains("Auto")) {
                    JOptionPane.showMessageDialog(null, "Please update and choose record.", "Record Not Choose", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                lockInput();
                btnConfirm.setText("Stop");
                initTimer((RecordList.Record) comboRecords.getSelectedItem());

                break;
            case "Exit":
                System.exit(0);
                break;
            case "Stop":
                timer.cancel();
                btnConfirm.setText("Start");
                unlockInput();
                clearError();
                break;
            case "Get Records":
                if (!checkData()) {
                    JOptionPane.showMessageDialog(null, "Please input all field above.", "Empty Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                RequestParams data = getData();
                TencentDnsAPI api = new TencentDnsAPI(data.getSecretId(), data.getSecretKey(), data.getSignatureMethod());
                List<RecordList.Record> list = api.getRecordList(data.getRegion(), data.getDomain());
                comboRecords.removeAllItems();
                list.forEach(comboRecords::addItem);
                break;
            default:
                break;
        }
    }

    /**
     * 锁定用户输入
     */
    private void lockInput() {
        fieldSecretKey.setEditable(false);
        fieldSecretId.setEditable(false);
        fieldDomain.setEditable(false);
        fieldRegion.setEditable(false);
        comboFrequency.setEnabled(false);
        comboRecords.setEnabled(false);
    }

    /**
     * 解锁用户输入
     */
    private void unlockInput() {
        fieldSecretKey.setEditable(true);
        fieldSecretId.setEditable(true);
        fieldDomain.setEditable(true);
        fieldRegion.setEditable(true);
        comboFrequency.setEnabled(true);
        comboRecords.setEnabled(true);
    }

    private void initTimer(RecordList.Record record) {
        timer.schedule(new TimerTask() {
            private String remoteIP = record.value;

            @Override
            public void run() {
                String ip;
                try {
                    ip = IpUtils.getIp();
                } catch (Exception e) {
                    showError("获取IP错误！");
                    return;
                }
                fieldIP.setText(ip);
                timeChecked.setText(new Timestamp(System.currentTimeMillis()).toString());
                if (remoteIP.equals(ip)) {
                    return;
                }
                RequestParams data = getData();
                TencentDnsAPI api = new TencentDnsAPI(data.getSecretId(), data.getSecretKey(), data.getSignatureMethod());
                ModifiedRecord result = api.modifyRecord(data.getRegion(),data.getDomain(), data.getRecord(), "www", record.type, record.line, ip);
                if (result.code != 0) {
                    //error
                    showError("Modified Record Error!:" + result.message);
                } else {
                    timeUpdated.setText(new Timestamp(System.currentTimeMillis()).toString());
                }

            }
        }, 0, getPeriod());
    }

    private long getPeriod() {
        String time = String.valueOf(comboFrequency.getSelectedItem());
        switch (time) {
            case "30 seconds":
                return 30000;
            case "1 min":
                return 60000;
            case "5 min":
                return 300000;
            case "10 min":
                return 600000;
            default:
                return 300000;
        }
    }

    private void showError(String msg) {
        fieldError.setText(msg);
        fieldStatus.setText("ERROR");
        fieldStatus.setForeground(new Color(255, 0, 0));
        fieldError.setForeground(new Color(255, 0, 0));
    }

    private void clearError() {
        fieldError.setText("None");
        fieldStatus.setText("OK");
        fieldStatus.setForeground(new Color(102, 205, 0));
        fieldError.setForeground(new java.awt.Color(187, 187, 187));
    }

    private boolean checkData() {
        RequestParams data = getData();
        return !"".equals(data.getSecretId()) && !"".equals(data.getSecretKey()) && !"".equals(data.getDomain()) && !"".equals(data.getRegion());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            RecordList.Record record = (RecordList.Record) e.getItem();
            fieldRecord.setText(String.valueOf(record.id));
        }
    }

    public void setData(RequestParams data) {
        fieldSecretId.setText(data.getSecretId());
        fieldRegion.setText(data.getRegion());
        fieldSignatureMethod.setText(data.getSignatureMethod());
        fieldSecretKey.setText(data.getSecretKey());
        fieldDomain.setText(data.getDomain());
        fieldIP.setText(data.getIp());
        fieldRecord.setText(data.getRecord());
        timeChecked.setText(data.getTimeChecked());
        timeUpdated.setText(data.getTimeUpdated());
        fieldStatus.setText(data.getStatus());
        fieldError.setText(data.getErrorMsg());
    }

    public void getData(RequestParams data) {
        data.setSecretId(fieldSecretId.getText());
        data.setRegion(fieldRegion.getText());
        data.setSignatureMethod(fieldSignatureMethod.getText());
        data.setSecretKey(fieldSecretKey.getText());
        data.setDomain(fieldDomain.getText());
        data.setIp(fieldIP.getText());
        data.setRecord(fieldRecord.getText());
        data.setTimeChecked(timeChecked.getText());
        data.setTimeUpdated(timeUpdated.getText());
        data.setStatus(fieldStatus.getText());
        data.setErrorMsg(fieldError.getText());
    }
}
