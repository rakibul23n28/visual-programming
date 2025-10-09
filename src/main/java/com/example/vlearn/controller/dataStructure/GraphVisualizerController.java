package com.example.vlearn.controller.dataStructure;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * GraphVisualizerController
 *
 * Step-by-step / animated graph visualizer inspired by BinaryTreeVisualizerController design.
 *
 * Features:
 *  - add/remove/find nodes
 *  - add/remove edges (optional weight)
 *  - BFS / DFS (visualized)
 *  - Dijkstra (visualized; asks for source and target via dialog)
 *  - Kruskal MST (visualized)
 *  - reset graph, clear highlights
 *
 * Note: this is a self-contained controller class that renders Nodes as Circles + Text and Edges as Lines.
 */
public class GraphVisualizerController {

    @FXML private Pane graphPane;
    @FXML private TextField nodeInputField;
    @FXML private TextField edgeStartField;
    @FXML private TextField edgeEndField;
    @FXML private TextField edgeWeightField;
    @FXML private Label messageLabel;
    @FXML private Label nodeCountLabel;
    @FXML private Label edgeCountLabel;
    @FXML private TextArea pathLogArea;


    // Step duration in ms for animations (match style of BinaryTreeVisualizerController)
    private final int STEP_DURATION = 450;

    // Graph data structures
    private final Map<String, GraphNode> nodes = new LinkedHashMap<>(); // preserve insertion order
    private final List<GraphEdge> edges = new ArrayList<>();
    private final Map<String, List<GraphEdge>> adjacency = new HashMap<>();

    // ----------------- INNER MODELS -----------------
    private static class GraphNode {
        final String id;           // label
        double x, y;               // layout coordinates
        Circle circle;
        Text labelText;

        GraphNode(String id) { this.id = id; }
    }

    private void logPath(String message) {
        Platform.runLater(() -> {
            pathLogArea.appendText(message + "\n");
        });
    }


    private static class GraphEdge {
        final GraphNode from;
        final GraphNode to;
        final double weight;
        Line line;
        Text weightText;

        GraphEdge(GraphNode from, GraphNode to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        // undirected equality check for convenience
        boolean connects(GraphNode a, GraphNode b) {
            return (from == a && to == b) || (from == b && to == a);
        }
    }

    // ----------------- UI HANDLERS -----------------

    @FXML
    private void handleAddNode() {
        String label = nodeInputField.getText();
        if (label == null || label.isBlank()) {
            showMessage("Enter a node label.");
            return;
        }
        nodeInputField.clear();

        if (nodes.containsKey(label)) {
            showMessage("Node '" + label + "' already exists.");
            return;
        }

        new Thread(() -> {
            Platform.runLater(() -> showMessage("Adding node '" + label + "'..."));
            GraphNode node = new GraphNode(label);
            // create visuals
            Platform.runLater(() -> {
                node.circle = createNodeCircle(20, Color.web("#E6E6FA"));
                node.labelText = new Text(label);
                node.labelText.setStyle("-fx-font-weight: bold;");
                graphPane.getChildren().addAll(node.circle, node.labelText);
            });
            sleep(STEP_DURATION);
            // store
            nodes.put(label, node);
            adjacency.put(label, new ArrayList<>());
            Platform.runLater(() -> {
                layoutNodes();   // simple layout after change
                updateCounts();
                showMessage("Node '" + label + "' added.");
            });
        }).start();
    }

