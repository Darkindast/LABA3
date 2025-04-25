package mephi.b22901.a.l3;


import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MonsterGUI {
    private JFrame frame;
    private JTree monsterTree;
    private JPanel editPanel;
    private JComboBox<String> formatBox;
    private final MonsterStorage storage = new MonsterStorage();
    private Monster currentMonster;

    private JLabel currentFileLabel;
    private JButton exportBtn;
    private boolean changesSaved = false;

    public MonsterGUI() {

        initFrame();
        initLayout();
        buildTree();
        frame.setVisible(true);
    }

    private void initFrame() {
        frame = new JFrame("Bestiarum Viewer");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void initLayout() {
        JSplitPane splitPane = new JSplitPane();

        monsterTree = new JTree();
        monsterTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Чудовища")));
        monsterTree.addTreeSelectionListener(e -> showSelectedMonster());
        JScrollPane treeScroll = new JScrollPane(monsterTree);
        splitPane.setLeftComponent(treeScroll);

        editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane rightScroll = new JScrollPane(editPanel);
        splitPane.setRightComponent(rightScroll);
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel();
 
        JComboBox<String> fileTypeBox = new JComboBox<>(new String[]{".json", ".xml", ".yaml"});
        leftPanel.add(new JLabel("В какой формат экспортировать:"));
        leftPanel.add(fileTypeBox);

        JPanel rightPanel = new JPanel();
        JButton importBtn = new JButton("Импорт");
        exportBtn = new JButton("Экспорт");
   
        exportBtn.setEnabled(true);

        importBtn.addActionListener(e -> importMonsters());
        exportBtn.addActionListener(e -> exportMonsters(fileTypeBox));
    
        rightPanel.add(importBtn);
        rightPanel.add(exportBtn);

        currentFileLabel = new JLabel("Файл: не выбран");
        currentFileLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(currentFileLabel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
    }

private void showSelectedMonster() {
    Object selected = monsterTree.getLastSelectedPathComponent();
    if (selected == null) {
        return; 
    }
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selected;
  
    if (!node.isLeaf()) {
        editPanel.removeAll();
        editPanel.revalidate();
        editPanel.repaint();
        currentMonster = null;
        return;
    }

    if (node.getParent() != null) {
        String selectedName = node.getUserObject().toString();

        currentMonster = storage.getAll().stream()
                .filter(m -> m.getName().equals(selectedName))
                .findFirst()
                .orElse(null);

        if (currentMonster != null) {
            editPanel.removeAll();

            createField("Имя", currentMonster.getName(), val -> {
                currentMonster.setName(val);
                node.setUserObject(val);
                ((DefaultTreeModel) monsterTree.getModel()).nodeChanged(node);
            });

            createField("Тип", currentMonster.getType(), currentMonster::setType);
            createField("Опасность", String.valueOf(currentMonster.getDanger()), val -> {
                try {
                    currentMonster.setDanger(Integer.parseInt(val));
                } catch (NumberFormatException e) {
                    showError("Опасность должна быть целым числом.");
                }
            });

            createField("Местоположение", currentMonster.getLocation(), currentMonster::setLocation);
            createField("Первое упоминание", currentMonster.getFirstMention(), val -> {
               try {
                   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                   Date parsedDate = dateFormat.parse(val);  // Парсим строку в дату
                   String formattedDate = dateFormat.format(parsedDate);  // Форматируем обратно в строку

                   // Устанавливаем отформатированную строку
                   currentMonster.setFirstMention(formattedDate);
               } catch (ParseException e) {
                   showError("Неверный формат даты. Используйте формат: yyyy-MM-dd.");
               }
           });

            createField("Иммунитеты", 
            String.join(",", Optional.ofNullable(currentMonster.getImmunities()).orElse(Collections.emptyList())), 
            val -> currentMonster.setImmunities(Arrays.asList(val.split(","))));
            
            if (currentMonster.getSize() == null) currentMonster.setSize(new Monster.Size());

            createField("Рост (м)", String.valueOf(currentMonster.getSize().getHeight_m()), val -> {
                try {
                    currentMonster.getSize().setHeight_m(Double.parseDouble(val));
                } catch (NumberFormatException e) {
                    showError("Рост должен быть числом.");
                }
            });

            createField("Вес (кг)", String.valueOf(currentMonster.getSize().getWeight_kg()), val -> {
                try {
                    currentMonster.getSize().setWeight_kg(Double.parseDouble(val));
                } catch (NumberFormatException e) {
                    showError("Вес должен быть числом.");
                }
            });

            createField("Время активности", currentMonster.getActiveTime(), currentMonster::setActiveTime);
            createField("Уязвимость", currentMonster.getVulnerability(), currentMonster::setVulnerability);

            if (currentMonster.getRecipe() == null) currentMonster.setRecipe(new Monster.Recipe());

            createField("Тип рецепта", currentMonster.getRecipe().getType(), currentMonster.getRecipe()::setType);
            createField("Время (мин)", String.valueOf(currentMonster.getRecipe().getTime_min()), val -> {
                try {
                    currentMonster.getRecipe().setTime_min(Integer.parseInt(val));
                } catch (NumberFormatException e) {
                    showError("Время рецепта должно быть целым числом.");
                }
            });
            createField("Эффективность", currentMonster.getRecipe().getEffectiveness(), currentMonster.getRecipe()::setEffectiveness);
            editPanel.revalidate();
            editPanel.repaint();
        }
    }
}


private void showError(String message) {
    JOptionPane.showMessageDialog(frame, message, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);  
}

private void createField(String label, String initialValue, java.util.function.Consumer<String> onUpdate) {
    JPanel row = new JPanel(new BorderLayout());
    row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
    JLabel jLabel = new JLabel(label);
    jLabel.setPreferredSize(new Dimension(150, 25));
    JTextField textField = new JTextField(initialValue);
    textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            onUpdate.accept(textField.getText());
        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            onUpdate.accept(textField.getText());
        }
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            onUpdate.accept(textField.getText());
        }
    });
    row.add(jLabel, BorderLayout.WEST);
    row.add(textField, BorderLayout.CENTER);
    editPanel.add(row);
}

