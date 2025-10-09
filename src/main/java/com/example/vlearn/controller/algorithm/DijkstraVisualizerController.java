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
 * DijkstraVisualizerController
 *
 * Complete graph visualizer with support for:
 * - Add/Remove/Find nodes
 * - Add/Remove edges
 * - Random graph generation (FIXED: Avoids infinite loop/stuck issue)
 * - BFS / DFS / Dijkstra / MST (Kruskal)
 * - Reset graph / Clear highlights
 */
public class DijkstraVisualizerController {

    @FXML private Pane graphPane;
    @FXML private TextField nodeInputField;
    @FXML private TextField edgeStartField;
    @FXML private TextField edgeEndField;
    @FXML private TextField edgeWeightField;
    @FXML private TextField randomGraphSizeField;
    @FXML private TextField startNodeField; // for Dijkstra
    @FXML private Label messageLabel;
    @FXML private Label nodeCountLabel;
    @FXML private Label edgeCountLabel;

    // Reduced duration for better perceived responsiveness, but still visible
    private final int STEP_DURATION = 350; // ms

    private final Map<String, GraphNode> nodes = new LinkedHashMap<>();
    private final List<GraphEdge> edges = new ArrayList<>();
    // Adjacency list is based on node ID (String) to list of edges
    private final Map<String, List<GraphEdge>> adjacency = new HashMap<>();

    private final Random random = new Random();

    // --------------------- INNER MODELS ---------------------
    private static class GraphNode {
        final String id;
        double x, y;
        Circle circle;
        Text labelText;
        GraphNode(String id) { this.id = id; }
        // For PriorityQueue in Dijkstra
        @Override
        public String toString() { return id; }
    }

    private static class GraphEdge {
        final GraphNode from;
        final GraphNode to;
        final double weight;
        Line line;
        Text weightText;
        GraphEdge(GraphNode from, GraphNode to, double weight) { this.from = from; this.to = to; this.weight = weight; }
        // Checks if this edge connects the two nodes, regardless of order
        boolean connects(GraphNode a, GraphNode b) { return (from == a && to == b) || (from == b && to == a); }
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

        // Use Platform.runLater for non-blocking UI updates
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
        // Efficiently remove edges connected to the node
        edges.removeIf(e -> {
            if (e.from == node || e.to == node) {
                removedEdges.add(e);
                return true;
            }
            return false;
        });

        adjacency.remove(label);
        adjacency.values().forEach(list -> list.removeAll(removedEdges));

