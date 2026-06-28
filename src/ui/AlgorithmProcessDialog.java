package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import data.CafeData;
import model.Cafe;
import model.Result;


public class AlgorithmProcessDialog extends JDialog {

    // ── Color palette (mirrors MainFrame) ────────────────────────────────────
    private final Color COLOR_NAVY        = new Color(18,  52, 103);
    private final Color COLOR_SUBTITLE    = new Color(100, 110, 125);
    private final Color COLOR_LABEL_FG    = new Color(35,  45,  60);
    private final Color COLOR_BORDER      = new Color(200, 207, 218);
    private final Color COLOR_SEPARATOR   = new Color(190, 200, 215);
    private final Color COLOR_PANEL_BG    = new Color(248, 249, 251);
    private final Color COLOR_HEADER_BG   = new Color(232, 237, 244);
    private final Color COLOR_HEADER_FG   = new Color(40,  55,  80);
    private final Color COLOR_ROW_ALT     = new Color(245, 247, 251);
    private final Color COLOR_ACCENT      = new Color(25,  95, 185);
    private final Color COLOR_BEST_BG     = new Color(220, 237, 255);   // ⭐ best row highlight
    private final Color COLOR_BEST_FG     = new Color(10,  70, 160);    // ⭐ best row text
    private final Color COLOR_OVER_FG     = new Color(160,  50,  50);   // ✗ skipped text
    private final Color COLOR_SELECTED_BG = new Color(230, 245, 230);   // ✓ selected row bg
    private final Color COLOR_SELECTED_FG = new Color(30,  110,  30);   // ✓ selected row text

    // ── Typography (mirrors MainFrame) ───────────────────────────────────────
    private final Font FONT_TITLE         = new Font("Segoe UI", Font.BOLD,  22);
    private final Font FONT_SUBTITLE      = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_CONTENT       = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_BUTTON        = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_BORDER_TITLE  = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_TABLE_HEADER  = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_SUMMARY_BOLD  = new Font("Segoe UI", Font.BOLD,  12);
    private final Font FONT_STEP_LABEL    = new Font("Segoe UI", Font.BOLD,  12);

    private final String algorithmName;
    private final Result result;
    private final int    budget;

    // Content area exposed so callers can inject real content later
    private JPanel contentPanel;

    /**
     * Creates a reusable algorithm process dialog.
     *
     * @param parent        the owning JFrame
     * @param algorithmName display name of the selected algorithm
     *                      e.g. "Dynamic Programming", "Brute Force", "Greedy"
     */
    public AlgorithmProcessDialog(JFrame parent, String algorithmName, Result result, int budget) {
        super(parent, "Algorithm Process — " + algorithmName, true);
        this.algorithmName = algorithmName;
        this.result        = result;
        this.budget        = budget;

        setSize(850, 600);
        setMinimumSize(new Dimension(700, 480));
        setLocationRelativeTo(parent);
        setResizable(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        root.add(createHeaderPanel(), BorderLayout.NORTH);
        root.add(createCenterPanel(), BorderLayout.CENTER);
        root.add(createFooterPanel(), BorderLayout.SOUTH);

        if ("Dynamic Programming".equals(algorithmName)) {
            showDynamicProgrammingTable();
        } else if ("Brute Force".equals(algorithmName)) {
            showBruteForceProcess();
        } else if ("Greedy".equals(algorithmName)) {
            showGreedyProcess();
        }

        setContentPane(root);
    }

    // ── Header ───────────────────────────────────────────────────────────────

    private JPanel createHeaderPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createEmptyBorder(18, 24, 0, 24));

