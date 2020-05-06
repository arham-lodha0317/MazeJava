import java.awt.*;
import java.util.*;

public class Maze {

    private int width;
    private int height;
    private Picture picture;

    private boolean[][] mazeArr;
    private HashMap<String, Point> map;

    private Point enter;
    private Point exit;

    private double pathNum;

    public Maze(Picture picture){

        this.picture = picture;
        width = picture.width();
        height = picture.height();
        mazeArr = new boolean[height][width];

        CreateMaze();

    }

    public void CreateMaze() {

        enter = new Point();
        exit = new Point();

        enter.setY(0);
        exit.setY(height - 1);

        pathNum = 0.0;

        for (int i = 0; i < picture.width(); i++) {
            if (picture.get(i, 0).equals(Color.WHITE)) {
                enter.setX(i);
            }
            if (picture.get(i, height - 1).equals(Color.WHITE)) {
                exit.setX(i);
            }
        }

        for (int r = 0; r < picture.height(); r++) {

            for (int c = 0; c < picture.width(); c++) {

                mazeArr[r][c] = picture.get(c, r).equals(Color.WHITE);

            }

        }

        map = new HashMap<>();

        map.put(enter.y + " " + enter.x, enter);
        map.put(exit.y +" "+ exit.x, exit);

        for (int i = 1; i < mazeArr.length - 1; i++) {

            String left = "";
            for (int j = 1; j < mazeArr[i].length - 1; j++) {
                if(mazeArr[i][j]){
                    pathNum++;
                    //Check if square is a corner
                    if(!mazeArr[i][j-1] && !mazeArr[i - 1][j] && countAdj(j, i) >= 2){
                        map.put(i + " " + j, new Point(j,i));
                        left = i + " " + j;
                    }
                    else if(!mazeArr[i][j-1] && !mazeArr[i + 1][j]  && countAdj(j, i) >= 2){
                        map.put(i + " " + j, new Point(j,i));
                        left = i + " " + j;

                        if(mazeArr[i-1][j]){
                            int k = 1;
                            while (i-k >= 0 && mazeArr[i-k][j]){
                                if(map.containsKey((i - k) + " " + j)){
                                    map.get(i + " " + j).up = map.get((i-k) + " " + j);
                                    map.get((i-k) + " " + j).down = map.get(i + " " + j);
                                    break;
                                }
                                k++;
                            }
                        }
                    }
                    else if(!mazeArr[i][j+1] && !mazeArr[i - 1][j] && countAdj(j, i) >= 2){
                        map.put(i + " " + j, new Point(j,i));
                        if(left.equals("") || !mazeArr[i][j-1]) {
                            map.put(i + " " + j, new Point(j, i));
                            left = i + " " + j;
                        }
                        else {
                            map.put(i + " " + j, new Point(j, i));
                            map.get(i + " " + j).left = map.get(left);
                            map.get(left).right = map.get(i + " " + j);
                            left = i + " " + j;
                        }
                    }
                            //Check if square is a junction
                    else if(countAdj(j,i) >= 3 || !mazeArr[i][j+1] && !mazeArr[i + 1][j] && countAdj(j, i) >= 2){

                        //If no left has been declared or Has wall on the left
                        if(left.equals("") || !mazeArr[i][j-1]) {
                            map.put(i + " " + j, new Point(j, i));
                            left = i + " " + j;

                            //if the block above has is a path
                            if(mazeArr[i-1][j]){
                                int k = 1;
                                while (i-k >= 0 && mazeArr[i-k][j]){
                                    if(map.containsKey((i - k) + " " + j)){
                                        map.get(i + " " + j).up = map.get((i-k) + " " + j);
                                        map.get((i-k) + " " + j).down = map.get(i + " " + j);
                                        break;
                                    }
                                    k++;
                                }
                            }
                        }
                        else {

                            map.put(i + " " + j, new Point(j, i));
                            map.get(i + " " + j).left = map.get(left);
                            map.get(left).right = map.get(i + " " + j);
                            left = i + " " + j;

                            //if the block above has is a path
                            if(mazeArr[i-1][j]){
                                int k = 1;
                                while (i-k >= 0 && mazeArr[i-k][j]){
                                    if(map.containsKey((i - k) + " " + j)){
                                        map.get(i + " " + j).up = map.get((i-k) + " " + j);
                                        map.get((i-k) + " " + j).down = map.get(i + " " + j);
                                        break;
                                    }
                                    k++;
                                }
                            }

                        }
                    }
                }
                else left = "";


            }

        }

        int k = 1;
        while (exit.y-k >= 0 && mazeArr[exit.y-k][exit.x]){
            if(map.containsKey((exit.y - k) + " " + exit.x)){
                map.get(exit.y + " " + exit.x).up = map.get((exit.y - k) + " " + exit.x);
                map.get((exit.y - k) + " " + exit.x).down = map.get(exit.y + " " + exit.x);
            }
            k++;
        }

        System.out.printf("Number of nodes created: %d \n", map.size());

//        seen = new HashSet<>();

    }

