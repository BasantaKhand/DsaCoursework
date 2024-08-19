import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileConverterGUI{
    private JFrame frame;
    private JFileChooser fileChooser;
    private JList<String> fileList;
    private JProgressBar overallProgressBar;
    private DefaultListModel<String> fileListModel;
    private JLabel statusLabel;
    private JButton startButton, cancelButton;
    private JComboBox<String> conversionOptions;
    private ExecutorService executorService;

    public FileConverterGUI() {
        // Initialize GUI components
        frame = new JFrame("File Converter");
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        overallProgressBar = new JProgressBar(0, 100);
        statusLabel = new JLabel("Select files and choose conversion options.");

        startButton = new JButton("Start Conversion");
        cancelButton = new JButton("Cancel");

        conversionOptions = new JComboBox<>(new String[]{"PDF to Docx", "Image Resize"});

        // Layout the GUI
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Conversion Type:"));
        topPanel.add(conversionOptions);
        topPanel.add(startButton);
        topPanel.add(cancelButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(fileList), BorderLayout.CENTER);
        panel.add(overallProgressBar, BorderLayout.SOUTH);
        panel.add(statusLabel, BorderLayout.PAGE_END);

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Event handlers
        startButton.addActionListener(new StartButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());
        fileChooser.addActionListener(new FileChooserListener());

        executorService = Executors.newFixedThreadPool(5); // Thread pool for conversions
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                for (File file : selectedFiles) {
                    fileListModel.addElement(file.getAbsolutePath());
                }
                startConversions(selectedFiles);
            }
        }
    }

    private void startConversions(File[] files) {
        overallProgressBar.setValue(0);
        statusLabel.setText("Starting conversion...");

        for (File file : files) {
            FileConversionTask task = new FileConversionTask(file, (String) conversionOptions.getSelectedItem());
            task.execute(); // Executes the task asynchronously
        }
    }

    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            executorService.shutdownNow(); // Attempt to cancel all tasks
            statusLabel.setText("Conversion cancelled.");
        }
    }

    private class FileChooserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                for (File file : selectedFiles) {
                    fileListModel.addElement(file.getAbsolutePath());
                }
            }
        }
    }

    private class FileConversionTask extends SwingWorker<Void, String> {
        private File file;
        private String conversionType;

        public FileConversionTask(File file, String conversionType) {
            this.file = file;
            this.conversionType = conversionType;
        }

        @Override
        protected Void doInBackground() {
            try {
                // Simulate file conversion process
                int progress = 0;
                for (int i = 0; i <= 100; i += 10) {
                    if (isCancelled()) {
                        statusLabel.setText("Conversion cancelled for: " + file.getName());
                        break;
                    }
                    Thread.sleep(100); // Simulate time-consuming task
                    progress = i;
                    setProgress(progress);
                    publish("Converting " + file.getName() + " (" + progress + "%)");
                }
                if (!isCancelled()) {
                    publish("Conversion complete: " + file.getName());
                }
            } catch (CancellationException | InterruptedException ex) {
                statusLabel.setText("Error during conversion of: " + file.getName());
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            // Update the status label with the latest status message
            String latestMessage = chunks.get(chunks.size() - 1);
            statusLabel.setText(latestMessage);
        }

        @Override
        protected void done() {
            // Update the overall progress bar
            int completedTasks = fileListModel.getSize();
            overallProgressBar.setValue((int) ((getProgress() / 100.0) * completedTasks));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileConverterGUI::new);
    }
}