    @FXML
    private void handleRemoveNode() {
        String label = nodeInputField.getText();
        if (label == null || label.isBlank()) {
            showMessage("Enter node label to remove.");
            return;
        }
        nodeInputField.clear();

        if (!nodes.containsKey(label)) {
            showMessage("Node '" + label + "' not found.");
            return;
        }

        new Thread(() -> {
            Platform.runLater(() -> showMessage("Removing node '" + label + "'..."));
            GraphNode toRemove = nodes.get(label);

            // highlight node to remove
            Platform.runLater(() -> highlightNodeCircle(toRemove.circle, Color.web("#FF1744")));
            sleep(STEP_DURATION);

            // remove edges incident to this node
            List<GraphEdge> removedEdges = new ArrayList<>();
            // iterate copy to avoid concurrent modification
            for (Iterator<GraphEdge> it = edges.iterator(); it.hasNext();) {
                GraphEdge e = it.next();
                if (e.from == toRemove || e.to == toRemove) {
                    removedEdges.add(e);
                    it.remove();
                }
            }
            // remove from adjacency lists
            adjacency.remove(label);
            for (List<GraphEdge> lst : adjacency.values()) {
                lst.removeIf(removedEdges::contains);
            }

            // remove visuals
            Platform.runLater(() -> {
                graphPane.getChildren().removeAll(toRemove.circle, toRemove.labelText);
                for (GraphEdge e : removedEdges) {
                    if (e.line != null) graphPane.getChildren().remove(e.line);
                    if (e.weightText != null) graphPane.getChildren().remove(e.weightText);
                }
                nodes.remove(label);
                layoutNodes();
                updateCounts();
                showMessage("Node '" + label + "' removed.");
            });
        }).start();
    }

    @FXML
    private void handleFindNode() {
        String label = nodeInputField.getText();
        if (label == null || label.isBlank()) {
            showMessage("Enter node label to find.");
            return;
        }
        nodeInputField.clear();

        new Thread(() -> {
            if (!nodes.containsKey(label)) {
                Platform.runLater(() -> showMessage("Node '" + label + "' not found."));
                return;
            }
            GraphNode node = nodes.get(label);
            Platform.runLater(() -> showMessage("Finding node '" + label + "'..."));
            // animate highlight
            Platform.runLater(() -> highlightNodeCircle(node.circle, Color.web("#FFFF00"))); // yellow
            sleep(STEP_DURATION * 2);
            Platform.runLater(() -> highlightNodeCircle(node.circle, Color.web("#00FF00"))); // green final
            Platform.runLater(() -> showMessage("Found node '" + label + "'."));
        }).start();
    }

    @FXML
    private void handleAddEdge() {
        String a = edgeStartField.getText();
        String b = edgeEndField.getText();
        String wText = edgeWeightField.getText();

        // --- Validation ---
        if (a == null || a.isBlank() || b == null || b.isBlank()) {
            showMessage("Enter both 'From' and 'To' node labels.");
            return;
        }

        edgeStartField.clear();
        edgeEndField.clear();
        edgeWeightField.clear();

        if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
            showMessage("One or both nodes not found.");
            return;
        }
        if (a.equals(b)) {
            showMessage("Self-loops not supported.");
            return;
        }

        // --- Parse weight safely ---
        double tempWeight = 1;
        if (wText != null && !wText.isBlank()) {
            try {
                tempWeight = Double.parseDouble(wText);
            } catch (NumberFormatException e) {
                showMessage("Invalid weight; defaulting to 1.");
            }
        }
        final double finalWeight = tempWeight; // ✅ final for lambda

        GraphNode from = nodes.get(a);
        GraphNode to = nodes.get(b);

        // --- Prevent duplicate edges (undirected) ---
        for (GraphEdge e : edges) {
            if (e.connects(from, to)) {
                showMessage("Edge already exists.");
                return;
            }
        }

        // --- Background thread for smooth animation ---
        new Thread(() -> {
            Platform.runLater(() -> showMessage("Adding edge " + a + " ↔ " + b + " ..."));

            // Create edge as final so lambda can access it
            final GraphEdge edge = new GraphEdge(from, to, finalWeight);

            Platform.runLater(() -> {
                edge.line = createEdgeLine(from, to);
                edge.weightText = new Text(String.valueOf(finalWeight));

                // --- Set weight text color ---
                edge.weightText.setFill(Color.BLUE); // change to any color you like

                graphPane.getChildren().addAll(edge.line, edge.weightText);
                positionWeightText(edge);
            });

            // Animate small delay
            sleep(STEP_DURATION);

            // --- Update graph data structures ---
            edges.add(edge);
            adjacency.get(a).add(edge);
            adjacency.get(b).add(edge);

            // --- Update counts and notify user ---
            Platform.runLater(() -> {
                updateCounts();
                showMessage("Edge " + a + " ↔ " + b + " added.");
            });
        }).start();
    }



