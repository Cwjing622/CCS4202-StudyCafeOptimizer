package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import algorithm.BruteForce;
import algorithm.DynamicProgramming;
import algorithm.Greedy;
import model.Cafe;
import model.Result;
import data.CafeData;
import ui.AlgorithmProcessDialog;

public class MainFrame extends JFrame {

    // Reference fields to access input, output nodes, tables, and dataset across methods
    private JTextField budgetField;
    private JLabel cafesValLabel;
    private JLabel costValLabel;
    private JLabel scoreValLabel;
    private JLabel timeValLabel;
    private DefaultTableModel compModel;

    private JRadioButton dpRadio;
    private JRadioButton bfRadio;
    private JRadioButton greedyRadio;

    private final List<Cafe> cafes = CafeData.getSampleData();

    // ── Color palette ────────────────────────────────────────────────────────
    private final Color COLOR_NAVY         = new Color(18, 52, 103);   // header title
    private final Color COLOR_SUBTITLE     = new Color(100, 110, 125); // subtitle text
    private final Color COLOR_ACCENT       = new Color(25, 95, 185);   // value labels / accent
    private final Color COLOR_PANEL_BG     = new Color(248, 249, 251); // panel backgrounds
    private final Color COLOR_BORDER       = new Color(200, 207, 218); // border lines
    private final Color COLOR_HEADER_BG    = new Color(232, 237, 244); // table header bg
    private final Color COLOR_HEADER_FG    = new Color(40,  55,  80);  // table header text
    private final Color COLOR_ROW_ALT      = new Color(245, 247, 251); // alternating row
    private final Color COLOR_SEPARATOR    = new Color(190, 200, 215); // separator line
    private final Color COLOR_LABEL_FG     = new Color(35,  45,  60);  // bold label text

    // ── Typography ───────────────────────────────────────────────────────────
    private final Font FONT_TITLE          = new Font("Segoe UI", Font.BOLD,  28);
    private final Font FONT_SUBTITLE       = new Font("Segoe UI", Font.ITALIC, 14);
    private final Font FONT_BORDER_TITLE   = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_COMPONENT      = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_TABLE_HEADER   = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_BUTTON         = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_REC_LABEL      = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_REC_VALUE      = new Font("Segoe UI", Font.PLAIN, 12);

    // ── Uniform sizes ────────────────────────────────────────────────────────
    private final Dimension BUTTON_SIZE    = new Dimension(130, 28);

    public MainFrame() {
        setTitle("Study Café Optimizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        // ── North: header + configuration ────────────────────────────────────
        JPanel northContainer = new JPanel(new BorderLayout(0, 8));
        northContainer.setBackground(Color.WHITE);
        northContainer.add(createHeaderPanel(),    BorderLayout.NORTH);
        northContainer.add(createTopPanel(),       BorderLayout.CENTER);
        contentPane.add(northContainer,            BorderLayout.NORTH);

        // ── Centre: dataset table ─────────────────────────────────────────────
        contentPane.add(createDatasetPanel(),      BorderLayout.CENTER);

        // ── South: recommendation + comparison ───────────────────────────────
        JPanel southContainer = new JPanel(new BorderLayout(10, 0));
        southContainer.setBackground(Color.WHITE);
        southContainer.setPreferredSize(new Dimension(1100, 245));
        southContainer.add(createRecommendationPanel(), BorderLayout.WEST);
        southContainer.add(createComparisonPanel(),     BorderLayout.CENTER);
        contentPane.add(southContainer, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    // ── Header ───────────────────────────────────────────────────────────────

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 0, 6, 0));

        JLabel titleLabel = new JLabel("Study Café Optimizer");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_NAVY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Find the Best Study Café using 0/1 Knapsack Algorithm");
        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setForeground(COLOR_SUBTITLE);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(COLOR_SEPARATOR);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        panel.add(titleLabel);
        panel.add(javax.swing.Box.createVerticalStrut(3));
        panel.add(subtitleLabel);
        panel.add(javax.swing.Box.createVerticalStrut(10));
        panel.add(separator);

        return panel;
    }

    // ── Configuration panel ──────────────────────────────────────────────────

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        topPanel.setBackground(COLOR_PANEL_BG);
        topPanel.setBorder(makeTitledBorder("Configuration"));

