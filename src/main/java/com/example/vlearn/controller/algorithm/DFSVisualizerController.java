package com.example.vlearn.controller.algorithm;

import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DFSVisualizerController
 *
 * Complete graph visualizer with support for:
 * - Add/Remove/Find nodes
 * - Add/Remove edges
 * - Random graph generation (safe, avoids infinite loops)
 * - Depth-First Search (DFS) visualization
 * - Reset graph / Clear highlights
 */
public class DFSVisualizerController {

    @FXML private Pane graphPane;
    @FXML private TextField nodeInputField;
    @FXML private TextField edgeStartField;
    @FXML private TextField edgeEndField;
    @FXML private TextField edgeWeightField;
    @FXML private TextField randomGraphSizeField;
    @FXML private Label messageLabel;
    @FXML private Label nodeCountLabel;
    @FXML private Label edgeCountLabel;
    @FXML private TextArea pathLogArea;
    @FXML private TextField startNodeField;

    private final int STEP_DURATION = 350; // ms for animation

    private final Map<String, GraphNode> nodes = new LinkedHashMap<>();
    private final List<GraphEdge> edges = new ArrayList<>();
    private final Map<String, List<GraphEdge>> adjacency = new HashMap<>();
    private final Random random = new Random();

    // --------------------- INNER MODELS ---------------------
    private static class GraphNode {
        final String id;
        double x, y;
        Circle circle;
        Text labelText;

        GraphNode(String id) { this.id = id; }
        @Override
        public String toString() { return id; }
    }

    private static class GraphEdge {
        final GraphNode from;
        final GraphNode to;
        final double weight;
        Line line;
        Text weightText;

        GraphEdge(GraphNode from, GraphNode to, double weight) {
            this.from = from; this.to = to; this.weight = weight;
        }

        boolean connects(GraphNode a, GraphNode b) {
            return (from == a && to == b) || (from == b && to == a);
        }
    }

    // --------------------- NODE / EDGE OPERATIONS ---------------------
    @FXML
    private void handleAddNode() {
        String label = nodeInputField.getText().trim();
        if (label.isBlank()) { showMessage("Enter node label."); return; }
        nodeInputField.clear();
        if (nodes.containsKey(label)) { showMessage("Node already exists."); return; }

        GraphNode node = new GraphNode(label);
        node.circle = createNodeCircle(20, Color.web("#E6E6FA"));
        node.labelText = new Text(label);
        node.labelText.setStyle("-fx-font-weight: bold;");
        nodes.put(label, node);
        adjacency.put(label, new ArrayList<>());

        Platform.runLater(() -> {
            graphPane.getChildren().addAll(node.circle, node.labelText);
            layoutNodes();
            updateCounts();
            showMessage("Node '" + label + "' added.");
        });
    }

    @FXML
    private void handleRemoveNode() {
        String label = nodeInputField.getText().trim();
        if (label.isBlank()) { showMessage("Enter node label to remove."); return; }
        nodeInputField.clear();
        GraphNode node = nodes.get(label);
        if (node == null) { showMessage("Node not found."); return; }

        List<GraphEdge> removedEdges = new ArrayList<>();
        edges.removeIf(e -> {
            if (e.from == node || e.to == node) {
                removedEdges.add(e);
                return true;
            }
            return false;
        });
        adjacency.remove(label);
        adjacency.values().forEach(list -> list.removeAll(removedEdges));
        nodes.remove(label);

        Platform.runLater(() -> {
            graphPane.getChildren().removeAll(node.circle, node.labelText);
            for (GraphEdge e : removedEdges) graphPane.getChildren().removeAll(e.line, e.weightText);
            layoutNodes();
            updateCounts();
            showMessage("Node '" + label + "' removed.");
        });
    }

    @FXML
    private void handleAddEdge() {
        String a = edgeStartField.getText().trim();
        String b = edgeEndField.getText().trim();
        String wText = edgeWeightField.getText().trim();
        edgeStartField.clear(); edgeEndField.clear(); edgeWeightField.clear();

        GraphNode na = nodes.get(a), nb = nodes.get(b);
        if (na == null || nb == null || a.equals(b)) { showMessage("Invalid nodes or self-loop."); return; }

        if (findEdgeBetween(na, nb) != null) { showMessage("Edge already exists."); return; }

        double w = 1.0;
        try {
            if (!wText.isBlank()) {
                w = Double.parseDouble(wText);
                if (w <= 0) { w = 1.0; showMessage("Weight must be positive. Defaulting to 1."); }
            }
        } catch (NumberFormatException e) { w = 1.0; showMessage("Invalid weight. Defaulting to 1."); }

        GraphEdge edge = new GraphEdge(na, nb, w);
        edge.line = createEdgeLine(na, nb);
        edge.weightText = new Text(String.format("%.1f", w));
        edge.weightText.setFill(Color.BLUE);

        edges.add(edge);
        adjacency.get(a).add(edge); adjacency.get(b).add(edge);

        Platform.runLater(() -> {
            graphPane.getChildren().addAll(edge.line, edge.weightText);
            positionWeightText(edge);
            layoutNodes();
            updateCounts();
            showMessage("Edge " + a + " ↔ " + b + " added.");
        });
    }