    public int countAdj(int x, int y){
        int num = 0;
        if(mazeArr[y - 1][x]) num++;
        if(mazeArr[y + 1][x]) num++;
        if(mazeArr[y][x-1]) num++;
        if(mazeArr[y][x+1])num++;

        return num;
    }

    public Picture check(){

        Picture picture = new Picture(this.picture.width(), this.picture.height());

        for (int r = 0; r < picture.height(); r++) {

            for (int c = 0; c < picture.width(); c++) {

                if(mazeArr[r][c]){
                    if(map.containsKey(r + " " + c)){
                        picture.set(c,r, Color.RED);
                    }
                    else picture.set(c,r, Color.WHITE);
                }
                else picture.set(c,r, Color.BLACK);

            }

        }

        return picture;

    }

    public LinkedList<Point> DFS(){

        HashSet<Point> seen = new HashSet<>();
        LinkedList<Point> stack = new LinkedList<>();

        Point currentPoint = enter;

        while (currentPoint != exit){
            seen.add(currentPoint);
            if(currentPoint.hasLeft() && !seen.contains(currentPoint.left)){
                stack.push(currentPoint);
                currentPoint = currentPoint.left;
            }
            else if(currentPoint.hasRight() && !seen.contains(currentPoint.right)){
                stack.push(currentPoint);
                currentPoint = currentPoint.right;
            }
            else if(currentPoint.hasDown() && !seen.contains(currentPoint.down)){
                stack.push(currentPoint);
                currentPoint = currentPoint.down;
            }
            else if(currentPoint.hasUp() && !seen.contains(currentPoint.up)){
                stack.push(currentPoint);
                currentPoint = currentPoint.up;
            }
            else{
                currentPoint = stack.pop();
            }
        }

        stack.push(exit);

        System.out.println("Using a depth first search to find a path");

        return stack;

    }

    public Picture generatePicture(LinkedList<Point> list){

        LinkedList<Point> nodes = new LinkedList<>();
        Point previous = list.removeFirst();

        System.out.printf("Number of nodes in path: %d \n", list.size());

        while (list.size() != 0){
            Point current = list.removeFirst();
            nodes.push(previous);
            if(current.x == previous.x){
                if(current.y > previous.y){
                    for (int i = previous.y+1; i < current.y ; i++) {
                        nodes.push(new Point(current.x, i));
                    }
                }
                else{
                    for (int i = previous.y-1; i > current.y ; i--) {
                        nodes.push(new Point(current.x, i));
                    }
                }
            }
            else{
                if(current.x > previous.x){
                    for (int i = previous.x+1; i < current.x ; i++) {
                        nodes.push(new Point(i, current.y));
                    }
                }
                else{
                    for (int i = previous.x-1; i > current.x ; i--) {
                        nodes.push(new Point(i,current.y));
                    }
                }
            }

            previous = current;
        }

        nodes.push(previous);

        System.out.printf("Number of real connections in path: %d \n", nodes.size());

        Picture path = new Picture(picture.width(), picture.height());
        HashSet<String> p = new HashSet<>();
        Color currentColor;
        double increment = .5 / nodes.size();
        double hue = 0;
        while (nodes.size() != 0){
            currentColor = new Color(Color.HSBtoRGB((float) hue, 1, 1));
            Point point = nodes.pop();
            p.add(point.toString());
            path.set(point.x, point.y, currentColor);
            hue += increment;

        }

        for (int r = 0; r < path.height(); r++) {

            for (int c = 0; c < path.width(); c++) {
                if(!mazeArr[r][c]){
                    path.set(c,r, Color.BLACK);
                }
                else if(!p.contains(r +" "+c)) path.set(c,r,Color.WHITE);
            }

        }

        return path;

    }

