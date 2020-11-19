package ui;

import lombok.NonNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import utils.Adapter;
import utils.Adapter.AccessMathMethods;
import utils.Adapter.InputData;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

class Frame extends JFrame {
    private static AccessMathMethods mathMethods;
    private ArrayList<InputData> enteredDataList = new ArrayList<>(Arrays.asList(InputData.values()));
    private JPanel inputDataPanel;// панель для ввода данных
    private ChartPanel schedule;
    ;
    private LayoutManager layoutManager;
    private JPanel container;
    private Adapter adapter = Adapter.getInstance();

    {
        layoutManager = new BorderLayout();
    }

    public Frame() throws HeadlessException {
        super("My First GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocation(50, 100);
        setVisible(true);
        container = new JPanel();
        container.setLayout(layoutManager);
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        setResizable(false);
        ((BorderLayout) layoutManager).setVgap(5);
        ((BorderLayout) layoutManager).setHgap(5);
        createGrid();
        setContentPane(container);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame myFrame = new Frame();
            myFrame.design();

            System.out.println(Charset.defaultCharset());
        });

    }

    public void design() {
        // Область вывода
//        JPanel consoleOutputPanel = new JPanel();
//        BorderLayout borderLayout = new BorderLayout();
//        consoleOutputPanel.setLayout(borderLayout);
//        JTextArea textArea = new JTextArea(20, 100);
//        textArea.setBackground(Color.BLACK);
//        JScrollPane consoleScrollPane = new JScrollPane(textArea);
//        consoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        consoleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        consoleScrollPane.setViewportView(textArea);
//        consoleOutputPanel.add(consoleScrollPane);
//        container.add(consoleOutputPanel, BorderLayout.SOUTH);
//        ConsoleStream consoleErrStream = new ConsoleStream(textArea, ">>");
//        consoleErrStream.setFont(new Font("Arial", Font.PLAIN, 14));
//        consoleErrStream.setTextColor(Color.RED);
//
//        PrintStream errPrintStream = new PrintStream(consoleErrStream);
////        System.setErr(errPrintStream);
//        ConsoleStream consoleOutStream = new ConsoleStream(textArea, ">>");
//        consoleOutStream.setFont(new Font("Verdana", Font.PLAIN, 14));
//        consoleOutStream.setTextColor(Color.WHITE);
//        PrintStream outPrintStream = new PrintStream(consoleOutStream);
//        System.setOut(outPrintStream);
        pack();
        inputByHand();
        revalidate();
    }

    // создание основной сетки приложения

    private void createGrid() {
//        JScrollPane scroll = new JScrollPane(); // TODO scroll
//        scroll.createVerticalScrollBar();
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        //scroll.setViewportView(container);
//        container.add(scroll);

        // объединенная панель ввода данных и графика
        JPanel middlePanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        middlePanel.setLayout(gridBagLayout);
        middlePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.weightx = 0.8;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 2, 2, 2);

        // ячейка с графиком
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;

        JFreeChart freeChart = LineChartSchedule.getChart();
        schedule = LineChartSchedule.createChartPanel(freeChart);
        gridBagLayout.setConstraints(schedule, constraints);
        middlePanel.add(schedule);
        schedule.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
