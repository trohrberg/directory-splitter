package de.tr82.directory.splitter.core;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Application extends JFrame {

    private JPanel contentPane;
    private JTextField txtSourceDir;
    private JTextField txtTargetDir;
    private JButton btnChooseSourceDir;
    private JButton btnChooseTargetDir;
    private JLabel lblHeader;
    private JLabel lblBucketNamePrefix;
    private JTextField txtBucketNamePrefix;
    private JLabel lblFirstBucketSize;
    private JTextField txtFirstBucketSize;
    private JComboBox cmbFirstBucketSizeUnit;
    private JLabel lblMaxBucketSize;
    private JTextField txtMaxBucketSize;
    private JComboBox cmbMaxBucketSizeUnit;
    private JButton btnClose;
    private JButton btnRun;
    private JCheckBox chkDryRun;
    private JProgressBar progressBar;
    private JTextArea textArea;
    private JLabel lblDryRun;

    /**
     * Create the frame.
     *
     * @throws UnsupportedLookAndFeelException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Application() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        setTitle("Directory Splitter");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
                Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        lblHeader = new JLabel("Fill in the details and hit 'Run ...'");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblHeader.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblHeader = new GridBagConstraints();
        gbc_lblHeader.gridwidth = 5;
        gbc_lblHeader.insets = new Insets(0, 0, 5, 0);
        gbc_lblHeader.gridx = 0;
        gbc_lblHeader.gridy = 0;
        contentPane.add(lblHeader, gbc_lblHeader);

        JLabel lblSourceDir = new JLabel("Source directory:");
        GridBagConstraints gbc_lblSourceDir = new GridBagConstraints();
        gbc_lblSourceDir.anchor = GridBagConstraints.EAST;
        gbc_lblSourceDir.insets = new Insets(0, 0, 5, 5);
        gbc_lblSourceDir.gridx = 0;
        gbc_lblSourceDir.gridy = 1;
        contentPane.add(lblSourceDir, gbc_lblSourceDir);

        txtSourceDir = new JTextField();
        txtSourceDir.setEditable(false);
        GridBagConstraints gbc_txtSourceDir = new GridBagConstraints();
        gbc_txtSourceDir.gridwidth = 3;
        gbc_txtSourceDir.insets = new Insets(0, 0, 5, 5);
        gbc_txtSourceDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtSourceDir.gridx = 1;
        gbc_txtSourceDir.gridy = 1;
        contentPane.add(txtSourceDir, gbc_txtSourceDir);
        txtSourceDir.setColumns(10);

        btnChooseSourceDir = new JButton("Choose ...");
        GridBagConstraints gbc_btnChooseSourceDir = new GridBagConstraints();
        gbc_btnChooseSourceDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnChooseSourceDir.insets = new Insets(0, 0, 5, 0);
        gbc_btnChooseSourceDir.gridx = 4;
        gbc_btnChooseSourceDir.gridy = 1;
        contentPane.add(btnChooseSourceDir, gbc_btnChooseSourceDir);
        btnChooseSourceDir.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(Application.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                txtSourceDir.setText(fileChooser.getSelectedFile().toString());
            }
        });

        JLabel lblTargetDir = new JLabel("Target directory:");
        GridBagConstraints gbc_lblTargetDir = new GridBagConstraints();
        gbc_lblTargetDir.anchor = GridBagConstraints.EAST;
        gbc_lblTargetDir.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetDir.gridx = 0;
        gbc_lblTargetDir.gridy = 2;
        contentPane.add(lblTargetDir, gbc_lblTargetDir);

        txtTargetDir = new JTextField();
        GridBagConstraints gbc_txtTargetDir = new GridBagConstraints();
        gbc_txtTargetDir.gridwidth = 3;
        gbc_txtTargetDir.insets = new Insets(0, 0, 5, 5);
        gbc_txtTargetDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtTargetDir.gridx = 1;
        gbc_txtTargetDir.gridy = 2;
        contentPane.add(txtTargetDir, gbc_txtTargetDir);
        txtTargetDir.setColumns(10);

        btnChooseTargetDir = new JButton("Choose ...");
        GridBagConstraints gbc_btnChooseTargetDir = new GridBagConstraints();
        gbc_btnChooseTargetDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnChooseTargetDir.insets = new Insets(0, 0, 5, 0);
        gbc_btnChooseTargetDir.gridx = 4;
        gbc_btnChooseTargetDir.gridy = 2;
        contentPane.add(btnChooseTargetDir, gbc_btnChooseTargetDir);
        btnChooseTargetDir.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(Application.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                txtTargetDir.setText(fileChooser.getSelectedFile().toString());
            }
        });

        lblBucketNamePrefix = new JLabel("Bucket name prefix:");
        GridBagConstraints gbc_lblBucketNamePrefix = new GridBagConstraints();
        gbc_lblBucketNamePrefix.anchor = GridBagConstraints.EAST;
        gbc_lblBucketNamePrefix.insets = new Insets(0, 0, 5, 5);
        gbc_lblBucketNamePrefix.gridx = 0;
        gbc_lblBucketNamePrefix.gridy = 3;
        contentPane.add(lblBucketNamePrefix, gbc_lblBucketNamePrefix);

        txtBucketNamePrefix = new JTextField();
        txtBucketNamePrefix.setText("RAWBLU_");
        GridBagConstraints gbc_txtBucketNamePrefix = new GridBagConstraints();
        gbc_txtBucketNamePrefix.gridwidth = 3;
        gbc_txtBucketNamePrefix.insets = new Insets(0, 0, 5, 5);
        gbc_txtBucketNamePrefix.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtBucketNamePrefix.gridx = 1;
        gbc_txtBucketNamePrefix.gridy = 3;
        contentPane.add(txtBucketNamePrefix, gbc_txtBucketNamePrefix);
        txtBucketNamePrefix.setColumns(10);

        lblFirstBucketSize = new JLabel("First bucket size:");
        GridBagConstraints gbc_lblFirstBucketSize = new GridBagConstraints();
        gbc_lblFirstBucketSize.anchor = GridBagConstraints.EAST;
        gbc_lblFirstBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_lblFirstBucketSize.gridx = 0;
        gbc_lblFirstBucketSize.gridy = 4;
        contentPane.add(lblFirstBucketSize, gbc_lblFirstBucketSize);

        txtFirstBucketSize = new JTextField();
        txtFirstBucketSize.setText("23");
        GridBagConstraints gbc_txtFirstBucketSize = new GridBagConstraints();
        gbc_txtFirstBucketSize.gridwidth = 3;
        gbc_txtFirstBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_txtFirstBucketSize.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtFirstBucketSize.gridx = 1;
        gbc_txtFirstBucketSize.gridy = 4;
        contentPane.add(txtFirstBucketSize, gbc_txtFirstBucketSize);
        txtFirstBucketSize.setColumns(10);

        cmbFirstBucketSizeUnit = new JComboBox();
        cmbFirstBucketSizeUnit
                .setModel(new DefaultComboBoxModel(new String[]{"Bytes", "KBytes", "MBytes", "GBytes"}));
        cmbFirstBucketSizeUnit.setSelectedIndex(3);
        GridBagConstraints gbc_cmbFirstBucketSizeUnit = new GridBagConstraints();
        gbc_cmbFirstBucketSizeUnit.insets = new Insets(0, 0, 5, 0);
        gbc_cmbFirstBucketSizeUnit.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbFirstBucketSizeUnit.gridx = 4;
        gbc_cmbFirstBucketSizeUnit.gridy = 4;
        contentPane.add(cmbFirstBucketSizeUnit, gbc_cmbFirstBucketSizeUnit);

        lblMaxBucketSize = new JLabel("Max bucket size:");
        GridBagConstraints gbc_lblMaxBucketSize = new GridBagConstraints();
        gbc_lblMaxBucketSize.anchor = GridBagConstraints.EAST;
        gbc_lblMaxBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_lblMaxBucketSize.gridx = 0;
        gbc_lblMaxBucketSize.gridy = 5;
        contentPane.add(lblMaxBucketSize, gbc_lblMaxBucketSize);

        txtMaxBucketSize = new JTextField();
        txtMaxBucketSize.setText("23");
        GridBagConstraints gbc_txtMaxBucketSize = new GridBagConstraints();
        gbc_txtMaxBucketSize.gridwidth = 3;
        gbc_txtMaxBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_txtMaxBucketSize.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtMaxBucketSize.gridx = 1;
        gbc_txtMaxBucketSize.gridy = 5;
        contentPane.add(txtMaxBucketSize, gbc_txtMaxBucketSize);
        txtMaxBucketSize.setColumns(10);

        cmbMaxBucketSizeUnit = new JComboBox();
        cmbMaxBucketSizeUnit.setModel(new DefaultComboBoxModel(new String[]{"Bytes", "KBytes", "MBytes", "GBytes"}));
        cmbMaxBucketSizeUnit.setSelectedIndex(3);
        GridBagConstraints gbc_cmbMaxBucketSizeUnit = new GridBagConstraints();
        gbc_cmbMaxBucketSizeUnit.insets = new Insets(0, 0, 5, 0);
        gbc_cmbMaxBucketSizeUnit.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbMaxBucketSizeUnit.gridx = 4;
        gbc_cmbMaxBucketSizeUnit.gridy = 5;
        contentPane.add(cmbMaxBucketSizeUnit, gbc_cmbMaxBucketSizeUnit);

        lblDryRun = new JLabel("Dry run:");
        GridBagConstraints gbc_lblDryRun = new GridBagConstraints();
        gbc_lblDryRun.insets = new Insets(0, 0, 5, 5);
        gbc_lblDryRun.gridx = 0;
        gbc_lblDryRun.gridy = 6;
        contentPane.add(lblDryRun, gbc_lblDryRun);

        chkDryRun = new JCheckBox("");
        chkDryRun.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_chkDryRun = new GridBagConstraints();
        gbc_chkDryRun.anchor = GridBagConstraints.WEST;
        gbc_chkDryRun.insets = new Insets(0, 0, 5, 5);
        gbc_chkDryRun.gridx = 1;
        gbc_chkDryRun.gridy = 6;
        contentPane.add(chkDryRun, gbc_chkDryRun);

        progressBar = new JProgressBar();
        GridBagConstraints gbc_progressBar = new GridBagConstraints();
        gbc_progressBar.fill = GridBagConstraints.BOTH;
        gbc_progressBar.insets = new Insets(0, 0, 5, 5);
        gbc_progressBar.gridwidth = 3;
        gbc_progressBar.gridx = 0;
        gbc_progressBar.gridy = 7;
        contentPane.add(progressBar, gbc_progressBar);

        btnRun = new JButton("Run ...");
        GridBagConstraints gbc_btnRun = new GridBagConstraints();
        gbc_btnRun.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRun.insets = new Insets(0, 0, 5, 5);
        gbc_btnRun.gridx = 3;
        gbc_btnRun.gridy = 7;
        contentPane.add(btnRun, gbc_btnRun);
        btnRun.addActionListener((ActionEvent e) -> {
            long firstBucketSize = determineBucketSize(txtFirstBucketSize.getText(), cmbFirstBucketSizeUnit.getSelectedItem());
            long maxBucketSize = determineBucketSize(txtMaxBucketSize.getText(), cmbMaxBucketSizeUnit.getSelectedItem());
            DirectorySplitter directorySplitter = new DirectorySplitter(txtSourceDir.getText(), txtTargetDir.getText(), txtBucketNamePrefix.getText(),
                    firstBucketSize, maxBucketSize, chkDryRun.isSelected(), new DirectorySplitter.OperationLogger() {
                @Override
                public void log(String message) {
                    textArea.append(message + "\n");
                }
            });
            try {
                directorySplitter.run();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnClose = new JButton("Close");
        GridBagConstraints gbc_btnClose = new GridBagConstraints();
        gbc_btnClose.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnClose.insets = new Insets(0, 0, 5, 0);
        gbc_btnClose.gridx = 4;
        gbc_btnClose.gridy = 7;
        contentPane.add(btnClose, gbc_btnClose);
        btnClose.addActionListener((ActionEvent e) -> {
            Application.this.dispatchEvent(new WindowEvent(Application.this, WindowEvent.WINDOW_CLOSING));
        });

        textArea = new JTextArea();

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.gridheight = 2;
        gbc_textArea.gridwidth = 5;
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 8;
        contentPane.add(scroll, gbc_textArea);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Application frame = new Application();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private long determineBucketSize(String bucketSize, Object bucketSizeUnit) {
        long result = Long.valueOf(bucketSize);
        if ("KBytes".equals(bucketSizeUnit)) {
            result *= 1024;
        }
        if ("MBytes".equals(bucketSizeUnit)) {
            result *= 1024 * 1024;
        }
        if ("GBytes".equals(bucketSizeUnit)) {
            result *= 1024 * 1024 * 1024;
        }

        return result;
    }
}