    public LinkedList<Point> BFS(){
        HashSet<Point> seen = new HashSet<>();
        LinkedList<LinkedList<Point>> queue = new LinkedList<>();

        queue.addLast(new LinkedList<>());
        Objects.requireNonNull(queue.getLast()).add(enter);

        while (!queue.isEmpty()){
            LinkedList<Point> path = queue.removeFirst();
            Point point = path.getLast();

            if(point == exit){
                return path;
            }
            else if(!seen.contains(point)){
                for (Point p : point.getAdj()) {
                    LinkedList<Point> newPath = new LinkedList<>(path);
                    newPath.addLast(p);
                    queue.addLast(newPath);

                    if (p == exit){
                        return newPath;
                    }
                }

                seen.add(point);
            }
        }


        System.out.println("Using a depth first search to find a path");
        return null;
    }

    public LinkedList<Point> AStar(){
        HashSet<Point> seen = new HashSet<>();
        PriorityQueue<Path> priorityQueue = new PriorityQueue<>();

        priorityQueue.add(new Path());
        priorityQueue.peek().path.addLast(enter);

        while (!priorityQueue.isEmpty()){
            Path path = priorityQueue.poll();
            Point point = path.path.getLast();

            if(point == exit){
                return path.path;
            }

            else if(!seen.contains(point)){
                for (Point p : point.getAdj()) {
                    Path newPath = new Path(path);
                    newPath.path.addLast(p);
                    priorityQueue.add(newPath);

                    if (p == exit){
                        return newPath.path;
                    }
                }

                seen.add(point);
            }
        }

        return null;
    }

    public double loadFactor(){
        return pathNum/(height * width);
    }

    @Override
    public String toString() {
        String string = "";

        for (int r = 0; r < mazeArr.length; r++) {
            string += Arrays.toString(mazeArr[r]) + '\n';
        }

        return string;
    }

    static class Point{
        private int x;
        private int y;

        Point up;
        Point down;
        Point left;
        Point right;

        private double upWeight;
        private double downWeight;
        private double leftWeight;
        private double rightWeight;


        Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        Point(){
            x =0;
            y = 0;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        boolean hasLeft(){
            return left != null;
        }

        boolean hasRight(){
            return right != null;
        }

        boolean hasUp(){
            return up != null;
        }

        boolean hasDown(){
            return down != null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public ArrayList<Point> getAdj(){
            ArrayList<Point> adj = new ArrayList<>();
            if(left != null) adj.add(left);
            if(right != null) adj.add(right);
            if(up != null) adj.add(up);
            if(down != null) adj.add(down);
            return adj;
        }

        @Override
        public String toString() {
            return y + " " + x;
        }

        public double calculateDistance(Point p){
            double changeinX = Math.pow(x - p.x, 2);
            double chaneginY = Math.pow(y - p.y,2);

            return Math.sqrt(chaneginY + changeinX);

        }
    }

    static class Path implements Comparator<Path>, Comparable<Path>{
        LinkedList<Point> path;
        double weight;

        Path(){
            path = new LinkedList<>();
            weight = 0.0;
        }

        Path(Path p){
            path = (LinkedList<Point>) p.path.clone();
            weight = p.weight;
        }

        public void setWeight(Point p, Point goal) {
            double x = Math.pow(p.x - goal.x, 2);
            double y = Math.pow(p.y - goal.y, 2);
            this.weight = Math.sqrt(x + y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path1 = (Path) o;
            return Double.compare(path1.weight, weight) == 0 &&
                    Objects.equals(path, path1.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, weight);
        }

        @Override
        public int compare(Path o1, Path o2) {
            return (int) ((int) o1.weight - o2.weight);
        }

        @Override
        public int compareTo(Path o) {
            return (int) (weight - o.weight);
        }
    }
}