        Platform.runLater(() -> {
            graphPane.getChildren().removeAll(node.circle, node.labelText);
            for (GraphEdge e : removedEdges) { graphPane.getChildren().removeAll(e.line, e.weightText); }
            nodes.remove(label);
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
        if (na == null || nb == null || a.equals(b)) { showMessage("Invalid nodes or self-loop attempted."); return; }

        if (findEdgeBetween(na, nb) != null) { showMessage("Edge already exists."); return; }

        double w = 1.0;
        try {
            if (!wText.isBlank()) {
                w = Double.parseDouble(wText);
                if (w <= 0) {
                    showMessage("Weight must be positive. Defaulting to 1.");
                    w = 1.0;
                }
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid weight format. Defaulting to 1.");
            w = 1.0;
        }

        final double weight = w;
        GraphEdge edge = new GraphEdge(na, nb, weight);
        edge.line = createEdgeLine(edge.from, edge.to);
        // Use a formatted string for better display of weights
        edge.weightText = new Text(String.format("%.1f", weight));
        edge.weightText.setFill(Color.BLUE);

        edges.add(edge);
        adjacency.get(a).add(edge); adjacency.get(b).add(edge);

        Platform.runLater(() -> {
            graphPane.getChildren().addAll(edge.line, edge.weightText);
            positionWeightText(edge);
            layoutNodes();
            updateCounts();
            showMessage("Edge " + a + " ↔ " + b + " (Weight: " + String.format("%.1f", weight) + ") added.");
        });
    }

    @FXML
    private void handleRemoveEdge() {
        String a = edgeStartField.getText().trim();
        String b = edgeEndField.getText().trim();
        edgeStartField.clear(); edgeEndField.clear();
        GraphNode na = nodes.get(a), nb = nodes.get(b);
        if (na == null || nb == null) { showMessage("Nodes not found."); return; }

        GraphEdge toRemove = findEdgeBetween(na, nb);
        if (toRemove == null) { showMessage("Edge not found."); return; }

        edges.remove(toRemove);
        adjacency.get(a).remove(toRemove);
        adjacency.get(b).remove(toRemove);

        Platform.runLater(() -> {
            graphPane.getChildren().removeAll(toRemove.line, toRemove.weightText);
            updateCounts();
            showMessage("Edge removed: " + a + " ↔ " + b + ".");
        });
    }

    // --------------------- RANDOM GRAPH ---------------------
    /**
     * FIX: The original method had a do-while loop that could cause the program
     * to hang if all possible edges from a node were already created.
     * This version uses a fixed-limit iteration and removes nodes from a list
     * of potential neighbors to ensure termination and avoid duplicate edge attempts.
     */
    @FXML
    private void handleGenerateRandomGraph() {
        String sizeText = randomGraphSizeField.getText().trim();
        randomGraphSizeField.clear();
        int n;
        try {
            n = Integer.parseInt(sizeText);
            if (n <= 1) throw new IllegalArgumentException("Graph size must be > 1.");
        } catch (Exception e) {
            showMessage("Invalid size. Enter a number greater than 1.");
            return;
        }

        resetGraph();
        List<String> labels = new ArrayList<>();

        // Run all generation logic on the JavaFX thread for clean UI updates
        Platform.runLater(() -> {
            // 1. Create Nodes
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

            // 2. Add Edges safely
            int maxEdgesPerNode = 3; // Max degree limit, as in the original logic

            for (String from : labels) {
                // List of neighbors that haven't been connected to 'from' yet
                List<String> potentialNeighbors = labels.stream()
                        .filter(to -> !to.equals(from) && findEdge(from, to) == null)
                        .collect(Collectors.toCollection(ArrayList::new));

                // Determine how many edges to attempt to add
                int edgesToTry = Math.min(potentialNeighbors.size(), 1 + random.nextInt(Math.min(maxEdgesPerNode, n - 1)));
                int edgesAdded = 0;

                while (edgesAdded < edgesToTry && !potentialNeighbors.isEmpty()) {
                    // Pick a random valid neighbor from the list
                    String to = potentialNeighbors.remove(random.nextInt(potentialNeighbors.size()));

                    double w = 1.0 + random.nextInt(10);
                    GraphNode na = nodes.get(from);
                    GraphNode nb = nodes.get(to);

                    GraphEdge edge = new GraphEdge(na, nb, w);
                    edge.line = createEdgeLine(edge.from, edge.to);
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
            showMessage("Random graph generated with " + n + " nodes and " + edges.size() + " edges.");
        });
    }

    @FXML
    private void handleRunDijkstra() {
        String start = startNodeField.getText().trim();
        startNodeField.clear();
        if (!nodes.containsKey(start)) { showMessage("Start node not found."); return; }

        clearHighlights();
        // Run Dijkstra in a separate thread to prevent blocking the UI
        new Thread(() -> runDijkstra(nodes.get(start))).start();
    }


    // --------------------- GRAPH UTILITY ---------------------
    @FXML
    private void handleResetGraph() { resetGraph(); }

    @FXML
    private void handleClearHighlights() { clearHighlights(); }

    // --------------------- ANIMATION / LAYOUT ---------------------
    private Circle createNodeCircle(double radius, Color fill) { Circle c = new Circle(radius, fill); c.setStroke(Color.web("#4B0082")); c.setStrokeWidth(2); return c; }
    private Line createEdgeLine(GraphNode a, GraphNode b) { Line l = new Line(a.x, a.y, b.x, b.y); l.setStroke(Color.web("#5555FF")); l.setStrokeWidth(2); return l; }
    private void positionWeightText(GraphEdge e) { e.weightText.setX((e.from.x + e.to.x)/2 + 4); e.weightText.setY((e.from.y + e.to.y)/2 - 4); e.weightText.setStyle("-fx-font-size: 11;"); }

    private void layoutNodes() {
        int n = nodes.size(); if (n==0) return;

        // --- INCREASED RADIUS FIX ---
        // Change from '-60' to '-30' to leave less padding, thus increasing the radius.
        double cx = graphPane.getWidth()/2+50, cy = graphPane.getHeight()/2+50;
        double r = Math.min(cx,cy) - 30; // 30 is the new padding/margin
        if (r < 60) r = 60; // Ensure a minimum radius
        // --------------------------

        int idx=0;
        for (GraphNode node : nodes.values()) { double angle=2*Math.PI*idx/n; node.x=cx+r*Math.cos(angle); node.y=cy+r*Math.sin(angle); idx++; }

        Platform.runLater(() -> {
            // Clear and redraw all elements to ensure correct z-order (edges first, then nodes)
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
            nodeCountLabel.setText("Nodes: "+nodes.size());
            edgeCountLabel.setText("Edges: "+edges.size());
        });
    }

    // --------------------- HELPERS ---------------------
    private GraphEdge findEdgeBetween(GraphNode a, GraphNode b) { for (GraphEdge e: edges) if (e.connects(a,b)) return e; return null; }
    private GraphEdge findEdge(String a, String b) { GraphNode na = nodes.get(a), nb = nodes.get(b); if (na==null||nb==null) return null; return findEdgeBetween(na,nb); }

    // Non-blocking sleep for the algorithm thread
    private void sleep(int ms){
        try{
            if (ms > 0) Thread.sleep(ms);
        } catch(InterruptedException ignored){
            Thread.currentThread().interrupt();
        }
    }

    private void showMessage(String msg){
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.web("#FF00FF"));
        });
    }

    // All highlighting should be run on the JavaFX thread using Platform.runLater
    private void highlightNodeCircle(Circle c, Color color){
        if(c==null) return;
        Platform.runLater(() -> {
            FillTransition ft=new FillTransition(Duration.millis(STEP_DURATION),c,(Color)c.getFill(),color);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();
            c.setStroke(color);
        });
        sleep(STEP_DURATION); // Block the algorithm thread to slow down the visualizer
    }

    private void highlightLine(Line l, Color color){
        if(l==null) return;
        Platform.runLater(() -> l.setStroke(color));
        sleep(STEP_DURATION/2); // Block the algorithm thread
    }

    private void clearHighlights(){
        Platform.runLater(() -> {
            nodes.values().forEach(n->{
                if(n.circle!=null){
                    n.circle.setFill(Color.web("#E6E6FA"));
                    n.circle.setStroke(Color.web("#4B0082"));
                }
            });
            edges.forEach(e->{
                if(e.line!=null) e.line.setStroke(Color.web("#5555FF"));
            });
        });
    }

    private void resetGraph(){
        nodes.clear();
        edges.clear();
        adjacency.clear();
        Platform.runLater(() -> {
            graphPane.getChildren().clear();
            updateCounts();
            messageLabel.setText("");
        });
    }


    // --------------------- DIJKSTRA ---------------------
    private void runDijkstra(GraphNode src){
        showMessage("Dijkstra running...");

        Map<GraphNode, Double> dist = new HashMap<>();
        Map<GraphNode, GraphNode> parent = new HashMap<>();

        nodes.values().forEach(n -> dist.put(n, Double.POSITIVE_INFINITY));
        dist.put(src, 0.0);

        // PriorityQueue to manage the nodes to visit, ordered by distance
        // The comparator ensures the correct distance is used after an update
        PriorityQueue<GraphNode> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.addAll(nodes.values());

        // Highlight the start node initially
        highlightNodeCircle(src.circle, Color.web("#7CFC00"));

        while(!pq.isEmpty()){
            GraphNode u = pq.poll();

            // Optimization: If the current distance is infinity, the rest of the unvisited nodes are unreachable
            if(dist.get(u).isInfinite()) break;

            // Highlight the node being processed
            highlightNodeCircle(u.circle, Color.web("#FFA500"));

            for(GraphEdge e: adjacency.get(u.id)){
                GraphNode v = e.from == u ? e.to : e.from;

                // Relaxation step
                double alt = dist.get(u) + e.weight;

                if(alt < dist.get(v)){
                    // Update distance and parent
                    dist.put(v, alt);
                    parent.put(v, u);

                    // Re-insert/update PriorityQueue: Java's PQ doesn't support 'decrease key' efficiently.
                    // The common workaround is to remove and re-add, or just add the new entry
                    // (the old, higher-dist entry will be processed later and ignored).
                    // This implementation uses the 'remove and re-add' strategy for a cleaner queue.
                    pq.remove(v);
                    pq.add(v);

                    highlightLine(e.line, Color.web("#00BFFF"));
                }
            }
        }

        // 3. Highlight the Shortest Paths (Backtracking)
        for(GraphNode target: nodes.values()){
            if(target == src || !parent.containsKey(target)) continue;

            List<GraphNode> path = new ArrayList<>();
            GraphNode cur = target;

            // Build path from target back to source
            path.add(cur);
            while(parent.containsKey(cur)){
                cur = parent.get(cur);
                path.add(cur);
            }
            Collections.reverse(path); // Reverse to get source-to-target order

            // Animate the final path
            for(int i = 0; i < path.size() - 1; i++){
                GraphNode a = path.get(i), b = path.get(i+1);
                GraphEdge e = findEdgeBetween(a, b);

                if(e != null){
                    highlightLine(e.line, Color.web("#FF1744"));
                    // Highlight target node in a final color
                    if (i == path.size() - 2) {
                        highlightNodeCircle(b.circle, Color.web("#FF69B4"));
                    }
                }
            }
        }

        showMessage("Dijkstra complete. Shortest path distances from " + src.id + ": " +
                nodes.values().stream()
                        .map(n -> n.id + "=" + (dist.get(n).isInfinite() ? "Inf" : String.format("%.1f", dist.get(n))))
                        .collect(Collectors.joining(", ")));
    }
}