    @FXML
    private void handleRemoveEdge() {
        String a = edgeStartField.getText();
        String b = edgeEndField.getText();
        if (a == null || a.isBlank() || b == null || b.isBlank()) {
            showMessage("Enter both 'From' and 'To' node labels.");
            return;
        }
        edgeStartField.clear();
        edgeEndField.clear();

        if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
            showMessage("One or both nodes not found.");
            return;
        }
        GraphNode na = nodes.get(a), nb = nodes.get(b);
        GraphEdge toRemove = null;
        for (GraphEdge e : edges) if (e.connects(na, nb)) { toRemove = e; break; }
        if (toRemove == null) { showMessage("Edge not found."); return; }

        GraphEdge finalToRemove = toRemove;
        new Thread(() -> {
            Platform.runLater(() -> showMessage("Removing edge " + a + " ↔ " + b + " ..."));
            Platform.runLater(() -> highlightLine(finalToRemove.line, Color.web("#FF1744")));
            sleep(STEP_DURATION);
            edges.remove(finalToRemove);
            adjacency.get(a).remove(finalToRemove);
            adjacency.get(b).remove(finalToRemove);
            Platform.runLater(() -> {
                graphPane.getChildren().removeAll(finalToRemove.line, finalToRemove.weightText);
                updateCounts();
                showMessage("Edge removed.");
            });
        }).start();
    }

    @FXML
    private void handleBFS() {
        pathLogArea.clear();
        // Ask for starting node
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("BFS Start Node");
        dialog.setHeaderText("Breadth-First Search");
        dialog.setContentText("Enter start node label:");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;
        String startLabel = result.get();
        if (!nodes.containsKey(startLabel)) { showMessage("Start node not found."); return; }

        new Thread(() -> bfs(nodes.get(startLabel))).start();
    }

    private void bfs(GraphNode start) {
        Platform.runLater(() -> showMessage("BFS from '" + start.id + "'..."));
        Set<GraphNode> visited = new HashSet<>();
        Queue<GraphNode> q = new LinkedList<>();
        q.add(start);
        visited.add(start);

        while (!q.isEmpty()) {
            GraphNode u = q.poll();
            Platform.runLater(() -> {
                highlightNodeCircle(u.circle, Color.web("#39FF14"));
                logPath("Visited node: " + u.id);
            });
            sleep(STEP_DURATION);

            // visit neighbors via adjacency edges
            for (GraphEdge e : adjacency.get(u.id)) {
                GraphNode v = e.from == u ? e.to : e.from;
                if (!visited.contains(v)) {
                    visited.add(v);
                    q.add(v);
                    Platform.runLater(() -> {
                        highlightLine(e.line, Color.web("#00BFFF")); // edge traversal highlight
                        highlightNodeCircle(v.circle, Color.web("#FFFF00")); // discovered
                        logPath("Discovered node: " + v.id + " via edge " + e.from.id + "↔" + e.to.id);
                    });
                    sleep(STEP_DURATION);
                }
            }
        }
        Platform.runLater(() -> showMessage("BFS completed."));
    }

    @FXML
    private void handleDFS() {
        pathLogArea.clear();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("DFS Start Node");
        dialog.setHeaderText("Depth-First Search");
        dialog.setContentText("Enter start node label:");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;
        String startLabel = result.get();
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
                    logPath("Traversing edge: " + e.from.id + "↔" + e.to.id);
                });
                sleep(STEP_DURATION);
                dfs(v, visited);
            }
        }
    }

    @FXML
    private void handleDijkstra() {
        pathLogArea.clear();
        // Ask for source and target
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dijkstra");
        dialog.setHeaderText("Dijkstra Shortest Path");
        dialog.setContentText("Enter source node label:");
        Optional<String> srcOpt = dialog.showAndWait();
        if (srcOpt.isEmpty()) return;
        dialog = new TextInputDialog();
        dialog.setTitle("Dijkstra");
        dialog.setHeaderText("Dijkstra Shortest Path");
        dialog.setContentText("Enter target node label:");
        Optional<String> tgtOpt = dialog.showAndWait();
        if (tgtOpt.isEmpty()) return;
        String src = srcOpt.get(), tgt = tgtOpt.get();
        if (!nodes.containsKey(src) || !nodes.containsKey(tgt)) { showMessage("Source/target not found."); return; }

        new Thread(() -> dijkstra(nodes.get(src), nodes.get(tgt))).start();
    }

    private void dijkstra(GraphNode src, GraphNode tgt) {
        Platform.runLater(() -> showMessage("Dijkstra " + src.id + " → " + tgt.id + "..."));

        Map<GraphNode, Double> dist = new HashMap<>();
        Map<GraphNode, GraphNode> parent = new HashMap<>();
        for (GraphNode n : nodes.values()) dist.put(n, Double.POSITIVE_INFINITY);
        dist.put(src, 0.0);

        Comparator<GraphNode> cmp = Comparator.comparingDouble(dist::get);
        PriorityQueue<GraphNode> pq = new PriorityQueue<>(cmp);
        pq.addAll(nodes.values());

        while (!pq.isEmpty()) {
            GraphNode u = pq.poll();
            if (dist.get(u).isInfinite()) break;
            Platform.runLater(() -> {
                highlightNodeCircle(u.circle, Color.web("#7CFC00"));
                logPath("Processing node: " + u.id + " (current distance: " + dist.get(u) + ")");
            });
            sleep(STEP_DURATION);

            if (u == tgt) break;

            for (GraphEdge e : adjacency.get(u.id)) {
                GraphNode v = e.from == u ? e.to : e.from;
                double alt = dist.get(u) + e.weight;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    parent.put(v, u);
                    pq.remove(v);
                    pq.add(v);
                    GraphEdge highlightEdge = e;
                    Platform.runLater(() -> {
                        highlightLine(highlightEdge.line, Color.web("#00BFFF"));
                        logPath("Relaxing edge: " + e.from.id + "↔" + e.to.id + " (weight " + e.weight + "), new distance to " + v.id + ": " + alt);
                    });
                    sleep(STEP_DURATION);
                }
            }
        }

        if (!parent.containsKey(tgt) && src != tgt) {
            Platform.runLater(() -> showMessage("No path found."));
            return;
        }

        List<GraphNode> path = new ArrayList<>();
        GraphNode cur = tgt;
        path.add(cur);
        while (parent.containsKey(cur)) {
            cur = parent.get(cur);
            path.add(cur);
        }
        Collections.reverse(path);

        Platform.runLater(() -> showMessage("Shortest path found (highlighting)."));
        for (int i = 0; i < path.size() - 1; i++) {
            GraphNode a = path.get(i);
            GraphNode b = path.get(i + 1);
            GraphEdge connecting = findEdgeBetween(a, b);
            if (connecting != null) {
                GraphEdge e = connecting;
                Platform.runLater(() -> {
                    highlightLine(e.line, Color.web("#FF1744"));
                    highlightNodeCircle(b.circle, Color.web("#FF69B4"));
                    logPath("Path edge: " + a.id + "↔" + b.id);
                });
                sleep(STEP_DURATION);
            }
        }
        Platform.runLater(() -> showMessage("Dijkstra complete. Distance: " + dist.getOrDefault(tgt, Double.POSITIVE_INFINITY)));
    }


    @FXML
    private void handleMST() {
        new Thread(() -> {
            Platform.runLater(() -> showMessage("Kruskal MST running..."));
            List<GraphEdge> sorted = edges.stream()
                    .sorted(Comparator.comparingDouble(e -> e.weight))
                    .collect(Collectors.toList());

            UnionFind uf = new UnionFind(new ArrayList<>(nodes.keySet()));
            List<GraphEdge> chosen = new ArrayList<>();
            for (GraphEdge e : sorted) {
                String a = e.from.id, b = e.to.id;
                if (!uf.connected(a, b)) {
                    uf.union(a, b);
                    chosen.add(e);
                    Platform.runLater(() -> {
                        highlightLine(e.line, Color.web("#00FF7F")); // chosen edge highlight
                        logPath("Edge chosen for MST: " + e.from.id + "↔" + e.to.id + " (weight " + e.weight + ")");
                    });
                    sleep(STEP_DURATION);
                } else {
                    Platform.runLater(() -> {
                        highlightLine(e.line, Color.web("#808080")); // rejected gray
                        logPath("Edge rejected (would form cycle): " + e.from.id + "↔" + e.to.id + " (weight " + e.weight + ")");
                    });
                    sleep(STEP_DURATION / 2);
                }
            }
            Platform.runLater(() -> showMessage("Kruskal complete. MST edges: " + chosen.size()));
        }).start();
    }


    @FXML
    private void handleResetGraph() {
        new Thread(() -> {
            Platform.runLater(() -> showMessage("Resetting graph..."));
            sleep(STEP_DURATION);
            Platform.runLater(this::resetGraph);
        }).start();
    }

    @FXML
    private void handleClearHighlights() {
        new Thread(() -> {
            Platform.runLater(() -> showMessage("Clearing highlights..."));
            sleep(STEP_DURATION / 2);
            Platform.runLater(this::clearHighlights);
        }).start();
    }

    // ----------------- HELPERS / RENDERING -----------------

    private Circle createNodeCircle(double radius, Color fill) {
        Circle c = new Circle(radius, fill);
        c.setStroke(Color.web("#4B0082"));
        c.setStrokeWidth(2);
        return c;
    }

    private Line createEdgeLine(GraphNode a, GraphNode b) {
        Line line = new Line(a.x, a.y, b.x, b.y);
        line.setStroke(Color.web("#5555FF"));
        line.setStrokeWidth(2);
        // keep line endpoints updated on node movement/layout (we re-layout and reposition on each layoutNodes call)
        return line;
    }

    private void positionWeightText(GraphEdge e) {
        double mx = (e.from.x + e.to.x) / 2;
        double my = (e.from.y + e.to.y) / 2;
        e.weightText.setX(mx + 4);
        e.weightText.setY(my - 4);
        e.weightText.setStyle("-fx-font-size: 11;");
    }

    private void layoutNodes() {
        // Simple circular layout to space nodes evenly
        int n = nodes.size();
        if (n == 0) return;
        double centerX = graphPane.getWidth() / 2 + 50;
        double centerY = graphPane.getHeight() / 2 + 50;
        double radius = Math.min(centerX, centerY) - 60;
        if (radius < 60) radius = 60;

        int idx = 0;
        for (GraphNode node : nodes.values()) {
            double angle = 2 * Math.PI * idx / n;
            node.x = centerX + radius * Math.cos(angle);
            node.y = centerY + radius * Math.sin(angle);
            idx++;
        }

        // Now apply positions to visuals and redraw edges
        Platform.runLater(() -> {
            // remove all edges and nodes from pane then re-add in correct stacking order (lines behind circles)
            graphPane.getChildren().clear();

            // draw edge lines first
            for (GraphEdge e : edges) {
                if (e.line == null) e.line = new Line();
                e.line.setStartX(e.from.x);
                e.line.setStartY(e.from.y);
                e.line.setEndX(e.to.x);
                e.line.setEndY(e.to.y);
                e.line.setStrokeWidth(2);
                if (!graphPane.getChildren().contains(e.line)) graphPane.getChildren().add(e.line);

                // weight text
                if (e.weightText == null) e.weightText = new Text(String.valueOf(e.weight));
                positionWeightText(e);
                if (!graphPane.getChildren().contains(e.weightText)) graphPane.getChildren().add(e.weightText);
            }

            // draw node visuals above edges
            for (GraphNode node : nodes.values()) {
                if (node.circle == null) node.circle = createNodeCircle(20, Color.web("#E6E6FA"));
                node.circle.setCenterX(node.x);
                node.circle.setCenterY(node.y);
                if (!graphPane.getChildren().contains(node.circle))
                    graphPane.getChildren().add(node.circle);

                node.labelText.setX(node.x - (node.labelText.getLayoutBounds().getWidth() / 2));
                node.labelText.setY(node.y + 5);
                if (!graphPane.getChildren().contains(node.labelText))
                    graphPane.getChildren().add(node.labelText);
            }
        });
    }

    private void renderGraph() {
        layoutNodes();
        updateCounts();
    }

    private void updateCounts() {
        nodeCountLabel.setText("Nodes: " + nodes.size());
        edgeCountLabel.setText("Edges: " + edges.size());
    }

    private GraphEdge findEdgeBetween(GraphNode a, GraphNode b) {
        for (GraphEdge e : edges) if (e.connects(a, b)) return e;
        return null;
    }

    private void clearHighlights() {
        // restore node fill and edge color
        for (GraphNode node : nodes.values()) {
            if (node.circle != null) {
                node.circle.setFill(Color.web("#E6E6FA"));
                node.circle.setStroke(Color.web("#4B0082"));
            }
        }
        for (GraphEdge e : edges) {
            if (e.line != null) {
                e.line.setStroke(Color.web("#5555FF"));
            }
        }
    }

    private void resetGraph() {
        nodes.clear();
        edges.clear();
        adjacency.clear();
        graphPane.getChildren().clear();
        updateCounts();
        messageLabel.setText("");
    }

    // ----------------- VISUAL ANIMATIONS -----------------

    private void highlightNodeCircle(Circle circle, Color color) {
        if (circle == null) return;
        FillTransition ft = new FillTransition(Duration.millis(STEP_DURATION), circle, (Color) circle.getFill(), color);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
        circle.setStroke(color);
        // synchronous-like delay for step-by-step feel
        sleep(STEP_DURATION);
    }

    private void highlightLine(Line line, Color color) {
        if (line == null) return;
        Color prev = (Color) (line.getStroke() instanceof Color ? line.getStroke() : Color.web("#5555FF"));
        // quickly animate by setting stroke, then revert after a small pause (we don't have a stroke transition here)
        line.setStroke(color);
        sleep(STEP_DURATION / 2);
        // keep color (some algorithms keep chosen color); we won't auto-revert here so user can see result
    }

    // ----------------- UTILITIES -----------------

    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.web("#FF00FF"));
        // auto-clear after 3s if unchanged
        new Thread(() -> {
            sleep(3000);
            Platform.runLater(() -> {
                if (messageLabel.getText().equals(message)) messageLabel.setText("");
            });
        }).start();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    // ----------------- HELPERS: DATA STRUCTURES -----------------

    /**
     * Find an edge object connecting labels a and b (if exists).
     */
    private GraphEdge findEdge(String a, String b) {
        GraphNode na = nodes.get(a), nb = nodes.get(b);
        if (na == null || nb == null) return null;
        return findEdgeBetween(na, nb);
    }

    /**
     * Simple Union-Find implementation using node labels (strings).
     */
    private static class UnionFind {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();

        UnionFind(List<String> items) {
            for (String s : items) {
                parent.put(s, s);
                rank.put(s, 0);
            }
        }
        private String find(String x) {
            String p = parent.get(x);
            if (p == null) return null;
            if (!p.equals(x)) parent.put(x, find(p));
            return parent.get(x);
        }
        void union(String a, String b) {
            String ra = find(a), rb = find(b);
            if (ra == null || rb == null) return;
            if (ra.equals(rb)) return;
            int raRank = rank.getOrDefault(ra, 0);
            int rbRank = rank.getOrDefault(rb, 0);
            if (raRank < rbRank) parent.put(ra, rb);
            else if (raRank > rbRank) parent.put(rb, ra);
            else {
                parent.put(rb, ra);
                rank.put(ra, raRank + 1);
            }
        }
        boolean connected(String a, String b) {
            String ra = find(a), rb = find(b);
            if (ra == null || rb == null) return false;
            return ra.equals(rb);
        }
    }
}