    @FXML
    private void handleRemoveEdge() {
        String a = edgeStartField.getText().trim();
        String b = edgeEndField.getText().trim();
        edgeStartField.clear(); edgeEndField.clear();
        GraphNode na = nodes.get(a), nb = nodes.get(b);
        if (na == null || nb == null) { showMessage("Nodes not found."); return; }

        GraphEdge edge = findEdgeBetween(na, nb);
        if (edge == null) { showMessage("Edge not found."); return; }

        edges.remove(edge);
        adjacency.get(a).remove(edge);
        adjacency.get(b).remove(edge);

        Platform.runLater(() -> {
            graphPane.getChildren().removeAll(edge.line, edge.weightText);
            updateCounts();
            showMessage("Edge removed: " + a + " ↔ " + b + ".");
        });
    }

    // --------------------- RANDOM GRAPH ---------------------
    @FXML
    private void handleGenerateRandomGraph() {
        String sizeText = randomGraphSizeField.getText().trim();
        randomGraphSizeField.clear();
        int n;
        try {
            n = Integer.parseInt(sizeText);
            if (n <= 1) throw new IllegalArgumentException();
        } catch (Exception e) {
            showMessage("Invalid size. Enter a number > 1."); return;
        }

        resetGraph();
        List<String> labels = new ArrayList<>();

        Platform.runLater(() -> {
            // Create nodes
            for (int i = 1; i <= n; i++) {
                String label = "N" + i;
                labels.add(label);

                GraphNode node = new GraphNode(label);
                node.circle = createNodeCircle(20, Color.web("#E6E6FA"));
                node.labelText = new Text(label);
                node.labelText.setStyle("-fx-font-weight: bold;");

                nodes.put(label, node);
                adjacency.put(label, new ArrayList<>());
                graphPane.getChildren().addAll(node.circle, node.labelText);
            }
            layoutNodes();

            // Add edges
            int maxEdgesPerNode = 3;
            for (String from : labels) {
                List<String> potentialNeighbors = labels.stream()
                        .filter(to -> !to.equals(from) && findEdge(from, to) == null)
                        .collect(Collectors.toCollection(ArrayList::new));

                int edgesToTry = Math.min(potentialNeighbors.size(), 1 + random.nextInt(Math.min(maxEdgesPerNode, n - 1)));
                int edgesAdded = 0;

                while (edgesAdded < edgesToTry && !potentialNeighbors.isEmpty()) {
                    String to = potentialNeighbors.remove(random.nextInt(potentialNeighbors.size()));
                    double w = 1 + random.nextInt(10);
                    GraphNode na = nodes.get(from);
                    GraphNode nb = nodes.get(to);

                    GraphEdge edge = new GraphEdge(na, nb, w);
                    edge.line = createEdgeLine(na, nb);
                    edge.weightText = new Text(String.format("%.1f", w));
                    edge.weightText.setFill(Color.BLUE);
                    positionWeightText(edge);

                    edges.add(edge);
                    adjacency.get(from).add(edge);
                    adjacency.get(to).add(edge);
                    graphPane.getChildren().addAll(edge.line, edge.weightText);

                    edgesAdded++;
                }
            }

            layoutNodes();
            updateCounts();
            showMessage("Random graph generated with " + n + " nodes.");
        });
    }

    // --------------------- DFS ---------------------
    @FXML
    private void handleRunDFS() {
        pathLogArea.clear();
        String startLabel = startNodeField.getText().trim();
        if (startLabel.isEmpty()) { showMessage("Enter start node label."); return; }
        if (!nodes.containsKey(startLabel)) { showMessage("Start node not found."); return; }

        new Thread(() -> {
            Platform.runLater(() -> showMessage("DFS from '" + startLabel + "'..."));
            Set<GraphNode> visited = new HashSet<>();
            dfs(nodes.get(startLabel), visited);
            Platform.runLater(() -> showMessage("DFS completed."));
        }).start();
    }