private void importMonsters() {
    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));
    int result = chooser.showOpenDialog(frame);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = chooser.getSelectedFile();
        ParserChain chain = new ParserChain();
        List<Monster> newMonsters = chain.handle(selectedFile);
        if (newMonsters == null || newMonsters.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Файл пуст или не распарсился.");
            return;
        }
        String fullPath = selectedFile.getAbsolutePath();
        currentFileLabel.setText("Файл: " + selectedFile.getName());
        storage.removeBySource(fullPath);
        newMonsters.forEach(m -> m.setSource(fullPath));
        storage.add(fullPath, newMonsters);
        changesSaved = false;
        buildTree();
    }
}

private void exportMonsters(JComboBox<String> fileTypeBox) {
    TreePath selectionPath = monsterTree.getSelectionPath();
    if (selectionPath == null) {
        JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите узел дерева.", "Ошибка", JOptionPane.WARNING_MESSAGE);
        return;
    }
    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
    String format = selectedNode.toString().toLowerCase();
    if (!format.equals("json") && !format.equals("xml") && !format.equals("yaml")) {
        JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите узел формата (json, xml, yaml).", "Ошибка", JOptionPane.WARNING_MESSAGE);
        return;
    }

    List<Monster> toExport = storage.getAll().stream()
            .filter(m -> getFormatFromSource(m.getSource()).equals(format))
            .collect(Collectors.toList());

    if (toExport.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "У выбранного узла нет монстров для экспорта.", "Ошибка", JOptionPane.WARNING_MESSAGE);
        return;
    }

    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));
    int result = chooser.showSaveDialog(frame);

    if (result == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();
        String selectedFileType = (String) fileTypeBox.getSelectedItem();

        if (!file.getName().endsWith(selectedFileType)) {
            file = new File(file.getAbsolutePath() + selectedFileType);
        }

        ParserChain chain = new ParserChain();  
        chain.export(file, toExport);  
        JOptionPane.showMessageDialog(frame, "Файл успешно экспортирован.");
    }
}

private void buildTree() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Чудовища");
    Map<String, DefaultMutableTreeNode> formatNodes = new HashMap<>();
    for (String format : List.of("json", "xml", "yaml")) {
        formatNodes.put(format, new DefaultMutableTreeNode(format));
    }

    for (Monster monster : storage.getAll()) { 
        String format = getFormatFromSource(monster.getSource());
        DefaultMutableTreeNode node = formatNodes.getOrDefault(format, new DefaultMutableTreeNode("другое"));
        node.add(new DefaultMutableTreeNode(monster.getName()));
    }
    formatNodes.values().forEach(root::add);
    monsterTree.setModel(new DefaultTreeModel(root));
}

private String getFormatFromSource(String source) {
    source = source.toLowerCase();
    if (source.endsWith(".json")) return "json";
    if (source.endsWith(".xml")) return "xml";
    if (source.endsWith(".yaml") || source.endsWith(".yml")) return "yaml";
    return "другое";
}
}