//        schedule.setSize(new Dimension(container.getWidth()/2,500));

        // панель для ввода данных
        JPanel inputContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        inputContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        String[] items = Arrays.stream(AccessMathMethods.values()).map(AccessMathMethods::getName)
                .collect(Collectors.toList()).toArray(new String[AccessMathMethods.values().length]);
        JComboBox<String> methodsComboBox = new JComboBox<>(items);
        methodsComboBox.insertItemAt("Выберите метод решения", 0);
        methodsComboBox.setSelectedItem("Выберите метод решения");
        inputContainer.add(methodsComboBox);
        methodsComboBox.addActionListener(e -> {
            adapter.setMathMethod(Arrays.stream(AccessMathMethods.values()).filter(o->o.getName().equals(methodsComboBox.getSelectedItem())).findFirst().get());;
        });
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Ввод 'вручную'", "Ввод из файла"});
        inputContainer.add(comboBox);
        comboBox.addActionListener(event -> {
            inputDataPanel.removeAll();
            switch (comboBox.getSelectedIndex()) {
                case 0:
                    SwingUtilities.invokeLater(this::inputByHand);
                    break;
                case 1:
                    SwingUtilities.invokeLater(this::inputFromFile);
                    break;
            }
            revalidate();
        });
        inputDataPanel = new JPanel();// панель для ввода данных
        GroupLayout inputDataLayout = new GroupLayout(inputDataPanel);
        inputDataPanel.setLayout(inputDataLayout);
        inputDataPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        inputDataPanel.setSize(480, schedule.getHeight());
        inputDataLayout.setAutoCreateGaps(true);
        inputDataLayout.setAutoCreateContainerGaps(true);
        gridBagLayout.rowHeights = new int[]{500};
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        //gridBagLayout.setConstraints(inputDataPanel, constraints);
        inputContainer.add(inputDataPanel);
        middlePanel.add(inputContainer, constraints);

        gridBagLayout.columnWidths = new int[]{inputDataPanel.getWidth(), getContentPane().getWidth() / 2};
        inputContainer.setMinimumSize(new Dimension(inputDataPanel.getWidth(), schedule.getHeight()));
        inputContainer.setPreferredSize(inputContainer.getMinimumSize());
        inputContainer.setMaximumSize(new Dimension(schedule.getWidth(), rootPane.getHeight()));
        container.add(middlePanel, BorderLayout.CENTER);
        pack();

    }

    //ввод вручную
    private ArrayList<InputData> inputByHand() {

        GroupLayout inputPanelLayout = (GroupLayout) inputDataPanel.getLayout();
        JLabel limitsLabel = new JLabel("Интервал изоляции корня:");
        JLabel downLimitLabel = new JLabel("a");
        JLabel upLimitLabel = new JLabel("b");
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(20);
        NumberFormatter formatter = new NumberFormatter(format);
        JFormattedTextField downLimitTextField = new JFormattedTextField(formatter);
        JFormattedTextField upLimitTextField = new JFormattedTextField(formatter);
        downLimitTextField.setColumns(20);
        upLimitTextField.setColumns(20);

        // поле для ввода погрешности
        JLabel accuracyLabel = new JLabel("Погрешность:");
        JFormattedTextField accuracyTextField = new JFormattedTextField(formatter);
        accuracyTextField.setColumns(20);
        DocumentListener wasAddedTextListener = new DocumentListener() { // слушатель изменяющий входные данные при изменении соответствующих полей
            @Override
            public void insertUpdate(DocumentEvent e) {
                @NonNull
                Double enteredNumber = 0.0;
                try {
                    enteredNumber = format.parse(e.getDocument().getText(0, e.getDocument().getLength()).trim()).doubleValue();
                    // System.out.println(enteredNumber);
                } catch (BadLocationException | ParseException ignored) {
//                    System.err.println("An error has occurred: BadLocationException or ParseException");
//                    ignored.printStackTrace();
                }
                if (e.getDocument().equals(downLimitTextField.getDocument())) {
                    enteredDataList.get(enteredDataList.indexOf(InputData.LOW_LIMIT)).setVal(enteredNumber);
                } else if (e.getDocument().equals(upLimitTextField.getDocument())) {
                    enteredDataList.get(enteredDataList.indexOf(InputData.UP_LIMIT)).setVal(enteredNumber);
                } else if (e.getDocument().equals(accuracyTextField.getDocument())) {
                    enteredDataList.get(enteredDataList.indexOf(InputData.ACCURACY)).setVal(enteredNumber);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };

        downLimitTextField.getDocument().addDocumentListener(wasAddedTextListener);
        upLimitTextField.getDocument().addDocumentListener(wasAddedTextListener);
        accuracyTextField.getDocument().addDocumentListener(wasAddedTextListener);

        JButton readyButton = new JButton("Готово");
        readyButton.addActionListener(o -> {
            adapter.solve();
            SwingUtilities.invokeLater(() -> {
                LineChartSchedule.createChart(schedule.getChart(), LineChartSchedule.createDataset());
            });
        });

//        if(adapter.getMathMethod().equals(AccessMathMethods.SIMPLE_ITERATION_METHOD)){
//            JLabel firstApproximationLabel = new JLabel("Первое приближение");
//            JTextField firstApproximationTextField = new JTextField(20);
//            firstApproximationTextField.getDocument().addDocumentListener(wasAddedTextListener);
//            inputPanelLayout.setHorizontalGroup(inputPanelLayout.createSequentialGroup().
//                    addGroup(inputPanelLayout.createParallelGroup().addComponent(accuracyLabel).addComponent(firstApproximationLabel))
//                    .addGroup(inputPanelLayout.createParallelGroup().addComponent(accuracyTextField).addComponent(firstApproximationTextField)));
//            inputPanelLayout.setVerticalGroup(inputPanelLayout.createSequentialGroup().
//                    addGroup(inputPanelLayout.createParallelGroup().addComponent(accuracyLabel).addComponent(accuracyTextField))
//                    .addGroup(inputPanelLayout.createParallelGroup().addComponent(firstApproximationLabel).addComponent(firstApproximationTextField)));
//        } else {}

        inputPanelLayout.setHorizontalGroup(inputPanelLayout.createSequentialGroup()
                .addGroup(inputPanelLayout.createParallelGroup()
                        .addComponent(limitsLabel, GroupLayout.Alignment.TRAILING)
                        .addComponent(accuracyLabel, GroupLayout.Alignment.TRAILING))
                .addGroup(inputPanelLayout.createParallelGroup()
                        .addComponent(downLimitLabel)
                        .addComponent(upLimitLabel))
                .addGroup(inputPanelLayout.createParallelGroup()
                        .addComponent(downLimitTextField)
                        .addComponent(upLimitTextField)
                        .addComponent(accuracyTextField)
                        .addComponent(readyButton)));
        inputPanelLayout.setVerticalGroup(inputPanelLayout.createSequentialGroup()
                .addGroup(inputPanelLayout.createParallelGroup()
                        .addComponent(limitsLabel)
                        .addComponent(downLimitLabel)
                        .addComponent(downLimitTextField))
                .addGroup(inputPanelLayout.createParallelGroup()
                        .addComponent(upLimitLabel)
                        .addComponent(upLimitTextField))
                .addGroup(inputPanelLayout.createParallelGroup()
                        .addComponent(accuracyLabel)
                        .addComponent(accuracyTextField))
                .addComponent(readyButton));

        return enteredDataList;
    }

    // создание панели для ввода данных из файла
    private void inputFromFile() {
        GroupLayout inputPanelLayout = (GroupLayout) inputDataPanel.getLayout();
        JLabel label = new JLabel("Путь к файлу с исходными данными:");
        JTextField pathToFileTextField = new JTextField("C:\\Users\\Aidar\\Desktop\\test.txt", 15);
        JButton readyButton = new JButton("Готово");
        inputPanelLayout.setHorizontalGroup(inputPanelLayout.createSequentialGroup()
                .addGroup(inputPanelLayout.createParallelGroup()
                        .addGroup(inputPanelLayout.createSequentialGroup()
                                .addComponent(label)
                                .addComponent(pathToFileTextField))
                        .addComponent(readyButton))
        );
        inputPanelLayout.setVerticalGroup(inputPanelLayout.createSequentialGroup()
                .addGroup(inputPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addComponent(label)
                        .addComponent(pathToFileTextField))
                .addComponent(readyButton));
        inputPanelLayout.linkSize(label, pathToFileTextField);

        readyButton.addActionListener((event) -> {
            try {
                readFromFile(pathToFileTextField.getText());
                adapter.solve();
                SwingUtilities.invokeLater(() -> {
                    LineChartSchedule.createChart(schedule.getChart(), LineChartSchedule.createDataset());
                });
            } catch (IllegalArgumentException ignored) {

            }
        });
    }

    //счтитывание данных из файла
    private void readFromFile(String path) {
        Scanner fileScanner = null;
        System.out.println("Введите путь к файлу с исходными данными: ");
        try {
            fileScanner = new Scanner(new File(path));
            fileScanner.useLocale(Locale.US);
            double lowLimit = fileScanner.nextDouble(); // нижняя граница
            double upLimit = fileScanner.nextDouble(); //верхняя граница
            if (lowLimit > upLimit) {
                System.out.println("Не правильно заданы границы интервала. Нижняя граница не может быть больше верхней.");
                throw new IllegalArgumentException();
            }
            InputData.LOW_LIMIT.setVal(lowLimit);
            InputData.UP_LIMIT.setVal(upLimit);
            InputData.ACCURACY.setVal(fileScanner.nextDouble());
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Введен некорректный путь к файлу");
        } catch (InputMismatchException imp) {
            //fileScanner.useLocale(Locale.FRANCE);
            System.err.println("Используйте точку(запятую) в качестве разделителя");
        }
        ;
    }

//    private JComponent sсheduleComponent() {
//        JComponent schedule1 = new Schedule();
//        //schedule1.print(super.getGraphics().create())
//        return schedule1;
//    }

    private class ConsoleStream extends OutputStream {

        private final StringBuilder sb = new StringBuilder();
        private final StringBuffer stringBuffer = new StringBuffer();
        private final List<Byte> byteList = new ArrayList<>();
        byte[] array = new byte[10];
        int i = 0;
        private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private String title;
        private JTextArea textArea;
        private Font font;
        private Color textColor;

        public ConsoleStream(final JTextArea textArea, String title) {
            this.textArea = textArea;
            this.title = title;

        }

        public void setTextColor(Color color) {
            this.textColor = color;
        }

        public void setFont(Font font) {
            this.font = font;
        }

        @Override
        public void write(int b) throws IOException {// todo красным цветом текст ошибок
            if (Character.isISOControl(b) && b == 10) {
                SwingUtilities.invokeLater(() -> {
                    textArea.setFont(font);
                    textArea.setForeground(textColor);
                    ;
                    try {
//                        byte[] buf =(byte[]) sb.chars().toArray();
//                        Stream.of(sb.chars().toArray()).collect();
                        byteArrayOutputStream.write(array);
                        textArea.append(title + new String(array, "UTF-8") + "\n");

                    } catch (IOException e) {
                        System.err.println("Error during flush");
                        e.printStackTrace();
                    }
                    array = new byte[200];
                    i = 0;
                    sb.setLength(0);
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    Array.setByte(array, i, (byte) b);
                    i++;
                    //byteList.add(b);
                });
            }


        }
    }


}
