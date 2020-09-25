package ui;

import javax.swing.*;
import java.awt.*;

class Schedule extends JPanel {

    private final Frame frame;
    private JLabelGrid label;
    private JScrollPane scrollPane;
    public Schedule(Frame frame) {
        this.frame = frame;
        // Создание панели прокрутки
        label = new JLabelGrid();
        scrollPane = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setRowHeaderView(new YHeader());
        scrollPane.setColumnHeaderView(new XHeader());
        // Определение свойств панели прокрутки
        scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.yellow));
        scrollPane.setWheelScrollingEnabled(true);
        add(scrollPane);
        // Вывод окна на экран
        setSize(450, 300);
        setVisible(true);
    }

    @Override
    public int getHeight() {
        return 500;
    }

    @Override
    public int getWidth() {
        return frame.getRootPane().getWidth() / 2;
    }

//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
////        setSize(100, 100);
////        Graphics2D g2d = (Graphics2D) g;
////        g2d.setColor(Color.BLACK);
////
////        g.drawLine(0, 0, getWidth(), getHeight());
//    }

    // Внутренний класс
    class JLabelGrid extends JLabel implements Scrollable {
        private static final long serialVersionUID = 1L;

        private int CELL_SIZE = 10;  // размер ячейки сетки
        private int CELL_COUNT = 50;  // количество ячеек сетки

        public JLabelGrid() {
        }

        // Предпочтительный размер компонента
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(CELL_SIZE * CELL_COUNT, CELL_SIZE * CELL_COUNT);
        }

        // Прорисовка компонента
        @Override
        public void paintComponent(Graphics g) {
            // Вызов метода базового класса
            super.paintComponent(g);
            // Устанавливаем заголовки
            for (int x = 0; x < CELL_COUNT; x++) {
                for (int y = 0; y < CELL_COUNT; y++) {
                    // Прорисовывем ячейку
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Предпочтительный размер области прокрутки
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        // Приращение при прокрутке на один элемент
        @Override
        public int getScrollableUnitIncrement(Rectangle visible, int or, int dir) {
            return CELL_SIZE;
        }

        // Приращение при прокрутке "блоком"
        @Override
        public int getScrollableBlockIncrement(Rectangle visible, int or, int dir) {
            return CELL_SIZE * 10;
        }

        // Контроль размера области прокрутки
        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    // Заголовок по оси X
    class XHeader extends JLabel{
        // Размер заголовка
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(label.getPreferredSize().width, 20);
        }

        // Прорисовываем линейку
        @Override
        public void paintComponent(Graphics g) {
            int width = getWidth();
            for (int i = 0; i < width; i += 50) {
                g.drawString("" + i, i, 15);
            }
        }
    }

    // Заголовок по оси Y
    class YHeader extends JLabel {
        // Размер заголовка
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(20, label.getPreferredSize().height);
        }

        // Прорисовываем линейку
        @Override
        public void paintComponent(Graphics g) {
            int height = getHeight();
            for (int i = 0; i < height; i += 50) {
                g.drawString("" + i, 0, i);
            }
        }
    }
}