        // Budget label + field
        JLabel budgetLabel = new JLabel("Budget (RM):");
        budgetLabel.setFont(FONT_COMPONENT);
        budgetLabel.setForeground(COLOR_LABEL_FG);

        budgetField = new JTextField("50", 7);
        budgetField.setFont(FONT_COMPONENT);
        budgetField.setPreferredSize(new Dimension(72, 26));

        topPanel.add(budgetLabel);
        topPanel.add(budgetField);

        // Separator gap
        topPanel.add(makeVerticalSeparator());

        // Algorithm radio buttons
        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setFont(FONT_COMPONENT);
        algoLabel.setForeground(COLOR_LABEL_FG);
        topPanel.add(algoLabel);

        dpRadio     = styledRadio("Dynamic Programming", true);
        bfRadio     = styledRadio("Brute Force",         false);
        greedyRadio = styledRadio("Greedy",              false);

        ButtonGroup algoGroup = new ButtonGroup();
        algoGroup.add(dpRadio);
        algoGroup.add(bfRadio);
        algoGroup.add(greedyRadio);

        topPanel.add(dpRadio);
        topPanel.add(bfRadio);
        topPanel.add(greedyRadio);

        // Separator gap
        topPanel.add(makeVerticalSeparator());

        // Buttons
        JButton solveButton   = styledButton("Solve");
        JButton compareButton = styledButton("Compare All");