        JPanel textBlock = new JPanel();
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
        textBlock.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Algorithm Process");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_NAVY);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Selected Algorithm:  " + algorithmName);
        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setForeground(COLOR_SUBTITLE);
        subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

        textBlock.add(titleLabel);
        textBlock.add(javax.swing.Box.createVerticalStrut(4));
        textBlock.add(subtitleLabel);

        // Thin separator below header
        JPanel separator = new JPanel();
        separator.setBackground(COLOR_SEPARATOR);
        separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));

        JPanel separatorWrapper = new JPanel(new BorderLayout());
        separatorWrapper.setBackground(Color.WHITE);
        separatorWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        separatorWrapper.add(separator, BorderLayout.CENTER);

        wrapper.add(textBlock,        BorderLayout.CENTER);
        wrapper.add(separatorWrapper, BorderLayout.SOUTH);

        return wrapper;
    }

    // ── Center (scrollable content area) ─────────────────────────────────────

    private JPanel createCenterPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(Color.WHITE);

        outer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(14, 24, 10, 24),
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(COLOR_BORDER, 1),
                    "Process Details",
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP,
                    FONT_BORDER_TITLE,
                    COLOR_LABEL_FG),
                BorderFactory.createEmptyBorder(6, 6, 6, 6))));

        // BoxLayout so table + summary stack without table claiming all vertical space
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Default placeholder shown until real content is injected
        String placeholderText;
        switch (algorithmName) {
            case "Dynamic Programming":
                placeholderText =
                        "Dynamic Programming Process\n\n" +
                        "The Dynamic Programming (DP) table will be displayed here.\n\n" +
                        "It will illustrate how the optimal study score is computed\n" +
                        "for every budget value step by step.";
                break;
            case "Brute Force":
                placeholderText =
                        "Brute Force Process\n\n" +
                        "All possible café combinations will be displayed here.\n\n" +
                        "Each combination will show its total cost,\n" +
                        "study score and whether it satisfies the budget.";
                break;
            default:
                placeholderText =
                        "Greedy Process\n\n" +
                        "The Greedy selection process will be displayed here.\n\n" +
                        "It will show the study score/price ratio,\n" +
                        "sorting order and café selection steps.";
        }

        JTextArea placeholder = new JTextArea(placeholderText);
        placeholder.setFont(FONT_CONTENT);
        placeholder.setForeground(COLOR_SUBTITLE);
        placeholder.setBackground(Color.WHITE);
        placeholder.setEditable(false);
        placeholder.setLineWrap(true);
        placeholder.setWrapStyleWord(true);
        placeholder.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        contentPanel.add(placeholder);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        outer.add(scrollPane, BorderLayout.CENTER);
        return outer;
    }

    // ── Footer ───────────────────────────────────────────────────────────────

    private JPanel createFooterPanel() {
        JPanel separatorLine = new JPanel();
        separatorLine.setBackground(COLOR_SEPARATOR);
        separatorLine.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));

        JButton closeButton = new JButton("Close");
        closeButton.setFont(FONT_BUTTON);
        closeButton.setPreferredSize(new Dimension(110, 30));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonRow.setBackground(COLOR_PANEL_BG);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(12, 0, 14, 0));
        buttonRow.add(closeButton);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_PANEL_BG);
        footer.add(separatorLine, BorderLayout.NORTH);
        footer.add(buttonRow,     BorderLayout.CENTER);

        return footer;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public JPanel getContentPanel() {
        return contentPanel;
    }

    // ── Dynamic Programming rendering (unchanged) ─────────────────────────────

    private void showDynamicProgrammingTable() {
        if (result == null || result.getDpTable() == null) return;

        int[][] dp = result.getDpTable();

        String[] columnNames = new String[dp[0].length + 1];
        columnNames[0] = "Cafe";
        for (int i = 0; i < dp[0].length; i++) {
            columnNames[i + 1] = String.valueOf(i);
        }

        Object[][] data = new Object[dp.length][dp[0].length + 1];
        data[0][0] = "Initial";
        for (int j = 0; j < dp[0].length; j++) {
            data[0][j + 1] = dp[0][j];
        }

        List<Cafe> cafes = CafeData.getSampleData();
        for (int i = 1; i < dp.length; i++) {
            data[i][0] = cafes.get(i - 1).getName();
            for (int j = 0; j < dp[0].length; j++) {
                data[i][j + 1] = dp[i][j];
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ROW_ALT);
                }
                return c;
            }
        };

        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        table.setEnabled(false);
        table.setFont(FONT_CONTENT);
        table.setRowHeight(26);
        table.setGridColor(COLOR_BORDER);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(COLOR_HEADER_BG);
        header.setForeground(COLOR_HEADER_FG);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(140);
        for (int c = 1; c < table.getColumnCount(); c++) {
            table.getColumnModel().getColumn(c).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(c).setPreferredWidth(36);        
        }

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setPreferredSize(new Dimension(780, 260));
        tableScroll.setAlignmentX(LEFT_ALIGNMENT);

        JLabel description = new JLabel(
            "<html>" +
            "<b>Rows</b>: Number of cafés considered &nbsp;|&nbsp; " +
            "<b>Columns</b>: Available budget (RM) &nbsp;|&nbsp; " +
            "Each cell = maximum achievable Study Score." +
            "</html>");
        description.setFont(FONT_CONTENT);
        description.setForeground(COLOR_SUBTITLE);
        description.setAlignmentX(LEFT_ALIGNMENT);
        description.setBorder(BorderFactory.createEmptyBorder(0, 2, 10, 0));

        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(description);
        contentPanel.add(tableScroll);
        contentPanel.add(javax.swing.Box.createVerticalStrut(12));
        contentPanel.add(buildDpSummaryPanel());
        contentPanel.add(javax.swing.Box.createVerticalGlue());

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Brute Force rendering (unchanged) ────────────────────────────────────

    private void showBruteForceProcess() {
        List<Cafe> cafes = CafeData.getSampleData();
        int n = cafes.size();

        String[] columnNames = { "Combination", "Total Cost", "Study Score", "Status" };

        List<Object[]> rows       = new ArrayList<>();
        int            bestScore  = -1;
        int            bestRowIdx = -1;

        int totalCombinations = 1 << n;
        int validCount        = 0;
        int overBudgetCount   = 0;

        for (int mask = 0; mask < totalCombinations; mask++) {
            StringBuilder combination = new StringBuilder();
            int     totalCost  = 0;
            int     totalScore = 0;
            boolean first      = true;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    Cafe cafe = cafes.get(i);
                    if (!first) combination.append(", ");
                    combination.append(cafe.getName());
                    first = false;
                    totalCost  += cafe.getPrice();
                    totalScore += cafe.getStudyScore();
                }
            }

            String comboLabel = (combination.length() == 0)
                    ? "{}"
                    : "{" + combination.toString() + "}";

            String status;
            if (totalCost <= budget) {
                status = "✓  Valid";
                validCount++;
                if (totalScore > bestScore) {
                    bestScore  = totalScore;
                    bestRowIdx = rows.size();
                }
            } else {
                status = "✗  Over Budget";
                overBudgetCount++;
            }

            rows.add(new Object[]{ comboLabel, totalCost, totalScore, status });
        }

        if (bestRowIdx >= 0) {
            rows.get(bestRowIdx)[3] = "⭐  BEST";
        }

        final int finalBestRowIdx = bestRowIdx;

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : rows) model.addRow(row);

        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (isRowSelected(row)) return c;

                String status = (String) getValueAt(row, 3);
                if (row == finalBestRowIdx) {
                    c.setBackground(COLOR_BEST_BG);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setForeground(COLOR_BEST_FG);
                } else if (status != null && status.startsWith("✗")) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ROW_ALT);
                    c.setForeground(COLOR_OVER_FG);
                    c.setFont(FONT_CONTENT);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ROW_ALT);
                    c.setForeground(COLOR_LABEL_FG);
                    c.setFont(FONT_CONTENT);
                }
                return c;
            }
        };

        table.setEnabled(false);
        table.setFont(FONT_CONTENT);
        table.setRowHeight(26);
        table.setGridColor(COLOR_BORDER);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(COLOR_HEADER_BG);
        header.setForeground(COLOR_HEADER_FG);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(340);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 280));
        tableScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        tableScroll.setAlignmentX(LEFT_ALIGNMENT);

        JLabel description = new JLabel(
            "<html>" +
            "Every possible subset of cafés is evaluated. " +
            "<b style='color:#0a46a0'>⭐ BEST</b> = highest study score within budget. " +
            "<span style='color:#a03232'>✗ Over Budget</span> = exceeds RM " + budget + "." +
            "</html>");
        description.setFont(FONT_CONTENT);
        description.setForeground(COLOR_SUBTITLE);
        description.setAlignmentX(LEFT_ALIGNMENT);
        description.setBorder(BorderFactory.createEmptyBorder(0, 2, 10, 0));

        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(description);
        contentPanel.add(tableScroll);
        contentPanel.add(javax.swing.Box.createVerticalStrut(12));
        contentPanel.add(buildBfSummaryPanel(totalCombinations, validCount, overBudgetCount, bestScore));
        contentPanel.add(javax.swing.Box.createVerticalGlue());

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Greedy rendering ──────────────────────────────────────────────────────

    private void showGreedyProcess() {
        List<Cafe> cafes = CafeData.getSampleData();

        // ── Pre-compute ratios ─────────────────────────────────────────────────
        // Each entry: { cafe, ratio }
        List<double[]>  ratioValues = new ArrayList<>();
        List<Cafe>      cafeOrder   = new ArrayList<>(cafes);

        for (Cafe cafe : cafeOrder) {
            double ratio = (cafe.getPrice() > 0)
                    ? (double) cafe.getStudyScore() / cafe.getPrice()
                    : 0.0;
            ratioValues.add(new double[]{ ratio });
        }

        // Sorted indices by ratio descending (mirrors the actual Greedy algorithm)
        List<Integer> sortedIndices = new ArrayList<>();
        for (int i = 0; i < cafeOrder.size(); i++) sortedIndices.add(i);
        sortedIndices.sort((a, b) ->
            Double.compare(ratioValues.get(b)[0], ratioValues.get(a)[0]));

        // ── Overall description ────────────────────────────────────────────────
        JLabel description = new JLabel(
            "<html>" +
            "The Greedy algorithm selects cafés based on the highest " +
            "<b>Study Score / Price</b> ratio. " +
            "Cafés are evaluated in descending ratio order and accepted " +
            "as long as the remaining budget allows." +
            "</html>");
        description.setFont(FONT_CONTENT);
        description.setForeground(COLOR_SUBTITLE);
        description.setAlignmentX(LEFT_ALIGNMENT);
        description.setBorder(BorderFactory.createEmptyBorder(0, 2, 12, 0));

        // ── Step 1: Ratio table ────────────────────────────────────────────────
        JLabel step1Label = buildStepLabel("Step 1 — Calculate Study Score / Price Ratio");

        String[] ratioColumns = { "Café", "Study Score", "Price (RM)", "Ratio" };
        DefaultTableModel ratioModel = new DefaultTableModel(ratioColumns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int i = 0; i < cafeOrder.size(); i++) {
            Cafe   cafe  = cafeOrder.get(i);
            double ratio = ratioValues.get(i)[0];
            ratioModel.addRow(new Object[]{
                cafe.getName(),
                cafe.getStudyScore(),
                cafe.getPrice(),
                String.format("%.2f", ratio)
            });
        }

        JTable ratioTable = buildStyledTable(ratioModel, -1);
        applyGreedyRatioColumns(ratioTable);
        JScrollPane ratioScroll = buildTableScroll(ratioTable, 160);

        // ── Step 2: Sorted order table ─────────────────────────────────────────
        JLabel step2Label = buildStepLabel("Step 2 — Sort by Ratio (Descending)");

        String[] sortedColumns = { "Rank", "Café", "Ratio" };
        DefaultTableModel sortedModel = new DefaultTableModel(sortedColumns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int rank = 0; rank < sortedIndices.size(); rank++) {
            int    idx   = sortedIndices.get(rank);
            Cafe   cafe  = cafeOrder.get(idx);
            double ratio = ratioValues.get(idx)[0];
            sortedModel.addRow(new Object[]{
                "#" + (rank + 1),
                cafe.getName(),
                String.format("%.2f", ratio)
            });
        }

        JTable sortedTable = buildStyledTable(sortedModel, -1);
        applyGreedySortedColumns(sortedTable);
        JScrollPane sortedScroll = buildTableScroll(sortedTable, 160);

        // ── Step 3: Selection process table ───────────────────────────────────
        JLabel step3Label = buildStepLabel("Step 3 — Selection Process (Budget: RM " + budget + ")");

        String[] selectionColumns = { "Café", "Cost (RM)", "Remaining Budget (RM)", "Decision" };
        DefaultTableModel selectionModel = new DefaultTableModel(selectionColumns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // Replay the greedy selection to capture per-step decisions
        List<Boolean> decisions     = new ArrayList<>();
        int           remaining     = budget;

        for (int idx : sortedIndices) {
            Cafe    cafe     = cafeOrder.get(idx);
            boolean selected = cafe.getPrice() <= remaining;
            if (selected) remaining -= cafe.getPrice();

            String decision = selected ? "✓  Selected" : "✗  Skipped (Budget Exceeded)";
            selectionModel.addRow(new Object[]{
                cafe.getName(),
                cafe.getPrice(),
                remaining,
                decision
            });
            decisions.add(selected);
        }

        // Pass decision flags into the renderer so rows can be coloured
        final List<Boolean> finalDecisions = decisions;

        JTable selectionTable = new JTable(selectionModel) {
            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (isRowSelected(row)) return c;

                boolean sel = (row < finalDecisions.size()) && finalDecisions.get(row);
                if (sel) {
                    c.setBackground(COLOR_SELECTED_BG);
                    c.setForeground(COLOR_SELECTED_FG);
                    c.setFont(FONT_CONTENT.deriveFont(Font.BOLD));
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ROW_ALT);
                    c.setForeground(COLOR_OVER_FG);
                    c.setFont(FONT_CONTENT);
                }
                return c;
            }
        };
        applyTableStyle(selectionTable);
        applyGreedySelectionColumns(selectionTable);
        JScrollPane selectionScroll = buildTableScroll(selectionTable, 160);

        // ── Inject all steps into contentPanel ────────────────────────────────
        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(description);

        contentPanel.add(step1Label);
        contentPanel.add(javax.swing.Box.createVerticalStrut(6));
        contentPanel.add(ratioScroll);
        contentPanel.add(javax.swing.Box.createVerticalStrut(14));

        contentPanel.add(step2Label);
        contentPanel.add(javax.swing.Box.createVerticalStrut(6));
        contentPanel.add(sortedScroll);
        contentPanel.add(javax.swing.Box.createVerticalStrut(14));

        contentPanel.add(step3Label);
        contentPanel.add(javax.swing.Box.createVerticalStrut(6));
        contentPanel.add(selectionScroll);
        contentPanel.add(javax.swing.Box.createVerticalStrut(14));

        contentPanel.add(buildGreedySummaryPanel());
        contentPanel.add(javax.swing.Box.createVerticalGlue());

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Greedy table column helpers ───────────────────────────────────────────

    private void applyGreedyRatioColumns(JTable table) {
        DefaultTableCellRenderer left   = makeLeftRenderer();
        DefaultTableCellRenderer center = makeCenterRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(left);
        table.getColumnModel().getColumn(0).setPreferredWidth(220);  // Café
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);  // Study Score
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Price
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Ratio
    }

    private void applyGreedySortedColumns(JTable table) {
        DefaultTableCellRenderer left   = makeLeftRenderer();
        DefaultTableCellRenderer center = makeCenterRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // Rank
        table.getColumnModel().getColumn(1).setCellRenderer(left);
        table.getColumnModel().getColumn(1).setPreferredWidth(280);  // Café
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Ratio
    }

    private void applyGreedySelectionColumns(JTable table) {
        DefaultTableCellRenderer left   = makeLeftRenderer();
        DefaultTableCellRenderer center = makeCenterRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(left);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);  // Café
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);  // Cost
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);  // Remaining Budget
        table.getColumnModel().getColumn(3).setCellRenderer(left);
        table.getColumnModel().getColumn(3).setPreferredWidth(220);  // Decision
    }

    // ── Shared table-building helpers ─────────────────────────────────────────

    /**
     * Builds a consistently styled JTable with alternating row colours.
     *
     * @param model       the data model
     * @param bestRowIdx  row index to highlight as BEST (-1 if not applicable)
     */
    private JTable buildStyledTable(DefaultTableModel model, final int bestRowIdx) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    if (row == bestRowIdx) {
                        c.setBackground(COLOR_BEST_BG);
                        c.setForeground(COLOR_BEST_FG);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ROW_ALT);
                        c.setForeground(COLOR_LABEL_FG);
                        c.setFont(FONT_CONTENT);
                    }
                }
                return c;
            }
        };
        applyTableStyle(table);
        return table;
    }

    /** Applies the standard table visual properties (grid, fonts, header). */
    private void applyTableStyle(JTable table) {
        table.setEnabled(false);
        table.setFont(FONT_CONTENT);
        table.setRowHeight(26);
        table.setGridColor(COLOR_BORDER);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(COLOR_HEADER_BG);
        header.setForeground(COLOR_HEADER_FG);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
    }

    /**
     * Wraps a table in a fixed-height scroll pane consistent with existing dialogs.
     *
     * @param table  the table to wrap
     * @param height preferred + maximum height in pixels
     */
    private JScrollPane buildTableScroll(JTable table, int height) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, height));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        scroll.setAlignmentX(LEFT_ALIGNMENT);
        return scroll;
    }

    private DefaultTableCellRenderer makeLeftRenderer() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.LEFT);
        r.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        return r;
    }

    private DefaultTableCellRenderer makeCenterRenderer() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        return r;
    }

    /** Bold step label used as a section heading above each Greedy table. */
    private JLabel buildStepLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_STEP_LABEL);
        lbl.setForeground(COLOR_NAVY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        return lbl;
    }

    // ── Summary panel builders ────────────────────────────────────────────────

    /** Summary panel for Dynamic Programming (reuses result object). */
    private JPanel buildDpSummaryPanel() {
        JPanel panel = buildSummaryShell("Result Summary");

        addSummaryRow(panel, "Optimal Study Score:", String.valueOf(result.getTotalStudyScore()), true);
        panel.add(javax.swing.Box.createVerticalStrut(8));

        JLabel cafesKey = summaryBoldLabel("Selected Cafes:");
        cafesKey.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panel.add(cafesKey);

        for (Cafe cafe : result.getSelectedCafes()) {
            panel.add(summaryBulletLabel(cafe.getName()));
        }

        return panel;
    }

    /**
     * Summary panel for Brute Force — shows combination statistics and
     * the best selection derived from the enumeration.
     */
    private JPanel buildBfSummaryPanel(int total, int valid, int overBudget, int bestScore) {
        JPanel panel = buildSummaryShell("Result Summary");

        // Statistics grid
        JPanel statsGrid = new JPanel(new java.awt.GridLayout(2, 2, 16, 6));
        statsGrid.setBackground(COLOR_PANEL_BG);
        statsGrid.setAlignmentX(LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        statsGrid.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        statsGrid.add(statCell("Total Combinations:",  String.valueOf(total)));
        statsGrid.add(statCell("Valid Combinations:",  String.valueOf(valid)));
        statsGrid.add(statCell("Over Budget:",         String.valueOf(overBudget)));
        statsGrid.add(statCell("Best Study Score:",    String.valueOf(bestScore)));

        panel.add(statsGrid);

        JPanel divider = new JPanel();
        divider.setBackground(COLOR_BORDER);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(divider);
        panel.add(javax.swing.Box.createVerticalStrut(8));

        JLabel cafesKey = summaryBoldLabel("Selected Cafes (Optimal Subset):");
        cafesKey.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panel.add(cafesKey);

        List<Cafe> selected = result.getSelectedCafes();
        if (selected == null || selected.isEmpty()) {
            JLabel none = new JLabel("  None within budget.");
            none.setFont(FONT_CONTENT);
            none.setForeground(COLOR_SUBTITLE);
            none.setAlignmentX(LEFT_ALIGNMENT);
            panel.add(none);
        } else {
            for (Cafe cafe : selected) {
                panel.add(summaryBulletLabel(cafe.getName()));
            }
        }

        return panel;
    }

    /** Summary panel for Greedy — mirrors DP/BF style with Greedy-specific stats. */
    private JPanel buildGreedySummaryPanel() {
        JPanel panel = buildSummaryShell("Result Summary");

        // Stats grid: 1 row × 2 cols
        JPanel statsGrid = new JPanel(new java.awt.GridLayout(1, 2, 16, 6));
        statsGrid.setBackground(COLOR_PANEL_BG);
        statsGrid.setAlignmentX(LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        statsGrid.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        statsGrid.add(statCell("Final Study Score:", String.valueOf(result.getTotalStudyScore())));
        statsGrid.add(statCell("Total Cost (RM):",   String.valueOf(result.getTotalCost())));

        panel.add(statsGrid);

        JPanel divider = new JPanel();
        divider.setBackground(COLOR_BORDER);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(divider);
        panel.add(javax.swing.Box.createVerticalStrut(8));

        JLabel cafesKey = summaryBoldLabel("Selected Cafes:");
        cafesKey.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panel.add(cafesKey);

        List<Cafe> selected = result.getSelectedCafes();
        if (selected == null || selected.isEmpty()) {
            JLabel none = new JLabel("  None within budget.");
            none.setFont(FONT_CONTENT);
            none.setForeground(COLOR_SUBTITLE);
            none.setAlignmentX(LEFT_ALIGNMENT);
            panel.add(none);
        } else {
            for (Cafe cafe : selected) {
                panel.add(summaryBulletLabel(cafe.getName()));
            }
        }

        return panel;
    }

    // ── Summary helper components ─────────────────────────────────────────────

    /** Base panel with the standard titled border used by all summary sections. */
    private JPanel buildSummaryShell(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_PANEL_BG);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                FONT_BORDER_TITLE,
                COLOR_LABEL_FG),
            BorderFactory.createEmptyBorder(6, 10, 10, 10)));
        return panel;
    }

    /** A key–value row where the value is rendered in accent blue. */
    private void addSummaryRow(JPanel parent, String key, String value, boolean accentValue) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.setBackground(COLOR_PANEL_BG);
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(FONT_SUMMARY_BOLD);
        keyLabel.setForeground(COLOR_LABEL_FG);

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(accentValue
                ? new Font("Segoe UI", Font.BOLD, 12)
                : FONT_CONTENT);
        valLabel.setForeground(accentValue ? COLOR_ACCENT : COLOR_LABEL_FG);

        row.add(keyLabel);
        row.add(valLabel);
        parent.add(row);
    }

    /** A small two-label cell used inside statistics grids. */
    private JPanel statCell(String key, String value) {
        JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        cell.setBackground(COLOR_PANEL_BG);

        JLabel k = new JLabel(key);
        k.setFont(FONT_SUMMARY_BOLD);
        k.setForeground(COLOR_LABEL_FG);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 12));
        v.setForeground(COLOR_ACCENT);

        cell.add(k);
        cell.add(v);
        return cell;
    }

    private JLabel summaryBoldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SUMMARY_BOLD);
        lbl.setForeground(COLOR_LABEL_FG);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel summaryBulletLabel(String name) {
        JLabel lbl = new JLabel("  \u2022  " + name);
        lbl.setFont(FONT_CONTENT);
        lbl.setForeground(COLOR_LABEL_FG);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return lbl;
    }

    // ── Convenience factory ───────────────────────────────────────────────────

    public static AlgorithmProcessDialog show(JFrame parent, String algorithmName, Result result, int budget) {
        AlgorithmProcessDialog dialog = new AlgorithmProcessDialog(parent, algorithmName, result, budget);
        dialog.setVisible(true);
        return dialog;
    }
}