    private void dfs(GraphNode u, Set<GraphNode> visited) {
        visited.add(u);
        Platform.runLater(() -> {
            highlightNodeCircle(u.circle, Color.web("#00FFFF"));
            logPath("Visited node: " + u.id);
        });
        sleep(STEP_DURATION);

        for (GraphEdge e : adjacency.get(u.id)) {
            GraphNode v = e.from == u ? e.to : e.from;
            if (!visited.contains(v)) {
                Platform.runLater(() -> {
                    highlightLine(e.line, Color.web("#FFB900"));
                    logPath("Traversing edge: " + e.from.id + " ↔ " + e.to.id);
                });
                sleep(STEP_DURATION);
                dfs(v, visited);
            }
        }
    }

    // --------------------- GRAPH UTILITIES ---------------------
    @FXML private void handleResetGraph() { resetGraph(); }
    @FXML private void handleClearHighlights() { clearHighlights(); }

    private Circle createNodeCircle(double radius, Color fill) {
        Circle c = new Circle(radius, fill);
        c.setStroke(Color.web("#4B0082"));
        c.setStrokeWidth(2);
        return c;
    }

    private Line createEdgeLine(GraphNode a, GraphNode b) {
        Line l = new Line(a.x, a.y, b.x, b.y);
        l.setStroke(Color.web("#5555FF"));
        l.setStrokeWidth(2);
        return l;
    }

    private void positionWeightText(GraphEdge e) {
        e.weightText.setX((e.from.x + e.to.x)/2 + 4);
        e.weightText.setY((e.from.y + e.to.y)/2 - 4);
        e.weightText.setStyle("-fx-font-size: 11;");
    }

    private void layoutNodes() {
        int n = nodes.size(); if (n == 0) return;

        double cx = graphPane.getWidth() / 2 + 50;
        double cy = graphPane.getHeight() / 2 + 50;
        double r = Math.max(60, Math.min(cx, cy) - 30);

        int idx = 0;
        for (GraphNode node : nodes.values()) {
            double angle = 2 * Math.PI * idx / n;
            node.x = cx + r * Math.cos(angle);
            node.y = cy + r * Math.sin(angle);
            idx++;
        }

        Platform.runLater(() -> {
            graphPane.getChildren().clear();
            for (GraphEdge e : edges) {
                e.line.setStartX(e.from.x); e.line.setStartY(e.from.y);
                e.line.setEndX(e.to.x); e.line.setEndY(e.to.y);
                positionWeightText(e);
                graphPane.getChildren().addAll(e.line, e.weightText);
            }
            for (GraphNode node : nodes.values()) {
                node.circle.setCenterX(node.x); node.circle.setCenterY(node.y);
                node.labelText.setX(node.x - node.labelText.getLayoutBounds().getWidth()/2);
                node.labelText.setY(node.y + 5);
                graphPane.getChildren().addAll(node.circle, node.labelText);
            }
        });
    }

    private void updateCounts() {
        Platform.runLater(() -> {
            nodeCountLabel.setText("Nodes: " + nodes.size());
            edgeCountLabel.setText("Edges: " + edges.size());
        });
    }

    private GraphEdge findEdgeBetween(GraphNode a, GraphNode b) {
        for (GraphEdge e : edges) if (e.connects(a, b)) return e;
        return null;
    }

    private GraphEdge findEdge(String a, String b) {
        GraphNode na = nodes.get(a), nb = nodes.get(b);
        if (na == null || nb == null) return null;
        return findEdgeBetween(na, nb);
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }

    private void showMessage(String msg) {
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.web("#FF00FF"));
        });
    }

    private void logPath(String msg) {
        Platform.runLater(() -> pathLogArea.appendText(msg + "\n"));
    }

    private void highlightNodeCircle(Circle c, Color color) {
        if (c == null) return;
        Platform.runLater(() -> {
            FillTransition ft = new FillTransition(Duration.millis(STEP_DURATION), c, (Color)c.getFill(), color);
            ft.setCycleCount(2); ft.setAutoReverse(true); ft.play();
            c.setStroke(color);
        });
        sleep(STEP_DURATION);
    }

    private void highlightLine(Line l, Color color) {
        if (l == null) return;
        Platform.runLater(() -> l.setStroke(color));
        sleep(STEP_DURATION / 2);
    }

    private void clearHighlights() {
        Platform.runLater(() -> {
            nodes.values().forEach(n -> {
                if (n.circle != null) { n.circle.setFill(Color.web("#E6E6FA")); n.circle.setStroke(Color.web("#4B0082")); }
            });
            edges.forEach(e -> { if (e.line != null) e.line.setStroke(Color.web("#5555FF")); });
        });
    }

    private void resetGraph() {
        nodes.clear(); edges.clear(); adjacency.clear();
        Platform.runLater(() -> {
            graphPane.getChildren().clear();
            updateCounts();
            messageLabel.setText("");
        });
    }
}