        // Attach action event listener for the optimization processing
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSolveAction();
            }
        });

        // Attach action event listener for computing multiple executions at once
        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCompareAllAction();
            }
        });

        topPanel.add(solveButton);
        topPanel.add(compareButton);

        return topPanel;
    }

    // ── Dataset table ────────────────────────────────────────────────────────

    private JPanel createDatasetPanel() {
        JPanel datasetPanel = new JPanel(new BorderLayout());
        datasetPanel.setBackground(Color.WHITE);
        datasetPanel.setBorder(makeTitledBorder("Café Dataset"));

        String[] columns = {
            "Café Name", "Price (RM)", "WiFi Score",
            "Distance Score", "Noise Score", "Study Score"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Cafe cafe : cafes) {
            Object[] rowData = {
                cafe.getName(),
                cafe.getPrice(),
                cafe.getWifiScore(),
                cafe.getDistanceScore(),
                cafe.getNoiseScore(),
                cafe.getStudyScore()
            };
            tableModel.addRow(rowData);
        }

        JTable datasetTable = new JTable(tableModel) {
            @Override
            public java.awt.Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int col) {
                java.awt.Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ROW_ALT);
                }
                return c;
            }
        };

        applyTableStyle(datasetTable);

        // Column alignment: name left, numbers centered
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < datasetTable.getColumnCount(); i++) {
            datasetTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(datasetTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        datasetPanel.add(scrollPane, BorderLayout.CENTER);

        return datasetPanel;
    }

    // ── Recommendation panel ─────────────────────────────────────────────────

    private JPanel createRecommendationPanel() {
        JPanel recPanel = new JPanel(new GridBagLayout());
        recPanel.setPreferredSize(new Dimension(400, 245));
        recPanel.setBackground(COLOR_PANEL_BG);
        recPanel.setBorder(makeTitledBorder("Recommendation"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 14, 7, 14);

        // Row 0 – Selected Cafes
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.38;
        recPanel.add(recLabel("Selected Cafes:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.62;
        cafesValLabel = recValueLabel("-");
        recPanel.add(cafesValLabel, gbc);

        // Row 1 – Total Cost
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.38;
        recPanel.add(recLabel("Total Cost:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.62;
        costValLabel = recValueLabel("-");
        recPanel.add(costValLabel, gbc);

        // Row 2 – Study Score
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.38;
        recPanel.add(recLabel("Study Score:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.62;
        scoreValLabel = recValueLabel("-");
        recPanel.add(scoreValLabel, gbc);

        // Row 3 – Execution Time
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.38;
        recPanel.add(recLabel("Execution Time:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.62;
        timeValLabel = recValueLabel("-");
        recPanel.add(timeValLabel, gbc);

        // Row 4 – View DP Table button
        gbc.gridy   = 4;
        gbc.gridx   = 0;
        gbc.gridwidth = 2;
        gbc.fill    = GridBagConstraints.NONE;
        gbc.anchor  = GridBagConstraints.CENTER;
        gbc.insets  = new Insets(12, 14, 10, 14);
        JButton viewDpBtn = styledButton("View Algorithm Process");
            viewDpBtn.addActionListener(e -> {

            try {

                int budget = Integer.parseInt(budgetField.getText().trim());

                Result result;

                String algorithmName;

                if (dpRadio.isSelected()) {
                    algorithmName = "Dynamic Programming";
                    result = new DynamicProgramming().solve(cafes, budget);

                } else if (bfRadio.isSelected()) {
                    algorithmName = "Brute Force";
                    result = new BruteForce().solve(cafes, budget);

                } else {
                    algorithmName = "Greedy";
                    result = new Greedy().solve(cafes, budget);
                }

                AlgorithmProcessDialog dialog =
                        new AlgorithmProcessDialog(this, algorithmName, result, budget);

                dialog.setVisible(true);

            } catch (NumberFormatException ex) {

                cafesValLabel.setText("Invalid Budget");
            }
        });
        recPanel.add(viewDpBtn, gbc);

        return recPanel;
    }

    // ── Algorithm comparison panel ───────────────────────────────────────────

    private JPanel createComparisonPanel() {
        JPanel compPanel = new JPanel(new BorderLayout());
        compPanel.setBackground(COLOR_PANEL_BG);
        compPanel.setBorder(makeTitledBorder("Algorithm Comparison"));

        String[] columns = {"Algorithm", "Total Cost", "Study Score", "Execution Time"};

        // Setup table model using reference instance field instead of immediate local creation
        compModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Populate initial baseline presentation lines
        compModel.addRow(new Object[]{"Dynamic Programming", "-", "-", "-"});
        compModel.addRow(new Object[]{"Brute Force",         "-", "-", "-"});
        compModel.addRow(new Object[]{"Greedy",              "-", "-", "-"});

        JTable compTable = new JTable(compModel) {
            @Override
            public java.awt.Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int col) {
                java.awt.Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ROW_ALT);
                }
                return c;
            }
        };

        applyTableStyle(compTable);

        // Center-align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < compTable.getColumnCount(); i++) {
            compTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(compTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        compPanel.add(scrollPane, BorderLayout.CENTER);

        return compPanel;
    }

    // ── Logic (unchanged) ────────────────────────────────────────────────────

    /**
     * Handles processing constraints when clicking the Solve controller component.
     * Executes the baseline framework structure for Greedy routing.
     */
    private void handleSolveAction() {
        try {
            int budget = Integer.parseInt(budgetField.getText().trim());

            if (budget <= 0) {
                cafesValLabel.setText("Budget must be greater than 0");
                costValLabel.setText("-");
                scoreValLabel.setText("-");
                timeValLabel.setText("-");
                return;
            }

            Result result;

            if (dpRadio.isSelected()) {
                DynamicProgramming dp = new DynamicProgramming();
                result = dp.solve(cafes, budget);
            } else if (bfRadio.isSelected()) {
                BruteForce bf = new BruteForce();
                result = bf.solve(cafes, budget);
            } else {
                Greedy greedy = new Greedy();
                result = greedy.solve(cafes, budget);
            }

            updateRecommendationPanel(result);

        } catch (NumberFormatException ex) {
            cafesValLabel.setText("Invalid Budget");
            costValLabel.setText("-");
            scoreValLabel.setText("-");
            timeValLabel.setText("-");
        }
    }

    /**
     * Triggers optimization engines sequentially across all three algorithmic options.
     * Overwrites dataset entries inside the existing calculation comparison table model view grid.
     */
    private void handleCompareAllAction() {
        try {
            int budget = Integer.parseInt(budgetField.getText().trim());

            if (budget <= 0) {
                compModel.setRowCount(0);
                compModel.addRow(new Object[]{"Dynamic Programming", "-", "-", "-"});
                compModel.addRow(new Object[]{"Brute Force",         "-", "-", "-"});
                compModel.addRow(new Object[]{"Greedy",              "-", "-", "-"});
                return;
            }

            // Initialize optimization engines
            DynamicProgramming dpSolver     = new DynamicProgramming();
            BruteForce         bfSolver     = new BruteForce();
            Greedy             greedySolver = new Greedy();

            // Run solving profiles
            Result dpResult     = dpSolver.solve(cafes, budget);
            Result bfResult     = bfSolver.solve(cafes, budget);
            Result greedyResult = greedySolver.solve(cafes, budget);

            // Wipe out current table configuration rows
            compModel.setRowCount(0);

            // Populate fresh performance rows derived dynamically
            addComparisonRow("Dynamic Programming", dpResult);
            addComparisonRow("Brute Force",         bfResult);
            addComparisonRow("Greedy",              greedyResult);

        } catch (NumberFormatException ex) {
            // Revert configuration lines to error states if input string reading crashes
            compModel.setRowCount(0);
            compModel.addRow(new Object[]{"Dynamic Programming", "Error", "Error", "Error"});
            compModel.addRow(new Object[]{"Brute Force",         "Error", "Error", "Error"});
            compModel.addRow(new Object[]{"Greedy",              "Error", "Error", "Error"});
        }
    }

    /**
     * Modular structural method helper to inject computed properties safely inside table configurations.
     */
    private void addComparisonRow(String algorithmName, Result result) {
        double msTime = result.getExecutionTime() / 1_000_000.0;
        String formattedTime = String.format("%.4f ms", msTime);

        compModel.addRow(new Object[]{
            algorithmName,
            "RM " + result.getTotalCost() + ".00",
            String.valueOf(result.getTotalStudyScore()),
            formattedTime
        });
    }

    /**
     * Dynamically updates labels with formatting metrics extracted from Result.
     */
    private void updateRecommendationPanel(Result result) {
        if (result.getSelectedCafes().isEmpty()) {
            cafesValLabel.setText("None");
        } else {
           String selectedNames = result.getSelectedCafes().stream()
                    .map(cafe -> "• " + cafe.getName())
                    .collect(Collectors.joining("<br>"));

            cafesValLabel.setText("<html>" + selectedNames + "</html>");
        }

        costValLabel.setText("RM " + result.getTotalCost() + ".00");
        scoreValLabel.setText(String.valueOf(result.getTotalStudyScore()));

        // Converts baseline nanoseconds runtime tracking configuration metrics to milliseconds scale
        double msTime = result.getExecutionTime() / 1_000_000.0;
        timeValLabel.setText(String.format("%.4f ms", msTime));
    }

    // ── Private UI helpers ───────────────────────────────────────────────────

    /** Applies a consistent, polished style to any JTable. */
    private void applyTableStyle(JTable table) {
        table.setFont(FONT_COMPONENT);
        table.setRowHeight(28);
        table.setGridColor(COLOR_BORDER);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(210, 225, 250));
        table.setSelectionForeground(COLOR_LABEL_FG);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(COLOR_HEADER_BG);
        header.setForeground(COLOR_HEADER_FG);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
    }

    /** Creates a uniform titled border with consistent font and color. */
    private javax.swing.border.Border makeTitledBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_BORDER_TITLE,
                COLOR_LABEL_FG);
        // Wrap with empty padding inside the titled border
        return BorderFactory.createCompoundBorder(
                tb,
                BorderFactory.createEmptyBorder(4, 6, 6, 6));
    }

    /** Creates a styled button with uniform size. */
    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setPreferredSize(BUTTON_SIZE);
        btn.setFocusPainted(false);
        return btn;
    }

    /** Creates a styled radio button with the shared component font. */
    private JRadioButton styledRadio(String text, boolean selected) {
        JRadioButton rb = new JRadioButton(text, selected);
        rb.setFont(FONT_COMPONENT);
        rb.setBackground(COLOR_PANEL_BG);
        rb.setForeground(COLOR_LABEL_FG);
        return rb;
    }

    /** Creates a bold label for the Recommendation panel. */
    private JLabel recLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_REC_LABEL);
        lbl.setForeground(COLOR_LABEL_FG);
        return lbl;
    }

    /** Creates a value label for the Recommendation panel (accent colour). */
    private JLabel recValueLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_REC_VALUE);
        lbl.setForeground(COLOR_ACCENT);
        return lbl;
    }

    /** Thin vertical separator to visually group configuration items. */
    private JPanel makeVerticalSeparator() {
        JPanel sep = new JPanel();
        sep.setBackground(COLOR_SEPARATOR);
        sep.setPreferredSize(new Dimension(1, 22));
        return sep;
    }
}